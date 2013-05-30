package husacct.define.domain.appliedrule.relationrules;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.task.conventions_checker.LayerCheckerHelper;
import husacct.define.task.conventions_checker.ModuleCheckerHelper;

import java.util.ArrayList;

public class IsOnlyAllowedToUseRule extends AppliedRuleStrategy{
	private ModuleCheckerHelper moduleCheckerHelper = new ModuleCheckerHelper();
	private LayerCheckerHelper layerCheckerHelper = new LayerCheckerHelper(this.getModuleTo());

	public boolean checkConvention() {
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
					    "IsOnlyModuleAllowedToUse", this.getModuleFrom(), this.getModuleTo());
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
				ArrayList<Layer> skipCallLayers = layerCheckerHelper
					.getSkipCallLayers(this.getModuleFrom().getId());
				for (Layer skipCallLayer : skipCallLayers) {
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
				ArrayList<Layer> skipCallLayers = layerCheckerHelper
					.getBackCallLayers(this.getModuleFrom().getId());
				for (Layer skipCallLayer : skipCallLayers) {
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
