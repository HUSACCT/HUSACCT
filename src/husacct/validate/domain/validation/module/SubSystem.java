package husacct.validate.domain.validation.module;

import java.util.List;
import husacct.validate.domain.validation.ruletype.RuleType;
import java.util.ArrayList;

public class SubSystem extends AbstractModule {
	public SubSystem(List<RuleType> ruleTypes) {
		super(ruleTypes);
	}

	@Override
	public List<RuleType> initDefaultModuleRuleTypes() {
		List<RuleType> defaultRuleTypes = new ArrayList<RuleType>();

		return defaultRuleTypes;
	}

	@Override
	public List<RuleType> initAllowedModuleRuleTypes() {
		List<RuleType> allowedRuleTypes = ruleTypes;

		return allowedRuleTypes;
	}
}