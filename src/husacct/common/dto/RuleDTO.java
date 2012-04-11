package husacct.common.dto;

public class RuleDTO extends AbstractDTO {
	public String ruleTypeKey;
	public String[] violationTypeKeys;
	public ModuleDTO moduleFrom;
	public ModuleDTO moduleTo;
	public RuleDTO[] exceptionRules;
}
