package husacct.analyse.task.reconstruct.components.HUSACCT;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.enums.DependencySubTypes;
import husacct.common.enums.ModuleTypes;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;

public class ComponentsHUSACCT_SelectedModule extends AlgorithmHUSACCT{
	private IModelQueryService queryService;
	private IDefineService defineService;
	private IDefineSarService defineSarService;
	private final Logger logger = Logger.getLogger(ComponentsHUSACCT_SelectedModule.class);

	ModuleDTO selectedModule;
	String selectedModuleUniqueName;
	ArrayList<String> softwareUnitsInSelectedModuleList = new ArrayList<String>();
	private HashMap<String,ArrayList<SoftwareUnitDTO>> componentsWithInterfaces = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
	private HashMap<String,ArrayList<SoftwareUnitDTO>> componentsWithImplementingClasses = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
	private HashMap<String,ArrayList<SoftwareUnitDTO>> exceptionsPerSoftwareUnit = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
	private HashMap<String,ArrayList<SoftwareUnitDTO>> enumsPerSoftwareUnit = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
	
	
	public ComponentsHUSACCT_SelectedModule(){
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
	}
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService, String xLibrariesRootPackage) {
		this.queryService = queryService;
		identifyComponentsInRootOfSelectedModule(dto.getSelectedModule());
		createComponentsAndOrSubSystems();
	}
	/**
	 * Determines for each Software Unit (SU) assigned to the selected module if it is a Component or a Subsystem.
	 * a) Identifies an assigned SoftwareUnit as a Component, if (one of) the SoftwareUnit's direct children implement a unit-specific interface.
	 * b) In case there is one assigned SoftwareUnit only:
	 * b1) If selectedModule is the root or a Layer and the assigned SU proofs to be a component, then a component + interface is added. 
	 * b2) If selectedModule is a Component, the algorithm is applied on the first set of children of the assigned SU.
	 * b3) If selectedModule is a SubSytem, the algorithm is applied on the first set of children of the assigned SU.
	 * @param selectedModuleArgument
	 */
	public void identifyComponentsInRootOfSelectedModule(ModuleDTO selectedModuleArgument) {
		this.selectedModule = selectedModuleArgument;
		try {
			// If the selectedModule is of type Facade or ExternalLibrary, nothing is done.
			if ((selectedModule == null) || selectedModule.type.equals(ModuleTypes.EXTERNAL_LIBRARY.toString()) || selectedModule.type.equals(ModuleTypes.FACADE.toString())) {
				return;
			}
			selectedModuleUniqueName = selectedModule.logicalPath;
			if (selectedModuleUniqueName.equals("")) {
				selectedModuleUniqueName = "**"; // Root of intended software architecture
			}
			softwareUnitsInSelectedModuleList = new ArrayList<String>();

			// 1) Select the first set of SoftwareUnits in the decomposition hierarchy of the selected module.  
			softwareUnitsInSelectedModuleList.addAll(defineService.getAssignedSoftwareUnitsOfModule(selectedModuleUniqueName));
			if (softwareUnitsInSelectedModuleList.size() == 1) {
				if (selectedModule.type.equals(ModuleTypes.COMPONENT.toString()) || selectedModule.type.equals(ModuleTypes.SUBSYSTEM.toString())) {
					softwareUnitsInSelectedModuleList = getSetOfChildSoftwareUnits(softwareUnitsInSelectedModuleList.get(0));
				}
			}
			for (String softwareUnitInSelectedModule : softwareUnitsInSelectedModuleList) {
				for (SoftwareUnitDTO softwareUnit : queryService.getChildUnitsOfSoftwareUnit(softwareUnitInSelectedModule)) {
					if (softwareUnit.type.equals("class")) {
						// 2) Get all dependencies on softwareUnit
						ArrayList<DependencyDTO> dependenciesList = new ArrayList<DependencyDTO>();
						Collections.addAll(dependenciesList, queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, ""));
						// 3) Determine if the software unit implement an interface	
						for(DependencyDTO dependency : dependenciesList){		
							if(dependency.subType.equals(DependencySubTypes.INH_IMPLEMENTS_INTERFACE.toString())){
								// 4) Get the used interface.
								SoftwareUnitDTO interfaceClass = queryService.getSoftwareUnitByUniqueName(dependency.to);
								// Check if the interface justifies the identification of a Component.
								boolean interfaceIsSpecificForUnit = isInterfaceSpecificForUnit(interfaceClass);
								boolean isInterfaceOfParent = isInterfaceTheInterfaceOfTheParentModule(interfaceClass, selectedModule);
								if (interfaceIsSpecificForUnit && !isInterfaceOfParent) {
									// The softwareUnitInSelectedModule is identified as a Component
									// Relate the interface with the parent package of the implementing class. A package can implement several interfaces.
									componentsWithInterfaces = addSoftwareUnitToHashMap(interfaceClass, softwareUnitInSelectedModule, componentsWithInterfaces);
									// Add softwareUnit to implementingClassesPerPackage, since it implements an interfaces.
									componentsWithImplementingClasses = addSoftwareUnitToHashMap(softwareUnit, softwareUnitInSelectedModule, componentsWithImplementingClasses);
								}
							}
							if(dependency.subType.equals(DependencySubTypes.DECL_EXCEPTION.toString())){
								SoftwareUnitDTO exceptionClass = queryService.getSoftwareUnitByUniqueName(dependency.to);
								exceptionsPerSoftwareUnit = addSoftwareUnitToHashMap(exceptionClass, softwareUnitInSelectedModule, exceptionsPerSoftwareUnit);
							}
							if(dependency.subType.equals(DependencySubTypes.ACC_ENUMERATION_VAR.toString())){
								SoftwareUnitDTO enumClass = queryService.getSoftwareUnitByUniqueName(dependency.to);
								enumsPerSoftwareUnit = addSoftwareUnitToHashMap(enumClass, softwareUnitInSelectedModule, enumsPerSoftwareUnit);
							}
						}
					}
				}
			}
		} catch (Exception e) {
	        logger.error(" Exception: "  + e );
        }
	}

	// For each softwareUnitsInSelectedModuleList, create a Component and its Interface, or a Subsystem, and assign software units to these modules.
	private void createComponentsAndOrSubSystems() {
		try {
			for (String softwareUnitInSelectedModule : softwareUnitsInSelectedModuleList) {
				SoftwareUnitDTO parentUnit = queryService.getSoftwareUnitByUniqueName(softwareUnitInSelectedModule);
				ArrayList<SoftwareUnitDTO> parentUnitsList = new ArrayList<SoftwareUnitDTO>();
				parentUnitsList.add(parentUnit);
				if(componentsWithInterfaces.containsKey(softwareUnitInSelectedModule)){ // Create a Component and an Interface
					// Determine if the new Component has to replace the SelectedModule
					boolean replaceSelectedModuleByComponent = hasTheComponentToReplaceTheSelectedModule(softwareUnitInSelectedModule);

					// Determine if all child units of the parenUnit are needed to provide the implemented service of the interfaces
					boolean allChildUnitsAreNeededToImplementTheInterfaceServices = false;
					HashSet<SoftwareUnitDTO> allChildUnitsNeededToImplementTheInterfaceServices = getAllChildUnitsNeededToImplementTheInterfaceServices(softwareUnitInSelectedModule);
					HashSet<SoftwareUnitDTO> softwareUnitsToAssignToComponent = new HashSet<SoftwareUnitDTO>();
					HashSet<SoftwareUnitDTO> softwareUnitsToAssignToSubSystem = new HashSet<SoftwareUnitDTO>();
					int size_allChildUnitsNeededToImplementTheInterfaceServices = allChildUnitsNeededToImplementTheInterfaceServices.size();
					SoftwareUnitDTO[] allChildUnitsOfSoftwareUnitInSelectedModule = queryService.getChildUnitsOfSoftwareUnit(softwareUnitInSelectedModule);
					int size_softwareUnitsInSelectedModuleList = allChildUnitsOfSoftwareUnitInSelectedModule.length;
					if (size_allChildUnitsNeededToImplementTheInterfaceServices == size_softwareUnitsInSelectedModuleList) {
						allChildUnitsAreNeededToImplementTheInterfaceServices = true;
						softwareUnitsToAssignToComponent.addAll(parentUnitsList);
					} else {
						for (SoftwareUnitDTO childUnit : allChildUnitsOfSoftwareUnitInSelectedModule) {
							boolean childUnitIsNeeded = false;
							for (SoftwareUnitDTO neededUnit : allChildUnitsNeededToImplementTheInterfaceServices) {
								boolean neededUnitIsNotAnInterface = true;
								for (SoftwareUnitDTO interfaceUnit : componentsWithInterfaces.get(softwareUnitInSelectedModule)) {
									if (neededUnit.uniqueName.equals(interfaceUnit.uniqueName)) {
										neededUnitIsNotAnInterface = false;
									}
								}
								if (neededUnitIsNotAnInterface && childUnit.uniqueName.equals(neededUnit.uniqueName)) {
									childUnitIsNeeded = true;
								}
							}
							if (childUnitIsNeeded) {
								softwareUnitsToAssignToComponent.add(childUnit);
							} else {
								softwareUnitsToAssignToSubSystem.add(childUnit);
							}
						}
					}
					if (allChildUnitsAreNeededToImplementTheInterfaceServices) {
						// To do: Create a Component only
					} else {
						// To do: Create a Component and a Subsystem
					}
					
					// Create a Component
					ModuleDTO newModule;
					ArrayList<SoftwareUnitDTO> interfaceUnits = componentsWithInterfaces.get(softwareUnitInSelectedModule);
					if (!replaceSelectedModuleByComponent) {
						// Create a new module as a child of the SelectedModule
						newModule = defineSarService.addModule(parentUnit.name, selectedModuleUniqueName, ModuleTypes.COMPONENT.toString(), 0, parentUnitsList);
						addToReverseReconstructionList(newModule); //add to cache for reverse
						// Create the Interface of the Component
						if (newModule != null) {
							ModuleDTO newInterfaceModule = defineSarService.addModule(parentUnit.name + "Interface", newModule.logicalPath, ModuleTypes.FACADE.toString(), 0, interfaceUnits);
							addToReverseReconstructionList(newInterfaceModule); //add to cache for reverse
						}
					} else { // Replace SelectedModule by the component
						ModuleDTO editedModule = defineSarService.editModule(selectedModule.logicalPath, ModuleTypes.COMPONENT.toString(), null, 0, null);
						addToReverseEditModuleList(selectedModule.logicalPath, ModuleTypes.SUBSYSTEM.toString());
						for (ModuleDTO subModule : editedModule.subModules) {
							if (subModule.type.equals(ModuleTypes.FACADE.toString())) {
								defineSarService.editModule(subModule.logicalPath, null, null, 0, interfaceUnits);
							}
						}
					}
				} else { // Create a subsystem, if the software unit is a package
					if (parentUnit.type.equals("package")) {
						ModuleDTO newModule = defineSarService.addModule(parentUnit.name, selectedModuleUniqueName, ModuleTypes.SUBSYSTEM.toString(), 0, parentUnitsList);
						addToReverseReconstructionList(newModule); //add to cache for reverse
					}
				}
			}
		} catch (Exception e) {
	        logger.error(" Exception: "  + e );
	    }
	}

	private HashMap<String,ArrayList<SoftwareUnitDTO>> addSoftwareUnitToHashMap(SoftwareUnitDTO softwareUnit, String keyOfHashMap, HashMap<String,ArrayList<SoftwareUnitDTO>> hashMap) {
		if(!hashMap.containsKey(keyOfHashMap)){
			ArrayList<SoftwareUnitDTO> softwareUnitsOfClass = new ArrayList<SoftwareUnitDTO>();
			softwareUnitsOfClass.add(softwareUnit);
			hashMap.put(keyOfHashMap, softwareUnitsOfClass);
		}
		else{
			ArrayList<SoftwareUnitDTO> softwareUnitsOfClass = hashMap.get(keyOfHashMap);
			softwareUnitsOfClass.add(softwareUnit);
			hashMap.put(keyOfHashMap, softwareUnitsOfClass);
		}
		return hashMap;
	}
	
	// Checks if the interface is implemented only a few times. Otherwise it is possibly a utility.
	private boolean isInterfaceSpecificForUnit(SoftwareUnitDTO interfaceClass) {
		/* if (interfaceClass.uniqueName.contains("IAnalyseService")) {
			boolean breakpoint = true;
		} */
		boolean interfaceIsSpecificForUnit = false;
		// Get all dependencies on the interfaceClass
		ArrayList<DependencyDTO> dependenciesList = new ArrayList<DependencyDTO>();
		Collections.addAll(dependenciesList, queryService.getDependenciesFromSoftwareUnitToSoftwareUnit("",interfaceClass.uniqueName));
		int numberOfImplementsDependencies = 0;
		for(DependencyDTO dependencyOnInterface : dependenciesList){		
			if(dependencyOnInterface.subType.equals(DependencySubTypes.INH_IMPLEMENTS_INTERFACE.toString())){
				numberOfImplementsDependencies ++;
			}
		}
		if (numberOfImplementsDependencies <= 2) { // One for the implementing facade and maybe one for a service stub. 
			interfaceIsSpecificForUnit = true;
		}
		return interfaceIsSpecificForUnit;
	}

	// Checks if the passed interface is already assigned, in case the parent module is a component, to the interface of the parent module 
	private boolean isInterfaceTheInterfaceOfTheParentModule(SoftwareUnitDTO interfaceClass, ModuleDTO parent) {
		boolean isInterfaceOfParent = false;
		if (parent.type.equals("Component")) {
			ModuleDTO[] children = defineService.getModule_TheChildrenOfTheModule(parent.logicalPath);
			for (ModuleDTO child : children) {
				if (child.type.equals("Facade")) {
					HashSet<String> softwareUnits = defineService.getAssignedSoftwareUnitsOfModule(child.logicalPath);
					for (String softwareUnit : softwareUnits) {
						if (softwareUnit.equals(interfaceClass.uniqueName)) {
							isInterfaceOfParent = true;
						}
					}
				}
			}
		}
		return isInterfaceOfParent;
	}

	/** Returns the first set of children (number of children >= 2) in the decomposition hierarchy of the parent SoftwareUnit
	 * @param parentUniqueName (of a SoftwareUnit)
	 * @return ArrayList<String> with unique names of children, or an empty list, if no child SoftwareUnits are existing.
	 */
	private ArrayList<String> getSetOfChildSoftwareUnits(String parentUniqueName) {
		ArrayList<String> childSoftwareUnits = new ArrayList<String>();
		String parentUnitUniqueName = parentUniqueName;
		while (childSoftwareUnits.size() < 2) {
			SoftwareUnitDTO[] childUnits = (queryService.getChildUnitsOfSoftwareUnit(parentUnitUniqueName));
			if (childUnits.length == 0) {
				break;
			} else if ((childUnits.length == 1)) {
				String newParentUniqueName = childUnits[0].uniqueName;
				childSoftwareUnits = getSetOfChildSoftwareUnits(newParentUniqueName);
			} else if ((childUnits.length >= 2)) {
				for (SoftwareUnitDTO childUnit : childUnits) {
					childSoftwareUnits.add(childUnit.uniqueName);
				}
			}
		}
		return childSoftwareUnits;
	}

	// Returns true, if selectedModule is a Subsystem AND all its assigned types will be assigned to the component.
	private boolean hasTheComponentToReplaceTheSelectedModule(String softwareUnitInSelectedModule) {
		boolean replaceSelectedModuleByComponent = false;
		if (selectedModule.type.equals(ModuleTypes.SUBSYSTEM.toString())) {
			HashSet<String> allClassesAssignedToSelectedModule = new HashSet<String>();
			for (String selectedModuleUniqueName : softwareUnitsInSelectedModuleList) {
				allClassesAssignedToSelectedModule.addAll(queryService.getAllPhysicalClassPathsOfSoftwareUnit(selectedModuleUniqueName));
			}
			HashSet<String> allClassesAssignedToComponent = new HashSet<String>();
			allClassesAssignedToComponent.addAll(queryService.getAllPhysicalClassPathsOfSoftwareUnit(softwareUnitInSelectedModule));
			for (SoftwareUnitDTO interfaceUnit : componentsWithInterfaces.get(softwareUnitInSelectedModule)) {
				allClassesAssignedToComponent.add(interfaceUnit.uniqueName);
			}
			if (allClassesAssignedToSelectedModule.equals(allClassesAssignedToComponent)) {
				replaceSelectedModuleByComponent = true;
			}
		}
		return replaceSelectedModuleByComponent;
	}

	private HashSet<SoftwareUnitDTO> getAllChildUnitsNeededToImplementTheInterfaceServices(String softwareUnitInSelectedModule) {
		HashSet<SoftwareUnitDTO> neededSoftwareUnits = new HashSet<SoftwareUnitDTO>();
		neededSoftwareUnits.addAll(componentsWithImplementingClasses.get(softwareUnitInSelectedModule));
		// If there is a dependency from an interface-implementing software unit, add softwareUnit to neededSoftwareUnits	
		for (SoftwareUnitDTO softwareUnit : queryService.getChildUnitsOfSoftwareUnit(softwareUnitInSelectedModule)) {
			for (SoftwareUnitDTO implementingClass : componentsWithImplementingClasses.get(softwareUnitInSelectedModule)) {
				DependencyDTO[] dependenciesList = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(implementingClass.uniqueName, softwareUnit.uniqueName);
				if(dependenciesList.length > 0){
					neededSoftwareUnits.add(softwareUnit); 
				}
			}
		}
		return neededSoftwareUnits;
	}
	
	//@Override
	public ArrayList<SoftwareUnitDTO> getClasses(String library) {
		// TODO Auto-generated method stub
		return null;
	}

	//@Override
	public TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(String library, TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers) {
		// TODO Auto-generated method stub
		return null;
	}

}
