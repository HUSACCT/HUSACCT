package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;
import husacct.define.IDefineService;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.validate.IValidateService;

import java.util.ArrayList;
import java.util.Locale;

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
				refreshDrawing();
			}
		});
	}

	private void initializeServices() {
		controlService = ServiceProvider.getInstance().getControlService();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		defineService = ServiceProvider.getInstance().getDefineService();
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
		AbstractDTO[] modules = defineService.getRootModules();
		resetCurrentPath();
		drawModules(modules);

		if (DrawingDetail.WITH_VIOLATIONS == detail) {
			showViolations();
		}
		drawLinesBasedOnSetting();
	}

	@Override
	public void moduleZoom(BaseFigure[] figures) {
		// FIXME: Make this code function with the multiple selected figures
		BaseFigure figure = figures[0];

		if (figure.isModule()) {
			try {
				ModuleDTO parentDTO = (ModuleDTO) this.figureMap.getModuleDTO(figure);
				getAndDrawModulesIn(parentDTO.logicalPath);
			} catch (Exception e) {
				logger.debug("Could not zoom on this object: " + figure);
				logger.debug("Possible type cast failure.");
			}
		}
	}

	@Override
	public void moduleZoomOut() {
		String parentPath = defineService.getParentFromModule(getCurrentPath());
		if (null != parentPath) {
			getAndDrawModulesIn(parentPath);
		} else {
			logger.debug("Tried to zoom out from " + getCurrentPath() + ", but it has no parent.");
			logger.debug("Reverting to the root of the application.");
			drawArchitecture(getCurrentDrawingDetail());
		}
	}
	
	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		ModuleDTO dtoFrom = (ModuleDTO) figureMap.getModuleDTO(figureFrom);
		ModuleDTO dtoTo = (ModuleDTO) figureMap.getModuleDTO(figureTo);
		ArrayList<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();
		for(String physicalFromPath : dtoFrom.physicalPaths){
			for(String physicalToPath : dtoTo.physicalPaths){
				DependencyDTO[] foundDependencies = analyseService.getDependencies(physicalFromPath,physicalToPath);
				for(DependencyDTO tempDependency : foundDependencies){
					dependencies.add(tempDependency);
				}
			}
		}
		return dependencies.toArray(new DependencyDTO[]{});
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		ModuleDTO dtoFrom = (ModuleDTO) figureMap.getModuleDTO(figureFrom);
		ModuleDTO dtoTo = (ModuleDTO) figureMap.getModuleDTO(figureTo);
		return validateService.getViolationsByLogicalPath(dtoFrom.logicalPath, dtoTo.logicalPath);
	}
	
	private void getAndDrawModulesIn(String parentName) {
		try{
			setCurrentPath(parentName);
			ModuleDTO[] children = defineService.getChildsFromModule(parentName);
			if (children.length > 0) {
				drawModules(children);
				drawLinesBasedOnSetting();
			} else {
				logger.debug("Tried to draw modules for " + parentName + ", but it has no children.");
			}
		}catch(NullPointerException e){
			logger.warn("NullPointerException, I think the define service isn't started.");
		}
	}

}
