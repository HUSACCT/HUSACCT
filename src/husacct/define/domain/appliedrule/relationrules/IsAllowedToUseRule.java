package husacct.define.domain.appliedrule.relationrules;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.task.conventions_checker.ModuleCheckerHelper;

public class IsAllowedToUseRule extends AppliedRuleStrategy{
	private ModuleCheckerHelper moduleCheckerHelper = new ModuleCheckerHelper();

	public boolean checkConvention() {
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
			return conventionSuccess;
	}

}
