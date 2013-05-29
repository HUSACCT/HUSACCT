package husacct.define.domain;

import husacct.ServiceProvider;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ToBeImplemented.ModuleFactory;
import husacct.define.domain.module.ToBeImplemented.ModuleStrategy;
import husacct.define.domain.module.ToBeImplemented.modules.Component;
import husacct.define.domain.module.ToBeImplemented.modules.ExternalLibrary;
import husacct.define.domain.module.ToBeImplemented.modules.Facade;
import husacct.define.domain.module.ToBeImplemented.modules.Layer;
import husacct.define.domain.module.ToBeImplemented.modules.SubSystem;

import java.util.ArrayList;
import java.util.Collections;

public class SoftwareArchitecture {
    
private	ModuleFactory factory = new ModuleFactory();
    private static SoftwareArchitecture instance = null;

    public static SoftwareArchitecture getInstance() {
	return instance == null ? (instance = new SoftwareArchitecture())
		: instance;
    }

    public static void setInstance(SoftwareArchitecture sA) {
	instance = sA;
    }

    private ArrayList<AppliedRuleStrategy> appliedRules;

    private ModuleStrategy rootModule;

    public SoftwareArchitecture() {
	this("SoftwareArchitecture", "This is the root of the architecture",
		new ArrayList<ModuleStrategy>(), new ArrayList<AppliedRuleStrategy>());
    }

    public SoftwareArchitecture(String name, String description) {
	this(name, description, new ArrayList<ModuleStrategy>(),
		new ArrayList<AppliedRuleStrategy>());
    }

    public SoftwareArchitecture(String name, String description,
	    ArrayList<ModuleStrategy> modules, ArrayList<AppliedRuleStrategy> rules) {
	rootModule =factory.createModule("Root");

	rootModule.set(name, description);
	setModules(modules);
	setAppliedRules(rules);
    }

    public void addAppliedRule(AppliedRuleStrategy rule) {
	if (!appliedRules.contains(rule) && !hasAppliedRule(rule.getId())) {
	    appliedRules.add(rule);
	} else {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService().getTranslatedString("RuleAlreadyAdded"));
	}
    }

    public long addModule(ModuleStrategy module) {
	long moduleId;
	if (!hasModule(module.getName())) {
	    rootModule.addSubModule(module);
	    moduleId = module.getId();
	} else {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService().getTranslatedString("SameNameModule"));
	    // TODO! Foutmelding ffs!
	}
	return moduleId;
    }

    public String addNewModule(ModuleStrategy module) {
	if (hasModule(module.getName())) {
	    return ServiceProvider.getInstance().getLocaleService()
		    .getTranslatedString("SameNameModule");
	} else {
	    rootModule.addSubModule(module);
	}
	return "";
    }

    // TODO: Holy sh...
    private ModuleStrategy generateNewType(ModuleStrategy oldModule, String newType) {
	Long id = oldModule.getId();
	String name = oldModule.getName();
	String desc = oldModule.getDescription();
	ArrayList<SoftwareUnitDefinition> softwareUnits = oldModule.getUnits();
	ArrayList<ModuleStrategy> subModules = oldModule.getSubModules();
	processDefaultComponents(oldModule);

	if (ServiceProvider.getInstance().getLocaleService()
		.getTranslatedString("Layer").toLowerCase()
		.equals(newType.toLowerCase())) {
	    Layer layer = new Layer();
	    layer.setDescription(desc);
	    layer.setId(id);
	    layer.setName(name);
	    layer.setType(newType);
	    layer.setSubModules(subModules);
	    layer.setUnits(softwareUnits);
	    return layer;
	} else if (ServiceProvider.getInstance().getLocaleService()
		.getTranslatedString("Component").toLowerCase()
		.equals(newType.toLowerCase())) {
	    Component component = (Component) factory.createModule("Component");
	    component.setDescription(desc);
	    component.setId(id);
	    component.setName(name);
	    component.setType(newType);
	    Facade f = (Facade) factory.createDummy("Facade");
	    subModules.add(f);
	    Collections.reverse(subModules);
	    component.setSubModules(subModules);
	    component.setUnits(softwareUnits);

	    return component;
	} else if (ServiceProvider.getInstance().getLocaleService()
		.getTranslatedString("SubSystem").toLowerCase()
		.equals(newType.toLowerCase())) {
	    SubSystem subSystem = new SubSystem();
	    subSystem.setDescription(desc);
	    subSystem.setId(id);
	    subSystem.setName(name);
	    subSystem.setType(newType);
	    subSystem.setSubModules(subModules);
	    subSystem.setUnits(softwareUnits);
	    return subSystem;
	} else if (ServiceProvider.getInstance().getLocaleService()
		.getTranslatedString("ExternalLibrary").toLowerCase()
		.equals(newType.toLowerCase())) {
		ExternalLibrary externalSystem = (ExternalLibrary) factory.createModule("ExternalLibrary");
	    externalSystem.setDescription(desc);
	    externalSystem.setId(id);
	    externalSystem.setName(name);
	    externalSystem.setType(newType);
	    externalSystem.setSubModules(subModules);
	    externalSystem.setUnits(softwareUnits);
	    return externalSystem;
	} else {
	    return null;
	}

    }

    public AppliedRuleStrategy getAppliedRuleById(long appliedRuleId) {
	if (hasAppliedRule(appliedRuleId)) {
	    for (AppliedRuleStrategy rule : appliedRules) {
		if (rule.getId() == appliedRuleId) {
		    return rule;
		}
	    }
	} else {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService().getTranslatedString("NoRule"));
	}
	return null;
    }

    public ArrayList<AppliedRuleStrategy> getAppliedRules() {
	return appliedRules;
    }

    public ArrayList<Long> getAppliedRulesIdsByModuleFromId(long moduleId) {
	ArrayList<Long> appliedRuleIds = new ArrayList<Long>();
	for (AppliedRuleStrategy rule : appliedRules) {
	    if (rule.getModuleFrom().getId() == moduleId) {
		appliedRuleIds.add(rule.getId());
	    }
	}
	return appliedRuleIds;
    }

    public ArrayList<Long> getAppliedRulesIdsByModuleToId(long moduleId) {
	ArrayList<Long> appliedRuleIds = new ArrayList<Long>();
	for (AppliedRuleStrategy rule : appliedRules) {
	    if (rule.getModuleTo().getId() == moduleId) {
		appliedRuleIds.add(rule.getId());
	    }
	}
	return appliedRuleIds;
    }

    public String getDescription() {
	return rootModule.getDescription();
    }

    public ArrayList<AppliedRuleStrategy> getEnabledAppliedRules() {
	ArrayList<AppliedRuleStrategy> enabledRuleList = new ArrayList<AppliedRuleStrategy>();
	for (AppliedRuleStrategy ar : appliedRules) {
	    if (ar.isEnabled()) {
		enabledRuleList.add(ar);
	    }
	}
	return enabledRuleList;
    }

    public ArrayList<AppliedRuleStrategy> getGeneratedRules() {
	return null; // TODO: Has to get an implementation
    }

    public ArrayList<Layer> getLayersBelow(Layer layer) {
	ArrayList<Layer> returnList = new ArrayList<Layer>();
	Layer underlyingLayer = getTheFirstLayerBelow(layer);
	Layer _temp = underlyingLayer;

	while (getTheFirstLayerBelow(_temp).equals(null)) {
	    returnList.add(_temp);
	    _temp = getTheFirstLayerBelow(_temp);
	}
	return returnList; // TODO: ?
    }

    public ModuleStrategy getModuleById(long moduleId) {
	ModuleStrategy currentModule = null;
	if (rootModule.getId() == moduleId || rootModule.hasSubModule(moduleId)) {
	    currentModule = rootModule;
	    while (currentModule.getId() != moduleId) {
		for (ModuleStrategy subModule : currentModule.getSubModules()) {
		    if (subModule.getId() == moduleId
			    || subModule.hasSubModule(moduleId)) {
			currentModule = subModule;
		    }
		}
	    }
	}
	if (currentModule == null) {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService().getTranslatedString("NoModule"));
	}
	return currentModule;
    }

    public ModuleStrategy getModuleByLogicalPath(String logicalPath) {
	ModuleStrategy currentModule = null;
	if (logicalPath.equals("**")) {
	    currentModule = rootModule;
	} else {
	    String[] moduleNames = logicalPath.split("\\.");
	    int i = 0;
	    for (ModuleStrategy module : rootModule.getSubModules()) {
		if (module.getName().equals(moduleNames[i])) {
		    currentModule = module;

		    for (int j = i; j < moduleNames.length; j++) {
			for (ModuleStrategy subModule : currentModule.getSubModules()) {
			    if (subModule.getName().equals(moduleNames[j])) {
				currentModule = subModule;
			    }
			}
		    }
		}
	    }
	    if (currentModule == null
		    || !currentModule.getName().equals(
			    moduleNames[moduleNames.length - 1])) {
		throw new RuntimeException(ServiceProvider.getInstance()
			.getLocaleService()
			.getTranslatedString("ModuleNotFound"));
	    }
	}
	return currentModule;
    }

    public ModuleStrategy getModuleByRegExSoftwareUnit(String softwareUnitName) {
	ModuleStrategy currentModule = null;
	if (rootModule.hasRegExSoftwareUnit(softwareUnitName)) {
	    currentModule = rootModule;
	    while (!currentModule
		    .hasRegExSoftwareUnitDirectly(softwareUnitName)) {
		for (ModuleStrategy subModule : currentModule.getSubModules()) {
		    if (subModule.hasRegExSoftwareUnit(softwareUnitName)) {
			currentModule = subModule;
		    }
		}
	    }
	}
	if (currentModule == null) {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService()
		    .getTranslatedString("SoftwareUnitNotMapped"));
	}
	return currentModule;
    }

    public ModuleStrategy getModuleBySoftwareUnit(String softwareUnitName) {
	ModuleStrategy currentModule = null;
	if (rootModule.hasSoftwareUnit(softwareUnitName)) {
	    currentModule = rootModule;
	    while (!currentModule.hasSoftwareUnitDirectly(softwareUnitName)) {
		for (ModuleStrategy subModule : currentModule.getSubModules()) {
		    if (subModule.hasSoftwareUnit(softwareUnitName)) {
			currentModule = subModule;
		    }
		}
	    }
	}
	if (currentModule == null) {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService()
		    .getTranslatedString("SoftwareUnitNotMapped"));
	}
	return currentModule;
    }

    public ArrayList<ModuleStrategy> getModules() {
	return rootModule.getSubModules();
    }

    private ArrayList<ModuleStrategy> getModulesForLayerSorting(long parentModuleId) {
	ArrayList<ModuleStrategy> modulesToCheck = rootModule.getSubModules();
	if (parentModuleId != -1L) {
	    ModuleStrategy parentModule = getModuleById(parentModuleId);
	    modulesToCheck = parentModule.getSubModules();
	}
	return modulesToCheck;
    }

    public String getModulesLogicalPath(long moduleId) {
	String logicalPath = "";
	ModuleStrategy wantedModule = getModuleById(moduleId);
	ModuleStrategy currentModule = null;

	if (rootModule.getId() == moduleId) {
	    logicalPath = "**";
	} else {
	    for (ModuleStrategy mod : rootModule.getSubModules()) {
		if (mod.getName().equals(wantedModule.getName())
			|| mod.hasSubModule(wantedModule.getName())) {
		    logicalPath += mod.getName();
		    currentModule = mod;

		    while (!currentModule.getName().equals(
			    wantedModule.getName())) {
			for (ModuleStrategy subModule : currentModule.getSubModules()) {
			    if (subModule.getName().equals(
				    wantedModule.getName())
				    || subModule.hasSubModule(wantedModule
					    .getName())) {
				logicalPath += "." + subModule.getName();
				currentModule = subModule;
			    }
			}
		    }
		    break;
		}
	    }
	}
	return logicalPath;
    }

    public String getName() {
	return rootModule.getName();
    }

    // TODO SEE IF CAN BE BETTER IMPLEMENTED yes we caaan :D //al gedaan in
    // ModuleDomainService maar dan zonder kut id....
    public long getParentModuleIdByChildId(long childModuleId) {
	long parentModuleId = -1L;

	if (rootModule.getId() == childModuleId) {
	    parentModuleId = -1;
	} else {
	    for (ModuleStrategy module : rootModule.getSubModules()) {
		if (module.getId() == childModuleId) {
		    parentModuleId = rootModule.getId();
		} else {
		    if (module.hasSubModule(childModuleId)) {
			ModuleStrategy currentModule = module;
			while (parentModuleId == -1L) {
			    for (ModuleStrategy subModule : currentModule
				    .getSubModules()) {
				if (subModule.getId() == childModuleId) {
				    parentModuleId = currentModule.getId();
				    break;
				} else if (subModule
					.hasSubModule(childModuleId)) {
				    currentModule = subModule;
				    break;
				}
			    }
			}
			break;
		    }
		}
	    }
	}
	return parentModuleId;
    }

    public ModuleStrategy getRootModule() {
	return rootModule;
    }

    public SoftwareUnitDefinition getSoftwareUnitByName(String softwareUnitName) {
	SoftwareUnitDefinition softwareUnit = null;
	if (rootModule.hasSoftwareUnit(softwareUnitName)) {
	    softwareUnit = rootModule.getSoftwareUnitByName(softwareUnitName);
	}
	if (softwareUnit == null) {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService().getTranslatedString("NoSoftwareUnit"));
	}
	return softwareUnit;
    }

    private Layer getTheFirstLayerAbove(int currentHierarchicalLevel,
	    long parentModuleId) {
	Layer layer = null;
	for (ModuleStrategy mod : getModulesForLayerSorting(parentModuleId)) {
	    if (mod instanceof Layer) {
		Layer l = (Layer) mod;
		if (l.getHierarchicalLevel() < currentHierarchicalLevel
			&& (layer == null || l.getHierarchicalLevel() > layer
				.getHierarchicalLevel())) {
		    layer = l;
		}
	    }
	}
	return layer;
    }

    public Layer getTheFirstLayerBelow(int currentHierarchicalLevel,
	    long parentModuleId) {
	Layer layer = null;
	for (ModuleStrategy mod : getModulesForLayerSorting(parentModuleId)) {
	    if (mod instanceof Layer) {
		Layer l = (Layer) mod;
		if (l.getHierarchicalLevel() > currentHierarchicalLevel
			&& (layer == null || l.getHierarchicalLevel() < layer
				.getHierarchicalLevel())) {
		    layer = l;
		}
	    }
	}
	return layer;
    }

    public Layer getTheFirstLayerBelow(Layer layer) {
	return getTheFirstLayerBelow(layer.getHierarchicalLevel(),
		getParentModuleIdByChildId(layer.getId()));
    }

    private boolean hasAppliedRule(long ruleID) {
	boolean ruleFound = false;
	for (AppliedRuleStrategy rule : appliedRules) {
	    if (rule.getId() == ruleID) {
		ruleFound = true;
	    }
	}
	return ruleFound;
    }

    private boolean hasModule(String name) {
	if (rootModule.getName().equals(name)) {
	    return true;
	} else {
	    for (ModuleStrategy module : rootModule.getSubModules()) {
		if (module.getName().equals(name)) {
		    return true;
		}
	    }
	}
	return false;
    }

    public void moveLayerDown(long layerId) {
	Layer layer = (Layer) getModuleById(layerId);
	Layer layerBelowLayer = getTheFirstLayerBelow(
		layer.getHierarchicalLevel(),
		getParentModuleIdByChildId(layerId));
	if (layerBelowLayer != null) {
	    switchHierarchicalLayerLevels(layer, layerBelowLayer);
	}
    }

    public void moveLayerUp(long layerId) {
	Layer layer = (Layer) getModuleById(layerId);
	Layer layerAboveLayer = getTheFirstLayerAbove(
		layer.getHierarchicalLevel(),
		getParentModuleIdByChildId(layerId));
	if (layerAboveLayer != null) {
	    switchHierarchicalLayerLevels(layer, layerAboveLayer);
	}
    }

    private void processDefaultComponents(ModuleStrategy oldModule) {
	if (oldModule instanceof Component) {
	    oldModule.getSubModules().remove(0);
	}
    }

    public void removeAllModules() {
	rootModule.setSubModules(new ArrayList<ModuleStrategy>());
    }

    public void removeAppliedRule(long appliedRuleId) {
	if (hasAppliedRule(appliedRuleId)) {
	    AppliedRuleStrategy rule = getAppliedRuleById(appliedRuleId);
	    appliedRules.remove(rule);
	} else {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService().getTranslatedString("NoRule"));
	}
    }

    public void removeAppliedRules() {
	appliedRules = new ArrayList<AppliedRuleStrategy>();
    }

    public void removeLayerAppliedRules() {
	ArrayList<AppliedRuleStrategy> rulesTobeRemoved = new ArrayList<AppliedRuleStrategy>();
	for (AppliedRuleStrategy rules : appliedRules) {
	    String moduleFromType = rules.getModuleFrom().getType()
		    .toLowerCase();
	    String moduleToType = rules.getModuleTo().getType().toLowerCase();
	    String ruleType = rules.getRuleType();

	    if (ruleType.equals("IsNotAllowedToUse")
		    && moduleFromType.equals("layer")
		    && moduleToType.equals("layer")) {
		rulesTobeRemoved.add(rules);
	    }
	}
	for (AppliedRuleStrategy rule : rulesTobeRemoved) {
	    int index = appliedRules.indexOf(rule);
	    appliedRules.remove(index);
	}
    }

    // TODO: Needs revising, too big
    public void removeModule(ModuleStrategy moduleToRemove) {
	if (moduleToRemove.equals(rootModule)) {
	    return;
	}
	removeRelatedRules(moduleToRemove);
	ModuleStrategy currentModule = null;
	boolean moduleFound = false;
	if (rootModule.getSubModules().contains(moduleToRemove)) {
	    moduleFound = true;
	    rootModule.getSubModules().remove(moduleToRemove);
	} else {
	    for (ModuleStrategy mod : rootModule.getSubModules()) {
		if (mod.getSubModules().contains(moduleToRemove)) {
		    mod.getSubModules().remove(moduleToRemove);
		    moduleFound = true;
		    break;
		} else if (mod.hasSubModule(moduleToRemove.getId())) {
		    currentModule = mod;
		    while (mod.hasSubModule(moduleToRemove.getId())) {
			for (ModuleStrategy subModule : currentModule.getSubModules()) {
			    if (subModule.getId() == moduleToRemove.getId()) {
				currentModule.removeSubModule(subModule);
				moduleFound = true;
				break;
			    } else if (subModule.hasSubModule(moduleToRemove
				    .getId())) {
				currentModule = subModule;
			    }
			}
		    }
		}
	    }
	}
	if (!moduleFound) {
	    throw new RuntimeException(ServiceProvider.getInstance()
		    .getLocaleService().getTranslatedString("NoModule"));
	}
    }

    private void removeRelatedRules(ModuleStrategy module) {
	// Copy all currentValues into another list to prevent
	// ConcurrentModificationExceptions
	@SuppressWarnings("unchecked")
	ArrayList<AppliedRuleStrategy> tmpList = (ArrayList<AppliedRuleStrategy>) appliedRules
		.clone();
	for (AppliedRuleStrategy rule : appliedRules) {
	    if (rule.getModuleFrom().equals(module)
		    || rule.getModuleTo().equals(module)) {
		tmpList.remove(rule);
	    }

	    @SuppressWarnings("unchecked")
	    ArrayList<AppliedRuleStrategy> tmpExceptionList = (ArrayList<AppliedRuleStrategy>) rule
		    .getExceptions().clone();
	    for (AppliedRuleStrategy exceptionRule : rule.getExceptions()) {
		if (exceptionRule.getModuleFrom().equals(module)
			|| exceptionRule.getModuleTo().equals(module)) {
		    tmpExceptionList.remove(exceptionRule);
		}
	    }
	    rule.setExceptions(tmpExceptionList);
	}
	appliedRules = tmpList;
    }

    public void setAppliedRules(ArrayList<AppliedRuleStrategy> appliedRules) {
	this.appliedRules = appliedRules;
    }

    public void setDescription(String description) {
	rootModule.setDescription(description);
    }

    public void setModules(ArrayList<ModuleStrategy> modules) {
	rootModule.setSubModules(modules);
    }

    public void setName(String name) {
	rootModule.setName(name);
    }

    private void switchHierarchicalLayerLevels(Layer layerOne, Layer layerTwo) {
	int hierarchicalLevelLayerOne = layerOne.getHierarchicalLevel();
	layerOne.setHierarchicalLevel(layerTwo.getHierarchicalLevel());
	layerTwo.setHierarchicalLevel(hierarchicalLevelLayerOne);
    }

    public ModuleStrategy updateModuleType(ModuleStrategy oldModule, String newType) {

	System.out.println(oldModule.getClass().getName()); // TODO: Print line?
	ModuleStrategy parent = oldModule.getparent();

	int index = oldModule.getparent().getSubModules().indexOf(oldModule);
	parent.getSubModules().remove(index);
	ModuleStrategy updatedModule = generateNewType(oldModule, newType);
	parent.addSubModule(index, updatedModule);

	return updatedModule;
    }
}
