package husacct.common.dto;

public class ModuleDTO extends AbstractDTO{
	public String logicalPath;
	public String[] physicalPaths;
	public ModuleDTO[] subModules;
	public String type;
	
	public ModuleDTO(){
		this.logicalPath = "";
		this.physicalPaths = new String[]{};
		this.subModules = new ModuleDTO[]{};
		this.type = "";
	}
	
	public ModuleDTO(String logicalPath, String[] physicalPaths,
			String type, ModuleDTO[] subModules) {
		super();
		this.logicalPath = logicalPath;
		this.physicalPaths = physicalPaths;
		this.subModules = subModules;
		this.type = type;
	}
}
