package husacct.graphics.task;

import java.util.Locale;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.define.IDefineService;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.validate.IValidateService;

public class DefinedController extends DrawingController {
	private IControlService controlService;
	protected IAnalyseService analyseService;
	protected IDefineService defineService;
	protected IValidateService validateService;

	public DefinedController() {
		super();

		initializeServices();

		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {

			}
		});
	}

	private void initializeServices() {
		controlService = ServiceProvider.getInstance().getControlService();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		defineService = ServiceProvider.getInstance().getDefineService();
	}
	
	public void showViolations(){
		super.showViolations();
		validateService.checkConformance();
	}

	public void drawArchitecture(DrawingDetail detail) {
		AbstractDTO[] modules = defineService.getRootModules();
		drawModules(modules);
	}

	protected void drawModules(AbstractDTO[] modules) {
		super.drawModules(modules);
	}

	@Override
	public void moduleZoom(BaseFigure[] zoomedModuleFigure) {
	}

	@Override
	public void moduleZoomOut() {

	}

	@Override
	public void exportToImage() {

	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		return null;
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		return null;
	}
}
