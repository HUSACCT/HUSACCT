package husacct.define.domain.services.stateservice.state.softwareunit;

import husacct.define.domain.services.SoftwareUnitDefinitionDomainService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.util.ArrayList;
import java.util.List;

public class SoftwareUnitRemoveCommand implements Istate {

	private long moduleId;
    private	List<String> data;
	
	public SoftwareUnitRemoveCommand() {
		// TODO Auto-generated constructor stub
	}
	


	public SoftwareUnitRemoveCommand(long selectedModuleId,
			List<String> selectedModules) {
	this.moduleId=selectedModuleId;
	this.data=selectedModules;
	}

	@Override
	public void undo() {
	 DefinitionController.getInstance().setSelectedModuleId(moduleId);
	 SoftwareUnitController controller = new SoftwareUnitController(moduleId);
	ArrayList<AnalyzedModuleComponent> units = StateService.instance().getAnalyzedSoftWareUnit(data);
	 
	 
	 controller.save(units);

	}

	@Override
	public void redo() {
		 DefinitionController.getInstance().setSelectedModuleId(moduleId);
		 SoftwareUnitDefinitionDomainService ser = new SoftwareUnitDefinitionDomainService();
		 ArrayList<AnalyzedModuleComponent> units = StateService.instance().getAnalyzedSoftWareUnit(data);
		 ser.removeSoftwareUnit(moduleId, units);


	}

}
