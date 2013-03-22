package husacct.control.task;

import husacct.bootstrap.AbstractBootstrap;

import org.apache.log4j.Logger;

public class BootstrapHandler {

    private Logger logger = Logger.getLogger(BootstrapHandler.class);

    public BootstrapHandler() {
    }

    public BootstrapHandler(String[] bootstraps) {
        for (String bootstrap : bootstraps) {
            Class<? extends AbstractBootstrap> bootstrapClass = getBootstrapClass(bootstrap);
            if (bootstrapClass != null) {
                executeBootstrap(bootstrapClass);
            }
        }
    }

    public void executeBootstrap(Class<? extends AbstractBootstrap> bootstrap) {
        logger.info("Trying to execute bootstrapper " + bootstrap.getName());
        try {
            AbstractBootstrap targetBootstrap = bootstrap.newInstance();
            targetBootstrap.execute();
        } catch (Exception exception) {
            exception.printStackTrace();
            error("Exception: " + exception.getMessage());
        }
    }

    private Class<? extends AbstractBootstrap> getBootstrapClass(String bootstrap) {
        String classNameToBeLoaded = "husacct.bootstrap." + bootstrap;
        try {
            Class<? extends AbstractBootstrap> myClass = Class.forName(classNameToBeLoaded).asSubclass(AbstractBootstrap.class);
            return myClass;
        } catch (ClassNotFoundException exception) {
            exception.printStackTrace();
            error("ClassNotFoundException " + exception.getMessage());
            System.exit(0);
        } catch (Error error) {
            error.printStackTrace();
            error("Error " + error.getMessage());

        }
        return null;
    }

    private void error(String message) {
        logger.debug("Unable to launch bootstrap: " + message);
        System.exit(0);
    }
}
