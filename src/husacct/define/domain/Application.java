package husacct.define.domain;

import java.util.ArrayList;

public class Application {

    private SoftwareArchitecture architecture;
    private String name;
    private ArrayList<Project> projects;
    private String version;

    public Application() {
	this("", new ArrayList<Project>(), "1.0");
    }

    public Application(String name) {
	this(name, new ArrayList<Project>(), "1.0");
    }

    public Application(String name, ArrayList<Project> projects, String version) {
	setName(name);
	setProjects(projects);
	setVersion(version);
	architecture = new SoftwareArchitecture();
    }

    public SoftwareArchitecture getArchitecture() {
	return architecture;
    }

    public String getName() {
	return name;
    }

    public ArrayList<Project> getProjects() {
	return projects;
    }

    public String getVersion() {
	return version;
    }

    public void setArchitecture(SoftwareArchitecture architecture) {
	this.architecture = architecture;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setProjects(ArrayList<Project> projects) {
	this.projects = projects;
    }

    public void setVersion(String version) {
	this.version = version;
    }
}
