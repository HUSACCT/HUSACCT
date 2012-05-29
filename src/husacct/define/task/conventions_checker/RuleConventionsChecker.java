package husacct.define.task.conventions_checker;

import husacct.define.domain.module.Module;

public class RuleConventionsChecker {
	private Module moduleFrom;
	private Module moduleTo;
	private String ruleTypeKey;
	
	private ModuleCheckerHelper moduleCheckerHelper;
	private LayerCheckerHelper layerCheckerHelper;
	
	public RuleConventionsChecker(Module moduleFrom, Module moduleTo, String ruleTypeKey) {
		this.setModuleFrom(moduleFrom);
		this.setModuleTo(moduleTo);
		this.setRuleTypeKey(ruleTypeKey);
		this.moduleCheckerHelper = new ModuleCheckerHelper();
		this.layerCheckerHelper = new LayerCheckerHelper(moduleTo);
	}
	
	public boolean checkRuleConventions() {
		boolean conventionCheckSucces = true;
		if(ruleTypeKey.equals("VisibilityConvention")) {
			conventionCheckSucces = checkVisibilityConvention();
		} else if(ruleTypeKey.equals("VisibilityConventionException")) {
			conventionCheckSucces = checkVisibilityConventionException();
		} else if(ruleTypeKey.equals("NamingConvention")) {
			conventionCheckSucces = checkNamingConvention();
		} else if(ruleTypeKey.equals("NamingConventionException")) {
			conventionCheckSucces = checkNamingConventionException();
		} else if(ruleTypeKey.equals("SubClassConvention")) {
			conventionCheckSucces = checkSubClassConvention();
		} else if(ruleTypeKey.equals("InterfaceConvention")) {
			conventionCheckSucces = checkInterfaceConvention();
		} else if(ruleTypeKey.equals("IsNotAllowedToUse")) {
			conventionCheckSucces = checkIsNotAllowedToUse();
		} else if(ruleTypeKey.equals("IsOnlyAllowedToUse")) {
			conventionCheckSucces = checkIsOnlyAllowedToUse();
		} else if(ruleTypeKey.equals("IsOnlyModuleAllowedToUse")) {
			conventionCheckSucces = checkIsOnlyModuleAllowedToUse();
		} else if(ruleTypeKey.equals("IsAllowedToUse")) {
			conventionCheckSucces = checkIsAllowedToUse();
		} else if(ruleTypeKey.equals("MustUse")) {
			conventionCheckSucces = checkMustUse();
		} else if(ruleTypeKey.equals("SkipCall")) {
			conventionCheckSucces = checkSkipCall();
		} else if(ruleTypeKey.equals("BackCall")) {
			conventionCheckSucces = checkBackCall();
		}
		return conventionCheckSucces;
	}
	
	private boolean checkVisibilityConvention() {
		boolean visibilityConventionSucces = moduleCheckerHelper.checkRuleTypeAlreadySet(ruleTypeKey, moduleFrom);
		return visibilityConventionSucces;
	}
	
	private boolean checkVisibilityConventionException() {
		boolean visibilityConventionSucces = moduleCheckerHelper.checkRuleTypeAlreadySet(ruleTypeKey, moduleFrom);
		return visibilityConventionSucces;
	}
	
	private boolean checkNamingConvention() {
		boolean namingConventionSucces = moduleCheckerHelper.checkRuleTypeAlreadySet(ruleTypeKey, moduleFrom);
		return namingConventionSucces;
	}
	
	private boolean checkNamingConventionException() {
		boolean namingConventionSucces = moduleCheckerHelper.checkRuleTypeAlreadySet(ruleTypeKey, moduleFrom);
		return namingConventionSucces;
	}
	
	private boolean checkSubClassConvention() {
		boolean subClassConventionSucces = moduleCheckerHelper.checkRuleTypeAlreadySet(ruleTypeKey, moduleFrom);
		return subClassConventionSucces;
	}
	
	private boolean checkInterfaceConvention() {
		boolean interfaceConventionSucces = moduleCheckerHelper.checkRuleTypeAlreadySet(ruleTypeKey, moduleFrom);
		return interfaceConventionSucces;
	}
	
	private boolean checkIsNotAllowedToUse() {
		boolean isNotAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToSelected("IsOnlyAllowedToUse", moduleFrom, moduleTo);
		if(isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToSelected("IsOnlyModuleAllowedToUse", moduleFrom, moduleTo);
		}
		if(isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToSelected("IsAllowedToUse", moduleFrom, moduleTo);
		}
		if(isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToSelected("MustUse", moduleFrom, moduleTo);
		}
		return isNotAllowedToUseSucces;
	}
	
	private boolean checkIsOnlyAllowedToUse() {
		boolean isOnlyAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse", moduleFrom, moduleTo);
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse", moduleFrom, moduleTo);
		}
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromOtherToSelected("IsOnlyModuleAllowedToUse", moduleFrom, moduleTo);
		}
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToOther("IsAllowedToUse", moduleFrom, moduleTo);
		}
		if(isOnlyAllowedToUseSucces) {
			isOnlyAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToOther("MustUse", moduleFrom, moduleTo);
		}
		return isOnlyAllowedToUseSucces;
	}
	
	private boolean checkIsOnlyModuleAllowedToUse() {
		boolean isOnlyModuleAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse", moduleFrom, moduleTo);
		if(isOnlyModuleAllowedToUseSucces) {
			isOnlyModuleAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse", moduleFrom, moduleTo);
		}
		if(isOnlyModuleAllowedToUseSucces) {
			isOnlyModuleAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromOtherToSelected("IsOnlyModuleAllowedToUse", moduleFrom, moduleTo);
		}
		if(isOnlyModuleAllowedToUseSucces) {
			isOnlyModuleAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromOtherToSelected("IsAllowedToUse", moduleFrom, moduleTo);
		}
		if(isOnlyModuleAllowedToUseSucces) {
			isOnlyModuleAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromOtherToSelected("MustUse", moduleFrom, moduleTo);
		}
		return isOnlyModuleAllowedToUseSucces;
	}
	
	private boolean checkIsAllowedToUse() {
		boolean isAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse", moduleFrom, moduleTo);
		if(isAllowedToUseSucces) {
			isAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse", moduleFrom, moduleTo);
		}
		if(isAllowedToUseSucces) {
			isAllowedToUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromOtherToSelected("IsOnlyModuleAllowedToUse", moduleFrom, moduleTo);
		}
		return isAllowedToUseSucces;
	}
	
	private boolean checkMustUse() {
		boolean mustUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse", moduleFrom, moduleTo);
		if(mustUseSucces) {
			mustUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse", moduleFrom, moduleTo);
		}
		if(mustUseSucces) {
			mustUseSucces = moduleCheckerHelper.checkRuleTypeAlreadyFromOtherToSelected("IsOnlyModuleAllowedToUse", moduleFrom, moduleTo);
		}
		return mustUseSucces;
	}
	
	private boolean checkSkipCall() {
		if(layerCheckerHelper.checkTypeIsLayer(moduleFrom) && layerCheckerHelper.checkHasSkippedToLayer(moduleFrom)) {
			Long layerSkippedToId = layerCheckerHelper.getLayerSkippedToId(moduleFrom.getId());
			this.moduleTo = layerCheckerHelper.getLayerById(layerSkippedToId);
			return checkIsAllowedToUse();
		} else {
			return false;
		}
	}
	
	private boolean checkBackCall() {
		if(layerCheckerHelper.checkTypeIsLayer(moduleFrom) && layerCheckerHelper.checkHasLayerCalledBackTo(moduleFrom)) {
			Long calledBackLayerId = layerCheckerHelper.getPreviousLayerId(moduleFrom.getId());
			this.moduleTo = layerCheckerHelper.getLayerById(calledBackLayerId);
			return checkIsAllowedToUse();
		} else {
			return false;
		}
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
	
	public String getErrorMessage() {
		String errorMessage = moduleCheckerHelper.getErrorMessage();
		if(errorMessage == "") {
			errorMessage = layerCheckerHelper.getErrorMessage();
		}
		return errorMessage;
	}
}
