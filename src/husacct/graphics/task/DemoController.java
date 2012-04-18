package husacct.graphics.task;

import husacct.common.dto.*;
import husacct.graphics.presentation.figures.BaseFigure;

public class DemoController extends BaseController
{
	
	public DemoController()
	{
		ModuleDTO presentationLayer = new ModuleDTO();
		presentationLayer.type = "layer";
		presentationLayer.logicalPath = "presentation";
		
		drawing.add(this.figureFactory.createFigure(presentationLayer));
	}

	@Override
	public void moduleZoom(BaseFigure zoomedModuleFigure) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawArchitecture(DrawingDetail detail) {
		// TODO Auto-generated method stub

	}

}
