package husacct.define.task;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;

public class RuleConventionsChecker {
	private Module moduleFrom;
	private Module moduleTo;
	private String ruleTypeKey;
	
	private ArrayList<AppliedRule> fromAppliedRules;
	private ArrayList<AppliedRule> toAppliedRules;
	
	private String errorMessage;
	
	public RuleConventionsChecker(Module moduleFrom, Module moduleTo, String ruleTypeKey) {
		this.setModuleFrom(moduleFrom);
		this.setModuleTo(moduleTo);
		this.setRuleTypeKey(ruleTypeKey);
		this.fromAppliedRules = new ArrayList<AppliedRule>();
		this.toAppliedRules = new ArrayList<AppliedRule>();
		this.errorMessage = "";
	}
	
	public boolean checkRuleConventions() {
		fillAppliedRules();
		boolean conventionError = true;
		if(ruleTypeKey.equals("VisibilityConvention")) {
			conventionError = checkVisibilityConvention();
		} else if(ruleTypeKey.equals("VisibilityConventionException")) {
			conventionError = checkVisibilityConventionException();
		} else if(ruleTypeKey.equals("NamingConvention")) {
			conventionError = checkNamingConvention();
		} else if(ruleTypeKey.equals("NamingConventionException")) {
			conventionError = checkNamingConventionException();
		} else if(ruleTypeKey.equals("IsNotAllowedToUse")) {
			conventionError = checkIsNotAllowedToUse();
		} else if(ruleTypeKey.equals("IsOnlyAllowedToUse")) {
			conventionError = checkIsOnlyAllowedToUse();
		} else if(ruleTypeKey.equals("IsOnlyModuleAllowedToUse")) {
			conventionError = checkIsOnlyModuleAllowedToUse();
		}
		return conventionError;
	}
	
	private void fillAppliedRules() {
		AppliedRuleDomainService appliedRuleService = new AppliedRuleDomainService();
		ArrayList<Long> fromAppliedRuleIds = appliedRuleService.getAppliedRulesIdsByModuleFromId(moduleFrom.getId());
		for(Long appliedRuleId : fromAppliedRuleIds) {
			AppliedRule appliedRule = appliedRuleService.getAppliedRuleById(appliedRuleId);
			this.fromAppliedRules.add(appliedRule);
		}
		ArrayList<Long> toAppliedRuleIds = appliedRuleService.getAppliedRulesIdsByModuleToId(moduleTo.getId());
		for(Long appliedRuleId : toAppliedRuleIds) {
			AppliedRule appliedRule = appliedRuleService.getAppliedRuleById(appliedRuleId);
			this.toAppliedRules.add(appliedRule);
		}
	}
	
	private boolean checkVisibilityConvention() {
		boolean visibilityConventionSucces = checkRuleTypeAlreadySet();
		return visibilityConventionSucces;
	}
	
	private boolean checkVisibilityConventionException() {
		boolean visibilityConventionSucces = checkRuleTypeAlreadySet();
		return visibilityConventionSucces;
	}
	
	private boolean checkNamingConvention() {
		boolean namingConventionSucces = checkRuleTypeAlreadySet();
		return namingConventionSucces;
	}
	
	private boolean checkNamingConventionException() {
		boolean namingConventionSucces = checkRuleTypeAlreadySet();
		return namingConventionSucces;
	}
	
	private boolean checkIsNotAllowedToUse() {
		boolean isNotAllowedToUseSucces = checkRuleTypeAlreadyFromThisToSelected("IsOnlyAllowedToUse");
		if(isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = checkRuleTypeAlreadyFromThisToSelected("IsOnlyModuleAllowedToUse");
		}
		if(isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = checkRuleTypeAlreadyFromThisToSelected("IsAllowedToUse");
		}
		if(isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = checkRuleTypeAlreadyFromThisToSelected("MustUse");
		}
		return isNotAllowedToUseSucces;
	}
	
	private boolean checkIsOnlyAllowedToUse() {
		boolean isOnlyAllowedToUseSucces = checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse");
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse");
		}
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = checkRuleTypeAlreadyFromOtherToSelected("IsOnlyModuleAllowedToUse");
		}
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = checkRuleTypeAlreadyFromThisToOther("IsAllowedToUse");
		}
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = checkRuleTypeAlreadyFromThisToOther("MustUse");
		}
		return isOnlyAllowedToUseSucces;
	}
	
	private boolean checkIsOnlyModuleAllowedToUse() {
		boolean isOnlyAllowedToUseSucces = checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse");
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse");
		}
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = checkRuleTypeAlreadyFromOtherToSelected("IsOnlyModuleAllowedToUse");
		}
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = checkRuleTypeAlreadyFromOtherToSelected("IsAllowedToUse");
		}
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = checkRuleTypeAlreadyFromOtherToSelected("MustUse");
		}
		return isOnlyAllowedToUseSucces;
	}
	
	private boolean checkRuleTypeAlreadySet() {
		for(AppliedRule appliedRule : fromAppliedRules) {
			if(appliedRule.getRuleType().equals(ruleTypeKey)) {
				setErrorMessage(DefineTranslator.translate("RuleTypeAlreadySet"));
				return false;
			}
		}
		return true;
	}
	
	private boolean checkRuleTypeAlreadyFromThisToSelected(String ruleType) {
		for(AppliedRule appliedRule : fromAppliedRules) {
			if(appliedRule.getRuleType().equals(ruleType) &&
				appliedRule.getModuleFrom().getId() == moduleFrom.getId() &&
				appliedRule.getModuleTo().getId() == moduleTo.getId()) {
				setErrorMessage(DefineTranslator.translate(ruleType + "AlreadyFromThisToSelected"));
				return false;
			}
		}
		return true;
	}
	
	private boolean checkRuleTypeAlreadyFromThisToOther(String ruleType) {
		for(AppliedRule appliedRule : fromAppliedRules) {
			if(appliedRule.getRuleType().equals(ruleType) &&
				appliedRule.getModuleFrom().getId() == moduleFrom.getId() &&
				appliedRule.getModuleTo().getId() != moduleTo.getId()) {
				setErrorMessage(DefineTranslator.translate(ruleType + "AlreadyFromThisToOther"));
				return false;
			}
		}
		return true;
	}
	
	private boolean checkRuleTypeAlreadyFromOtherToSelected(String ruleType) {
		for(AppliedRule appliedRule : toAppliedRules) {
			if(appliedRule.getRuleType().equals(ruleType) &&
				appliedRule.getModuleFrom().getId() != moduleFrom.getId() &&
				appliedRule.getModuleTo().getId() == moduleTo.getId()) {
				setErrorMessage(DefineTranslator.translate(ruleType + "AlreadyFromOtherToSelected") + " \"" + appliedRule.getModuleFrom().getName() + "\"");
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
	
	public void setErrorMessage(String errorKey) {
		this.errorMessage = errorKey;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}
}
