package husacct.define.domain.services.stateservice.state.module;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;

public class UpdateModuleTypeCommand implements Istate {
private ModuleStrategy oldModule;
private ModuleStrategy newModule;
	public UpdateModuleTypeCommand(ModuleStrategy oldModule,ModuleStrategy newModule) {
		this.oldModule=oldModule;
		this.newModule=newModule;
	}
	
	@Override
	public void undo() {
	ModuleDomainService service = new ModuleDomainService();
	
	service.updateModuleType(oldModule.getId(), oldModule.getName(), oldModule.getDescription(),oldModule.getType());
    DefinitionController.getInstance().notifyObservers();
	}

	@Override
	public void redo() {
		ModuleDomainService service = new ModuleDomainService();
		service.updateModuleType(newModule.getId(), newModule.getName(), newModule.getDescription(),newModule.getType());
		DefinitionController.getInstance().notifyObservers();
	}

}
