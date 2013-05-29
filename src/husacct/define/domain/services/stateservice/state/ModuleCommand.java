package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;


public class ModuleCommand implements Istate{


	private ModuleStrategy child;
		
	
	
	
	public ModuleCommand(ModuleStrategy ModuleStrategy) {
		this.child=ModuleStrategy;
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
