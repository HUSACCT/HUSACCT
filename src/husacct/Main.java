package husacct;

import husacct.control.task.MainController;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {
	
	public static void main(String[] args) {
		
		PropertyConfigurator.configure("husacct.properties");
		Logger logger = Logger.getLogger(Main.class);
		logger.debug("Starting HUSACCT");
		
		new MainController(args);
	}

}
