package husacct.validate.domain.validation.module;

import java.util.List;
import husacct.validate.domain.validation.ruletype.RuleType;

abstract class AbstractModule {
	abstract List<RuleType> initAllowedModuleRuleTypes();
	abstract List<RuleType> initDefaultModuleRuleTypes();
	
	protected List<RuleType> ruleTypes;
	protected List<RuleType> allowedRuleTypes;
	protected List<RuleType> defaultRuleTypes;
	
	public AbstractModule(List<RuleType> ruleTypes) {
		this.ruleTypes = ruleTypes;
		this.allowedRuleTypes = initAllowedModuleRuleTypes();
		this.defaultRuleTypes = initDefaultModuleRuleTypes();
	}

	public List<RuleType> getAllowedRuleTypes() {
		return this.allowedRuleTypes;
	}
	
	public List<RuleType> getDefaultRuleTypes() {
		return this.defaultRuleTypes;
	}
	
	public RuleType getRuleType(String ruleTypeKey) {
		RuleType ruleType = null;
		for (RuleType ruleTypeObj : ruleTypes) {
			if (ruleTypeObj.getKey().equals(ruleTypeKey)) {
				ruleType = ruleTypeObj;
			}
		}
		return ruleType;
	}	

	public void setAllowedRuleType(String ruleTypeKey, boolean value) {
		RuleType ruleType = getRuleType(ruleTypeKey);
		
		if (!allowedRuleTypes.contains(ruleType) && value) {
			allowedRuleTypes.add(ruleType);
		} else if (allowedRuleTypes.contains(ruleType) && !value) {
			allowedRuleTypes.remove(ruleType);
		}
	}

	public void setDefaultRuleType(String ruleTypeKey, boolean value) {
		RuleType ruleType = getRuleType(ruleTypeKey);
		if (!defaultRuleTypes.contains(ruleType)) {
			defaultRuleTypes.add(ruleType);
		} 
		else {
			if (value == false) {
			defaultRuleTypes.remove(ruleType);
			}
		}
	}
}