package husacct.define.domain.services.stateservice.state.module;

import java.util.ArrayList;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;
import husacct.define.task.DefinitionController;


public class ModuleAddCommand implements Istate{


	private ModuleStrategy child;
		
	private ArrayList<Object[]> data= new ArrayList<Object[]>();
	
	
	public ModuleAddCommand(ArrayList<Object[]> commandData) {
		this.data=commandData;
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
