package husacct.control.task;

import husacct.ServiceProvider;

public class ApplicationController {

	private ServiceProvider serviceProvider;

	public ApplicationController() {
		serviceProvider = ServiceProvider.getInstance();
	}

	public void analyseApplication(String applicationName, String[] paths, String language, String version) {
		serviceProvider.getDefineService().createApplication(applicationName, paths, language, version);
	}
}