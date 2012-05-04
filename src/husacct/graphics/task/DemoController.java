package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.presentation.figures.NamedFigure;

import java.util.ArrayList;

public class DemoController extends BaseController {

	private final int ITEMS_PER_ROW = 2;

	public DemoController() {

		AbstractDTO[] modules = new AbstractDTO[5];

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
		infrastructureLayer.logicalPath = "infrastructure";
		modules[2] = infrastructureLayer;

		ModuleDTO domainLayer = new ModuleDTO();
		domainLayer.type = "layer";
		domainLayer.logicalPath = "domain";
		modules[3] = domainLayer;

		ModuleDTO testLayer = new ModuleDTO();
		testLayer.type = "layer";
		testLayer.logicalPath = "test";
		modules[4] = testLayer;

		this.drawModules(modules);

		this.drawLinesBasedOnSetting();

		BasicLayoutStrategy bls = new BasicLayoutStrategy(drawing);
		bls.doLayout(ITEMS_PER_ROW);
	}

	@Override
	public void moduleZoom(BaseFigure[] zoomedModuleFigure) {
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
		NamedFigure figFrom = (NamedFigure) figureFrom;
		NamedFigure figTo = (NamedFigure) figureTo;

		ArrayList<DependencyDTO> dependencies = new ArrayList<DependencyDTO>();

		if (figFrom.getName().equals("presentation") && figTo.getName().equals("task")) {
			dependencies.add(new DependencyDTO("task", "presentation", "wa", 1));
		}

		if (figFrom.getName().equals("task") && figTo.getName().equals("domain")) {
			dependencies.add(new DependencyDTO("task", "domain", "wa", 1));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 2));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 3));
		}

		if (figFrom.getName().equals("domain") && figTo.getName().equals("infrastructure")) {
			dependencies.add(new DependencyDTO("task", "domain", "wa", 1));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 2));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 3));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 4));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 5));
			dependencies.add(new DependencyDTO("task", "domain", "wa", 6));
		}

		if (figFrom.getName().equals("infrastructure") && figTo.getName().equals("test")) {
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
		NamedFigure figFrom = (NamedFigure) figureFrom;
		NamedFigure figTo = (NamedFigure) figureTo;

		ViolationDTO[] violations = new ViolationDTO[0];

		// From ValidateServiceStub.java
		ViolationTypeDTO constructorCall = new ViolationTypeDTO("InvocConstructor", "InvocConstructorDescription",
				false);
		ViolationTypeDTO extendingAbstractClass = new ViolationTypeDTO("Extends", "ExtendsDescription", false);
		ViolationTypeDTO implementationOfInterface = new ViolationTypeDTO("Implements", "ImplementsDescription", false);
		ViolationTypeDTO extendClass = new ViolationTypeDTO("Extends", "ExtendsDescription", false);
		RuleTypeDTO ruleType = new RuleTypeDTO("IsNotAllowedToUse", "IsNotAllowedToUseDescription",
				new ViolationTypeDTO[] { constructorCall, extendingAbstractClass, implementationOfInterface,
						extendClass }, new RuleTypeDTO[] {});

		if (figFrom.getName().equals("domain") && figTo.getName().equals("task")) {
			violations = new ViolationDTO[2];
			ViolationDTO taskLayerErr1 = new ViolationDTO("domain", "task", "domain", "task", extendClass, ruleType,
					"error 1", 1);
			violations[0] = taskLayerErr1;
			ViolationDTO taskLayerErr2 = new ViolationDTO("domain", "task", "domain", "task", extendClass, ruleType,
					"error 2", 1);
			violations[1] = taskLayerErr2;
		}

		if (figFrom.getName().equals("task") && figTo.getName().equals("task")) {
			violations = new ViolationDTO[2];
			ViolationDTO taskLayerErr1 = new ViolationDTO("task", "task", "task", "task", extendClass, ruleType,
					"error 3", 1);
			violations[0] = taskLayerErr1;
			ViolationDTO taskLayerErr2 = new ViolationDTO("task", "task", "task", "task", extendClass, ruleType,
					"error 4", 1);
			violations[1] = taskLayerErr2;
		}

		return violations;
	}

}
