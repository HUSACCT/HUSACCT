package husacct.define.domain.services;

import husacct.define.domain.Application;
import husacct.define.domain.SoftwareArchitecture;

public class SoftwareArchitectureDomainService {
	private Application app;
	
	/**
	 * Software Architecture
	 */
	public void createNewArchitectureDefinition(String name) {
		SoftwareArchitecture.getInstance().setName(name);
	}
	
	/**
	 * Application
	 */
	public void createApplication(String name, String[] paths, String language, String version) {
		    app = new Application(name, paths, language, version);
//			Application.setInstance(app);	
	}
	
	public Application getApplicationDetails(){
		if (app == null) { app = new Application();}
		return app;
	}

}
