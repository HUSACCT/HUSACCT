package husacct.define.domain;

import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;

import java.util.ArrayList;

public class SoftwareArchitecture {
	
	private String name;
	private String description;
	private ArrayList<Module> modules;
	private ArrayList<AppliedRule> appliedRules;
	
	private static SoftwareArchitecture instance = null;
	public static SoftwareArchitecture getInstance()
	{
		return instance == null ? (instance = new SoftwareArchitecture()) : instance;
	}
	
	public static void setInstance(SoftwareArchitecture sA)
	{
		instance = sA;
	}
	
	public SoftwareArchitecture() {
		this("","",new ArrayList<Module>(),new ArrayList<AppliedRule>());
	}
	
	public SoftwareArchitecture(String name, String description) {
		this(name,description,new ArrayList<Module>(),new ArrayList<AppliedRule>());
	}
	
	public SoftwareArchitecture(String name, String description, ArrayList<Module> modules, ArrayList<AppliedRule> rules) {
		
		this.setName(name);
		this.setDescription(description);
		setModules(modules);
		setAppliedRules(rules);
	}
	
	/*
	 * Getters and Setters
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setModules(ArrayList<Module> modules) {
		this.modules = modules;
	}

	public ArrayList<Module> getModules() {
		return modules;
	}
	
	public void setAppliedRules(ArrayList<AppliedRule> appliedRules) {
		this.appliedRules = appliedRules;
	}

	public ArrayList<AppliedRule> getAppliedRules() {
		return appliedRules;
	}

	/*
	 * Applied Rules
	 */
	public ArrayList<Long> getAppliedRulesIdsByModuleFromId(long moduleId) {
		ArrayList<Long> appliedRuleIds = new ArrayList<Long>();
		for (AppliedRule rule : appliedRules){
			if (rule.getModuleFrom().getId() == moduleId){
				appliedRuleIds.add(rule.getId());
			}
		}
		return appliedRuleIds;
	}
	
	public ArrayList<Long> getAppliedRulesIdsByModuleToId(long moduleId) {
		ArrayList<Long> appliedRuleIds = new ArrayList<Long>();
		for (AppliedRule rule : appliedRules){
			if (rule.getModuleTo().getId() == moduleId){
				appliedRuleIds.add(rule.getId());
			}
		}
		return appliedRuleIds;
	}
	
	public void addAppliedRule(AppliedRule rule)
	{
		if(!appliedRules.contains(rule) && !this.hasAppliedRule(rule.getId()))
		{
			appliedRules.add(rule);
		}else{
			throw new RuntimeException("This rule has already been added!");
		}
	}
	
	public void removeAppliedRule(long appliedRuleId)
	{
		if(this.hasAppliedRule(appliedRuleId))
		{
			AppliedRule rule = getAppliedRuleById(appliedRuleId);
			appliedRules.remove(rule);	
		}else{
			throw new RuntimeException("This rule does not exist!");
		}
	}
	
	private boolean hasAppliedRule(long l) 
	{
		boolean ruleFound = false;
		for(AppliedRule rule : appliedRules) 
		{
			if(rule.getId() == l)
			{
				ruleFound = true;
			}
		}
		return ruleFound;
	}
	
	public AppliedRule getAppliedRuleById(long appliedRuleId){
		AppliedRule appliedRule = new AppliedRule();
		if(this.hasAppliedRule(appliedRuleId))
		{
			for(AppliedRule rule : appliedRules) 
			{
				if(rule.getId() == appliedRuleId)
				{
					appliedRule = rule;
					break;
				}
			}		
		}else{
			throw new RuntimeException("This rule does not exist!");
		}
		return appliedRule;
	}
	
	public ArrayList<AppliedRule> getEnabledAppliedRules() {
		ArrayList<AppliedRule> enabledRuleList =  new ArrayList<AppliedRule>();
		for (AppliedRule ar : appliedRules){
			if (ar.isEnabled()){
				enabledRuleList.add(ar);
			}
		}
		return enabledRuleList;
	}

	/*
	 * SoftwareUnitDefinitions
	 */
	public SoftwareUnitDefinition getSoftwareUnitByName(String softwareUnitName) {
		SoftwareUnitDefinition softwareUnit = null;
		for (Module mod : modules){
			if (mod.hasSoftwareUnit(softwareUnitName)){
				softwareUnit = mod.getSoftwareUnitByName(softwareUnitName);
				break;
			}
		}
		if (softwareUnit == null){ throw new RuntimeException("This Software Unit does not exist!");}
		return softwareUnit;
	}
	
	/*
	 * MODULES
	 */
	public Module getModuleById(long moduleId) {
		Module currentModule = null;
		for(Module module : modules){
			
			if (module.getId() == moduleId || module.hasSubModule(moduleId)){
				currentModule = module;
				while (currentModule.getId() != moduleId){
					for (Module subModule : currentModule.getSubModules()){
						if (subModule.getId() == moduleId || subModule.hasSubModule(moduleId)){
							currentModule = subModule;
						}
					}
				}
				break;
			}
			
		}
		if (currentModule == null){throw new RuntimeException("This module does not exist!");}
		return currentModule;
	}
	
	public Module getModuleBySoftwareUnit(SoftwareUnitDefinition su) {
		Module currentModule = null;
		for(Module module : modules){
			
			if (module.hasSoftwareUnit(su.getName())){
				currentModule = module;
				while (!currentModule.hasSoftwareUnitDirectly(su.getName())){
					for (Module subModule : currentModule.getSubModules()){
						if (subModule.hasSoftwareUnit(su.getName())){
							currentModule = subModule;
						}
					}
				}
				break;
			}
			
		}
		if (currentModule == null){throw new RuntimeException("This softwareunit is not mapped to a module!");}
		return currentModule;
	}
	
	
	public long addModule(Module module)
	{
		long moduleId;
		if(!this.hasModule(module.getName())) {
			if (!modules.contains(module)){
				modules.add(module);
				moduleId = module.getId();
			}else {
				throw new RuntimeException("Cannot add module, since there already is a module with the same unique data!");
			}
		}else{
			throw new RuntimeException("There is already a module with this name!");
		}
		return moduleId;
	}
	
	public void removeModule(Module moduleToRemove)
	{
		removeRelatedRules(moduleToRemove);
		Module currentModule = null;
		boolean moduleFound = false;
		if(modules.contains(moduleToRemove)) {
			moduleFound = true;
			modules.remove(moduleToRemove);
		}else{
			for (Module mod : modules){
				if(mod.getSubModules().contains(moduleToRemove)) {
					mod.getSubModules().remove(moduleToRemove);
					moduleFound = true;
					break;
				}else if (mod.hasSubModule(moduleToRemove.getId())){	
					currentModule = mod;
					while (mod.hasSubModule(moduleToRemove.getId())){
						for (Module subModule : currentModule.getSubModules()){
							if (subModule.getId() == moduleToRemove.getId()){
								currentModule.removeSubModule(subModule);
								moduleFound = true;
								break;
							}else if (subModule.hasSubModule(moduleToRemove.getId())){
								currentModule = subModule;
							}
						}
					}
				}
			}
		}
		if (!moduleFound) {	throw new RuntimeException("This module does not exist!");}
	}
	
	private void removeRelatedRules(Module module) {
		//Copy all currentValues into another list to prevent ConcurrentModificationExceptions 
		@SuppressWarnings("unchecked")
		ArrayList<AppliedRule> tmpList = (ArrayList<AppliedRule>) appliedRules.clone();
		for (AppliedRule rule : appliedRules){
			if (rule.getModuleFrom().equals(module) || 
					rule.getModuleTo().equals(module)){
				tmpList.remove(rule);
			}	
			
			for (AppliedRule exceptionRule : rule.getExceptions()){
				if (exceptionRule.getModuleFrom().equals(module) || 
						exceptionRule.getModuleTo().equals(module)){
					rule.getExceptions().remove(exceptionRule);
				}		
			}
		}
		appliedRules = tmpList;	
	}

	private boolean hasModule(String name) 
	{
		for(Module module : modules) 
		{
			
			if(module.getName().equals(name))
			{
				return true;
			}
		}
		
		return false;
	}
	
	public Module getModuleByLogicalPath(String logicalPath) {		
		String[] moduleNames = logicalPath.split("\\.");
		int i = 0;
		Module currentModule = null;
		for (Module module : modules){
			if (module.getName().equals(moduleNames[i])){
				currentModule = module;
				
				for (int j = i;j<moduleNames.length;j++){
					for (Module subModule : currentModule.getSubModules()){
						if (subModule.getName().equals(moduleNames[j])){
							currentModule = subModule;							
						}
					}
				}
			}
		}
		if (currentModule == null || 
				!currentModule.getName().equals(moduleNames[moduleNames.length-1])){ 
			throw new RuntimeException("This module is not found!");
		}
		return currentModule;
	}

	public String getModulesLogicalPath(long moduleId) {
		String logicalPath = "";
		Module wantedModule =  getModuleById(moduleId);
		Module currentModule = null;
		
		for (Module mod : modules){
			if (mod.getName().equals(wantedModule.getName()) || 
					mod.hasSubModule(wantedModule.getName())){
				logicalPath += mod.getName();
				currentModule = mod;
				
				while (!currentModule.getName().equals(wantedModule.getName())){
					for (Module subModule : currentModule.getSubModules()){
						if (subModule.getName().equals(wantedModule.getName()) ||
								subModule.hasSubModule(wantedModule.getName())){
							logicalPath += "." + subModule.getName();
							currentModule = subModule;
						}
					}
				}
				break;
			}
		}
		return logicalPath;
	}
	
	public long getParentModuleIdByChildId(long childModuleId) {
		long parentModuleId = -1L;
		for(Module module : modules) {
			if (module.getId() != childModuleId && module.hasSubModule(childModuleId)) {
				Module currentModule = module;
				while(parentModuleId == -1L) {
					for (Module subModule : currentModule.getSubModules()) {
						if (subModule.getId() == childModuleId) {
							parentModuleId = currentModule.getId();
							break;
						} else if(subModule.hasSubModule(childModuleId)) {
							currentModule = subModule;
							break;
						}
					}
				}
				break;
			}
		}
		return parentModuleId;
	}
	
	/*
	 * Layers
	 */
	public void moveLayerUp(long layerId) {
		Layer layer = (Layer) getModuleById(layerId);
		Layer layerAboveLayer = getTheFirstLayerAbove(layer.getHierarchicalLevel(), getParentModuleIdByChildId(layerId));
		if (layerAboveLayer != null){
			switchHierarchicalLayerLevels(layer, layerAboveLayer);
		}
	}
	
	private Layer getTheFirstLayerAbove(int currentHierarchicalLevel, long parentModuleId){
		Layer layer = null;
		for (Module mod : getModulesForLayerSorting(parentModuleId)){
			if (mod instanceof Layer) {
				Layer l = (Layer)mod;
				if (l.getHierarchicalLevel() < currentHierarchicalLevel &&
						(layer == null || l.getHierarchicalLevel() > layer.getHierarchicalLevel())){
					layer = l;
				}
			}
		}
		return layer;
	}

	public void moveLayerDown(long layerId) {
		Layer layer = (Layer)getModuleById(layerId);
		Layer layerBelowLayer = getTheFirstLayerBelow(layer.getHierarchicalLevel(), getParentModuleIdByChildId(layerId));
		if (layerBelowLayer != null){
			switchHierarchicalLayerLevels(layer, layerBelowLayer);
		}
	}
	
	private Layer getTheFirstLayerBelow(int currentHierarchicalLevel, long parentModuleId){
		Layer layer = null;
		for (Module mod : getModulesForLayerSorting(parentModuleId)){
			if (mod instanceof Layer) {
				Layer l = (Layer)mod;
				if (l.getHierarchicalLevel() > currentHierarchicalLevel &&
						(layer == null || l.getHierarchicalLevel() < layer.getHierarchicalLevel())){
					layer = l;
				}
			}
		}
		return layer;
	}
	
	private ArrayList<Module> getModulesForLayerSorting(long parentModuleId) {
		ArrayList<Module> modulesToCheck = modules;
		if(parentModuleId != -1L) {
			Module parentModule = getModuleById(parentModuleId);
			modulesToCheck = parentModule.getSubModules();
		}
		return modulesToCheck;
	}
	
	private void switchHierarchicalLayerLevels(Layer layerOne, Layer layerTwo){
		int hierarchicalLevelLayerOne = layerOne.getHierarchicalLevel();
		layerOne.setHierarchicalLevel(layerTwo.getHierarchicalLevel());
		layerTwo.setHierarchicalLevel(hierarchicalLevelLayerOne);
	}
}
