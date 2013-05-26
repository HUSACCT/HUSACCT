package husacct.define.domain.appliedrule.relationrules;

import java.util.ArrayList;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.Layer;
import husacct.define.task.conventions_checker.LayerCheckerHelper;
import husacct.define.task.conventions_checker.ModuleCheckerHelper;

public class IsNotAllowedToMakeBackCallRule extends AppliedRuleStrategy{
	private ModuleCheckerHelper moduleCheckerHelper = new ModuleCheckerHelper();
	private LayerCheckerHelper layerCheckerHelper = new LayerCheckerHelper(this.getModuleTo());

	public boolean checkConvention() {
		boolean conventionSuccess = moduleCheckerHelper.checkRuleTypeAlreadySet(
				this.getRuleType(), this.getModuleFrom());
		if (conventionSuccess) {
			conventionSuccess = layerCheckerHelper.checkTypeIsLayer(this.getModuleFrom());
		}
		if (conventionSuccess) {
			ArrayList<Layer> backCallLayers = layerCheckerHelper
					.getBackCallLayers(this.getModuleFrom().getId());
			for (Layer backCallLayer : backCallLayers) {
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
							"IsOnlyModuleAllowedToUse", this.getModuleFrom(), this.getModuleTo());
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
			ArrayList<Layer> backCallLayers = layerCheckerHelper
					.getBackCallLayers(this.getModuleFrom().getId());
			ArrayList<Layer> skipCallLayers = layerCheckerHelper
					.getSkipCallLayers(this.getModuleFrom().getId());
			for (Layer skipCallLayer : skipCallLayers) {
				if (skipCallLayer.equals(this.getModuleTo())) {
					isNotAllowedToUseSucces = moduleCheckerHelper
							.checkRuleTypeAlreadySet(
									"IsNotAllowedToMakeSkipCall", this.getModuleFrom());
				}
			}
			for (Layer backCallLayer : backCallLayers) {
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
