package husacct.control.task.codeviewer;

import husacct.common.OSDetector;
import husacct.control.task.configuration.ConfigurationManager;

import java.io.IOException;
import java.util.ArrayList;

public class EclipseCodeviewerImpl implements CodeviewerService {
	
	
	public EclipseCodeviewerImpl () {}
	
	@Override
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		String location = "";
		location = ConfigurationManager.getProperty("IDELocation", "");
		switch(OSDetector.getOS()) {
		case LINUX:
			break;
		case MAC:
			break;
		case WINDOWS:
			try {
				Runtime.getRuntime().exec(location + " --launcher.openFile \"" + fileName + "\"");
				/* TODO Remove after testing
				 Runtime.getRuntime().exec(new String[] {
					"rundll32", 
					"url.dll,FileProtocolHandler",
					location + " --launcher.openFile " + fileName
				});
				 */
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		}
	}

}
