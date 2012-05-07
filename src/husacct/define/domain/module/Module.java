package husacct.define.domain.module;

import husacct.define.domain.SoftwareUnitDefinition;

import java.util.ArrayList;

public class Module implements Comparable<Module> {
	
	protected static long STATIC_ID;
	protected long id;
	protected String name;
	protected String description;
	protected String type;
	protected ArrayList<SoftwareUnitDefinition> mappedSUunits;
	protected ArrayList<Module> subModules;
	
	public Module()
	{
		this("", "");
	}

	public Module(String name, String description)
	{	
		this.id = STATIC_ID++;
		STATIC_ID++;
		this.name = name;
		this.description = description;
		this.type = "Module";
		this.mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
		this.subModules = new ArrayList<Module>();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ArrayList<SoftwareUnitDefinition> getUnits() {
		return mappedSUunits;
	}
	
	public String[] getPhysicalPaths(){
		ArrayList<String> pathsList = new ArrayList<String>();
		for (SoftwareUnitDefinition unit : mappedSUunits){
			pathsList.add(unit.getName());
		}
		String[] paths = new String[pathsList.size()];
		pathsList.toArray(paths);
		return paths;
	}

	public void setUnits(ArrayList<SoftwareUnitDefinition> units) {
		this.mappedSUunits = units;
	}

	public ArrayList<Module> getSubModules() {
		return subModules;
	}

	public void setSubModules(ArrayList<Module> subModules) {
		this.subModules = subModules;
	}
	
	//SoftwareUnitDefinition
	public void addSUDefinition(SoftwareUnitDefinition unit)
	{
		if(!mappedSUunits.contains(unit) && !this.hasSUDefinition(unit.getName())) {
			mappedSUunits.add(unit);
		}else{
			System.out.println("This software unit has already been added!");
		}
	}
	
	public void removeSUDefintion(SoftwareUnitDefinition unit)
	{
		if(mappedSUunits.contains(unit) && this.hasSUDefinition(unit.getName())) {
			mappedSUunits.remove(unit);
		}else{
			System.out.println("This software unit does not exist!");
		}
	}
	
	private boolean hasSUDefinition(String name) 
	{
		for(SoftwareUnitDefinition unit : mappedSUunits) 
		{
			if(unit.getName().equals(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	//Module
	public void addSubModule(Module subModule)
	{
		if(!subModules.contains(subModule) && !this.hasSubModule(subModule.getName())) {
			subModules.add(subModule);
		}else{
			System.out.println("This sub module has already been added!");
		}
	}
	
	public void removeSubModule(Module subModule)
	{
		if(subModules.contains(subModule) && this.hasSubModule(subModule.getName())) {
			subModules.remove(subModule);
		}else{
			System.out.println("This sub module does not exist!");
		}
	}
	
	public boolean hasSubModule(String name) 
	{
		boolean hasSubModule = false;
		for(Module subModule : subModules) 
		{
			if(subModule.getName().equals(name) || subModule.hasSubModule(name))
			{
				hasSubModule = true;
			}
		}
		return hasSubModule;
	}
	
	public boolean hasSubModule(long id) 
	{
		boolean hasSubModule = false;
		for(Module subModule : subModules) 
		{
			if(subModule.getId() == id || subModule.hasSubModule(id))
			{
				hasSubModule = true;
			}
		}
		return hasSubModule;
	}
	
	public boolean hasSoftwareUnit(String softwareUnitName) 
	{
		boolean hasSoftwareUnit = false;
		for (SoftwareUnitDefinition unit : mappedSUunits){
			if (unit.getName().equals(softwareUnitName)){
				hasSoftwareUnit = true;
			}
		}
		for (Module mod : subModules){
			if (mod.hasSoftwareUnit(softwareUnitName)){
				hasSoftwareUnit = true;
			}
		}
		return hasSoftwareUnit;
	}
	
	public SoftwareUnitDefinition getSoftwareUnitByName(String softwareUnitName){
		SoftwareUnitDefinition softwareUnit = null;
		for (SoftwareUnitDefinition unit : mappedSUunits){
			if (unit.getName().equals(softwareUnitName)){
				softwareUnit = unit;
			}
		}
		for (Module mod : subModules){
			if (mod.hasSoftwareUnit(softwareUnitName)){
				softwareUnit = getSoftwareUnitByName(softwareUnitName);
			}
		}
		if (softwareUnit == null){ throw new RuntimeException("This Software Unit does not exist!");}
		return softwareUnit;
	}
	
	@Override
	public boolean equals(Object obj) {
	    if (this == obj)
	        return true;
	    if (obj == null)
	        return false;
	    if (obj instanceof Module){
	    	Module m = (Module)obj;
	    	if (!m.name.equals(this.name)){
	    		return false;
	    	}
	    	return true;
	    }
	    return false;
	}

	public long getId() {
		return id;
	}

	public boolean isMapped() {
		boolean isMapped = false;
		if (mappedSUunits.size() > 0){
			isMapped = true;
		}
		for (Module mod : subModules){
			if (mod.isMapped()){
				isMapped = true;
			}
		}
		return isMapped;
	}

	@Override
	public int compareTo(Module compareObject) {
		int compareResult = 0;
		if(compareObject instanceof Layer || this.getId() < compareObject.getId()) {
			compareResult = -1;
		} else if(this.getId() > compareObject.getId()) {
			compareResult = 1;
		}
		return compareResult;
	}

}
