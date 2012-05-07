package husacct.common.dto;

public class ModuleDTO extends AbstractDTO{
//	public final String logicalPath;
//	public final String[] physicalPaths;
//	public final ModuleDTO[] subModules;
//	public final String type;
	
	public ModuleDTO(String logicalPath, String[] physicalPaths,
			String type, ModuleDTO[] subModules) {
		super();
		this.logicalPath = logicalPath;
		this.physicalPaths = physicalPaths;
		this.subModules = subModules;
		this.type = type;
	}
	
	//Dit wordt verwijdert
	public String logicalPath;
	public String[] physicalPaths;
	public ModuleDTO[] subModules;
	public String type;
	public ModuleDTO(){}
}
