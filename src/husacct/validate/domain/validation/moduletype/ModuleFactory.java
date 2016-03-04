package husacct.validate.domain.validation.moduletype;

import java.util.HashMap;
import java.util.List;

import husacct.validate.domain.exception.ModuleNotFoundException;
import husacct.validate.domain.validation.ruletype.RuleType;

public class ModuleFactory {
	private HashMap<String, AbstractModule> moduleTypesMap = new HashMap<String, AbstractModule>();
	
	public ModuleFactory(List<RuleType> rules) {
		for (ModuleTypes moduleType : ModuleTypes.values()) {
			moduleTypesMap.put(moduleType.name().toLowerCase().replace("_", ""), createModule(moduleType.toString().toLowerCase().replace("_", ""), rules));
		}
	}
	
	private AbstractModule createModule(String moduleType, List<RuleType> rules) {
		switch (moduleType.toLowerCase()) {
			case "component":
				return new Component(rules);
			case "externallibrary":
				return new ExternalLibrary(rules);
			case "layer":
				return new Layer(rules);
			case "subsystem":
				return new SubSystem(rules);
			case "facade":
				return new Facade(rules);
			default:
				throw new ModuleNotFoundException(moduleType);
		}
	}

	public List<RuleType> getAllowedRuleTypesOfModule(String moduleType) throws ModuleNotFoundException {
		if (moduleType.toLowerCase().equals("root"))
			moduleType = "subsystem";
		moduleType = moduleType.toLowerCase().replace("_", "");
		AbstractModule module = moduleTypesMap.get(moduleType);
		return module.getAllowedRuleTypes();
	}
	
	public List<RuleType> getDefaultRuleTypesOfModule(String moduleType) throws ModuleNotFoundException {
		if (moduleType.toLowerCase().equals("root"))
			moduleType = "subsystem";
		AbstractModule module = moduleTypesMap.get(moduleType.toLowerCase().replace("_", ""));
		return module.getDefaultRuleTypes();
	}
	
	public void setAllowedRuleTypeOfModule(String moduleType, String ruleTypeKey, boolean value) throws ModuleNotFoundException {
		AbstractModule module = moduleTypesMap.get(moduleType.toLowerCase().replace("_", ""));
		module.setAllowedRuleType(ruleTypeKey, value);
	}
	
	public void setDefaultRuleTypeOfModule(String moduleType, String ruleTypeKey, boolean value) throws ModuleNotFoundException {
		AbstractModule module = moduleTypesMap.get(moduleType.toLowerCase().replace("_", ""));
		module.setDefaultRuleType(ruleTypeKey, value);
	}
}