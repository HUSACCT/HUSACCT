package husacct.common.dto;

public class RuleDTO extends AbstractDTO {
	public String ruleTypeKey;
	public String[] violationTypeKeys;
	public ModuleDTO moduleFrom;
	public ModuleDTO moduleTo;
	public String regex;
	public RuleDTO[] exceptionRules;
	
	public RuleDTO(String ruleTypeKey, ModuleDTO moduleTo,
			ModuleDTO moduleFrom, String[] violationTypeKeys, String regex, RuleDTO[] exceptionRules) {
		super();
		this.ruleTypeKey = ruleTypeKey;
		this.violationTypeKeys = violationTypeKeys;
		this.moduleFrom = moduleFrom;
		this.moduleTo = moduleTo;
		this.regex = regex;
		this.exceptionRules = exceptionRules;
	}
}
