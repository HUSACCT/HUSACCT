package husacct.define.domain;

import java.util.ArrayList;

public class Application {

	private String name;
	private String version;
	private ArrayList<Project> projects;
	private SoftwareArchitecture architecture;

	public Application(){
		this("",new ArrayList<Project>(), "1.0");
	}

	public Application(String name){
		this(name,new ArrayList<Project>(), "1.0");
	}

	public Application(String name, ArrayList<Project> projects, String version){
		this.setName(name);
		this.setProjects(projects);
		this.setVersion(version);
		this.architecture = new SoftwareArchitecture();
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setProjects(ArrayList<Project> projects){
		this.projects = projects;
	}

	public ArrayList<Project> getProjects(){
		return projects;
	}

	public SoftwareArchitecture getArchitecture(){
		return architecture;
	}

	public void setArchitecture(SoftwareArchitecture architecture){
		this.architecture = architecture;
	}

	public String getVersion(){
		return version;
	}

	public void setVersion(String version){
		this.version = version;
	}	
}
