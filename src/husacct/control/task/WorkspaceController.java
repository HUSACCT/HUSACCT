package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.savechain.ISaveable;
import husacct.control.domain.Workspace;
import husacct.control.presentation.workspace.CreateWorkspaceFrame;
import husacct.control.presentation.workspace.OpenWorkspaceFrame;
import husacct.control.presentation.workspace.SaveWorkspaceFrame;
import husacct.control.task.workspace.IWorkspaceResource;
import husacct.control.task.workspace.ResourceFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class WorkspaceController {

	private Logger logger = Logger.getLogger(WorkspaceController.class);
	private static Workspace currentWorkspace;

	public void showCreateWorkspaceGui() {
		new CreateWorkspaceFrame(this);
	}

	public void showOpenWorkspaceGui() {
		new OpenWorkspaceFrame(this);

	}
	
	public void showSaveWorkspaceGui() {
		new SaveWorkspaceFrame(this);

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

	public void closeWorkspace() {	
		Object[] options = { "Yes", "No", "Cancel" };
		int n = JOptionPane.showOptionDialog(null,
				"Save changes?",
				"Close workspace", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
		if (n == JOptionPane.YES_OPTION) {
			//saveWorkspace();
		} else if (n == JOptionPane.NO_OPTION) {
			System.out.println("no");
		} else if (n == JOptionPane.CANCEL_OPTION) {
			System.out.println("cancel");
		} else {
			System.out.println("none");
		}
		
	}
	
	public static boolean isOpenWorkspace(){
		if(WorkspaceController.currentWorkspace != null){
			return true;
		} else {
			return false;
		}
	}

}
