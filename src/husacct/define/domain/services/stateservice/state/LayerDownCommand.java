package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.module.ToBeImplemented.ModuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class LayerDownCommand  implements Istate{

	private ModuleStrategy ModuleStrategy;

	public LayerDownCommand(ModuleStrategy ModuleStrategy) {
		this.ModuleStrategy=ModuleStrategy;
	}
	
	@Override
	public void undo() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void redo() {
		// TODO Auto-generated method stub
		
	}

}
