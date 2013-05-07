package husacct.define.domain;

import husacct.ServiceProvider;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.define.domain.services.AppliedRuleDomainService;

import java.util.ArrayList;

public class SoftwareArchitecture {
	
	private Module rootModule;
	
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
		this("SoftwareArchitecture","This is the root of the architecture",new ArrayList<Module>(),new ArrayList<AppliedRule>());
	}
	
	public SoftwareArchitecture(String name, String description) {
		this(name,description,new ArrayList<Module>(),new ArrayList<AppliedRule>());
	}
	
	public SoftwareArchitecture(String name, String description, ArrayList<Module> modules, ArrayList<AppliedRule> rules) {
		rootModule = new Module(name, description);
		rootModule.setId(0);
		setModules(modules);
		setAppliedRules(rules);
	}
	
	/*
	 * Getters and Setters
	 */
	public void setName(String name) {
		rootModule.setName(name);
	}

	public String getName() {
		return rootModule.getName();
	}

	public void setDescription(String description) {
		rootModule.setDescription(description);
	}

	public String getDescription() {
		return rootModule.getDescription();
	}

	public void setModules(ArrayList<Module> modules) {
		rootModule.setSubModules(modules);
	}

	public ArrayList<Module> getModules() {
		return rootModule.getSubModules();
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
	
	public ArrayList<AppliedRule> getGeneratedRules()
	{
		return null; //TODO
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
			throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RuleAlreadyAdded"));
		}
	}
	
	public void removeAppliedRules() {
		appliedRules = new ArrayList<AppliedRule>();
	
	}
	
	public void removeLayerAppliedRules() {
		ArrayList<AppliedRule>rulesTobeRemoved = new ArrayList<AppliedRule>();
		for (AppliedRule rules : appliedRules) {
			String moduleFromType =rules.getModuleFrom().getType().toLowerCase();
			String moduleToType=rules.getModuleTo().getType().toLowerCase();
			String ruleType=rules.getRuleType();
			
			if (ruleType.equals("IsNotAllowedToUse")&&moduleFromType.equals("layer")&&moduleToType.equals("layer")) {
			
				rulesTobeRemoved.add(rules);
			}
		}
	
		for (AppliedRule rule : rulesTobeRemoved) {
			int index= appliedRules.indexOf(rule);
			appliedRules.remove(index);
		}
		
		
	
	}
	
	public void removeAppliedRule(long appliedRuleId)
	{
		if(this.hasAppliedRule(appliedRuleId))
		{
			AppliedRule rule = getAppliedRuleById(appliedRuleId);
			appliedRules.remove(rule);	
		}else{
			throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoRule"));
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
			throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoRule"));
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
		if (rootModule.hasSoftwareUnit(softwareUnitName)){
			softwareUnit = rootModule.getSoftwareUnitByName(softwareUnitName);
		}
		if (softwareUnit == null){ throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoSoftwareUnit"));}
		return softwareUnit;
	}
	
	/*
	 * MODULES
	 */
	public Module getModuleById(long moduleId) {
		Module currentModule = null;
		if (rootModule.getId() == moduleId || rootModule.hasSubModule(moduleId)){
			currentModule = rootModule;
			while (currentModule.getId() != moduleId){
				for (Module subModule : currentModule.getSubModules()){
					if (subModule.getId() == moduleId || subModule.hasSubModule(moduleId)){
						currentModule = subModule;
					}
				}
			}
		}
		if (currentModule == null){throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoModule"));}
		return currentModule;
	}
	
	public Module getModuleBySoftwareUnit(String softwareUnitName) {
		Module currentModule = null;
			if (rootModule.hasSoftwareUnit(softwareUnitName)){
				currentModule = rootModule;
				while (!currentModule.hasSoftwareUnitDirectly(softwareUnitName)){
					for (Module subModule : currentModule.getSubModules()){
						if (subModule.hasSoftwareUnit(softwareUnitName)){
							currentModule = subModule;
						}
					}
				}
			}
		if (currentModule == null){throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareUnitNotMapped"));}
		return currentModule;
	}
	
	public Module getModuleByRegExSoftwareUnit(String softwareUnitName) {
		Module currentModule = null;
			if (rootModule.hasRegExSoftwareUnit(softwareUnitName)){
				currentModule = rootModule;
				while (!currentModule.hasRegExSoftwareUnitDirectly(softwareUnitName)){
					for (Module subModule : currentModule.getSubModules()){
						if (subModule.hasRegExSoftwareUnit(softwareUnitName)){
							currentModule = subModule;
						}
					}
				}
			}
		if (currentModule == null){throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareUnitNotMapped"));}
		return currentModule;
	}
	
	
	public long addModule(Module module)
	{
		long moduleId;
		if(!this.hasModule(module.getName())) {
			rootModule.addSubModule(module);
			moduleId = module.getId();
			
		}else
		{
			throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SameNameModule")); //TODO! Foutmelding ffs!
		}
		return moduleId;
	}
	
	public String addNewModule(Module module)
	{
		if(!this.hasModule(module.getName())) {
			rootModule.addSubModule(module);
		} else {
			return ServiceProvider.getInstance().getLocaleService().getTranslatedString("SameNameModule");
		}
		return "";
	}

	public void removeAllModules() {
		rootModule.setSubModules(new ArrayList<Module>());
	}
	
	public void removeModule(Module moduleToRemove)
	{
		//You cannot remove the rootModule
		if (moduleToRemove.equals(rootModule)){return;}
		removeRelatedRules(moduleToRemove);
		Module currentModule = null;
		boolean moduleFound = false;
		if(rootModule.getSubModules().contains(moduleToRemove)) {
			moduleFound = true;
			rootModule.getSubModules().remove(moduleToRemove);
		}else{
			for (Module mod : rootModule.getSubModules()){
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
		if (!moduleFound) {	throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NoModule"));}
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
			
			@SuppressWarnings("unchecked")
			ArrayList<AppliedRule> tmpExceptionList = (ArrayList<AppliedRule>) rule.getExceptions().clone();
			for (AppliedRule exceptionRule : rule.getExceptions()){
				if (exceptionRule.getModuleFrom().equals(module) || 
						exceptionRule.getModuleTo().equals(module)){
					tmpExceptionList.remove(exceptionRule);
				}		
			}
			rule.setExceptions(tmpExceptionList);
		}
		appliedRules = tmpList;	
	}

	private boolean hasModule(String name) 
	{
		if (rootModule.getName().equals(name)) { 
			return true;
		} 
		else {
			for(Module module : rootModule.getSubModules()) {
				if(module.getName().equals(name)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public Module getModuleByLogicalPath(String logicalPath) {
		Module currentModule = null;
		if (logicalPath.equals("**")){
			currentModule = rootModule;
		} else {
			String[] moduleNames = logicalPath.split("\\.");
			int i = 0;
			for (Module module : rootModule.getSubModules()){
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
				throw new RuntimeException(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ModuleNotFound"));
			}
		}
		return currentModule;
	}

	public String getModulesLogicalPath(long moduleId) {
		String logicalPath = "";
		Module wantedModule =  getModuleById(moduleId);
		Module currentModule = null;
		
		if (rootModule.getId() == moduleId){
			logicalPath = "**";
		} else {
			for (Module mod : rootModule.getSubModules()){
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
		}
		return logicalPath;
	}
	//TODO SEE IF CAN BE BETTER IMPLEMENTED yes we caaan :D 
	public long getParentModuleIdByChildId(long childModuleId) {
		long parentModuleId = -1L;
		
		if (rootModule.getId() == childModuleId){
			parentModuleId = -1;
		} else {
			for(Module module : rootModule.getSubModules()) {
				if (module.getId() == childModuleId) {
					parentModuleId = rootModule.getId();
				} else {
					if (module.hasSubModule(childModuleId)) {
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
	
	public ArrayList<Layer> getLayersBelow(Layer layer){
		ArrayList<Layer> returnList = new ArrayList<Layer>();
		Layer underlyingLayer = getTheFirstLayerBelow(layer);
		
		Layer _temp = underlyingLayer;
		while (getTheFirstLayerBelow(_temp).equals(null))
		{
			returnList.add(_temp);
			_temp = getTheFirstLayerBelow(_temp);
		}
		return returnList; //TODO!
	}
	
	public Layer getTheFirstLayerBelow(Layer layer)
	{
		return getTheFirstLayerBelow(layer.getHierarchicalLevel(),getParentModuleIdByChildId(layer.getId()));
	}
	
	public Layer getTheFirstLayerBelow(int currentHierarchicalLevel, long parentModuleId){
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
		ArrayList<Module> modulesToCheck = rootModule.getSubModules();
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

	public Module getRootModule() {
		return rootModule;
	}
}
