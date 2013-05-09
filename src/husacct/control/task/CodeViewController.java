package husacct.control.task;

import husacct.control.task.configuration.NonExistingSettingException;

import java.util.ArrayList;

public class CodeViewController {
	
	private MainController mainController;
	
	public CodeViewController(MainController mainController) {
		this.mainController = mainController;
	}
	
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		String application;
		try {
			application = mainController.getConfigurationManager().getProperty("CodeViewApplication");
		} catch (NonExistingSettingException e) {
			application = "default";
		}
		switch(application) {
		case "default":
			
			break;
		
		default:
			break;
		}
	}
}
