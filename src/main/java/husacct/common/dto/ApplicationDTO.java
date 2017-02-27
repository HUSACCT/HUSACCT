package husacct.common.dto;

import java.util.ArrayList;

public class ApplicationDTO extends AbstractDTO{
	public String name;
	public ArrayList<ProjectDTO> projects;
	public String version;
	
	public ApplicationDTO(String name, ArrayList<ProjectDTO> projects, String version) {
		super();
		this.name = name;
		this.projects = projects;
		this.version = version;
	}
}
