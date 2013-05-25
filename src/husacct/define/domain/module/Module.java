package husacct.define.domain.module;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitRegExDefinition;
import husacct.define.domain.services.DefaultRuleDomainService;
import husacct.define.domain.services.WarningMessageService;

import java.util.ArrayList;

public class Module implements Comparable<Module> {

    protected static long STATIC_ID = 1;
    protected String description;
    protected long id;
    protected ArrayList<SoftwareUnitRegExDefinition> mappedRegExSUunits;
    protected ArrayList<SoftwareUnitDefinition> mappedSUunits;
    protected String name;
    protected Module parent;
    protected ArrayList<Module> subModules;
    protected String type;

    public Module() {
	this("", "");
    }

    public Module(String name, String description) {
	id = STATIC_ID;
	STATIC_ID++;
	this.name = name;
	this.description = description;
	type = "Module";
	mappedSUunits = new ArrayList<SoftwareUnitDefinition>();
	mappedRegExSUunits = new ArrayList<SoftwareUnitRegExDefinition>();
	subModules = new ArrayList<Module>();
    }

    public void addSubModule(int index, Module subModule) {
	if (!subModules.contains(subModule)
		&& !this.hasSubModule(subModule.getName())) {
	    subModule.parent = this;
	    subModules.add(index, subModule);
	} else {
	    System.out.println("This sub module has already been added!");
	}
    }

    public String addSubModule(Module subModule) {
	if (!subModules.contains(subModule)
		&& !moduleAlreadyExistentWithinSystem(subModule.getName())) {
	    subModule.parent = this;
	    subModules.add(subModule);
	    DefaultRuleDomainService service = new DefaultRuleDomainService();
	    service.addDefaultRules(subModule);
	    WarningMessageService.getInstance().processModule(subModule);
	    return "";
	} else {
	    return ServiceProvider.getInstance().getLocaleService()
		    .getTranslatedString("SameNameModule");
	}

    }

    public void addSUDefinition(SoftwareUnitDefinition unit) {
	if (!mappedSUunits.contains(unit)
		&& !hasSoftwareUnitDirectly(unit.getName())) {
	    mappedSUunits.add(unit);
	} else {
	    System.out.println("This software unit has already been added!");
	}
    }

    public void addSURegExDefinition(SoftwareUnitRegExDefinition unit) {
	if (!mappedRegExSUunits.contains(unit)) {
	    mappedRegExSUunits.add(unit);
	} else {
	    System.out
		    .println("This regex software unit has already been added!");
	}
    }

    @Override
    public int compareTo(Module compareObject) {
	int compareResult = 0;
	if (compareObject instanceof Layer || getId() < compareObject.getId()) {
	    compareResult = -1;
	} else if (getId() > compareObject.getId()) {
	    compareResult = 1;
	}
	return compareResult;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (obj instanceof Module) {
	    Module m = (Module) obj;
	    if (m.id != id) {
		return false;
	    }
	    return true;
	}
	return false;
    }

    public String getDescription() {
	return description;
    }

    public long getId() {
	return id;
    }

    public String getName() {
	return name;
    }

    public Module getparent() {
	return parent;
    }

    public SoftwareUnitRegExDefinition getRegExSoftwareUnitByName(
	    String softwareUnitName) {
	SoftwareUnitRegExDefinition softwareUnit = null;
	for (SoftwareUnitRegExDefinition unit : mappedRegExSUunits) {
	    if (unit.getName().equals(softwareUnitName)) {
		softwareUnit = unit;
	    }
	}
	for (Module mod : subModules) {
	    if (mod.hasRegExSoftwareUnit(softwareUnitName)) {
		softwareUnit = mod.getRegExSoftwareUnitByName(softwareUnitName);
	    }
	}
	if (softwareUnit == null) {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService().getTranslatedString("NoSoftwareUnit"));
	}
	return softwareUnit;
    }

    public ArrayList<SoftwareUnitRegExDefinition> getRegExUnits() {
	return mappedRegExSUunits;
    }

    public SoftwareUnitDefinition getSoftwareUnitByName(String softwareUnitName) {
	SoftwareUnitDefinition softwareUnit = null;
	for (SoftwareUnitDefinition unit : mappedSUunits) {
	    if (unit.getName().equals(softwareUnitName)) {
		softwareUnit = unit;
	    }
	}
	for (Module mod : subModules) {
	    if (mod.hasSoftwareUnit(softwareUnitName)) {
		softwareUnit = mod.getSoftwareUnitByName(softwareUnitName);
	    }
	}
	if (softwareUnit == null) {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService().getTranslatedString("NoSoftwareUnit"));
	}
	return softwareUnit;
    }

    public ArrayList<Module> getSubModules() {
	return subModules;
    }

    public String getType() {
	return type;
    }

    public ArrayList<SoftwareUnitDefinition> getUnits() {
	return mappedSUunits;
    }

    public boolean hasRegExSoftwareUnit(String softwareUnitName) {
	return hasRegExSoftwareUnit(softwareUnitName, false);
    }

    private boolean hasRegExSoftwareUnit(String softwareUnitName,
	    boolean directly) {
	boolean hasSoftwareUnit = false;
	for (SoftwareUnitRegExDefinition unit : mappedRegExSUunits) {
	    if (unit.getName().equals(softwareUnitName)) {
		hasSoftwareUnit = true;
	    }
	}
	if (!directly) {
	    for (Module mod : subModules) {
		if (mod.hasRegExSoftwareUnit(softwareUnitName, directly)) {
		    hasSoftwareUnit = true;
		}
	    }
	}
	return hasSoftwareUnit;
    }

    public boolean hasRegExSoftwareUnitDirectly(String softwareUnitName) {
	return hasRegExSoftwareUnit(softwareUnitName, true);
    }

    public boolean hasSoftwareUnit(String softwareUnitName) {
	return hasSoftwareUnit(softwareUnitName, false);
    }

    private boolean hasSoftwareUnit(String softwareUnitName, boolean directly) {
	boolean hasSoftwareUnit = false;
	for (SoftwareUnitDefinition unit : mappedSUunits) {
	    if (unit.getName().equals(softwareUnitName)) {
		hasSoftwareUnit = true;
	    }
	}
	if (!directly) {
	    for (Module mod : subModules) {
		if (mod.hasSoftwareUnit(softwareUnitName, directly)) {
		    hasSoftwareUnit = true;
		}
	    }
	}
	return hasSoftwareUnit;
    }

    public boolean hasSoftwareUnitDirectly(String softwareUnitName) {
	return hasSoftwareUnit(softwareUnitName, true);
    }

    public boolean hasSubModule(long id) {
	boolean hasSubModule = false;
	for (Module subModule : subModules) {
	    if (subModule.getId() == id || subModule.hasSubModule(id)) {
		hasSubModule = true;
	    }
	}
	return hasSubModule;
    }

    public boolean hasSubModule(String name) {
	boolean hasSubModule = false;

	for (Module subModule : subModules) {
	    if (subModule.getName().equals(name)) {
		hasSubModule = true;
	    } else if (!(subModule instanceof Layer)
		    && subModule.hasSubModule(name)) {
		hasSubModule = true;
	    }
	}
	return hasSubModule;
    }

    public boolean hasSubModules() {
	return subModules.isEmpty();
    }

    public boolean isMapped() {
	boolean isMapped = false;
	if (mappedSUunits.size() > 0) {
	    isMapped = true;
	}
	return isMapped;
    }

    public boolean moduleAlreadyExistentWithinSystem(String name) {
	Module parentWalker = this;
	while (parentWalker.parent != null && !(parentWalker instanceof Layer)) {
	    parentWalker = parentWalker.parent;
	}
	return parentWalker.hasSubModule(name);
    }

    public void removeSubModule(Module subModule) {
	if (subModules.contains(subModule)
		&& this.hasSubModule(subModule.getName())) {
	    subModules.remove(subModule);
	} else {
	    System.out.println("This sub module does not exist!");
	}
    }

    public void removeSUDefintion(SoftwareUnitDefinition unit) {
	if (mappedSUunits.contains(unit)
		&& hasSoftwareUnitDirectly(unit.getName())) {
	    mappedSUunits.remove(unit);
	} else {
	    System.out.println("This software unit does not exist!");
	}
    }

    public void removeSURegExDefinition(SoftwareUnitRegExDefinition unit) {
	System.out.println(unit.getName());
	if (mappedRegExSUunits.contains(unit)) {
	    mappedRegExSUunits.remove(unit);
	} else {
	    System.out.println("This regex software unit does not exist!");
	}
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public void setId(long id) {
	this.id = id;
    }

    public void setName(String name) {
	this.name = name;
    }

    public void setRegExUnits(ArrayList<SoftwareUnitRegExDefinition> units) {
	mappedRegExSUunits = units;
    }

    public void setSubModules(ArrayList<Module> subModules) {
	for (Module module : subModules) {
	    module.parent = this;
	}
	this.subModules = subModules;
    }

    public void setType(String type) {
	this.type = type;
    }

    public void setUnits(ArrayList<SoftwareUnitDefinition> units) {
	mappedSUunits = units;
    }

}
