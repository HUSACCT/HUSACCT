package husacct.define.presentation;

import husacct.Main;
import husacct.define.DefineServiceImpl;
import husacct.define.domain.services.SoftwareArchitectureDomainService;
import husacct.define.task.DefinitionController;

import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.WindowConstants;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class MainGUI {

	public static void main(String[] args) {
		new MainGUI(args);
	}
	
	public MainGUI(String[] args)
	{
		URL propertiesFile = getClass().getResource("/husacct/common/resources/husacct.properties");
		PropertyConfigurator.configure(propertiesFile);

		Logger logger = Logger.getLogger(Main.class);
		logger.info("Starting Define");
		
		SoftwareArchitectureDomainService softwareDomainService = new SoftwareArchitectureDomainService();
		softwareDomainService.createApplication("application 1", new String[] {}, "Java", "1.0");
		
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
		mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		
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

		DefinitionController.getInstance().createTestData();
	}
}