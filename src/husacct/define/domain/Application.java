package husacct.define.domain;

public class Application {
	
	private String name;
	private String[] paths;
	private String programmingLanguage;
	private String version;
	private SoftwareArchitecture architecture;
	
	public Application()
	{
		this("",new String[]{},"", "1.0");
	}
	
	public Application(String name, String lang)
	{
		this(name,new String[]{},lang, "1.0");
	}
	
	public Application(String name, String[] paths, String lang, String version)
	{
		this.setName(name);
		this.setPaths(paths);
		this.setLanguage(lang);
		this.setVersion(version);
		this.architecture = new SoftwareArchitecture();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setPaths(String[] paths) {
		this.paths = paths;
	}

	public String[] getPaths() {
		return paths;
	}

	public void setLanguage(String language) {
		this.programmingLanguage = language;
	}

	public String getLanguage() {
		return programmingLanguage;
	}

	public SoftwareArchitecture getArchitecture() {
		return architecture;
	}

	public void setArchitecture(SoftwareArchitecture architecture) {
		this.architecture = architecture;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	
	
	

}
