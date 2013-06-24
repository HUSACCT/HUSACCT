package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.services.stateservice.interfaces.Istate;

import java.util.ArrayList;

public class StateKeeper implements Istate {

	private ArrayList<Istate> states;
	private int currentIndex = -1;
	private int _currentIndex = 0;
	private int sizeoflist = 0;

	private enum State {
		inAction, outAction
	};

	private State myState = State.outAction;

	public StateKeeper(ArrayList<Istate> listOfStatesTokeep) {
		this.states = listOfStatesTokeep;
	}

	@Override
	public void undo() {
		myState = State.inAction;
		upDate();
		Istate result = null;
		if (currentIndex == -1) {
			currentIndex = sizeoflist - 1;
			_currentIndex = currentIndex;
			result = states.get(currentIndex);
		} else {
			_currentIndex = currentIndex;
			currentIndex--;

			if (currentIndex == -1) {

				result = states.get(_currentIndex);
			} else {
				result = states.get(currentIndex);

			}
		}

		result.undo();

	}

	@Override
	public void redo() {

		myState = State.inAction;
		Istate result = null;
		
		if(_currentIndex==1){
				result=	states.get(currentIndex);
		_currentIndex--;
			
		}else{
	
			
			_currentIndex=currentIndex;
		currentIndex++;
		
		if (sizeoflist==currentIndex) {
			result=	states.get(_currentIndex);
			
		} else {
			result=	states.get(currentIndex);
		}
		
		
		}
		
		
		

		result.redo();

	}

	private void upDate() {
		sizeoflist = states.size();

	}

	public void insertCommand(Istate sate) {
	upDate();
		System.out.println("inserted Command :"+sate.getClass().getName() );
		myState = State.outAction;
		if ( 6 == states.size()) {
			removeStates();
			currentIndex++;
			registerState(sate);
		} else {
			registerState(sate);

		}

	}

	public void registerState(Istate sate) {

		if (states.size() == 5) {
			states.remove(0);

			states.add(sate);
		} else {

			states.add(sate);
		}

		upDate();
	}

	private void removeStates() {

		for (int i = this.currentIndex + 1; i < states.size(); i++) {
			states.remove(i);
		}

	}

	public boolean[] getStates() {
		boolean undo = false;
		boolean redo = false;
		if (myState == State.outAction) {
			if (sizeoflist > 0) {
				undo = true;
				redo = false;
			}
		} else {
			if (sizeoflist > 0) {
				undo = true;
				redo = true;
			}

			if ((sizeoflist - currentIndex) == 1) {
				redo = false;
			}

			if ((_currentIndex + 1) == sizeoflist) {
				redo = true;
			}
			if (currentIndex == -1) {
				undo = true;
			}

			if (currentIndex == 0) {
				undo = false;
			}
			if (currentIndex - sizeoflist == 0) {
				redo = false;
			}
			if (_currentIndex == sizeoflist) {

				redo = true;
			}

		}

		return new boolean[] { undo, redo };
	}

}
