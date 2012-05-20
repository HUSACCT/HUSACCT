package husacct;

import husacct.analyse.AnalyseServiceImpl;
import husacct.analyse.IAnalyseService;
import husacct.control.ControlServiceImpl;
import husacct.control.IControlService;
import husacct.define.DefineServiceImpl;
import husacct.define.IDefineService;
import husacct.graphics.GraphicsServiceImpl;
import husacct.graphics.IGraphicsService;
import husacct.validate.IValidateService;
import husacct.validate.ValidateServiceImpl;

import org.apache.log4j.Logger;

public final class ServiceProvider {

	private static Logger logger = Logger.getLogger(ServiceProvider.class);
	
	private static ServiceProvider _instance;

	private IControlService controlService;
	private IAnalyseService analyseService;
	private IDefineService defineService;
	private IValidateService validateService;
	private IGraphicsService graphicsService;

	private ServiceProvider() {
		try {
			_instance = this;
			resetServices();
		} catch (StackOverflowError error) {
			logger.debug("Unable to initiate services, avoid using the ServiceProvider within the ServiceImpl constructor or field declaration. Terminating.");
			System.exit(0);
		}
	}
	
	public static ServiceProvider getInstance() {
		if (ServiceProvider._instance == null) {
			logger.debug("Creating new serviceprovider");
			new ServiceProvider();
		}
		return ServiceProvider._instance;
	}

	public void resetServices(){
		logger.debug("Resetting services");
		if(this.controlService == null) this.controlService = new ControlServiceImpl();
		this.analyseService = new AnalyseServiceImpl();
		this.defineService = new DefineServiceImpl();
		this.validateService = new ValidateServiceImpl();
		this.graphicsService = new GraphicsServiceImpl();
	}
	
	public IControlService getControlService() {
		return controlService;
	}

	public IAnalyseService getAnalyseService() {
		return analyseService;
	}

	public IDefineService getDefineService() {
		return defineService;
	}

	public IValidateService getValidateService() {
		return validateService;
	}
	
	public IGraphicsService getGraphicsService() {
		return graphicsService;
	}

}
