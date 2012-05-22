package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.services.IServiceListener;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.validate.IValidateService;

import java.util.ArrayList;
import java.util.HashMap;

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
//	    TODO: Uncomment wanneer analyse addServiceListener heeft ge�mplementeerd!
//	    ServiceProvider.getInstance().getAnalyseService().addServiceListener(new IServiceListener(){
//	        @Override
//			public void update() {
//				refreshDrawing();				
//			}
//	    });
	}

	@Override
	public void refreshDrawing() {
		super.notifyServiceListeners();
		getAndDrawModulesIn(getCurrentPath());
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
		resetCurrentPath();
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
		// FIXME: Make this code function with the multiple selected figures
		ArrayList<String> parentNames = new ArrayList<String>();
		for(BaseFigure figure : figures){
			if (figure.isModule()) {
				try {
					AnalysedModuleDTO parentDTO = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figure);
//					getAndDrawModulesIn(parentDTO.uniqueName);
					parentNames.add(parentDTO.uniqueName);
				} catch (Exception e) {
					e.printStackTrace();
					logger.warn("Could not zoom on this object: " + figure);
					logger.debug("Possible type cast failure.");
				}
			}
		}
		getAndDrawModulesIn(parentNames.toArray(new String[]{}));
	}

	@Override
	public void moduleZoomOut() {
		super.notifyServiceListeners();
		AnalysedModuleDTO parentDTO = analyseService.getParentModuleForModule(getCurrentPath());
		if (null != parentDTO) {
			getAndDrawModulesIn(parentDTO.uniqueName);
		} else {
			logger.warn("Tried to zoom out from \"" + getCurrentPath() + "\", but it has no parent (could be root if it's an empty string).");
			logger.debug("Reverting to the root of the application.");
			drawArchitecture(getCurrentDrawingDetail());
		}
	}

	private void getAndDrawModulesIn(String parentName) {
		AnalysedModuleDTO[] children = analyseService.getChildModulesInModule(parentName);
		if (parentName.equals("")) {
			drawArchitecture(getCurrentDrawingDetail());
		} else if (children.length > 0) {
			setCurrentPath(parentName);
			drawModulesAndLines(children);
		} else {
			logger.warn("Tried to draw modules for " + parentName + ", but it has no children.");
		}
	}
	
	private void getAndDrawModulesIn(String[] parentNames) {
		HashMap<String, ArrayList<AbstractDTO>> allChildren = new HashMap<String, ArrayList<AbstractDTO>>(); 
		for(String parentName : parentNames){
			AbstractDTO[] children = analyseService.getChildModulesInModule(parentName);
			if (parentName.equals("")) {
				drawArchitecture(getCurrentDrawingDetail());
			} else if (children.length > 0) {
//				setCurrentPath(parentName);
				ArrayList<AbstractDTO> knownChildren = new ArrayList<AbstractDTO>();
				for(AbstractDTO child : children){
					knownChildren.add(child);
				}
				allChildren.put(parentName, knownChildren);
			} else {
				logger.warn("Tried to draw modules for " + parentName + ", but it has no children.");
			}
		}
		drawModulesAndLines(allChildren);
	}

	@Override
	public void moduleOpen(String path) {
		super.notifyServiceListeners();
		if(path.isEmpty()){
			drawArchitecture(getCurrentDrawingDetail());
		}else{
			getAndDrawModulesIn(path);
		}
	}
}
