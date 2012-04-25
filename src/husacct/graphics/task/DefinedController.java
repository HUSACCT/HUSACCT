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

	private void drawModules(AbstractDTO[] modules) {
		ModuleDTO[] castedModules = (ModuleDTO[]) modules;

		for (ModuleDTO dto : castedModules) {
			BaseFigure logicalFigure = figureFactory.createFigure(dto);
			drawing.add(logicalFigure);
		}
	}

	@Override
	public void moduleZoom(BaseFigure zoomedModuleFigure) {
		// TODO
	}

	@Override
	public void moduleZoomOut() {
		// TODO Auto-generated method stub
		
	}

}
