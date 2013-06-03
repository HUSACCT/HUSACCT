package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.presentation.MainGui;
import husacct.control.task.configuration.ConfigurationManager;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class MainController {

	private CommandLineController commandLineController;
	private ViewController viewController;
	private WorkspaceController workspaceController;
	private StateController stateController;
	private ApplicationController applicationController;
	private ImportController importController;
	private ExportController exportController;
	private ApplicationAnalysisHistoryLogController applicationAnalysisHistoryLogController;
	private UserActionLogController userActionLogController;
	private CodeViewController codeViewController;

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
		this.commandLineController = new CommandLineController();
		this.workspaceController = new WorkspaceController(this);
		this.viewController = new ViewController(this);
		this.stateController = new StateController(this);
		this.applicationController = new ApplicationController(this);
		this.importController = new ImportController(this);
		this.exportController = new ExportController(this);
		this.applicationAnalysisHistoryLogController = new ApplicationAnalysisHistoryLogController(this);
		this.userActionLogController = new UserActionLogController(this);
	}

	private void setAppleProperties(){
		logger.debug("Setting Mac OS X specific properties");		
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Husacct");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
	}

	private void openMainGui() {
		this.mainGUI = new MainGui(this);
	}

	public void parseCommandLineArguments(String[] commandLineArguments){
		this.commandLineController.parse(commandLineArguments);
	}

	public CommandLineController getCommandLineController(){
		return this.commandLineController;
	}

	public ViewController getViewController(){
		return this.viewController;
	}

	public WorkspaceController getWorkspaceController(){
		return this.workspaceController;
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
	
	public ApplicationAnalysisHistoryLogController getApplicationAnalysisHistoryLogController(){
		return this.applicationAnalysisHistoryLogController;
	}
	
	public UserActionLogController getUserActionLogController(){
		return this.userActionLogController;
	}

	public void exit(){
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		int clickedOption = JOptionPane.showConfirmDialog(this.mainGUI, localeService.getTranslatedString("AreYouSureYouWantToExitHUSACCT"), localeService.getTranslatedString("Exit"), JOptionPane.YES_NO_OPTION);
		if(clickedOption == JOptionPane.YES_OPTION){
			if(stateController.getState().contains(States.OPENED)) {			
				clickedOption = JOptionPane.showConfirmDialog(this.mainGUI, localeService.getTranslatedString("DoYouWantToSaveBeforeQuit"), localeService.getTranslatedString("Exit"), JOptionPane.YES_NO_OPTION);
				if(clickedOption == JOptionPane.NO_OPTION){				
					logger.debug("Close HUSACCT");
					System.exit(0);
				}
				else {
					workspaceController.showSaveWorkspaceGui();
				}
			}
			else {
				System.exit(0);
			}
		}
	}

	public MainGui getMainGui(){
		return mainGUI;
	}

	public CodeViewController getCodeViewerController() {
		return this.codeViewController;
	}

	public void initialiseCodeViewerController() {
		this.codeViewController = new CodeViewController(this);
	}
}
