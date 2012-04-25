package husacct.validate.domain.validation.iternal_tranfer_objects;

public class Mapping {
	private final String logicalPath;
	private final String physicalPath;
	private final String logicalPathType;

	public Mapping(String logicalPath, String logicalPathType, String physicalPath){
		this.logicalPath = logicalPath;
		this.physicalPath = physicalPath;
		this.logicalPathType = logicalPathType;
	}

	public String getLogicalPath() {
		return logicalPath;
	}

	public String getPhysicalPath() {
		return physicalPath;
	}

	public String getLogicalPathType() {
		return logicalPathType;
	}
}