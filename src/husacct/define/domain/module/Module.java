package husacct.define.domain.module;

import husacct.ServiceProvider;
import husacct.analyse.infrastructure.antlr.csharp.CSharpParser.property_declaration2_return;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.services.DefaultRuleDomainService;

import java.util.ArrayList;

public class Module implements Comparable<Module> {
	
	protected static long STATIC_ID = 1;
	protected long id;
	protected String name;
	protected String description;
	protected String type;
	protected ArrayList<SoftwareUnitDefinition> mappedSUunits;
	protected ArrayList<SoftwareUnitRegExDefinition> mappedRegExSUunits;
	protected ArrayList<Module> subModules;
	protected Module parent;
	public Module()
	{
		this("", "");
	}

	public Module(String name, String description)
	{	
		this.id = STATIC_ID;
		STATIC_ID++;
		this.name = name;
		this.description = description;
		this.type = "Module";
		this.mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
		this.mappedRegExSUunits = new ArrayList<SoftwareUnitRegExDefinition>();
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

	public void setUnits(ArrayList<SoftwareUnitDefinition> units) {
		this.mappedSUunits = units;
	}
	
	public ArrayList<SoftwareUnitRegExDefinition> getRegExUnits() {
		return mappedRegExSUunits;
	}

	public void setRegExUnits(ArrayList<SoftwareUnitRegExDefinition> units) {
		this.mappedRegExSUunits = units;
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
		if(!mappedSUunits.contains(unit) && !this.hasSoftwareUnitDirectly(unit.getName())) {
			mappedSUunits.add(unit);
		}else{
			System.out.println("This software unit has already been added!");
		}
	}
	
	public void removeSUDefintion(SoftwareUnitDefinition unit)
	{
		if(mappedSUunits.contains(unit) && this.hasSoftwareUnitDirectly(unit.getName())) {
			mappedSUunits.remove(unit);
		}else{
			System.out.println("This software unit does not exist!");
		}
	}
	
	//SoftwareUnitDefinition
	public void addSURegExDefinition(SoftwareUnitRegExDefinition unit)
	{
		if(!mappedRegExSUunits.contains(unit)) {
			mappedRegExSUunits.add(unit);
		}else{
			System.out.println("This regex software unit has already been added!");
		}
	}
	
	public void removeSURegExDefinition(SoftwareUnitRegExDefinition unit)
	{
		System.out.println(unit.getName());
		if(mappedRegExSUunits.contains(unit)) {
			mappedRegExSUunits.remove(unit);
		}else{
			System.out.println("This regex software unit does not exist!");
		}
	}
	
	//Module
	public void addSubModule(Module subModule)
	{
		if(!subModules.contains(subModule) && !this.hasSubModule(subModule.getName())) {
			subModule.parent=this;
			subModules.add(subModule);
<<<<<<< HEAD
			DefaultRuleDomainService service = new DefaultRuleDomainService();
			service.addDefaultRules(subModule);  //Correct way?
		}
		else {
=======
			DefaultRuleDomainService.getInstance().setDefaultRule(subModule);
		}else{
>>>>>>> develop
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
	
	public boolean hasSubModules()
	{
		return subModules.isEmpty();	
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
	
	public boolean hasSoftwareUnitDirectly(String softwareUnitName) 
	{
		return hasSoftwareUnit(softwareUnitName, true);
	}
	
	public boolean hasSoftwareUnit(String softwareUnitName) 
	{
		return hasSoftwareUnit(softwareUnitName, false);
	}
	
	private boolean hasSoftwareUnit(String softwareUnitName, boolean directly) 
	{
		boolean hasSoftwareUnit = false;
		for (SoftwareUnitDefinition unit : mappedSUunits){
			if (unit.getName().equals(softwareUnitName)){
				hasSoftwareUnit = true;
			}
		}
		if (!directly) {
			for (Module mod : subModules){
				if (mod.hasSoftwareUnit(softwareUnitName, directly)){
					hasSoftwareUnit = true;
				}
			}
		}
		return hasSoftwareUnit;
	}
	
	private boolean hasRegExSoftwareUnit(String softwareUnitName, boolean directly) 
	{
		boolean hasSoftwareUnit = false;
		for (SoftwareUnitRegExDefinition unit : mappedRegExSUunits){
			if (unit.getName().equals(softwareUnitName)){
				hasSoftwareUnit = true;
			}
		}
		if (!directly) {
			for (Module mod : subModules){
				if (mod.hasRegExSoftwareUnit(softwareUnitName, directly)){
					hasSoftwareUnit = true;
				}
			}
		}
		return hasSoftwareUnit;
	}
	
	public boolean hasRegExSoftwareUnitDirectly(String softwareUnitName) 
	{
		return hasRegExSoftwareUnit(softwareUnitName, true);
	}
	
	public boolean hasRegExSoftwareUnit(String softwareUnitName) 
	{
		return hasRegExSoftwareUnit(softwareUnitName, false);
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
				softwareUnit = mod.getSoftwareUnitByName(softwareUnitName);
			}
		}
		if (softwareUnit == null){ throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoSoftwareUnit"));}
		return softwareUnit;
	}
	
	public SoftwareUnitRegExDefinition getRegExSoftwareUnitByName(String softwareUnitName){
		SoftwareUnitRegExDefinition softwareUnit = null;
		for (SoftwareUnitRegExDefinition unit : mappedRegExSUunits){
			if (unit.getName().equals(softwareUnitName)){
				softwareUnit = unit;
			}
		}
		for (Module mod : subModules){
			if (mod.hasRegExSoftwareUnit(softwareUnitName)){
				softwareUnit = mod.getRegExSoftwareUnitByName(softwareUnitName);
			}
		}
		if (softwareUnit == null){ throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoSoftwareUnit"));}
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
	    	if (m.id != this.id){
	    		return false;
	    	}
	    	return true;
	    }
	    return false;
	}

	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
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

	public Module getparent() {
		
		return parent;
	}

}
