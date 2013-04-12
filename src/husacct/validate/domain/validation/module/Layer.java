package husacct.validate.domain.validation.module;

import java.util.List;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;
import java.util.ArrayList;

public class Layer extends AbstractModule {

    public Layer(List<RuleType> ruleTypes) {
	super(ruleTypes);
    }

    @Override
    public List<RuleType> initDefaultModuleRuleTypes() {
	List<RuleType> moduleRuleTypes = new ArrayList<RuleType>();

	for (RuleType ruleType : ruleTypes) {
	    if (ruleType.equals(RuleTypes.IS_NOT_ALLOWED_BACK_CALL) ||
		    ruleType.equals(RuleTypes.IS_NOT_ALLOWED_SKIP_CALL)) {
		moduleRuleTypes.add(ruleType);
	    }
	}
	return moduleRuleTypes;
    }

    @Override
    public List<RuleType> initAllowedModuleRuleTypes() {
	return ruleTypes;
    }
}
