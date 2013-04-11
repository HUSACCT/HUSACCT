package husacct.validate.domain.validation.module;

import java.util.List;
import husacct.validate.domain.validation.ruletype.RuleType;

public interface IModule {
	abstract List<RuleType> initDefaultModuleRuleTypes();
	abstract List<RuleType> initAllowedModuleRuleTypes();

	public List<RuleType> getDefaultModuleruleTypes();

	public List<RuleType> getAllowedModuleruleTypes();
}
