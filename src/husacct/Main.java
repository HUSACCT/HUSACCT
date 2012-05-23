package husacct;

import husacct.control.IControlService;

import java.net.URL;

import org.apache.log4j.PropertyConfigurator;

public class Main {
	
	public Main(String[] consoleArguments){
		
		System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Husacct");
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("apple.awt.fileDialogForDirectories", "true");
		
		setLog4jConfiguration();
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		controlService.startApplication(consoleArguments);
	}
	
	public void setLog4jConfiguration(){
		URL propertiesFile = getClass().getResource("/husacct/common/resources/husacct.properties");
		PropertyConfigurator.configure(propertiesFile);
	}
	
	public static void main(String[] consoleArguments) {
		new Main(consoleArguments);
	}
	
}