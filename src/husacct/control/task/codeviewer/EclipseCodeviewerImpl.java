package husacct.control.task.codeviewer;

import husacct.ServiceProvider;
import husacct.common.OSDetector;
import husacct.control.IControlService;
import husacct.control.task.configuration.NonExistingSettingException;

import java.io.IOException;
import java.util.ArrayList;

public class EclipseCodeviewerImpl implements CodeviewerService {
	
	private IControlService controlService;
	
	public EclipseCodeviewerImpl () {
		controlService = ServiceProvider.getInstance().getControlService();
	}
	
	@Override
	public void displayErrorsInFile(String fileName, ArrayList<Integer> errorLines) {
		String location = "";
		try {
			location = controlService.getProperty("IDELocation");
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
