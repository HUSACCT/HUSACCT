package husacct.define.domain.module.modules;

import java.util.ArrayList;

import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

public class Root extends ModuleStrategy {
	@Override
	public void set(String name, String description){
		this.id=0;
		this.name = name;
		this.description = description;
		this.type ="Root";
		this.mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
		this.subModules = new ArrayList<ModuleStrategy>();
	}
}
