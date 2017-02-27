package husacct;

import husacct.control.IControlService;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.net.URL;
import java.util.Date;

public class Main {
	
	public Main(String[] commandLineArguments){
		setLog4jConfiguration();
		printSystemInfo();
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		controlService.parseCommandLineArguments(commandLineArguments);
		controlService.startApplication();
	}
	
	private void setLog4jConfiguration(){
		URL propertiesFile = getClass().getResource("log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
	}//Test Comment for crucible
	
	private void printSystemInfo(){
		Logger logger = Logger.getLogger(Main.class);
		Runtime runtime = Runtime.getRuntime();
		//RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
		logger.info("STARTING HUSACCT ");
		logger.info("Processors: " + runtime.availableProcessors() + " Free memory: " + runtime.freeMemory() + " Max memory: " + runtime.maxMemory() + " Total memory: " + runtime.totalMemory() );
	}
	
	public static void main(String[] commandlineArguments) {
		new Main(commandlineArguments);
	}
	
}