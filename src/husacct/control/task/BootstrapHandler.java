package husacct.control.task;

import husacct.bootstrap.AbstractBootstrap;

import org.apache.log4j.Logger;

public class BootstrapHandler {

	private Logger logger = Logger.getLogger(BootstrapHandler.class);
	private String[] args = {};
	private boolean runArgs = false;
	
	public BootstrapHandler(){

	}
	
	public BootstrapHandler(String[] bootstraps) {
		for(String bootstrap : bootstraps){
			runArgs = false;
			if(bootstrap.contains("?")){
				String allArgs = bootstrap.substring(bootstrap.indexOf('?') +1);
				args = allArgs.contains("|") ? allArgs.split("|") : new String[]{ allArgs };
				bootstrap = bootstrap.substring(0,  bootstrap.indexOf('?'));
				runArgs = true;
			}
			Class<? extends AbstractBootstrap> bootstrapClass = getBootstrapClass(bootstrap);
			if(bootstrapClass != null){
				executeBootstrap(bootstrapClass);
			}
		}
	}
	
	public void executeBootstrap(Class<? extends AbstractBootstrap> bootstrap){
		logger.info("Trying to execute bootstrapper " + bootstrap.getName());
		try {
			AbstractBootstrap targetBootstrap = bootstrap.newInstance();
			if(!runArgs){
				targetBootstrap.execute();
			}else{
				targetBootstrap.execute(args);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			error("Exception: " + exception.getMessage());
		}
	}
	
	private Class<? extends AbstractBootstrap> getBootstrapClass(String bootstrap){
		String classNameToBeLoaded = "husacct.bootstrap." + bootstrap;
		try {
			Class<? extends AbstractBootstrap> myClass = Class.forName(classNameToBeLoaded).asSubclass(AbstractBootstrap.class);
			return myClass;
		} catch (ClassNotFoundException exception) {
			exception.printStackTrace();
			error("ClassNotFoundException " + exception.getMessage());
			System.exit(0);
		} catch (Error error){
			error.printStackTrace();
			error("Error " + error.getMessage());
		}
		return null;
	}
	
	private void error(String message){
		logger.debug("Unable to launch bootstrap: " + message);
		System.exit(0);
	}
}
