package husacct.graphics.task;

import husacct.ServiceProvider;
import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ModuleDTO;
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
	public void moduleZoom(BaseFigure zoomedModuleFigure) {
		// TODO
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
	public void toggleViolations() {
		// TODO Auto-generated method stub
		
	}

}
