package husacct.control;

import husacct.common.savechain.ISaveable;
import husacct.common.services.ObservableService;
import husacct.control.domain.Workspace;
import husacct.control.presentation.util.DialogUtils;
import husacct.control.task.ApplicationController;
import husacct.control.task.LocaleController;
import husacct.control.task.MainController;
import husacct.control.task.StateController;
import husacct.control.task.WorkspaceController;
import husacct.control.task.threading.ThreadWithLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JDialog;

import org.apache.log4j.Logger;
import org.jdom2.Element;


public class ControlServiceImpl extends ObservableService implements IControlService, ISaveable{

	private Logger logger = Logger.getLogger(ControlServiceImpl.class);
	ArrayList<ILocaleChangeListener> listeners = new ArrayList<ILocaleChangeListener>();
	
	private MainController mainController; 
	private LocaleController localeController;
	private WorkspaceController workspaceController;
	private ApplicationController applicationController;
	private StateController stateController;
	
	public ControlServiceImpl(){
		logger.debug("Starting HUSACCT");
		mainController = new MainController();
		localeController = mainController.getLocaleController();
		workspaceController = mainController.getWorkspaceController();
		applicationController = mainController.getApplicationController();
		stateController = mainController.getStateController();
	}
	
	@Override
	public void startApplication(){
		startApplication(new String[]{});
	}
	
	@Override
	public void startApplication(String[] consoleArguments) {
		setServiceListeners();
		mainController.readArguments(consoleArguments);
		mainController.startGui();
	}
	
	@Override
	public void addLocaleChangeListener(ILocaleChangeListener listener) {
		localeController.addLocaleChangeListener(listener);
	}

	@Override
	public Locale getLocale() {
		return localeController.getLocale();
	}
	
	@Override
	public Element getWorkspaceData() {
		Element data = new Element("workspace");
		Workspace workspace = workspaceController.getCurrentWorkspace();
		data.setAttribute("name", workspace.getName());
		return data;
	}
	
	@Override
	public void loadWorkspaceData(Element workspaceData) {
		try {
			String workspaceName = workspaceData.getAttributeValue("name");
			workspaceController.createWorkspace(workspaceName);
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
	public String getTranslatedString(String stringIdentifier){
		return localeController.getTranslatedString(stringIdentifier);
	}
	
	@Override
	public List<String> getStringIdentifiers(String translatedString){
		return new ArrayList<String>();
	}
	
	@Override
	public void centerDialog(JDialog dialog){
		DialogUtils.alignCenter(dialog);
	}
	
	@Override
	public ThreadWithLoader getThreadWithLoader(String progressInfoText, Runnable threadTask) {
		ThreadWithLoader loader = new ThreadWithLoader(mainController, progressInfoText, threadTask);
		return loader;
	}
	
	@Override
	public void setServiceListeners(){
		stateController.setServiceListeners();
	}
	
	public MainController getMainController(){
		return mainController;
	}

}
