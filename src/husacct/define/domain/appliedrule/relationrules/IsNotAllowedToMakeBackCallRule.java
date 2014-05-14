package husacct.define.domain.appliedrule.relationrules;


import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.conventions_checker.LayerCheckerHelper;
import husacct.define.domain.conventions_checker.ModuleCheckerHelper;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;

import java.util.ArrayList;

public class IsNotAllowedToMakeBackCallRule extends AppliedRuleStrategy{
	private ModuleCheckerHelper moduleCheckerHelper;
	private LayerCheckerHelper layerCheckerHelper;

	public boolean checkConvention() {
		moduleCheckerHelper = new ModuleCheckerHelper();
		layerCheckerHelper = new LayerCheckerHelper(this.getModuleTo());
		boolean conventionSuccess = moduleCheckerHelper.checkRuleTypeAlreadySet(
				this.getRuleType(), this.getModuleFrom());
		if (conventionSuccess) {
			conventionSuccess = layerCheckerHelper.checkTypeIsLayer(this.getModuleFrom());
		}
		if (conventionSuccess) {
			ArrayList<ModuleStrategy> backCallLayers = layerCheckerHelper
					.getBackCallLayers(this.getModuleFrom().getId());
			for (ModuleStrategy backCallLayer : backCallLayers) {
				this.setModuleTo(backCallLayer);
				if (!checkIsNotAllowedToUse()) {
					conventionSuccess = false;
					break;
				}
			}
		}
		return conventionSuccess;
	}

	private boolean checkIsNotAllowedToUse() {
		boolean isNotAllowedToUseSucces = moduleCheckerHelper
				.checkRuleTypeAlreadyFromThisToSelected("IsOnlyAllowedToUse",
						this.getModuleFrom(), this.getModuleTo());
		if (isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = moduleCheckerHelper
					.checkRuleTypeAlreadyFromThisToSelected(
							"IsTheOnlyModuleAllowedToUse", this.getModuleFrom(), this.getModuleTo());
		}
		if (isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = moduleCheckerHelper
					.checkRuleTypeAlreadyFromThisToSelected("IsAllowedToUse",
							this.getModuleFrom(), this.getModuleTo());
		}
		if (isNotAllowedToUseSucces) {
			isNotAllowedToUseSucces = moduleCheckerHelper
					.checkRuleTypeAlreadyFromThisToSelected("MustUse",
							this.getModuleFrom(), this.getModuleTo());
		}
		if (isNotAllowedToUseSucces
				&& layerCheckerHelper.checkTypeIsLayer(this.getModuleFrom())
				&& layerCheckerHelper.checkTypeIsLayer(this.getModuleTo())) {
			ArrayList<ModuleStrategy> backCallLayers = layerCheckerHelper
					.getBackCallLayers(this.getModuleFrom().getId());
			for (ModuleStrategy backCallLayer : backCallLayers) {
				if (backCallLayer.equals(this.getModuleTo())) {
					isNotAllowedToUseSucces = moduleCheckerHelper
							.checkRuleTypeAlreadySet(
									"IsNotAllowedToMakeBackCall", this.getModuleFrom());
				}
			}
		}
		return isNotAllowedToUseSucces;
	}

}
