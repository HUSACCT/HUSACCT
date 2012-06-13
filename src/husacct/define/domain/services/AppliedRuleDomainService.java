package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;

import java.util.ArrayList;

public class AppliedRuleDomainService {

	public AppliedRule[] getAppliedRules(boolean enabledRulesOnly) {
		ArrayList<AppliedRule> ruleList;
		if (enabledRulesOnly) {
			ruleList = SoftwareArchitecture.getInstance().getAppliedRules();
		} else {
			ruleList = SoftwareArchitecture.getInstance().getEnabledAppliedRules();
		}
		AppliedRule[] rules = new AppliedRule[ruleList.size()]; ruleList.toArray(rules);
		return rules;
	}
	
	public AppliedRule[] getAppliedRules() {
		return getAppliedRules(true);
	}
	
	public long addAppliedRule(String ruleTypeKey, String description, String[] dependencies,
			String regex, long moduleFromId, long moduleToId, boolean enabled) {
		Module moduleFrom = SoftwareArchitecture.getInstance().getModuleById(moduleFromId);
		Module moduleTo;
		if (moduleToId != -1){
			moduleTo = SoftwareArchitecture.getInstance().getModuleById(moduleToId);
		} else {
			moduleTo = new Module();
		}
		
		return addAppliedRule(ruleTypeKey,description,dependencies,regex,moduleFrom , moduleTo, enabled);
	}
	
	public long addAppliedRule(String ruleTypeKey, String description, String[] dependencies,
			String regex, Module moduleFrom, Module moduleTo, boolean enabled) {

		AppliedRule rule = new AppliedRule(ruleTypeKey,description,dependencies,regex,moduleFrom, moduleTo, enabled);
		SoftwareArchitecture.getInstance().addAppliedRule(rule);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		return rule.getId();
	}
	
	public void updateAppliedRule(long appliedRuleId, String ruleTypeKey,String description, String[] dependencies, 
			String regex,long moduleFromId, long moduleToId, boolean enabled) {

		Module moduleFrom = SoftwareArchitecture.getInstance().getModuleById(moduleFromId);
		Module moduleTo = SoftwareArchitecture.getInstance().getModuleById(moduleToId);
		updateAppliedRule(appliedRuleId, ruleTypeKey, description, dependencies, 
				regex, moduleFrom, moduleTo, enabled);
	}
	
	public void updateAppliedRule(long appliedRuleId, String ruleTypeKey,String description, String[] dependencies, 
			String regex, Module moduleFrom, Module moduleTo, boolean enabled) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		rule.setRuleType(ruleTypeKey);
		rule.setDescription(description);
		rule.setDependencies(dependencies);
		rule.setRegex(regex);
		rule.setModuleFrom(moduleFrom);
		rule.setModuleTo(moduleTo);
		rule.setEnabled(enabled);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	public void removeAppliedRules() {
		SoftwareArchitecture.getInstance().removeAppliedRules();
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	public void removeAppliedRule(long appliedrule_id) {
		SoftwareArchitecture.getInstance().removeAppliedRule(appliedrule_id);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}

	public String getRuleTypeByAppliedRule(long appliedruleId) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedruleId);
		String ruleTypeKey = rule.getRuleType();
		return ruleTypeKey;
	}

	public void setAppliedRuleIsEnabled(long appliedRuleId, boolean enabled) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		rule.setEnabled(enabled);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	public ArrayList<Long> getAppliedRulesIdsByModuleFromId(long moduleId) {
		return SoftwareArchitecture.getInstance().getAppliedRulesIdsByModuleFromId(moduleId);
	}
	
	public ArrayList<Long> getAppliedRulesIdsByModuleToId(long moduleId) {
		return SoftwareArchitecture.getInstance().getAppliedRulesIdsByModuleToId(moduleId);
	}

	public long getModuleToIdOfAppliedRule(long appliedRuleId) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		Long moduleToId = rule.getModuleTo().getId();
		return moduleToId;
	}
	
	public boolean getAppliedRuleIsEnabled(long appliedRuleId) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		boolean isEnabled = rule.isEnabled();
		return isEnabled;
	}

	public AppliedRule getAppliedRuleById(long appliedRuleId) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		return rule;
	}
}
