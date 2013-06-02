package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.appliedrule.AppliedRuleFactory;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.Module;

import java.util.ArrayList;

public class AppliedRuleDomainService {
    
    private AppliedRuleFactory ruleFactory = new AppliedRuleFactory();

    public long addAppliedRule(String ruleTypeKey, String description,
	    String[] dependencies, String regex, long moduleFromId,
	    long moduleToId, boolean enabled) {
	Module moduleFrom = SoftwareArchitecture.getInstance().getModuleById(
		moduleFromId);
	Module moduleTo;
	if (moduleToId != -1) {
	    moduleTo = SoftwareArchitecture.getInstance().getModuleById(
		    moduleToId);
	} else {
	    moduleTo = moduleFrom;
	}

	return addAppliedRule(ruleTypeKey, description, dependencies, regex,
		moduleFrom, moduleTo, enabled);
    }

    public long addAppliedRule(String ruleTypeKey, String description,
	    String[] dependencies, String regex, Module moduleFrom,
	    Module moduleTo, boolean enabled) {

	AppliedRuleStrategy rule = ruleFactory.createDummyRule(ruleTypeKey);
	if(moduleTo.getId() != -1)
		rule.setAppliedRule(description, dependencies, regex, moduleFrom, moduleTo, enabled); 
	else
		rule.setAppliedRule(description, dependencies, regex, moduleFrom, moduleFrom, enabled); 
	SoftwareArchitecture.getInstance().addAppliedRule(rule);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();

	return rule.getId();
    }

    public AppliedRuleStrategy getAppliedRuleById(long appliedRuleId) {
	return SoftwareArchitecture.getInstance().getAppliedRuleById(
		appliedRuleId);
    }

    public boolean getAppliedRuleIsEnabled(long appliedRuleId) {
	return SoftwareArchitecture.getInstance()
		.getAppliedRuleById(appliedRuleId).isEnabled();
    }

    public AppliedRuleStrategy[] getAppliedRules() {
	return getAppliedRules(false);
    }

    public AppliedRuleStrategy[] getAppliedRules(boolean enabledRulesOnly) {
	ArrayList<AppliedRuleStrategy> ruleList;
	if (enabledRulesOnly) {
	    ruleList = SoftwareArchitecture.getInstance()
		    .getEnabledAppliedRules();
	} else {
	    ruleList = SoftwareArchitecture.getInstance().getAppliedRules();
	}
	AppliedRuleStrategy[] rules = new AppliedRuleStrategy[ruleList.size()];
	ruleList.toArray(rules);

	return rules;
    }

    public ArrayList<Long> getAppliedRulesIdsByModuleFromId(long moduleId) {
	return SoftwareArchitecture.getInstance()
		.getAppliedRulesIdsByModuleFromId(moduleId);
    }

    public ArrayList<Long> getAppliedRulesIdsByModuleToId(long moduleId) {
	return SoftwareArchitecture.getInstance()
		.getAppliedRulesIdsByModuleToId(moduleId);
    }

    public long getModuleToIdOfAppliedRule(long appliedRuleId) {
	return SoftwareArchitecture.getInstance()
		.getAppliedRuleById(appliedRuleId).getModuleTo().getId();
    }

    public String getRuleTypeByAppliedRule(long appliedruleId) {
	return SoftwareArchitecture.getInstance()
		.getAppliedRuleById(appliedruleId).getRuleType();
    }

    /**
     * Domain checks
     */
    public boolean isDuplicate(AppliedRuleStrategy rule) {
	AppliedRuleStrategy[] appliedRules = getAppliedRules(false);
	for (AppliedRuleStrategy appliedRule : appliedRules) {
	    if (rule.equals(appliedRule)) {
		return true;
	    }
	}
	return false;
    }

    public void removeAppliedRule(long appliedrule_id) {
	SoftwareArchitecture.getInstance().removeAppliedRule(appliedrule_id);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void removeAppliedRules() {
	SoftwareArchitecture.getInstance().removeAppliedRules();
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void setAppliedRuleIsEnabled(long appliedRuleId, boolean enabled) {
	SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId)
		.setEnabled(enabled);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void updateAppliedRule(long appliedRuleId, Boolean isGenerated,
	    String ruleTypeKey, String description, String[] dependencies,
	    String regex, long moduleFromId, long moduleToId, boolean enabled) {

	Module moduleFrom = SoftwareArchitecture.getInstance().getModuleById(
		moduleFromId);
	Module moduleTo = SoftwareArchitecture.getInstance().getModuleById(
		moduleToId);
	updateAppliedRule(appliedRuleId, ruleTypeKey, description,
		dependencies, regex, moduleFrom, moduleTo, enabled);
    }

    public void updateAppliedRule(long appliedRuleId, String ruleTypeKey,
	    String description, String[] dependencies, String regex,
	    Module moduleFrom, Module moduleTo, boolean enabled) {
	AppliedRuleStrategy rule = SoftwareArchitecture.getInstance()
		.getAppliedRuleById(appliedRuleId);
	rule.setRuleType(ruleTypeKey);
	rule.setDescription(description);
	rule.setDependencies(dependencies);
	rule.setRegex(regex);
	rule.setModuleFrom(moduleFrom);
	rule.setModuleTo(moduleTo);
	rule.setEnabled(enabled);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public String[][] getCategories() {
	return ruleFactory.getCategories();
    }

    public boolean isMandatory(String ruleTypeKey, Module moduleFrom) {
	DefaultRuleDomainService defaultRuleService = new DefaultRuleDomainService();
	return defaultRuleService.isMandatoryRule(ruleTypeKey, moduleFrom);
    }
}
