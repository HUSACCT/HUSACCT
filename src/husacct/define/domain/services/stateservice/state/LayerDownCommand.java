package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.module.Module;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class LayerDownCommand  implements Istate{

	private Module module;

	public LayerDownCommand(Module module) {
		this.module=module;
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
