package husacct.validate.domain.ruletype;

public class Mapping {
	private final String logicalPath;
	private final String physicalPath;

	public Mapping(String logicalPath, String physicalPath){
		this.logicalPath = logicalPath;
		this.physicalPath = physicalPath;
	}

	public String getLogicalPath() {
		return logicalPath;
	}

	public String getPhysicalPath() {
		return physicalPath;
	}
}