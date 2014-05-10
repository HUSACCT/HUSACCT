package husacct.validate.domain.validation.moduletype;

import java.util.List;
import husacct.validate.domain.validation.ruletype.RuleType;
import java.util.ArrayList;

public class Facade extends AbstractModule {
	
	public Facade(List<RuleType> ruleTypes) {
		super(ruleTypes);
	}

	@Override
	public List<RuleType> initDefaultModuleRuleTypes() {
		List<RuleType> defaultRuleTypes = new ArrayList<RuleType>();
		return defaultRuleTypes;
	}

	@Override
	public List<RuleType> initAllowedModuleRuleTypes() {
		List<RuleType> allowedRuleTypes = new ArrayList<RuleType>();
		return allowedRuleTypes;
	}
}