package husacct.define.task;

import husacct.define.presentation.ApplicationJInternalFrame;


public class ApplicationController {
	public ApplicationJInternalFrame jframe;
	private static ApplicationController instance;
	
	public static ApplicationController getInstance() {
		return instance == null ? (instance = new ApplicationController()) : instance;
	}
	
	public static void setInstance(ApplicationController aC) {
		instance = aC;
	}

	public ApplicationController() {
		
	}

	public ApplicationJInternalFrame getApplicationFrame(){
		return jframe;
	}
	/**
	 * Start the application with GUI by calling this method.
	 */
	public void initUi() {
		// Create a new instance of the jframe
		jframe = new ApplicationJInternalFrame();

		// This method sets the definition jpanel in the jframe.
		DefinitionController definitionController = DefinitionController.getInstance();
		definitionController.initSettings();
		jframe.setContentView(definitionController.initUi());

		jframe.setVisible(true);
	}

}
