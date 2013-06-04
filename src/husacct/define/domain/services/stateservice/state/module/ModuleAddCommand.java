package husacct.define.domain.services.stateservice.state.module;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.ModuleDomainService;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;


public class ModuleAddCommand implements Istate{


	private ModuleStrategy child;
		

	
	
	public ModuleAddCommand(ModuleStrategy module) {
		this.child=module;
	}

	@Override
	public void undo() {
		
	
		DefinitionController.getInstance().removeModuleById(child.getId());
	
		
		
	}

	@Override
	public void redo() {
		
	
		DefinitionController.getInstance().passModuleToService(child.getparent().getId(), child);
      
		
	}

}
