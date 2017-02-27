package husacct.common.dto;

public class ViolationDTO extends AbstractDTO {
	public final String fromClasspath;		// Path of the software unit with the violation
	public final String toClasspath;		// Path of the software unit that is depended-upon
	public final int linenumber;			// Line in the source of the fromClass that contains the violating code construct
	public final String violationTypeKey; 	// Contains dependencyType (for dependency-related rule types) or visibilityType, etc. 
	public final String dependencySubType;
	public final boolean indirect;
	public final String severityKey;
	// The following three identify the violated rule: ruleTypeKey + logicalModuleFrom + logicalModuleTo
	public final String ruleTypeKey;		// Identifier of RuleType; the type of rule
	public final String logicalModuleFrom;	// Of the violated rule; not of the from-to software units
	public final String logicalModuleTo;	// Of the violated rule; not of the from-to software units


	public ViolationDTO(String fromClasspath, String toClasspath, String logicalModuleFrom, String logicalModuleTo, String violationType, String ruleType, String dependencySubType, int linenumber, String severityKey, boolean isIndirect) {
		this.fromClasspath = fromClasspath;
		this.toClasspath = toClasspath;
		this.logicalModuleFrom = logicalModuleFrom;
		this.logicalModuleTo = logicalModuleTo;
		this.ruleTypeKey = ruleType;
		this.linenumber = linenumber;
		this.violationTypeKey = violationType;
		this.dependencySubType = dependencySubType;
		this.indirect = isIndirect;
		this.severityKey = severityKey;
	}
	
    public String toString() {
    	
        String representation = "";
        representation += "\n fromClasspath: " + fromClasspath;
        representation += "\n toClasspath: " + toClasspath;
        representation += "\n logicalModuleFrom: " + logicalModuleFrom;
        representation += "\n logicalModuleTo: " + logicalModuleTo;
        representation += "\n ruleTypeKey: " + ruleTypeKey;
        representation += "linenumber: " + linenumber + ", dependencySubType: " + dependencySubType;
        representation += ", indirect: " + indirect;
        representation += "\n";
        return representation;
    }
}