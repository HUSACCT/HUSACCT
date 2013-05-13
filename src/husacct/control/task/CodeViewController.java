package husacct.control.task;

import husacct.control.task.codeviewer.CodeviewerService;
import husacct.control.task.codeviewer.EclipseCodeviewerImpl;
import husacct.control.task.codeviewer.InternalCodeviewerImpl;
import husacct.control.task.configuration.ConfigurationManager;
import husacct.control.task.configuration.NonExistingSettingException;

import java.util.ArrayList;

public class CodeViewController {
	
	private ConfigurationManager configurationManager;
	private CodeviewerService currentCodeviewer;
	
	public CodeViewController(MainController mainController) {
		configurationManager = mainController.getConfigurationManager();
		setCurrentCodeviewer();
	}
	
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		currentCodeviewer.displayErrorsInFile(fileName, errorLines);
	}
	
	public void setCurrentCodeviewer() {
		try {
			if(!configurationManager.getPropertyAsBoolean("ExternalCodeviewer")) {
				currentCodeviewer = new InternalCodeviewerImpl();
			} else {
				currentCodeviewer = new EclipseCodeviewerImpl(configurationManager);
			}
		} catch (NonExistingSettingException e) {
			e.printStackTrace();
		}
		
	}
}
