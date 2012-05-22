package husacct.define.task;

import java.util.ArrayList;

import husacct.define.domain.AppliedRule;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;

public class RuleConventionsChecker {
	private Module moduleFrom;
	private Module moduleTo;
	private String ruleTypeKey;
	
	private ArrayList<AppliedRule> appliedRules;
	
	private String errorKey;
	
	public RuleConventionsChecker(Module moduleFrom, Module moduleTo, String ruleTypeKey) {
		this.setModuleFrom(moduleFrom);
		this.setModuleTo(moduleTo);
		this.setRuleTypeKey(ruleTypeKey);
		this.appliedRules = new ArrayList<AppliedRule>();
		this.errorKey = "";
	}
	
	public boolean checkRuleConventions() {
		fillAppliedRules();
		boolean conventionError = false;
		if(ruleTypeKey.equals("VisibilityConvention")) {
			conventionError = checkVisibilityConvention();
		} else if(ruleTypeKey.equals("VisibilityConventionException")) {
			conventionError = checkVisibilityConventionException();
		} else if(ruleTypeKey.equals("NamingConvention")) {
			conventionError = checkNamingConvention();
		} else if(ruleTypeKey.equals("NamingConventionException")) {
			conventionError = checkNamingConventionException();
		}
		return conventionError;
	}
	
	private void fillAppliedRules() {
		AppliedRuleDomainService appliedRuleService = new AppliedRuleDomainService();
		ArrayList<Long> appliedRuleIds = appliedRuleService.getAppliedRulesIdsByModule(moduleFrom.getId());
		for(Long appliedRuleId : appliedRuleIds) {
			AppliedRule appliedRule = appliedRuleService.getAppliedRuleById(appliedRuleId);
			this.appliedRules.add(appliedRule);
		}
	}
	
	private boolean checkVisibilityConvention() {
		boolean visibilityConventionError = checkRuleTypeAlreadySet();
		return visibilityConventionError;
	}
	
	private boolean checkVisibilityConventionException() {
		boolean visibilityConventionError = checkRuleTypeAlreadySet();
		return visibilityConventionError;
	}
	
	private boolean checkNamingConvention() {
		boolean namingConventionError = checkRuleTypeAlreadySet();
		return namingConventionError;
	}
	
	private boolean checkNamingConventionException() {
		boolean namingConventionError = checkRuleTypeAlreadySet();
		return namingConventionError;
	}
	
	private boolean checkRuleTypeAlreadySet() {
		for(AppliedRule appliedRule : appliedRules) {
			if(appliedRule.getRuleType().equals(ruleTypeKey)) {
				setErrorKey("RuleTypeAlreadySet");
				return false;
			}
		}
		return true;
	}
	
	public Module getModuleTo() {
		return moduleTo;
	}

	public void setModuleTo(Module moduleTo) {
		this.moduleTo = moduleTo;
	}

	public Module getModuleFrom() {
		return moduleFrom;
	}

	public void setModuleFrom(Module moduleFrom) {
		this.moduleFrom = moduleFrom;
	}

	public String getRuleTypeKey() {
		return ruleTypeKey;
	}

	public void setRuleTypeKey(String ruleTypeKey) {
		this.ruleTypeKey = ruleTypeKey;
	}
	
	public void setErrorKey(String errorKey) {
		this.errorKey = errorKey;
	}
	
	public String getErrorKey() {
		return this.errorKey;
	}
}
