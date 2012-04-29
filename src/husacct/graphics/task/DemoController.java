package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ViolationDTO;
import husacct.graphics.presentation.decorators.Decorator;
import husacct.graphics.presentation.figures.BaseFigure;

import org.apache.log4j.Logger;
import org.jhotdraw.draw.ConnectionFigure;

public class DemoController extends BaseController {
	
	private Logger logger = Logger.getLogger(DemoController.class);

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
		this.drawViolationsForShownModules();

		BasicLayoutStrategy bls = new BasicLayoutStrategy(drawing);
		bls.doLayout(ITEMS_PER_ROW);
	}
	
	// Dependencies
	
	private void drawDependenciesForShownModules(){
		for (BaseFigure figureFrom : this.drawing.getShownModules()) {
			for (BaseFigure figureTo : this.drawing.getShownModules()) {
				getAndDrawDependencyBetween(figureFrom, figureTo);
			}
		}
	}
	
	private void getAndDrawDependencyBetween(BaseFigure figureFrom, BaseFigure figureTo){
		ModuleDTO dtoFrom = (ModuleDTO) this.getDTOFromFigure(figureFrom);
		ModuleDTO dtoTo = (ModuleDTO) this.getDTOFromFigure(figureTo);
		
		DependencyDTO[] dependencies = getDependenciesBetween(dtoFrom.logicalPath, dtoTo.logicalPath);
		
		try{
			BaseFigure dependencyFigure = this.figureFactory.createFigure(dependencies);
			this.connectionStrategy.connect((ConnectionFigure) ((Decorator) dependencyFigure).getDecorator(), figureFrom, figureTo);
			drawing.add(dependencyFigure);
		} catch (RuntimeException e) {
			logger.debug(e.getMessage() + " " + dtoFrom.logicalPath + " -> " + dtoTo.logicalPath);
		}
	}
	
	protected DependencyDTO[] getDependenciesBetween(String from, String to) {
		DependencyDTO[] dependencies = new DependencyDTO[0];
		if(from=="task"  && to=="presentation"){
			dependencies = new DependencyDTO[1];
			DependencyDTO taskLayerDep1 = new DependencyDTO("task","presentation","wa",1);
			dependencies[0] = taskLayerDep1;
		}
		return dependencies;
	}

	// Violations
	
	public void drawViolationsForShownModules() {
		for (BaseFigure figureFrom : this.drawing.getShownModules()) {
			for (BaseFigure figureTo : this.drawing.getShownModules()) {
				getAndDrawViolationBetween(figureFrom, figureTo);
			}
		}
	}
	
	private void getAndDrawViolationBetween(BaseFigure figureFrom, BaseFigure figureTo){
		ModuleDTO dtoFrom = (ModuleDTO) this.getDTOFromFigure(figureFrom);
		ModuleDTO dtoTo = (ModuleDTO) this.getDTOFromFigure(figureTo);
		
		try{
			ViolationDTO[] dependencies = getViolationsBetween(dtoFrom.logicalPath, dtoTo.logicalPath);
			BaseFigure violationFigure = this.figureFactory.createFigure(dependencies);
			this.connectionStrategy.connect((ConnectionFigure) ((Decorator) violationFigure).getDecorator(), figureFrom, figureTo);
			drawing.add(violationFigure);
		} catch (RuntimeException e) {
			logger.debug(e.getMessage() + " " + dtoFrom.logicalPath + " -> " + dtoTo.logicalPath);
		}
	}
	
	protected ViolationDTO[] getViolationsBetween(String from, String to) {
		ViolationDTO[] violations = new ViolationDTO[0];
		if(from=="Domain layer"  && to=="Infrastructure layer"){
			violations = new ViolationDTO[2];
			ViolationDTO taskLayerErr1 = new ViolationDTO(null, null, "error1", null, null, null, null);
			violations[0] = taskLayerErr1;
			ViolationDTO taskLayerErr2 = new ViolationDTO(null, null, "error2", null, null, null, null);
			violations[1] = taskLayerErr2;
		}
		return violations;
	}
	
	@Override
	public void moduleZoom(BaseFigure zoomedModuleFigure) {
		// do recursion, because the dtodecorator might be hidden behind other
		// decorators
		if (zoomedModuleFigure instanceof Decorator) {
			this.moduleZoom(((Decorator) zoomedModuleFigure).getDecorator());
		}

		if (zoomedModuleFigure.isModule()) {
			AbstractDTO aDto = this.getDTOFromFigure(zoomedModuleFigure);
			if (aDto instanceof ModuleDTO) {
				ModuleDTO dto = (ModuleDTO) aDto;
				if (!dto.logicalPath.equals("task")) {
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
