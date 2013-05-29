package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.interfaces.Istate;

public class LayerUpCommand implements Istate {

	private ModuleStrategy ModuleStrategy;

	public LayerUpCommand(ModuleStrategy ModuleStrategy) {
		this.ModuleStrategy=ModuleStrategy;
	}
	
	@Override
	public void undo() {
		

	}

	@Override
	public void redo() {
		

	}

}
