package husacct.define.domain.services.stateservice.state;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;


public class ModuleCommand implements Istate{


	private Module child;
		
	
	
	
	public ModuleCommand(Module module) {
		this.child=module;
	}

	@Override
	public void undo() {
		
		
		
	DefinitionController.getInstance().removeModuleById(child.getId());
		
		
	}

	@Override
	public void redo() {
		//this is a hack will be changed
	
		DefinitionController.getInstance().passModuleToService(child.getparent().getId(), child);
      
		
	}

}
