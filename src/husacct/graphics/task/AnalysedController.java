package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.graphics.presentation.figures.BaseFigure;

import java.util.Locale;

import org.apache.log4j.Logger;

public class AnalysedController extends BaseController {

	private final int ITEMS_PER_ROW = 4;
	
	private IControlService controlService;
	private Logger logger = Logger.getLogger(AnalysedController.class);

	public AnalysedController() {
		super();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		controlService = ServiceProvider.getInstance().getControlService();

		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				getAndDrawModulesIn(getCurrentPath());
				if(violationsAreShown()){
					drawViolationsForShownModules();
				}
			}
		});
	}

	public void drawArchitecture(DrawingDetail detail) {
		AbstractDTO[] modules = analyseService.getRootModules();
		this.resetCurrentPath();
		this.drawModules(modules);
		
		if(detail == DrawingDetail.WITH_VIOLATIONS){
			this.showViolations();
		}
		this.drawLinesBasedOnSetting();
	}

	protected void drawModules(AbstractDTO[] modules) {
		super.drawModules(modules);
		layoutStrategy.doLayout(ITEMS_PER_ROW);
	}
	
	// Dependencies
	
	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figureTo);
		
		return analyseService.getDependencies(dtoFrom.uniqueName, dtoTo.uniqueName);
	}
	
	// violations
	
	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figureTo);
		
		return validateService.getViolationsByPhysicalPath(dtoFrom.uniqueName, dtoTo.uniqueName);
	}
	
	// Listener methods

	@Override
	public void moduleZoom(BaseFigure figure) {
		if (figure.isModule()) { //FIXME? : Can zoom only on modules
			AbstractDTO dto = this.figureMap.getModuleDTO(figure);
	
			if (dto.getClass().getSimpleName().equals("AnalysedModuleDTO")) {
				AnalysedModuleDTO parentDTO = ((AnalysedModuleDTO) dto);
				getAndDrawModulesIn(parentDTO.uniqueName);
			}
		}
	}

	@Override
	public void moduleZoomOut() {
		AnalysedModuleDTO parentDTO = analyseService.getParentModuleForModule(this.getCurrentPath());
		if (null != parentDTO) {
			this.getAndDrawModulesIn(parentDTO.uniqueName);
		} else {
			logger.debug("Tried to zoom out from " + this.getCurrentPath() + ", but it has no parent.");
			logger.debug("Reverting to the root of the application.");
			drawArchitecture(DrawingDetail.WITHOUT_VIOLATIONS);
		}
	}

	private void getAndDrawModulesIn(String parentName) {
		this.setCurrentPath(parentName);
		AnalysedModuleDTO[] children = analyseService.getChildModulesInModule(parentName);
		if (children.length > 0) {
			this.drawModules(children);
			this.drawLinesBasedOnSetting();
		} else {
			logger.debug("Tried to draw modules for " + parentName + ", but it has no children.");
		}
	}
}
