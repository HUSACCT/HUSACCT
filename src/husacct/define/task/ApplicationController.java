package husacct.define.task;

import husacct.define.presentation.ApplicationJFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;


public class ApplicationController implements ActionListener {
	public ApplicationJFrame jframe;
	private static ApplicationController instance;
	
	private Logger logger;
	
	public static ApplicationController getInstance() {
		return instance == null ? (instance = new ApplicationController()) : instance;
	}
	/**
	 * MainController constructor. This constructor will initialize a new Definition and Analyse controller.
	 */
	public ApplicationController() {
		logger = Logger.getLogger(ApplicationController.class);
	}

	public ApplicationJFrame getApplicationFrame(){
		return jframe;
	}
	/**
	 * Start the application with GUI by calling this method.
	 */
	public void initUi() {
		// Create a new instance of the jframe
		jframe = new ApplicationJFrame();

		// This method sets the definition jpanel in the jframe.
		DefinitionController definitionController = DefinitionController.getInstance();
		definitionController.initSettings();
		jframe.setContentView(definitionController.initUi());

		// Set the visibility of the jframe to true so the jframe is now visible
//		UiDialogs.showOnScreen(0, jframe);
		jframe.setVisible(true);
	}

	/**
	 * Start the application without GUI by calling this method.
	 */
	public void initCommand() {
		logger.info("initCommand()");

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String helpCommando = "Available commands are:\n" + "Help\tRequest help\n" + "Exit\tExit the application";
		String lineseperator = "----------------------------------------------------------------------------------------";
		String cmdLine = "Enter command: ";
		try {
			logger.info(helpCommando);
			logger.info(lineseperator);
			logger.info(cmdLine);

			String line = "";
			while ((line = in.readLine()) != null) {
				if (line.startsWith("help")) {
					logger.info(helpCommando);
				} else if (line.startsWith("exit")) {
					System.exit(0);
				} else {
					logger.info("Sorry, unknown commando");
				}
				logger.info(lineseperator);
				logger.info(cmdLine);
			}
		} catch (Exception e) {
			logger.info("initCommand() - Exception: " + e);
		}
	}

	public void actionPerformed(ActionEvent action) {

	}

}
