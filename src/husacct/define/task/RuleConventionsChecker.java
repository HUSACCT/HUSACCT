package husacct.define.task;

import java.util.ArrayList;

import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;

public class RuleConventionsChecker {
	private Module moduleFrom;
	private Module moduleTo;
	private String ruleTypeKey;
	
	private String errorMessage;
	
	private AppliedRuleDomainService appliedRuleService;
	
	public RuleConventionsChecker(Module moduleFrom, Module moduleTo, String ruleTypeKey) {
		this.setModuleFrom(moduleFrom);
		this.setModuleTo(moduleTo);
		this.setRuleTypeKey(ruleTypeKey);
		this.errorMessage = "";
		this.appliedRuleService = new AppliedRuleDomainService();
	}
	
	public boolean checkRuleConventions() {
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
		} else if(ruleTypeKey.equals("IsAllowedToUse")) {
			conventionError = checkIsAllowedToUse();
		} else if(ruleTypeKey.equals("MustUse")) {
			conventionError = checkMustUse();
		} else if(ruleTypeKey.equals("SkipCall")) {
			conventionError = checkSkipCall();
		} else if(ruleTypeKey.equals("BackCall")) {
			conventionError = checkBackCall();
		}
		return conventionError;
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
		boolean isOnlyModuleAllowedToUseSucces = checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse");
		if(isOnlyModuleAllowedToUseSucces) {
			isOnlyModuleAllowedToUseSucces = checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse");
		}
		if(isOnlyModuleAllowedToUseSucces) {
			isOnlyModuleAllowedToUseSucces = checkRuleTypeAlreadyFromOtherToSelected("IsOnlyModuleAllowedToUse");
		}
		if(isOnlyModuleAllowedToUseSucces) {
			isOnlyModuleAllowedToUseSucces = checkRuleTypeAlreadyFromOtherToSelected("IsAllowedToUse");
		}
		if(isOnlyModuleAllowedToUseSucces) {
			isOnlyModuleAllowedToUseSucces = checkRuleTypeAlreadyFromOtherToSelected("MustUse");
		}
		return isOnlyModuleAllowedToUseSucces;
	}
	
	private boolean checkIsAllowedToUse() {
		boolean isAllowedToUseSucces = checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse");
		if(isAllowedToUseSucces) {
			isAllowedToUseSucces = checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse");
		}
		if(isAllowedToUseSucces) {
			isAllowedToUseSucces = checkRuleTypeAlreadyFromOtherToSelected("IsOnlyModuleAllowedToUse");
		}
		return isAllowedToUseSucces;
	}
	
	private boolean checkMustUse() {
		boolean mustUseSucces = checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse");
		if(mustUseSucces) {
			mustUseSucces = checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse");
		}
		if(mustUseSucces) {
			mustUseSucces = checkRuleTypeAlreadyFromOtherToSelected("IsOnlyModuleAllowedToUse");
		}
		return mustUseSucces;
	}
	
	private boolean checkSkipCall() {
		// #TODO:: implement Skip Call Checks
		return true;
	}
	
	private boolean checkBackCall() {
		// #TODO:: implement Back Call Checks
		return true;
	}
	
	private boolean checkRuleTypeAlreadySet() {
		for(AppliedRule appliedRule : this.getFromModuleAppliedRules(moduleFrom)) {
			if(appliedRule.getRuleType().equals(ruleTypeKey)) {
				setErrorMessage(DefineTranslator.translate("RuleTypeAlreadySet"));
				return false;
			}
		}
		return true;
	}
	
	private boolean checkRuleTypeAlreadyFromThisToSelected(String ruleType) {
		return this.checkRuleTypeAlreadyFromThisToSelected(ruleType, this.moduleFrom, this.moduleTo);
	}
	
	private boolean checkRuleTypeAlreadyFromThisToSelected(String ruleType, Module fromModule, Module toModule) {
		for(AppliedRule appliedRule : this.getFromModuleAppliedRules(fromModule)) {
			if(appliedRule.getRuleType().equals(ruleType) &&
			   appliedRule.getModuleFrom().getId() == fromModule.getId() &&
			   appliedRule.getModuleTo().getId() == toModule.getId()) {
				setErrorMessage(DefineTranslator.translate(appliedRule.getRuleType() + "AlreadyFromThisToSelected"));
				return false;
			}
		}
		for(Module fromModuleChild : fromModule.getSubModules()) {
			if(!this.checkRuleTypeAlreadyFromThisToSelected(ruleType, fromModuleChild, toModule)) {
				return false;
			}
		}
		for(Module toModuleChild : toModule.getSubModules()) {
			if(!this.checkRuleTypeAlreadyFromThisToSelected(ruleType, fromModule, toModuleChild)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkRuleTypeAlreadyFromThisToOther(String ruleType) {
		return this.checkRuleTypeAlreadyFromThisToOther(ruleType, this.moduleFrom, this.moduleTo);
	}
	
	private boolean checkRuleTypeAlreadyFromThisToOther(String ruleType, Module fromModule, Module toModule) {
		for(AppliedRule appliedRule : this.getFromModuleAppliedRules(fromModule)) {
			if(appliedRule.getRuleType().equals(ruleType) &&
				appliedRule.getModuleFrom().getId() == fromModule.getId() &&
				appliedRule.getModuleTo().getId() != toModule.getId()) {
				setErrorMessage(DefineTranslator.translate(ruleType + "AlreadyFromThisToOther"));
				return false;
			}
		}
		for(Module fromModuleChild : fromModule.getSubModules()) {
			if(!this.checkRuleTypeAlreadyFromThisToOther(ruleType, fromModuleChild, toModule)) {
				return false;
			}
		}
		for(Module toModuleChild : toModule.getSubModules()) {
			if(!this.checkRuleTypeAlreadyFromThisToOther(ruleType, fromModule, toModuleChild)) {
				return false;
			}
		}
		return true;
	}
		
	private ArrayList<AppliedRule> getFromModuleAppliedRules(Module fromModule) {
		ArrayList<Long> appliedRuleIds = appliedRuleService.getAppliedRulesIdsByModuleFromId(fromModule.getId());
		ArrayList<AppliedRule> appliedRules = new ArrayList<AppliedRule>();
		for(Long appliedRuleId : appliedRuleIds) {
			appliedRules.add(appliedRuleService.getAppliedRuleById(appliedRuleId));
		}
		return appliedRules;
	}
	
	private boolean checkRuleTypeAlreadyFromOtherToSelected(String ruleType) {
		return checkRuleTypeAlreadyFromOtherToSelected(ruleType, moduleFrom, moduleTo);
	}
	
	private boolean checkRuleTypeAlreadyFromOtherToSelected(String ruleType, Module fromModule, Module toModule) {
		for(AppliedRule appliedRule : getToModuleAppliedRules(toModule)) {
			if(appliedRule.getRuleType().equals(ruleType) &&
				appliedRule.getModuleFrom().getId() != fromModule.getId() &&
				appliedRule.getModuleTo().getId() == toModule.getId()) {
				setErrorMessage(DefineTranslator.translate(ruleType + "AlreadyFromOtherToSelected") + " \"" + appliedRule.getModuleFrom().getName() + "\"");
				return false;
			}
		}
		return true;
	}
	
	private ArrayList<AppliedRule> getToModuleAppliedRules(Module toModule) {
		ArrayList<Long> appliedRuleIds = appliedRuleService.getAppliedRulesIdsByModuleToId(toModule.getId());
		ArrayList<AppliedRule> appliedRules = new ArrayList<AppliedRule>();
		for(Long appliedRuleId : appliedRuleIds) {
			appliedRules.add(appliedRuleService.getAppliedRuleById(appliedRuleId));
		}
		return appliedRules;
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
