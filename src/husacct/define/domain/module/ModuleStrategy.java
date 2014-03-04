package husacct.define.domain.module;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.services.DefaultRuleDomainService;
import husacct.define.domain.services.WarningMessageService;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public abstract class ModuleStrategy implements Comparable<ModuleStrategy> {

	protected static long STATIC_ID = 1;
	public static void setStaticId(long highestId){
		STATIC_ID = highestId++;
		STATIC_ID++;
	}

	
	protected long id;
	protected String name;
	protected String description;
	protected String type;
	protected ArrayList<SoftwareUnitDefinition> mappedSUunits;
	protected ArrayList<SoftwareUnitRegExDefinition> mappedRegExSUunits;
	protected ArrayList<ModuleStrategy> subModules;
	protected ModuleStrategy parent;
	// fromStorage == true, if the object is restored from persistency; false, if new
	protected boolean fromStorage = false; 

	public void set (String name, String description, boolean stored){
		this.fromStorage = stored;
		set(name, description);
	}
	
	public void set(String name, String description){
		this.id = STATIC_ID++;
		STATIC_ID++;
		this.name = name;
		this.description = description;
		this.mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
		this.mappedRegExSUunits = new ArrayList<SoftwareUnitRegExDefinition>();
		this.subModules = new ArrayList<ModuleStrategy>();
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

	public boolean getFromStorage() {
		return fromStorage;
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

	public ArrayList<ModuleStrategy> getSubModules() {
		return subModules;
	}

	public void setSubModules(ArrayList<ModuleStrategy> subModules) {
		for (ModuleStrategy module : subModules) {
			module.parent=this;
		}
		this.subModules = subModules;
	}

	public void addSUDefinition(SoftwareUnitDefinition unit){
		if(!mappedSUunits.contains(unit) && !this.hasSoftwareUnitDirectly(unit.getName())) {
			mappedSUunits.add(unit);
		}else{
			System.out.println("This software unit has already been added!");
		}
	}

	public void removeSUDefintion(SoftwareUnitDefinition unit){
		if(mappedSUunits.contains(unit) && this.hasSoftwareUnitDirectly(unit.getName())) {
			mappedSUunits.remove(unit);
		}else{
			System.out.println("This software unit does not exist!");
		}
	}
	
	public void removeAllSUDefintions(){
		mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
	}

	public void addSURegExDefinition(SoftwareUnitRegExDefinition unit){
		if(!mappedRegExSUunits.contains(unit)) {
			mappedRegExSUunits.add(unit);
		}else{
			System.out.println("This regex software unit has already been added!");
		}
	}

	public void removeSURegExDefinition(SoftwareUnitRegExDefinition unit){
		System.out.println(unit.getName());
		if(mappedRegExSUunits.contains(unit)) {
			mappedRegExSUunits.remove(unit);
		}else{
			System.out.println("This regex software unit does not exist!");
		}
	}

	public String addSubModule(ModuleStrategy subModule){
		if(!subModules.contains(subModule) && !hasSubModule(subModule.getName())) {
			subModule.parent=this;
			subModules.add(subModule);
			if (!subModule.fromStorage){
				new DefaultRuleDomainService().addDefaultRules(subModule);
			}
			return "";
		}else{
			return ServiceProvider.getInstance().getLocaleService().getTranslatedString("SameNameModule");
		}

	}

	public void addSubModule(int index,ModuleStrategy subModule){
		if(!subModules.contains(subModule) && !this.hasSubModule(subModule.getName())) {
			subModule.parent=this;
			subModules.add(index,subModule);		
		}else{
			System.out.println("This sub module has already been added!");
		}
	}

	public void removeSubModule(ModuleStrategy subModule){
		if(subModules.contains(subModule) && this.hasSubModule(subModule.getName())) {
			subModules.remove(subModule);
		}else{
			System.out.println("This sub module does not exist!");
		}
	}

	public boolean hasSubModules(){
		boolean returnValue;
		if (subModules.isEmpty()){
			returnValue = false;
		}
		else{
			returnValue = true;
		}
		return returnValue;	
	}

	public boolean hasSubModule(String name){
		boolean hasSubModule = false;

		for(ModuleStrategy subModule : subModules){
			if(subModule.getName().equals(name)){
				hasSubModule = true;
			}else if(!(subModule instanceof Layer) && subModule.hasSubModule(name)){
				hasSubModule = true;
			}
		}
		return hasSubModule;
	}

	public boolean hasSubModule(long id){
		boolean hasSubModule = false;
		for(ModuleStrategy subModule : subModules){
			if(subModule.getId() == id || subModule.hasSubModule(id)){
				hasSubModule = true;
			}
		}
		return hasSubModule;
	}

	public boolean hasSoftwareUnitDirectly(String softwareUnitName){
		return hasSoftwareUnit(softwareUnitName, true);
	}

	public boolean hasSoftwareUnit(String softwareUnitName){
		return hasSoftwareUnit(softwareUnitName, false);
	}

	public boolean hasRegExSoftwareUnitDirectly(String softwareUnitName){
		return hasRegExSoftwareUnit(softwareUnitName, true);
	}

	public boolean hasRegExSoftwareUnit(String softwareUnitName){
		return hasRegExSoftwareUnit(softwareUnitName, false);
	}
	
	// Gives reliable results only if there are no subsubmodules, subsubsubmodles ...!!!
	public int countSoftwareUnits(){
		int counter = 0;
		for (int i = 0; i < mappedSUunits.size(); i++){
			counter++;
		}
		if(this.hasSubModules()){
			for(ModuleStrategy sub : this.subModules){
				counter+= sub.countSoftwareUnits();
			}
		}
		return counter;
	}
	
	public HashMap<String, String> getSoftwareUnitNames(){
		HashMap<String, String> names = new HashMap<String, String>();
		for(SoftwareUnitDefinition softwareUnit : mappedSUunits){
			names.put(softwareUnit.getName(), softwareUnit.getType().toString());
		}
		return names;
	}

	public SoftwareUnitDefinition getSoftwareUnitByName(String softwareUnitName){
		SoftwareUnitDefinition softwareUnit = null;
		for (SoftwareUnitDefinition unit : mappedSUunits){
			if (unit.getName().equals(softwareUnitName)){
				softwareUnit = unit;
			}
		}
		for (ModuleStrategy mod : subModules){
			if (mod.hasSoftwareUnit(softwareUnitName)){
				softwareUnit = mod.getSoftwareUnitByName(softwareUnitName);
			}
		}
		if (softwareUnit == null){ 
			throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoSoftwareUnit"));
			}
		else{
			return softwareUnit;
		}
	}

	public SoftwareUnitRegExDefinition getRegExSoftwareUnitByName(String softwareUnitName){
		SoftwareUnitRegExDefinition softwareUnit = null;
		for (SoftwareUnitRegExDefinition unit : mappedRegExSUunits){
			if (unit.getName().equals(softwareUnitName)){
				softwareUnit = unit;
			}
		}
		for (ModuleStrategy mod : subModules){
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
		if (obj instanceof ModuleStrategy){
			ModuleStrategy m = (ModuleStrategy)obj;
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
		return isMapped;
	}

	public int compareTo(ModuleStrategy compareObject) {
		int compareResult = 0;
		if(compareObject instanceof Layer || this.getId() < compareObject.getId()) {
			compareResult = -1;
		} else if(this.getId() > compareObject.getId()) {
			compareResult = 1;
		}
		return compareResult;
	}

	public ModuleStrategy getparent() {
		return parent;
	}

	private boolean hasSoftwareUnit(String softwareUnitName, boolean directly){
		boolean hasSoftwareUnit = false;
		for (SoftwareUnitDefinition unit : mappedSUunits){
			if (unit.getName().equals(softwareUnitName)){
				hasSoftwareUnit = true;
			}
		}
		if (!directly){
			for (ModuleStrategy mod : subModules){
				if (mod.hasSoftwareUnit(softwareUnitName, directly)){
					hasSoftwareUnit = true;
				}
			}
		}
		return hasSoftwareUnit;
	}

	private boolean hasRegExSoftwareUnit(String softwareUnitName, boolean directly){
		boolean hasSoftwareUnit = false;
		for (SoftwareUnitRegExDefinition unit : mappedRegExSUunits){
			if (unit.getName().equals(softwareUnitName)){
				hasSoftwareUnit = true;
			}
		}
		if (!directly) {
			for (ModuleStrategy mod : subModules){
				if (mod.hasRegExSoftwareUnit(softwareUnitName, directly)){
					hasSoftwareUnit = true;
				}
			}
		}
		return hasSoftwareUnit;
	}
	
	public void copyValuestoNewCompont(ModuleStrategy newModule)
	{
		newModule.setId(this.getId());
		newModule.setName(this.getName());
		newModule.setDescription(this.getDescription());
		newModule.parent=this.getparent();
		newModule.setSubModules(this.getSubModules());
		newModule.setRegExUnits(this.getRegExUnits());
		newModule.setUnits(this.getUnits());
		newModule.fromStorage=(this.getFromStorage());
	}

	public void setParent(ModuleStrategy moduleParent) {
		this.parent=moduleParent;
	}


	public void addSUDefinition(List<SoftwareUnitDefinition> units) {
		for (SoftwareUnitDefinition softwareUnitDefinition : units) {
			addSUDefinition(softwareUnitDefinition);
		}
		
	}


	public void removeSUDefintion(List<SoftwareUnitDefinition> units) {
		for (SoftwareUnitDefinition softwareUnitDefinition : units) {
			removeSUDefintion(softwareUnitDefinition);
		}
		
	}


	public ArrayList<SoftwareUnitDefinition> getAndRemoveSoftwareUnits(
			List<String> names) {
		ArrayList<SoftwareUnitDefinition> units = new ArrayList<SoftwareUnitDefinition>();
		Iterator<SoftwareUnitDefinition> loop = getUnits().iterator();
		while(loop.hasNext())
		{
			for (String uniqName : names) {
				SoftwareUnitDefinition buffer = loop.next();
				if (uniqName.toLowerCase().equals(buffer.getName().toLowerCase())){
					units.add(buffer);
					loop.remove();
				}
			}
		}
		
	
	
		return units;
	}



	

}
