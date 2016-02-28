package husacct.graphics.task;


import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.domain.Drawing;
import husacct.graphics.domain.DrawingView;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.domain.figures.FigureFactory;
import husacct.graphics.domain.figures.ModuleFigure;
import husacct.graphics.domain.figures.ParentFigure;
import husacct.graphics.domain.figures.RelationFigure;
import husacct.graphics.task.modulelayout.BasicLayoutStrategy;
import husacct.graphics.task.modulelayout.ContainerLayoutStrategy;
import husacct.graphics.task.modulelayout.ModuleLayoutsEnum;
import husacct.graphics.task.modulelayout.FigureConnectorStrategy;
import husacct.graphics.task.modulelayout.LayeredLayoutStrategy;
import husacct.graphics.task.modulelayout.NoLayoutStrategy;
import husacct.graphics.task.modulelayout.layered.LayoutStrategy;
import husacct.graphics.task.modulelayout.state.DrawingState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;

public abstract class DrawingController {
	private static final double					MIN_ZOOMFACTOR	= 0.25;
	private static final double					MAX_ZOOMFACTOR	= 1.75;
	
	private Drawing								drawing;
	protected DrawingView						drawingView;
	
	protected DrawingSettingsHolder 			drawingSettingsHolder;
	protected final FigureFactory				figureFactory;
	private final FigureConnectorStrategy		connectionStrategy;
	private LayoutStrategy						layoutStrategy;
	protected ModuleLayoutsEnum					layoutStrategyOption;
	
	public ArrayList<ModuleFigure>				contextFigures; 			// List with all the figures with isContext = true, not being a line (public, because of testability)
	protected HashMap<String, String> 			parentFigureNameAndTypeMap; // Map with key = uniqueName of the parent figure and value = type. 
	private final HashMap<String, DrawingState>	storedStates	= new HashMap<String, DrawingState>();
	
	protected Logger							logger			= Logger.getLogger(DrawingController.class);

	
	public static DrawingController getController(DrawingTypesEnum drawingType) {
		DrawingController controller = null;
		if (drawingType == DrawingTypesEnum.IMPLEMENTED_ARCHITECTURE) {
			controller = new AnalysedController();
		} else if (drawingType == DrawingTypesEnum.INTENDED_ARCHITECTURE) {
			controller = new DefinedController();
		}
		return controller;
	}
	
	public DrawingController() {
		drawingSettingsHolder = new DrawingSettingsHolder();
		layoutStrategyOption = ModuleLayoutsEnum.BASIC_LAYOUT;
		figureFactory = new FigureFactory();
		connectionStrategy = new FigureConnectorStrategy();
		parentFigureNameAndTypeMap = new HashMap<String,String>();
		
		drawing = new Drawing();
		drawingView = new DrawingView(drawing);
		updateLayoutStrategy();
	}
	
	public void clearDrawing() {
		drawing.clearAll();
		drawingView.clearSelection();
		drawingView.invalidate();
	}
	
	// Method to create the top-level diagram.
	public DrawingView drawArchitectureTopLevel() {
		try {
			ArrayList<ModuleFigure> includedModuleFiguresInRoot = new ArrayList<ModuleFigure>();
			ArrayList<ModuleFigure> allModuleFiguresInRoot = getModuleFiguresInRoot();
			if (drawingSettingsHolder.areExternalLibrariesShown()) {
				// Select all modules in root
				includedModuleFiguresInRoot = allModuleFiguresInRoot;
			} else {
				// Select only internal modules in root
				for (ModuleFigure moduleFigureInRoot : allModuleFiguresInRoot){
					String moduleType = moduleFigureInRoot.getType().toLowerCase();
					if (!moduleType.equals("externallibrary") && !moduleType.equals("library")) {
						includedModuleFiguresInRoot.add(moduleFigureInRoot);	
					}
				}
			}
			drawingSettingsHolder.resetCurrentPaths();
			drawModulesAndRelations_SingleLevel(includedModuleFiguresInRoot.toArray(new ModuleFigure[includedModuleFiguresInRoot.size()]));
		} catch (Exception e) {
			logger.error(" Exception: " + e.getMessage());
		}
		return drawingView;
	}

	
	protected void drawModulesAndRelations_SingleLevel(ModuleFigure[] modules) {
		clearDrawing();
		for (ModuleFigure moduleFigure : modules) {
			drawing.add(moduleFigure);
		}
		updateLayout();
		drawRelationFiguresForShownModules();
		drawingView.cannotZoomOut();
	}
	
	protected void drawModulesAndRelations_MultiLevel(HashMap<String, ArrayList<ModuleFigure>> modules) {
		clearDrawing();
		for (String parentUniqueName : modules.keySet()) {
			ParentFigure parentFigure = null;
			if (!parentUniqueName.isEmpty()) {
				// Add the parent figure
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
			// Add the children to the parent figure (or else, they become part of the root)  
			for (ModuleFigure childModuleFigure : modules.get(parentUniqueName)) {
				if (parentFigure != null){ 
					parentFigure.add(childModuleFigure);
				}
				drawing.add(childModuleFigure);
			}
			// Set the layout of the parent figure
			if (parentFigure != null){ 
				ContainerLayoutStrategy cls = new ContainerLayoutStrategy(parentFigure);
				cls.doLayout();
			}
		}
		updateLayout();
		drawRelationFiguresForShownModules();
	}
	
	public void drawRelationFiguresForShownModules() {
		ModuleFigure[] shownModules = drawing.getShownModules();
		for (ModuleFigure figureFrom : shownModules) {
			for (ModuleFigure figureTo : shownModules) {
				if (figureFrom != figureTo) {
					if (hasRelationBetween(figureFrom, figureTo)) {
						boolean drawRelationsWithoutViolations = true;
						if (drawingSettingsHolder.areViolationsShown()) {
							// Draw RelationFigures with Violations
							ViolationDTO[] violations = getViolationsBetween(figureFrom, figureTo);
							if (violations.length > 0){ 
								drawRelationsWithoutViolations = false;
								figureFrom.addDecorator(figureFactory.createViolationsDecorator());
								RelationFigure violationFigure = getRelationFigureWithViolationsBetween(figureFrom, figureTo);
								if (violationFigure != null) {
									connectionStrategy.connect(violationFigure, figureFrom, figureTo);
									drawing.add(violationFigure);
								}
							}
						}
						if (drawingSettingsHolder.areDependenciesShown() && drawRelationsWithoutViolations) {
							// Draw RelationFigures without Violations
							RelationFigure dependencyFigure = getRelationFigureBetween(figureFrom, figureTo);
							if (dependencyFigure != null) {
								connectionStrategy.connect(dependencyFigure, figureFrom, figureTo);
								drawing.add(dependencyFigure);
							}
						}
					}
				}
			}
		}
		if (drawingSettingsHolder.areSmartLinesOn()) {
			drawing.updateLineFigureToContext();
		}
		if (drawingSettingsHolder.areLinesProportionalWide()) { 
			drawing.updateLineFigureThicknesses(drawing.getMaxAll());
		}
	}
	
	public void exportImage() {
		drawing.showExportToImagePanel();
	}
	
	public void gatherChildModuleFiguresAndContextFigures_AndDraw(String[] parentNames) { // Public visibility, because of testability. Do not call from Presentation!
		if (parentNames.length == 0) {
			drawArchitectureTopLevel(); 
		} else {
			/* 1) find the children of the selected module(s) (in parentNames) and store them in parentChildrenMap
				  Map parentChildrenMap: key = parentName; value = ArrayList<knownChildrenOfParent> */
			HashMap<String, ArrayList<ModuleFigure>> parentChildrenMap = new HashMap<String, ArrayList<ModuleFigure>>(); 
			for (String parentName : parentNames) {
				if (!parentName.equals("") && !parentName.equals("**")) {
					ArrayList<ModuleFigure> knownChildren = getChildModuleFiguresOfParent(parentName);
					if (knownChildren.size() > 0) {
						parentChildrenMap.put(parentName, knownChildren);
					} else {
						return;
					}
				}
			}
			// 2) If there are contextFigures, put an entry in parentChildrenMap for each combo of parent-child contextFigure(s)   
			if (contextFigures.size() > 0) {
				// a) Filter out context figures that are children of one of the parents.
				HashSet<ModuleFigure> filteredContextFigures = new HashSet<ModuleFigure>();
				for (ModuleFigure contextFigure : contextFigures) {
					for (String parentName : parentNames) {
						if (!contextFigure.getUniqueName().startsWith(parentName)) {
							filteredContextFigures.add(contextFigure);
						}
					}
				}
				// b) Add the filteredContextFigures with their parents to parentChildrenMap
				ArrayList<ModuleFigure> contextFiguresInRoot = new ArrayList<ModuleFigure>();
				for (ModuleFigure figure : filteredContextFigures) {
					String parentOfContextFigure = getUniqueNameOfParentModule(figure.getUniqueName());
					if (parentOfContextFigure.equals("")) {
						contextFiguresInRoot.add(figure.clone());
					} else {
						if (parentChildrenMap.containsKey(parentOfContextFigure)) {
							ArrayList<ModuleFigure> children = parentChildrenMap.get(parentOfContextFigure);
							if (!children.contains(parentOfContextFigure)) {
								children.add(figure.clone());
							}
						} else {
							ArrayList<ModuleFigure> children = new ArrayList<ModuleFigure>();
							children.add(figure.clone());
							parentChildrenMap.put(parentOfContextFigure, children);
						}
					}
					parentChildrenMap.put("", contextFiguresInRoot);
				}
				resetContextFigures();
			}
			// 3) Hand-over to drawing services 
			Set<String> parentNamesKeySet = parentChildrenMap.keySet();
			Set<String> currentPaths = new HashSet<String>();
			for (String parentName : parentNamesKeySet) {
				if (!parentName.equals("")) {
					currentPaths.add(parentName);
				}
			}
			setCurrentPaths(currentPaths.toArray(new String[] {}));
			if (parentNamesKeySet.size() == 1) {
				String onlyParentModule = parentNamesKeySet.iterator().next();
				ArrayList<ModuleFigure> onlyParentChildren = parentChildrenMap.get(onlyParentModule);
				this.drawModulesAndRelations_SingleLevel(onlyParentChildren.toArray(new ModuleFigure[] {}));
			} else {
				this.drawModulesAndRelations_MultiLevel(parentChildrenMap);
			}
		}
	}

	public DependencyDTO[] getDependenciesOfLine(BaseFigure selectedLine) {
		if (selectedLine instanceof RelationFigure) {
			ConnectionFigure cf = (ConnectionFigure) selectedLine;
			ModuleFigure from = (ModuleFigure) cf.getStartFigure();
			ModuleFigure to = (ModuleFigure) cf.getEndFigure();
			return getDependenciesBetween(from, to);
		} else {
			return new DependencyDTO[] {};
		}
	}
	
	public ViolationDTO[] getViolationsOfLine(BaseFigure selectedLine) {
		if (selectedLine instanceof RelationFigure) {
			ConnectionFigure cf = (ConnectionFigure) selectedLine;
			ModuleFigure from = (ModuleFigure) cf.getStartFigure();
			ModuleFigure to = (ModuleFigure) cf.getEndFigure();
			return getViolationsBetween(from, to);
		} else {
			return new ViolationDTO[] {};
		}
	}
	
	public DrawingSettingsHolder getDrawingSettingsHolder() {
		return drawingSettingsHolder;
	}
	
	public Drawing getDrawing() {
		return drawing;
	}
	
	public DrawingView getDrawingView() {
		return drawingView;
	}
	
	public ModuleLayoutsEnum getLayoutStrategy() {
		return layoutStrategyOption;
	}
	
	public void layoutStrategyChange(ModuleLayoutsEnum selectedStrategyEnum) {
		layoutStrategyOption = selectedStrategyEnum;
		updateLayoutStrategy();
	}

	public DrawingView moduleOpen(String[] paths) {
		saveSingleLevelFigurePositions();
		if (paths.length == 0) 
			drawArchitectureTopLevel();
		else
			gatherChildModuleFiguresAndContextFigures_AndDraw(paths);
		return drawingView;
	}
	
	public DrawingView refreshDrawing() {
		gatherChildModuleFiguresAndContextFigures_AndDraw(drawingSettingsHolder.getCurrentPaths());
		return drawingView;
	}
	
	public void resetContextFigures() { // Public, because of testability.
		contextFigures = new ArrayList<ModuleFigure>();
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
	
	public DrawingView zoomIn() {
		try {
			Set<Figure> selection = drawingView.getSelectedFigures();
			if (selection.size() > 0) {
				// 1) Create a list of ModuleFigures in the current drawing as base to create a zoomed-in drawing.  
				ArrayList<ModuleFigure> selectedModules = new ArrayList<ModuleFigure>();
				ArrayList<ModuleFigure> moduleFiguresForNewDrawing = new ArrayList<ModuleFigure>();
				// 1a) Add the selected figures (property isContext = false)
				for (Figure s : selection) {
					if(s instanceof ModuleFigure) { 
						ModuleFigure mf = (ModuleFigure) s;
						mf.setContext(false); // minimizing potential side effects
						selectedModules.add(mf);
						moduleFiguresForNewDrawing.add(mf);
					} else if (s instanceof ParentFigure){
						// Do nothing yet. Maybe later: create ModuleFigure and add it.
					}
				}
				if(drawingSettingsHolder.isZoomWithContextOn()){
					// 1b)Objective: Add context ModuleFigures: modules with a relation to one of the selected figures.
					// Get all modules in the current drawing
					ModuleFigure[] potentialContextModules = drawing.getShownModules();
					/* If a selected figure has a relation with a potentialContextFigure, 
					 *    then set isContext = true, and add it to figuresForNewDrawing. */
					for(ModuleFigure selected : selectedModules){
						for (ModuleFigure module : potentialContextModules){
							if (!module.equals(selected)) {
								if(hasRelationBetween(selected, module)) {
									module.setContext(true);
									moduleFiguresForNewDrawing.add(module);
								}
							}							
						}
					}
				}
				// 2) Create a list with the uniqueNames of the to be zoomed-in modules + reset and set contextFigures.
				resetContextFigures();
				ArrayList<String> parentNames = new ArrayList<String>(); // Parent is a module to-be-zoomed-in 
				for (ModuleFigure moduleFigure : moduleFiguresForNewDrawing){
					if (!moduleFigure.isContext()) {
						parentNames.add(moduleFigure.getUniqueName());
						parentFigureNameAndTypeMap.put(moduleFigure.getUniqueName(), moduleFigure.getType());
					} else {
						contextFigures.add((ModuleFigure) moduleFigure);
					}
				}
				// 3) Forward to next process step
				if (parentNames.size() > 0) {
					saveSingleLevelFigurePositions();
					this.gatherChildModuleFiguresAndContextFigures_AndDraw(parentNames.toArray(new String[] {}));
				}
			}
		} catch (Exception e) {
			logger.error(" Exception: " + e.getMessage());
		}
		return drawingView;
	}
	
	public void zoomFactorChanged(double zoomFactor) {
		zoomFactor = Math.max(MIN_ZOOMFACTOR, zoomFactor);
		zoomFactor = Math.min(MAX_ZOOMFACTOR, zoomFactor);
		drawingView.setScaleFactor(zoomFactor);
	}
	
	public DrawingView zoomOut() {
		// Objective: Collapse the ParentModule(s) witch is/are most decomposed.
		resetContextFigures();
		saveSingleLevelFigurePositions();
		// Determine the decomposition level.
		HashMap<String, Integer> pathsWithDecompositionLevel = new HashMap<String, Integer>(); 
		int highestLevel = 0;
		for (String currentPath : drawingSettingsHolder.getCurrentPaths()) {
			if (currentPath != null) {
				String[] levels = currentPath.split("\\.");
				int decompositionLevel = levels.length;
				pathsWithDecompositionLevel.put(currentPath, decompositionLevel);
				if (decompositionLevel > highestLevel) {
					highestLevel = decompositionLevel;
				}
			}
		}
		// Collapse the parent(s) with the highest decomposition level.
		boolean drawTopLevel = true;
		ArrayList<String> parentNames = new ArrayList<String>(); // Parent is a module to-be-zoomed-in 
		HashSet<String> contextFigureNames = new HashSet<String>(); // Parent is a module to-be-zoomed-in 
		for (String path : pathsWithDecompositionLevel.keySet()) {
			String parentName;
			if (pathsWithDecompositionLevel.get(path) == highestLevel) {
				parentName = getUniqueNameOfParentModule(path);
			} else {
				parentName = path;
			}
			if (parentName.equals("")) {
				contextFigureNames.add(path);
			} else {
				parentNames.add(parentName);
				drawTopLevel = false;
			}
		}
		if (drawTopLevel) {
			drawArchitectureTopLevel();
		} else {
			//Determine the context figures
			for (ModuleFigure moduleFigure : drawing.getShownModules()) {
				String parentName = getUniqueNameOfParentModule(moduleFigure.getUniqueName());
				if (parentName.equals("")) {
					contextFigureNames.add(moduleFigure.getUniqueName());
				}			
			}
			for (String contextFigureName : contextFigureNames) {
				ModuleFigure contextFigure = getModuleFiguresByUniqueName(contextFigureName);
				contextFigures.add(contextFigure);
			}
			gatherChildModuleFiguresAndContextFigures_AndDraw(parentNames.toArray(new String[] {}));
		}
		return drawingView;
	}
	
	// Abstract	methods
	protected abstract ArrayList<ModuleFigure> getChildModuleFiguresOfParent(String parentName);
	
	protected abstract DependencyDTO[] getDependenciesBetween(ModuleFigure figureFrom, ModuleFigure figureTo);
	
	// Return null if no module is found, or if uniqueName = null, "", or "**".
	protected abstract ModuleFigure getModuleFiguresByUniqueName(String uniqueName);

	protected abstract ArrayList<ModuleFigure> getModuleFiguresInRoot();

	protected abstract RelationFigure getRelationFigureBetween(ModuleFigure figureFrom, ModuleFigure figureTo);
	
	protected abstract RelationFigure getRelationFigureWithViolationsBetween(ModuleFigure figureFrom, ModuleFigure figureTo);

	protected abstract String getUniqueNameOfParentModule(String childUniqueName);
	
	protected abstract ViolationDTO[] getViolationsBetween(ModuleFigure figureFrom, ModuleFigure figureTo);

	protected abstract boolean hasRelationBetween(ModuleFigure figureFrom, ModuleFigure figureTo);
	
	// Methods to save and restore figure positions
	protected void restoreFigurePositions(String paths) {
		if (storedStates.containsKey(paths)) {
			DrawingState state = storedStates.get(paths);
			state.restore();
			drawingView.setHasHiddenFigures(state.hasHiddenFigures());
		}
	}
	
	protected void saveFigurePositions() {
		String paths = drawingSettingsHolder.getCurrentPathsToString();
		DrawingState state;
		if (storedStates.containsKey(paths)) state = storedStates.get(paths);
		else
			state = new DrawingState(drawing);
		
		state.save();
		storedStates.put(paths, state);
	}
	
	public void saveSingleLevelFigurePositions() {
		if (drawingSettingsHolder.getCurrentPaths().length < 2) saveFigurePositions();
	}
	

	
}
