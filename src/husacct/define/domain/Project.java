package husacct.define.domain;

import java.util.ArrayList;

public class Project {
	private String name;
	private ArrayList<String> paths;
	private String programmingLanguage;
	private String version;
	private String description;
	
	public Project()
	{
		this("",new ArrayList<String>(),"", "1.0", "");
	}
	
	public Project(String name, ArrayList<String> paths, String lang)
	{
		this(name,paths,lang, "1.0", "");
	}
	
	public Project(String name, ArrayList<String> paths, String lang, String version, String description)
	{
		this.setName(name);
		this.setPaths(paths);
		this.setProgrammingLanguage(lang);
		this.setVersion(version);
		this.setDescription(description);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<String> getPaths() {
		return paths;
	}
	public void setPaths(ArrayList<String> paths) {
		this.paths = paths;
	}
	public String getProgrammingLanguage() {
		return programmingLanguage;
	}
	public void setProgrammingLanguage(String programmingLanguage) {
		this.programmingLanguage = programmingLanguage;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
