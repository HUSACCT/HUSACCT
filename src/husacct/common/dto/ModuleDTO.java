package husacct.common.dto;

public class ModuleDTO extends AbstractDTO{
	public String logicalPath;
	public String type;
	public ModuleDTO[] subModules;
	
	public ModuleDTO(){
		this.logicalPath = "";
		this.subModules = new ModuleDTO[]{};
		this.type = "";
	}
	
	public ModuleDTO(String logicalPath, 
			String type, ModuleDTO[] subModules) {
		super();
		this.logicalPath = logicalPath;
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
