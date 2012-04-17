package husacct.graphics.task;

import husacct.graphics.presentation.figures.BaseFigure;


public class DefinedController extends BaseController
{
	public DefinedController() {
		super();
	}
	
	public void drawArchitecture(DrawingDetail detail) { 
	}

	@Override
	public void moduleZoom(BaseFigure selectedFigure) {
		System.out.println("Zooming in on " + selectedFigure.getName());
	}

	@Override
	public void moduleSelected(BaseFigure selectedFigure) {
		System.out.println("Selected module " + selectedFigure.getName());
	}	

}
