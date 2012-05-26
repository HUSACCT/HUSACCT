package husacct.define.task.conventions_checker;

import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;

import java.util.ArrayList;

public class ModuleCheckerHelper {
	
	private AppliedRuleDomainService appliedRuleService;
	private String errorMessage;
	
	public ModuleCheckerHelper() {
		this.setErrorMessage("");
		this.appliedRuleService = new AppliedRuleDomainService();
	}
	
	public boolean checkRuleTypeAlreadySet(String ruleTypeKey, Module moduleFrom) {
		for(AppliedRule appliedRule : this.getFromModuleAppliedRules(moduleFrom)) {
			if(appliedRule.getRuleType().equals(ruleTypeKey)) {
				setErrorMessage(DefineTranslator.translate("RuleTypeAlreadySet"));
				return false;
			}
		}
		return true;
	}
	
	public boolean checkRuleTypeAlreadyFromThisToSelected(String ruleType, Module fromModule, Module toModule) {
		for(AppliedRule appliedRule : this.getFromModuleAppliedRules(fromModule)) {
			if(appliedRule.getRuleType().equals(ruleType) &&
			   appliedRule.getModuleFrom().getId() == fromModule.getId() &&
			   appliedRule.getModuleTo().getId() == toModule.getId()) {
				setErrorMessage("'" + appliedRule.getModuleFrom().getName() + "' " + DefineTranslator.translate(ruleType) + " '" + appliedRule.getModuleTo().getName() + "'");
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
	
	public boolean checkRuleTypeAlreadyFromThisToOther(String ruleType, Module fromModule, Module toModule) {
		for(AppliedRule appliedRule : this.getFromModuleAppliedRules(fromModule)) {
			if(appliedRule.getRuleType().equals(ruleType) &&
				appliedRule.getModuleFrom().getId() == fromModule.getId() &&
				appliedRule.getModuleTo().getId() != toModule.getId()) {
				setErrorMessage("'" + appliedRule.getModuleFrom().getName() + "' " + DefineTranslator.translate(ruleType) + " '" + appliedRule.getModuleTo().getName() + "'");
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
	
	public boolean checkRuleTypeAlreadyFromOtherToSelected(String ruleType, Module fromModule, Module toModule) {
		for(AppliedRule appliedRule : getToModuleAppliedRules(toModule)) {
			if(appliedRule.getRuleType().equals(ruleType) &&
				checkRuleTypeAlreadyFromOtherToSelectedFromModuleId(appliedRule.getModuleFrom(), fromModule) &&
				appliedRule.getModuleTo().getId() == toModule.getId()) {
				setErrorMessage("'" + appliedRule.getModuleFrom().getName() + "' " + DefineTranslator.translate(ruleType) + " '" + appliedRule.getModuleTo().getName() + "'");
				return false;
			}
		}
		for(Module toModuleChild : toModule.getSubModules()) {
			if(!this.checkRuleTypeAlreadyFromOtherToSelected(ruleType, fromModule, toModuleChild)) {
				return false;
			}
		}
		return true;
	}
	
	private boolean checkRuleTypeAlreadyFromOtherToSelectedFromModuleId(Module appliedRuleModule, Module fromModule) {
		if(appliedRuleModule.getId() == fromModule.getId()) {
			return false;
		} else {
			for(Module fromModuleChild : fromModule.getSubModules()) {
				if(!checkRuleTypeAlreadyFromOtherToSelectedFromModuleId(appliedRuleModule, fromModuleChild)) {
					return false;
				}
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

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String message) {
		if(message != "") {
			this.errorMessage = DefineTranslator.translate("NotAllowedBecauseDefined") + ":\n\n " + message;
		} else {
			this.errorMessage = "";
		}
	}
}
