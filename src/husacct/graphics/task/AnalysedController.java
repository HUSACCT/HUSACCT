package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
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
				if (violationsAreShown()) {
					drawViolationsForShownModules();
				}
			}
		});
	}

	public void drawArchitecture(DrawingDetail detail) {
		AbstractDTO[] modules = analyseService.getRootModules();
		this.resetCurrentPath();
		this.drawModules(modules);

		if (DrawingDetail.WITH_VIOLATIONS == detail) {
			this.showViolations();
		}
		this.drawLinesBasedOnSetting();
	}

	protected void drawModules(AbstractDTO[] modules) {
		super.drawModules(modules);
		layoutStrategy.doLayout(ITEMS_PER_ROW);
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figureTo);

		return analyseService.getDependencies(dtoFrom.uniqueName, dtoTo.uniqueName);
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		AnalysedModuleDTO dtoFrom = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figureFrom);
		AnalysedModuleDTO dtoTo = (AnalysedModuleDTO) this.figureMap.getModuleDTO(figureTo);

		// return
		// validateService.getViolationsByPhysicalPath(dtoFrom.uniqueName,
		// dtoTo.uniqueName);

		// TODO validation service probably has to actually run through the main
		// GUI to have this to work.
		// REMOVE ME WHEN STUFF WORKS!
		// From ValidateServiceStub.java
		ViolationDTO[] violations = new ViolationDTO[0];
		ViolationTypeDTO constructorCall = new ViolationTypeDTO("InvocConstructor", "InvocConstructorDescription",
				false);
		ViolationTypeDTO extendingAbstractClass = new ViolationTypeDTO("Extends", "ExtendsDescription", false);
		ViolationTypeDTO implementationOfInterface = new ViolationTypeDTO("Implements", "ImplementsDescription", false);
		ViolationTypeDTO extendClass = new ViolationTypeDTO("Extends", "ExtendsDescription", false);
		RuleTypeDTO ruleType = new RuleTypeDTO("IsNotAllowedToUse", "IsNotAllowedToUseDescription",
				new ViolationTypeDTO[] { constructorCall, extendingAbstractClass, implementationOfInterface,
						extendClass }, new RuleTypeDTO[] {});

		if (dtoFrom.uniqueName.equals("domain") && dtoTo.uniqueName.equals("infrastructure")) {
			violations = new ViolationDTO[2];
			ViolationDTO taskLayerErr1 = new ViolationDTO("domain", "infrastructure", "domain", "infrastructure",
					extendClass, ruleType, "error 1", 1);
			violations[0] = taskLayerErr1;
			ViolationDTO taskLayerErr2 = new ViolationDTO("domain", "infrastructure", "domain", "infrastructure",
					extendClass, ruleType, "error 2", 1);
			violations[1] = taskLayerErr2;
		}

		if (dtoFrom.uniqueName.equals("infrastructure") && dtoTo.uniqueName.equals("infrastructure")) {
			violations = new ViolationDTO[2];
			ViolationDTO taskLayerErr1 = new ViolationDTO("infrastructure", "infrastructure", "infrastructure",
					"infrastructure", extendClass, ruleType, "error 3", 1);
			violations[0] = taskLayerErr1;
			ViolationDTO taskLayerErr2 = new ViolationDTO("infrastructure", "infrastructure", "infrastructure",
					"infrastructure", extendClass, ruleType, "error 4", 1);
			violations[1] = taskLayerErr2;
		}

		return violations;
	}

	// Listener methods

	@Override
	public void moduleZoom(BaseFigure[] figures) {
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
		AnalysedModuleDTO parentDTO = analyseService.getParentModuleForModule(this.getCurrentPath());
		if (null != parentDTO) {
			this.getAndDrawModulesIn(parentDTO.uniqueName);
		} else {
			logger.debug("Tried to zoom out from " + this.getCurrentPath() + ", but it has no parent.");
			logger.debug("Reverting to the root of the application.");
			drawArchitecture(getCurrentDrawingDetail());
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
