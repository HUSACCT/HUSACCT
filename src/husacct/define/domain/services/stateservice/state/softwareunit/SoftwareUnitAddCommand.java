package husacct.define.domain.services.stateservice.state.softwareunit;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;

import antlr.collections.List;

public class SoftwareUnitAddCommand implements Istate {
	private ModuleStrategy module;
	private ArrayList<AnalyzedModuleComponent> units;
	
	
	public SoftwareUnitAddCommand(ModuleStrategy module,
			ArrayList<AnalyzedModuleComponent> unitTobeRemoved) {
		this.module=module;
		this.units=unitTobeRemoved;
	}

	@Override
	public void undo() {
	SoftwareUnitDefinitionDomainService service = new SoftwareUnitDefinitionDomainService();
	for (AnalyzedModuleComponent unit : units) {
		service.removeSoftwareUnit(module.getId(), units);
	}
	
		
	}

	@Override
	public void redo() {
		DefinitionController.getInstance().setSelectedModuleId(module.getId());
		SoftwareUnitController controller = new SoftwareUnitController(module.getId());
		controller.save(units);
		
	}

}
