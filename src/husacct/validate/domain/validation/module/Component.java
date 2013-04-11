package husacct.validate.domain.validation.module;

import java.util.ArrayList;
import java.util.List;

import husacct.validate.domain.validation.ruletype.RuleType;

public class Component extends AbstractModule {

	public Component(List<RuleType> ruleTypes) {
		super(ruleTypes);
	}

	@Override
	public List<RuleType> initDefaultModuleRuleTypes() {
		List<RuleType> ModuleRuleTypes = new ArrayList<RuleType>();
		
		for (RuleType ruleType : ruleTypes) {
			if (ruleType.getKey().equals("FacadeConvention")) {
				ModuleRuleTypes.add(ruleType);
			}
		}
		return ModuleRuleTypes;
	}

	@Override
	public List<RuleType> initAllowedModuleRuleTypes() {
		return null;
	}
}
