package husacct.common.dto;

public class ModuleDTO extends AbstractDTO{
	public String logicalPath;
	/**
	 * @deprecated The physicalPaths are being changed. Currently the physicalPaths are being saved in an array of strings. 
	 * This is being replaced by its own DTO, PhysicalPathDTO. Instead of the variable physicalPaths, please use physicalPathDTOs
	 */
	@Deprecated
	public String[] physicalPaths;
	public PhysicalPathDTO[] physicalPathDTOs;
	public ModuleDTO[] subModules;
	public String type;
	
	public ModuleDTO(){
		this.logicalPath = "";
		this.physicalPaths = new String[]{};
		this.physicalPathDTOs = new PhysicalPathDTO[]{};
		this.subModules = new ModuleDTO[]{};
		this.type = "";
	}
	
	public ModuleDTO(String logicalPath, PhysicalPathDTO[] physicalPaths,
			String type, ModuleDTO[] subModules) {
		super();
		this.logicalPath = logicalPath;
		this.physicalPathDTOs = physicalPaths;
		this.subModules = subModules;
		this.type = type;
	}
	
	/**
	 * @deprecated The physicalPaths are being changed. Currently the physicalPaths are being saved in an array of strings. 
	 * This is being replaced by its own DTO, PhysicalPathDTO. Instead of this constructor, please use contructor with the physicalPathDTOs
	 */
	@Deprecated
	public ModuleDTO(String logicalPath, String[] physicalPaths,
			String type, ModuleDTO[] subModules) {
		super();
		this.logicalPath = logicalPath;
		this.physicalPaths = physicalPaths;
		this.subModules = subModules;
		this.type = type;
	}
	
	@Deprecated
	public ModuleDTO(String logicalPath, String[] physicalPaths, PhysicalPathDTO[] physicalPathDTOs,
			String type, ModuleDTO[] subModules) {
		super();
		this.logicalPath = logicalPath;
		this.physicalPaths = physicalPaths;
		this.physicalPathDTOs = physicalPathDTOs;
		this.subModules = subModules;
		this.type = type;
	}
}
