package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.PhysicalPathDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.services.IServiceListener;
import husacct.define.IDefineService;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.util.DrawingDetail;
import husacct.validate.IValidateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DefinedController extends DrawingController {
	protected IAnalyseService analyseService;
	protected IDefineService defineService;
	protected IValidateService validateService;

	public DefinedController() {
		super();
		initializeServices();
	}

	private void initializeServices() {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		defineService = ServiceProvider.getInstance().getDefineService();
		ServiceProvider.getInstance().getDefineService().addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				refreshDrawing();
			}
		});
	}

	@Override
	public void refreshDrawing() {
		super.notifyServiceListeners();
		getAndDrawModulesIn(getCurrentPaths());
	}

	@Override
	public void showViolations() {
		super.showViolations();
		validateService.checkConformance();
	}

	@Override
	public void drawArchitecture(DrawingDetail detail) {
		super.notifyServiceListeners();
		AbstractDTO[] modules = defineService.getRootModules();
		resetCurrentPaths();
		if (DrawingDetail.WITH_VIOLATIONS == detail) {
			showViolations();
		}
		drawModulesAndLines(modules);
	}
	
	private HashMap<String, BaseFigure> definedFigures;

	@Override
	public void moduleZoom(BaseFigure[] figures) {
		super.notifyServiceListeners();
		definedFigures = new HashMap<String, BaseFigure>();
		ArrayList<String> parentNames = new ArrayList<String>();
		for (BaseFigure figure : figures) {
			if (figure.isModule()) {
				try {
					ModuleDTO parentDTO = (ModuleDTO) this.figureMap.getModuleDTO(figure);
					parentNames.add(parentDTO.logicalPath);
					definedFigures.put(parentDTO.logicalPath, figure);
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("Could not zoom on this object: " + figure.getName() + ". Expected a different DTO type.");
				}
			} else {
				logger.warn("Could not zoom on this object: " + figure.getName() + ". Not a module to zoom on.");
			}
		}

		if (parentNames.size() > 0) {
			saveSingleLevelFigurePositions();
			getAndDrawModulesIn(parentNames.toArray(new String[] {}));
		}
	}

	@Override
	public void moduleZoomOut() {
		super.notifyServiceListeners();
		if (getCurrentPaths().length > 0) {
			saveSingleLevelFigurePositions();
			String firstCurrentPaths = getCurrentPaths()[0];
			String parentPath = defineService.getParentFromModule(firstCurrentPaths);
			if (null != parentPath) {
				getAndDrawModulesIn(parentPath);
			} else {
				logger.warn("Tried to zoom out from \"" + getCurrentPaths() + "\", but it has no parent (could be root if it's an empty string).");
				logger.debug("Reverting to the root of the application.");
				drawArchitecture(getCurrentDrawingDetail());
			}
		} else {
			logger.warn("Tried to zoom out from \"" + getCurrentPaths() + "\", but it has no parent (could be root if it's an empty string).");
			logger.debug("Reverting to the root of the application.");
			drawArchitecture(getCurrentDrawingDetail());
		}
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		ModuleDTO dtoFrom = (ModuleDTO) figureMap.getModuleDTO(figureFrom);
		ModuleDTO dtoTo = (ModuleDTO) figureMap.getModuleDTO(figureTo);
		ArrayList<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();

		if (!figureFrom.equals(figureTo)) {
			for (PhysicalPathDTO physicalFromPathDTO : dtoFrom.physicalPathDTOs) {
				for (PhysicalPathDTO physicalToPath : dtoTo.physicalPathDTOs) {
					DependencyDTO[] foundDependencies = analyseService.getDependencies(physicalFromPathDTO.path, physicalToPath.path);
					for (DependencyDTO tempDependency : foundDependencies) {
						dependencies.add(tempDependency);
					}
				}
			}
		}
		return dependencies.toArray(new DependencyDTO[] {});
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		ModuleDTO dtoFrom = (ModuleDTO) figureMap.getModuleDTO(figureFrom);
		ModuleDTO dtoTo = (ModuleDTO) figureMap.getModuleDTO(figureTo);
		return validateService.getViolationsByLogicalPath(dtoFrom.logicalPath, dtoTo.logicalPath);
	}

	private void getAndDrawModulesIn(String parentName) {
		if (parentName.equals("") || parentName.equals("**")) {
			drawArchitecture(getCurrentDrawingDetail());
		} else {
			ModuleDTO[] children = defineService.getChildrenFromModule(parentName);
			if (children.length > 0) {
				setCurrentPaths(new String[] { parentName });
				drawModulesAndLines(children);
			} else {
				logger.warn("Tried to draw modules for \"" + parentName + "\", but it has no children.");
			}
		}
	}

	private void getAndDrawModulesIn(String[] parentNames) {
		if(parentNames.length==0){
			drawArchitecture(getCurrentDrawingDetail());
		}else{
			HashMap<String, ArrayList<AbstractDTO>> allChildren = new HashMap<String, ArrayList<AbstractDTO>>();
			for (String parentName : parentNames) {
				AbstractDTO[] children = defineService.getChildrenFromModule(parentName);
				if (parentName.equals("") || parentName.equals("**")) {
					drawArchitecture(getCurrentDrawingDetail());
					continue;
				} else if (children.length > 0) {
					ArrayList<AbstractDTO> knownChildren = new ArrayList<AbstractDTO>();
					for (AbstractDTO child : children) {
						knownChildren.add(child);
					}
					allChildren.put(parentName, knownChildren);
				} else {
					AbstractDTO value = figureMap.getModuleDTO(definedFigures.get(parentName));
					ArrayList<AbstractDTO> tmpList = new ArrayList<AbstractDTO>();
					tmpList.add(value);
					allChildren.put("", tmpList);
					logger.warn("Tried to draw modules for \"" + parentName + "\", but it has no children.");
				}
			}
			setCurrentPaths(parentNames);
	
			Set<String> parentNamesKeySet = allChildren.keySet();
			if (parentNamesKeySet.size() == 1 && !areFiguresSavedForZoom()) {
				String onlyParentModule = parentNamesKeySet.iterator().next();
				ArrayList<AbstractDTO> onlyParentChildren = allChildren.get(onlyParentModule);
				drawModulesAndLines(onlyParentChildren.toArray(new AbstractDTO[] {}));
			} else {
				drawModulesAndLines(allChildren);
			}
		}
	}

	@Override
	public void moduleOpen(String[] paths) {
		super.notifyServiceListeners();
		if (paths.length == 0) {
			drawArchitecture(getCurrentDrawingDetail());
		} else {
			getAndDrawModulesIn(paths);
		}
	}
}
