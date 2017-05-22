package husacct.control.task.codeviewer;

import husacct.ServiceProvider;
import husacct.common.OSDetector;
import husacct.control.task.configuration.ConfigurationManager;
import husacct.validate.domain.validation.Severity;

import java.io.IOException;
import java.util.HashMap;

public class ExternalCodeviewerImpl implements CodeviewerService {
	
	public ExternalCodeviewerImpl () {}
	
	@Override
	public void displayErrorsInFile(String fileName, HashMap<Integer, Severity> errors) {
		String location = "";
		location = ConfigurationManager.getProperty("IDELocation");
		switch(OSDetector.getOS()) {
		case LINUX:
			break;
		case MAC:
			break;
		case WINDOWS:
			try {
				Runtime.getRuntime().exec(location + " --launcher.openFile \"" + fileName + "\"");
			} catch (IOException e) {
				String errorMessage = "Unable to open external code viewer at path: " + location;
				ServiceProvider.getInstance().getControlService().showErrorMessage(errorMessage);
				//e.printStackTrace();
			}
			break;
		}
	}

}