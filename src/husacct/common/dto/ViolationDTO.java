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
	
	public final Color severityColor;
	public final String userDefinedName;
	public final String systemDefinedName;
	public final int severityValue;

	public ViolationDTO(String fromClasspath, String toClasspath, String logicalModuleFrom, String logicalModuleTo, ViolationTypeDTO violationType, RuleTypeDTO ruleType, String message , int linenumber, Color severityColor, String userDefinedName, String systemDefinedName, int severityValue) {
		this.fromClasspath = fromClasspath;
		this.toClasspath = toClasspath;
		this.logicalModuleFrom = logicalModuleFrom;
		this.logicalModuleTo = logicalModuleTo;
		this.violationType = violationType;
		this.ruleType = ruleType;
		this.message = message;
		this.linenumber = linenumber;
		
		//Set temporarily with static values
		this.severityColor = severityColor;
		this.userDefinedName = "Medium";
		this.systemDefinedName = "";
		this.severityValue = 3;
	}

	public String getFromClasspath() {
		return fromClasspath;
	}

	public String getToClasspath() {
		return toClasspath;
	}

	public String getLogicalModuleFrom() {
		return logicalModuleFrom;
	}

	public String getLogicalModuleTo() {
		return logicalModuleTo;
	}

	public RuleTypeDTO getRuleType() {
		return ruleType;
	}

	public ViolationTypeDTO getViolationType() {
		return violationType;
	}

	public String getMessage() {
		return message;
	}

	public int getLinenumber() {
		return linenumber;
	}

	public Color getSeverityColor() {
		return severityColor;
	}

	public String getUserDefinedName() {
		return userDefinedName;
	}

	public String getSystemDefinedName() {
		return systemDefinedName;
	}

	public int getSeverityValue() {
		return severityValue;
	}
}