package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ViolationDTO;
import husacct.define.IDefineService;
import husacct.graphics.presentation.figures.BaseFigure;

public class DefinedController extends BaseController {
	private IDefineService defineService;

	public DefinedController() {
		super();

		defineService = ServiceProvider.getInstance().getDefineService();
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
		// TODO implement zooming here by retrieving the parent from defined service
	}

	@Override
	public void moduleZoomOut() {
		// TODO Auto-generated method stub

	}

	@Override
	public void exportToImage() {
		// TODO Auto-generated method stub

	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		// TODO Auto-generated method stub
		return null;
	}

}
