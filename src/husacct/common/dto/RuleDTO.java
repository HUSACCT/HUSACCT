package husacct.common.dto;

public class RuleDTO extends AbstractDTO {
	public String ruleTypeKey;
	public boolean enabled;
	public String[] violationTypeKeys;
	public ModuleDTO moduleFrom;
	public ModuleDTO moduleTo;
	public String regex;
	public boolean isException;
	public RuleDTO[] exceptionRules;
	
	public RuleDTO(String ruleTypeKey, boolean enabled, ModuleDTO moduleTo, ModuleDTO moduleFrom, 
			String[] violationTypeKeys, String regex, RuleDTO[] exceptionRules, boolean isException) {
		super();
		this.ruleTypeKey = ruleTypeKey;
		this.enabled = enabled;
		this.violationTypeKeys = violationTypeKeys;
		this.moduleFrom = moduleFrom;
		this.moduleTo = moduleTo;
		this.regex = regex;
		this.exceptionRules = exceptionRules;
		this.isException = isException;
	}
	
    public String toString() {
    	
        String representation = "";
        representation += "\nRuleTypeKey: " + ruleTypeKey + ", IsException: " + isException;
        representation += "\nModuleFrom: " + moduleFrom.logicalPath;
        representation += "\nModuleTo: " + moduleTo.logicalPath;
        representation += "\nRegEx: " + regex;
        representation += "\nEnabled: " + enabled;
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
