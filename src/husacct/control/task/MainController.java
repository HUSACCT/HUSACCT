package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.presentation.MainGui;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class MainController {

	private CommandLineController commandLineController;
	private ViewController viewController;
	private WorkspaceController workspaceController;
	private StateController stateController;
	private ApplicationController applicationController;
	private ExportImportController exportImportController;
	private ApplicationAnalysisHistoryLogController applicationAnalysisHistoryLogController;
	private ActionLogController actionLogController;
	private CodeViewController codeViewController;
	private FileController fileController;

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
		this.exportImportController = new ExportImportController(this);
		this.applicationAnalysisHistoryLogController = new ApplicationAnalysisHistoryLogController(this);
		this.actionLogController = new ActionLogController(this);
		this.fileController = new FileController(this);
	}

	private void setAppleProperties(){
		logger.info("Setting Mac OS X specific properties");		
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

	public ExportImportController getExportImportController(){
		return this.exportImportController;
	}
	
	public ApplicationAnalysisHistoryLogController getApplicationAnalysisHistoryLogController(){
		return this.applicationAnalysisHistoryLogController;
	}
	
	public ActionLogController getActionLogController(){
		return this.actionLogController;
	}
	
	public FileController getFileController() {
		return this.fileController;
	}

	public void exit(){
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		Object[] options = { localeService.getTranslatedString("Exit"), 
							 localeService.getTranslatedString("Save&Exit"),
				             localeService.getTranslatedString("Cancel") };
		int clickedOption = JOptionPane.showOptionDialog(this.mainGUI, localeService.getTranslatedString("AreYouSureYouWantToExitHUSACCT"), localeService.getTranslatedString("Exit"), JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);
		if(clickedOption == JOptionPane.YES_OPTION){
			logger.info("Close HUSACCT" + "\n");
			System.exit(0);
		} else if (clickedOption == JOptionPane.NO_OPTION) {
			workspaceController.showSaveWorkspaceGui();
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
