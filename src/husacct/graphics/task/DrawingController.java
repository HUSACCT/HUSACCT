package husacct.graphics.task;

import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.GraphicsFrame;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.FigureFactory;
import husacct.graphics.presentation.figures.RelationFigure;
import husacct.validate.IValidateService;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;

public abstract class DrawingController implements UserInputListener {

	public final int ITEMS_PER_ROW = 4;

	protected Drawing drawing;
	protected DrawingView view;
	protected GraphicsFrame drawTarget;
	protected String currentPath = "";
	private boolean isViolationsShown = false;

	protected Logger logger = Logger.getLogger(DrawingController.class);

	protected FigureFactory figureFactory;
	protected FigureConnectorStrategy connectionStrategy;
	protected BasicLayoutStrategy layoutStrategy;

	protected IAnalyseService analyseService;
	protected IValidateService validateService;

	protected FigureMap figureMap = new FigureMap();

	public DrawingController() {
		figureFactory = new FigureFactory();
		connectionStrategy = new FigureConnectorStrategy();
		
		initializeComponents();
	}

	private void initializeComponents() {
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
	}

	public void clearLines() {
		drawing.clearAllLines();
	}

	public String getCurrentPath() {
		return currentPath;
	}

	public void resetCurrentPath() {
		currentPath = "";
	}

	public void setCurrentPath(String path) {
		currentPath = path;
	}

	public boolean areViolationsShown() {
		return isViolationsShown;
	}

	public void hideViolations() {
		isViolationsShown = false;
		drawing.setFiguresNotViolated(figureMap.getViolatedFigures());
	}

	public void showViolations() {
		isViolationsShown = true;
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

	protected void drawModules(AbstractDTO[] modules) {
		clearDrawing();
		for (AbstractDTO dto : modules) {
			BaseFigure generatedFigure = figureFactory.createFigure(dto);
			drawing.add(generatedFigure);
			figureMap.linkModule(generatedFigure, dto);
		}
		drawTarget.setCurrentPathInfo(getCurrentPath());
		layoutStrategy.doLayout(ITEMS_PER_ROW);
	}

	public void toggleViolations() {
		if (areViolationsShown()) {
			hideViolations();
		} else {
			showViolations();
		}
		drawLinesBasedOnSetting();
	}

	@Override
	public void exportToImage() {
		drawing.showExportToImagePanel();
	}

	protected void drawLinesBasedOnSetting() {
		clearLines();
		drawDependenciesForShownModules();
		if (areViolationsShown()) {
			drawViolationsForShownModules();
		}
		drawing.resizeRelationFigures();
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
		validateService.checkConformance();
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
			figureFrom.setViolated(true);
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
}
