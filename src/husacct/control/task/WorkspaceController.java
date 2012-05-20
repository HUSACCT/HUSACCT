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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class WorkspaceController {

	private Logger logger = Logger.getLogger(WorkspaceController.class);
	private Workspace currentWorkspace;

	private MainController mainController;
	
	public WorkspaceController(MainController mainController){
		this.mainController = mainController;
		currentWorkspace = null;
	}

	public void showCreateWorkspaceGui() {
		new CreateWorkspaceDialog(mainController);
	}
	
	public void showOpenWorkspaceGui() {
		new OpenWorkspaceDialog(mainController);
	}
	
	public SaveWorkspaceDialog showSaveWorkspaceGui() {
		return new SaveWorkspaceDialog(mainController);
	}
	
	public void createWorkspace(String name){
		logger.debug("New workspace: " + name);
		Workspace workspace = new Workspace();
		workspace.setName(name);
		currentWorkspace = workspace;
		if(mainController.guiEnabled) mainController.getMainGui().setTitle(name);
	}
	
	public void closeWorkspace() {
		currentWorkspace = null;
		if(mainController.guiEnabled) {
			mainController.getMainGui().setTitle("");
			mainController.getViewController().closeAll();
		}
		ServiceProvider.getInstance().resetServices();
	}
	
	public boolean saveWorkspace(String resourceIdentifier, HashMap<String, Object> dataValues) {
		IResource workspaceResource = ResourceFactory.get(resourceIdentifier);
		Document document = getWorkspaceData();
		return workspaceResource.save(document, dataValues);
	}
	
	public boolean loadWorkspace(String resourceIdentifier, HashMap<String, Object> dataValues){
		IResource workspaceResource = ResourceFactory.get(resourceIdentifier);
		Document doc = workspaceResource.load(dataValues);
		return loadWorkspace(doc);
	}
	
	public Document getWorkspaceData(){
		Element rootElement = new Element("husacct");
		rootElement.setAttribute("version", "1");		
		
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

	public boolean loadWorkspace(Document document){
		try {
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
			IControlService controlService = ServiceProvider.getInstance().getControlService();
			controlService.showErrorMessage(message);
		}
		return false;
	}
	
	private List<ISaveable> getSaveableServices() {
		List<ISaveable> saveableServices = new ArrayList<ISaveable>();
		
		if(ServiceProvider.getInstance().getControlService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getControlService());
		}
		
		if(ServiceProvider.getInstance().getDefineService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getDefineService());
		}
		
		if(ServiceProvider.getInstance().getAnalyseService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getAnalyseService());
		}

		if(ServiceProvider.getInstance().getGraphicsService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getGraphicsService());
		}
		
		if(ServiceProvider.getInstance().getValidateService() instanceof ISaveable){
			saveableServices.add((ISaveable) ServiceProvider.getInstance().getValidateService());
		}
		return saveableServices;
	}
	
	public boolean isOpenWorkspace(){
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
		if(mainController != null && mainController.guiEnabled) {
			mainController.getMainGui().setTitle(workspace.getName());
		}
	}

}
