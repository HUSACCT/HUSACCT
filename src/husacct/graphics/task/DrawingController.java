package husacct.graphics.task;

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
import husacct.graphics.task.layout.FigureConnectorStrategy;
import husacct.graphics.task.layout.LayeredLayoutStrategy;
import husacct.graphics.task.layout.LayoutStrategy;
import husacct.graphics.task.layout.NoLayoutStrategy;
import husacct.graphics.util.DrawingDetail;
import husacct.graphics.util.DrawingLayoutStrategy;
import husacct.graphics.util.FigureMap;
import husacct.graphics.util.threads.DrawingLinesThread;
import husacct.graphics.util.threads.DrawingMultiLevelThread;
import husacct.graphics.util.threads.DrawingSingleLevelThread;
import husacct.graphics.util.threads.ThreadMonitor;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.Figure;

public abstract class DrawingController extends DrawingSettingsController {
	protected static final boolean debugPrint = true;
	protected DrawingLayoutStrategy layoutStrategyOption;

	private HashMap<String, DrawingState> storedStates = new HashMap<String, DrawingState>();

	private Drawing drawing;
	private DrawingView drawingView;
	private GraphicsFrame graphicsFrame;

	protected IControlService controlService;
	protected Logger logger = Logger.getLogger(DrawingController.class);

	private FigureFactory figureFactory;
	private FigureConnectorStrategy connectionStrategy;
	private LayoutStrategy layoutStrategy;

	protected ThreadMonitor threadMonitor;
	private FigureMap figureMap = new FigureMap();

	public DrawingController(IControlService controlService) {
		super();
		layoutStrategyOption = DrawingLayoutStrategy.BASIC_LAYOUT;

		figureFactory = new FigureFactory();
		connectionStrategy = new FigureConnectorStrategy();

		this.controlService = controlService;
		this.controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				refreshFrame();
			}
		});

		initializeComponents();
		switchLayoutStrategy();
		loadDefaultSettings();
	}

	private void initializeComponents() {
		drawing = new Drawing();
		drawingView = new DrawingView(drawing);
		drawingView.addListener(this);

		graphicsFrame = new GraphicsFrame(drawingView);
		graphicsFrame.addListener(this);
		graphicsFrame.setSelectedLayout(layoutStrategyOption);

		threadMonitor = new ThreadMonitor(this);
	}

	private void runThread(Runnable runnable) {
		if (!threadMonitor.add(runnable)) {
			logger.warn("A drawing thread is already running. Wait until it has finished before running another.");
			graphicsFrame.setOutOfDate();
		}
	}

	public FigureMap getFigureMap() {
		return figureMap;
	}

	public Drawing getDrawing() {
		return drawing;
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

	public DrawingLayoutStrategy getLayoutStrategy() {
		return layoutStrategyOption;
	}

	public void changeLayoutStrategy(DrawingLayoutStrategy selectedStrategyEnum) {
		layoutStrategyOption = selectedStrategyEnum;
		switchLayoutStrategy();
		updateLayout();
	}

	@Override
	public void showDependencies() {
		notifyServiceListeners();
		super.showDependencies();
		graphicsFrame.turnOnDependencies();
	}

	@Override
	public void hideDependencies() {
		notifyServiceListeners();
		super.hideDependencies();
		graphicsFrame.turnOffDependencies();
	}

	@Override
	public void showViolations() {
		notifyServiceListeners();
		super.showViolations();
		graphicsFrame.turnOnViolations();
	}

	@Override
	public void hideViolations() {
		notifyServiceListeners();
		super.hideViolations();
		graphicsFrame.turnOffViolations();
		drawing.setFiguresNotViolated(figureMap.getViolatedFigures());
	}

	public void showSmartLines() {
		notifyServiceListeners();
		super.showSmartLines();
		graphicsFrame.turnOnSmartLines();
	}
	
	public void hideSmartLines() {
		notifyServiceListeners();
		super.hideSmartLines();
		graphicsFrame.turnOffSmartLines();
	}

	public JInternalFrame getGUI() {
		return graphicsFrame;
	}

	public void clearDrawing() {
		figureMap.clearAll();
		drawing.clearAll();

		drawingView.clearSelection();
		drawingView.invalidate();
	}

	public void clearLines() {
		drawing.clearAllLines();
	}

	public void setCurrentPaths(String[] paths) {
		super.setCurrentPaths(paths);
		if (!getCurrentPaths()[0].isEmpty()) {
			drawingView.canZoomOut();
		} else {
			drawingView.cannotZoomOut();
		}
	}

	@Override
	public void figureSelected(BaseFigure[] figures) {
		BaseFigure selectedFigure = figures[0];
		if (figureMap.isViolatedFigure(selectedFigure)) {
			graphicsFrame.showViolationsProperties(figureMap.getViolatedDTOs(selectedFigure));
		} else if (figureMap.isViolationLine(selectedFigure)) {
			graphicsFrame.showViolationsProperties(figureMap.getViolationDTOs(selectedFigure));
		} else if (figureMap.isDependencyLine(selectedFigure)) {
			graphicsFrame.showDependenciesProperties(figureMap.getDependencyDTOs(selectedFigure));
		} else {
			graphicsFrame.hidePropertiesPane();
		}
	}

	@Override
	public void figureDeselected(BaseFigure[] figures) {
		if (drawingView.getSelectionCount() == 0) {
			graphicsFrame.hidePropertiesPane();
		}
	}

	public void drawArchitecture(DrawingDetail detail) {
		drawingView.cannotZoomOut();
	}

	protected void drawModulesAndLines(AbstractDTO[] modules) {
		runDrawSingleLevelTask(modules);
	}

	private void runDrawSingleLevelTask(AbstractDTO[] modules) {
		runThread(new DrawingSingleLevelThread(this, modules));
	}

	public void drawSingleLevel(AbstractDTO[] modules) {
		graphicsFrame.setUpToDate();
		drawSingleLevelModules(modules);
		updateLayout();
		drawLinesBasedOnSetting();
		graphicsFrame.setCurrentPaths(getCurrentPaths());
		graphicsFrame.updateGUI();
	}

	public void drawSingleLevelModules(AbstractDTO[] modules) {
		for (AbstractDTO dto : modules) {
			try {
				BaseFigure generatedFigure = figureFactory.createFigure(dto);
				drawing.add(generatedFigure);
				figureMap.linkModule(generatedFigure, dto);
			} catch (Exception e) {
				logger.error("Could not generate and display figure.", e);
			}
		}
	}

	protected void drawModulesAndLines(HashMap<String, ArrayList<AbstractDTO>> modules) {
		runDrawMultiLevelTask(modules);
	}

	private void runDrawMultiLevelTask(HashMap<String, ArrayList<AbstractDTO>> modules) {
		runThread(new DrawingMultiLevelThread(this, modules));
	}

	public void drawMultiLevel(HashMap<String, ArrayList<AbstractDTO>> modules) {
		clearDrawing();
		drawMultiLevelModules(modules);
		updateLayout();
		drawLinesBasedOnSetting();
		graphicsFrame.setCurrentPaths(getCurrentPaths());
		graphicsFrame.updateGUI();
	}

	public void drawMultiLevelModules(HashMap<String, ArrayList<AbstractDTO>> modules) {
		graphicsFrame.setUpToDate();
		for (String parentName : modules.keySet()) {
			ParentFigure parentFigure = null;
			if (!parentName.isEmpty()) {
				parentFigure = figureFactory.createParentFigure(parentName);
				drawing.add(parentFigure);
			}
			for (AbstractDTO dto : modules.get(parentName)) {
				try {
					BaseFigure generatedFigure = figureFactory.createFigure(dto);

					if (parentFigure != null) {
						parentFigure.add(generatedFigure);
					}

					drawing.add(generatedFigure);
					figureMap.linkModule(generatedFigure, dto);
				} catch (Exception e) {
					logger.error("Could not generate and display figure.", e);
				}
			}
			if (!parentName.isEmpty()) {
				parentFigure.updateLayout();
			}
		}
	}

	protected void updateLayout() {
		String currentPaths = getCurrentPathsToString();

		if (hasSavedFigureStates(currentPaths)) {
			restoreFigurePositions(currentPaths);
		} else {
			layoutStrategy.doLayout();
			drawingView.setHasHiddenFigures(false);
		}

		drawing.updateLines();
	}

	protected void drawLinesBasedOnSettingInTask() {
		clearLines();
		setDrawingViewNonVisible();
		runDrawLinesTask();
	}

	private void runDrawLinesTask() {
		runThread(new DrawingLinesThread(this));
	}

	public void drawLinesBasedOnSetting() {
		if (areDependenciesShown()) {
			drawDependenciesForShownModules();
		}
		if (areViolationsShown()) {
			drawViolationsForShownModules();
		}
		if (areSmartLinesOn()) {
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
			drawDependenciesBetween(dependencies, figureFrom, figureTo);
		}
	}

	public void drawDependenciesBetween(DependencyDTO[] dependencies, BaseFigure figureFrom, BaseFigure figureTo) {
		RelationFigure dependencyFigure = null;
		try {
			dependencyFigure = figureFactory.createFigure(dependencies);
		} catch (Exception e) {
			logger.error("Could not create a dependency figure.", e);
		}
		if (null != dependencyFigure) {
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
			drawViolationsIn(violations, figureFrom);
		}
	}

	public void drawViolationsIn(ViolationDTO[] violations, BaseFigure figureFrom) {
		try {
			figureFrom.addDecorator(figureFactory.createViolationsDecorator(violations));
		} catch (Exception e) {
			logger.error("Could not attach decorator to figure to indicate internal violations.", e);
		}
		figureMap.linkViolatedModule(figureFrom, violations);
	}

	private void getAndDrawViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		ViolationDTO[] violations = getViolationsBetween(figureFrom, figureTo);
		if (violations.length > 0) {
			drawViolationsBetween(violations, figureFrom, figureTo);
		}
	}

	public void drawViolationsBetween(ViolationDTO[] violations, BaseFigure figureFrom, BaseFigure figureTo) {
		try {
			RelationFigure violationFigure = figureFactory.createFigure(violations);
			figureMap.linkViolations(violationFigure, violations);
			connectionStrategy.connect(violationFigure, figureFrom, figureTo);
			drawing.add(violationFigure);
		} catch (Exception e) {
			logger.error("Could not create a violation line between figures.", e);
		}
	}

	protected abstract ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo);

	public abstract void refreshDrawing();

	public void refreshFrame() {
		graphicsFrame.refreshFrame();
	}

	@Override
	public void exportToImage() {
		drawing.showExportToImagePanel();
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
			drawingView.setHasHiddenFigures(state.hasHiddenFigures());
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

			String rect = String.format(Locale.US, "[x=%1.2f,y=%1.2f,w=%1.2f,h=%1.2f]", bounds.x, bounds.y, bounds.width, bounds.height);
			if (bf.getName().equals("Main"))
				System.out.println(String.format("%s: %s", bf.getName(), rect));
		}
	}

	@Override
	public void drawingZoomChanged(double zoomFactor) {
		drawingView.setScaleFactor(zoomFactor);
	}

	@Override
	public void hideModules() {
		drawingView.hideSelectedFigures();
	}

	@Override
	public void restoreModules() {
		drawingView.restoreHiddenFigures();
	}

	@Override
	public void moduleZoom() {
		Set<Figure> selection = drawingView.getSelectedFigures();
		if (selection.size() > 0) {
			BaseFigure[] selectedFigures = selection.toArray(new BaseFigure[selection.size()]);

			moduleZoom(selectedFigures);
		}
	}

	public void setDrawingViewVisible() {
		graphicsFrame.hideLoadingScreen();
		drawingView.setVisible(true);
	}

	public void setDrawingViewNonVisible() {
		drawingView.setVisible(false);
		graphicsFrame.showLoadingScreen();
	}

	public boolean isDrawingVisible() {
		return drawingView.isVisible();
	}
}
