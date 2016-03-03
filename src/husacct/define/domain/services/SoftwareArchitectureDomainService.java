package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.common.dto.ProjectDTO;
import husacct.define.domain.Application;
import husacct.define.domain.Project;
import java.util.ArrayList;

public class SoftwareArchitectureDomainService {
    private Application app;

    public void createApplication(String name, ArrayList<ProjectDTO> projects, String version) {
		ArrayList<Project> moduleProjects = new ArrayList<Project>();
		for (ProjectDTO project : projects) {
		    moduleProjects.add(new Project(project.name, project.paths, project.programmingLanguage, project.version, project.description));
		}
		app = new Application(name, moduleProjects, version);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
    }

    public Application getApplicationDetails() {
		if (app == null) {
		    app = new Application();
		}
		return app;
    }

}
