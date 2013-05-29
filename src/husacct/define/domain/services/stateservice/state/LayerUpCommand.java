package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.module.Module;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class LayerUpCommand implements Istate {

	private Module module;

	public LayerUpCommand(Module module) {
		this.module=module;
	}
	
	@Override
	public void undo() {
		

	}

	@Override
	public void redo() {
		

	}

}
