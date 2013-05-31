package husacct.define.domain.appliedrule.relationrules;

import java.util.ArrayList;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.Layer;
import husacct.define.task.conventions_checker.LayerCheckerHelper;
import husacct.define.task.conventions_checker.ModuleCheckerHelper;

public class MustUseRule extends AppliedRuleStrategy{
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
				    .checkRuleTypeAlreadyFromThisToOther("IsOnlyAllowedToUse",
				    		this.getModuleFrom(), this.getModuleTo());
			}
			if (conventionSuccess) {
				conventionSuccess = moduleCheckerHelper
				    .checkRuleTypeAlreadyFromOtherToSelected(
					    "IsOnlyModuleAllowedToUse", this.getModuleFrom(), this.getModuleTo());
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
