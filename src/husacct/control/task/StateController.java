package husacct.control.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.define.IDefineService;
import husacct.validate.IValidateService;

import java.util.ArrayList;

public class StateController {
	
	public static final int NONE = 0;
	public static final int EMPTY = 1;
	public static final int DEFINED = 2;
	public static final int ANALYSED = 3;
	public static final int MAPPED = 4;
	public static final int VALIDATED = 5;
	
	private int state;
	
	ArrayList<IStateChangeListener> stateListeners = new ArrayList<IStateChangeListener>();
	
	public StateController(){
		checkState();
	}
	
	public void checkState(){
		
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
		
		int newState = StateController.NONE;
		
		if(WorkspaceController.isOpenWorkspace()){
			newState = StateController.EMPTY;
		}
		
		if(defineService.isDefined()){
			newState = StateController.DEFINED;
		}
		
		if(analyseService.isAnalysed()){
			newState = StateController.ANALYSED;
		}

		if(defineService.isMapped()){
			newState = StateController.MAPPED;
		}
		
		if(validateService.isValidated()){
			newState = StateController.VALIDATED;
		}
		
		this.setState(newState);
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
