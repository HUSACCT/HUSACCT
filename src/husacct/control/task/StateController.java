package husacct.control.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.ApplicationDTO;
import husacct.common.enums.States;
import husacct.common.services.IServiceListener;
import husacct.define.IDefineService;
import husacct.graphics.IGraphicsService;
import husacct.validate.IValidateService;

import java.util.ArrayList;
import java.util.List;

public class StateController {

	private List<States> states = new ArrayList<States>();

	private ArrayList<IStateChangeListener> stateListeners = new ArrayList<IStateChangeListener>();

	private WorkspaceController workspaceController;

	private boolean isAnalysing = false;
	private boolean isValidating = false;

	public StateController(MainController mainController){
		workspaceController = mainController.getWorkspaceController();
		states.add(States.NONE);
	}

	public void setServiceListeners(){
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
		IGraphicsService graphicsService = ServiceProvider.getInstance().getGraphicsService();

		IServiceListener serviceListener = new IServiceListener() {
			@Override
			public void update() {
				checkState();

			}
		};

		defineService.addServiceListener(serviceListener);
		analyseService.addServiceListener(serviceListener);
		validateService.addServiceListener(serviceListener);
		graphicsService.addServiceListener(serviceListener);
	}

	public void checkState(){
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
		IValidateService validateService = ServiceProvider.getInstance().getValidateService();
		List<States> newStates = new ArrayList<States>();
		if(workspaceController.isAWorkspaceOpened()){
			newStates.add(States.OPENED);
		}
		ApplicationDTO applicationData = defineService.getApplicationDetails();
		if(applicationData.projects.size() > 0) {
			newStates.add(States.APPSET);
		}
		if(defineService.isDefined()){
			newStates.add(States.DEFINED);
		}
		if(defineService.isMapped()){
			newStates.add(States.MAPPED);
		}
		if(analyseService.isAnalysed()){
			newStates.add(States.ANALYSED);
		}
		if(isAnalysing()) {
			newStates.add(States.ANALYSING);
		}
		if(validateService.isValidated()){
			newStates.add(States.VALIDATED);
		}
		if(isValidating()) {
			newStates.add(States.VALIDATING);
		}
		if(newStates.isEmpty()){
			newStates.add(States.NONE);
		}
		setStates(newStates);
	}

	public List<States> getStates(){
		return this.states;
	}

	public void setStates(List<States> states){
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

	public boolean isValidating() {
		return this.isValidating;
	}

	public boolean isAnalysing() {
		return this.isAnalysing;
	}

	public void setAnalysing(boolean analysing) {		
		this.isAnalysing = analysing;
		checkState();
	}

	public void setValidating(boolean validating) {
		this.isValidating = validating;
		checkState();
	}

}
