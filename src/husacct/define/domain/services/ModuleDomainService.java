package husacct.define.domain.services;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.ModuleComparator;

import java.util.ArrayList;
import java.util.Collections;

public class ModuleDomainService {
	
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

	public void removeModuleById(long moduleId) {
		Module module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		SoftwareArchitecture.getInstance().removeModule(module);
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
	
	public ArrayList<Long> getSubModuleIds(Long parentModuleId) {
		Module parentModule = SoftwareArchitecture.getInstance().getModuleById(parentModuleId);
		
		ArrayList<Long> moduleIdList = new ArrayList<Long>();
		for (Module module : parentModule.getSubModules()) {
			moduleIdList.add(module.getId());
			//get the submoduleIds of this submodule
			//recursive
			ArrayList<Long> subModuleIdList = getSubModuleIds(module.getId());
			for (Long l : subModuleIdList){
				moduleIdList.add(l);
			}
		}
		return moduleIdList;
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
	}
	
	public void moveLayerDown(long layerId){
		SoftwareArchitecture.getInstance().moveLayerDown(layerId);
	}
}
