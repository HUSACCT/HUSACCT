package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.ModuleComparator;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.task.JtreeController;

import java.util.ArrayList;
import java.util.Collections;

public class ModuleDomainService {

	private ModuleFactory factory = new ModuleFactory();

	public long addModuleToParent(long parentModuleId, ModuleStrategy module) {
        SoftwareArchitecture.getInstance().addModule(parentModuleId, module);
		StateService.instance().addModule(module);
		long moduleId = module.getId();
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		return moduleId;
	}


	public long addModuleToRoot(ModuleStrategy module) {
		long moduleId = SoftwareArchitecture.getInstance().addModule(module);
		StateService.instance().addModule(module);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		return moduleId;
	}

	public String addNewModuleToParent(long parentModuleId,	ModuleStrategy module) {
		StateService.instance().addModule(module);
		ServiceProvider.getInstance().getDefineService()
				.notifyServiceListeners();
	
		return SoftwareArchitecture.getInstance().addModule(parentModuleId,module);
	}
	
	public ModuleStrategy createNewModule(String type) {
		ModuleStrategy result=	factory.createModule(type);
		return result;
	}

	public ModuleStrategy getModuleById(long moduleId) {
		return SoftwareArchitecture.getInstance().getModuleById(moduleId);
	}

	public ModuleStrategy getModuleByLogicalPath(String logicalPath) {
		return SoftwareArchitecture.getInstance().getModuleByLogicalPath(
				logicalPath);
	}

	public ModuleStrategy getModuleIdBySoftwareUnit(SoftwareUnitDefinition su) {
		return SoftwareArchitecture.getInstance().getModuleBySoftwareUnit(
				su.getName());
	}
	
	public ModuleStrategy getModuleByName(String name){
		return SoftwareArchitecture.getInstance().getModuleByName(name);
	}

	public String getModuleNameById(long moduleId) {
		String moduleName = new String();
		if (moduleId != -1) {
			moduleName = SoftwareArchitecture.getInstance()
					.getModuleById(moduleId).getName();
		}
		return moduleName;
	}

	public ModuleStrategy getParentModule(ModuleStrategy module) {
		return module.getparent();
	}

	public Long getParentModuleIdByChildId(Long moduleId) {
		return SoftwareArchitecture.getInstance().getParentModuleIdByChildId(
				moduleId);
	}

	public ModuleStrategy getRootModule() {
		return SoftwareArchitecture.getInstance().getRootModule();
	}

	public ModuleStrategy[] getRootModules() {
		ArrayList<ModuleStrategy> moduleList = SoftwareArchitecture
				.getInstance().getModules();
		ModuleStrategy[] modules = new ModuleStrategy[moduleList.size()];
		moduleList.toArray(modules);
		return modules;
	}

	public ArrayList<Long> getRootModulesIds() {
		ArrayList<ModuleStrategy> moduleList = SoftwareArchitecture
				.getInstance().getModules();
		ArrayList<Long> moduleIdList = new ArrayList<Long>();
		for (ModuleStrategy module : moduleList) {
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

			ModuleStrategy module = SoftwareArchitecture.getInstance()
					.getModuleById(moduleId);
			childModuleIdList.remove(module.getId());
		}
		return childModuleIdList;
	}

	public ArrayList<ModuleStrategy> getSortedModules() {
		ArrayList<ModuleStrategy> modules = SoftwareArchitecture.getInstance()
				.getModules();
		Collections.sort(modules, new ModuleComparator());
		for (ModuleStrategy module : modules) {
			sortModuleChildren(module);
		}
		return modules;
	}

	public ArrayList<Long> getSubModuleIds(Long parentModuleId) {
		ArrayList<Long> childModuleIdList = new ArrayList<Long>();

		if (parentModuleId != -1) {
			ModuleStrategy parentModule = SoftwareArchitecture.getInstance()
					.getModuleById(parentModuleId);

			for (ModuleStrategy module : parentModule.getSubModules()) {
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
		StateService.instance().layerDown(layerId);
		SoftwareArchitecture.getInstance().moveLayerDown(layerId);
		ServiceProvider.getInstance().getDefineService()
				.notifyServiceListeners();
	}

	public void moveLayerUp(long layerId) {
		StateService.instance().layerUp(layerId);
		SoftwareArchitecture.getInstance().moveLayerUp(layerId);
		ServiceProvider.getInstance().getDefineService()
				.notifyServiceListeners();
	}

	public void removeAllModules() {
		SoftwareArchitecture.getInstance().removeAllModules();
		ServiceProvider.getInstance().getDefineService()
				.notifyServiceListeners();
	}

	public void removeModuleById(long moduleId) {
		ModuleStrategy module = SoftwareArchitecture.getInstance()
				.getModuleById(moduleId);
      try{
		SoftwareArchitecture.getInstance().removeModule(module);
	    JtreeController.instance().registerTreeRemoval(module);
      }catch(Exception e)
      {
    	  e.printStackTrace();
      }
		ServiceProvider.getInstance().getDefineService()
				.notifyServiceListeners();
	}

	public void sortModuleChildren(ModuleStrategy module) {
		ArrayList<ModuleStrategy> children = module.getSubModules();
		Collections.sort(children, new ModuleComparator());
		for (ModuleStrategy child : children) {
			sortModuleChildren(child);
		}
	}

	public void updateModule(long moduleId, String moduleName, String moduleDescription) {
		ModuleStrategy module = SoftwareArchitecture.getInstance()
				.getModuleById(moduleId);
		StateService.instance().addUpdateModule(moduleId,
				new String[] { module.getName(), moduleDescription },
				new String[] { moduleName, moduleDescription });
		module.setName(moduleName);
		module.setDescription(moduleDescription);
		ServiceProvider.getInstance().getDefineService()
				.notifyServiceListeners();
	}
	
	public void updateModuleType(long moduleId, String moduleName, String moduleDescription, String newType) {

		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		if(module.getId() > 0){
			DefaultRuleDomainService service = new DefaultRuleDomainService();
			service.removeDefaultRules(module);
			ModuleStrategy updatedModule = SoftwareArchitecture.getInstance().updateModuleType(module, newType);
			service.addDefaultRules(updatedModule);
			service.updateModuleRules(updatedModule);
			StateService.instance().addUpdateModule(module, updatedModule);
		}
		ServiceProvider.getInstance().getDefineService()
				.notifyServiceListeners();

	}
	
	public void updateFacade(long moduleId, String moduleName){
		ModuleStrategy parent = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		for(ModuleStrategy subModule : parent.getSubModules()){
			if(subModule.getType().equals("Facade")){
				subModule.setName("Facade<"+moduleName+">");
			}
		}
	}
	
}
