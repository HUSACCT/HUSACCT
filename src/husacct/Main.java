package husacct;

import husacct.control.task.MainController;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {
	
	private Logger logger = Logger.getLogger(Main.class);
	
	public Main(String[] args){
		setLog4jConfiguration();
		logger.debug("Starting HUSACCT");
		setAppleProperties();
		new MainController(args);
	}
	
	private void setAppleProperties(){
		logger.debug("Setting apple specific properties");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Husacct");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
	}
	
	public void setLog4jConfiguration(){
		URL propertiesFile = getClass().getResource("/husacct/common/resources/husacct.properties");
		PropertyConfigurator.configure(propertiesFile);
	}
	
	public static void main(String[] args) {
		new Main(args);
	}
	
}
