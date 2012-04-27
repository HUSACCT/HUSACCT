package husacct.control.task;

import husacct.control.presentation.MainGui;

import org.apache.log4j.Logger;

public class MainController {
	
	ViewController viewController;
	WorkspaceController workspaceController;
	LocaleController localeController;
	StateController stateController;
	
	public MainGui mainGUI;
	
	private Logger logger = Logger.getLogger(MainController.class);
	
	public MainController(){
		setControllers();
		openMainGui();
	}

	private void setControllers() {
		this.workspaceController = new WorkspaceController();
		this.viewController = new ViewController(this);
		this.localeController = new LocaleController();
		this.stateController = new StateController();
	}

	private void openMainGui() {
		this.mainGUI = new MainGui(this);
	}
	
	public ViewController getViewController(){
		return this.viewController;
	}
	
	public WorkspaceController getWorkspaceController(){
		return this.workspaceController;
	}

	public LocaleController getLocaleController() {
		return this.localeController;
	}
	
	public StateController getStateController(){
		return this.stateController;
	}
	
	public void exit(){
		// TODO: check saved 
		logger.debug("Close HUSACCT");
		System.exit(0);
	}
	
	public MainGui getMainGui(){
		return mainGUI;
	}
}
