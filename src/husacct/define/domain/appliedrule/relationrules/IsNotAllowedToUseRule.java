package husacct.define.domain.appliedrule.relationrules;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.task.conventions_checker.LayerCheckerHelper;
import husacct.define.task.conventions_checker.ModuleCheckerHelper;

import java.util.ArrayList;

public class IsNotAllowedToUseRule extends AppliedRuleStrategy{
	private ModuleCheckerHelper moduleCheckerHelper = new ModuleCheckerHelper();
	private LayerCheckerHelper layerCheckerHelper = new LayerCheckerHelper(this.getModuleTo());

	public boolean checkConvention() {
		boolean conventionSuccess = moduleCheckerHelper
				.checkRuleTypeAlreadyFromThisToSelected("IsOnlyAllowedToUse",
						this.getModuleFrom(), this.getModuleTo());
			if (conventionSuccess) {
				conventionSuccess = moduleCheckerHelper
				    .checkRuleTypeAlreadyFromThisToSelected(
					    "IsOnlyModuleAllowedToUse", this.getModuleFrom(), this.getModuleTo());
			}
			if (conventionSuccess) {
				conventionSuccess = moduleCheckerHelper
				    .checkRuleTypeAlreadyFromThisToSelected("IsAllowedToUse",
				    		this.getModuleFrom(), this.getModuleTo());
			}
			if (conventionSuccess) {
				conventionSuccess = moduleCheckerHelper
				    .checkRuleTypeAlreadyFromThisToSelected("MustUse",
				    		this.getModuleFrom(), this.getModuleTo());
			}
			if (conventionSuccess
				&& layerCheckerHelper.checkTypeIsLayer(this.getModuleFrom())
				&& layerCheckerHelper.checkTypeIsLayer(this.getModuleTo())) {
			    ArrayList<Layer> backCallLayers = layerCheckerHelper
				    .getBackCallLayers(this.getModuleFrom().getId());
			    ArrayList<Layer> skipCallLayers = layerCheckerHelper
				    .getSkipCallLayers(this.getModuleFrom().getId());
			    for (Layer skipCallLayer : skipCallLayers) {
				if (skipCallLayer.equals(this.getModuleTo())) {
					conventionSuccess = moduleCheckerHelper
					    .checkRuleTypeAlreadySet(
						    "IsNotAllowedToMakeSkipCall", this.getModuleFrom());
				}
			    }
			    for (Layer backCallLayer : backCallLayers) {
				if (backCallLayer.equals(this.getModuleTo())) {
					conventionSuccess = moduleCheckerHelper
					    .checkRuleTypeAlreadySet(
						    "IsNotAllowedToMakeBackCall", this.getModuleFrom());
				}
			    }
			}
			return conventionSuccess;
	}

}
