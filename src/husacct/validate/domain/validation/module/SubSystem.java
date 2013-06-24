package husacct.validate.domain.validation.module;

import java.util.List;
import husacct.validate.domain.validation.ruletype.RuleType;
import husacct.validate.domain.validation.ruletype.RuleTypes;

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
    	List<RuleType> allowedRuleTypes = new ArrayList<RuleType>();

		for (RuleType ruleType : ruleTypes) {
		    if (!ruleType.equals(RuleTypes.IS_NOT_ALLOWED_BACK_CALL)
			    && !ruleType.equals(RuleTypes.IS_NOT_ALLOWED_SKIP_CALL)
			    && !ruleType.equals(RuleTypes.FACADE_CONVENTION)) {
			allowedRuleTypes.add(ruleType);
		    }
		}
		return allowedRuleTypes;
    }
}