package husacct.validate.domain.validation.module;

import java.util.ArrayList;
import java.util.List;

import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

public class Component extends AbstractModule {

	public Component(List<RuleType> ruleTypes) {
		super(ruleTypes);
	}

	@Override
	public List<RuleType> initDefaultModuleRuleTypes() {
		List<RuleType> defaultModuleRuleTypes = new ArrayList<RuleType>();

		for (RuleType ruleType : ruleTypes) {
			if (ruleType.equals(RuleTypes.FACADE_CONVENTION)) {
				defaultModuleRuleTypes.add(ruleType);
			}
		}
		return defaultModuleRuleTypes;
	}

	@Override
	public List<RuleType> initAllowedModuleRuleTypes() {
		List<RuleType> allowedRules = ruleTypes;

		for (RuleType ruleType : ruleTypes) {
			if (ruleType.equals(RuleTypes.IS_NOT_ALLOWED_BACK_CALL) ||
					ruleType.equals(RuleTypes.IS_NOT_ALLOWED_SKIP_CALL)) {
				allowedRules.remove(ruleType);
			}
		}

		return allowedRules;
	}
}