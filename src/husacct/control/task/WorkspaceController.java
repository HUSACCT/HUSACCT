package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.savechain.ISaveable;
import husacct.control.IControlService;
import husacct.control.domain.Workspace;
import husacct.control.presentation.workspace.CreateWorkspaceDialog;
import husacct.control.presentation.workspace.OpenWorkspaceDialog;
import husacct.control.presentation.workspace.SaveWorkspaceDialog;
import husacct.control.task.resources.IResource;
import husacct.control.task.resources.ResourceFactory;
import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WorkspaceController {

	private Logger logger = Logger.getLogger(WorkspaceController.class);
	private Workspace currentWorkspace;

	private MainController mainController;

	
	public WorkspaceController(MainController mainController){
		this.mainController = mainController;
		currentWorkspace = null;
	}

	public void showCreateWorkspaceGui() {
		showSaveWorkspaceGuiIfUnSaved();
		new CreateWorkspaceDialog(mainController);
	}
	
	public void showOpenWorkspaceGui() {
		showSaveWorkspaceGuiIfUnSaved();
		new OpenWorkspaceDialog(mainController);
	}
	
	public SaveWorkspaceDialog showSaveWorkspaceGui() {
		return new SaveWorkspaceDialog(mainController);
	}

	public void createWorkspace(String name){
		logger.info( new Date().toString() + " New workspace: " + name);
		Workspace workspace = new Workspace();
		workspace.setName(name);
		currentWorkspace = workspace;
		
		if(ServiceProvider.getInstance().getControlService().isGuiEnabled()){
			mainController.getMainGui().setTitle(name);
			mainController.getViewController().closeAll();
		}
		ServiceProvider.getInstance().resetServices();
		mainController.getStateController().checkState();
	}
	
	public void closeWorkspace() {
		showSaveWorkspaceGuiIfUnSaved();
		currentWorkspace = null;
		if(ServiceProvider.getInstance().getControlService().isGuiEnabled()) {
			mainController.getMainGui().setTitle("");
			mainController.getViewController().closeAll();
		}
		ServiceProvider.getInstance().resetServices();
		mainController.getStateController().checkState();
	}

	public boolean saveWorkspace(String resourceIdentifier, HashMap<String, Object> dataValues, HashMap<String, Object> config) {
		IResource workspaceResource = ResourceFactory.get(resourceIdentifier);
		Document document = getWorkspaceData();
 		return workspaceResource.save(document, dataValues, config);
	}
	
	public Document getWorkspaceData(){
		Element rootElement = new Element("husacct");
		rootElement.setAttribute("version", "5.4");		
		
		for(ISaveable service : getSaveableServices()){
			String serviceName = service.getClass().getName();
			try {
				Element container = new Element(serviceName);
				Element serviceData = service.getWorkspaceData();
				container.addContent(serviceData);
				rootElement.addContent(container);
			} catch (Exception e) {
				logger.debug("Unable to save workspacedata for " + serviceName + ": " + e.getMessage());
			}
		}

		Document doc = new Document(rootElement);
		return doc;
	}

	public boolean loadWorkspace(String resourceIdentifier, HashMap<String, Object> dataValues){
		IResource workspaceResource = ResourceFactory.get(resourceIdentifier);
		Document document = workspaceResource.load(dataValues);
		try {
			ServiceProvider.getInstance().resetServices();
			List<ISaveable> savableServices = getSaveableServices();
			if(document.hasRootElement()){
				Element rootElement = document.getRootElement();
				for(ISaveable service : savableServices){
					String serviceName = service.getClass().getName();
					List<Element> elementQuery = rootElement.getChildren(serviceName);
					for(Element serviceDataContainer : elementQuery){
						Element serviceData = serviceDataContainer.getChildren().get(0); 
						service.loadWorkspaceData(serviceData);
					}
				}
			}
			return true;
		} catch (Exception exception){
			String message = "Unable to load workspacedata\n" + exception.getMessage();
			if (ServiceProvider.getInstance().getControlService().isGuiEnabled()) {
				IControlService controlService = ServiceProvider.getInstance().getControlService();
				controlService.showErrorMessage(message);
			} else {
				logger.error(message);
			}
		}
		return false;
	}
	
	private List<ISaveable> getSaveableServices() {
		List<ISaveable> saveableServices = new ArrayList<ISaveable>();
		
		if(ServiceProvider.getInstance().getControlService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getControlService());
		}
		
		if(ServiceProvider.getInstance().getDefineService() instanceof ISaveable){
			saveableServices.add(ServiceProvider.getInstance().getDefineService());
		}
		
		if(ServiceProvider.getInstance().getAnalyseService() instanceof ISaveable){
			saveableServices.add(ServiceProvider.getInstance().getAnalyseService());
		}
		
		if(ServiceProvider.getInstance().getValidateService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getValidateService());
		}
		
		if(ServiceProvider.getInstance().getGraphicsService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getGraphicsService());
		}
		
		return saveableServices;
	}
	
	public boolean isAWorkspaceOpened(){
		if(currentWorkspace != null){
			return true;
		}
		return false;
	}
	
	public Workspace getCurrentWorkspace(){
		return currentWorkspace;
	}

	public void setWorkspace(Workspace workspace) {
		currentWorkspace = workspace;
		if(mainController != null && ServiceProvider.getInstance().getControlService().isGuiEnabled()) {
			mainController.getMainGui().setTitle(workspace.getName());
		}
	}
	
	private void showSaveWorkspaceGuiIfUnSaved() {
		if (isAWorkspaceOpened() && getCurrentWorkspace().isDirty()) {
			showSaveWorkspaceGui();
		}
	}
}
