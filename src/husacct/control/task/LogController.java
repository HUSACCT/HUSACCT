package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.domain.Workspace;
import husacct.control.presentation.log.AnalysisHistoryOverviewFrame;
import husacct.control.task.resources.IResource;
import husacct.control.task.resources.ResourceFactory;

import java.io.File;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class LogController {

	private Logger logger = Logger.getLogger(LogController.class);
	private Workspace currentWorkspace;

	private MainController mainController;
	
	//Hey, at least it's dynamic :)
	private File logFile = new File(new File("").getAbsolutePath().replace("\\", "\\\\") + "\\src\\husacct\\common\\resources\\logging\\applicationanalysishistory.xml".replace("\\", "\\\\"));
	
	public LogController(MainController mainController){
		this.mainController = mainController;
		currentWorkspace = null;
	}
	
	public boolean logFileExists(){
		return logFile.exists();
	}
	
	public HashMap<String, HashMap<String, String>> getApplicationHistoryFromFile(String workspace, String application, String project){
		HashMap<String, HashMap<String, String>> output = new HashMap<String, HashMap<String, String>>();
		
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", logFile);
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
			logger.debug("Unable load application analysis history file: " + e.getMessage());
		}
		
		return output;
	}
	
	public int getNumberOfAnalyses(String workspace, String application, String project){
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", logFile);
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
									return projectElement.getChildren().size();
								}
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			logger.debug("Unable load application analysis history file: " + e.getMessage());
		}
		
		return 0;
	}

	public void showApplicationAnalysisHistoryOverview() {
		if(logFileExists()){
			String workspace = "";
			String application = "";
			String project = "";
			
			try{
				workspace = mainController.getWorkspaceController().getCurrentWorkspace().getName();
				application = ServiceProvider.getInstance().getDefineService().getApplicationDetails().name;
				project = ServiceProvider.getInstance().getDefineService().getApplicationDetails().projects.get(0).name;
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoWorkspaceApplicationProjectIsOpen"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoWorkspaceApplicationProjectIsOpenTitle"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(getNumberOfAnalyses(workspace, application, project)<1){
				JOptionPane.showMessageDialog(null, ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoApplicationAnalysisHistory"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoApplicationAnalysisHistoryTitle"), JOptionPane.ERROR_MESSAGE);
			}else{
				new AnalysisHistoryOverviewFrame(mainController);
			}
		}else{
			JOptionPane.showMessageDialog(null, ServiceProvider.getInstance().getLocaleService().getTranslatedString("ApplicationAnalysisHistoryFileDoesntExist"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("ApplicationAnalysisHistoryFileDoesntExistTitle"), JOptionPane.ERROR_MESSAGE);
		}
	}
	
	public Workspace getCurrentWorkspace(){
		return currentWorkspace;
	}
}
