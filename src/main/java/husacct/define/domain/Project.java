package husacct.define.domain;

import java.util.ArrayList;

public class Project {
	private String description;
	private String name;
	private ArrayList<String> paths;
	private String programmingLanguage;
	private String version;

	public Project() {
		this("", new ArrayList<>(), "", "1.0", "");
	}

	public Project(String name, ArrayList<String> paths, String lang) {
		this(name, paths, lang, "1.0", "");
	}

	public Project(String name, ArrayList<String> paths, String lang,
			String version, String description) {
		setName(name);
		setPaths(paths);
		setProgrammingLanguage(lang);
		setVersion(version);
		setDescription(description);
	}

	public String getDescription() {
		return description;
	}

	public String getName() {
		return name;
	}

	public ArrayList<String> getPaths() {
		return paths;
	}

	public String getProgrammingLanguage() {
		return programmingLanguage;
	}

	public String getVersion() {
		return version;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setPaths(ArrayList<String> paths) {
		this.paths = paths;
	}

	public void setProgrammingLanguage(String programmingLanguage) {
		this.programmingLanguage = programmingLanguage;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
