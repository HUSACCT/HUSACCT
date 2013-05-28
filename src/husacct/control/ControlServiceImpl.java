package husacct.control;

import husacct.ServiceProvider;
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
import husacct.control.task.MainController;
import husacct.control.task.StateController;
import husacct.control.task.States;
import husacct.control.task.ViewController;
import husacct.control.task.WorkspaceController;
import husacct.control.task.configuration.ConfigPanel;
import husacct.control.task.configuration.ConfigurationManager;
import husacct.control.task.threading.ThreadWithLoader;

import java.awt.Component;
import java.io.File;
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
	
	public ControlServiceImpl(){
		logger.debug("Starting HUSACCT");
		mainController = new MainController();
		workspaceController = mainController.getWorkspaceController();
		applicationController = mainController.getApplicationController();
		stateController = mainController.getStateController();
		viewController = mainController.getViewController();
		mainController.initialiseCodeViewerController();
		codeViewController = mainController.getCodeViewerController();
		setDefaultSettings();
	}
	
	private void setDefaultSettings() {
		String appDataFolderString = System.getProperty("user.home") + File.separator + "HUSACCT" + File.separator;
		System.out.println("App data folder: " + appDataFolderString);
		File appDataFolderObject = new File(appDataFolderString);
		if(!appDataFolderObject.exists()){
			appDataFolderObject.mkdir();
		}
		ConfigurationManager.setPropertieIfEmpty("PlatformIndependentAppDataFolder", appDataFolderString);
		ConfigurationManager.setPropertieIfEmpty("LastUsedLoadXMLWorkspacePath", appDataFolderString + "husacct_workspace.xml");
		ConfigurationManager.setPropertieIfEmpty("LastUsedSaveXMLWorkspacePath", appDataFolderString + "husacct_workspace.xml");
		ConfigurationManager.setPropertieIfEmpty("LastUsedAddProjectPath", appDataFolderString);	
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
		try {
			String workspaceName = workspaceData.getAttributeValue("name");
			String languageName = workspaceData.getAttributeValue("language");
			workspaceController.createWorkspace(workspaceName);
			ServiceProvider.getInstance().getLocaleService().setLocale(new Locale(languageName));
		} catch (Exception e){
			logger.debug("WorkspaceData corrupt: " + e);
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
	public String getConfigurationName() {
		return ServiceProvider.getInstance().getLocaleService().getTranslatedString("ConfigGeneral");
	}

	@Override
	public ConfigPanel getConfigurationPanel() {
		if (generalConfigurationPanel == null)
			generalConfigurationPanel = new GeneralConfigurationPanel();
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
}
