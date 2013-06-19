package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.services.stateservice.interfaces.Istate;

import java.util.ArrayList;

public class StateKeeper implements Istate {

	private ArrayList<Istate> states;
	
	public StateKeeper(ArrayList<Istate> listOfStatesTokeep) {
	this.states=listOfStatesTokeep;
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
