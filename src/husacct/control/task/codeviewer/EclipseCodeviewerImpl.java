package husacct.control.task.codeviewer;

import husacct.common.OSDetector;
import husacct.control.task.configuration.ConfigurationManager;
import husacct.control.task.configuration.NonExistingSettingException;

import java.io.IOException;
import java.util.ArrayList;

public class EclipseCodeviewerImpl implements CodeviewerService {
	
	private ConfigurationManager configurationManager;
	
	public EclipseCodeviewerImpl (ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}
	
	@Override
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		String location = "";
		try {
			location = configurationManager.getProperty("IDELocation");
		} catch (NonExistingSettingException nese) {
			nese.printStackTrace();
		}
		switch(OSDetector.getOS()) {
		case LINUX:
			break;
		case MAC:
			break;
		case WINDOWS:
			try {
				Runtime.getRuntime().exec(new String[] {
					"rundll32", 
					"url.dll,FileProtocolHandler",
					location + " --launcher.openFile " + fileName
				});
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

}
