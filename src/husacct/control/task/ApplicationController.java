package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.locale.ILocaleService;
import husacct.control.IControlService;
import husacct.control.presentation.util.AboutDialog;
import husacct.control.presentation.util.ConfigurationDialog;
import husacct.control.presentation.util.DocumentationDialog;
import husacct.control.presentation.util.HelpDialog;
import husacct.control.presentation.util.LoadingDialog;
import husacct.control.presentation.util.SetApplicationDialog;
import husacct.control.task.threading.ThreadWithLoader;

import java.awt.Component;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class ApplicationController {

	private MainController mainController;
	private Logger logger = Logger.getLogger(ApplicationController.class);
	private LoadingDialog currentLoader;
	private Thread currentThread;
	
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
				applicationDTO.projects,
				applicationDTO.version
		);
	}
	
	public void analyseApplication(){
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		ApplicationDTO applicationDTO = ServiceProvider.getInstance().getDefineService().getApplicationDetails();

		ThreadWithLoader analyseThread = controlService.getThreadWithLoader(localeService.getTranslatedString("AnalysingApplication"), new AnalyseTask(mainController,applicationDTO));
		currentLoader = analyseThread.getLoader();
		currentThread = analyseThread.getThread();
		currentLoader.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {		
				mainController.getStateController().setAnalysing(false);

				logger.debug("Stopping Thread");				
			}			
		});	
		analyseThread.run();	
	}
	
	public LoadingDialog getCurrentLoader() {
		return this.currentLoader;
	}
	
	public void setCurrentLoader(LoadingDialog ld) {
		this.currentLoader = ld;
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
	
	public void showConfigurationGUI() {
		new ConfigurationDialog(mainController);
	}

	public void showDocumentationGUI() {
		new DocumentationDialog(mainController);
		
	}
	public void showHelpGUI(Component component) {
		new HelpDialog(mainController, component);
	}
}