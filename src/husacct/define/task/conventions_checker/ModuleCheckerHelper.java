package husacct.define.task.conventions_checker;

import husacct.ServiceProvider;
import husacct.define.domain.AppliedRule;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;

import java.util.ArrayList;

public class ModuleCheckerHelper {

    private AppliedRuleDomainService appliedRuleService;
    private String errorMessage;

    public ModuleCheckerHelper() {
	setErrorMessage("");
	appliedRuleService = new AppliedRuleDomainService();
    }

    public boolean checkRuleTypeAlreadyFromOtherToSelected(String ruleType,
	    Module fromModule, Module toModule) {
	for (AppliedRule appliedRule : getToModuleAppliedRules(toModule)) {
	    if (appliedRule.getRuleType().equals(ruleType)
		    && checkRuleTypeAlreadyFromOtherToSelectedFromModuleId(
			    appliedRule.getModuleFrom(), fromModule)
		    && appliedRule.getModuleTo().getId() == toModule.getId()) {
		setErrorMessage("'"
			+ appliedRule.getModuleFrom().getName()
			+ "' "
			+ ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString(ruleType) + " '"
			+ appliedRule.getModuleTo().getName() + "'");
		return false;
	    }
	}
	for (Module toModuleChild : toModule.getSubModules()) {
	    if (!checkRuleTypeAlreadyFromOtherToSelected(ruleType, fromModule,
		    toModuleChild)) {
		return false;
	    }
	}
	return true;
    }

    private boolean checkRuleTypeAlreadyFromOtherToSelectedFromModuleId(
	    Module appliedRuleModule, Module fromModule) {
	if (appliedRuleModule.getId() == fromModule.getId()) {
	    return false;
	} else {
	    for (Module fromModuleChild : fromModule.getSubModules()) {
		if (!checkRuleTypeAlreadyFromOtherToSelectedFromModuleId(
			appliedRuleModule, fromModuleChild)) {
		    return false;
		}
	    }
	}
	return true;
    }

    public boolean checkRuleTypeAlreadyFromThisToOther(String ruleType,
	    Module fromModule, Module toModule) {
	for (AppliedRule appliedRule : getFromModuleAppliedRules(fromModule)) {
	    if (appliedRule.getRuleType().equals(ruleType)
		    && appliedRule.getModuleFrom().getId() == fromModule
			    .getId()
		    && appliedRule.getModuleTo().getId() != toModule.getId()) {
		setErrorMessage("'"
			+ appliedRule.getModuleFrom().getName()
			+ "' "
			+ ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString(ruleType) + " '"
			+ appliedRule.getModuleTo().getName() + "'");
		return false;
	    }
	}
	for (Module fromModuleChild : fromModule.getSubModules()) {
	    if (!checkRuleTypeAlreadyFromThisToOther(ruleType, fromModuleChild,
		    toModule)) {
		return false;
	    }
	}
	for (Module toModuleChild : toModule.getSubModules()) {
	    if (!checkRuleTypeAlreadyFromThisToOther(ruleType, fromModule,
		    toModuleChild)) {
		return false;
	    }
	}
	return true;
    }

    public boolean checkRuleTypeAlreadyFromThisToSelected(String ruleType,
	    Module fromModule, Module toModule) {
	for (AppliedRule appliedRule : getFromModuleAppliedRules(fromModule)) {
	    if (appliedRule.getRuleType().equals(ruleType)
		    && appliedRule.getModuleFrom().getId() == fromModule
			    .getId()
		    && appliedRule.getModuleTo().getId() == toModule.getId()) {
		setErrorMessage("'"
			+ appliedRule.getModuleFrom().getName()
			+ "' "
			+ ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString(ruleType) + " '"
			+ appliedRule.getModuleTo().getName() + "'");
		return false;
	    }
	}
	for (Module fromModuleChild : fromModule.getSubModules()) {
	    if (!checkRuleTypeAlreadyFromThisToSelected(ruleType,
		    fromModuleChild, toModule)) {
		return false;
	    }
	}
	for (Module toModuleChild : toModule.getSubModules()) {
	    if (!checkRuleTypeAlreadyFromThisToSelected(ruleType, fromModule,
		    toModuleChild)) {
		return false;
	    }
	}
	return true;
    }

    public boolean checkRuleTypeAlreadySet(String ruleTypeKey, Module moduleFrom) {
	for (AppliedRule appliedRule : getFromModuleAppliedRules(moduleFrom)) {
	    if (appliedRule.getRuleType().equals(ruleTypeKey)) {
		setErrorMessage("'"
			+ ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString(ruleTypeKey) + "'");
		return false;
	    }
	}
	return true;
    }

    public String getErrorMessage() {
	return errorMessage;
    }

    private ArrayList<AppliedRule> getFromModuleAppliedRules(Module fromModule) {
	ArrayList<Long> appliedRuleIds = appliedRuleService
		.getAppliedRulesIdsByModuleFromId(fromModule.getId());
	ArrayList<AppliedRule> appliedRules = new ArrayList<AppliedRule>();
	for (Long appliedRuleId : appliedRuleIds) {
	    appliedRules.add(appliedRuleService
		    .getAppliedRuleById(appliedRuleId));
	}
	return appliedRules;
    }

    private ArrayList<AppliedRule> getToModuleAppliedRules(Module toModule) {
	ArrayList<Long> appliedRuleIds = appliedRuleService
		.getAppliedRulesIdsByModuleToId(toModule.getId());
	ArrayList<AppliedRule> appliedRules = new ArrayList<AppliedRule>();
	for (Long appliedRuleId : appliedRuleIds) {
	    appliedRules.add(appliedRuleService
		    .getAppliedRuleById(appliedRuleId));
	}
	return appliedRules;
    }

    public void setErrorMessage(String message) {
	if (message != "") {
	    errorMessage = ServiceProvider.getInstance().getLocaleService()
		    .getTranslatedString("NotAllowedBecauseDefined")
		    + ":\n\n " + message;
	} else {
	    errorMessage = "";
	}
    }
}
