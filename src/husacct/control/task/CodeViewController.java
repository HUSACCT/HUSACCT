package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.control.IControlService;
import husacct.control.task.codeviewer.CodeviewerService;
import husacct.control.task.codeviewer.EclipseCodeviewerImpl;
import husacct.control.task.codeviewer.InternalCodeviewerImpl;
import husacct.control.task.configuration.ConfigurationManager;
import husacct.validate.domain.validation.Severity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class CodeViewController {
	
	private IControlService controlService;
	private CodeviewerService currentCodeviewer;
	private static Logger logger = Logger.getLogger(ServiceProvider.class);
	
	public CodeViewController(MainController mainController) {
		controlService = ServiceProvider.getInstance().getControlService();
	}
	
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		if(controlService == null)
			controlService = ServiceProvider.getInstance().getControlService();
		ApplicationDTO application = controlService.getApplicationDTO();
		ProjectDTO project = application.projects.get(0);
		fileName = project.paths.get(0) + "\\" + fileName;
		setCurrentCodeviewer();
		currentCodeviewer.displayErrorsInFile(fileName, errorLines);
	}
	
	public void setCurrentCodeviewer() {
		String ExternalCodeviewer = ConfigurationManager.getProperty("ExternalCodeviewer");
		boolean enabled = Boolean.parseBoolean(ExternalCodeviewer);
		if(enabled) {
			currentCodeviewer = new EclipseCodeviewerImpl();
		} else {
			currentCodeviewer = new InternalCodeviewerImpl();
		}
	}

	public void displayErrorsInFile(String fileName, HashMap<Integer, Severity> errors) {
		String path = findFilePath(fileName);
		// Check if the path was converted
		if(path != ""){
			setCurrentCodeviewer();
			currentCodeviewer.displayErrorsInFile(path, errors);
		}
		else{
			// Path empty, thus not found, show error.
			JOptionPane.showMessageDialog(
				null,
				ServiceProvider.getInstance().getLocaleService().getTranslatedString("CodeViewerNoSourceMsg"),
				ServiceProvider.getInstance().getLocaleService().getTranslatedString("CodeViewerNoSource"),
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
	
	private boolean fileExists(String file){
		File f = new File(file);
		return f.exists();
	}
	
	//TODO Multiple project support
	//TODO Better solution language
	public String findFilePath(String classPath) {
		// Init control service if not set
		if(controlService == null)
			controlService = ServiceProvider.getInstance().getControlService();
		
		// Convert classPath to filePath
		String filePath = "";
		logger.info("Trying to find file path for class path: " + classPath);
		
		// Grab root path
		ApplicationDTO application = controlService.getApplicationDTO();
		ProjectDTO project = application.projects.get(0);
		String rootPath = project.paths.get(0) + File.separator;
		
		// Check default convertion
		String fileName = classPath;
		fileName = rootPath + fileName.replace(".", File.separator);
		switch(project.programmingLanguage) {
			case "Java": fileName += ".java"; break;
			case "C#": fileName += ".cs"; break;
		}
		if(fileExists(fileName)){
			logger.info("Basic path converter found the path: " + fileName);
			filePath = fileName;
		}
		
		// If not found log
		if(filePath == "")
			logger.info("No valid file path could be found.");
		
		// Return path, empty if not found.
		return filePath;
	}
}