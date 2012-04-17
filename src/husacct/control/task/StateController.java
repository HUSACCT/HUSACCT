package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.ControlServiceImpl;

import java.util.ArrayList;

public class StateController {
	
	private static final int NONE = 0;
	private static final int EMPTY = 1;
	private static final int DEFINED = 2;
	private static final int MAPPED = 3;
	private static final int VALIDATED = 4;
	
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
