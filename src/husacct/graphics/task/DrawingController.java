package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.GraphicsFrame;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.FigureFactory;
import husacct.graphics.presentation.figures.ParentFigure;
import husacct.graphics.presentation.figures.RelationFigure;
import husacct.graphics.presentation.menubars.ContextMenu;
import husacct.graphics.task.layout.BasicLayoutStrategy;
import husacct.graphics.task.layout.DrawingState;
import husacct.graphics.task.layout.FigureConnectorStrategy;
import husacct.graphics.task.layout.LayeredLayoutStrategy;
import husacct.graphics.task.layout.LayoutStrategy;
import husacct.graphics.task.layout.NoLayoutStrategy;
import husacct.graphics.util.DrawingDetail;
import husacct.graphics.util.DrawingLayoutStrategy;
import husacct.graphics.util.UserInputListener;
import husacct.graphics.util.threads.DrawingLinesThread;
import husacct.graphics.util.threads.DrawingMultiLevelThread;
import husacct.graphics.util.threads.DrawingSingleLevelThread;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;

public abstract class DrawingController implements UserInputListener {
	protected static final boolean debugPrint = true;
	protected boolean areSmartLinesOn;
	private boolean areDependenciesShown;
	private boolean areViolationsShown;
	protected DrawingLayoutStrategy layoutStrategyOption;

	private HashMap<String, DrawingState> storedStates = new HashMap<String, DrawingState>();
	private ArrayList<BaseFigure> savedFiguresForZoom;

	protected Drawing drawing;
	protected DrawingView view;
	protected GraphicsFrame drawTarget;
	protected ContextMenu contextMenu;
	protected String[] currentPaths = new String[] {};

	protected IControlService controlService;
	protected Logger logger = Logger.getLogger(DrawingController.class);

	protected FigureFactory figureFactory;
	protected FigureConnectorStrategy connectionStrategy;
	protected LayoutStrategy layoutStrategy;

	protected FigureMap figureMap = new FigureMap();

	public DrawingController() {
		savedFiguresForZoom = new ArrayList<BaseFigure>();
		layoutStrategyOption = DrawingLayoutStrategy.BASIC_LAYOUT;

		figureFactory = new FigureFactory();
		connectionStrategy = new FigureConnectorStrategy();

		controlService = ServiceProvider.getInstance().getControlService();
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				refreshFrame();
			}
		});

		initializeComponents();
		switchLayoutStrategy();
	}

	private void initializeComponents() {
		notifyServiceListeners();
		drawing = new Drawing();
		view = new DrawingView(drawing);
		view.addListener(this);

		drawTarget = new GraphicsFrame(view);
		drawTarget.addListener(this);
		drawTarget.setSelectedLayout(layoutStrategyOption);

		contextMenu = new ContextMenu(controlService);
		contextMenu.addListener(this);
		view.setContextMenu(contextMenu);

		showDependencies();
		hideViolations();
		deactivateSmartLines();
	}

	private void switchLayoutStrategy() {
		switch (layoutStrategyOption) {
		case BASIC_LAYOUT:
			layoutStrategy = new BasicLayoutStrategy(drawing);
			break;
		case LAYERED_LAYOUT:
			layoutStrategy = new LayeredLayoutStrategy(drawing);
			break;
		default:
			layoutStrategy = new NoLayoutStrategy();
			break;
		}
	}
	
	public DrawingLayoutStrategy getLayoutStrategy(){
		return layoutStrategyOption;
	}
	
	public void changeLayoutStrategy(DrawingLayoutStrategy selectedStrategyEnum){
		layoutStrategyOption = selectedStrategyEnum;
		switchLayoutStrategy();
		updateLayout();
	}

	@Override
	public void toggleDependencies() {
		notifyServiceListeners();
		if(areDependenciesShown()){
			hideDependencies();
		} else {
			showDependencies();
		}
		drawLinesBasedOnSetting();
	}

	public boolean areDependenciesShown(){
		return areDependenciesShown;
	}
	
	public void showDependencies(){
		areDependenciesShown = true;
		drawTarget.turnOnDependencies();
	}

	public void hideDependencies() {
		areDependenciesShown = false;
		drawTarget.turnOffDependencies();
	}

	@Override
	public void toggleViolations() {
		notifyServiceListeners();
		if (areViolationsShown()) {
			hideViolations();
		} else {
			showViolations();
		}
		drawLinesBasedOnSetting();
	}
	
	public boolean areViolationsShown() {
		return areViolationsShown;
	}

	public void showViolations() {
		areViolationsShown = true;
		drawTarget.turnOnViolations();
	}
	
	public void hideViolations() {
		areViolationsShown = false;
		drawTarget.turnOffViolations();
		drawing.setFiguresNotViolated(figureMap.getViolatedFigures());
	}

	public void toggleSmartLines(){
		notifyServiceListeners();
		if(areSmartLinesOn()){
			deactivateSmartLines();
		} else {
			activateSmartLines();
		}
		drawLinesBasedOnSetting();
	}

	public boolean areSmartLinesOn(){
		return areSmartLinesOn;
	}
	
	public void deactivateSmartLines() {
		areSmartLinesOn = false;
		drawTarget.turnOffSmartLines();
	}

	public void activateSmartLines() {
		areSmartLinesOn = true;
		drawTarget.turnOnSmartLines();
	}

	public JInternalFrame getGUI() {
		return drawTarget;
	}

	public void clearDrawing() {
		figureMap.clearAll();
		drawing.clearAll();

		view.clearSelection();
		view.invalidate();
	}

	public void clearLines() {
		drawing.clearAllLines();
	}

	public String[] getCurrentPaths() {
		return currentPaths;
	}

	public String getCurrentPathsToString() {
		String stringPaths = "";
		for (String path : getCurrentPaths()) {
			stringPaths += path + " + ";
		}
		return stringPaths;
	}

	public void resetCurrentPaths() {
		currentPaths = new String[] {};
	}

	public void setCurrentPaths(String[] paths) {
		currentPaths = paths;
		contextMenu.setCanZoomout((paths.length == 0));
	}

	protected DrawingDetail getCurrentDrawingDetail() {
		DrawingDetail detail = DrawingDetail.WITHOUT_VIOLATIONS;
		if (areViolationsShown()) {
			detail = DrawingDetail.WITH_VIOLATIONS;
		}
		return detail;
	}

	@Override
	public void figureSelected(BaseFigure[] figures) {
		BaseFigure selectedFigure = figures[0];
		if (figureMap.isViolatedFigure(selectedFigure)) {
			drawTarget.showViolationsProperties(figureMap.getViolatedDTOs(selectedFigure));
		} else if (figureMap.isViolationLine(selectedFigure)) {
			drawTarget.showViolationsProperties(figureMap.getViolationDTOs(selectedFigure));
		} else if (figureMap.isDependencyLine(selectedFigure)) {
			drawTarget.showDependenciesProperties(figureMap.getDependencyDTOs(selectedFigure));
		} else {
			drawTarget.hidePropertiesPane();
		}
	}

	@Override
	public void figureDeselected(BaseFigure[] figures) {
		if (view.getSelectionCount() == 0) {
			drawTarget.hidePropertiesPane();
		}
	}

	public abstract void drawArchitecture(DrawingDetail detail);

	protected void drawModulesAndLines(AbstractDTO[] modules) {
		clearDrawing();
		runDrawSingleLevelTask(modules);
	}

	private void runDrawSingleLevelTask(AbstractDTO[] modules) {
		DrawingSingleLevelThread task = new DrawingSingleLevelThread(this, modules);
		Thread drawThread = new Thread(task);
		drawThread.start();
	}

	public void drawSingleLevel(AbstractDTO[] modules) {
		setDrawingViewNonVisible();
		
		drawTarget.setCurrentPaths(getCurrentPaths());
		drawTarget.updateGUI();
		for (AbstractDTO dto : modules) {
			BaseFigure generatedFigure = figureFactory.createFigure(dto);
			drawing.add(generatedFigure);
			figureMap.linkModule(generatedFigure, dto);
		}

		drawLines();

		updateLayout();
		
		setDrawingViewVisible();
	}

	protected void drawModulesAndLines(HashMap<String, ArrayList<AbstractDTO>> modules) {
		clearDrawing();
		runDrawMultiLevelTask(modules);
	}
	
	private void runDrawMultiLevelTask(HashMap<String, ArrayList<AbstractDTO>> modules) {
		DrawingMultiLevelThread task = new DrawingMultiLevelThread(this, modules);
		Thread drawThread = new Thread(task);
		drawThread.start();
	}

	public void drawMultiLevel(HashMap<String, ArrayList<AbstractDTO>> modules) {
		setDrawingViewNonVisible();
		
		HashMap<BaseFigure, AbstractDTO> savedFiguresToBeDrawn = getSavedFiguresForZoom();
		clearSavedFiguresForZoom();
		clearDrawing();
		for (String parentName : modules.keySet()) {
			ParentFigure parentFigure = null;
			if(!parentName.isEmpty()){
				parentFigure = figureFactory.createParentFigure(parentName);
				drawing.add(parentFigure);
			}
			for (AbstractDTO dto : modules.get(parentName)) {
				BaseFigure generatedFigure = figureFactory.createFigure(dto);
				if(!parentName.isEmpty()){
					parentFigure.add(generatedFigure);
				}
				drawing.add(generatedFigure);
				figureMap.linkModule(generatedFigure, dto);
			}
			if(!parentName.isEmpty()){
				parentFigure.updateLayout();
			}
		}
		for(BaseFigure figure : savedFiguresToBeDrawn.keySet()){
			drawing.add(figure);
			figureMap.linkModule(figure, savedFiguresToBeDrawn.get(figure));
		}
		drawTarget.setCurrentPaths(getCurrentPaths());
		drawTarget.updateGUI();
		updateLayout();
		drawLines();
		
		setDrawingViewVisible();
	}

	protected void updateLayout() {
		String currentPaths = getCurrentPathsToString();

		if (hasSavedFigureStates(currentPaths)) {
			restoreFigurePositions(currentPaths);
		} else {
			layoutStrategy.doLayout();
			contextMenu.setHasHiddenFigures(false);
			contextMenu.setHasSelection(false);			
		}

		updateLines();
	}

	private void updateLines() {
		for (Figure f : drawing.getChildren()) {
			BaseFigure bf = (BaseFigure) f;
			if (bf.isLine()) {
				RelationFigure cf = (RelationFigure) f;
				cf.updateConnection();
			}
		}
	}
	
	protected void clearSavedFiguresForZoom(){
		savedFiguresForZoom.clear();
	}
	
	protected boolean areFiguresSavedForZoom(){
		return savedFiguresForZoom.size() > 0;
	}
	
	protected void addSavedFiguresForZoom(BaseFigure savedFigure){
		savedFiguresForZoom.add(savedFigure);
	}
	
	protected HashMap<BaseFigure, AbstractDTO> getSavedFiguresForZoom(){
		return figureMap.getAllDTOsWithClonedFigures(savedFiguresForZoom);
	}

	protected void drawLinesBasedOnSetting() {
		clearLines();
		runDrawLinesTask();
	}

	private void runDrawLinesTask(){
		DrawingLinesThread task = new DrawingLinesThread(this);
		Thread drawThread = new Thread(task);
		drawThread.start();
	}
	
	public void drawLines(){
		if(areDependenciesShown()){
			drawDependenciesForShownModules();
		}
		if (areViolationsShown()) {
			drawViolationsForShownModules();
		}
		if(areSmartLinesOn()){
			drawing.updateLineFigureToContext();
		}
	}

	public void drawDependenciesForShownModules() {
		BaseFigure[] shownModules = drawing.getShownModules();
		for (BaseFigure figureFrom : shownModules) {
			for (BaseFigure figureTo : shownModules) {
				getAndDrawDependenciesBetween(figureFrom, figureTo);
			}
		}
	}

	private void getAndDrawDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		DependencyDTO[] dependencies = (DependencyDTO[]) getDependenciesBetween(figureFrom, figureTo);
		if (dependencies.length > 0) {
			RelationFigure dependencyFigure = figureFactory.createFigure(dependencies);
			figureMap.linkDependencies(dependencyFigure, dependencies);
			connectionStrategy.connect(dependencyFigure, figureFrom, figureTo);
			drawing.add(dependencyFigure);
		}
	}

	protected abstract DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo);

	public void drawViolationsForShownModules() {
		BaseFigure[] shownModules = drawing.getShownModules();
		for (BaseFigure figureFrom : shownModules) {
			for (BaseFigure figureTo : shownModules) {
				if (figureFrom == figureTo) {
					getAndDrawViolationsIn(figureFrom);
				} else {
					getAndDrawViolationsBetween(figureFrom, figureTo);
				}
			}
		}
	}

	private void getAndDrawViolationsIn(BaseFigure figureFrom) {
		ViolationDTO[] violations = getViolationsBetween(figureFrom, figureFrom);
		if (violations.length > 0) {
			figureFrom.addDecorator(figureFactory.createViolationsDecorator(violations));
			figureMap.linkViolatedModule(figureFrom, violations);
		}
	}

	private void getAndDrawViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		ViolationDTO[] violations = getViolationsBetween(figureFrom, figureTo);
		if (violations.length > 0) {
			RelationFigure violationFigure = figureFactory.createFigure(violations);
			figureMap.linkViolations(violationFigure, violations);
			connectionStrategy.connect(violationFigure, figureFrom, figureTo);
			drawing.add(violationFigure);
		}
	}

	protected abstract ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo);

	public abstract void refreshDrawing();

	public void refreshFrame() {
		drawTarget.refreshFrame();
	}

	public void refreshFrameClean() {
		drawTarget.refreshFrameClean();
	}

	@Override
	public void exportToImage() {
		drawing.showExportToImagePanel();
	}

	public void notifyServiceListeners() {
		ServiceProvider.getInstance().getGraphicsService().notifyServiceListeners();
	}

	public void saveSingleLevelFigurePositions() {
		if (getCurrentPaths().length < 2) {
			saveFigurePositions();
		}
	}

	protected void saveFigurePositions() {
		String paths = getCurrentPathsToString();
		DrawingState state;
		if (storedStates.containsKey(paths))
			state = storedStates.get(paths);
		else
			state = new DrawingState(drawing);

		state.save(figureMap);
		storedStates.put(paths, state);
	}

	protected boolean hasSavedFigureStates(String paths) {
		return storedStates.containsKey(paths);
	}

	protected void restoreFigurePositions(String paths) {
		if (storedStates.containsKey(paths)) {
			DrawingState state = storedStates.get(paths);
			state.restore(figureMap);

			contextMenu.setHasHiddenFigures(state.hasHiddenFigures());
			contextMenu.setHasSelection(false);
		}
	}

	protected void resetAllFigurePositions() {
		storedStates.clear();
	}

	protected void printFigures(String msg) {
		if (!debugPrint)
			return;

		System.out.println(msg);

		for (Figure f : drawing.getChildren()) {
			BaseFigure bf = (BaseFigure) f;
			Rectangle2D.Double bounds = bf.getBounds();

			String rect = String.format(Locale.US, "[x=%1.2f,y=%1.2f,w=%1.2f,h=%1.2f]", bounds.x, bounds.y,
					bounds.width, bounds.height);
			if (bf.getName().equals("Main"))
				System.out.println(String.format("%s: %s", bf.getName(), rect));
		}
	}

	@Override
	public void drawingZoomChanged(double zoomFactor) {
		view.setScaleFactor(zoomFactor);
	}

	@Override
	public void hideModules() {
		Set<Figure> selection = view.getSelectedFigures();
		for (Figure f : drawing.getChildren()) {
			BaseFigure bf = (BaseFigure) f;

			if (!bf.isLine()) {
				if (selection.contains(bf)) {
					bf.setEnabled(false);
				}
			} else if (bf.isLine()) {
				ConnectionFigure cf = (ConnectionFigure) f;
				if (selection.contains(cf.getStartFigure()) || selection.contains(cf.getEndFigure())) {
					bf.setEnabled(false);
				}
			}
		}

		contextMenu.setHasHiddenFigures(selection.size() > 0);
		view.clearSelection();
	}

	@Override
	public void restoreModules() {
		List<Figure> selection = drawing.getChildren();
		for (Figure f : selection) {
			BaseFigure bf = (BaseFigure) f;
			bf.setEnabled(true);
		}

		contextMenu.setHasHiddenFigures(false);
	}

	@Override
	public void moduleZoom() {
		Set<Figure> selection = view.getSelectedFigures();
		if (selection.size() > 0) {
			BaseFigure[] selectedFigures = selection.toArray(new BaseFigure[selection.size()]);

			moduleZoom(selectedFigures);
		}
	}
	
	public void setDrawingViewVisible(){
		drawTarget.hideLoadingScreen();
		view.setVisible(true);
	}
	
	public void setDrawingViewNonVisible(){
		view.setVisible(false);
		drawTarget.showLoadingScreen();
	}
}
