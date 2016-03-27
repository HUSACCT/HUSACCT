package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.analyse.serviceinterface.enums.DependencySubTypes;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;
import husacct.validate.domain.validation.moduletype.ModuleTypes;

public class AlgorithmComponents extends AlgorithmGeneral{
	private IModelQueryService queryService;
	private ArrayList<SoftwareUnitDTO> internalRootPackagesWithClasses; 
	private ArrayList<SoftwareUnitDTO> identifiedInterfaceClasses; 
	private String xLibrariesRootPackage = "xLibraries";
	private IDefineService defineService;
	private IDefineSarService defineSarService;
	private final Logger logger = Logger.getLogger(AlgorithmComponents.class);

	public AlgorithmComponents(IModelQueryService queryService){
		this.queryService = queryService;
		defineService = ServiceProvider.getInstance().getDefineService();
		defineSarService = defineService.getSarService();
	}

	@Override
	public void execute(ModuleDTO Module, int th, IModelQueryService qService, String library, String dependencyType) {
		identifyComponentsInRootOfSelectedModule(Module);
	}

	public void identifyComponentsInRootOfSelectedModule(ModuleDTO module) {
		try {
			String selectedModuleUniqueName = module.logicalPath;
			if (selectedModuleUniqueName.equals("")) {
				selectedModuleUniqueName = "**"; // Root of intended software architecture
			}
			// 1) Select all the software units in the root of the selected module
			ArrayList<String> softwareUnitsInSelectedModuleList = new ArrayList<String>();
			ArrayList<SoftwareUnitDTO> classesInRootPackages = new ArrayList<SoftwareUnitDTO>();
			softwareUnitsInSelectedModuleList.addAll(defineService.getAssignedSoftwareUnitsOfModule(selectedModuleUniqueName));
			for (String softwareUnitInSelectedModule : softwareUnitsInSelectedModuleList) {
				for (SoftwareUnitDTO softwareUnit : queryService.getChildUnitsOfSoftwareUnit(softwareUnitInSelectedModule)) {
					if (softwareUnit.type.equals("class")) {
						classesInRootPackages.add(softwareUnit);
					}
				}
			}
			// 2) Select the software units that implement an interface
			HashMap<String,ArrayList<SoftwareUnitDTO>> interfacesPerPackage = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
			for(SoftwareUnitDTO softwareUnitInSelectedModule : classesInRootPackages){
				// a) Get all dependencies on the software unit
				ArrayList<DependencyDTO> dependenciesList = new ArrayList<DependencyDTO>();
				Collections.addAll(dependenciesList, queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnitInSelectedModule.uniqueName, ""));
				// b) Determine if the software unit implement an interface	
				for(DependencyDTO dependency : dependenciesList){		
					if(dependency.subType.equals(DependencySubTypes.INH_IMPLEMENTS_INTERFACE.toString())){
						// 3) Get the used interface. Continue only if the interface is implemented ones only (otherwise it is possibly a utility).
						SoftwareUnitDTO interfaceClass = queryService.getSoftwareUnitByUniqueName(dependency.to);
						if (isInterfaceImplementedOnceOnly(interfaceClass)) {
							// 4) Relate the interface with the parent package of the implementingClassesInSelectedModule
							// Determine the parent package of the implementing class, and relate package and interface.
							// Note: a package can implement several interfaces.
							String parentName = queryService.getParentUnitOfSoftwareUnit(softwareUnitInSelectedModule.uniqueName).uniqueName;
							if(!interfacesPerPackage.containsKey(parentName)){
								ArrayList<SoftwareUnitDTO> softwareUnitsOfInterface = new ArrayList<SoftwareUnitDTO>();
								softwareUnitsOfInterface.add(interfaceClass);
								interfacesPerPackage.put(parentName, softwareUnitsOfInterface);
							}
							else{
								ArrayList<SoftwareUnitDTO> softwareUnitsOfInterface = interfacesPerPackage.get(parentName);
								softwareUnitsOfInterface.add(interfaceClass);
								interfacesPerPackage.put(parentName, softwareUnitsOfInterface);
							}
						}
					}
				}
			}
			// Create the component and its interface, and assign the software units to these modules
			for(String parentPackageUniqueName : interfacesPerPackage.keySet()){
				SoftwareUnitDTO parentUnit = queryService.getSoftwareUnitByUniqueName(parentPackageUniqueName);
				ArrayList<SoftwareUnitDTO> parentUnitsList = new ArrayList<SoftwareUnitDTO>();
				parentUnitsList.add(parentUnit);
				
				ModuleDTO newModule = defineSarService.addModule(parentUnit.name, selectedModuleUniqueName, ModuleTypes.COMPONENT.toString(), 0, parentUnitsList);
				addToReverseReconstructionList(newModule); //add to cache for reverse
				ArrayList<SoftwareUnitDTO> interfaceUnits = interfacesPerPackage.get(parentPackageUniqueName);
				ModuleDTO newInterfaceModule = defineSarService.addModule(parentUnit.name + "Interface", selectedModuleUniqueName + "." + parentUnit.name, ModuleTypes.FACADE.toString(), 0, interfaceUnits);
				addToReverseReconstructionList(newInterfaceModule); //add to cache for reverse
			}
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
        }
	}

	public void identifyComponentsInRoot() { // Old mechanism, not used currently
		// Select all interface classes.
		determineInternalRootPackagesWithClasses();
		identifiedInterfaceClasses = new ArrayList<SoftwareUnitDTO>();
		for(int i = 0; i<internalRootPackagesWithClasses.size(); i++){
			String newRoot = internalRootPackagesWithClasses.get(i).uniqueName;
			for (SoftwareUnitDTO child : queryService.getChildUnitsOfSoftwareUnit(newRoot)) {
				if (child.type.equalsIgnoreCase("interface")) {
					identifiedInterfaceClasses.add(child);
				}
			}
		}
		// Select the package(s) implementing an interface and relate packages and interface
		HashMap<String,ArrayList<SoftwareUnitDTO>> interfacesPerPackage = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
		for(SoftwareUnitDTO interfaceClass : identifiedInterfaceClasses){
			// Get all dependencies on the interfaceClass
			ArrayList<DependencyDTO> dependenciesList = new ArrayList<DependencyDTO>();
			Collections.addAll(dependenciesList, queryService.getDependenciesFromSoftwareUnitToSoftwareUnit("",interfaceClass.uniqueName));
			for(DependencyDTO dependencyOnInterface : dependenciesList){		
				// Determine the classes that implement the interface	
				if(dependencyOnInterface.subType.equals(DependencySubTypes.INH_IMPLEMENTS_INTERFACE.toString()) && queryService.getParentUnitOfSoftwareUnit(dependencyOnInterface.to).type.equals("package")){
					// Determine the parent package of the implementing class, and relate package and interface.
					// Note: a package can implement several interfaces.
					String parentName = queryService.getParentUnitOfSoftwareUnit(dependencyOnInterface.to).uniqueName;
					if(!interfacesPerPackage.containsKey(parentName)){
						ArrayList<SoftwareUnitDTO> softwareUnitsOfInterface = new ArrayList<SoftwareUnitDTO>();
						softwareUnitsOfInterface.add(interfaceClass);
						interfacesPerPackage.put(parentName, softwareUnitsOfInterface);
					}
					else{
						ArrayList<SoftwareUnitDTO> softwareUnitsOfInterface = interfacesPerPackage.get(parentName);
						softwareUnitsOfInterface.add(interfaceClass);
						interfacesPerPackage.put(parentName, softwareUnitsOfInterface);
					}
				}
			}
		}
		// Create the component and its interface, and assign the software units to these modules
		for(String parentPackageUniqueName : interfacesPerPackage.keySet()){
			SoftwareUnitDTO parentUnit = queryService.getSoftwareUnitByUniqueName(parentPackageUniqueName);
			ArrayList<SoftwareUnitDTO> parentUnitsList = new ArrayList<SoftwareUnitDTO>();
			parentUnitsList.add(parentUnit);
			
			defineSarService.addModule(parentUnit.name, "**", ModuleTypes.COMPONENT.toString(), 0, parentUnitsList);
			ArrayList<SoftwareUnitDTO> interfaceUnits = interfacesPerPackage.get(parentPackageUniqueName);
			defineSarService.addModule(parentUnit.name + "Interface", parentUnit.name, ModuleTypes.FACADE.toString(), 0, interfaceUnits);
		}
		
		
		//logicalpath: **, validate/domain/validation/moduletype/moduletypes.java, 0, 
		
		//IDEfineServce.getmodule_thechildrenofthemmodule -> if interface, dan koppel gevonden interface
		
		//map key: uniquename value: arraylist<interfaces
		
		
		//dependencies op uniquename van interface
		//getDependenciesFromSoftwareUnitToSoftwareUnit
		//dependencyDTO.subType
		//INH_IMPLEMENTS_INTERFACE("Implements Interface", DependencyTypes.INHERITANCE),
		//getParentUnit returns softwareunitDTO,
		
		//in package get classes that uses interface
		//validate.propertyrulestype.facadeconvention.check 
		//module component get child, edit module
		//threshold 20% begin met 50%
	}

	
	@Override
	public ArrayList<SoftwareUnitDTO> getClasses(String library) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(String library,
			TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers) {
		// TODO Auto-generated method stub
		return null;
	}
	private void determineInternalRootPackagesWithClasses() {
		internalRootPackagesWithClasses = new ArrayList<SoftwareUnitDTO>();
		SoftwareUnitDTO[] allRootUnits = queryService.getSoftwareUnitsInRoot();
		for (SoftwareUnitDTO rootModule : allRootUnits) {
			if (!rootModule.uniqueName.equals(xLibrariesRootPackage)) {
				for (String internalPackage : queryService.getRootPackagesWithClass(rootModule.uniqueName)) {
					internalRootPackagesWithClasses.add(queryService.getSoftwareUnitByUniqueName(internalPackage));

				}
			}
		}
		if (internalRootPackagesWithClasses.size() == 1) {
			// Temporal solution useful for HUSACCT20 test. To be improved!
			// E.g., classes in root are excluded from the process.
			String newRoot = internalRootPackagesWithClasses.get(0).uniqueName;
			internalRootPackagesWithClasses = new ArrayList<SoftwareUnitDTO>();
			for (SoftwareUnitDTO child : queryService.getChildUnitsOfSoftwareUnit(newRoot)) {
				if (child.type.equalsIgnoreCase("package")) {
					internalRootPackagesWithClasses.add(child);
				}
			}
		}
	}
	private boolean isInterfaceImplementedOnceOnly(SoftwareUnitDTO interfaceClass) {
		boolean interfaceIsImplementedOnceOnly = false;
		// Get all dependencies on the interfaceClass
		ArrayList<DependencyDTO> dependenciesList = new ArrayList<DependencyDTO>();
		Collections.addAll(dependenciesList, queryService.getDependenciesFromSoftwareUnitToSoftwareUnit("",interfaceClass.uniqueName));
		int numberOfImplementsDependencies = 0;
		for(DependencyDTO dependencyOnInterface : dependenciesList){		
			if(dependencyOnInterface.subType.equals(DependencySubTypes.INH_IMPLEMENTS_INTERFACE.toString())){
				numberOfImplementsDependencies ++;
			}
		}
		if (numberOfImplementsDependencies == 1) {
			interfaceIsImplementedOnceOnly = true;
		}
		return interfaceIsImplementedOnceOnly;
	}
}
