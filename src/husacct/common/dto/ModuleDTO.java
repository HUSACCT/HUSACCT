package husacct.common.dto;

public class ModuleDTO extends AbstractDTO{
	public String logicalPath; //The unique name; the full path
	public String name; //The short name; only unique within the parent module
	public String type;
	public ModuleDTO[] subModules;
	
	public ModuleDTO(){
		this.logicalPath = "";
		this.name = "";
		this.subModules = new ModuleDTO[]{};
		this.type = "";
	}
	
	public ModuleDTO(String logicalPath, String name, String type, ModuleDTO[] subModules) {
		super();
		this.logicalPath = logicalPath;
		this.name = name;
		this.subModules = subModules;
		this.type = type;
		
	}
	
    public String toString() {
        String representation = "";
        representation += "\nLogicalPath: " + logicalPath;
        representation += "\nType: " + type;
        representation += "\nSubModules: ";
        for (ModuleDTO m : subModules){
        	representation += (m.logicalPath) + ", ";
        }
        representation += "\n";
        return representation;
    }
}
