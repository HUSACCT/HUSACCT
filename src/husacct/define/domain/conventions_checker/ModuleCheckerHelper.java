package husacct.define.domain.conventions_checker;

import husacct.ServiceProvider;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.AppliedRuleDomainService;

import java.util.ArrayList;

public class ModuleCheckerHelper {

    private AppliedRuleDomainService appliedRuleService;
    private String errorMessage;

    public ModuleCheckerHelper() {
	setErrorMessage("");
	appliedRuleService = new AppliedRuleDomainService();
    }

    public boolean rootIsNotIncludedInRule(ModuleStrategy fromModule, ModuleStrategy toModule){
    	if(fromModule.getType().equals("Root") || toModule.getType().equals("Root"))
    		return false;
    	else
    		return true;
    }
    
    public boolean checkRuleTypeAlreadyFromOtherToSelected(String ruleType, ModuleStrategy fromModule, ModuleStrategy toModule) {
	for (AppliedRuleStrategy appliedRule : getToModuleAppliedRules(toModule)) {
	    if (appliedRule.getRuleTypeKey().equals(ruleType)
		    && checkRuleTypeAlreadyFromOtherToSelectedFromModuleId(appliedRule.getModuleFrom(), fromModule)
		    && appliedRule.getModuleTo().getId() == toModule.getId() 
		    && !appliedRule.isEnabled()) {
	    		setErrorMessage("'" + appliedRule.getModuleFrom().getName() + "' "
	    		+ ServiceProvider.getInstance().getLocaleService().getTranslatedString(ruleType) + " '"
	    		+ appliedRule.getModuleTo().getName() + "'");
	    		return false;
	    }
	}
	for (ModuleStrategy toModuleChild : toModule.getSubModules()) {
	    if (!checkRuleTypeAlreadyFromOtherToSelected(ruleType, fromModule, toModuleChild)) {
		return false;
	    }
	}
	return true;
    }

    private boolean checkRuleTypeAlreadyFromOtherToSelectedFromModuleId(ModuleStrategy appliedRuleModule, ModuleStrategy fromModule) {
	if (appliedRuleModule.getId() == fromModule.getId()) {
	    return false;
	} else {
	    for (ModuleStrategy fromModuleChild : fromModule.getSubModules()) {
		if (!checkRuleTypeAlreadyFromOtherToSelectedFromModuleId(
			appliedRuleModule, fromModuleChild)) {
		    return false;
		}
	    }
	}
	return true;
    }

    public boolean checkRuleTypeAlreadyFromThisToOther(String ruleType,
	    ModuleStrategy fromModule, ModuleStrategy toModule) {
	for (AppliedRuleStrategy appliedRule : getFromModuleAppliedRules(fromModule)) {
	    if (appliedRule.getRuleTypeKey().equals(ruleType)
		    && appliedRule.getModuleFrom().getId() == fromModule
			    .getId()
		    && appliedRule.getModuleTo().getId() != toModule.getId() && !appliedRule.isEnabled()) {
		setErrorMessage("'"
			+ appliedRule.getModuleFrom().getName()
			+ "' "
			+ ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString(ruleType) + " '"
			+ appliedRule.getModuleTo().getName() + "'");
		return false;
	    }
	}
	for (ModuleStrategy fromModuleChild : fromModule.getSubModules()) {
	    if (!checkRuleTypeAlreadyFromThisToOther(ruleType, fromModuleChild,
		    toModule)) {
		return false;
	    }
	}
	for (ModuleStrategy toModuleChild : toModule.getSubModules()) {
	    if (!checkRuleTypeAlreadyFromThisToOther(ruleType, fromModule,
		    toModuleChild)) {
		return false;
	    }
	}
	return true;
    }

    public boolean checkRuleTypeAlreadyFromThisToSelected(String ruleType,
	    ModuleStrategy fromModule, ModuleStrategy toModule) {
	for (AppliedRuleStrategy appliedRule : getFromModuleAppliedRules(fromModule)) {
	    if (appliedRule.getRuleTypeKey().equals(ruleType)
		    && appliedRule.getModuleFrom().getId() == fromModule
			    .getId()
		    && appliedRule.getModuleTo().getId() == toModule.getId() && appliedRule.isEnabled()) {
		setErrorMessage("'"
			+ appliedRule.getModuleFrom().getName()
			+ "' "
			+ ServiceProvider.getInstance().getLocaleService()
				.getTranslatedString(ruleType) + " '"
			+ appliedRule.getModuleTo().getName() + "'");
		return false;
	    }
	}
	for (ModuleStrategy fromModuleChild : fromModule.getSubModules()) {
	    if (!checkRuleTypeAlreadyFromThisToSelected(ruleType,
		    fromModuleChild, toModule)) {
		return false;
	    }
	}
	for (ModuleStrategy toModuleChild : toModule.getSubModules()) {
	    if (!checkRuleTypeAlreadyFromThisToSelected(ruleType, fromModule,
		    toModuleChild)) {
		return false;
	    }
	}
	return true;
    }

    public boolean checkRuleTypeAlreadySet(String ruleTypeKey, ModuleStrategy moduleFrom) {
	for (AppliedRuleStrategy appliedRule : getFromModuleAppliedRules(moduleFrom)) {
	    if (appliedRule.getRuleTypeKey().equals(ruleTypeKey)) {
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

    private ArrayList<AppliedRuleStrategy> getFromModuleAppliedRules(ModuleStrategy fromModule) {
	
    	
    ArrayList<Long> appliedRuleIds = new ArrayList<Long>();
    	appliedRuleIds=	appliedRuleService.getAppliedRulesIdsByModuleFromId(fromModule.getId());
	ArrayList<AppliedRuleStrategy> appliedRules = new ArrayList<AppliedRuleStrategy>();
	
	for (Long appliedRuleId : appliedRuleIds) {
	    appliedRules.add(appliedRuleService
		    .getAppliedRuleById(appliedRuleId));
	}
	return appliedRules;
    }

    private ArrayList<AppliedRuleStrategy> getToModuleAppliedRules(ModuleStrategy toModule) {
	ArrayList<Long> appliedRuleIds = appliedRuleService
		.getAppliedRulesIdsByModuleToId(toModule.getId());
	ArrayList<AppliedRuleStrategy> appliedRules = new ArrayList<AppliedRuleStrategy>();
	for (Long appliedRuleId : appliedRuleIds) {
	    appliedRules.add(appliedRuleService
		    .getAppliedRuleById(appliedRuleId));
	}
	return appliedRules;
    }

    public void setErrorMessage(String message) {
	if (!message.equals("")) {
	    errorMessage = ServiceProvider.getInstance().getLocaleService()
		    .getTranslatedString("NotAllowedBecauseDefined") + ":\n\n " + message;
	} else {
	    errorMessage = "";
	}
    }
}
