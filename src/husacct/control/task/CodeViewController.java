package husacct.control.task;

import husacct.ServiceProvider;
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
		setCurrentCodeviewer();
		currentCodeviewer.displayErrorsInFile(fileName, errorLines);
	}
	
	public void setCurrentCodeviewer() {
		if(!ConfigurationManager.getPropertyAsBoolean("ExternalCodeviewer", "false")) {
			currentCodeviewer = new InternalCodeviewerImpl();
		} else {
			currentCodeviewer = new EclipseCodeviewerImpl();
		}
	}
}