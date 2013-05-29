package husacct.define.domain.module.ToBeImplemented.modules;

import java.util.ArrayList;

import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.module.ToBeImplemented.ModuleStrategy;

public class Root extends ModuleStrategy {

	
	
	public void set(String name, String description){
		this.id=0;
		this.name = name;
		this.description = description;
		this.type ="Root";
		this.mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
		this.mappedRegExSUunits = new ArrayList<SoftwareUnitRegExDefinition>();
		this.subModules = new ArrayList<ModuleStrategy>();
	}
}
