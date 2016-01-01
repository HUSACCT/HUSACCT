package husacct.define.domain.module.modules;


import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitRegExDefinition;
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
		
	}
	
	public void copyValuestoNewModule(ModuleStrategy newModule){ 
		newModule.setId(this.getId());
		newModule.setName(this.getName());
		newModule.setDescription(this.getDescription());
		newModule.setParent(this.getparent());
		ModuleStrategy facade = subModules.get(0);
	
		//can be bettter implemented :TODO
		SoftwareArchitecture.getInstance().removeModule(facade);
		newModule.setSubModules(this.getSubModules());
		newModule.setRegExUnits(this.getRegExUnits());
		newModule.setUnits(this.getUnits());		
	}
	
}
