package husacct.control.task;

import husacct.control.presentation.MainGui;

import org.apache.log4j.Logger;

public class MainController {
	
	private ViewController viewController;
	private WorkspaceController workspaceController;
	private LocaleController localeController;
	private StateController stateController;
	private ApplicationController applicationController;
	private ImportController importController;
	private ExportController exportController;
	
	public MainGui mainGUI;
	
	private Logger logger = Logger.getLogger(MainController.class);
	
	public boolean guiEnabled = false; 
	
	public MainController(){
		setControllers();
		setAppleProperties();
	}
	
	public void startGui(){
		guiEnabled = true;
		openMainGui();
	}
	
	private void setControllers() {
		this.workspaceController = new WorkspaceController(this);
		this.viewController = new ViewController(this);
		this.localeController = new LocaleController();
		this.stateController = new StateController(this);
		this.applicationController = new ApplicationController(this);
		this.importController = new ImportController(this);
		this.exportController = new ExportController(this);
	}
	
	private void setAppleProperties(){
		logger.debug("Setting Mac OS X specific properties");		
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Husacct");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
	}
	
	public void readArguments(String[] consoleArguments){
		boolean argumentsFound = false;
		String consoleArgumentsString = "";
		for(String argument : consoleArguments){
			argumentsFound = true;
			consoleArgumentsString += argument; 
			if(argument.equals("nogui")){
				guiEnabled = false;
			}
		}
		if(argumentsFound) logger.debug("Parsed console arguments: " + consoleArgumentsString);
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
	
	public ImportController getImportController(){
		return this.importController;
	}
	
	public ExportController getExportController(){
		return this.exportController;
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
