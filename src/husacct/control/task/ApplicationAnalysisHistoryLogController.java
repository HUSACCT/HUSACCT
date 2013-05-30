package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.ProjectDTO;
import husacct.control.domain.Workspace;
import husacct.control.presentation.log.AnalysisHistoryOverviewFrame;
import husacct.control.task.configuration.ConfigurationManager;
import husacct.control.task.resources.IResource;
import husacct.control.task.resources.ResourceFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;
import org.jdom2.Document;
import org.jdom2.Element;

public class ApplicationAnalysisHistoryLogController extends MethodLogController{

	private Logger logger = Logger.getLogger(ApplicationAnalysisHistoryLogController.class);
	private Workspace currentWorkspace;

	private MainController mainController;
	
	private File logFile = new File(ConfigurationManager.getProperty("PlatformIndependentAppDataFolder") + ConfigurationManager.getProperty("ApplicationHistoryXMLFilename"));
	
	public ApplicationAnalysisHistoryLogController(MainController mainController){
		this.mainController = mainController;
		currentWorkspace = null;
		//logMethod("Instantiate logcontroller");
	}
	
	public boolean logFileExists(){
		//logMethod("Check if " + logFile.getAbsolutePath() + " exists.");
		//printLoggedMethods();
		return logFile.exists();
	}
	
	public HashMap<String, HashMap<String, String>> getApplicationHistoryFromFile(String workspace, String application, ArrayList<ProjectDTO> projects){
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
							
							//Given projects
							for(ProjectDTO project : projects){
								
								//Project
								for(Element projectElement : applicationElement.getChildren()){
									if(projectElement.getAttributeValue("name").equals(project.name)){
										
										//Analysis
										for(Element analysisElement : projectElement.getChildren()){
											
											//Analysis info
											HashMap<String, String> analysisInfo = new HashMap<String, String>();
											analysisInfo.put("application", applicationElement.getAttributeValue("name"));
											analysisInfo.put("project", projectElement.getAttributeValue("name"));
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
			}
			
		} catch (Exception e) {
			logger.debug("Unable load application analysis history file: " + e.getMessage());
		}
		
		return output;
	}
	
	public int getNumberOfAnalyses(String workspace, String application, ArrayList<ProjectDTO> projects){
		HashMap<String, Object> resourceData = new HashMap<String, Object>();
		resourceData.put("file", logFile);
		IResource xmlResource = ResourceFactory.get("xml");
		
		int output = 0;
		
		try {
			Document doc = xmlResource.load(resourceData);	
			Element xmlFileRootElement = doc.getRootElement();
			
			//Workspace
			for(Element workspaceElement : xmlFileRootElement.getChildren()){
				
				if(workspaceElement.getAttributeValue("name").equals(workspace)){
					
					//Application
					for(Element applicationElement : workspaceElement.getChildren()){
						if(applicationElement.getAttributeValue("name").equals(application)){
							
							//Given projects
							for(ProjectDTO project : projects){
								
								//XML projects
								for(Element projectElement : applicationElement.getChildren()){
									if(projectElement.getAttributeValue("name").equals(project.name)){
										output += projectElement.getChildren().size();
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

	public void showApplicationAnalysisHistoryOverview() {
		if(logFileExists()){
			String workspace = "";
			String application = "";
			ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
			
			try{
				workspace = mainController.getWorkspaceController().getCurrentWorkspace().getName();
				application = ServiceProvider.getInstance().getDefineService().getApplicationDetails().name;
				projects = ServiceProvider.getInstance().getDefineService().getApplicationDetails().projects;
			}catch(Exception e){
				JOptionPane.showMessageDialog(null, ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoWorkspaceApplicationProjectIsOpen"), ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoWorkspaceApplicationProjectIsOpenTitle"), JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(getNumberOfAnalyses(workspace, application, projects)<1){
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
