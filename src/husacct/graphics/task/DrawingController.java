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
import husacct.graphics.task.layout.BasicLayoutStrategy;
import husacct.graphics.task.layout.DrawingState;
import husacct.graphics.task.layout.LayoutStrategy;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.Figure;

public abstract class DrawingController implements UserInputListener {
	protected static final boolean debugPrint = true;

	private boolean areViolationsShown = false;
	private HashMap<String, DrawingState> storedStates = new HashMap<String, DrawingState>();

	protected Drawing drawing;
	protected DrawingView view;
	protected GraphicsFrame drawTarget;
	protected String[] currentPaths = new String[]{};

	protected IControlService controlService;
	protected Logger logger = Logger.getLogger(DrawingController.class);

	protected FigureFactory figureFactory;
	protected FigureConnectorStrategy connectionStrategy;
	protected LayoutStrategy layoutStrategy;

	protected FigureMap figureMap = new FigureMap();

	public DrawingController() {
		figureFactory = new FigureFactory();
		connectionStrategy = new FigureConnectorStrategy();

		initializeComponents();

		controlService = ServiceProvider.getInstance().getControlService();
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				refreshFrame();
			}
		});
	}

	private void initializeComponents() {
		notifyServiceListeners();
		drawing = new Drawing();
		view = new DrawingView(drawing);
		view.addListener(this);

		drawTarget = new GraphicsFrame(view);
		drawTarget.addListener(this);

		layoutStrategy = new BasicLayoutStrategy(drawing);
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
		for(String path : getCurrentPaths()){
			stringPaths += path + " + ";
		}
		return stringPaths;
	}

	public void resetCurrentPaths() {
		currentPaths = new String[]{};
	}

	public void setCurrentPaths(String[] paths) {
		currentPaths = paths;
	}

	public boolean areViolationsShown() {
		return areViolationsShown;
	}

	public void hideViolations() {
		areViolationsShown = false;
		drawTarget.turnOffViolations();
		drawing.setFiguresNotViolated(figureMap.getViolatedFigures());
	}

	public void showViolations() {
		areViolationsShown = true;
		drawTarget.turnOnViolations();
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
		drawTarget.setCurrentPaths(getCurrentPaths());
		drawTarget.updateGUI();

		for (AbstractDTO dto : modules) {
			BaseFigure generatedFigure = figureFactory.createFigure(dto);
			drawing.add(generatedFigure);
			figureMap.linkModule(generatedFigure, dto);
		}

		drawLinesBasedOnSetting();

		updateLayout();
	}

	protected void drawModulesAndLines(HashMap<String, ArrayList<AbstractDTO>> modules) {
		clearDrawing();
		for (String parentName : modules.keySet()) {
			ParentFigure parentFigure = figureFactory.createParentFigure(parentName);
			parentFigure.willChange();
			parentFigure.changed();
			drawing.add(parentFigure);
			for (AbstractDTO dto : modules.get(parentName)) {
				BaseFigure generatedFigure = figureFactory.createFigure(dto);
				parentFigure.addChildFigure(generatedFigure);
				drawing.add(generatedFigure);
				figureMap.linkModule(generatedFigure, dto);
			}
		}
		drawTarget.setCurrentPaths(getCurrentPaths());
		drawTarget.updateGUI();
		drawLinesBasedOnSetting();
		updateLayout();
		drawLinesBasedOnSetting();
	}

	protected void updateLayout() {
		String currentPaths = getCurrentPathsToString();
		
		if (hasSavedFigureStates(currentPaths)) {
			restoreFigurePositions(currentPaths);
		} else {
			int width = drawTarget.getWidth();
			int height = drawTarget.getHeight();

			layoutStrategy.doLayout(width, height);
		}
		
		updateLines();
		
		// bring modulefigures to the front
		ArrayList<Figure> moduleFigures = new ArrayList<Figure>();
		for (Figure f : drawing.getChildren()) {
			if(((BaseFigure)f).isModule()) {
				moduleFigures.add(f);
			}
		}
		for(Figure f : moduleFigures) {
			drawing.bringToFront(f);
		}
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

	protected void drawLinesBasedOnSetting() {
		clearLines();
		drawDependenciesForShownModules();
		if (areViolationsShown()) {
			drawViolationsForShownModules();
		}
		drawing.updateLineFigureToContext();
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

	@Override
	public void exportToImage() {
		drawing.showExportToImagePanel();
	}

	public void notifyServiceListeners() {
		ServiceProvider.getInstance().getGraphicsService().notifyServiceListeners();
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
		}
	}

	protected void resetFigurePositions(String path) {
		storedStates.remove(path);
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
}
