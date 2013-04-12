package husacct.validate.domain.validation.module;

import husacct.validate.domain.validation.ruletype.RuleType;

import java.util.List;

public abstract class AbstractModule implements IModule {
	private List<RuleType> defaultModuleRuleTypes;
	private List<RuleType> allowedModuleRuleTypes;
	protected List<RuleType> ruleTypes;

	public abstract List<RuleType> initDefaultModuleRuleTypes();
	public abstract List<RuleType> initAllowedModuleRuleTypes();

	public AbstractModule(List<RuleType> ruleTypes) {
		this.ruleTypes = ruleTypes;
		this.defaultModuleRuleTypes = this.initDefaultModuleRuleTypes();
		this.allowedModuleRuleTypes = this.initAllowedModuleRuleTypes();
	}

	public List<RuleType> getDefaultModuleruleTypes() {
		return this.defaultModuleRuleTypes;
	}

	public List<RuleType> getAllowedModuleruleTypes() {
		return this.allowedModuleRuleTypes;
	}
}
