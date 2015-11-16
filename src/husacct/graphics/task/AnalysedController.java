package husacct.graphics.task;


import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.services.IServiceListener;
import husacct.control.IControlService;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.util.DrawingDetail;
import husacct.validate.IValidateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

public class AnalysedController extends DrawingController {
	private final Logger			logger			= Logger.getLogger(AnalysedController.class);
	protected IAnalyseService		analyseService;
	protected IControlService		controlService;
	protected IValidateService		validateService;


	public AnalysedController() {
		super();
		initializeServices();
	}

	@Override
	public void drawArchitecture(DrawingDetail detail) {
		super.drawArchitecture(getCurrentDrawingDetail());
		super.notifyServiceListeners();

		AbstractDTO[] modules;

		if (areExternalLibrariesShown) {
			// Support of External libraries is improved in version 2.4 and does not require separate method calls.  
			modules = analyseService.getSoftwareUnitsInRoot();
		} else {
			SoftwareUnitDTO[] analysedModules = analyseService.getSoftwareUnitsInRoot();
			int nrOfInternalModules = 0;
			for (SoftwareUnitDTO analysedModule : analysedModules){
				if (!analysedModule.name.toLowerCase().equals("xlibraries"))
					nrOfInternalModules++;
			}
			AbstractDTO[] internalModules = new SoftwareUnitDTO[nrOfInternalModules];
			int i = 0;
			for (SoftwareUnitDTO analysedModule : analysedModules){
				if (!analysedModule.name.toLowerCase().equals("xlibraries")) {
					internalModules[i] = analysedModule;
					i++;	
				}
			}
			modules = internalModules;
		}
		resetCurrentPaths();
		if (DrawingDetail.WITH_VIOLATIONS == detail) 
			showViolations();
		drawModulesAndLines(modules);

		/*
		 * Multiple projects is removed, but the code remains. Check Git issue #
		 * 
		 * ArrayList<ProjectDTO> projects =
		 * this.controlService.getApplicationDTO().projects;
		 * 
		 * AbstractDTO[] projectArray = projects.toArray(new
		 * AbstractDTO[projects.size()]);
		 */
	}

	private void getAndDrawModulesIn(String parentName) {
		ArrayList<AbstractDTO> children = getChildrenOf(parentName);
		if (parentName.equals("")) {
			drawArchitecture(getCurrentDrawingDetail());
		} else if (children.size() > 0) {
			setCurrentPaths(new String[] { parentName });
			drawModulesAndLines(children.toArray(new AbstractDTO[] {}));
		} else {
			logger.warn("Tried to draw modules for \"" + parentName + "\", but it has no children.");
		}
	}

	private void getAndDrawModulesIn(String[] parentNames) {
		if (parentNames.length == 0) {
			drawArchitecture(getCurrentDrawingDetail()); 
		} else {
			// First, find the children of the selected module(s) (in parentnames) and store them in allChildren
			HashMap<String, ArrayList<AbstractDTO>> allChildren = new HashMap<String, ArrayList<AbstractDTO>>();
			ArrayList<String> compoundedNames = new ArrayList<String>();

			for (String parentName : parentNames) {
				compoundedNames.add(parentName);
				ArrayList<AbstractDTO> knownChildren = getChildrenOf(parentName);
				if (knownChildren.size() > 0) 
					allChildren.put(parentName, knownChildren);
			}

			if (contextFigures.size() > 0) {
				ArrayList<AbstractDTO> tmp = new ArrayList<AbstractDTO>();
				for (BaseFigure figure : contextFigures) {
					if (!figure.isLine() && !figure.isParent()) {
						AbstractDTO dto = getFigureMap().getModuleDTO(figure);
						if(dto instanceof SoftwareUnitDTO){
							SoftwareUnitDTO moduleDTO = (SoftwareUnitDTO) getFigureMap().getModuleDTO(figure);
							for (String parentName : parentNames) {
								//NOTE: A check to see if the current figure is part of the parents children.
								String[] partParentName = parentName.split("\\.");
								String[] partModuleParentName = moduleDTO.uniqueName.split("\\.");
								
								if(!allChildren.containsKey(partModuleParentName[0])){
									if(!partParentName[0].equals(partModuleParentName[0])){
										if (dto != null){ 
											tmp.add(dto); 
											break;
										} else{
											logger.debug(figure.getName() + " -> " + figure);
										}
									}
								}
							}
						}
					} else if (figure.isParent()) {
						ArrayList<AbstractDTO> knownChildren = getChildrenOf(figure.getName());
						if (knownChildren.size() > 0) allChildren.put(figure.getName(), knownChildren);
					}
				if (tmp.size() > 0) allChildren.put("", tmp);
				}
			}
			setCurrentPaths(parentNames);
			Set<String> parentNamesKeySet = allChildren.keySet();
			if (parentNamesKeySet.size() == 1) {
				String onlyParentModule = parentNamesKeySet.iterator().next();
				ArrayList<AbstractDTO> onlyParentChildren = allChildren.get(onlyParentModule);
				this.drawModulesAndLines(onlyParentChildren.toArray(new AbstractDTO[] {}));
			} else {
				this.drawModulesAndLines(allChildren);
			}
		}
	}

	private ArrayList<AbstractDTO> getChildrenOf(String parentName) {
		AbstractDTO[] children = analyseService.getChildUnitsOfSoftwareUnit(parentName);

		ArrayList<AbstractDTO> knownChildren = new ArrayList<AbstractDTO>();

		if (parentName.equals("")) drawArchitecture(getCurrentDrawingDetail());
		else if (children.length > 0) {
			knownChildren = new ArrayList<AbstractDTO>();
			for (AbstractDTO child : children)
				knownChildren.add(child);
		} else
			logger.warn("Tried to draw modules for \"" + parentName
					+ "\", but it has no children.");
		return knownChildren;
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		SoftwareUnitDTO dtoFrom = (SoftwareUnitDTO) getFigureMap().getModuleDTO(figureFrom);
		SoftwareUnitDTO dtoTo = (SoftwareUnitDTO) getFigureMap().getModuleDTO(figureTo);
		if (!dtoFrom.uniqueName.equals(dtoTo.uniqueName) && dtoFrom != null && dtoTo != null){ 
			return analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(dtoFrom.uniqueName, dtoTo.uniqueName);
		}
		return new DependencyDTO[] {};
	}
	
	protected boolean hasDependencyBetween(BaseFigure figureFrom, BaseFigure figureTo){
		boolean b = false;
		//TODO This will always return a stacktrace of a nullpointerexception if there isn't a dependency, 
		//This needs to be cleaner but we couldn't find a method in time.
		try{
			SoftwareUnitDTO dtoFrom = (SoftwareUnitDTO) getFigureMap().getModuleDTO(figureFrom);
			SoftwareUnitDTO dtoTo = (SoftwareUnitDTO) getFigureMap().getModuleDTO(figureTo);
			if (dtoFrom != null && dtoTo != null && !dtoFrom.uniqueName.equals(dtoTo.uniqueName)){ 
				if((analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(dtoFrom.uniqueName, dtoTo.uniqueName).length > 0) || (analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(dtoTo.uniqueName, dtoFrom.uniqueName).length > 0)){
					b = true;
				}
			}
		}catch (Exception e) {
			logger.warn(" dto was null, but that is ok");
			//e.printStackTrace();
		}
		return b;		
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		SoftwareUnitDTO dtoFrom = (SoftwareUnitDTO) getFigureMap().getModuleDTO(figureFrom);
		SoftwareUnitDTO dtoTo = (SoftwareUnitDTO) getFigureMap().getModuleDTO(figureTo);
		return validateService.getViolationsByPhysicalPath(dtoFrom.uniqueName, dtoTo.uniqueName);
	}

	@Override
	public void hideLibraries() {
		super.hideLibraries();
		refreshDrawing();
	}

	private void initializeServices() {
		controlService = ServiceProvider.getInstance().getControlService();

		analyseService = ServiceProvider.getInstance().getAnalyseService();
		analyseService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				AnalysedController.this.refreshDrawing();
			}
		});

		this.validateService = ServiceProvider.getInstance()
				.getValidateService();
		this.validateService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				if (AnalysedController.this.areViolationsShown()) {
					AnalysedController.this.refreshDrawing();
				}
			}
		});
	}

	@Override
	public void moduleOpen(String[] paths) {
		super.notifyServiceListeners();
		saveSingleLevelFigurePositions();
		resetContextFigures();
		if (paths.length == 0) drawArchitecture(getCurrentDrawingDetail());
		else
			this.getAndDrawModulesIn(paths);
	}

	@Override
	public void moduleZoom(BaseFigure[] figures) {
		super.notifyServiceListeners();
		resetContextFigures();
		//parentFigureNameAndTypeMap = new HashMap<String,String>();
		ArrayList<String> parentNames = new ArrayList<String>();
		for (BaseFigure figure : figures){
			if (figure.isModule() && !(figure.isContext())) 
				try {				
					SoftwareUnitDTO parentDTO = (SoftwareUnitDTO) getFigureMap().getModuleDTO(figure);
					parentNames.add(parentDTO.uniqueName);
					parentFigureNameAndTypeMap.put(parentDTO.uniqueName, parentDTO.type);
				} catch (Exception e) {
					logger.warn("Could not zoom on this object: " + figure.getName() + ". Expected a different DTO type.");
					//e.printStackTrace();
			} else if (figure.isContext() || !figure.isLine()) {
				contextFigures.add(figure);
			} else {
				logger.warn("Could not zoom on this object: " + figure.getName() + ". Not a module to zoom on.");
			}
		}

		if (parentNames.size() > 0) {
			saveSingleLevelFigurePositions();
			this.getAndDrawModulesIn(parentNames.toArray(new String[] {}));
		}
	}

	@Override
	public void moduleZoomOut() {
		super.notifyServiceListeners();
		if (getCurrentPaths().length > 0) {
			saveSingleLevelFigurePositions();
			resetContextFigures();
			String firstCurrentPaths = getCurrentPaths()[0];
			SoftwareUnitDTO parentDTO = analyseService.getParentUnitOfSoftwareUnit(firstCurrentPaths);
			if (parentDTO != null) 
				this.getAndDrawModulesIn(parentDTO.uniqueName);
			else
				zoomOutFailed();
		} else
			zoomOutFailed();
		
		super.restoreModules();
	}

	@Override
	public void refreshDrawing() {
		super.notifyServiceListeners();
		super.refreshDrawing();
		this.getAndDrawModulesIn(getCurrentPaths());
	}

	private void resetContextFigures() {
		contextFigures = new ArrayList<BaseFigure>();
	}

	@Override
	public void showLibraries() {
		super.showLibraries();
		refreshDrawing();
	}

	@Override
	public void showViolations() {
		if (validateService.isValidated()) {
			super.showViolations();
		} else {
			super.hideViolations();
		}
	}

	public void zoomOutFailed() {
		logger.warn("Tried to zoom out from \""
				+ getCurrentPaths()
				+ "\", but it has no parent (could be root if it's an empty string).");
		logger.info("Reverting to the root of the application.");
		drawArchitecture(getCurrentDrawingDetail());
	}

	/*
	 * Code for zooming when Multiple Projects are supported. 
	 * May need some refactoring.
	 * 
	 * public void moduleZoom(BaseFigure[] figures) {
	 * super.notifyServiceListeners(); this.resetContextFigures();
	 * 
	 * boolean allProjects = false; for(BaseFigure fig : figures){ if(!(fig
	 * instanceof ProjectFigure)){ allProjects = false; } else{ allProjects =
	 * true; } }
	 * 
	 * if(allProjects){ setCurrentPathsForProjects(figures); ProjectDTO project
	 * = (ProjectDTO) this.getFigureMap().getModuleDTO(figures[0]);
	 * AbstractDTO[] abstractDTOs = project.analysedModules.toArray(new
	 * AbstractDTO[project.analysedModules.size()] ); if(abstractDTOs.length !=
	 * 0){
	 * 
	 * this.drawModulesAndLines(abstractDTOs); } }
	 * 
	 * ArrayList<String> parentNames =
	 * this.sortFiguresBasedOnZoomability(figures);
	 * 
	 * if (parentNames.size() > 0) { saveSingleLevelFigurePositions();
	 * this.getAndDrawModulesIn(parentNames.toArray(new String[] {})); } }
	 * 
	// TODO Needs to be removed as soon as uniqueName of a
	// AnalysedDTO contains a project
	private void setCurrentPathsForProjects(BaseFigure[] figures) {
		String[] paths = new String[1];
		paths[0] = figures[0].getName();
		super.setCurrentPaths(paths);
	}
	 */
}
