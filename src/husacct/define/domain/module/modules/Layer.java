package husacct.define.domain.module.modules;

import java.util.ArrayList;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.module.ModuleStrategy;


public class Layer extends ModuleStrategy {
	
	private static int STATIC_LEVEL = 1;
	private int hierarchicalLevel;
	
	public void set(String name, String description){
		this.id = STATIC_ID;
		STATIC_ID++;
		this.hierarchicalLevel = STATIC_LEVEL;
		STATIC_LEVEL++;
		this.name = name;
		this.description = description;
		
		this.mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
		this.mappedRegExSUunits = new ArrayList<SoftwareUnitRegExDefinition>();
		this.subModules = new ArrayList<ModuleStrategy>();
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
