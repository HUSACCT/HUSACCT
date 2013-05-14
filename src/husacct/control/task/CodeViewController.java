package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.task.codeviewer.CodeviewerService;
import husacct.control.task.codeviewer.EclipseCodeviewerImpl;
import husacct.control.task.codeviewer.InternalCodeviewerImpl;
import husacct.control.task.configuration.NonExistingSettingException;

import java.util.ArrayList;

public class CodeViewController {
	
	private IControlService controlService;
	private CodeviewerService currentCodeviewer;
	
	public CodeViewController(MainController mainController) {
		controlService = ServiceProvider.getInstance().getControlService();
	}
	
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		setCurrentCodeviewer();
		currentCodeviewer.displayErrorsInFile(fileName, errorLines);
	}
	
	public void setCurrentCodeviewer() {
		try {
			if(!controlService.getPropertyAsBoolean("ExternalCodeviewer")) {
				currentCodeviewer = new InternalCodeviewerImpl();
			} else {
				currentCodeviewer = new EclipseCodeviewerImpl();
			}
		} catch (NonExistingSettingException e) {
			e.printStackTrace();
		}
		
	}
}