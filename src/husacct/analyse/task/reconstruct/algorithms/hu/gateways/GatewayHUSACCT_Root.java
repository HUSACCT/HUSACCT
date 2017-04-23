package husacct.analyse.task.reconstruct.algorithms.hu.gateways;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.dto.ReconstructArchitectureDTO;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.enums.ModuleTypes;

public class GatewayHUSACCT_Root extends AlgorithmHUSACCT{
	ModuleDTO selectedModule;
	private final Logger logger = Logger.getLogger(GatewayHUSACCT_Root.class);
	private ArrayList<SoftwareUnitDTO> softwareUnitsToIncludeInAlgorithm = new ArrayList<SoftwareUnitDTO>();
	private HashMap<String, SoftwareUnitDTO> softwareUnitsToExclude = new HashMap<String, SoftwareUnitDTO>();
	//Gateway finder.
	//Gateway: only has inside-dependencies towards it, so it is located in the bottom layer
	//Gateway: is the only class with imports towards an external application 

	public GatewayHUSACCT_Root (IModelQueryService queryService) {
		super(queryService);
	}
		
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) throws Exception {
		try {
			selectedModule = dto.getSelectedModule();
			this.threshold = dto.getThreshold();
			// If the selectedModule is of type Facade or ExternalLibrary, nothing is done.
			if ((selectedModule == null) || selectedModule.type.equals(ModuleTypes.EXTERNAL_LIBRARY.toString()) || selectedModule.type.equals(ModuleTypes.FACADE.toString())) {
				return;
			}
	  
			if (!"**".equals(selectedModule.logicalPath)) {
				softwareUnitsToIncludeInAlgorithm = getRelevantSoftwareUnits();
			}
			HashMap<SoftwareUnitDTO, ArrayList<SoftwareUnitDTO>> gateways = identifyGateWays(softwareUnitsToIncludeInAlgorithm);
			createModule(gateways,selectedModule);
			
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }
	}
	
	private ArrayList<SoftwareUnitDTO> getChildUnitsOutOfPackage(SoftwareUnitDTO softwareUnit){
		ArrayList<SoftwareUnitDTO> listOfClassesFound = new ArrayList<SoftwareUnitDTO>();
		ArrayList<SoftwareUnitDTO> originalUnit = new ArrayList<SoftwareUnitDTO>();
		originalUnit.add(softwareUnit);
		if(softwareUnit.type.equals("package")){
			SoftwareUnitDTO[] list = queryService.getChildUnitsOfSoftwareUnit(softwareUnit.uniqueName);
			for(SoftwareUnitDTO unit: list){
				if(unit.type.equals("package")){
					for(SoftwareUnitDTO childUnit : queryService.getChildUnitsOfSoftwareUnit(unit.uniqueName)){
						listOfClassesFound.add(childUnit);
					}
				}
				listOfClassesFound.add(unit);
			}
			return listOfClassesFound;
		}
		return originalUnit;
	}
	
	private HashMap<SoftwareUnitDTO, ArrayList<SoftwareUnitDTO>> identifyGateWays(ArrayList<SoftwareUnitDTO> selectedUnits){
		ArrayList<SoftwareUnitDTO> gateways = new ArrayList<SoftwareUnitDTO>();
		
		HashMap<SoftwareUnitDTO, ArrayList<SoftwareUnitDTO>> mapOfGateways = new HashMap<SoftwareUnitDTO, ArrayList<SoftwareUnitDTO>>();
		Set<SoftwareUnitDTO> chosenClasses = new HashSet<SoftwareUnitDTO>();
		SoftwareUnitDTO library = null;	
		
		

		for (SoftwareUnitDTO softwareUnitParent : selectedUnits){		
			for(SoftwareUnitDTO softwareUnit : getChildUnitsOutOfPackage(softwareUnitParent)){
			
				DependencyDTO[] allDependencies = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName,"");
	
				Set<SoftwareUnitDTO> set = new HashSet<SoftwareUnitDTO>();
				ArrayList<DependencyDTO> unitDependencies = new ArrayList<>();
				ArrayList<DependencyDTO> unitExternalDependencies = new ArrayList<>();
				for(DependencyDTO dep : allDependencies){
					if(dep.from.equals(softwareUnit.uniqueName)){
						unitDependencies.add(dep);
						SoftwareUnitDTO depTo = queryService.getSoftwareUnitByUniqueName(dep.to);
						if (depTo.type.toUpperCase().equals("LIBRARY")){
							unitExternalDependencies.add(dep);
							library = depTo;
						}
					}
				}
				
				int totalNumberOfDep = unitDependencies.size();
				if((totalNumberOfDep * .6) <= unitExternalDependencies.size() && totalNumberOfDep > 0){
					set.add(softwareUnit);	
				}
				
				gateways.addAll(set);			
	
				chosenClasses = determineClassWithMostDependencies(library, gateways);
				if(chosenClasses.contains(softwareUnit)){
					mapOfGateways = addSoftwareUnitToHashMap(softwareUnit, library, mapOfGateways);	
				}
				set = new HashSet<SoftwareUnitDTO>();	
			}
		}	
		
		return mapOfGateways;
		
	}
	
	private Set<SoftwareUnitDTO> determineClassWithMostDependencies(SoftwareUnitDTO library, ArrayList<SoftwareUnitDTO> listOfClasses){
		HashMap<SoftwareUnitDTO, Integer> classWithNumberOfDependencies = new HashMap<SoftwareUnitDTO, Integer>();
		Set<SoftwareUnitDTO> setOfClasses = new HashSet<SoftwareUnitDTO>();
		for(SoftwareUnitDTO unit : listOfClasses){
			DependencyDTO[] dependencies =  queryService.getDependenciesFromSoftwareUnitToSoftwareUnit( unit.uniqueName, library.uniqueName);
			classWithNumberOfDependencies.put(unit, dependencies.length);
			
			Map.Entry<SoftwareUnitDTO, Integer> maxEntry = null;
			for (Map.Entry<SoftwareUnitDTO, Integer> entry : classWithNumberOfDependencies.entrySet())
			{
			    if (maxEntry == null || entry.getValue().compareTo(maxEntry.getValue()) >= 0)
			    {
			        maxEntry = entry;
			    }
			    setOfClasses.add(maxEntry.getKey());	    
			}
		} 
		return setOfClasses;
	}
	
	private HashMap<SoftwareUnitDTO,ArrayList<SoftwareUnitDTO>> addSoftwareUnitToHashMap(SoftwareUnitDTO softwareUnit, SoftwareUnitDTO keyOfHashMap, HashMap<SoftwareUnitDTO,ArrayList<SoftwareUnitDTO>> hashMap) {
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
	private void createModule(HashMap<SoftwareUnitDTO, ArrayList<SoftwareUnitDTO>> gateways, ModuleDTO selectedModule){
		ModuleDTO gatewayModule = new ModuleDTO();
		for(HashMap.Entry<SoftwareUnitDTO, ArrayList<SoftwareUnitDTO>> gateway : gateways.entrySet()){
			if(!"".equals(gateway.getKey())){
				gatewayModule = defineSarService.addModule(gateway.getKey().name + " Gateway", selectedModule.logicalPath, ModuleTypes.SUBSYSTEM.toString(), 0, gateway.getValue());	
				if(!gatewayModule.logicalPath.equals("")){
					createRuleType(gatewayModule,selectedModule);
				}
			}
			
		}
		addToReverseReconstructionList(gatewayModule);
	}
	private void createRuleType(ModuleDTO libraryModule, ModuleDTO moduleTo){
		defineSarService.addMainRule(moduleTo.logicalPath, libraryModule.logicalPath, "IsTheOnlyModuleAllowedToUse");
	}

	private ArrayList<SoftwareUnitDTO> getSetOfChildSoftwareUnits(SoftwareUnitDTO parentSoftwareUnit) {
		ArrayList<SoftwareUnitDTO> childSoftwareUnits = new ArrayList<SoftwareUnitDTO>();
		SoftwareUnitDTO softwareUnit = parentSoftwareUnit;
		while (childSoftwareUnits.size() < 2) {
			SoftwareUnitDTO[] childUnits = (queryService.getChildUnitsOfSoftwareUnit(softwareUnit.uniqueName));
			if (childUnits.length == 0) {
				if (!softwareUnit.equals(parentSoftwareUnit)) {
					childSoftwareUnits.add(softwareUnit);
				}
				break;
			} else if ((childUnits.length == 1)) {
				softwareUnit = childUnits[0];
			} else if ((childUnits.length >= 2)) {
				for (SoftwareUnitDTO childUnit : childUnits) {
					childSoftwareUnits.add(childUnit);
				}
			}
		}
		return childSoftwareUnits;
	}
	
	private ArrayList<SoftwareUnitDTO> getRelevantSoftwareUnits() {
		ArrayList<SoftwareUnitDTO> softwareUnitsToReturn = new ArrayList<SoftwareUnitDTO>();
		addSoftwareUnitsAssignedToComponentInterface_To_softwareUnitsToExcludeMap();
		
		int numberOfAssignedSoftwareUnits = defineService.getAssignedSoftwareUnitsOfModule(selectedModule.logicalPath).size();
		if (numberOfAssignedSoftwareUnits > 1) {
			for(String logicalSoftwarePathSelectedModule : defineService.getAssignedSoftwareUnitsOfModule(selectedModule.logicalPath)){
				SoftwareUnitDTO suDTO = queryService.getSoftwareUnitByUniqueName(logicalSoftwarePathSelectedModule);
				if (!softwareUnitsToExclude.containsKey(suDTO.uniqueName)) {
					softwareUnitsToReturn.add(suDTO);
				}
			}
		} else if (numberOfAssignedSoftwareUnits == 1){
			SoftwareUnitDTO assignedSU = new SoftwareUnitDTO("", "", "", "");
			for(String uniqueNameAssignedSU : defineService.getAssignedSoftwareUnitsOfModule(selectedModule.logicalPath)){
				assignedSU = queryService.getSoftwareUnitByUniqueName(uniqueNameAssignedSU);
			}
			for (SoftwareUnitDTO subModule : getSetOfChildSoftwareUnits(assignedSU)){
				if (!softwareUnitsToExclude.containsKey(subModule.uniqueName)) {
					softwareUnitsToReturn.add(subModule);
				}
			}
		}
		return softwareUnitsToReturn;
	}
	
	private void addSoftwareUnitsAssignedToComponentInterface_To_softwareUnitsToExcludeMap() {
		if (selectedModule.type.equals(ModuleTypes.COMPONENT.toString())) {
			for (ModuleDTO subModule : selectedModule.subModules) {
				if (subModule.type.equals(ModuleTypes.FACADE.toString())) {
					defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath);
					for (String assignedUnitUniqueName : defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath)) {
						SoftwareUnitDTO assignedUnit = queryService.getSoftwareUnitByUniqueName(assignedUnitUniqueName);
						if (!assignedUnit.name.isEmpty()) {
							softwareUnitsToExclude.put(assignedUnit.uniqueName, assignedUnit);
						}
					}
				}
			}
		}
	}
	
	@Override
	public ReconstructArchitectureDTO getAlgorithmParameterSettings() {
  		ReconstructArchitectureDTO reconstructArchitecture = new ReconstructArchitectureDTO();
  		reconstructArchitecture.approachConstant = AnalyseReconstructConstants.Algorithm.Gateways_HUSACCT_Root;
  		reconstructArchitecture.threshold = 10;
  		reconstructArchitecture.relationType = AnalyseReconstructConstants.RelationTypes.allDependencies;
  		reconstructArchitecture.granularity = AnalyseReconstructConstants.Granularities.PackagesAndClasses;
  		reconstructArchitecture.parameterDTOs = createParameterPanels();
  		return reconstructArchitecture;
  	}
	private ArrayList<ReconstructArchitectureParameterDTO> createParameterPanels(){
		ArrayList<ReconstructArchitectureParameterDTO> parameterDTOs = new ArrayList<>();
		parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createThresholdParameter(10));
		parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createRelationTypeParameter(AnalyseReconstructConstants.RelationTypes.allDependencies));
		parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createGranularityPanel(AnalyseReconstructConstants.Granularities.PackagesAndClasses));
		return parameterDTOs;
	}
}
