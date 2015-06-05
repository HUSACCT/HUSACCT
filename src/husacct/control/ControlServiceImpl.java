package husacct.control;

import husacct.ServiceProvider;
import husacct.common.OSDetector;
import husacct.common.dto.ApplicationDTO;
import husacct.common.savechain.ISaveable;
import husacct.common.services.IConfigurable;
import husacct.common.services.ObservableService;
import husacct.control.domain.Workspace;
import husacct.control.presentation.util.DialogUtils;
import husacct.control.presentation.util.GeneralConfigurationPanel;
import husacct.control.task.ApplicationController;
import husacct.control.task.BootstrapHandler;
import husacct.control.task.CodeViewController;
import husacct.control.task.FileController;
import husacct.control.task.IFileChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;
import husacct.control.task.States;
import husacct.control.task.ViewController;
import husacct.control.task.WorkspaceController;
import husacct.control.task.configuration.ConfigPanel;
import husacct.control.task.configuration.ConfigurationManager;
import husacct.control.task.threading.ThreadWithLoader;
import husacct.validate.domain.validation.Severity;

import java.awt.Component;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.swing.JDialog;

import org.apache.log4j.Logger;
import org.jdom2.Element;


public class ControlServiceImpl extends ObservableService implements IControlService, ISaveable, IConfigurable {

	private Logger logger = Logger.getLogger(ControlServiceImpl.class);
	ArrayList<ILocaleChangeListener> listeners = new ArrayList<ILocaleChangeListener>();
	
	private MainController mainController; 
	private WorkspaceController workspaceController;
	private ApplicationController applicationController;
	private StateController stateController;
	private ViewController viewController;
	private CodeViewController codeViewController;
	private GeneralConfigurationPanel generalConfigurationPanel;
	private FileController fileController;
	
	private Thread eventHandlerThread;
	
	public ControlServiceImpl(){
		mainController = new MainController();
		workspaceController = mainController.getWorkspaceController();
		applicationController = mainController.getApplicationController();
		stateController = mainController.getStateController();
		viewController = mainController.getViewController();
		mainController.initialiseCodeViewerController();
		codeViewController = mainController.getCodeViewerController();
		fileController = mainController.getFileController();
		setDefaultSettings();
	}
	
	private void setDefaultSettings() {
		String appDataFolderPath = OSDetector.getAppFolder();
		logger.info("App data folder (platform specific): " + appDataFolderPath);
		
		ConfigurationManager.setPropertyIfEmpty("LastUsedLoadXMLWorkspacePath", appDataFolderPath + "husacct_workspace.xml");
		ConfigurationManager.setPropertyIfEmpty("LastUsedSaveXMLWorkspacePath", appDataFolderPath + "husacct_workspace.xml");
		ConfigurationManager.setPropertyIfEmpty("LastUsedAddProjectPath", appDataFolderPath);
		ConfigurationManager.setPropertyIfEmpty("ApplicationHistoryXMLFilename", "applicationanalysishistory.xml");
		ConfigurationManager.setPropertyIfEmpty("ActionLogger", "false");
		ConfigurationManager.setPropertyIfEmpty("Language", "en");
	}

	@Override
	public void parseCommandLineArguments(String[] commandLineArguments){
		mainController.parseCommandLineArguments(commandLineArguments);
	}
	
	@Override
	public void startApplication() {
		mainController.startGui();

		if(mainController.getCommandLineController().getResult().contains("bootstrap")){
			new BootstrapHandler(mainController.getCommandLineController().getResult().getStringArray("bootstrap"));
		}
	}
	
	@Override
	public Element getWorkspaceData() {
		Element data = new Element("workspace");
		Workspace workspace = workspaceController.getCurrentWorkspace();
		data.setAttribute("name", workspace.getName());
		data.setAttribute("language", ServiceProvider.getInstance().getLocaleService().getLocale().getLanguage());

		return data;
	}
	
	@Override
	public void loadWorkspaceData(Element workspaceData) {
		String languageName = "en";
		try {
			String workspaceName = workspaceData.getAttributeValue("name");
			languageName = workspaceData.getAttributeValue("language");
			workspaceController.createWorkspace(workspaceName);
		} catch (Exception e){
			logger.debug("WorkspaceData corrupt: " + e);
		}
		try {
			ServiceProvider.getInstance().getLocaleService().setLocale(new Locale(languageName));
		} catch (Exception e){
			logger.warn(" LocaleService not found at specified path ");
		}
	}
	
	@Override
	public void showErrorMessage(String message){
		applicationController.showErrorMessage(message);
	}
	
	@Override
	public void showInfoMessage(String message){
		applicationController.showInfoMessage(message);
	}
	
	@Override
	public void centerDialog(JDialog dialog){
		DialogUtils.alignCenter(dialog);
	}
	
	@Override
	public ThreadWithLoader getThreadWithLoader(String progressInfoText, Runnable threadTask) {
		ThreadWithLoader loader = new ThreadWithLoader(mainController, progressInfoText, threadTask);
		
		mainController.getApplicationController().setCurrentLoader(loader.getLoader());
		
		return loader;
	}
	
	@Override
	public void setServiceListeners(){
		stateController.setServiceListeners();
		viewController.setLocaleListeners();
	}
	
	public MainController getMainController(){
		return mainController;
	}
	
	@Override
	public void finishPreAnalysing() {
		mainController.getStateController().addState(States.PREANALYSED);
	}
	
	@Override
	public boolean isPreAnalysed() {
		return getState().contains(States.PREANALYSED);
	}
	
	@Override
	public List<States> getState() {
		return this.getMainController().getStateController().getState();
	}
	

	@Override
	public void updateProgress(int progressPercentage) {
		try{
		mainController.getApplicationController().getCurrentLoader().setProgressText(progressPercentage);
		}catch(Exception e){
			
		}
	}
	
	@Override
	public void setValidate(boolean validate) {
		this.mainController.getStateController().setValidating(validate);
		this.mainController.getStateController().checkState();
	}

	@Override
	public ApplicationDTO getApplicationDTO() {
		return mainController.getWorkspaceController().getCurrentWorkspace().getApplicationData();
	}
	
	@Override
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errors) {
		codeViewController.displayErrorsInFile(fileName, errors);
	}
	
	@Override
	public void displayErrorsInFile(String fileName, HashMap<Integer, Severity> errors) {
		codeViewController.displayErrorsInFile(fileName, errors);
	}

	@Override
	public void displayErrorInFile(String fileName, int lineNumber, Severity severity) {
		HashMap<Integer, Severity> errors = new HashMap<Integer, Severity>();
		errors.put(lineNumber, severity);
		codeViewController.displayErrorsInFile(fileName, errors);
	}
	
	@Override
	public String getConfigurationName() {
		return ServiceProvider.getInstance().getLocaleService().getTranslatedString("ConfigGeneral");
	}

	@Override
	public ConfigPanel getConfigurationPanel() {
		if (generalConfigurationPanel == null)
			generalConfigurationPanel = new GeneralConfigurationPanel(mainController);
		return generalConfigurationPanel;
	}
	
	@Override
	public HashMap<String, ConfigPanel> getSubItems() {
		HashMap<String, ConfigPanel> subitems = new HashMap<String, ConfigPanel>();
		return subitems;
	}
	
	@Override
	public void showHelpDialog(Component comp) {
		mainController.getApplicationController().showHelpGUI(comp);
	}

	@Override
	public void addProjectForListening(String path) {
		try {
			fileController.addProject(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		eventHandlerThread = new Thread() {
			public void run() {
				try {
					fileController.processEvents();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		eventHandlerThread.start();
	}

	@Override
	public void addFileChangeListener(IFileChangeListener listener) {
		fileController.addFileChangeListener(listener);
	}	
}
