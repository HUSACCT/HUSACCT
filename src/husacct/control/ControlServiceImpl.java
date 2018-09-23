package husacct.control;

import husacct.ServiceProvider;
import husacct.common.OSDetector;
import husacct.common.dto.ApplicationDTO;
import husacct.common.enums.States;
import husacct.common.savechain.ISaveable;
import husacct.common.services.IConfigurable;
import husacct.common.services.ObservableService;
import husacct.control.domain.Workspace;
import husacct.control.presentation.util.DialogUtils;
import husacct.control.presentation.util.GeneralConfigurationPanel;
import husacct.control.presentation.util.LoadingDialog;
import husacct.control.presentation.viewcontrol.ViewController;
import husacct.control.task.*;
import husacct.control.task.configuration.ConfigPanel;
import husacct.control.task.configuration.ConfigurationManager;
import husacct.control.task.threading.ThreadWithLoader;
import husacct.externalinterface.SaccCommandDTO;
import husacct.externalinterface.ViolationReportDTO;
import husacct.validate.domain.validation.Severity;
import org.apache.log4j.Logger;
import org.jdom2.Element;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class ControlServiceImpl extends ObservableService implements IControlService, ISaveable, IConfigurable {

	private Logger logger = Logger.getLogger(ControlServiceImpl.class);
	ArrayList<ILocaleChangeListener> listeners = new ArrayList<ILocaleChangeListener>();
	private boolean isGuiEnabled = false; 
	
	private MainController mainController; 
	private WorkspaceController workspaceController;
	private ApplicationController applicationController;
	private StateController stateController;
	private ViewController viewController;
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
	public ViolationReportDTO performSoftwareArchitectureComplianceCheck(SaccCommandDTO saccCommandDTO) {
		ExternalComplianceCheck externalComplianceCheck = new ExternalComplianceCheck();
		ViolationReportDTO violationReport = externalComplianceCheck.performSoftwareArchitectureComplianceCheck(saccCommandDTO);
		return violationReport;
	}

	@Override
	public void startApplication() {
		mainController.startGui();
		isGuiEnabled = true;
		if(mainController.getCommandLineController().getResult().contains("bootstrap")){
			new BootstrapHandler(mainController.getCommandLineController().getResult().getStringArray("bootstrap"));
		}
	}
	
	@Override
	public boolean isGuiEnabled() {
		return isGuiEnabled;
	}
	
	@Override
	public Element getWorkspaceData() {
		Element data = new Element("workspace");
		Workspace workspace = workspaceController.getCurrentWorkspace();
		if (workspace != null) {
			data.setAttribute("name", (workspace.getName() == null) ? "" : workspace.getName());
			data.setAttribute("language", ServiceProvider.getInstance().getLocaleService().getLocale().getLanguage());
		}
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
		
		mainController.getApplicationController().setCurrentLoadingDialog(loader.getLoadingDialog());
		
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
	public List<States> getStates() {
		return this.getMainController().getStateController().getStates();
	}
	

	@Override
	public void updateProgress(int progressPercentage) {
		try{
			LoadingDialog loadingDialog = mainController.getApplicationController().getCurrentLoadingDialog();
			if (loadingDialog != null) {
				loadingDialog.setProgressText(progressPercentage);
			}
		}catch(Exception e){
			logger.warn(" Error while updating progress in thread loader dialog.");
		}
	}
	
	@Override
	public void setValidating(boolean isValidating) {
		this.mainController.getStateController().setValidating(isValidating);
	}

	@Override
	public ApplicationDTO getApplicationDTO() {
		return mainController.getWorkspaceController().getCurrentWorkspace().getApplicationData();
	}
	
	@Override
	public void displayErrorsInFile(String fileName, HashMap<Integer, Severity> errors) {
		mainController.getCodeViewerController().displayErrorsInFile(fileName, errors);
	}

	@Override
	public void displayErrorInFile(String fileName, int lineNumber, Severity severity) {
		HashMap<Integer, Severity> errors = new HashMap<Integer, Severity>();
		if ((lineNumber > 0) && (severity != null)) {
			errors.put(lineNumber, severity);
		}
		mainController.getCodeViewerController().displayErrorsInFile(fileName, errors);
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
	public String showMojoExportImportDialog(boolean isExport) {
		return mainController.getExportImportController().showExportImportMojoGUI(isExport);
	}
	
	@Override
	public void addProjectForListening(String path) {
		try {
			fileController.addProject(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		eventHandlerThread = new Thread() {
			@Override
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
