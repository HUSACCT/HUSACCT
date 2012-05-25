package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.validate.IValidateService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.apache.log4j.Logger;

public class AnalysedController extends DrawingController {
	protected IAnalyseService analyseService;
	protected IValidateService validateService;
	
	private Logger logger = Logger.getLogger(AnalysedController.class);

	public AnalysedController() {
		super();
		initializeServices();
	}

	private void initializeServices() {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		// TODO: Uncomment wanneer analyse addServiceListener heeft geïmplementeerd!
		// ServiceProvider.getInstance().getAnalyseService().addServiceListener(new IServiceListener(){
		// @Override
		// public void update() {
		// refreshDrawing();
		// }
		// });
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
		AbstractDTO[] modules = analyseService.getRootModules();
		resetCurrentPaths();
		if (DrawingDetail.WITH_VIOLATIONS == detail) {
			showViolations();
		}
		drawModulesAndLines(modules);
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) figureMap.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) figureMap.getModuleDTO(figureTo);
		if (!figureFrom.equals(figureTo)) {
			return analyseService.getDependencies(dtoFrom.uniqueName, dtoTo.uniqueName);
		}
		return new DependencyDTO[] {};
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) figureMap.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) figureMap.getModuleDTO(figureTo);
		return validateService.getViolationsByPhysicalPath(dtoFrom.uniqueName, dtoTo.uniqueName);
	}

	@Override
	public void moduleZoom(BaseFigure[] figures) {
		super.notifyServiceListeners();
		saveFigurePositions();
		ArrayList<String> parentNames = new ArrayList<String>();
		for (BaseFigure figure : figures) {
			if (figure.isModule()) {
				try {
					AnalysedModuleDTO parentDTO = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figure);
					parentNames.add(parentDTO.uniqueName);
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("Could not zoom on this object: " + figure.getName() +". Expected a different DTO type.");
				}
			}else{
				logger.warn("Could not zoom on this object: " + figure.getName() +". Not a module to zoom on.");
			}
		}

		
		if(parentNames.size()>0){
			getAndDrawModulesIn(parentNames.toArray(new String[] {}));
		}
	}

	@Override
	public void moduleZoomOut() {
		super.notifyServiceListeners();
		if (getCurrentPaths().length > 0) {
			String firstCurrentPaths = getCurrentPaths()[0];
			AnalysedModuleDTO parentDTO = analyseService.getParentModuleForModule(firstCurrentPaths);
			if (null != parentDTO) {
				getAndDrawModulesIn(parentDTO.uniqueName);
			} else {
				logger.warn("Tried to zoom out from \"" + getCurrentPaths() + "\", but it has no parent (could be root if it's an empty string).");
				logger.debug("Reverting to the root of the application.");
				drawArchitecture(getCurrentDrawingDetail());
			}
		}else{
			logger.warn("Tried to zoom out from \"" + getCurrentPaths() + "\", but it has no parent (could be root if it's an empty string).");
			logger.debug("Reverting to the root of the application.");
			drawArchitecture(getCurrentDrawingDetail());
		}
		saveFigurePositions();
	}

	private void getAndDrawModulesIn(String parentName) {
		AnalysedModuleDTO[] children = analyseService.getChildModulesInModule(parentName);
		if (parentName.equals("")) {
			drawArchitecture(getCurrentDrawingDetail());
		} else if (children.length > 0) {
			setCurrentPaths(new String[] { parentName });
			drawModulesAndLines(children);
		} else {
			logger.warn("Tried to draw modules for \"" + parentName + "\", but it has no children.");
		}
	}

	private void getAndDrawModulesIn(String[] parentNames) {
		HashMap<String, ArrayList<AbstractDTO>> allChildren = new HashMap<String, ArrayList<AbstractDTO>>();
		for (String parentName : parentNames) {
			AbstractDTO[] children = analyseService.getChildModulesInModule(parentName);
			if (parentName.equals("")) {
				drawArchitecture(getCurrentDrawingDetail());
				continue;
			} else if (children.length > 0) {
				ArrayList<AbstractDTO> knownChildren = new ArrayList<AbstractDTO>();
				for (AbstractDTO child : children) {
					knownChildren.add(child);
				}
				allChildren.put(parentName, knownChildren);
			} else {
				logger.warn("Tried to draw modules for \"" + parentName + "\", but it has no children.");
			}
		}
		setCurrentPaths(parentNames);

		Set<String> parentNamesKeySet = allChildren.keySet();
		if (parentNamesKeySet.size() == 1) {
			String onlyParentModule = parentNamesKeySet.iterator().next();
			ArrayList<AbstractDTO> onlyParentChildren = allChildren.get(onlyParentModule);
			drawModulesAndLines(onlyParentChildren.toArray(new AbstractDTO[] {}));
		} else {
			drawModulesAndLines(allChildren);
		}
	}

	@Override
	public void moduleOpen(String[] paths) {
		super.notifyServiceListeners();
		if (paths.length==0) {
			drawArchitecture(getCurrentDrawingDetail());
		} else {
			getAndDrawModulesIn(paths);
		}
	}
}
