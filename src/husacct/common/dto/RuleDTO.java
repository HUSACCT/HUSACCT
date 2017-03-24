package husacct.common.dto;

public class RuleDTO extends AbstractDTO {
	public String ruleTypeKey;			// Identifier of RuleType. Identifier of the rule = ruleTypeKey + logicalModuleFrom + logicalModuleTo
	public ModuleDTO moduleFrom;		// Logical Module-From
	public ModuleDTO moduleTo;			// Logical Module-To
	public boolean enabled;
	public String[] violationTypeKeys;	// DependencyType(s) (in case of dependency-related rules) or visibility-setting(s)
	public String regex = "";			// Regular expression used in naming convention
	public boolean isException;
	public RuleDTO mainRule;			// May be null; filled in case of an exception rule
	public RuleDTO[] exceptionRules;
	
	public RuleDTO(String ruleTypeKey, boolean enabled, ModuleDTO moduleTo, ModuleDTO moduleFrom, 
			String[] violationTypeKeys, String regex, boolean isException, RuleDTO mainRule, RuleDTO[] exceptionRules) {
		super();
		this.ruleTypeKey = ruleTypeKey;
		this.enabled = enabled;
		this.violationTypeKeys = violationTypeKeys;
		this.moduleFrom = moduleFrom;
		this.moduleTo = moduleTo;
		this.regex = regex;
		this.isException = isException;
		this.mainRule = mainRule;
		this.exceptionRules = exceptionRules;
	}
	
    @Override
	public String toString() {
    	String mainRuleKey = "";
    	if ((mainRule != null) && (mainRule.ruleTypeKey != null)) {
    		mainRuleKey = mainRule.ruleTypeKey;
    	}
    	
        String representation = "";
        representation += "\nRuleTypeKey: " + ruleTypeKey + ", IsException: " + isException + " To: " + mainRuleKey;
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
