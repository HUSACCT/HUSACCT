package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.control.presentation.util.AboutHusacctFrame;
import husacct.control.presentation.util.SetApplicationFrame;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class ApplicationController {

	private static MainController mainController;
	private static Logger logger = Logger.getLogger(ApplicationController.class);
	
	public ApplicationController(MainController mainController) {
		ApplicationController.mainController = mainController;
	}

	public void showApplicationDetailsGui(){
		new SetApplicationFrame(ApplicationController.mainController);
	}
	
	public void setApplicationData(ApplicationDTO applicationDTO) {
		ServiceProvider.getInstance().getDefineService().createApplication(
				applicationDTO.name, 
				applicationDTO.paths, 
				applicationDTO.programmingLanguage, 
				applicationDTO.version
		);
		ServiceProvider.getInstance().getAnalyseService().analyseApplication();
	}
	
	public void showAboutHusacctGui(){
		new AboutHusacctFrame();
	}
	
	public static void showErrorMessage(String message){
		if(ApplicationController.mainController != null){
			JOptionPane.showMessageDialog(ApplicationController.mainController.getMainGui(),
				    message,
				    "Error",
				    JOptionPane.ERROR_MESSAGE);
		}
		ApplicationController.logger.error("Error: " + message);
	}
}