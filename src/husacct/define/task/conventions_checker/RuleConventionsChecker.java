package husacct.define.task.conventions_checker;

import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;

import java.util.ArrayList;

public class RuleConventionsChecker {
    private LayerCheckerHelper layerCheckerHelper;
    private ModuleCheckerHelper moduleCheckerHelper;
    private Module moduleFrom;

    private Module moduleTo;
    private String ruleTypeKey;

    public RuleConventionsChecker(Module moduleFrom, Module moduleTo,
	    String ruleTypeKey) {
	setModuleFrom(moduleFrom);
	setModuleTo(moduleTo);
	setRuleTypeKey(ruleTypeKey);
	moduleCheckerHelper = new ModuleCheckerHelper();
	layerCheckerHelper = new LayerCheckerHelper(moduleTo);
    }

    private boolean checkBackCall() {
	boolean backCallSucces = moduleCheckerHelper.checkRuleTypeAlreadySet(
		ruleTypeKey, moduleFrom);
	if (backCallSucces) {
	    backCallSucces = layerCheckerHelper.checkTypeIsLayer(moduleFrom);
	}
	if (backCallSucces) {
	    ArrayList<Layer> backCallLayers = layerCheckerHelper
		    .getBackCallLayers(moduleFrom.getId());
	    for (Layer backCallLayer : backCallLayers) {
		moduleTo = backCallLayer;
		if (!checkIsNotAllowedToUse()) {
		    backCallSucces = false;
		    break;
		}
	    }
	}
	return backCallSucces;
    }

    /**
     * checks are the same as MustUse
     */
    private boolean checkInterfaceConvention() {
	boolean interfaceConventionSucces = moduleCheckerHelper
		.checkRuleTypeAlreadySet(ruleTypeKey, moduleFrom);
	if (interfaceConventionSucces) {
	    interfaceConventionSucces = checkMustUse();
	}
	return interfaceConventionSucces;
    }

    private boolean checkIsAllowedToUse() {
	boolean isAllowedToUseSucces = moduleCheckerHelper
		.checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse",
			moduleFrom, moduleTo);
	if (isAllowedToUseSucces) {
	    isAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse",
			    moduleFrom, moduleTo);
	}
	if (isAllowedToUseSucces) {
	    isAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromOtherToSelected(
			    "IsOnlyModuleAllowedToUse", moduleFrom, moduleTo);
	}
	return isAllowedToUseSucces;
    }

    private boolean checkIsNotAllowedToUse() {
	boolean isNotAllowedToUseSucces = moduleCheckerHelper
		.checkRuleTypeAlreadyFromThisToSelected("IsOnlyAllowedToUse",
			moduleFrom, moduleTo);
	if (isNotAllowedToUseSucces) {
	    isNotAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromThisToSelected(
			    "IsOnlyModuleAllowedToUse", moduleFrom, moduleTo);
	}
	if (isNotAllowedToUseSucces) {
	    isNotAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromThisToSelected("IsAllowedToUse",
			    moduleFrom, moduleTo);
	}
	if (isNotAllowedToUseSucces) {
	    isNotAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromThisToSelected("MustUse",
			    moduleFrom, moduleTo);
	}
	if (isNotAllowedToUseSucces
		&& layerCheckerHelper.checkTypeIsLayer(moduleFrom)
		&& layerCheckerHelper.checkTypeIsLayer(moduleTo)) {
	    ArrayList<Layer> backCallLayers = layerCheckerHelper
		    .getBackCallLayers(moduleFrom.getId());
	    ArrayList<Layer> skipCallLayers = layerCheckerHelper
		    .getSkipCallLayers(moduleFrom.getId());
	    for (Layer skipCallLayer : skipCallLayers) {
		if (skipCallLayer.equals(moduleTo)) {
		    isNotAllowedToUseSucces = moduleCheckerHelper
			    .checkRuleTypeAlreadySet(
				    "IsNotAllowedToMakeSkipCall", moduleFrom);
		}
	    }
	    for (Layer backCallLayer : backCallLayers) {
		if (backCallLayer.equals(moduleTo)) {
		    isNotAllowedToUseSucces = moduleCheckerHelper
			    .checkRuleTypeAlreadySet(
				    "IsNotAllowedToMakeBackCall", moduleFrom);
		}
	    }
	}
	return isNotAllowedToUseSucces;
    }

    private boolean checkIsOnlyAllowedToUse() {
	boolean isOnlyAllowedToUseSucces = moduleCheckerHelper
		.checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse",
			moduleFrom, moduleTo);
	if (isOnlyAllowedToUseSucces) {
	    isOnlyAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromThisToSelected(
			    "IsOnlyAllowedToUse", moduleFrom, moduleTo);
	}
	if (isOnlyAllowedToUseSucces) {
	    isOnlyAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse",
			    moduleFrom, moduleTo);
	}
	if (isOnlyAllowedToUseSucces) {
	    isOnlyAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromOtherToSelected(
			    "IsOnlyModuleAllowedToUse", moduleFrom, moduleTo);
	}
	if (isOnlyAllowedToUseSucces) {
	    isOnlyAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromThisToOther("IsAllowedToUse",
			    moduleFrom, moduleTo);
	}
	if (isOnlyAllowedToUseSucces) {
	    isOnlyAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromThisToOther("MustUse", moduleFrom,
			    moduleTo);
	}
	if (isOnlyAllowedToUseSucces) {
	    if (!moduleCheckerHelper.checkRuleTypeAlreadySet(
		    "IsNotAllowedToMakeSkipCall", moduleFrom)) {
		ArrayList<Layer> skipCallLayers = layerCheckerHelper
			.getSkipCallLayers(moduleFrom.getId());
		for (Layer skipCallLayer : skipCallLayers) {
		    if (skipCallLayer == moduleTo) {
			isOnlyAllowedToUseSucces = false;
			break;
		    }
		}
	    }
	}
	if (isOnlyAllowedToUseSucces) {
	    if (!moduleCheckerHelper.checkRuleTypeAlreadySet(
		    "IsNotAllowedToMakeBackCall", moduleFrom)) {
		ArrayList<Layer> skipCallLayers = layerCheckerHelper
			.getBackCallLayers(moduleFrom.getId());
		for (Layer skipCallLayer : skipCallLayers) {
		    if (skipCallLayer == moduleTo) {
			isOnlyAllowedToUseSucces = false;
			break;
		    }
		}
	    }
	}
	return isOnlyAllowedToUseSucces;
    }

    private boolean checkIsOnlyModuleAllowedToUse() {
	boolean isOnlyModuleAllowedToUseSucces = moduleCheckerHelper
		.checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse",
			moduleFrom, moduleTo);
	if (isOnlyModuleAllowedToUseSucces) {
	    isOnlyModuleAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse",
			    moduleFrom, moduleTo);
	}
	if (isOnlyModuleAllowedToUseSucces) {
	    isOnlyModuleAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromOtherToSelected(
			    "IsOnlyModuleAllowedToUse", moduleFrom, moduleTo);
	}
	if (isOnlyModuleAllowedToUseSucces) {
	    isOnlyModuleAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromOtherToSelected("IsAllowedToUse",
			    moduleFrom, moduleTo);
	}
	if (isOnlyModuleAllowedToUseSucces) {
	    isOnlyModuleAllowedToUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromOtherToSelected("MustUse",
			    moduleFrom, moduleTo);
	}
	if (isOnlyModuleAllowedToUseSucces) {
	    if (!moduleCheckerHelper.checkRuleTypeAlreadySet(
		    "IsNotAllowedToMakeSkipCall", moduleFrom)) {
		ArrayList<Layer> skipCallLayers = layerCheckerHelper
			.getSkipCallLayers(moduleFrom.getId());
		for (Layer skipCallLayer : skipCallLayers) {
		    if (skipCallLayer == moduleTo) {
			isOnlyModuleAllowedToUseSucces = false;
			break;
		    }
		}
	    }
	}
	if (isOnlyModuleAllowedToUseSucces) {
	    if (!moduleCheckerHelper.checkRuleTypeAlreadySet(
		    "IsNotAllowedToMakeBackCall", moduleFrom)) {
		ArrayList<Layer> skipCallLayers = layerCheckerHelper
			.getBackCallLayers(moduleFrom.getId());
		for (Layer skipCallLayer : skipCallLayers) {
		    if (skipCallLayer == moduleTo) {
			isOnlyModuleAllowedToUseSucces = false;
			break;
		    }
		}
	    }
	}
	return isOnlyModuleAllowedToUseSucces;
    }

    private boolean checkMustUse() {
	boolean mustUseSucces = moduleCheckerHelper
		.checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse",
			moduleFrom, moduleTo);
	if (mustUseSucces) {
	    mustUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse",
			    moduleFrom, moduleTo);
	}
	if (mustUseSucces) {
	    mustUseSucces = moduleCheckerHelper
		    .checkRuleTypeAlreadyFromOtherToSelected(
			    "IsOnlyModuleAllowedToUse", moduleFrom, moduleTo);
	}
	if (mustUseSucces) {
	    if (!moduleCheckerHelper.checkRuleTypeAlreadySet(
		    "IsNotAllowedToMakeSkipCall", moduleFrom)) {
		ArrayList<Layer> skipCallLayers = layerCheckerHelper
			.getSkipCallLayers(moduleFrom.getId());
		for (Layer skipCallLayer : skipCallLayers) {
		    if (skipCallLayer == moduleTo) {
			mustUseSucces = false;
			break;
		    }
		}
	    }
	}
	if (mustUseSucces) {
	    if (!moduleCheckerHelper.checkRuleTypeAlreadySet(
		    "IsNotAllowedToMakeBackCall", moduleFrom)) {
		ArrayList<Layer> skipCallLayers = layerCheckerHelper
			.getBackCallLayers(moduleFrom.getId());
		for (Layer skipCallLayer : skipCallLayers) {
		    if (skipCallLayer == moduleTo) {
			mustUseSucces = false;
			break;
		    }
		}
	    }
	}
	return mustUseSucces;
    }

    private boolean checkNamingConvention() {
	boolean namingConventionSucces = moduleCheckerHelper
		.checkRuleTypeAlreadySet(ruleTypeKey, moduleFrom);
	return namingConventionSucces;
    }

    public boolean checkRuleConventions() {
	boolean conventionCheckSucces = true;
	if (ruleTypeKey.equals("VisibilityConvention")) {
	    conventionCheckSucces = checkVisibilityConvention();
	} else if (ruleTypeKey.equals("NamingConvention")) {
	    conventionCheckSucces = checkNamingConvention();
	} else if (ruleTypeKey.equals("SubClassConvention")) {
	    conventionCheckSucces = checkSubClassConvention();
	} else if (ruleTypeKey.equals("InterfaceConvention")) {
	    conventionCheckSucces = checkInterfaceConvention();
	} else if (ruleTypeKey.equals("IsNotAllowedToUse")) {
	    conventionCheckSucces = checkIsNotAllowedToUse();
	} else if (ruleTypeKey.equals("IsOnlyAllowedToUse")) {
	    conventionCheckSucces = checkIsOnlyAllowedToUse();
	} else if (ruleTypeKey.equals("IsOnlyModuleAllowedToUse")) {
	    conventionCheckSucces = checkIsOnlyModuleAllowedToUse();
	} else if (ruleTypeKey.equals("IsAllowedToUse")) {
	    conventionCheckSucces = checkIsAllowedToUse();
	} else if (ruleTypeKey.equals("MustUse")) {
	    conventionCheckSucces = checkMustUse();
	} else if (ruleTypeKey.equals("IsNotAllowedToMakeSkipCall")) {
	    conventionCheckSucces = checkSkipCall();
	} else if (ruleTypeKey.equals("IsNotAllowedToMakeBackCall")) {
	    conventionCheckSucces = checkBackCall();
	}
	return conventionCheckSucces;
    }

    private boolean checkSkipCall() {
	boolean skipCallSucces = moduleCheckerHelper.checkRuleTypeAlreadySet(
		ruleTypeKey, moduleFrom);
	if (skipCallSucces) {
	    skipCallSucces = layerCheckerHelper.checkTypeIsLayer(moduleFrom);
	}
	if (skipCallSucces) {
	    ArrayList<Layer> skipCallLayers = layerCheckerHelper
		    .getSkipCallLayers(moduleFrom.getId());
	    for (Layer skipCallLayer : skipCallLayers) {
		moduleTo = skipCallLayer;
		if (!checkIsNotAllowedToUse()) {
		    skipCallSucces = false;
		    break;
		}
	    }
	}
	return skipCallSucces;
    }

    /**
     * checks are the same as MustUse
     */
    private boolean checkSubClassConvention() {
	boolean subClassConventionSucces = moduleCheckerHelper
		.checkRuleTypeAlreadySet(ruleTypeKey, moduleFrom);
	if (subClassConventionSucces) {
	    subClassConventionSucces = checkMustUse();
	}
	return subClassConventionSucces;
    }

    private boolean checkVisibilityConvention() {
	boolean visibilityConventionSucces = moduleCheckerHelper
		.checkRuleTypeAlreadySet(ruleTypeKey, moduleFrom);
	return visibilityConventionSucces;
    }

    public String getErrorMessage() {
	String errorMessage = moduleCheckerHelper.getErrorMessage();
	if (errorMessage == "") {
	    errorMessage = layerCheckerHelper.getErrorMessage();
	}
	return errorMessage;
    }

    public Module getModuleFrom() {
	return moduleFrom;
    }

    public Module getModuleTo() {
	return moduleTo;
    }

    public String getRuleTypeKey() {
	return ruleTypeKey;
    }

    public void setModuleFrom(Module moduleFrom) {
	this.moduleFrom = moduleFrom;
    }

    public void setModuleTo(Module moduleTo) {
	this.moduleTo = moduleTo;
    }

    public void setRuleTypeKey(String ruleTypeKey) {
	this.ruleTypeKey = ruleTypeKey;
    }
}