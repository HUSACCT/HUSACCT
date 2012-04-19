package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.savechain.ISaveable;
import husacct.control.domain.Workspace;
import husacct.control.presentation.workspace.CreateWorkspaceFrame;
import husacct.control.presentation.workspace.OpenWorkspaceFrame;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.XMLOutputter;

public class WorkspaceController {

	private Logger logger = Logger.getLogger(WorkspaceController.class);
	private static Workspace currentWorkspace;

	public void showCreateWorkspaceGui() {
		new CreateWorkspaceFrame(this);
	}

	public void showOpenWorkspaceGui() {
		new OpenWorkspaceFrame(this);

	}

	public void saveWorkspace() {

		try {
			
			Element controlData = null;
			Element defineData = null;
			Element analyseData = null;
			Element graphicsData = null;
			Element validateData = null;
			
			if(ServiceProvider.getInstance().getControlService() instanceof ISaveable){
				ISaveable service = (ISaveable) ServiceProvider.getInstance().getControlService();
				controlData = service.getWorkspaceData();
			}
			
			if(ServiceProvider.getInstance().getDefineService() instanceof ISaveable){
				ISaveable service = (ISaveable) ServiceProvider.getInstance().getDefineService();
				defineData = service.getWorkspaceData();
			}
			
			if(ServiceProvider.getInstance().getAnalyseService() instanceof ISaveable){
				ISaveable service = (ISaveable) ServiceProvider.getInstance().getAnalyseService();
				analyseData = service.getWorkspaceData();
			}
			
			if(ServiceProvider.getInstance().getGraphicsService() instanceof ISaveable){
				ISaveable service = (ISaveable) ServiceProvider.getInstance().getGraphicsService();
				graphicsData = service.getWorkspaceData();
			}
			
			if(ServiceProvider.getInstance().getValidateService() instanceof ISaveable){
				ISaveable service = (ISaveable) ServiceProvider.getInstance().getValidateService();
				validateData = service.getWorkspaceData();
			}
			
			Element controlDataContainer = new Element("control");
			Element defineDataContainer = new Element("define");
			Element analyseDataContainer = new Element("analyse");
			Element graphicsDataContainer = new Element("graphics");
			Element validateDataContainer = new Element("validate");
			
			if(controlData != null) controlDataContainer.addContent(controlData);
			if(defineData != null) defineDataContainer.addContent(defineData);
			if(analyseData != null) analyseDataContainer.addContent(analyseData);
			if(graphicsData != null) graphicsDataContainer.addContent(graphicsData);
			if(validateData != null) validateDataContainer.addContent(validateData);
			
			Element rootElement = new Element("husacct");
			rootElement.setAttribute("version", "1");
			
			rootElement.addContent(controlDataContainer);
			rootElement.addContent(defineDataContainer);
			rootElement.addContent(analyseDataContainer);
			rootElement.addContent(graphicsDataContainer);
			rootElement.addContent(validateDataContainer);
			
			Document doc = new Document(rootElement);
			XMLOutputter xmlOutputter = new XMLOutputter();
			
			logger.debug(xmlOutputter.outputString(doc));

		} catch (Exception e){
			logger.error(String.format("Unable to save workspace: %s", e));
		}
	
	}

	public void loadWorkspace(Document document){
		
	}
	
	public void closeWorkspace() {	
		Object[] options = { "Yes", "No", "Cancel" };
		int n = JOptionPane.showOptionDialog(null,
				"Save changes?",
				"Close workspace", JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE, null, options, options[2]);
		if (n == JOptionPane.YES_OPTION) {
			saveWorkspace();
		} else if (n == JOptionPane.NO_OPTION) {
			System.out.println("no");
		} else if (n == JOptionPane.CANCEL_OPTION) {
			System.out.println("cancel");
		} else {
			System.out.println("none");
		}
		
	}

}
