package husacct.control.task;

import java.util.ArrayList;

public class StateController {
	
	private static final int NONE = 0;
	private static final int EMPTY = 1;
	private static final int DEFINED = 2;
	private static final int MAPPED = 3;
	private static final int VALIDATED = 4;
	
	private int state = StateController.NONE;
	
	private ArrayList<IStateChangeListener> listeners;
	
	public StateController(){
		listeners = new ArrayList<IStateChangeListener>();
		// TODO: set/check state, changelisteners
	}
	
	
}
