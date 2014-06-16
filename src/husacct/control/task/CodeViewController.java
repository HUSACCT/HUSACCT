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
	private String fileName;
	
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

	// ====================================
	// Display errors in file (by ClassPath)
	// classPath, <linenumber , severity>
	// ====================================
	public void displayErrorsInFile(String classPath, HashMap<Integer, Severity> errors) {
		String path = findFilePath(classPath);
		// Check if the path was converted
		if(path != ""){
			setCurrentCodeviewer();
			currentCodeviewer.displayErrorsInFile(path, errors);
		}
		else{
			// Path empty, thus not found, show error.
			String message = ServiceProvider.getInstance().getLocaleService().getTranslatedString("CodeViewerNoSourceMsg") + " " + fileName;

			JOptionPane.showMessageDialog(
				null,
				message,
				ServiceProvider.getInstance().getLocaleService().getTranslatedString("CodeViewerNoSource"),
				JOptionPane.ERROR_MESSAGE
			);
		}
	}
	

	// ====================================
	// Check if file exists.
	// ====================================
	private boolean fileExists(String file){
		File f = new File(file);
		return f.exists();
	}
	
	// ====================================
	// Check if path is dir.
	// ====================================
	private boolean isDir(String file){
		File f = new File(file);
		return f.isDirectory();
	}

	// ====================================
	// Find file path by the class path
	// ====================================
	// TODO Multiple project support
	// TODO Better solution language
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
		
		// Final extension
		String extension = "";
		switch(project.programmingLanguage) {
			case "Java": extension = ".java"; break;
			case "C#": extension = ".cs"; break;
		}
		
		// Check default conversion
		fileName = classPath;
		fileName = rootPath + fileName.replace(".", File.separator) + extension;
		if(fileExists(fileName)){
			logger.info("Basic path converter found the path: " + fileName);
			filePath = fileName;
		}
		
		// Check if the folder names contain dots
		if(filePath == ""){
			String[] classParts = classPath.split(".");
			String winPath = rootPath;
			
			for(String pathPart : classParts){
				if(fileExists(winPath + extension))
					break;
				if(isDir(winPath + pathPart))
					winPath += pathPart + File.separator;
				else
					winPath += pathPart + ".";
			}
			winPath += extension;
			
			if(fileExists(winPath)){
				filePath = winPath;
				logger.info("Advanced folder explorer found the path: " + fileName);
			}
		}
		
		// If not found log
		if(filePath == "")
			logger.info("No valid file path could be found for: " + fileName);
		
		// Return path, empty if not found.
		return filePath;
	}
}