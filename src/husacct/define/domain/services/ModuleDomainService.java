package husacct.define.domain.services;

import husacct.ServiceProvider;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.enums.ModuleTypes;
import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.ModuleComparator;
import husacct.define.domain.module.ModuleFactory;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.module.modules.Layer;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition;
import husacct.define.domain.softwareunit.SoftwareUnitDefinition.Type;
import husacct.define.task.JtreeController;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.log4j.Logger;

public class ModuleDomainService {

	private ModuleFactory factory = new ModuleFactory();
	private final Logger logger = Logger.getLogger(ModuleDomainService.class);
	
	public ModuleDomainService() {
		// Many instances are created by clients.
	}

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
		long parentModuleId = getModuleByLogicalPath(parentLogicalPath).getId();
		message = addModuleToParent(parentModuleId, newModule);
		if (message.equals("")) {
			logger.info(" Module added with name: " + newModule.getName() + ", Type: " + newModule.getType() + ", Assigned units: " + newModule.countSoftwareUnits());
			return newModule;
		} else {
			logger.info(" Module not added with name: " + name + ", Type: " + moduleType);
			return null;
		}
	}
	
	/**  
	 * Edits module based on the arguments
	 * Note: This method is called in the course of SAR, so no error messages will be displayed in the GUI if an error occurs.
	 * @return Null, if the he module could not be found and edited with success, else a ModuleStrategy is returned.
	 */
	public ModuleStrategy editModule(String logicalPath, String newType, String newName, int newHierarchicalLevel, ArrayList<SoftwareUnitDTO> newSoftwareUnits) {
		ModuleStrategy moduleToEdit = null;
		try{
			moduleToEdit = getModuleByLogicalPath(logicalPath);
			if ((moduleToEdit != null) && (moduleToEdit.getId() >= 0)) {
				if (newType != null)
					updateModuleType(moduleToEdit, newType);
				if (newName != null)
					updateModuleName(moduleToEdit, newName);
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
		return message;
	}

	public ModuleStrategy createNewModule(String type) {
		ModuleStrategy result =	factory.createModule(type);
		return result;
	}

	public ModuleStrategy getModuleById(long moduleId) {
		return SoftwareArchitecture.getInstance().getModuleById(moduleId);
	}

	/** 
	 * Finds the ModuleStrategy with the given logicalPath.
	 * @param logicalPath
	 * @return ModuleStrategy of the found module, or a dummy-module with moduleId = -1 and type = "blank".
	 */
	public ModuleStrategy getModuleByLogicalPath(String logicalPath) {
		SoftwareArchitecture softwareArchitecture = SoftwareArchitecture.getInstance();
		ModuleStrategy foundModule = new ModuleFactory().createDummy("blank");
		ModuleStrategy currentModule = null;
		if ((logicalPath == null) || (logicalPath.equals(""))){
		} else {
			if (logicalPath.equals("**")) {
				currentModule = softwareArchitecture.getRootModule();
			} else {
				String[] moduleNames = logicalPath.split("\\.");
				int i = 0;
				for (ModuleStrategy module : softwareArchitecture.getRootModule().getSubModules()) {
					if (module.getName().equals(moduleNames[i])) {
						currentModule = module;
						if (moduleNames.length > 1) {
							for (int j = 1; j < moduleNames.length; j++) {
								for (ModuleStrategy subModule : currentModule.getSubModules()) {
									if (subModule.getName().equals(moduleNames[j])) {
										currentModule = subModule;
									}
								}
							}
						}
					}
				}
				String moduleName_notUnique =  moduleNames[moduleNames.length - 1];
				if (currentModule != null && currentModule.getName().equals(moduleName_notUnique)) {
					foundModule = currentModule;
				}
			}
		}
		return foundModule;

		/*		ModuleStrategy module;
		module = SoftwareArchitecture.getInstance().getModuleByLogicalPath(logicalPath);
		return module;
*/
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
		SoftwareArchitecture.getInstance().moveLayerDown(layerId);
		ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
	}

	public void moveLayerUp(long layerId) {
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

	/**
	 * Updates name, description and/or type of a module. Arguments with null value are not processed. 
	 * @param moduleId
	 * @param newModuleName
	 * @param newModuleDescription
	 * @param newModuleType
	 */
	public void updateModuleDetails(long moduleId, String newModuleName, String newModuleDescription, String newModuleType) {
		ModuleStrategy module = SoftwareArchitecture.getInstance().getModuleById(moduleId);
		if ((module != null) && (module.getId() > 0)) {
			if (newModuleName != null) 
				updateModuleName(module, newModuleName);
			if (newModuleDescription != null)
				module.setDescription(newModuleDescription);
			if ((newModuleType != null) && (newModuleType.toString() != module.getType()))
				updateModuleType(module, newModuleType);
			ServiceProvider.getInstance().getDefineService().notifyServiceListeners();
		}
	}
	
	private void updateModuleType(ModuleStrategy module, String newType) {
		if((module != null) && (module.getId() > 0)){
			if (module.getType() != newType){
				DefaultRuleDomainService service = new DefaultRuleDomainService();
				service.removeDefaultRules(module);
				ModuleStrategy updatedModule = SoftwareArchitecture.getInstance().updateModuleType(module, newType);
				service.addDefaultRules(updatedModule);
				service.updateModuleRules(updatedModule);
			}
		}
	}
	
	private void updateModuleName(ModuleStrategy module, String newModuleName){
		if (module != null) {
			module.setName(newModuleName);
			if (module.getType().equals(ModuleTypes.COMPONENT.toString())) {
				for(ModuleStrategy subModule : module.getSubModules()){
					if(subModule.getType().equals(ModuleTypes.FACADE.toString())){
						subModule.setName("Interface<"+newModuleName+">");
					}
				}
			}
		}
	}
}
