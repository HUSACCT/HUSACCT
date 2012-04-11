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

public final class ServiceProvider {

	private static ServiceProvider _instance;
	
	private IControlService controlService;
	private IAnalyseService analyseService;
	private IGraphicsService graphicsService;
	private IDefineService defineService;
	private IValidateService validateService;
	
	private ServiceProvider(){
		this.controlService = new ControlServiceImpl();
		this.analyseService = new AnalyseServiceImpl();
		this.graphicsService = new GraphicsServiceImpl();
		this.defineService = new DefineServiceImpl();
		this.validateService = new ValidateServiceImpl();
	}

	public static ServiceProvider getInstance(){
		if(ServiceProvider._instance == null){
			ServiceProvider._instance = new ServiceProvider();
		}
		return ServiceProvider._instance;
	}
	
	public IControlService getControlService() {
		return controlService;
	}
	
	public IAnalyseService getAnalyseService() {
		return analyseService;
	}

	public IGraphicsService getGraphicsService() {
		return graphicsService;
	}

	public IDefineService getDefineService() {
		return defineService;
	}

	public IValidateService getValidateService() {
		return validateService;
	}
	
}
