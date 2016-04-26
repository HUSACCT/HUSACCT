package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.ModuleComparator;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition.Type;
import husacct.define.task.JtreeController;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

public class ModuleDomainService {

	private ModuleFactory factory = new ModuleFactory();
	private final Logger logger = Logger.getLogger(ModuleDomainService.class);

	/**  
	 * Adds module based on the arguments
	 * Note: This method is called in the course of SAR, so no error messages will be displayed in the GUI if an error occurs.
	 * @return Null, if the he module could not be added with success, else a ModuleStrategy is returned.
	 */
	public ModuleStrategy addModule(String name, String parentLogicalPath, String moduleType, int hierarchicalLevel, ArrayList<SoftwareUnitDTO> softwareUnits) {
		String message = "";
		// 1) Create new module
		ModuleStrategy newModule;
		switch (moduleType) {
		case "ExternalLibrary":
			newModule = createNewModule("ExternalLibrary");
			break;
		case "Component":
			newModule = createNewModule("Component");
			break;
		case "Facade":
			newModule = createNewModule("Facade");
			break;
		case "SubSystem":
			newModule = createNewModule("SubSystem");
			break;
		case "Layer":
			newModule = createNewModule("Layer");
			((Layer) newModule).setHierarchicalLevel(hierarchicalLevel);
			break;
		default:
			newModule = createNewModule("SubSystem");
			break;
		}

		// 2) Set attributes
		newModule.set(name, "");
		if (softwareUnits != null) {
			for (SoftwareUnitDTO softwareUnit : softwareUnits) {
				Type softwareUnitDefinitionType = Type.SUBSYSTEM;
				if (softwareUnit.type.toUpperCase().equals("CLASS")) {
					softwareUnitDefinitionType = Type.CLASS;
				} else if (softwareUnit.type.toUpperCase().equals("INTERFACE")) {
					softwareUnitDefinitionType = Type.INTERFACE;
				} else if (softwareUnit.type.toUpperCase().equals("EXTERNALLIBRARY")) {
					softwareUnitDefinitionType = Type.EXTERNALLIBRARY;
				} else if (softwareUnit.type.toUpperCase().equals("LIBRARY")) {
					softwareUnitDefinitionType = Type.LIBRARY;
				} else if (softwareUnit.type.toUpperCase().equals("PACKAGE")) {
					softwareUnitDefinitionType = Type.PACKAGE;
				}
				newModule.addSUDefinition(new SoftwareUnitDefinition(softwareUnit.uniqueName, softwareUnitDefinitionType));
			}
		}
		
		// 3) Add module to parent
		long parentModuleId = SoftwareArchitecture.getInstance().getModuleByLogicalPath(parentLogicalPath).getId();
		message = addModuleToParent(parentModuleId, newModule);

		if (message.equals("")) {
			logger.info(" Module added with name: " + newModule.getName() + ", Type: " + newModule.getType() + ", Assigned units: " + newModule.countSoftwareUnits());
			return newModule;
		} else {
			logger.info(" Module not added with name: " + name + ", Type: " + moduleType);
			return null;
		}
	}
	
	public ModuleStrategy editModule(String logicalPath, String newType, String newName, int newHierarchicalLevel, ArrayList<SoftwareUnitDTO> newSoftwareUnits) {
		ModuleStrategy moduleToEdit = null;
		try{
			moduleToEdit = getModuleByLogicalPath(logicalPath);
			if (moduleToEdit != null) {
				if (newType != null)
					updateModuleType(moduleToEdit.getId(), newType);
				if (newName != null)
					moduleToEdit.setName(newName);
				if ((newHierarchicalLevel != 0) && (moduleToEdit instanceof Layer))
					((Layer) moduleToEdit).setHierarchicalLevel(newHierarchicalLevel);
				if (newSoftwareUnits != null) {
					moduleToEdit.removeAllSUDefintions();
					for (SoftwareUnitDTO softwareUnit : newSoftwareUnits) {
						Type softwareUnitDefinitionType = Type.SUBSYSTEM;
						if (softwareUnit.type.toUpperCase().equals("CLASS")) {
							softwareUnitDefinitionType = Type.CLASS;
						} else if (softwareUnit.type.toUpperCase().equals("INTERFACE")) {
							softwareUnitDefinitionType = Type.INTERFACE;
						} else if (softwareUnit.type.toUpperCase().equals("EXTERNALLIBRARY")) {
							softwareUnitDefinitionType = Type.EXTERNALLIBRARY;
						} else if (softwareUnit.type.toUpperCase().equals("LIBRARY")) {
							softwareUnitDefinitionType = Type.LIBRARY;
						} else if (softwareUnit.type.toUpperCase().equals("PACKAGE")) {
							softwareUnitDefinitionType = Type.PACKAGE;
						}
						moduleToEdit.addSUDefinition(new SoftwareUnitDefinition(softwareUnit.uniqueName, softwareUnitDefinitionType));
					}
				}
			}
	    } catch(Exception e) {
	        logger.error(e);
	    }
		return moduleToEdit;
	}
	
	public String addModuleToParent(long parentModuleId, ModuleStrategy module) {
		String message = "";
		if (parentModuleId <= 0) {
			message = SoftwareArchitecture.getInstance().addModuleToRoot(module);
		} else {
			message = SoftwareArchitecture.getInstance().addModuleToParent(parentModuleId, module);
		}
		if (message.equals("")) {
			StateService.instance().addModule(module);
			ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		}
		return message;
	}

	public ModuleStrategy createNewModule(String type) {
		ModuleStrategy result =	factory.createModule(type);
		return result;
	}

	public ModuleStrategy getModuleById(long moduleId) {
		return SoftwareArchitecture.getInstance().getModuleById(moduleId);
	}

	// Returns the ModuleStrategy of the found module, or throws an exception if no module is found.
	public ModuleStrategy getModuleByLogicalPath(String logicalPath) {
		return SoftwareArchitecture.getInstance().getModuleByLogicalPath(logicalPath);
	}

	// Returns the ModuleStrategy of the found module, or null if no module is found.
	public ModuleStrategy getModuleByLogicalPath_NoException(String logicalPath) {
		ModuleStrategy foundModule;
		try {
			foundModule = SoftwareArchitecture.getInstance().getModuleByLogicalPath(logicalPath);
	    } catch (Exception e) {
	    	foundModule = null;
	    }
	    return foundModule;
	}

	public ModuleStrategy getModuleIdBySoftwareUnit(SoftwareUnitDefinition su) {
		return SoftwareArchitecture.getInstance().getModuleBySoftwareUnit(su.getName());
	}
	
	public String getModuleNameById(long moduleId) {
		String moduleName = new String();
		if (moduleId != -1) {
			ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
			if (module != null) {
				moduleName = module.getName();
			}
		}
		return moduleName;
	}

	public ModuleStrategy getParentModule(ModuleStrategy module) {
		return module.getparent();
	}

	public Long getParentModuleIdByChildId(Long moduleId) {
		return SoftwareArchitecture.getInstance().getParentModuleIdByChildId(moduleId);
	}

	public ModuleStrategy getRootModule() {
		return SoftwareArchitecture.getInstance().getRootModule();
	}

	public ModuleStrategy[] getRootModules() {
		ArrayList<ModuleStrategy> moduleList = SoftwareArchitecture.getInstance().getModules();
		ModuleStrategy[] modules = new ModuleStrategy[moduleList.size()];
		moduleList.toArray(modules);
		return modules;
	}

	public ArrayList<Long> getRootModulesIds() {
		ArrayList<ModuleStrategy> moduleList = SoftwareArchitecture.getInstance().getModules();
		ArrayList<Long> moduleIdList = new ArrayList<Long>();
		for (ModuleStrategy module : moduleList) {
			moduleIdList.add(module.getId());
		}
		return moduleIdList;
	}

	public ArrayList<Long> getSiblingModuleIds(long moduleId) {
		ArrayList<Long> childModuleIdList = new ArrayList<Long>();
		if (moduleId != -1) {
			ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
			if (module != null) {
				long parentModuleId = SoftwareArchitecture.getInstance().getParentModuleIdByChildId(moduleId);
				childModuleIdList = getSubModuleIds(parentModuleId);
				childModuleIdList.remove(module.getId());
			}
		}
		return childModuleIdList;
	}

	public ArrayList<ModuleStrategy> getSortedModules() {
		ArrayList<ModuleStrategy> modules = SoftwareArchitecture.getInstance().getModules();
		Collections.sort(modules, new ModuleComparator());
		for (ModuleStrategy module : modules) {
			sortModuleChildren(module);
		}
		return modules;
	}

	public ArrayList<Long> getSubModuleIds(Long parentModuleId) {
		ArrayList<Long> childModuleIdList = new ArrayList<Long>();
		if (parentModuleId != -1) {
			ModuleStrategy parentModule = SoftwareArchitecture.getInstance().getModuleById(parentModuleId);
			if (parentModule != null) {
				for (ModuleStrategy module : parentModule.getSubModules()) {
					childModuleIdList.add(module.getId());
					ArrayList<Long> subModuleIdList = getSubModuleIds(module.getId());
					for (Long l : subModuleIdList) {
						childModuleIdList.add(l);
					}
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
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}

	public void moveLayerUp(long layerId) {
		StateService.instance().layerUp(layerId);
		SoftwareArchitecture.getInstance().moveLayerUp(layerId);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}

	public void removeAllModules() {
		SoftwareArchitecture.getInstance().removeAllModules();
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}

	public void removeModuleById(long moduleId) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		if (module != null) {
			try{
				SoftwareArchitecture.getInstance().removeModule(module);
				JtreeController.instance().registerTreeRemoval(module);
			}catch(Exception e) {
				logger.error(e);
			}
			ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		}
	}

	public void sortModuleChildren(ModuleStrategy module) {
		ArrayList<ModuleStrategy> children = module.getSubModules();
		Collections.sort(children, new ModuleComparator());
		for (ModuleStrategy child : children) {
			sortModuleChildren(child);
		}
	}

	public void updateModule(long moduleId, String moduleName, String moduleDescription) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		if (module != null) {
			StateService.instance().addUpdateModule(moduleId, new String[] { module.getName(), moduleDescription },
					new String[] { moduleName, moduleDescription });
			module.setName(moduleName);
			module.setDescription(moduleDescription);
			ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		}
	}
	
	public void updateModuleType(long moduleId, String newType) {
		ModuleStrategy oldModule = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		if((oldModule != null) && (oldModule.getId() > 0)){
			if (oldModule.getType() != newType){
				DefaultRuleDomainService service = new DefaultRuleDomainService();
				service.removeDefaultRules(oldModule);
				ModuleStrategy updatedModule = SoftwareArchitecture.getInstance().updateModuleType(oldModule, newType);
				service.addDefaultRules(updatedModule);
				service.updateModuleRules(updatedModule);
				StateService.instance().addUpdateModule(oldModule, updatedModule);
			}
		}
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}
	
	public void updateFacade(long moduleId, String moduleName){
		ModuleStrategy parent = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		if (parent != null) {
			for(ModuleStrategy subModule : parent.getSubModules()){
				if(subModule.getType().equals("Facade")){
					subModule.setName("Interface<"+moduleName+">");
				}
			}
		}
	}
}
