package husacct.control.task;

import husacct.control.domain.Workspace;
import husacct.control.presentation.log.AnalysisHistoryOverviewFrame;
import husacct.control.task.resources.IResource;
import husacct.control.task.resources.ResourceFactory;

import java.io.File;
import java.util.HashMap;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class LogController {

	private Logger logger = Logger.getLogger(LogController.class);
	private Workspace currentWorkspace;

	private MainController mainController;
	//TODO: Change this to be dynamic:
	private String logFilePath = "D:\\Software\\Databases\\Eclipse Git Repository\\HUSACCT\\src\\husacct\\common\\resources\\logging\\applicationanalysishistory.xml";
	
	public LogController(MainController mainController){
		this.mainController = mainController;
		currentWorkspace = null;
	}
	
	public HashMap<String, HashMap<String, String>> getApplicationHistoryFromFile(String workspace, String application, String project){
		HashMap<String, HashMap<String, String>> output = new HashMap<String, HashMap<String, String>>();
		
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", new File(logFilePath));
		IResource xmlResource = ResourceFactory.get("xml");
		try {
			Document doc = xmlResource.load(resourceData);	
			Element xmlFileRootElement = doc.getRootElement();
			
			//Workspace
			for(Element workspaceElement : xmlFileRootElement.getChildren()){
				if(workspaceElement.getAttributeValue("name").equals(workspace)){
					
					//Application
					for(Element applicationElement : workspaceElement.getChildren()){
						if(applicationElement.getAttributeValue("name").equals(application)){
							
							//Project
							for(Element projectElement : applicationElement.getChildren()){
								if(projectElement.getAttributeValue("name").equals(project)){
									
									//Analysis
									for(Element analysisElement : projectElement.getChildren()){
										
										//Analysis info
										HashMap<String, String> analysisInfo = new HashMap<String, String>();
										analysisInfo.put("application", applicationElement.getAttributeValue("name"));
										for(Element analysisInfoElement : analysisElement.getChildren()){
											analysisInfo.put(analysisInfoElement.getName(), analysisInfoElement.getText());
										}
										
										//Add every analysis to hashmap
										output.put(analysisElement.getAttributeValue("timestamp"), analysisInfo);
									}
								}
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			logger.debug("Unable to import logical architecture: " + e.getMessage());
		}
		
		return output;
	}

	public void showApplicationAnalysisHistoryOverview() {
		new AnalysisHistoryOverviewFrame(mainController);
	}
	
	public Workspace getCurrentWorkspace(){
		return currentWorkspace;
	}
}
