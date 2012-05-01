package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.NamedFigure;

import org.apache.log4j.Logger;

public class DemoController extends BaseController {

	private final int ITEMS_PER_ROW = 2;
	
	public DemoController() {
		
		AbstractDTO[] modules = new AbstractDTO[4];
		
		ModuleDTO presentationLayer = new ModuleDTO();
		presentationLayer.type = "layer";
		presentationLayer.logicalPath = "presentation";
		modules[0] = presentationLayer;

		ModuleDTO taskLayer = new ModuleDTO();
		taskLayer.type = "layer";
		taskLayer.logicalPath = "task";
		modules[1] = taskLayer;
		
		ModuleDTO infrastructureLayer = new ModuleDTO();
		infrastructureLayer.type = "layer";
		infrastructureLayer.logicalPath = "Infrastructure layer";
		modules[2] = infrastructureLayer;

		ModuleDTO domainLayer = new ModuleDTO();
		domainLayer.type = "layer";
		domainLayer.logicalPath = "Domain layer";
		modules[3] = domainLayer;
		
		this.drawModules(modules);

		this.drawDependenciesForShownModules();

		BasicLayoutStrategy bls = new BasicLayoutStrategy(drawing);
		bls.doLayout(ITEMS_PER_ROW);
	}
	
	@Override
	public void moduleZoom(BaseFigure zoomedModuleFigure) {
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
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		NamedFigure figFrom = (NamedFigure)figureFrom;
		NamedFigure figTo = (NamedFigure)figureTo;
		
		DependencyDTO[] dependencies = new DependencyDTO[0];
		
		if(figFrom.getName().equals("task") && figTo.getName().equals("presentation")){
			dependencies = new DependencyDTO[1];
			DependencyDTO taskLayerDep1 = new DependencyDTO("task","presentation","wa",1);
			dependencies[0] = taskLayerDep1;
		}
		
		return dependencies;
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		NamedFigure figFrom = (NamedFigure)figureFrom;
		NamedFigure figTo = (NamedFigure)figureTo;
		
		ViolationDTO[] violations = new ViolationDTO[0];
		
		if(figFrom.getName().equals("Domain layer") && figTo.getName().equals("Infrastructure layer")) {
			violations = new ViolationDTO[2];
			ViolationDTO taskLayerErr1 = new ViolationDTO(null, null, "error1", null, null, null, null);
			violations[0] = taskLayerErr1;
			ViolationDTO taskLayerErr2 = new ViolationDTO(null, null, "error2", null, null, null, null);
			violations[1] = taskLayerErr2;
		}
		
		
		if(figFrom.getName().equals("task") && figTo.getName().equals("task")) {
			violations = new ViolationDTO[2];
			ViolationDTO taskLayerErr1 = new ViolationDTO(null, null, "error1", null, null, null, null);
			violations[0] = taskLayerErr1;
			ViolationDTO taskLayerErr2 = new ViolationDTO(null, null, "error2", null, null, null, null);
			violations[1] = taskLayerErr2;			
		}
		
		return violations;
	}

}
