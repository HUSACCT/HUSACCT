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
import husacct.graphics.presentation.figures.RelationFigure;
import husacct.graphics.task.layout.LayeredLayoutStrategy;
import husacct.graphics.task.layout.LayoutStrategy;

import java.util.Locale;

import javax.swing.JInternalFrame;

import org.apache.log4j.Logger;

public abstract class DrawingController implements UserInputListener {

	protected Drawing drawing;
	protected DrawingView view;
	protected GraphicsFrame drawTarget;
	protected String currentPath = "";
	private boolean isViolationsShown = false;

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
				refreshDrawing();
			}
		});
	}

	private void initializeComponents() {
		drawing = new Drawing();
		view = new DrawingView(drawing);
		view.addListener(this);

		drawTarget = new GraphicsFrame(view);
		drawTarget.addListener(this);

		layoutStrategy = new LayeredLayoutStrategy(drawing);
//		layoutStrategy = new BasicLayoutStrategy(drawing);
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
		drawTarget.turnOffViolations();
		drawing.setFiguresNotViolated(figureMap.getViolatedFigures());
	}

	public void showViolations() {
		isViolationsShown = true;
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
		
		drawTarget.setCurrentPath(getCurrentPath());
		drawTarget.updateGUI();
		
		for (AbstractDTO dto : modules) {
			BaseFigure generatedFigure = figureFactory.createFigure(dto);
			drawing.add(generatedFigure);
			figureMap.linkModule(generatedFigure, dto);
		}
		
		// ATTN: The calls to drawLinesBasedOnSetting(); updateLayout(); drawLinesBasedOnSetting();
		// are done specifically in that order for a reason!
		// Due to a bug in the RelationFigure the lines are drawing themselves incorrectly
		// after updating the layout of the drawing. 
		// To solve this we first draw the entire drawing, update the layout and then 
		// remove all the lines and re-add them to the drawing. 
		// As it's currently unknown what causes the bug or how to solve it and the
		// deadline for Construction II is approaching, we have decided to go with a 
		// work around. However, this bug should be fixed as soon as possible. 
		drawLinesBasedOnSetting();
		
		updateLayout();
		
		drawLinesBasedOnSetting();
	}

	protected void updateLayout() {
		int width = drawTarget.getWidth();
		int height = drawTarget.getHeight();

		layoutStrategy.doLayout(width, height);
		drawing.redraw();
	}

	@Override
	public void toggleViolations() {
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
}
