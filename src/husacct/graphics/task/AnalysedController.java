package husacct.graphics.task;

import java.util.ArrayList;
import java.util.HashMap;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.validate.IValidateService;
import husacct.graphics.presentation.figures.NamedFigure;

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
	}

	@Override
	public void refreshDrawing() {
		getAndDrawModulesIn(getCurrentPath());
	}

	@Override
	public void showViolations() {
		super.showViolations();
		validateService.checkConformance();
	}

	@Override
	public void drawArchitecture(DrawingDetail detail) {
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
			System.out.println("--------------------====---------------");
			System.out.println(((NamedFigure) figureFrom).getName());
			System.out.println(((NamedFigure) figureTo).getName());
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
		AnalysedModuleDTO parentDTO = analyseService.getParentModuleForModule(getCurrentPath());
		if (null != parentDTO) {
			getAndDrawModulesIn(parentDTO.uniqueName);
		} else {
			logger.warn("Tried to zoom out from " + getCurrentPath() + ", but it has no parent.");
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
		HashMap<String, ArrayList<AnalysedModuleDTO>> allChildren = new HashMap<String, ArrayList<AnalysedModuleDTO>>(); 
		for(String parentName : parentNames){
			AnalysedModuleDTO[] children = analyseService.getChildModulesInModule(parentName);
			if (parentName.equals("")) {
				drawArchitecture(getCurrentDrawingDetail());
			} else if (children.length > 0) {
//				setCurrentPath(parentName);
				ArrayList<AnalysedModuleDTO> knownChildren = new ArrayList<AnalysedModuleDTO>();
				for(AnalysedModuleDTO child : children){
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
		getAndDrawModulesIn(path);
	}
}
