package husacct.control.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.define.IDefineService;
import husacct.validate.IValidateService;

import java.util.ArrayList;
import java.util.List;

public class StateController {
	
	private List<States> states;
	
	ArrayList<IStateChangeListener> stateListeners = new ArrayList<IStateChangeListener>();
	
	public StateController(){
		checkState();
	}
	
	public void checkState(){
		
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
		
		List<States> newStates = new ArrayList<States>();

		if(validateService.isValidated()){
			newStates.add(States.VALIDATED);
		}
		
		if(defineService.isMapped()){
			newStates.add(States.MAPPED);
		}
		
		if(analyseService.isAnalysed()){
			newStates.add(States.ANALYSED);
		}
		
		if(defineService.isDefined()){
			newStates.add(States.DEFINED);
		}
		
		if(WorkspaceController.isOpenWorkspace()){
			newStates.add(States.OPENED);
		}
		
		if(newStates.isEmpty()){
			newStates.add(States.NONE);
		}
		
		
		setState(newStates);
	}
	
	public List<States> getState(){
		return this.states;
	}
	
	public void setState(List<States> states){
		this.states = states;
		notifyStateListeners(states);
	}
	
	public void addStateChangeListener(IStateChangeListener listener) {
		this.stateListeners.add(listener);
	}
	
	public void notifyStateListeners(List<States> states){
		for(IStateChangeListener listener : this.stateListeners){
			listener.changeState(states);
		}
	}
}
