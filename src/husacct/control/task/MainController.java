package husacct.control.task;

import husacct.control.presentation.MainGui;

import org.apache.log4j.Logger;

public class MainController {
	
	private ViewController viewController;
	private WorkspaceController workspaceController;
	private LocaleController localeController;
	private StateController stateController;
	private ApplicationController applicationController;
	private ImportExportController importExportController;
	
	public MainGui mainGUI;
	
	private Logger logger = Logger.getLogger(MainController.class);
	
	public boolean guiEnabled = true; 
	
	public MainController(String[] args){
		readArguments(args);
		setControllers();
		if(guiEnabled) openMainGui();
		stateController.checkState();
	}

	private void readArguments(String[] args){
		logger.debug("Arguments:" + args);
		for(String s : args){
			if(s.equals("nogui")){
				guiEnabled = false;
			}
		}
	}
	
	private void setControllers() {
		this.workspaceController = new WorkspaceController(this);
		this.viewController = new ViewController(this);
		this.localeController = new LocaleController();
		this.stateController = new StateController();
		this.applicationController = new ApplicationController(this);
		this.importExportController = new ImportExportController(this);
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
	
	public ApplicationController getApplicationController(){
		return this.applicationController;
	}
	
	public ImportExportController getImportExportController(){
		return this.importExportController;
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
