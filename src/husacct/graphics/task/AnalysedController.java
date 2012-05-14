package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.validate.IValidateService;

import java.util.Locale;

import org.apache.log4j.Logger;

public class AnalysedController extends DrawingController {
	private IControlService controlService;
	protected IAnalyseService analyseService;
	protected IValidateService validateService;
	private Logger logger = Logger.getLogger(AnalysedController.class);

	public AnalysedController() {
		super();
		initializeServices();
		
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				refreshDrawing();
			}
		});		
	}
	
	private void initializeServices() {
		controlService = ServiceProvider.getInstance().getControlService();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
	}
	
	@Override
	public void refreshDrawing() {
		getAndDrawModulesIn(getCurrentPath());
	}
	
	public void showViolations(){
		super.showViolations();
		try{
			validateService.checkConformance();
		}catch(NullPointerException e){
			logger.warn("NullPointerException, I think the validate service isn't started.");
		}
	}

	public void drawArchitecture(DrawingDetail detail) {
		AbstractDTO[] modules = analyseService.getRootModules();
		resetCurrentPath();
		drawModules(modules);

		if (DrawingDetail.WITH_VIOLATIONS == detail) {
			showViolations();
		}
		drawLinesBasedOnSetting();
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) figureMap.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) figureMap.getModuleDTO(figureTo);
		if(!figureFrom.equals(figureTo)){
			return analyseService.getDependencies(dtoFrom.uniqueName, dtoTo.uniqueName);
		}
		return new DependencyDTO[]{};
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
		BaseFigure figure = figures[0];

		if (figure.isModule()) {
			try {
				AnalysedModuleDTO parentDTO = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figure);
				getAndDrawModulesIn(parentDTO.uniqueName);
			} catch (Exception e) {
				logger.debug("Could not zoom on this object: " + figure);
				logger.debug("Possible type cast failure.");
			}
		}
	}

	@Override
	public void moduleZoomOut() {
		AnalysedModuleDTO parentDTO = analyseService.getParentModuleForModule(getCurrentPath());
		if (null != parentDTO) {
			getAndDrawModulesIn(parentDTO.uniqueName);
		} else {
			logger.debug("Tried to zoom out from " + getCurrentPath() + ", but it has no parent.");
			logger.debug("Reverting to the root of the application.");
			drawArchitecture(getCurrentDrawingDetail());
		}
	}

	private void getAndDrawModulesIn(String parentName) {
		AnalysedModuleDTO[] children = analyseService.getChildModulesInModule(parentName);
		if (children.length > 0) {
			setCurrentPath(parentName);
			drawModules(children);
			drawLinesBasedOnSetting();
		} else {
			logger.debug("Tried to draw modules for " + parentName + ", but it has no children.");
		}
	}

	public void moduleOpen(String path) {
		getAndDrawModulesIn(path);
	}
}
