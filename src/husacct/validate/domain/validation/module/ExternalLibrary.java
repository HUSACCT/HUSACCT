package husacct.validate.domain.validation.module;

import java.util.List;

import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;
import java.util.ArrayList;

public class ExternalLibrary extends AbstractModule {

	public ExternalLibrary(List<RuleType> ruleTypes) {
		super(ruleTypes);
	}

	@Override
	public List<RuleType> initDefaultModuleRuleTypes() {
		return new ArrayList<RuleType>();
	}

	@Override
	public List<RuleType> initAllowedModuleRuleTypes() {
	    List<RuleType> allowedRules = new ArrayList<RuleType>();

	    for (RuleType ruleType : ruleTypes) {
		if (ruleType.equals(RuleTypes.IS_NOT_ALLOWED) ||
			ruleType.equals(RuleTypes.IS_ONLY_ALLOWED) ||
			ruleType.equals(RuleTypes.IS_ONLY_MODULE_ALLOWED) ||
			ruleType.equals(RuleTypes.MUST_USE)) {
		    allowedRules.add(ruleType);
		}
	    }
	    return allowedRules;
	}
}
