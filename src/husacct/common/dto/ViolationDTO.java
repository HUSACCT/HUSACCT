package husacct.common.dto;

import java.awt.Color;

public class ViolationDTO extends AbstractDTO {
	public final String fromClasspath;
	public final String toClasspath;
	public final String logicalModuleFrom;
	public final String logicalModuleTo;
	public final RuleTypeDTO ruleType;
	public final ViolationTypeDTO violationType;
	public final String message;
	public final int linenumber;
	public String dependencySubType;
	public final boolean indirect;
	
	public final Color severityColor;
	public final String severityName;
	public final int severityValue;

	public ViolationDTO(String fromClasspath, String toClasspath, String logicalModuleFrom, String logicalModuleTo, ViolationTypeDTO violationType, RuleTypeDTO ruleType, String message, String dependencySubType, int linenumber, Color severityColor, String severityName, int severityValue, boolean isIndirect) {
		this.indirect = isIndirect;
		this.fromClasspath = fromClasspath;
		this.toClasspath = toClasspath;
		this.logicalModuleFrom = logicalModuleFrom;
		this.logicalModuleTo = logicalModuleTo;
		this.violationType = violationType;
		this.ruleType = ruleType;
		this.message = message;
		this.dependencySubType = dependencySubType;
		this.linenumber = linenumber;
		this.severityColor = severityColor;
		this.severityName = severityName;
		this.severityValue = severityValue;
	}
	
    public String toString() {
    	
        String representation = "";
        representation += "\nfromClasspath: " + fromClasspath;
        representation += "\ntoClasspath: " + toClasspath;
        representation += "\nlogicalModuleFrom: " + logicalModuleFrom;
        representation += "\nlogicalModuleTo: " + logicalModuleTo;
        representation += ruleType.toString();
        representation += "linenumber: " + linenumber + ", dependencySubType: " + dependencySubType;
        representation += ", indirect: " + indirect;
        representation += "\n";
        return representation;
    }
}