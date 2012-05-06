package husacct.define.domain.services;

import husacct.define.domain.Application;
import husacct.define.domain.SoftwareArchitecture;

public class SoftwareArchitectureDomainService {
	
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
			Application app = new Application(name, paths, language, version);
			Application.setInstance(app);	
	}
	
	public Application getApplicationDetails(){
		return Application.getInstance();
	}

}
