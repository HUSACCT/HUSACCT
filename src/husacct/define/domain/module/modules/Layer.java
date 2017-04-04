package husacct.define.domain.module.modules;


import husacct.common.enums.ModuleTypes;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

import java.util.ArrayList;


public class Layer extends ModuleStrategy {
	
	private static int STATIC_LEVEL = 1;
	private int hierarchicalLevel;
	
	@Override
	public void set(String name, String description){
		this.id = STATIC_ID;
		STATIC_ID++;
		this.name = name;
		this.description = description;
		this.mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
		this.subModules = new ArrayList<ModuleStrategy>();
		
		this.type = ModuleTypes.LAYER.toString();
		setNewHierarchicalLevel();
	}
	
	public void setNewHierarchicalLevel() {
		this.hierarchicalLevel = STATIC_LEVEL;
		STATIC_LEVEL++;
	}
	
	public void setHierarchicalLevel(int hierarchicalLevel) {
		this.hierarchicalLevel = hierarchicalLevel;
		if(hierarchicalLevel >= STATIC_LEVEL){
			STATIC_LEVEL = hierarchicalLevel++;
		}
	}

	public int getHierarchicalLevel() {
		return hierarchicalLevel;
	}

	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (obj instanceof Layer){
	    	Layer l = (Layer)obj;
	    	if (l.id != this.id){
	    		return false;
	    	}
	    	return true;
	    }
	    return false;
	}
	
		
	@Override
	public int compareTo(ModuleStrategy compareModule) {
		int compareResult = 0;
		if(compareModule instanceof Layer || this.getId() < compareModule.getId()) {
			Layer compareLayer = (Layer) compareModule;
			if(this.getHierarchicalLevel() > compareLayer.getHierarchicalLevel()) {
				compareResult = 1;
			} else if(this.getHierarchicalLevel() < compareLayer.getHierarchicalLevel()) {
				compareResult = -1;
			}
		} else {
			compareResult = -1;
		}
		return compareResult;
	}
}
