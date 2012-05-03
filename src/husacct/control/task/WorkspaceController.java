package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.savechain.ISaveable;
import husacct.control.domain.Workspace;
import husacct.control.presentation.workspace.CloseWorkspaceDialog;
import husacct.control.presentation.workspace.CreateWorkspaceDialog;
import husacct.control.presentation.workspace.OpenWorkspaceFrame;
import husacct.control.presentation.workspace.SaveWorkspaceFrame;
import husacct.control.task.workspace.IWorkspaceResource;
import husacct.control.task.workspace.ResourceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class WorkspaceController {

	private Logger logger = Logger.getLogger(WorkspaceController.class);
	private static Workspace currentWorkspace;

	private static MainController mainController;
	
	public WorkspaceController(MainController mainController){
		this.mainController = mainController;
	}

	public void showCreateWorkspaceGui() {
		new CreateWorkspaceDialog(mainController);
	}

	public void showCloseWorkspaceGui(){
		new CloseWorkspaceDialog(mainController);
	}
	
	public void showOpenWorkspaceGui() {
		new OpenWorkspaceFrame(mainController);
	}
	
	public SaveWorkspaceFrame showSaveWorkspaceGui() {
		return new SaveWorkspaceFrame(mainController);

	}
	
	public void createWorkspace(String name){
		Workspace workspace = new Workspace();
		workspace.setName(name);
		WorkspaceController.currentWorkspace = workspace;
		mainController.getMainGui().setTitle(name);
	}
	
	public void closeWorkspace() {
		WorkspaceController.currentWorkspace = null;
		mainController.getMainGui().setTitle("");
	}
	
	public void saveWorkspace(String resourceIdentifier, HashMap<String, Object> dataValues) {
		IWorkspaceResource workspaceResource = ResourceFactory.get(resourceIdentifier);
		Document document = getWorkspaceData();
		workspaceResource.save(document, dataValues);
	}
	
	public void loadWorkspace(String resourceIdentifier, HashMap<String, Object> dataValues){
		IWorkspaceResource workspaceResource = ResourceFactory.get(resourceIdentifier);
		Document doc = workspaceResource.load(dataValues);
		loadWorkspace(doc);
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

	public void loadWorkspace(Document document){
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
	
	public static boolean isOpenWorkspace(){
		if(WorkspaceController.currentWorkspace != null){
			return true;
		}
		return false;
	}
	
	public static Workspace getCurrentWorkspace(){
		return WorkspaceController.currentWorkspace;
	}

	public static void setWorkspace(Workspace workspace) {
		WorkspaceController.currentWorkspace = workspace;
		if(mainController != null) {
			mainController.getMainGui().setTitle(workspace.getName());
		}
	}

}
