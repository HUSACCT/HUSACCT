package husacct.common.dto;

public class RuleDTO extends AbstractDTO {
	public String ruleTypeKey;
	public boolean isGenerated;
	public String[] violationTypeKeys;
	public ModuleDTO moduleFrom;
	public ModuleDTO moduleTo;
	public String regex;
	public RuleDTO[] exceptionRules;
	
	public RuleDTO(String ruleTypeKey, boolean isGenerated, ModuleDTO moduleTo,
			ModuleDTO moduleFrom, String[] violationTypeKeys, String regex, RuleDTO[] exceptionRules) {
		super();
		this.ruleTypeKey = ruleTypeKey;
		this.isGenerated = isGenerated;
		this.violationTypeKeys = violationTypeKeys;
		this.moduleFrom = moduleFrom;
		this.moduleTo = moduleTo;
		this.regex = regex;
		this.exceptionRules = exceptionRules;
	}
	
    public String toString() {
        String representation = "";
        representation += "\nRuleTypeKey: " + ruleTypeKey;
        representation += "\nModuleFrom: " + moduleFrom.logicalPath;
        representation += "\nModuleTo: " + moduleTo.logicalPath;
        representation += "\nRegEx: " + regex;
        representation += "\nIsGenerated: " + isGenerated;
        representation += "\nExceptionRules: ";
        for (RuleDTO r : exceptionRules){
        	representation += "\nRuleTypeKey: " + (r.ruleTypeKey) + ", from: " + (r.moduleFrom.logicalPath) + ", to: " + r.moduleTo.logicalPath;
        }
        representation += "\nViolationTypeKeys: ";
        for (String v : violationTypeKeys){
        	representation += v + ", ";
        }
        representation += "\n";
        return representation;
    }
}
