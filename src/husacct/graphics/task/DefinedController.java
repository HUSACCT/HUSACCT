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
import husacct.validate.IValidateService;

import java.util.ArrayList;

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
		AbstractDTO[] modules = defineService.getRootModules();
		setPath(ROOT);
		if (DrawingDetail.WITH_VIOLATIONS == detail) {
			showViolations();
		}
		drawModulesAndLines(modules);
	}

	@Override
	public void moduleZoom(BaseFigure[] figures) {
		super.notifyServiceListeners();
		// FIXME: Make this code function with the multiple selected figures
		BaseFigure figure = figures[0];

		if (figure.isModule()) {
			try {
				ModuleDTO parentDTO = (ModuleDTO) this.figureMap.getModuleDTO(figure);
				getAndDrawModulesIn(parentDTO.logicalPath);
			} catch (Exception e) {
				logger.warn("Could not zoom on this object: " + figure);
				logger.debug("Possible type cast failure.");
			}
		}
	}

	@Override
	public void moduleZoomOut() {
		super.notifyServiceListeners();
		String parentPath = defineService.getParentFromModule(getCurrentPath());
		if (null != parentPath) {
			getAndDrawModulesIn(parentPath);
		} else {
			logger.warn("Tried to zoom out from \"" + getCurrentPath() + "\", but it has no parent (could be root if it's an empty string).");
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
				setPath(parentName);
				drawModulesAndLines(children);
			} else {
				logger.warn("Tried to draw modules for " + parentName + ", but it has no children.");
			}
		}

	}

	@Override
	public void moduleOpen(String path) {
		super.notifyServiceListeners();
		if (path.isEmpty()) {
			drawArchitecture(getCurrentDrawingDetail());
		} else {
			getAndDrawModulesIn(path);
		}
	}
}
