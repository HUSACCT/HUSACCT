package husacct.validate.domain.validation.module;

import java.util.List;

import husacct.validate.domain.exception.ModuleNotFoundException;
import husacct.validate.domain.validation.ruletype.RuleType;

public class ModuleFactory {
	public AbstractModule createModule(ModuleTypes type, List<RuleType> rules) throws ModuleNotFoundException {
		switch (type) {
			case COMPONENT:
				return new Component(rules);
			case EXTERNAL_LIBRARY:
				return new ExternalLibrary(rules);
			case LAYER:
				return new Layer(rules);
			case SUBSYSTEM:
				return new SubSystem(rules);
			case FACADE:
				return new Facade(rules);
			default:
				throw new ModuleNotFoundException(type);
		}
	}
}