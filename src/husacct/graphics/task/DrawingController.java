package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.Figure;

public abstract class DrawingController extends DrawingSettingsController {
	private static final double MIN_ZOOMFACTOR = 0.25;
	private static final double MAX_ZOOMFACTOR = 1.75;

	protected static final boolean debugPrint = true;
	protected DrawingLayoutStrategy layoutStrategyOption;

	private final HashMap<String, DrawingState> storedStates = new HashMap<String, DrawingState>();

	private Drawing drawing;
	private DrawingView drawingView;
	private GraphicsFrame graphicsFrame;

	protected ILocaleService localeService;
	protected Logger logger = Logger.getLogger(DrawingController.class);

	private final FigureFactory figureFactory;
	private final FigureConnectorStrategy connectionStrategy;
	private LayoutStrategy layoutStrategy;

	protected ThreadMonitor threadMonitor;
	private final FigureMap figureMap = new FigureMap();

	public DrawingController() {
		super();
		this.layoutStrategyOption = DrawingLayoutStrategy.BASIC_LAYOUT;

		this.figureFactory = new FigureFactory();
		this.connectionStrategy = new FigureConnectorStrategy();

		this.localeService = ServiceProvider.getInstance().getLocaleService();
		this.localeService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				DrawingController.this.refreshFrame();
			}
		});

		this.initializeComponents();
		this.switchLayoutStrategy();
		this.loadDefaultSettings();
	}

	@Override
	public void changeLayoutStrategy(DrawingLayoutStrategy selectedStrategyEnum) {
		this.layoutStrategyOption = selectedStrategyEnum;
		this.switchLayoutStrategy();
		this.updateLayout();
	}

	public void clearDrawing() {
		this.figureMap.clearAll();
		this.drawing.clearAll();

		this.drawingView.clearSelection();
		this.drawingView.invalidate();
	}

	public void clearLines() {
		this.drawing.clearAllLines();
	}

	public void drawArchitecture(DrawingDetail detail) {
		this.drawingView.cannotZoomOut();
	}

	public void drawDependenciesBetween(DependencyDTO[] dependencies,
			BaseFigure figureFrom, BaseFigure figureTo) {
		RelationFigure dependencyFigure = null;
		try {
			dependencyFigure = this.figureFactory.createFigure(dependencies);
		} catch (Exception e) {
			this.logger.error("Could not create a dependency figure.", e);
		}
		if (null != dependencyFigure) {
			this.figureMap.linkDependencies(dependencyFigure, dependencies);
			this.connectionStrategy.connect(dependencyFigure, figureFrom,
					figureTo);
			this.drawing.add(dependencyFigure);
		}
	}

	public void drawDependenciesForShownModules() {
		BaseFigure[] shownModules = this.drawing.getShownModules();
		for (BaseFigure figureFrom : shownModules) {
			for (BaseFigure figureTo : shownModules) {
				this.getAndDrawDependenciesBetween(figureFrom, figureTo);
			}
		}
	}

	@Override
	public void drawingZoomChanged(double zoomFactor) {
		zoomFactor = Math.max(MIN_ZOOMFACTOR, zoomFactor);
		zoomFactor = Math.min(MAX_ZOOMFACTOR, zoomFactor);
		this.drawingView.setScaleFactor(zoomFactor);
	}

	public void drawLinesBasedOnSetting() {
		if (this.areDependenciesShown()) {
			this.drawDependenciesForShownModules();
		}
		if (this.areViolationsShown()) {
			this.drawViolationsForShownModules();
		}
		if (this.areSmartLinesOn()) {
			this.drawing.updateLineFigureToContext();
		}
	}

	protected void drawLinesBasedOnSettingInTask() {
		this.clearLines();
		this.setDrawingViewNonVisible();
		this.runDrawLinesTask();
	}

	protected void drawModulesAndLines(AbstractDTO[] modules) {
		this.runDrawSingleLevelTask(modules);
	}

	protected void drawModulesAndLines(
			HashMap<String, ArrayList<AbstractDTO>> modules) {
		this.runDrawMultiLevelTask(modules);
	}

	public void drawMultiLevel(HashMap<String, ArrayList<AbstractDTO>> modules) {
		this.clearDrawing();
		this.drawMultiLevelModules(modules);
		this.updateLayout();
		this.drawLinesBasedOnSetting();
		this.graphicsFrame.setCurrentPaths(this.getCurrentPaths());
		this.graphicsFrame.updateGUI();
	}

	public void drawMultiLevelModules(
			HashMap<String, ArrayList<AbstractDTO>> modules) {
		this.graphicsFrame.setUpToDate();
		for (String parentName : modules.keySet()) {
			ParentFigure parentFigure = null;
			if (!parentName.isEmpty()) {
				parentFigure = this.figureFactory
						.createParentFigure(parentName);
				this.drawing.add(parentFigure);
			}
			for (AbstractDTO dto : modules.get(parentName)) {
				try {
					BaseFigure generatedFigure = this.figureFactory
							.createFigure(dto);

					if (parentFigure != null) {
						parentFigure.add(generatedFigure);
					}

					this.drawing.add(generatedFigure);
					this.figureMap.linkModule(generatedFigure, dto);
				} catch (Exception e) {
					this.logger.error("Could not generate and display figure.",
							e);
				}
			}
			if (!parentName.isEmpty()) {
				parentFigure.updateLayout();
			}
		}
	}

	public void drawSingleLevel(AbstractDTO[] modules) {
		this.graphicsFrame.setUpToDate();
		this.drawSingleLevelModules(modules);
		this.updateLayout();
		this.drawLinesBasedOnSetting();
		this.graphicsFrame.setCurrentPaths(this.getCurrentPaths());
		this.graphicsFrame.updateGUI();
	}

	public void drawSingleLevelModules(AbstractDTO[] modules) {
		for (AbstractDTO dto : modules) {
			try {
				BaseFigure generatedFigure = this.figureFactory
						.createFigure(dto);
				this.drawing.add(generatedFigure);
				this.figureMap.linkModule(generatedFigure, dto);
			} catch (Exception e) {
				this.logger.error("Could not generate and display figure.", e);
			}
		}
	}

	public void drawViolationsBetween(ViolationDTO[] violations,
			BaseFigure figureFrom, BaseFigure figureTo) {
		try {
			RelationFigure violationFigure = this.figureFactory
					.createFigure(violations);
			this.figureMap.linkViolations(violationFigure, violations);
			this.connectionStrategy.connect(violationFigure, figureFrom,
					figureTo);
			this.drawing.add(violationFigure);
		} catch (Exception e) {
			this.logger.error(
					"Could not create a violation line between figures.", e);
		}
	}

	public void drawViolationsForShownModules() {
		BaseFigure[] shownModules = this.drawing.getShownModules();
		for (BaseFigure figureFrom : shownModules) {
			for (BaseFigure figureTo : shownModules) {
				if (figureFrom == figureTo) {
					this.getAndDrawViolationsIn(figureFrom);
				} else {
					this.getAndDrawViolationsBetween(figureFrom, figureTo);
				}
			}
		}
	}

	public void drawViolationsIn(ViolationDTO[] violations,
			BaseFigure figureFrom) {
		try {
			figureFrom.addDecorator(this.figureFactory
					.createViolationsDecorator(violations));
		} catch (Exception e) {
			this.logger
					.error("Could not attach decorator to figure to indicate internal violations.",
							e);
		}
		this.figureMap.linkViolatedModule(figureFrom, violations);
	}

	@Override
	public void exportToImage() {
		this.drawing.showExportToImagePanel();
	}

	@Override
	public void figureDeselected(BaseFigure[] figures) {
		if (this.drawingView.getSelectionCount() == 0) {
			this.graphicsFrame.hideProperties();
		}
	}

	@Override
	public void figureSelected(BaseFigure[] figures) {
		BaseFigure selectedFigure = figures[0];
		if (this.figureMap.isViolatedFigure(selectedFigure)) {
			this.graphicsFrame.showViolationsProperties(this.figureMap
					.getViolatedDTOs(selectedFigure));
		} else if (this.figureMap.isViolationLine(selectedFigure)) {
			this.graphicsFrame.showViolationsProperties(this.figureMap
					.getViolationDTOs(selectedFigure));
		} else if (this.figureMap.isDependencyLine(selectedFigure)) {
			this.graphicsFrame.showDependenciesProperties(this.figureMap
					.getDependencyDTOs(selectedFigure));
		} else {
			this.graphicsFrame.hideProperties();
		}
	}

	private void getAndDrawDependenciesBetween(BaseFigure figureFrom,
			BaseFigure figureTo) {
		DependencyDTO[] dependencies = this.getDependenciesBetween(figureFrom,
				figureTo);
		if (dependencies.length > 0) {
			this.drawDependenciesBetween(dependencies, figureFrom, figureTo);
		}
	}

	private void getAndDrawViolationsBetween(BaseFigure figureFrom,
			BaseFigure figureTo) {
		ViolationDTO[] violations = this.getViolationsBetween(figureFrom,
				figureTo);
		if (violations.length > 0) {
			this.drawViolationsBetween(violations, figureFrom, figureTo);
		}
	}

	private void getAndDrawViolationsIn(BaseFigure figureFrom) {
		ViolationDTO[] violations = this.getViolationsBetween(figureFrom,
				figureFrom);
		if (violations.length > 0) {
			this.drawViolationsIn(violations, figureFrom);
		}
	}

	protected abstract DependencyDTO[] getDependenciesBetween(
			BaseFigure figureFrom, BaseFigure figureTo);

	public Drawing getDrawing() {
		return this.drawing;
	}

	public FigureMap getFigureMap() {
		return this.figureMap;
	}

	public JInternalFrame getGUI() {
		return this.graphicsFrame;
	}

	public DrawingLayoutStrategy getLayoutStrategy() {
		return this.layoutStrategyOption;
	}

	protected abstract ViolationDTO[] getViolationsBetween(
			BaseFigure figureFrom, BaseFigure figureTo);

	protected boolean hasSavedFigureStates(String paths) {
		return this.storedStates.containsKey(paths);
	}

	@Override
	public void hideDependencies() {
		super.hideDependencies();
		this.graphicsFrame.turnOffDependencies();
	}

	@Override
	public void hideModules() {
		this.drawingView.hideSelectedFigures();
	}

	@Override
	public void hideSmartLines() {
		super.hideSmartLines();
		this.graphicsFrame.turnOffSmartLines();
	}

	@Override
	public void hideViolations() {
		super.hideViolations();
		this.graphicsFrame.turnOffViolations();
		this.drawing.setFiguresNotViolated(this.figureMap.getViolatedFigures());
	}

	private void initializeComponents() {
		this.drawing = new Drawing();
		this.drawing.setFigureMap(this.figureMap);
		this.drawingView = new DrawingView(this.drawing);
		this.drawingView.addListener(this);

		this.graphicsFrame = new GraphicsFrame(this.drawingView);
		this.graphicsFrame.addListener(this);
		this.graphicsFrame.setSelectedLayout(this.layoutStrategyOption);

		this.threadMonitor = new ThreadMonitor(this);
	}

	public boolean isDrawingVisible() {
		return this.drawingView.isVisible();
	}

	@Override
	public void moduleZoom() {
		Set<Figure> selection = this.drawingView.getSelectedFigures();

		if (selection.size() > 0) {
			ArrayList<BaseFigure> figures = new ArrayList<BaseFigure>();
			java.util.Collections.addAll(figures,
					selection.toArray(new BaseFigure[selection.size()]));

			for (BaseFigure f : figures) {
				f.isContext(false); // minimising potential side effects
			}

			this.drawingView.selectAll();
			List<BaseFigure> allFigures = Arrays.asList(this.drawingView
					.getSelectedFigures().toArray(new BaseFigure[0]));
			this.drawingView.clearSelection();
			this.drawingView.addToSelection(selection);

			for (BaseFigure f : allFigures) {
				if (!f.isContext() && f.isModule() && !figures.contains(f)) {
					f.isContext(true);
					figures.add(f);
				}
			}

			BaseFigure[] selectedFigures = figures
					.toArray(new BaseFigure[figures.size()]);

			this.moduleZoom(selectedFigures);
		}
	}

	protected void printFigures(String msg) {
		if (!debugPrint) {
			return;
		}

		System.out.println(msg);

		for (Figure f : this.drawing.getChildren()) {
			BaseFigure bf = (BaseFigure) f;
			Rectangle2D.Double bounds = bf.getBounds();

			String rect = String.format(Locale.US,
					"[x=%1.2f,y=%1.2f,w=%1.2f,h=%1.2f]", bounds.x, bounds.y,
					bounds.width, bounds.height);
			if (bf.getName().equals("Main")) {
				System.out.println(String.format("%s: %s", bf.getName(), rect));
			}
		}
	}

	@Override
	public abstract void refreshDrawing();

	public void refreshFrame() {
		this.graphicsFrame.refreshFrame();
	}

	protected void resetAllFigurePositions() {
		this.storedStates.clear();
	}

	protected void restoreFigurePositions(String paths) {
		if (this.storedStates.containsKey(paths)) {
			DrawingState state = this.storedStates.get(paths);
			state.restore(this.figureMap);
			this.drawingView.setHasHiddenFigures(state.hasHiddenFigures());
		}
	}

	@Override
	public void restoreModules() {
		this.drawingView.restoreHiddenFigures();
	}

	private void runDrawLinesTask() {
		this.runThread(new DrawingLinesThread(this));
	}

	private void runDrawMultiLevelTask(
			HashMap<String, ArrayList<AbstractDTO>> modules) {
		this.runThread(new DrawingMultiLevelThread(this, modules));
	}

	private void runDrawSingleLevelTask(AbstractDTO[] modules) {
		this.runThread(new DrawingSingleLevelThread(this, modules));
	}

	private void runThread(Runnable runnable) {
		if (!this.threadMonitor.add(runnable)) {
			this.logger
					.warn("A drawing thread is already running. Wait until it has finished before running another.");
			this.graphicsFrame.setOutOfDate();
		}
	}

	protected void saveFigurePositions() {
		String paths = this.getCurrentPathsToString();
		DrawingState state;
		if (this.storedStates.containsKey(paths)) {
			state = this.storedStates.get(paths);
		} else {
			state = new DrawingState(this.drawing);
		}

		state.save(this.figureMap);
		this.storedStates.put(paths, state);
	}

	public void saveSingleLevelFigurePositions() {
		if (this.getCurrentPaths().length < 2) {
			this.saveFigurePositions();
		}
	}

	@Override
	public void setCurrentPaths(String[] paths) {
		super.setCurrentPaths(paths);
		if (!this.getCurrentPaths()[0].isEmpty()) {
			this.drawingView.canZoomOut();
		} else {
			this.drawingView.cannotZoomOut();
		}
	}

	public void setDrawingViewNonVisible() {
		this.drawingView.setVisible(false);
		this.graphicsFrame.showLoadingScreen();
	}

	public void setDrawingViewVisible() {
		this.graphicsFrame.hideLoadingScreen();
		this.drawingView.setVisible(true);
	}

	@Override
	public void setZoomSlider(double zoomFactor) {
		this.graphicsFrame.setZoomSlider(zoomFactor);
	}

	@Override
	public void showDependencies() {
		super.showDependencies();
		this.graphicsFrame.turnOnDependencies();
	}

	@Override
	public void showSmartLines() {
		super.showSmartLines();
		this.graphicsFrame.turnOnSmartLines();
	}

	@Override
	public void showViolations() {
		super.showViolations();
		this.graphicsFrame.turnOnViolations();
	}

	private void switchLayoutStrategy() {
		switch (this.layoutStrategyOption) {
		case BASIC_LAYOUT:
			this.layoutStrategy = new BasicLayoutStrategy(this.drawing);
			break;
		case LAYERED_LAYOUT:
			this.layoutStrategy = new LayeredLayoutStrategy(this.drawing);
			break;
		default:
			this.layoutStrategy = new NoLayoutStrategy();
			break;
		}
	}

	protected void updateLayout() {
		String currentPaths = this.getCurrentPathsToString();

		if (this.hasSavedFigureStates(currentPaths)) {
			this.restoreFigurePositions(currentPaths);
		} else {
			this.layoutStrategy.doLayout();
			this.drawingView.setHasHiddenFigures(false);
		}

		this.drawing.updateLines();
	}
}
