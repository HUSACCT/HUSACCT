package husacct.graphics.task;

import org.jhotdraw.draw.ConnectionFigure;

import husacct.common.dto.*;
import husacct.graphics.presentation.decorators.Decorator;
import husacct.graphics.presentation.figures.BaseFigure;

public class DemoController extends BaseController
{
	
	public DemoController()
	{
		ModuleDTO presentationLayer = new ModuleDTO();
		presentationLayer.type = "layer";
		presentationLayer.logicalPath = "presentation";
		drawing.add(this.figureFactory.createFigure(presentationLayer));
		
		ModuleDTO taskLayer = new ModuleDTO();
		taskLayer.type = "layer";
		taskLayer.logicalPath = "task";
		drawing.add(this.figureFactory.createFigure(taskLayer));
		
		DependencyDTO presTaskDep1 = new DependencyDTO("presentation", "task", "call", 239);
		DependencyDTO presTaskDep2 = new DependencyDTO("presentation", "task", "import", 2);
		BaseFigure presTaskDepFigure = this.figureFactory.createFigure(
				new DependencyDTO[]{ presTaskDep1, presTaskDep2 });
		BaseFigure foundPresentationLayerFigure = drawing.findFigureByName("presentation");
		BaseFigure foundTaskLayerFigure = drawing.findFigureByName("task");
		this.connectionStrategy.connect(
				(ConnectionFigure)((Decorator)presTaskDepFigure).getDecorator(), 
				foundPresentationLayerFigure, 
				foundTaskLayerFigure);
		drawing.add(presTaskDepFigure);
		
		ModuleDTO infrastructureLayer = new ModuleDTO();
		infrastructureLayer.type = "layer";
		infrastructureLayer.logicalPath = "infrastructure";
		drawing.add(this.figureFactory.createFigure(infrastructureLayer));
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
