package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.analyse.serviceinterface.enums.DependencySubTypes;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineSarService;
import husacct.validate.domain.validation.moduletype.ModuleTypes;

public class AlgorithmComponents extends AlgorithmGeneral{
	private final Logger logger = Logger.getLogger(AlgorithmComponents.class);
	private IModelQueryService queryService;
	private ArrayList<SoftwareUnitDTO> internalRootPackagesWithClasses; 
	private ArrayList<SoftwareUnitDTO> identifiedInterfaceClasses; 
	private String xLibrariesRootPackage = "xLibraries";
	private IDefineSarService defineSarService;
	

	public AlgorithmComponents(IModelQueryService queryService){
		this.queryService = queryService;
		defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
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
	public void identifyComponents() {
		
		determineInternalRootPackagesWithClasses();
		identifiedInterfaceClasses = new ArrayList<SoftwareUnitDTO>();
		HashMap<String,ArrayList<SoftwareUnitDTO>> interfacesPerPackage = new HashMap<String, ArrayList<SoftwareUnitDTO>>();
		
		for(int i = 0; i<internalRootPackagesWithClasses.size(); i++){
			String newRoot = internalRootPackagesWithClasses.get(i).uniqueName;
			for (SoftwareUnitDTO child : queryService.getChildUnitsOfSoftwareUnit(newRoot)) {
				if (child.type.equalsIgnoreCase("interface")) {
					identifiedInterfaceClasses.add(child);
				}
			}
		}

		ArrayList<DependencyDTO> dependenciesList = new ArrayList<DependencyDTO>();
		
		
		for(SoftwareUnitDTO interfaceClass : identifiedInterfaceClasses){		
			Collections.addAll(dependenciesList, queryService.getDependenciesFromSoftwareUnitToSoftwareUnit("",interfaceClass.uniqueName));
		
			for(DependencyDTO dependencyClass : dependenciesList){		
					
				if(dependencyClass.subType.equals(DependencySubTypes.INH_IMPLEMENTS_INTERFACE.toString()) && queryService.getParentUnitOfSoftwareUnit(dependencyClass.to).type.equals("package")){
					ArrayList<SoftwareUnitDTO> arraySoftwareUnits = new ArrayList<SoftwareUnitDTO>();
					
					arraySoftwareUnits.add(interfaceClass);
					String parentName = queryService.getParentUnitOfSoftwareUnit(dependencyClass.to).uniqueName;
					
					if(!interfacesPerPackage.containsKey(parentName)){
						interfacesPerPackage.put(parentName, arraySoftwareUnits);
					}
					else{
						ArrayList<SoftwareUnitDTO> existingInterfaces  = new ArrayList<SoftwareUnitDTO>();
						existingInterfaces = interfacesPerPackage.get(parentName);
						//logger.info(interfaceClass);
						existingInterfaces.add(interfaceClass);
						interfacesPerPackage.put(parentName, existingInterfaces);
					}
					
					
					
				}
				
			}
		}
		for(String parentUniqueName : interfacesPerPackage.keySet()){
			SoftwareUnitDTO parentUnit = queryService.getSoftwareUnitByUniqueName(parentUniqueName);
			ArrayList<SoftwareUnitDTO> parentUnitsList = new ArrayList<SoftwareUnitDTO>();
			parentUnitsList.add(parentUnit);
			
			defineSarService.addModule(parentUnit.name, "**", ModuleTypes.COMPONENT.toString(), 0, parentUnitsList);
			ArrayList<SoftwareUnitDTO> interfaceUnits = interfacesPerPackage.get(parentUniqueName);
			defineSarService.addModule(parentUnit.name + "Interface", parentUnit.name, ModuleTypes.FACADE.toString(), 0, interfaceUnits);
			
		}
		
		
		//module van type component voor iedere combinatie interface package
		//IDefineSarService.addmodule en editmodule voor de interface
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
	public void define(ModuleDTO Module, int th, IModelQueryService qService, String library, String dependencyType) {
		// TODO Auto-generated method stub
		
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
}
