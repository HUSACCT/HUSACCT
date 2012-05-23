package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.graphics.presentation.figures.BaseFigure;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Locale;

import org.jhotdraw.draw.Figure;

public class DemoController extends DrawingController {
	
	public DemoController() {
		initializeDrawing();
	}

	private void initializeDrawing() {
		ArrayList<AbstractDTO> modules = new ArrayList<AbstractDTO>();

		ModuleDTO presentationLayer = new ModuleDTO();
		presentationLayer.type = "layer";
		presentationLayer.logicalPath = "presentation";
		modules.add(presentationLayer);
		
		ModuleDTO neuralInterfaceLayer = new ModuleDTO();
		neuralInterfaceLayer.type = "layer";
		neuralInterfaceLayer.logicalPath = "neural_interface";
		modules.add(neuralInterfaceLayer);		

		ModuleDTO taskLayer = new ModuleDTO();
		taskLayer.type = "layer";
		taskLayer.logicalPath = "task";
		modules.add(taskLayer);
		
		ModuleDTO infrastructureLayer = new ModuleDTO();
		infrastructureLayer.type = "layer";
		infrastructureLayer.logicalPath = "infrastructure";
		modules.add(infrastructureLayer);

		ModuleDTO domainLayer = new ModuleDTO();
		domainLayer.type = "layer";
		domainLayer.logicalPath = "domain";
		modules.add(domainLayer);
		
		domainLayer = new ModuleDTO();
		domainLayer.type = "layer";
		domainLayer.logicalPath = "domain_two";
		modules.add(domainLayer);		

		ModuleDTO testLayer = new ModuleDTO();
		testLayer.type = "layer";
		testLayer.logicalPath = "floating";
		modules.add(testLayer);
//		
//		ModuleDTO testClass = new ModuleDTO();
//		testClass.type = "class";
//		testClass.logicalPath = "*";
//		//modules.add(testClass);
//		
//		ModuleDTO testModule = new ModuleDTO();
//		testModule.type = "subsystem";
//		testModule.logicalPath = "myModule";
//		modules.add(testModule);
//		
//		ModuleDTO unrecognizableModuleTypeDTO = new ModuleDTO();
//		unrecognizableModuleTypeDTO.type = "foobar";
//		unrecognizableModuleTypeDTO.logicalPath = "tests";
//		modules.add(unrecognizableModuleTypeDTO);
//		
//		ModuleDTO component = new ModuleDTO();
//		component.type = "component";
//		component.logicalPath = "uml2component";
//		modules.add(component);

		AbstractDTO[] dtos = new AbstractDTO[modules.size()];
		dtos = modules.toArray(dtos);
		setPath(ROOT);
		drawModulesAndLines(dtos);
		
		printFigures("Constructor");
	}
	
	private boolean debugPrint = false;
	
	private void printFigures(String msg) {
		if (!debugPrint)
			return;
		
		System.out.println(msg);
		
		for (Figure f : drawing.getChildren()) {
			BaseFigure bf = (BaseFigure) f;
			Rectangle2D.Double bounds = bf.getBounds();
			
			String rect = String.format(Locale.US, "[x=%1.2f,y=%1.2f,w=%1.2f,h=%1.2f]", bounds.x, bounds.y, bounds.width, bounds.height);
			if (bf.getName().equals("floating"))
				System.out.println(String.format("%s: %s", bf.getName(), rect));
		}		
	}

	@Override
	public void refreshDrawing() {
		initializeDrawing();
	}

	@Override
	public void moduleZoom(BaseFigure[] zoomedModuleFigures) {
		printFigures("moduleZoom()");
		saveFigurePositions(getCurrentPath());
		BaseFigure zoomedFigure = zoomedModuleFigures[0];

		if (zoomedFigure.getName().equals("floating")) {
			ArrayList<AbstractDTO> modules = new ArrayList<AbstractDTO>();

			ModuleDTO child1 = new ModuleDTO();
			child1.type = "abstract";
			child1.logicalPath = "tests.test1";
			modules.add(child1);

			ModuleDTO child2 = new ModuleDTO();
			child2.type = "class";
			child2.logicalPath = "tests.test2";
			modules.add(child2);

			ModuleDTO child3 = new ModuleDTO();
			child3.type = "interface";
			child3.logicalPath = "tests.test3";
			modules.add(child3);

			AbstractDTO[] dtos = new AbstractDTO[modules.size()];
			dtos = modules.toArray(dtos);
			setPath("tests");
			this.drawModulesAndLines(dtos);
		}
	}

	@Override
	public void drawArchitecture(DrawingDetail detail) {

	}

	@Override
	public void moduleZoomOut() {
		printFigures("moduleZoomOut()");
		saveFigurePositions(getCurrentPath());
		setPath(ROOT);
		refreshDrawing();
	}

	@Override
	protected DependencyDTO[] getDependenciesBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		ArrayList<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();

		if (figureFrom.getName().equals("presentation") && figureTo.getName().equals("task")) {
			dependencies.add(new DependencyDTO("task", "presentation", "wa", 1));
		}
		if (figureFrom.getName().equals("presentation") && figureTo.getName().equals("extra_task")) {
			dependencies.add(new DependencyDTO("extra_task", "presentation", "wa", 1));
		}		
		if (figureFrom.getName().equals("presentation") && figureTo.getName().equals("task_two")) {
			dependencies.add(new DependencyDTO("task_two", "presentation", "wa", 1));
		}	
		if (figureFrom.getName().equals("neural_interface") && figureTo.getName().equals("domain")) {
			dependencies.add(new DependencyDTO("domain", "neural_interface", "wa", 1));
		}		

		if (figureFrom.getName().equals("task") && figureTo.getName().equals("domain")) {
			dependencies.add(new DependencyDTO("task", "domain", "wa", 1));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 2));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 3));
		}

		if (figureFrom.getName().equals("domain") && figureTo.getName().equals("infrastructure")) {
			dependencies.add(new DependencyDTO("task", "domain", "wa", 1));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 2));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 3));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 4));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 5));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 6));
		}
		
		if (figureFrom.getName().equals("infrastructure") && figureTo.getName().equals("domain")) {
			dependencies.add(new DependencyDTO("task", "domain", "wa", 1));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 2));
		}
		
		if (figureFrom.getName().equals("domain") && figureTo.getName().equals("domain_two")) {
			dependencies.add(new DependencyDTO("domain_two", "domain", "wa", 1));
		}

		if (figureFrom.getName().equals("infrastructure") && figureTo.getName().equals("test")) {
			dependencies.add(new DependencyDTO("task", "domain", "wa", 1));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 2));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 3));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 4));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 5));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 6));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 7));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 8));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 9));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 10));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 11));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 12));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 1));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 2));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 3));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 4));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 5));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 6));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 7));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 8));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 9));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 10));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 11));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 12));
		}

		return dependencies.toArray(new DependencyDTO[] {});
	}

	@Override
	protected ViolationDTO[] getViolationsBetween(BaseFigure figureFrom, BaseFigure figureTo) {
		ViolationDTO[] violations = new ViolationDTO[0];

		// From ValidateServiceStub.java
		ViolationTypeDTO constructorCall = new ViolationTypeDTO("InvocConstructor", "InvocConstructorDescription", false);
		ViolationTypeDTO extendingAbstractClass = new ViolationTypeDTO("Extends", "ExtendsDescription", false);
		ViolationTypeDTO implementationOfInterface = new ViolationTypeDTO("Implements", "ImplementsDescription", false);
		ViolationTypeDTO extendClass = new ViolationTypeDTO("Extends", "ExtendsDescription", false);
		RuleTypeDTO ruleType = new RuleTypeDTO("IsNotAllowedToUse", "IsNotAllowedToUseDescription", new ViolationTypeDTO[] { constructorCall, extendingAbstractClass, implementationOfInterface, extendClass }, new RuleTypeDTO[] {});

		if (figureFrom.getName().equals("domain") && figureTo.getName().equals("task")) {
			violations = new ViolationDTO[2];
			ViolationDTO taskLayerErr1 = new ViolationDTO("domain", "task", "domain", "task", extendClass, ruleType, "error 1", 1, Color.red, "", "", 3);
			violations[0] = taskLayerErr1;
			ViolationDTO taskLayerErr2 = new ViolationDTO("domain", "task", "domain", "task", extendClass, ruleType, "error 2", 1, Color.red, "", "", 3);
			violations[1] = taskLayerErr2;
		}
		
		if (figureFrom.getName().equals("task") && figureTo.getName().equals("domain")) {
			violations = new ViolationDTO[2];
			ViolationDTO taskLayerErr1 = new ViolationDTO("domain", "task", "domain", "task", extendClass, ruleType, "error 1", 1, Color.red, "", "", 3);
			violations[0] = taskLayerErr1;
			ViolationDTO taskLayerErr2 = new ViolationDTO("domain", "task", "domain", "task", extendClass, ruleType, "error 2", 1, Color.red, "", "", 3);
			violations[1] = taskLayerErr2;
		}

		if (figureFrom.getName().equals("task") && figureTo.getName().equals("task")) {
			return new ViolationDTO[] { new ViolationDTO("task", "task", "task", "task", extendClass, ruleType, "error 3", 1, Color.red, "", "", 3),
					new ViolationDTO("task", "task", "task", "task", extendClass, ruleType, "error 4", 1, Color.red, "", "", 3), new ViolationDTO("task", "task", "task", "task", extendClass, ruleType, "error 5", 1, Color.PINK, "", "", 99) };
		}

		if (figureFrom.getName().equals("presentation") && figureTo.getName().equals("test")) {
			violations = new ViolationDTO[] { new ViolationDTO("presentation", "test", "presentation", "test", extendClass, ruleType, "error 5", 1, Color.blue, "", "", 1),
					new ViolationDTO("presentation", "test", "presentation", "test", extendClass, ruleType, "error 6", 1, Color.orange, "", "", 2) };
		}

		return violations;
	}

	@Override
	public void moduleOpen(String path) {

	}

}
