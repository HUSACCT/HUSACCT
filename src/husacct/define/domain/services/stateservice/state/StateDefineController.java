package husacct.define.domain.services.stateservice.state;

import husacct.define.domain.services.stateservice.interfaces.Istate;

import java.util.ArrayList;

public class StateDefineController {
	private int currentIndex = -1;
	private boolean islocked = false;
	private boolean undo = true;
	private boolean redu = true;

	private ArrayList<Istate> states = new ArrayList<Istate>();

	public boolean undo() {

		islocked = true;
	
		redu = true;
		states.get(currentIndex).undo();
		currentIndex--;
		if (currentIndex - 1 == -1) {
			undo = false;
		}

		return currentIndex != -1;

	}

	public boolean redo() {
		int size = states.size();
       undo= true;
		islocked = true;
	    if (currentIndex+2  == size) {
			redu = false;
			}
		
		currentIndex++;
		states.get(currentIndex).redo();
		
		
	

		return true;

	}

	public void insertCommand(Istate sate) {
		if (!islocked && currentIndex + 1 != states.size()) {
			removeStates();
			currentIndex++;
			registerState(sate);
		} else if (!islocked) {
			registerState(sate);
			
		}

	}

	private void registerState(Istate sate) {
		if (states.size() == 5) {
			states.remove(0);
			System.out.println(sate.getClass().getName()+ "  <<<<<<<<<<< exe");
			states.add(sate);
			} else {
				System.out.println();
				currentIndex++;
				System.out.println(sate.getClass().getName()+ "  <<<<<<<<<<< normal");
				states.add(sate);
		}

	}

	private void removeStates() {

		for (int i = this.currentIndex + 1; i < states.size(); i++) {
			states.remove(i);
		}

	}

	public void unlock() {
		islocked = false;

	}

	public boolean[] getStatesStatus() {
			if (!islocked) {
			if (states.size() > 0) {
				undo = true;
				redu = false;
			}

		}

		return new boolean[] { undo, redu };

	}

}
