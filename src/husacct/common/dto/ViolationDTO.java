package husacct.common.dto;

import java.awt.Color;

public class ViolationDTO extends AbstractDTO {
	private String fromClasspath;
	private String toClasspath;
	private String logicalModuleFrom;
	private String logicalModuleTo;
	private RuleTypeDTO ruleType;
	private ViolationTypeDTO violationType;
	private MessageDTO message;
	
	private Color severityColor;
	private String userDefinedName;
	private String systemDefinedName;
	private int value;

	public ViolationDTO(String fromClasspath, String toClasspath, String logicalModuleFrom, String logicalModuleTo, ViolationTypeDTO violationType, RuleTypeDTO ruleType, MessageDTO message) {
		this.fromClasspath = fromClasspath;
		this.toClasspath = toClasspath;
		this.logicalModuleFrom = logicalModuleFrom;
		this.logicalModuleTo = logicalModuleTo;
		this.violationType = violationType;
		this.ruleType = ruleType;
		this.message = message;
		
		//Set temporarily with static values
		this.severityColor = Color.red;
		this.userDefinedName = "Medium";
		this.systemDefinedName = "";
		this.value = 3;
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

	public MessageDTO getMessage() {
		return message;
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

	public int getValue() {
		return value;
	}
}