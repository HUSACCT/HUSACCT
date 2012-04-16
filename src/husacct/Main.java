package husacct;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import husacct.control.task.MainController;

public class Main {
	
	public static void main(String[] args) {
		
		BasicConfigurator.configure();
		
		Logger logger = Logger.getLogger(Main.class);
		logger.debug("Starting HUSACCT");
		new MainController();
	}

}
