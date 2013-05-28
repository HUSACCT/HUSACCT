package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ProjectDTO;
import husacct.control.IControlService;
import husacct.control.task.codeviewer.CodeviewerService;
import husacct.control.task.codeviewer.EclipseCodeviewerImpl;
import husacct.control.task.codeviewer.InternalCodeviewerImpl;
import husacct.control.task.configuration.ConfigurationManager;

import java.util.ArrayList;

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
}