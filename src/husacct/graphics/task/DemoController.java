package husacct.graphics.task;

import javax.swing.JTable;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.graphics.presentation.decorators.DTODecorator;
import husacct.graphics.presentation.decorators.Decorator;
import husacct.graphics.presentation.figures.BaseFigure;

import org.jhotdraw.draw.ConnectionFigure;

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
		ViolationDTO taskLayerErr1 = new ViolationDTO(null, null, "error1", null, null, null, null);
		ViolationDTO taskLayerErr2 = new ViolationDTO(null, null, "error2", null, null, null, null);
		drawing.add(this.figureFactory.createFigure(taskLayer, new ViolationDTO[]{ taskLayerErr1, taskLayerErr2 }));
		
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
		infrastructureLayer.logicalPath = "Infrastructure layer";
		drawing.add(this.figureFactory.createFigure(infrastructureLayer));
		
		ModuleDTO domainLayer = new ModuleDTO();
		domainLayer.type = "layer";
		domainLayer.logicalPath = "Domain layer";
		drawing.add(this.figureFactory.createFigure(domainLayer));
		
		this.drawViolationsForShownModules();
	}

	@Override
	public void moduleZoom(BaseFigure zoomedModuleFigure)
	{
		// do recursion, because the dtodecorator might be hidden behind other decorators
		if(zoomedModuleFigure instanceof Decorator) {
			this.moduleZoom(((Decorator) zoomedModuleFigure).getDecorator());
		}
		
		if(zoomedModuleFigure instanceof DTODecorator) {
			AbstractDTO aDto = ((DTODecorator)zoomedModuleFigure).getDTO();
			if(aDto instanceof ModuleDTO) {
				ModuleDTO dto = (ModuleDTO)aDto;
				if(!dto.logicalPath.equals("task")) {
					throw new RuntimeException("we only support zooming on the task layer in this demo");
				}
				
				this.clearDrawing();
			}
		}
	}

	@Override
	public void drawArchitecture(DrawingDetail detail) {
		// TODO Auto-generated method stub

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
