package husacct.define.domain.services.stateservice.state.module;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.UndoRedoService;
import husacct.define.domain.services.stateservice.interfaces.Istate;


public class ModuleAddCommand implements Istate{


	private ModuleStrategy child;
		

	
	
	public ModuleAddCommand(ModuleStrategy module) {
		this.child=module;
	}

	@Override
	public void undo() {
		
	
	
		UndoRedoService.getInstance().removeSeperatedModule(child);
	
		
		
	}

	@Override
	public void redo() {
		
	
		UndoRedoService.getInstance().addSeperatedModule(child);
		
      
		
	}

}
