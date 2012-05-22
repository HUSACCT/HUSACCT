package husacct.control.task;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.control.presentation.util.AboutDialog;
import husacct.control.presentation.util.LoadingDialog;
import husacct.control.presentation.util.SetApplicationDialog;

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
	
	public void setApplicationData(ApplicationDTO applicationDTO) {
		ServiceProvider.getInstance().getDefineService().createApplication(
				applicationDTO.name, 
				applicationDTO.paths, 
				applicationDTO.programmingLanguage, 
				applicationDTO.version
		);
		
		analyseApplication();
	}
	
	private void analyseApplication(){
		AnalyseTask analyseTask = new AnalyseTask();
		
		final LoadingDialog loadingDialog = new LoadingDialog(mainController, "Analysing application");
		final Thread analyseThread = new Thread(analyseTask);
		Thread loadingThread = new Thread(loadingDialog);
		
		// Use new thread to listen if analysethread is finished
		Thread monitorThread = new Thread(new Runnable() {
			public void run() {
				try {
					analyseThread.join();
					loadingDialog.dispose();
					mainController.getViewController().showApplicationOverviewGui();
					logger.debug("analyse thread finished");
				} catch (InterruptedException exception){
					logger.debug("analyse thread interrupted");
				}
				
			}
		});
		
		loadingDialog.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				analyseThread.interrupt();
			}
		});
		
		loadingThread.setName("loading");
		analyseThread.setName("analyse");
		monitorThread.setName("monitor");
		
		loadingThread.start();
		analyseThread.start();
		monitorThread.start();
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