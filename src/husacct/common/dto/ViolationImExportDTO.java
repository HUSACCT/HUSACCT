package husacct.common.dto;



public class ViolationImExportDTO extends AbstractDTO {
	public String from = "";			// Path of the software unit with the violation
	public String to = "";				// Path of the software unit that is depended-upon
	public int line = 0;				// Line in the source of the fromClass that contains the violating code construct
	public String depType = ""; 		// DependencyType (for dependency-related rule types) or visibilityType, etc. 
	public String depSubType = "";		// DependencySubType for dependency-related rule types)
	public boolean indirect = false;	// Direct/indirect dependency (for dependency-related rule types)
	public String severity = "";		// Key of the severity
	public String message = "";			// Short explanation of the violated rule
	// The following three identify the violated rule: ruleTypeKey + logicalModuleFrom + logicalModuleTo
	public String ruleType = "";		// Identifier of RuleType; the type of violated rule
	public String fromMod = "";			// ModuleFrom of the violated rule; not of the from-to software units
	public String toMod = "";			// ModuleTo of the violated rule; not of the from-to software units


	public ViolationImExportDTO() {
	}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public String getDepType() {
		return depType;
	}

	public void setDepType(String depType) {
		this.depType = depType;
	}

	public String getDepSubType() {
		return depSubType;
	}

	public void setDepSubType(String depSubType) {
		this.depSubType = depSubType;
	}

	public boolean isIndirect() {
		return indirect;
	}

	public void setIndirect(boolean indirect) {
		this.indirect = indirect;
	}

	public String getSeverity() {
		return severity;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRuleType() {
		return ruleType;
	}

	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}

	public String getFromMod() {
		return fromMod;
	}

	public void setFromMod(String fromMod) {
		this.fromMod = fromMod;
	}

	public String getToMod() {
		return toMod;
	}

	public void setToMod(String toMod) {
		this.toMod = toMod;
	}

	public ViolationImExportDTO(String fromClasspath, String toClasspath, String logicalModuleFrom, String logicalModuleTo, String violationType, String ruleType, String dependencySubType, int linenumber, String severityKey, boolean isIndirect) {
		this.from = fromClasspath;
		this.to = toClasspath;
		this.fromMod = logicalModuleFrom;
		this.toMod = logicalModuleTo;
		this.ruleType = ruleType;
		this.line = linenumber;
		this.depType = violationType;
		this.depSubType = dependencySubType;
		this.indirect = isIndirect;
		this.severity = severityKey;
	}
	
    @Override
	public String toString() {
    	
        String representation = "";
        representation += "\n fromClasspath: " + from;
        representation += "\n toClasspath: " + to;
        representation += "\n logicalModuleFrom: " + fromMod;
        representation += "\n logicalModuleTo: " + toMod;
        representation += "\n ruleTypeKey: " + ruleType;
        representation += "linenumber: " + line + ", dependencySubType: " + depSubType;
        representation += ", indirect: " + indirect;
        representation += "\n";
        return representation;
    }
}