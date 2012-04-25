package husacct.define.presentation;

import husacct.define.DefineServiceImpl;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;

public class MainGUI {

	public static void main(String args[])
	{
		// Init Main Frame
		JFrame mainFrame = new JFrame();
		mainFrame.setVisible(true);
		mainFrame.pack();
		mainFrame.setSize(1100, 800);
		
		//StartupWay: Service
		DefineServiceImpl defineService = new DefineServiceImpl();
		JInternalFrame jframe = defineService.getDefinedGUI();
		jframe.setVisible(true);
		mainFrame.add(jframe);
		mainFrame.repaint();
		
		//StartupWay: Direct Frame
//		ApplicationJFrame jframe = new ApplicationJFrame();
//		jframe.setVisible(true);
//		DefinitionController definitionController = DefinitionController.getInstance();
//		definitionController.initSettings();
//		jframe.setContentView(definitionController.initUi());
//		mainFrame.add(jframe);
//		mainFrame.repaint();
		
		//StartupWay: Controller 
//		ApplicationController appController = new ApplicationController();
//		appController.initUi();
//		mainFrame.add(appController.getApplicationFrame());
//		mainFrame.repaint();

	}
}