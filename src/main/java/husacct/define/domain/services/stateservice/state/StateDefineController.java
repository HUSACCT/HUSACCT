package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.services.stateservice.interfaces.Istate;
import java.util.ArrayList;

public class StateDefineController {

	private ArrayList<Istate> states = new ArrayList<>();
	private StateKeeper keeper = new StateKeeper(states);

	public boolean undo() {

		keeper.undo();

		return true;

	}

	public boolean redo() {
		keeper.redo();
		return true;
	}

	public void insertCommand(Istate sate) {

		keeper.insertCommand(sate);

	}

	public boolean[] getStatesStatus() {

		return keeper.getStates();

	}

}
