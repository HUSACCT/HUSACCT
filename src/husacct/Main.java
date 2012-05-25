package husacct;

import husacct.control.IControlService;

import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

public class Main {
	
	public Main(String[] consoleArguments){
		
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Husacct");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
		
		setLog4jConfiguration();
		printSystemInfo();
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		controlService.startApplication(consoleArguments);
		
	}
	
	private void setLog4jConfiguration(){
		URL propertiesFile = getClass().getResource("/husacct/common/resources/husacct.properties");
		PropertyConfigurator.configure(propertiesFile);
	}
	
	private void printSystemInfo(){
		Logger logger = Logger.getLogger(Main.class);
		Runtime runtime = Runtime.getRuntime();
		//RuntimeMXBean mxBean = ManagementFactory.getRuntimeMXBean();
		
		logger.debug("Available processors: " + runtime.availableProcessors());
		logger.debug("Free memory: " + runtime.freeMemory());
		logger.debug("Max memory: " + runtime.maxMemory());
		logger.debug("Total memory: " + runtime.totalMemory());
		//logger.debug("Process: " + mxBean.getName());
		
	}
	
	public static void main(String[] consoleArguments) {
		new Main(consoleArguments);
	}
	
}