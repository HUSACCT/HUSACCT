package husacct.define.task;

import husacct.define.presentation.ApplicationJFrame;
import husacct.define.presentation.utils.Log;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;


public class ApplicationController implements ActionListener {
	public ApplicationJFrame jframe;
	private static ApplicationController instance;
	
	public static ApplicationController getInstance() {
		return instance == null ? (instance = new ApplicationController()) : instance;
	}
	/**
	 * MainController constructor. This constructor will initialize a new Definition and Analyse controller.
	 */
	public ApplicationController() {
		Log.i(this, "constructor()");
	}

	public ApplicationJFrame getApplicationFrame(){
		return jframe;
	}
	/**
	 * Start the application with GUI by calling this method.
	 */
	public void initUi() {
		Log.i(this, "initUi()");

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
		Log.i(this, "initCommand()");

		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		String helpCommando = "Available commands are:\n" + "Help\tRequest help\n" + "Exit\tExit the application";
		String lineseperator = "----------------------------------------------------------------------------------------";
		String cmdLine = "Enter command: ";
		try {
			Log.i(this, helpCommando);
			Log.i(this, lineseperator);
			Log.i(this, cmdLine);

			String line = "";
			while ((line = in.readLine()) != null) {
				if (line.startsWith("help")) {
					Log.i(this, helpCommando);
				} else if (line.startsWith("exit")) {
					System.exit(0);
				} else {
					Log.i(this, "Sorry, unknown commando");
				}
				Log.i(this, lineseperator);
				Log.i(this, cmdLine);
			}
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "initCommand() - Exception: " + e);
		}
	}

	public void actionPerformed(ActionEvent action) {

	}

}
