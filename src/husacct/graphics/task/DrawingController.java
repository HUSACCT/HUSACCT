package husacct.graphics.task;


import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.Drawing;
import husacct.graphics.presentation.DrawingView;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.FigureFactory;
import husacct.graphics.presentation.figures.ModuleFigure;
import husacct.graphics.presentation.figures.ParentFigure;
import husacct.graphics.presentation.figures.RelationFigure;
import husacct.graphics.task.layout.BasicLayoutStrategy;
import husacct.graphics.task.layout.FigureConnectorStrategy;
import husacct.graphics.task.layout.LayeredLayoutStrategy;
import husacct.graphics.task.layout.NoLayoutStrategy;
import husacct.graphics.task.layout.layered.LayoutStrategy;
import husacct.graphics.task.layout.state.DrawingState;
import husacct.graphics.util.DrawingLayoutStrategyEnum;
import husacct.graphics.util.FigureMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.Figure;

public abstract class DrawingController {
	private static final double					MIN_ZOOMFACTOR	= 0.25;
	private static final double					MAX_ZOOMFACTOR	= 1.75;
	
	protected static final boolean				debugPrint		= true;
	protected DrawingSettingsHolder 			drawingSettingsHolder;
	protected DrawingLayoutStrategyEnum				layoutStrategyOption;
	
	private final HashMap<String, DrawingState>	storedStates	= new HashMap<String, DrawingState>();
	
	private Drawing								drawing;
	protected DrawingView						drawingView;
	
	private final FigureFactory					figureFactory;
	private final FigureConnectorStrategy		connectionStrategy;
	private LayoutStrategy						layoutStrategy;
	
	private final FigureMap						figureMap		= new FigureMap();
	protected ArrayList<BaseFigure>				contextFigures; 			// List with all the figures with isContext = true, not being a line.
	protected HashMap<String, String> 			parentFigureNameAndTypeMap; // Map with key = uniqueName of the parent figure and value = type. 
	
	protected Logger							logger			= Logger.getLogger(DrawingController.class);

	
	public static DrawingController getController(String drawingType) { // To do: Parameter = String drawingType
		DrawingController controller = null;
		if (drawingType.equals("AnalysedDrawing")) {
			controller = new AnalysedController();
		} else if (drawingType.equals("DefinedDrawing")) {
			controller = new DefinedController();
		}
		return controller;
	}
	
	public DrawingController() {
		drawingSettingsHolder = new DrawingSettingsHolder();
		layoutStrategyOption = DrawingLayoutStrategyEnum.BASIC_LAYOUT;
		figureFactory = new FigureFactory();
		connectionStrategy = new FigureConnectorStrategy();
		parentFigureNameAndTypeMap = new HashMap<String,String>();
		
		drawing = new Drawing();
		drawingView = new DrawingView(drawing);
		updateLayoutStrategy();
	}
	
	public void layoutStrategyChange(DrawingLayoutStrategyEnum selectedStrategyEnum) {
		layoutStrategyOption = selectedStrategyEnum;
		updateLayoutStrategy();
	}

	public abstract DrawingView moduleOpen(String[] paths);
	
	public void clearDrawing() {
		figureMap.clearAll();
		drawing.clearAll();
		
		drawingView.clearSelection();
		drawingView.invalidate();
	}
	
	public void clearLines() {
		drawing.clearAllLines();
	}
	
	public abstract DrawingView drawArchitectureTopLevel();
	
	public void zoomFactorChanged(double zoomFactor) {
		zoomFactor = Math.max(MIN_ZOOMFACTOR, zoomFactor);
		zoomFactor = Math.min(MAX_ZOOMFACTOR, zoomFactor);
		drawingView.setScaleFactor(zoomFactor);
	}
	
	public void drawLinesBasedOnSetting() {
		drawDependenciesAndViolationsForShownModules();
		if (drawingSettingsHolder.areSmartLinesOn()) 
			drawing.updateLineFigureToContext();
		if (drawingSettingsHolder.areLinesProportionalWide()) 
			drawing.updateLineFigureThicknesses(figureMap.getMaxAll());
	}
	
	protected void drawModulesAndLines(AbstractDTO[] modules) {
		clearDrawing();
		drawSingleLevel(modules);
		drawingView.cannotZoomOut();
	}
	
	protected void drawModulesAndLines(HashMap<String, ArrayList<AbstractDTO>> modules) {
		clearDrawing();
		drawMultiLevel(modules);
	}
	
	public void drawMultiLevel(HashMap<String, ArrayList<AbstractDTO>> modules) {
		clearDrawing();
		drawMultiLevelModules(modules);
		updateLayout();
		drawLinesBasedOnSetting();
	}
	
	public void drawMultiLevelModules(HashMap<String, ArrayList<AbstractDTO>> modules) {
		for (String parentUniqueName : modules.keySet()) {
			ParentFigure parentFigure = null;
			if (!parentUniqueName.isEmpty()) {
				if (parentFigureNameAndTypeMap.containsKey(parentUniqueName)) {
					String parentType = parentFigureNameAndTypeMap.get(parentUniqueName);
					if ((parentType != null) && !parentType.equals("")) {
						parentFigure = figureFactory.createParentFigure(parentUniqueName, parentType);
					} else {
						parentFigure = figureFactory.createParentFigure(parentUniqueName, "");
					}
				} else {
					parentFigure = figureFactory.createParentFigure(parentUniqueName, "");
				}
				drawing.add(parentFigure);
			}
			for (AbstractDTO dto : modules.get(parentUniqueName))
				try {
					BaseFigure generatedFigure = figureFactory.createFigure(dto);
					if (parentFigure != null){ 
						parentFigure.add(generatedFigure);
					}
					drawing.add(generatedFigure);
					figureMap.linkModule(generatedFigure, dto);
				} catch (Exception e) {
					logger.error("Could not generate and display figure.", e);
				}
			if (!parentUniqueName.isEmpty()) 
				parentFigure.updateLayout();
		}
	}
	
	public void drawSingleLevel(AbstractDTO[] modules) {
		drawSingleLevelModules(modules);
		updateLayout();
		drawLinesBasedOnSetting();
		drawingView.cannotZoomOut();
	}
	
	public void drawSingleLevelModules(AbstractDTO[] modules) {
		for (AbstractDTO dto : modules)
			try {
				BaseFigure generatedFigure = figureFactory.createFigure(dto);
				drawing.add(generatedFigure);
				figureMap.linkModule(generatedFigure, dto);
			} catch (Exception e) {
				logger.error("Could not generate and display figure.", e);
			}
	}
	
	public void exportImage() {
		drawing.showExportToImagePanel();
	}
	
	public DependencyDTO[] getDependenciesOfLine(BaseFigure selectedLine) {
		return figureMap.getDependencyDTOs(selectedLine);
	}
	
	public ViolationDTO[] getViolationsOfLine(BaseFigure selectedLine) {
		return figureMap.getViolationDTOs(selectedLine);
	}
	
	public BaseFigure[] getAllFigures() {
		return drawingView.toFigureArray(drawingView.findFigures(drawingView.getBounds()));
	}
	
	public DrawingSettingsHolder getDrawingSettingsHolder() {
		return drawingSettingsHolder;
	}
	
	public void drawDependenciesAndViolationsForShownModules() {
		BaseFigure[] shownModules = drawing.getShownModules();
		for (BaseFigure figureFrom : shownModules) {
			for (BaseFigure figureTo : shownModules) {
				if (figureFrom != figureTo) {
					DependencyDTO[] dependencies = getDependenciesBetween(figureFrom, figureTo);
					if (drawingSettingsHolder.areDependenciesShown() && (dependencies.length > 0)) {
						if (drawingSettingsHolder.areViolationsShown()) {
							ViolationDTO[] violations = getViolationsBetween(figureFrom, figureTo);
							if (violations.length > 0){ 
								figureFrom.addDecorator(figureFactory.createViolationsDecorator());
								drawDependenciesAndViolationsBetween(dependencies, violations, figureFrom, figureTo);
							} else {
								drawDependenciesBetween(dependencies,figureFrom, figureTo);
							}
						} else {
							drawDependenciesBetween(dependencies,figureFrom, figureTo);
						}
					}
				}
			}
		}
	}
	
	public void drawDependenciesBetween(DependencyDTO[] dependencies, BaseFigure figureFrom, BaseFigure figureTo) {
		RelationFigure dependencyFigure = null;
		try {
			dependencyFigure = figureFactory.createRelationFigure(dependencies);
		} catch (Exception e) {
			logger.error("Could not create a dependency figure.", e);
		}
		if (dependencyFigure != null) {
			figureMap.linkDependencies(dependencyFigure, dependencies);
			connectionStrategy.connect(dependencyFigure, figureFrom, figureTo);
			drawing.add(dependencyFigure);
		}
	}
	
	public void drawDependenciesAndViolationsBetween(DependencyDTO[] dependencies, ViolationDTO[] violations, BaseFigure figureFrom, BaseFigure figureTo) {
		RelationFigure violationFigure = null;
		try {
			violationFigure = figureFactory.createRelationFigureWithViolations(dependencies, violations);
		} catch (Exception e) {
			logger.error("Could not create a violation line between figures.", e);
		}
		if (violationFigure != null) {
			figureMap.linkViolations(violationFigure, violations);
			connectionStrategy.connect(violationFigure, figureFrom, figureTo);
			drawing.add(violationFigure);
		}
	}
	
	protected abstract DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo);
	
	public Drawing getDrawing() {
		return drawing;
	}
	
	public DrawingView getDrawingView() {
		return drawingView;
	}
	
	public FigureMap getFigureMap() {
		return figureMap;
	}
	
	public DrawingLayoutStrategyEnum getLayoutStrategy() {
		return layoutStrategyOption;
	}
	
	public BaseFigure[] getSelectedFigures() {
		return drawingView.toFigureArray(drawingView.getSelectedFigures());
	}
	
	protected abstract ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo);

	protected boolean hasDependencyBetween(BaseFigure figureFrom, BaseFigure figureTo){
		boolean b = false;
		return b;
	}	
	
	protected boolean hasSavedFigureStates(String paths) {
		return storedStates.containsKey(paths);
	}
	
	public boolean isDrawingVisible() {
		return drawingView.isVisible();
	}

	public DrawingView zoomIn() {
		Set<Figure> selection = drawingView.getSelectedFigures();
		
		if (selection.size() > 0) {
			ArrayList<BaseFigure> figures = new ArrayList<BaseFigure>();
			for (Figure s : selection) {
				if(s instanceof ParentFigure) {
					// Don't add figures.remove(f);
				} else {
					BaseFigure f = (BaseFigure) s;
					f.setContext(false); // minimizing potential side effects
					figures.add(f);
				}
			}
			if(drawingSettingsHolder.isZoomWithContextOn()){
				drawingView.clearSelection();
				drawingView.selectAll();
				List<BaseFigure> allFigures = Arrays.asList(drawingView.getSelectedFigures().toArray(new BaseFigure[0]));
				List<BaseFigure> contextModules = new ArrayList<BaseFigure>();
				for(BaseFigure fig : allFigures){
					if(fig instanceof ModuleFigure)
						contextModules.add(fig);
				}
				drawingView.clearSelection();
				drawingView.addToSelection(selection);
				
				for(BaseFigure selected : figures){
					for (BaseFigure module : contextModules){
						if (!module.equals(selected)) {
							if(hasDependencyBetween(selected, module)) {
								module.setContext(true);
							}
						}							
					}
				}
				for(BaseFigure contextModule : contextModules){
					if(contextModule.isContext())
						figures.add(contextModule);
				}
			}
			
			BaseFigure[] selectedFigures = figures.toArray(new BaseFigure[figures.size()]);
			this.zoomIn(selectedFigures);
		}
		return drawingView;
	}
	
	public abstract DrawingView refreshDrawing();

	
	protected void resetAllFigurePositions() {
		storedStates.clear();
	}
	
	protected void restoreFigurePositions(String paths) {
		if (storedStates.containsKey(paths)) {
			DrawingState state = storedStates.get(paths);
			state.restore(figureMap);
			drawingView.setHasHiddenFigures(state.hasHiddenFigures());
		}
	}
	
	protected void saveFigurePositions() {
		String paths = drawingSettingsHolder.getCurrentPathsToString();
		DrawingState state;
		if (storedStates.containsKey(paths)) state = storedStates.get(paths);
		else
			state = new DrawingState(drawing);
		
		state.save(figureMap);
		storedStates.put(paths, state);
	}
	
	public void saveSingleLevelFigurePositions() {
		if (drawingSettingsHolder.getCurrentPaths().length < 2) saveFigurePositions();
	}
	
	public void setCurrentPaths(String[] paths) {
		drawingSettingsHolder.setCurrentPaths(paths);
		if (!drawingSettingsHolder.getCurrentPaths()[0].isEmpty()) 
			drawingView.canZoomOut();
		else
			drawingView.cannotZoomOut();
	}
	
	private void updateLayoutStrategy() {
		switch (layoutStrategyOption) {
			case BASIC_LAYOUT:
				layoutStrategy = new BasicLayoutStrategy(drawing);
				break;
			case LAYERED_LAYOUT:
				layoutStrategy = new LayeredLayoutStrategy(drawing);
				break;
			case NO_LAYOUT:
				layoutStrategy = new NoLayoutStrategy();
				break;
			default:
				layoutStrategy = new BasicLayoutStrategy(drawing);
				break;
		}
	}
	
	protected void updateLayout() {
		layoutStrategy.doLayout();
		drawingView.setHasHiddenFigures(false);
		drawing.updateLines();
	}
	
	public abstract void zoomIn(BaseFigure[] zoomedModuleFigure);

	public abstract DrawingView zoomOut();
}
