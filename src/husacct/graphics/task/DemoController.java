package husacct.graphics.task;

import husacct.common.dto.*;

public class DemoController extends BaseController
{
	
	public DemoController()
	{
		ModuleDTO presentationLayer = new ModuleDTO();
		presentationLayer.type = "module";
		presentationLayer.logicalPath = "presentation";
		
		drawing.add(this.figureFactory.createFigure(presentationLayer));
	}

	@Override
	public void moduleZoom(AbstractDTO zoomedModuleDTO) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawArchitecture(DrawingDetail detail) {
		// TODO Auto-generated method stub

	}

}
