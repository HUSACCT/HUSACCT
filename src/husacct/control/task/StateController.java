package husacct.control.task;

import java.util.ArrayList;

public class StateController {
	
	public static final int NONE = 0;
	public static final int EMPTY = 1;
	public static final int DEFINED = 2;
	public static final int MAPPED = 3;
	public static final int VALIDATED = 4;
	
	private int state = StateController.NONE;
	
	ArrayList<IStateChangeListener> stateListeners = new ArrayList<IStateChangeListener>();
	
	public StateController(){
		setState(state);
	}
	
	public int getState(){
		return this.state;
	}
	
	public void setState(int inputState){
		this.state = inputState;
		notifyStateListeners(inputState);
	}
	
	public void addStateChangeListener(IStateChangeListener listener) {
		this.stateListeners.add(listener);
	}
	
	public void notifyStateListeners(int state){
		for(IStateChangeListener listener : this.stateListeners){
			listener.changeState(state);
		}
	}
}
