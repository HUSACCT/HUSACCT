package husacct.common.dto;

public class ModuleDTO extends AbstractDTO {

    public String logicalPath;
    public PhysicalPathDTO[] physicalPathDTOs;
    public ModuleDTO[] subModules;
    public String type;

    public ModuleDTO() {
        this.logicalPath = "";
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
}
