package husacct.analyse.task.reconstruct.components.HUSACCT;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.enums.DependencySubTypes;
import husacct.common.enums.ModuleTypes;

public class ComponentsAndSubSystems_HUSACCT extends AlgorithmComponentsAndSubSystems{
	private final Logger logger = Logger.getLogger(ComponentsAndSubSystems_HUSACCT.class);
	private String relationType;
	private ModuleDTO selectedModule;
	private ArrayList<String> softwareUnitsInSelectedModuleList;
	private HashMap<String,ArrayList<SoftwareUnitDTO>> componentsWithInterfaces = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
	private HashMap<String,ArrayList<SoftwareUnitDTO>> componentsWithImplementingClasses = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
	private HashMap<String,ArrayList<SoftwareUnitDTO>> exceptionsPerSoftwareUnit = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
	private HashMap<String,ArrayList<SoftwareUnitDTO>> enumsPerSoftwareUnit = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
	private HashMap<String, SoftwareUnitDTO> softwareUnitsToExcludeMap = new HashMap<String, SoftwareUnitDTO>();
	
	public ComponentsAndSubSystems_HUSACCT (IModelQueryService queryService) {
		super(queryService);
	}
		
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) {
		relationType = dto.getRelationType();
		selectedModule = dto.getSelectedModule();
		if (selectedModule.logicalPath.equals("")) {
			selectedModule.logicalPath = "**"; // Root of intended software architecture
			selectedModule.type = "Root"; // Root of intended software architecture
		}
		softwareUnitsInSelectedModuleList = new ArrayList<String>();

		// If the selectedModule is of type Facade or ExternalLibrary, nothing is done.
		if ((selectedModule == null) || selectedModule.type.equals(ModuleTypes.EXTERNAL_LIBRARY.toString()) || selectedModule.type.equals(ModuleTypes.FACADE.toString())) {
			return;
		}

		// Select the set of SUs to be used, and activate the component-identifying algorithm  
		if (selectedModule.logicalPath == "**") {
			for (SoftwareUnitDTO rootUnit : queryService.getSoftwareUnitsInRoot()) {
				if (!rootUnit.uniqueName.equals("xLibraries")) {
					softwareUnitsInSelectedModuleList.add(rootUnit.uniqueName);
				}
			}
		} else {
			softwareUnitsInSelectedModuleList.addAll(defineService.getAssignedSoftwareUnitsOfModule(selectedModule.logicalPath));
		}
		if (softwareUnitsInSelectedModuleList.size() == 1) {
			if (selectedModule.type.equals("Root") || selectedModule.type.equals(ModuleTypes.SUBSYSTEM.toString())) {
				softwareUnitsInSelectedModuleList = getSetOfChildSoftwareUnits(softwareUnitsInSelectedModuleList.get(0));
				identifyComponentsInRootOfSelectedModule(dto.getSelectedModule());
			} else if (selectedModule.type.equals(ModuleTypes.COMPONENT.toString())) {
				softwareUnitsInSelectedModuleList = getSetOfChildSoftwareUnits(softwareUnitsInSelectedModuleList.get(0));
				identifyComponentsInRootOfSelectedModule(dto.getSelectedModule());
				// In case the selectedModule is a Component, the SUs assigned to the interface should not be assigned again. 
				addSoftwareUnitsAssignedToComponentInterface_To_softwareUnitsToExcludeMap();
			} else if (selectedModule.type.equals(ModuleTypes.LAYER.toString())) {
				// Check if the assigned SU is identified as a component. If not, get the children of the SU and apply the algoritm on them,
				boolean componentDetected = identifyComponentsInRootOfSelectedModule(dto.getSelectedModule());
				if (!componentDetected) {
					softwareUnitsInSelectedModuleList = getSetOfChildSoftwareUnits(softwareUnitsInSelectedModuleList.get(0));
					identifyComponentsInRootOfSelectedModule(dto.getSelectedModule());
				}
			}
		} else {
			identifyComponentsInRootOfSelectedModule(dto.getSelectedModule());
		}
		
		// Finally, add the identified Components and Subsystems to the intended architecture
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
	private boolean identifyComponentsInRootOfSelectedModule(ModuleDTO selectedModuleArgument) {
		boolean componentDetected = false;
		this.selectedModule = selectedModuleArgument;
		try {
			for (String softwareUnitInSelectedModule : softwareUnitsInSelectedModuleList) {
				for (SoftwareUnitDTO softwareUnit : queryService.getChildUnitsOfSoftwareUnit(softwareUnitInSelectedModule)) {
					if (softwareUnit.type.equals("class")) {
						// 2) Get all dependencies on softwareUnit
						ArrayList<DependencyDTO> dependenciesList = new ArrayList<DependencyDTO>();
						dependenciesList.addAll(getRelationsBetweenSoftwareUnits(softwareUnit.uniqueName, "", relationType));
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
									componentDetected = true;
									// Relate the interface with the parent package of the implementing class. A package can implement several interfaces.
									componentsWithInterfaces = addSoftwareUnitToHashMap(interfaceClass, softwareUnitInSelectedModule, componentsWithInterfaces);
									// Add the interfaceClass to softwareUnitsToExcludeMap, to prevent that it is assigned to another module.
									softwareUnitsToExcludeMap.put(interfaceClass.uniqueName, interfaceClass);
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
		return componentDetected;
	}

	// For each softwareUnitsInSelectedModuleList, create a Component and its Interface, or a Subsystem, and assign software units to these modules.
	private void createComponentsAndOrSubSystems() {
		ArrayList<String> filteredListOfSoftwareUnitsInSelected = filterSoftwareUnitsInSelectedModuleList(); // Filter out software units assigned to component interfaces.
		try {
			for (String softwareUnitWithinSelectedModule : filteredListOfSoftwareUnitsInSelected) {
				SoftwareUnitDTO parentUnit = queryService.getSoftwareUnitByUniqueName(softwareUnitWithinSelectedModule);
				HashSet<SoftwareUnitDTO> parentUnitsList = new HashSet<SoftwareUnitDTO>();
				parentUnitsList.add(parentUnit);
				if(componentsWithInterfaces.containsKey(softwareUnitWithinSelectedModule)){ 
					// Create a Component and an Interface
					// First, check if all child units of the parenUnit are needed to provide the implemented service of the interfaces
					boolean allChildUnitsAreNeededToImplementTheInterfaceServices = false;
					HashSet<SoftwareUnitDTO> allChildUnitsNeededToImplementTheInterfaceServices = getAllChildUnitsNeededToImplementTheInterfaceServices(softwareUnitWithinSelectedModule);
					HashSet<SoftwareUnitDTO> softwareUnitsToAssignToComponent = new HashSet<SoftwareUnitDTO>();
					HashSet<SoftwareUnitDTO> softwareUnitsToAssignToSubSystem = new HashSet<SoftwareUnitDTO>();
					int size_allChildUnitsNeededToImplementTheInterfaceServices = allChildUnitsNeededToImplementTheInterfaceServices.size();
					SoftwareUnitDTO[] allChildUnitsOfSoftwareUnitInSelectedModule = queryService.getChildUnitsOfSoftwareUnit(softwareUnitWithinSelectedModule);
					int size_softwareUnitsInSelectedModuleList = allChildUnitsOfSoftwareUnitInSelectedModule.length;
					if (size_allChildUnitsNeededToImplementTheInterfaceServices == size_softwareUnitsInSelectedModuleList) {
						allChildUnitsAreNeededToImplementTheInterfaceServices = true;
						softwareUnitsToAssignToComponent.addAll(parentUnitsList);
					} else {
						// Determine per child SU if it is used by an interface-implementing class
						for (SoftwareUnitDTO childUnit : allChildUnitsOfSoftwareUnitInSelectedModule) {
							if (!softwareUnitsToExcludeMap.containsKey(childUnit.uniqueName)) {
								boolean childUnitIsNeeded = false;
								for (SoftwareUnitDTO neededUnit : allChildUnitsNeededToImplementTheInterfaceServices) {
									boolean neededUnitIsNotAnInterface = true;
									for (SoftwareUnitDTO interfaceUnit : componentsWithInterfaces.get(softwareUnitWithinSelectedModule)) {
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
						// Second check: Determine iteratively if the SUs in softwareUnitsToAssignToSubSystem are used by the SUs in softwareUnitsToAssignToComponent.
						// If so, move them to softwareUnitsToAssignToComponent
						boolean tryMove = true;
						while (tryMove) {
							tryMove = false;
							ArrayList<SoftwareUnitDTO> softwareUnitsToMove = new ArrayList<SoftwareUnitDTO>();
							for (SoftwareUnitDTO suSubsystem : softwareUnitsToAssignToSubSystem) {
								boolean moveToComponent = false;
								for (SoftwareUnitDTO suComponent : softwareUnitsToAssignToComponent) {
									ArrayList<DependencyDTO> dependenciesList = getRelationsBetweenSoftwareUnits(suComponent.uniqueName, suSubsystem.uniqueName, relationType);
									if(dependenciesList.size() > 0){
										moveToComponent = true; 
									}
								}
								if (moveToComponent) {
									softwareUnitsToMove.add(suSubsystem);
									tryMove = true;
								} 
							}
							if (tryMove) {
								softwareUnitsToAssignToSubSystem.removeAll(softwareUnitsToMove);
								softwareUnitsToAssignToComponent.addAll(softwareUnitsToMove);
							}
						}

						// Reconsider after second check
						if (softwareUnitsToAssignToSubSystem.size() == 0) {
							allChildUnitsAreNeededToImplementTheInterfaceServices = true;
						}
					}
					
					// Create a Component, conditionally as submodule of a sSubsystem 
					if (allChildUnitsAreNeededToImplementTheInterfaceServices) {
						// Create a Component only
						createComponent(parentUnit, softwareUnitsToAssignToComponent, false);
					} else {
						// Create a Subsystem and a Component
						createSubSystem(parentUnit, softwareUnitsToAssignToSubSystem);						
						parentUnit.name = parentUnit.name + "Component";
						createComponent(parentUnit, softwareUnitsToAssignToComponent, true);
					}
				} else { 
					// Create a subsystem, if the software unit is a package
					createSubSystem(parentUnit, parentUnitsList);
				}
			}
		} catch (Exception e) {
	        logger.error(" Exception: "  + e );
	    }
	}

	private void createComponent(SoftwareUnitDTO parentUnit, HashSet<SoftwareUnitDTO> softwareUnitsToAssignToComponent, boolean hasSiblingSubSystem) {
		ModuleDTO newModule;
		ArrayList<SoftwareUnitDTO> interfaceUnits = componentsWithInterfaces.get(parentUnit.uniqueName);
		// Determine if the new Component has to replace the SelectedModule
		boolean replaceSelectedModuleByComponent = shouldTheComponentReplaceTheSelectedModule(parentUnit.uniqueName);

		if (!replaceSelectedModuleByComponent || hasSiblingSubSystem) {
			// Create a new module as a child of the SelectedModule
			newModule = createModule_andAddItToReverseList(parentUnit.name, selectedModule.logicalPath, ModuleTypes.COMPONENT.toString(), 0, new ArrayList<SoftwareUnitDTO>(softwareUnitsToAssignToComponent));
			if (!newModule.logicalPath.equals("")) {
				// Create the Interface of the Component.
				createModule_andAddItToReverseList(parentUnit.name + "Interface", newModule.logicalPath, ModuleTypes.FACADE.toString(), 0, interfaceUnits);
			}
		} else { // Replace SelectedModule by the component
			ModuleDTO editedModule = defineSarService.editModule(selectedModule.logicalPath, ModuleTypes.COMPONENT.toString(), null, 0, null);
			if (!editedModule.logicalPath.equals("")) {
				addToReverseReconstructionList(editedModule); //add to cache for reverse
				for (ModuleDTO subModule : editedModule.subModules) {
					if (subModule.type.equals(ModuleTypes.FACADE.toString())) {
						defineSarService.editModule(subModule.logicalPath, null, null, 0, interfaceUnits);
					}
				}
			}
		}
	}
	
	private void createSubSystem(SoftwareUnitDTO parentUnit, HashSet<SoftwareUnitDTO> softwareUnitsToAssignToSubSystem) {
		if (parentUnit.type.equals("package")) {
			ArrayList<SoftwareUnitDTO> softwareUnits = new ArrayList<SoftwareUnitDTO>(softwareUnitsToAssignToSubSystem);
			String type = ModuleTypes.SUBSYSTEM.toString();
			createModule_andAddItToReverseList(parentUnit.name, selectedModule.logicalPath, type, 0, softwareUnits);
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
		dependenciesList.addAll(getRelationsBetweenSoftwareUnits("",interfaceClass.uniqueName, relationType));
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
		String softwareUnit = parentUniqueName;
		while (childSoftwareUnits.size() < 2) {
			SoftwareUnitDTO[] childUnits = (queryService.getChildUnitsOfSoftwareUnit(softwareUnit));
			if (childUnits.length == 0) {
				if (!softwareUnit.equals(parentUniqueName)) {
					childSoftwareUnits.add(softwareUnit);
				}
				break;
			} else if ((childUnits.length == 1)) {
				softwareUnit = childUnits[0].uniqueName;
			} else if ((childUnits.length >= 2)) {
				for (SoftwareUnitDTO childUnit : childUnits) {
					childSoftwareUnits.add(childUnit.uniqueName);
				}
			}
		}
		return childSoftwareUnits;
	}

	// Returns true, if selectedModule is a Subsystem AND all its assigned types will be assigned to the component.
	private boolean shouldTheComponentReplaceTheSelectedModule(String softwareUnitInSelectedModule) {
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
				ArrayList<DependencyDTO> dependenciesList = getRelationsBetweenSoftwareUnits(implementingClass.uniqueName, softwareUnit.uniqueName, relationType);
				if(dependenciesList.size() > 0){
					neededSoftwareUnits.add(softwareUnit); 
				}
			}
		}
		return neededSoftwareUnits;
	}

	private void addSoftwareUnitsAssignedToComponentInterface_To_softwareUnitsToExcludeMap() {
		if (selectedModule.type.equals(ModuleTypes.COMPONENT.toString())) {
			for (ModuleDTO subModule : selectedModule.subModules) {
				if (subModule.type.equals(ModuleTypes.FACADE.toString())) {
					defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath);
					for (String assignedUnitUniqueName : defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath)) {
						SoftwareUnitDTO assignedUnit = queryService.getSoftwareUnitByUniqueName(assignedUnitUniqueName);
						if (!assignedUnit.name.isEmpty()) {
							softwareUnitsToExcludeMap.put(assignedUnit.uniqueName, assignedUnit);
						}
					}
				}
			}
		}
	}
	
	private ArrayList<String> filterSoftwareUnitsInSelectedModuleList() {
		ArrayList<String> newSoftwareUnitsInSelectedModuleList =  new ArrayList<String>();
		for (String su : softwareUnitsInSelectedModuleList) {
			if (!softwareUnitsToExcludeMap.containsKey(su)) {
				newSoftwareUnitsInSelectedModuleList.add(su);
			}
		}
		return newSoftwareUnitsInSelectedModuleList;
	}
}
