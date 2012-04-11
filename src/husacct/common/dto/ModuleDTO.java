package husacct.common.dto;

public class ModuleDTO extends AbstractDTO{
	public String logicalPath;
	public String[] physicalPaths;
	public ModuleDTO[] subModules;
	public String type;
}
