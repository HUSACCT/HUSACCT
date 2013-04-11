package husacct.validate.domain.validation.module;

import java.util.List;

import husacct.validate.domain.validation.ruletype.RuleType;

public class ExternalLibrary extends AbstractModule {

	public ExternalLibrary(List<RuleType> ruleTypes) {
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
