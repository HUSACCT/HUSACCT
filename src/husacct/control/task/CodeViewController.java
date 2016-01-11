package husacct.control.task;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.control.IControlService;
import husacct.control.presentation.codeviewer.CodeViewInternalFrame;
import husacct.control.task.codeviewer.CodeviewerService;
import husacct.control.task.codeviewer.ExternalCodeviewerImpl;
import husacct.control.task.codeviewer.InternalCodeviewerImpl;
import husacct.control.task.configuration.ConfigurationManager;
import husacct.validate.domain.validation.Severity;

import java.io.File;
import java.util.HashMap;

import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class CodeViewController {
	
	private IControlService controlService;
	private CodeviewerService currentCodeviewer;
	private static Logger logger = Logger.getLogger(ServiceProvider.class);
	private String fileName;
	MainController mainController;
	private CodeViewInternalFrame internalCodeViewerView;
	
	public CodeViewController(MainController mainController) {
		this.mainController = mainController;
		controlService = ServiceProvider.getInstance().getControlService();
		internalCodeViewerView = new CodeViewInternalFrame();
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
	
	public JInternalFrame getCodeViewInternalFrame() {
		return internalCodeViewerView;
	}

	private void setCurrentCodeviewer() {
		String ExternalCodeviewer = ConfigurationManager.getProperty("ExternalCodeviewer");
		boolean enabled = Boolean.parseBoolean(ExternalCodeviewer);
		if(enabled) {
			currentCodeviewer = new ExternalCodeviewerImpl();
		} else {
			currentCodeviewer = new InternalCodeviewerImpl(internalCodeViewerView, mainController);
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
	public String findFilePath(String classPath) {
		// Init control service if not set
		if(controlService == null)
			controlService = ServiceProvider.getInstance().getControlService();
		
		// Request sourceFilePath (since version 3.3)
		final IAnalyseService analyseService = ServiceProvider.getInstance().getAnalyseService();
		String sourceFilePath = analyseService.getSourceFilePathOfClass(classPath);
		if ((sourceFilePath != null) && !sourceFilePath.equals("")) {
			return sourceFilePath;
		}
		
		// Convert classPath to filePath (algorithm before version 3.3). Kept here as back-up.
		String filePath = "";
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
		
		// Return path; empty if no source is found.
		return filePath;
	}
}