package husacct.common.dto;

public class ViolationDTO extends AbstractDTO {
	private String fromClasspath;
	private String toClasspath;
	private String errorMessage;
	private String logicalModuleFrom;
	private String logicalModuleTo;
	private RuleTypeDTO ruleType;
	private ViolationTypeDTO violationType;

	public ViolationDTO(String fromClasspath, String toClasspath, String errorMessage,
			String logicalModuleFrom, String logicalModuleTo, ViolationTypeDTO violationType, RuleTypeDTO ruleType) {
		this.fromClasspath = fromClasspath;
		this.toClasspath = toClasspath;
		this.errorMessage = errorMessage;
		this.logicalModuleFrom = logicalModuleFrom;
		this.logicalModuleTo = logicalModuleTo;
		this.violationType = violationType;
		this.ruleType = ruleType;
	}

	public String getFromClasspath() {
		return fromClasspath;
	}

	public String getToClasspath() {
		return toClasspath;
	}

	public String getErrorMessage() {
		return errorMessage;
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
}