package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.module.Module;
import husacct.define.domain.module.ModuleComparator;
import husacct.define.task.JtreeController;

import java.util.ArrayList;
import java.util.Collections;

public class ModuleDomainService {

    public long addModuleToParent(long parentModuleId, Module module) {
	Module parentModule = SoftwareArchitecture.getInstance().getModuleById(
		parentModuleId);
	parentModule.addSubModule(module);
	long moduleId = module.getId();

	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
	return moduleId;
    }

    public long addModuleToRoot(Module module) {
	long moduleId = SoftwareArchitecture.getInstance().addModule(module);

	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
	return moduleId;
    }

    public String addNewModuleToParent(long parentModuleId, Module module) {
	Module parentModule = SoftwareArchitecture.getInstance().getModuleById(
		parentModuleId);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
	return parentModule.addSubModule(module);
    }

    public Module getModuleById(long moduleId) {
	return SoftwareArchitecture.getInstance().getModuleById(moduleId);
    }

    public Module getModuleByLogicalPath(String logicalPath) {
	return SoftwareArchitecture.getInstance().getModuleByLogicalPath(
		logicalPath);
    }

    public Module getModuleIdBySoftwareUnit(SoftwareUnitDefinition su) {
	return SoftwareArchitecture.getInstance().getModuleBySoftwareUnit(
		su.getName());
    }

    public String getModuleNameById(long moduleId) {
	String moduleName = new String();
	if (moduleId != -1) {
	    moduleName = SoftwareArchitecture.getInstance()
		    .getModuleById(moduleId).getName();
	}
	return moduleName;
    }

    public Module getParentModule(Module module) {
	return recursiveSearch(SoftwareArchitecture.getInstance()
		.getRootModule(), module);
    }

    public Long getParentModuleIdByChildId(Long moduleId) {
	return SoftwareArchitecture.getInstance().getParentModuleIdByChildId(
		moduleId);
    }

    public Module getRootModule() {
	return SoftwareArchitecture.getInstance().getRootModule();
    }

    public Module[] getRootModules() {
	ArrayList<Module> moduleList = SoftwareArchitecture.getInstance()
		.getModules();
	Module[] modules = new Module[moduleList.size()];
	moduleList.toArray(modules);
	return modules;
    }

    public ArrayList<Long> getRootModulesIds() {
	ArrayList<Module> moduleList = SoftwareArchitecture.getInstance()
		.getModules();
	ArrayList<Long> moduleIdList = new ArrayList<Long>();
	for (Module module : moduleList) {
	    moduleIdList.add(module.getId());
	}
	return moduleIdList;
    }

    public ArrayList<Long> getSiblingModuleIds(long moduleId) {
	ArrayList<Long> childModuleIdList = new ArrayList<Long>();
	if (moduleId != -1) {
	    long parentModuleId = SoftwareArchitecture.getInstance()
		    .getParentModuleIdByChildId(moduleId);
	    childModuleIdList = getSubModuleIds(parentModuleId);

	    Module module = SoftwareArchitecture.getInstance().getModuleById(
		    moduleId);
	    childModuleIdList.remove(module.getId());
	}
	return childModuleIdList;
    }

    public ArrayList<Module> getSortedModules() {
	ArrayList<Module> modules = SoftwareArchitecture.getInstance()
		.getModules();
	Collections.sort(modules, new ModuleComparator());
	for (Module module : modules) {
	    sortModuleChildren(module);
	}
	return modules;
    }

    public ArrayList<Long> getSubModuleIds(Long parentModuleId) {
	ArrayList<Long> childModuleIdList = new ArrayList<Long>();

	if (parentModuleId != -1) {
	    Module parentModule = SoftwareArchitecture.getInstance()
		    .getModuleById(parentModuleId);

	    for (Module module : parentModule.getSubModules()) {
		childModuleIdList.add(module.getId());

		ArrayList<Long> subModuleIdList = getSubModuleIds(module
			.getId());
		for (Long l : subModuleIdList) {
		    childModuleIdList.add(l);
		}
	    }
	} else {
	    childModuleIdList = getRootModulesIds();
	}
	return childModuleIdList;
    }

    public void moveLayerDown(long layerId) {
	SoftwareArchitecture.getInstance().moveLayerDown(layerId);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void moveLayerUp(long layerId) {
	SoftwareArchitecture.getInstance().moveLayerUp(layerId);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    private Module recursiveSearch(Module currentModule,
	    Module comparrisonModule) {
	if (currentModule.equals(comparrisonModule)) {
	    return currentModule;
	}
	if (currentModule.hasSubModules()) {
	    for (Module subModule : currentModule.getSubModules()) {
		return recursiveSearch(subModule, comparrisonModule);
	    }
	}
	return new Module();
    }

    public void removeAllModules() {
	SoftwareArchitecture.getInstance().removeAllModules();
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void removeModuleById(long moduleId) {
	Module module = SoftwareArchitecture.getInstance().getModuleById(
		moduleId);

	SoftwareArchitecture.getInstance().removeModule(module);
	// TODO: this is a quick fix
	try {
	    JtreeController.instance().registerTreeRemoval(module);
	} catch (Exception e) {
	    // TODO: Add an exception scenario
	}
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void sortModuleChildren(Module module) {
	ArrayList<Module> children = module.getSubModules();
	Collections.sort(children, new ModuleComparator());
	for (Module child : children) {
	    sortModuleChildren(child);
	}
    }

    public void updateModule(long moduleId, String moduleName,
	    String moduleDescription) {
	Module module = SoftwareArchitecture.getInstance().getModuleById(
		moduleId);
	module.setName(moduleName);
	module.setDescription(moduleDescription);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();
    }

    public void updateModule(long moduleId, String moduleName,
	    String moduleDescription, String newType) {

	Module module = SoftwareArchitecture.getInstance().getModuleById(
		moduleId);
	DefaultRuleDomainService service = new DefaultRuleDomainService();
	service.removeDefaultRules(module);
	Module updatedModule = SoftwareArchitecture.getInstance()
		.updateModuleType(module, newType);
	service.addDefaultRules(updatedModule);
	service.updateModuleRules(updatedModule);
	ServiceProvider.getInstance().getDefineService()
		.notifyServiceListeners();

    }
}
