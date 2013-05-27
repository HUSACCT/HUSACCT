package husacct.validate.domain.validation.module;

import java.util.List;

import husacct.validate.domain.exception.ModuleNotFoundException;
import husacct.validate.domain.validation.ruletype.RuleType;

public class ModuleFactory {
	public AbstractModule createModule(String type, List<RuleType> rules) throws ModuleNotFoundException {
		switch (type) {
			case "Component":
				return new Component(rules);
			case "ExternalLibrary":
				return new ExternalLibrary(rules);
			case "Layer":
				return new Layer(rules);
			case "SubSystem":
				return new SubSystem(rules);
			case "Facade":
				return new Facade(rules);
			default:
				throw new ModuleNotFoundException(type);
		}
	}
}