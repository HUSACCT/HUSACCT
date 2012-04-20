package husacct.define.presentation;

import husacct.define.task.ApplicationController;

public class MainGUI {

	public static void main(String args[])
	{
		// Init Main Frame
//		JFrame mainFrame = new JFrame();
//		mainFrame.setVisible(true);
//		mainFrame.pack();
//		mainFrame.setSize(1100, 800);
		
		// Start the Application controller
		ApplicationController gui = new ApplicationController();
		
		// Init the UI internal frame
		gui.initUi();
		
		// Add Internal frame to Main Frame
//		mainFrame.add(gui.jframe);
	}
}