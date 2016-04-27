package husacct.analyse.task.reconstruct.layers.goldstein;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.enums.ModuleTypes;

public class LayersGoldstein_SelectedModuleOriginal extends AlgorithmGoldstein{

	
	private ModuleDTO selectedModule;
	private int layerThreshold;
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private ArrayList<SoftwareUnitDTO> softwareUnitsToIncludeInAlgorithm = new ArrayList<SoftwareUnitDTO>();
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layersWithSoftwareUnitsMap = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
	private HashMap<String, SoftwareUnitDTO> softwareUnitsToExclude = new HashMap<String, SoftwareUnitDTO>();

	public LayersGoldstein_SelectedModuleOriginal (IModelQueryService queryService) {
		super(queryService);
	}
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) {
		try {
			selectedModule = dto.getSelectedModule();
			if (selectedModule.logicalPath.equals("")) {
				selectedModule.logicalPath = "**"; // Root of intended software architecture
				selectedModule.type = "Root"; // Root of intended software architecture
			}
			layerThreshold = dto.getThreshold();

			// If the selectedModule is of type Facade or ExternalLibrary, nothing is done.
			if ((selectedModule == null) || selectedModule.type.equals(ModuleTypes.EXTERNAL_LIBRARY.toString()) || selectedModule.type.equals(ModuleTypes.FACADE.toString())) {
				return;
			}
	
			// Select the set of SUs to be used, and activate the component-identifying algorithm  
			if (selectedModule.logicalPath == "**") {
				for (SoftwareUnitDTO rootUnit : queryService.getSoftwareUnitsInRoot()) {
					if (!rootUnit.uniqueName.equals("xLibraries")) {
						softwareUnitsToIncludeInAlgorithm.add(rootUnit);
						if (softwareUnitsToIncludeInAlgorithm.size() == 1) {
							softwareUnitsToIncludeInAlgorithm = getSetOfChildSoftwareUnits(rootUnit);
						}
					}
				}
			} else {
				softwareUnitsToIncludeInAlgorithm = getRelevantSoftwareUnits();
			}
	
			identifyLayers(softwareUnitsToIncludeInAlgorithm, dto.getRelationType());
			addIdentifiedLayersToIntendedArchitecture();
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }

	}
	
	private void identifyLayers(ArrayList<SoftwareUnitDTO> units, String depedencyType) {
		// 1) Assign all internalRootPackages to bottom layer
		int layerId = 1;
		ArrayList<SoftwareUnitDTO> assignedUnits = new ArrayList<SoftwareUnitDTO>();
		assignedUnits.addAll(units);
		layersWithSoftwareUnitsMap.put(layerId, assignedUnits);

		// 2) Split the original bottom layer in a top layer and a (reduced) bottom layer, if appropriate.
		identifyTopLayerBasedOnUnitsInBottomLayer(layerId, depedencyType);

		// 3) Look iteratively for packages on top of the bottom layer, etc.
		while (layersWithSoftwareUnitsMap.lastKey() > layerId) {
			layerId++;
			identifyTopLayerBasedOnUnitsInBottomLayer(layerId, depedencyType);
		}
	}

	private void addIdentifiedLayersToIntendedArchitecture() {
		int highestLevelLayer = layersWithSoftwareUnitsMap.size();
		if (highestLevelLayer > 1) {
			// Reverse the layer levels. The numbering of the layers within the
			// intended architecture is different: the highest level layer has
			// hierarchcalLevel = 1
			int lowestLevelLayer = 1;
			int raise = highestLevelLayer - lowestLevelLayer;
			TreeMap<Integer, ArrayList<SoftwareUnitDTO>> tempLayers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
			for (int i = lowestLevelLayer; i <= highestLevelLayer; i++) {
				ArrayList<SoftwareUnitDTO> unitsOfLayer = layersWithSoftwareUnitsMap.get(i);
				int level = lowestLevelLayer + raise;
				tempLayers.put(level, unitsOfLayer);
				raise--;
			}
			layersWithSoftwareUnitsMap = tempLayers;
			
			int numberOfAddedLayers = 0;
			for (int level : layersWithSoftwareUnitsMap.keySet()) {
				ArrayList<ModuleDTO> modulesToBeMoved = new ArrayList<ModuleDTO>();
				for(SoftwareUnitDTO softwareUnitDTO : layersWithSoftwareUnitsMap.get(level)){
					modulesToBeMoved.add(defineService.getModule_BasedOnSoftwareUnitName(softwareUnitDTO.uniqueName));
				}
				
				ModuleDTO newModule = createModule_andAddItToReverseList("Layer" + level, selectedModule.logicalPath, "Layer", level, layersWithSoftwareUnitsMap.get(level));
				if (!newModule.logicalPath.equals("")) {
					numberOfAddedLayers ++;
				}
			}
			logger.info(" Number of added Layers: " + numberOfAddedLayers);
		}
	}
	
	private void identifyTopLayerBasedOnUnitsInBottomLayer(int bottomLayerId, String relationType) {
		ArrayList<SoftwareUnitDTO> assignedUnitsOriginalBottomLayer = layersWithSoftwareUnitsMap.get(bottomLayerId);
		@SuppressWarnings("unchecked")
		ArrayList<SoftwareUnitDTO> assignedUnitsBottomLayerClone = (ArrayList<SoftwareUnitDTO>) assignedUnitsOriginalBottomLayer.clone();
		ArrayList<SoftwareUnitDTO> assignedUnitsNewBottomLayer = new ArrayList<SoftwareUnitDTO>();
		ArrayList<SoftwareUnitDTO> assignedUnitsTopLayer = new ArrayList<SoftwareUnitDTO>();
		
		for (SoftwareUnitDTO softwareUnit : assignedUnitsOriginalBottomLayer) {
			boolean rootPackageDoesNotUseOtherPackage = true;
			
			for (SoftwareUnitDTO otherSoftwareUnit : assignedUnitsBottomLayerClone) {
				if (!otherSoftwareUnit.uniqueName.equals(softwareUnit.uniqueName)) {
					int nrOfDependenciesFromsoftwareUnitToOther = getRelationsBetweenSoftwareUnits(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName, relationType).size();
					int nrOfDependenciesFromOtherTosoftwareUnit = getRelationsBetweenSoftwareUnits(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName, relationType).size();
					if (nrOfDependenciesFromsoftwareUnitToOther > ((nrOfDependenciesFromOtherTosoftwareUnit / 100) * layerThreshold)) {
						rootPackageDoesNotUseOtherPackage = false;
					}
				}
			}
			
			if (rootPackageDoesNotUseOtherPackage) { // Leave unit in the lower layer
				assignedUnitsNewBottomLayer.add(softwareUnit);
			} else { // Assign unit to the higher layer
				assignedUnitsTopLayer.add(softwareUnit);
			}

		}
		if ((assignedUnitsTopLayer.size() > 0) && (assignedUnitsNewBottomLayer.size() > 0)) {
			layersWithSoftwareUnitsMap.remove(bottomLayerId);
			layersWithSoftwareUnitsMap.put(bottomLayerId, assignedUnitsNewBottomLayer);
			bottomLayerId++;
			layersWithSoftwareUnitsMap.put(bottomLayerId, assignedUnitsTopLayer);
		}
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

	// Returns the SUs assigned to selectedModule or, if only one SU is assigned, the children of this SU.
	// In case the selectedModule is a Component, the SUs assigned to the interface should not be returned. Prepare. 
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
	
	/** Returns the first set of children (number of children >= 2) in the decomposition hierarchy of the parent SoftwareUnit
	 * @param parentSoftwareUnit (of a SoftwareUnit)
	 * @return ArrayList<String> with unique names of children, or an empty list, if no child SoftwareUnits are existing.
	 */
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

}
