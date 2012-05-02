package husacct.define.domain;

import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;

import java.util.ArrayList;


public class DefineDomainService {
	private static DefineDomainService instance = null;
	public static DefineDomainService getInstance() {
		return instance == null ? (instance = new DefineDomainService()) : instance;
	}
	
	public DefineDomainService() {
		
	}
	
	//GENERIC
	public void createNewArchitectureDefinition(String name) {
		SoftwareArchitecture.getInstance().setName(name);
	}
	
	/**
	 * Modules
	 */
	public long addModuleToRoot(Module module){
		long moduleId = SoftwareArchitecture.getInstance().addModule(module);
		return moduleId;
	}
	
	public long addModuleToParent(long parentModuleId, Module module){
		Module parentModule = SoftwareArchitecture.getInstance().getModuleById(parentModuleId);
		parentModule.addSubModule(module);
		long moduleId = module.getId();
		return moduleId;
	}	
	
	public void updateModule(long moduleId, String moduleName, String moduleDescription) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		module.setName(moduleName);
		module.setDescription(moduleDescription);
	}
	
	public String getModuleNameById(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		String moduleName = module.getName();
		return moduleName;
	}

	public Module getModuleById(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		return module;
	}

	public void removeModuleById(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		SoftwareArchitecture.getInstance().removeModule(module);
	}
	
	public Module[] getRootModules(){
		ArrayList<Module> moduleList = SoftwareArchitecture.getInstance().getModules();
		Module[] modules = new Module[moduleList.size()]; moduleList.toArray(modules);
		return modules;
	}
	
	public ArrayList<Long> getRootModulesIds(){
		ArrayList<Module> moduleList = SoftwareArchitecture.getInstance().getModules();
		ArrayList<Long> moduleIdList = new ArrayList<Long>();
		for (Module module : moduleList) {
			moduleIdList.add(module.getId());
		}
		return moduleIdList;
	}
	
	public Module getModuleByLogicalPath(String logicalPath){
		Module module = SoftwareArchitecture.getInstance().getModuleByLogicalPath(logicalPath);
		return module;
	}
	
	/**
	 * Layers
	 */
	public long addLayer(long parentModuleId, String name, int level) {
		long moduleId = -1;

		Module layer = new Layer(name, level);
		((Layer) layer).setHierarchicalLevel(level);
		
		if (parentModuleId == -1){
			moduleId = addModuleToRoot(layer);
		}else {
			moduleId = addModuleToParent(parentModuleId, layer);
		}
		return moduleId;
	}
	
	public void setModuleName(long moduleId, String newName) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		module.setName(newName);
	}

	public void moveLayerUp(long layerId){
		SoftwareArchitecture.getInstance().moveUpDown(layerId);
	}
	
	public void moveLayerDown(long layerId){
		SoftwareArchitecture.getInstance().moveLayerDown(layerId);
	}
	
	/**
	 * Applied Rule
	 */
	public AppliedRule[] getAppliedRules() {
		ArrayList<AppliedRule> ruleList = SoftwareArchitecture.getInstance().getAppliedRules();
		AppliedRule[] rules = new AppliedRule[ruleList.size()]; ruleList.toArray(rules);
		return rules;
	}
	
	public long addAppliedRule(String ruleTypeKey, String description, String[] dependencies,
			String regex, long moduleFromId, long moduleToId, boolean enabled) {
		Module moduleFrom = SoftwareArchitecture.getInstance().getModuleById(moduleFromId);
		Module moduleTo = SoftwareArchitecture.getInstance().getModuleById(moduleToId);

		AppliedRule rule = new AppliedRule(ruleTypeKey,description,dependencies,regex, moduleFrom, moduleTo, enabled);
		SoftwareArchitecture.getInstance().addAppliedRule(rule);
		return rule.getId();
	}
	
	public void updateAppliedRule(long appliedRuleId, String ruleTypeKey,String description, String[] dependencies, 
			String regex,long moduleFromId, long moduleToId, boolean enabled) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		rule.setRuleType(ruleTypeKey);
		rule.setDescription(description);
		rule.setDependencies(dependencies);
		rule.setRegex(regex);
		Module moduleFrom = SoftwareArchitecture.getInstance().getModuleById(moduleFromId);
		Module moduleTo = SoftwareArchitecture.getInstance().getModuleById(moduleToId);
		rule.setRestrictedModule(moduleFrom);
		rule.setUsedModule(moduleTo);
		rule.setEnabled(enabled);
	}
	
	
	public void removeAppliedRule(long appliedrule_id) {
		SoftwareArchitecture.getInstance().removeAppliedRule(appliedrule_id);
	}

	public String getRuleTypeByAppliedRule(long appliedruleId) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedruleId);
		String ruleTypeKey = rule.getRuleType();
		return ruleTypeKey;
	}

	public void setAppliedRuleIsEnabled(long appliedRuleId, boolean enabled) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		rule.setEnabled(enabled);
	}
	
	public ArrayList<Long> getAppliedRulesIdsByModule(long moduleId) {
		return SoftwareArchitecture.getInstance().getAppliedRulesIdsByModule(moduleId);
	}

	public long getModuleToIdOfAppliedRule(long appliedRuleId) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		Long moduleToId = rule.getRestrictedModule().getId();
		return moduleToId;
	}
	
	public boolean getAppliedRuleIsEnabled(long appliedRuleId) {
		AppliedRule rule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		boolean isEnabled = rule.isEnabled();
		return isEnabled;
	}

	/**
	 * Applied Rule Exception
	 */
	public void addExceptionToAppliedRule(long parentRuleId, String ruleType, String description, long moduleFromId, long moduleToId) {
		Module moduleFrom = SoftwareArchitecture.getInstance().getModuleById(moduleFromId);
		Module moduleTo = SoftwareArchitecture.getInstance().getModuleById(moduleToId);

		AppliedRule exceptionRule = new AppliedRule(ruleType,description, moduleFrom, moduleTo);
		
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		parentRule.addException(exceptionRule);
	}
	
	public void removeAppliedRuleException(long parentRuleId, long exceptionRuleId) {
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		parentRule.removeExceptionById(exceptionRuleId);
	}

	public void removeAllAppliedRuleExceptions(long appliedRuleId) {
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(appliedRuleId);
		parentRule.removeAllExceptions();
	}

	public ArrayList<Long> getExceptionIdsByAppliedRule(long parentRuleId) {
		AppliedRule parentRule = SoftwareArchitecture.getInstance().getAppliedRuleById(parentRuleId);
		ArrayList<AppliedRule> exceptionRules = parentRule.getExceptions();
		ArrayList<Long> exceptionIds = new ArrayList<Long>();
		for (AppliedRule exception : exceptionRules){
			exceptionIds.add(exception.getId());
		}
		return exceptionIds;
	}
	
	/**
	 * Software Unit Definitions
	 */
	public ArrayList<String> getSoftwareUnitNames(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();
		ArrayList<String> softwareUnitNames = new ArrayList<String>();
		for (SoftwareUnitDefinition unit : softwareUnits){
			softwareUnitNames.add(unit.getName());
		}
		return softwareUnitNames;
	}
	
	public ArrayList<SoftwareUnitDefinition> getSoftwareUnit(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		ArrayList<SoftwareUnitDefinition> softwareUnits = module.getUnits();
		return softwareUnits;
	}
	
	public String getSoftwareUnitType(String softwareUnitName) {
		SoftwareUnitDefinition unit = SoftwareArchitecture.getInstance().getSoftwareUnitByName(softwareUnitName);
		String softwareUnitType = unit.getType().toString();
		return softwareUnitType;
	}

	public void addSoftwareUnit(long moduleId, String softwareUnit, String t) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		//TODO add try catch fopr type
		Type type = Type.valueOf(t);
		SoftwareUnitDefinition unit = new SoftwareUnitDefinition(softwareUnit, type);
		module.addSUDefinition(unit);
	}
	
	public void removeSoftwareUnit(long moduleId, String softwareUnit) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		SoftwareUnitDefinition unit = SoftwareArchitecture.getInstance().getSoftwareUnitByName(softwareUnit);
		module.removeSUDefintion(unit);
	}

	
	/**
	 * Application
	 */
	public void createApplication(String name, String[] paths, String language, String version) {
			Application app = new Application(name, paths, language, version);
			Application.setInstance(app);	
	}
	
	public Application getApplicationDetails(){
		return Application.getInstance();
	}

}
