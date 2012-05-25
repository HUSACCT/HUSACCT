package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.ModuleComparator;

import java.util.ArrayList;
import java.util.Collections;

public class ModuleDomainService {
	
	public long addModuleToRoot(Module module){
		long moduleId = SoftwareArchitecture.getInstance().addModule(module);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		return moduleId;
	}
	
	public long addModuleToParent(long parentModuleId, Module module){
		Module parentModule = SoftwareArchitecture.getInstance().getModuleById(parentModuleId);
		parentModule.addSubModule(module);
		long moduleId = module.getId();
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		return moduleId;
	}	
	
	public void updateModule(long moduleId, String moduleName, String moduleDescription) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		module.setName(moduleName);
		module.setDescription(moduleDescription);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}

	public void removeModuleById(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		SoftwareArchitecture.getInstance().removeModule(module);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	public String getModuleNameById(long moduleId) {
		String moduleName;
		if (moduleId != -1){
			Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
			moduleName = module.getName();
		} else {
			moduleName = "";
		}
		return moduleName;
	}

	public Module getModuleById(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		return module;
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
	
	public ArrayList<Long> getSiblingModuleIds(long moduleId) {
		ArrayList<Long> childModuleIdList = new ArrayList<Long>();
		if (moduleId != -1) {
			long parentModuleId = SoftwareArchitecture.getInstance().getParentModuleIdByChildId(moduleId);
			childModuleIdList = getSubModuleIds(parentModuleId);
			
			Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
			childModuleIdList.remove(module.getId());
		}
		return childModuleIdList; 
	}
	
	public ArrayList<Long> getSubModuleIds(Long parentModuleId) {
		ArrayList<Long> childModuleIdList = new ArrayList<Long>();
		
		if (parentModuleId != -1) {
			Module parentModule = SoftwareArchitecture.getInstance().getModuleById(parentModuleId);
			
			for (Module module : parentModule.getSubModules()) {
				childModuleIdList.add(module.getId());
				//get the submoduleIds of this submodule
				//recursive
				ArrayList<Long> subModuleIdList = getSubModuleIds(module.getId());
				for (Long l : subModuleIdList){
					childModuleIdList.add(l);
				}
			}
		}else {
			childModuleIdList = getRootModulesIds();
		}
		return childModuleIdList;
	}
	
	
	public ArrayList<Module> getSortedModules() {
		ArrayList<Module> modules = SoftwareArchitecture.getInstance().getModules();
		Collections.sort(modules, new ModuleComparator());
		for(Module module : modules) {
			this.sortModuleChildren(module);
		}
		return modules;
	}
	
	public void sortModuleChildren(Module module) {
		ArrayList<Module> children = module.getSubModules();
		Collections.sort(children, new ModuleComparator());
		for(Module child : children) {
			this.sortModuleChildren(child);
		}
	}
	
	public Module getModuleByLogicalPath(String logicalPath){
		Module module = SoftwareArchitecture.getInstance().getModuleByLogicalPath(logicalPath);
		return module;
	}
	
	public void moveLayerUp(long layerId){
		SoftwareArchitecture.getInstance().moveLayerUp(layerId);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	public void moveLayerDown(long layerId){
		SoftwareArchitecture.getInstance().moveLayerDown(layerId);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}

	public Module getModuleIdBySoftwareUnit(SoftwareUnitDefinition su) {
		Module module = SoftwareArchitecture.getInstance().getModuleBySoftwareUnit(su.getName());
		return module;
	}

}
