package husacct.define.domain.services.stateservice.state.softwareunit;

import java.util.ArrayList;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

public class SoftwareUnitRemoveCommand implements Istate {

	private ModuleStrategy module;
 private	ArrayList<AnalyzedModuleComponent> data;
	
	public SoftwareUnitRemoveCommand() {
		// TODO Auto-generated constructor stub
	}
	
	public SoftwareUnitRemoveCommand(ModuleStrategy module,
			ArrayList<AnalyzedModuleComponent> data) {
		this.data=data;
		this.module=module;
	}

	@Override
	public void undo() {
	 DefinitionController.getInstance().setSelectedModuleId(module.getId());
	 SoftwareUnitController controller = new SoftwareUnitController(module.getId());
	 controller.save(data);

	}

	@Override
	public void redo() {
		DefinitionController.getInstance().setSelectedModuleId(module.getId());
		 SoftwareUnitController controller = new SoftwareUnitController(module.getId());
		 ModuleDomainService t = new ModuleDomainService();
		 SoftwareUnitDefinitionDomainService ser = new SoftwareUnitDefinitionDomainService();
		 ser.removeSoftwareUnit(module.getId(), data);


	}

}
