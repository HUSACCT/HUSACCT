package husacct.common.dto;

import java.util.ArrayList;

public class ProjectDTO extends AbstractDTO {
	public String name;
	public ArrayList<String> paths;
	public String programmingLanguage;
	public String version;
	public String description;
	public ArrayList<SoftwareUnitDTO> analysedModules;
	
	public ProjectDTO(String name, ArrayList<String> paths,
			String programmingLanguage, String version, String description,
			ArrayList<SoftwareUnitDTO> analysedModules) {
		super();
		this.name = name;
		this.paths = paths;
		this.programmingLanguage = programmingLanguage;
		this.version = version;
		this.description = description;
		this.analysedModules = analysedModules;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
}