package husacct.bootstrap;

import husacct.ServiceProvider;
import husacct.analyse.AnalyseServiceImpl;
import husacct.control.ControlServiceImpl;
import husacct.control.task.BootstrapHandler;
import husacct.define.DefineServiceImpl;
import husacct.graphics.GraphicsServiceImpl;
import husacct.validate.ValidateServiceImpl;

public abstract class AbstractBootstrap {
	
	private BootstrapHandler bootstrapHandler = new BootstrapHandler(); 
	
	public abstract void execute ();
	
	public void executeBootstrap(Class<? extends AbstractBootstrap> bootstrap){
		bootstrapHandler.executeBootstrap(bootstrap);
	}
	
	public ControlServiceImpl getControlService(){
		return (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
	}
	
	public DefineServiceImpl getDefineService(){
		return (DefineServiceImpl) ServiceProvider.getInstance().getDefineService();
	}
	
	public AnalyseServiceImpl getAnalyseService(){
		return (AnalyseServiceImpl) ServiceProvider.getInstance().getAnalyseService();
	}
	
	public ValidateServiceImpl getValidateService(){
		return (ValidateServiceImpl) ServiceProvider.getInstance().getValidateService();
	}
	
	public GraphicsServiceImpl getGraphicsService(){
		return (GraphicsServiceImpl) ServiceProvider.getInstance().getGraphicsService();
	}
}