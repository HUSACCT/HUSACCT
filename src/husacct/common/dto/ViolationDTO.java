package husacct.common.dto;

public class ViolationDTO extends AbstractDTO {
	private String fromClasspath;
	private String toClasspath;
	private String logicalModuleFrom;
	private String logicalModuleTo;
	private RuleTypeDTO ruleType;
	private ViolationTypeDTO violationType;
	private MessageDTO message;

	public ViolationDTO(String fromClasspath, String toClasspath,
			String logicalModuleFrom, String logicalModuleTo, ViolationTypeDTO violationType, RuleTypeDTO ruleType, MessageDTO message) {
		this.fromClasspath = fromClasspath;
		this.toClasspath = toClasspath;
		this.logicalModuleFrom = logicalModuleFrom;
		this.logicalModuleTo = logicalModuleTo;
		this.violationType = violationType;
		this.ruleType = ruleType;
		this.setMessage(message);
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

	public void setMessage(MessageDTO message) {
		this.message = message;
	}
}