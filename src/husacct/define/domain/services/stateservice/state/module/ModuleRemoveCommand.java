package husacct.define.domain.services.stateservice.state.module;

import java.util.ArrayList;

import husacct.define.domain.services.stateservice.interfaces.Istate;

public class ModuleRemoveCommand implements Istate {

	
	private ArrayList<Object[]> data;
	
	public ModuleRemoveCommand(ArrayList<Object[]> data) {
		this.data=data;
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
