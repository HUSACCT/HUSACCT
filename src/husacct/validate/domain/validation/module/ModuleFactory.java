package husacct.validate.domain.validation.module;

import java.util.List;

import husacct.validate.domain.exception.ModuleNotFoundException;
import husacct.validate.domain.validation.ruletype.RuleType;

public class ModuleFactory {
	public AbstractModule createModule(String type, List<RuleType> rules) throws ModuleNotFoundException {
		
		switch(type.toLowerCase()) {
			case "component": 
				return new Component(rules);
			case "externallibrary": 
				return new ExternalLibrary(rules);
			case "layer": 	
				return new Layer(rules);
			case "subsystem": 
				return new SubSystem(rules);
			default:
				throw new ModuleNotFoundException(type);
		}
	}
}