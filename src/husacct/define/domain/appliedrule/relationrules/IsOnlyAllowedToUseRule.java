package husacct.define.domain.appliedrule.relationrules;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.task.conventions_checker.LayerCheckerHelper;
import husacct.define.task.conventions_checker.ModuleCheckerHelper;

import java.util.ArrayList;

public class IsOnlyAllowedToUseRule extends AppliedRuleStrategy{
	private ModuleCheckerHelper moduleCheckerHelper;
	private LayerCheckerHelper layerCheckerHelper;

	public boolean checkConvention() {
		moduleCheckerHelper = new ModuleCheckerHelper();
		layerCheckerHelper = new LayerCheckerHelper(this.getModuleTo());
		boolean conventionSuccess = moduleCheckerHelper
				.checkRuleTypeAlreadyFromThisToSelected("IsNotAllowedToUse",
						this.getModuleFrom(), this.getModuleTo());
			if (conventionSuccess) {
				conventionSuccess = moduleCheckerHelper
				    .checkRuleTypeAlreadyFromThisToSelected(
					    "IsOnlyAllowedToUse", this.getModuleFrom(), this.getModuleTo());
			}
			if (conventionSuccess) {
				conventionSuccess = moduleCheckerHelper
				    .checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse",
				    		this.getModuleFrom(), this.getModuleTo());
			}
			if (conventionSuccess) {
				conventionSuccess = moduleCheckerHelper
				    .checkRuleTypeAlreadyFromOtherToSelected(
					    "IsTheOnlyModuleAllowedToUse", this.getModuleFrom(), this.getModuleTo());
			}
			if (conventionSuccess) {
			    conventionSuccess = moduleCheckerHelper
				    .checkRuleTypeAlreadyFromThisToOther("IsAllowedToUse",
				    		this.getModuleFrom(), this.getModuleTo());
			}
			if (conventionSuccess) {
			    conventionSuccess = moduleCheckerHelper
				    .checkRuleTypeAlreadyFromThisToOther("MustUse", this.getModuleFrom(),
				    		this.getModuleTo());
			}
			if (conventionSuccess) {
			    if (!moduleCheckerHelper.checkRuleTypeAlreadySet(
				    "IsNotAllowedToMakeSkipCall", this.getModuleFrom())) {
				ArrayList<ModuleStrategy> skipCallLayers = layerCheckerHelper
					.getSkipCallLayers(this.getModuleFrom().getId());
				for (ModuleStrategy skipCallLayer : skipCallLayers) {
				    if (skipCallLayer == this.getModuleTo()) {
					conventionSuccess = false;
					break;
				    }
				}
			    }
			}
			if (conventionSuccess) {
			    if (!moduleCheckerHelper.checkRuleTypeAlreadySet(
				    "IsNotAllowedToMakeBackCall", this.getModuleFrom())) {
				ArrayList<ModuleStrategy> skipCallLayers = layerCheckerHelper
					.getBackCallLayers(this.getModuleFrom().getId());
				for (ModuleStrategy skipCallLayer : skipCallLayers) {
				    if (skipCallLayer == this.getModuleTo()) {
					conventionSuccess = false;
					break;
				    }
				}
			    }
			}
			return conventionSuccess;
	}

}
