package husacct.define.domain.appliedrule.propertyrules;

import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.task.conventions_checker.ModuleCheckerHelper;

public class VisibilityConventionRule extends AppliedRuleStrategy{
	private ModuleCheckerHelper moduleCheckerHelper;

	public boolean checkConvention() {
		moduleCheckerHelper = new ModuleCheckerHelper();
		boolean conventionSuccess = moduleCheckerHelper
				.checkRuleTypeAlreadySet(this.getRuleType(), this.getModuleFrom());
		return conventionSuccess;
	}

}
