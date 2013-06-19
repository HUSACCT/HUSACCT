package husacct.define.domain.module.modules;


import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

import java.util.ArrayList;

public class Component extends ModuleStrategy {

	public void set(String name, String description){
		this.id = STATIC_ID;
		STATIC_ID++;
		this.name = name;
		this.description = description;
		this.type = "Component";
		this.mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
		this.mappedRegExSUunits = new ArrayList<SoftwareUnitRegExDefinition>();
		this.subModules = new ArrayList<ModuleStrategy>();
	    ModuleStrategy facade = new ModuleFactory().createModule("Facade");
	    facade.set("Facade<"+name+">","this is the Facade of your Component");
	    facade.setParent(this);
	    this.subModules.add(facade);
	}
	
	public void copyValuestoNewCompont(ModuleStrategy newModule){ 
		newModule.setId(this.getId());
		newModule.setName(this.getName());
		newModule.setDescription(this.getDescription());
		newModule.setParent(this.getparent());
		this.subModules.remove(0);
		newModule.setSubModules(this.getSubModules());
		newModule.setRegExUnits(this.getRegExUnits());
		newModule.setUnits(this.getUnits());		
	}
	
}
