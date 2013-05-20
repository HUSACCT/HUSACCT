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
			ruleList = SoftwareArchitecture.getInstance().getEnabledAppliedRules();
		} else {
			ruleList = SoftwareArchitecture.getInstance().getAppliedRules();
		}
		AppliedRule[] rules = new AppliedRule[ruleList.size()]; 
		ruleList.toArray(rules);

		return rules;
	}

	public AppliedRule[] getAppliedRules() {
		return getAppliedRules(false);
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
		if(isDuplicate(rule)){
			return -1;
		}		
		SoftwareArchitecture.getInstance().addAppliedRule(rule);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();

		return rule.getId();
	}

	public void updateAppliedRule(long appliedRuleId, Boolean isGenerated, String ruleTypeKey,String description, String[] dependencies, 
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
		return SoftwareArchitecture.getInstance().getAppliedRuleById(appliedruleId).getRuleType();
	}

	public void setAppliedRuleIsEnabled(long appliedRuleId, boolean enabled) {
		SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId).setEnabled(enabled);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}

	public ArrayList<Long> getAppliedRulesIdsByModuleFromId(long moduleId) {
		return SoftwareArchitecture.getInstance().getAppliedRulesIdsByModuleFromId(moduleId);
	}

	public ArrayList<Long> getAppliedRulesIdsByModuleToId(long moduleId) {
		return SoftwareArchitecture.getInstance().getAppliedRulesIdsByModuleToId(moduleId);
	}

	public long getModuleToIdOfAppliedRule(long appliedRuleId) {
		return SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId).getModuleTo().getId();
	}

	public boolean getAppliedRuleIsEnabled(long appliedRuleId) {
		return SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId).isEnabled();
	}

	public AppliedRule getAppliedRuleById(long appliedRuleId) {
		return SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
	}


	/** 
	 * Domain checks
	 */
	public boolean isDuplicate(AppliedRule rule){
		AppliedRule[] appliedRules = getAppliedRules(false);
		for(AppliedRule appliedRule : appliedRules){
			if(rule.equals(appliedRule)){
				return true;
			}
		}
		return false;
	}
}
