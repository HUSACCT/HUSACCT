package husacct.validate.domain.validation.module;

import java.util.List;
import husacct.validate.domain.validation.ruletype.RuleType;

public class Layer extends AbstractModule {

	public Layer(List<RuleType> ruleTypes) {
		super(ruleTypes);
	}

	@Override
	public List<RuleType> initDefaultModuleRuleTypes() {
		return null;
	}

	@Override
	public List<RuleType> initAllowedModuleRuleTypes() {
		return null;
	}
}
