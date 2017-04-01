package husacct.define.domain.services.stateservice.state.module;

import husacct.ServiceProvider;
import husacct.common.enums.ModuleTypes;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class UpdateModuleCommand implements Istate{
	private long moduleId;
	private String[] oldModuleValue;
	private String[] newModuleValue;

	public UpdateModuleCommand(Long moduleid,String[] old,String[] newString) {
		this.moduleId=moduleid;
		this.oldModuleValue=old;
		this.newModuleValue=newString;
	}
	
	@Override
	public void undo() {
		ModuleDomainService service = new ModuleDomainService();
		service.updateModuleDetails(moduleId, oldModuleValue[0], oldModuleValue[1], ModuleTypes.SUBSYSTEM.toString());
		ServiceProvider.getInstance().getDefineService().getDefinedGUI().updateUI();
	}

	@Override
	public void redo() {
		ModuleDomainService service = new ModuleDomainService();
		service.updateModuleDetails(moduleId, newModuleValue[0], newModuleValue[1], ModuleTypes.SUBSYSTEM.toString());
		ServiceProvider.getInstance().getDefineService().getDefinedGUI().updateUI();
	}

}
