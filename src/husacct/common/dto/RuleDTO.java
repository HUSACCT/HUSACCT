package husacct.common.dto;

public class RuleDTO extends AbstractDTO {
//	public final String ruleTypeKey;
//	public final String[] violationTypeKeys;
//	public final ModuleDTO moduleFrom;
//	public final ModuleDTO moduleTo;
//	public final RuleDTO[] exceptionRules;
	
	public String ruleTypeKey;
	public String[] violationTypeKeys;
	public ModuleDTO moduleFrom;
	public ModuleDTO moduleTo;
	public RuleDTO[] exceptionRules;
	
	public RuleDTO(String ruleTypeKey, ModuleDTO moduleTo,
			ModuleDTO moduleFrom, String[] violationTypeKeys, RuleDTO[] exceptionRules) {
		super();
		this.ruleTypeKey = ruleTypeKey;
		this.violationTypeKeys = violationTypeKeys;
		this.moduleFrom = moduleFrom;
		this.moduleTo = moduleTo;
		this.exceptionRules = exceptionRules;
	}
}
