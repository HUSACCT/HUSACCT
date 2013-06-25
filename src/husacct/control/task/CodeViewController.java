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

public class CodeViewController {
	
	private IControlService controlService;
	private CodeviewerService currentCodeviewer;
	
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
		setCurrentCodeviewer();
		currentCodeviewer.displayErrorsInFile(path, errors);
	}
	
	//TODO Multiple project support
	//TODO Better solution language
	public String findFilePath(String fileName) {
		if(controlService == null)
			controlService = ServiceProvider.getInstance().getControlService();
		ApplicationDTO application = controlService.getApplicationDTO();
		ProjectDTO project = application.projects.get(0);
		fileName = project.paths.get(0) + File.separator + fileName.replace(".", File.separator);
		switch(project.programmingLanguage) {
			case "Java":
				fileName += ".java";
				break;
			case "C#":
				fileName += ".cs";
				break;
		}
		
		return fileName;
	}
}