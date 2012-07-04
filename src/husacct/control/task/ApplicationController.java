package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;
import husacct.control.presentation.util.AboutDialog;
import husacct.control.presentation.util.SetApplicationDialog;
import husacct.control.task.threading.ThreadWithLoader;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class ApplicationController {

	private MainController mainController;
	private Logger logger = Logger.getLogger(ApplicationController.class);
	
	public ApplicationController(MainController mainController) {
		this.mainController = mainController;
	}

	public void showApplicationDetailsGui(){
		new SetApplicationDialog(mainController);
	}
	
	public void setAndAnalyseApplicationData(ApplicationDTO applicationDTO){
		setApplicationData(applicationDTO);
		analyseApplication();
	}
	
	public void setApplicationData(ApplicationDTO applicationDTO) {
		ServiceProvider.getInstance().getDefineService().createApplication(
				applicationDTO.name, 
				applicationDTO.paths, 
				applicationDTO.programmingLanguage, 
				applicationDTO.version
		);
	}
	
	public void analyseApplication(){
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		ThreadWithLoader analyseThread = controlService.getThreadWithLoader(localeService.getTranslatedString("AnalysingApplication"), new AnalyseTask());
		analyseThread.run();
	}
	
	public void showAboutHusacctGui(){
		new AboutDialog(mainController);
	}
	
	public void showErrorMessage(String message){
		JOptionPane.showMessageDialog(mainController.getMainGui(), message, "Error", JOptionPane.ERROR_MESSAGE);
		logger.error("Error: " + message);
	}
	
	public void showInfoMessage(String message){
		JOptionPane.showMessageDialog(mainController.getMainGui(), message, "Info", JOptionPane.INFORMATION_MESSAGE);
		logger.error("Info: " + message);
	}
}