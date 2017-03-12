package husacct.analyse.task.reconstruct.algorithms.hu.layers.scanniello;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.analyse.task.reconstruct.dto.ReconstructArchitectureDTO;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.enums.ModuleTypes;


public class Layers_Scanniello_SelectedModule_Improved extends Layers_Scanniello_SuperClass{		
	ModuleDTO selectedModule;
	ArrayList<String> softwareUnitsInSelectedModuleList;
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private ArrayList<SoftwareUnitDTO> softwareUnitsToIncludeInAlgorithm = new ArrayList<SoftwareUnitDTO>();
	private HashMap<String, SoftwareUnitDTO> softwareUnitsToExclude = new HashMap<String, SoftwareUnitDTO>();
	
	public Layers_Scanniello_SelectedModule_Improved (IModelQueryService queryService) {
		super(queryService);
	}
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService)throws Exception {
		try {
			selectedModule = dto.getSelectedModule();
			if (selectedModule.logicalPath.equals("")) {
				selectedModule.logicalPath = "**"; // Root of intended software architecture
				selectedModule.type = "Root"; // Root of intended software architecture
			}
			this.threshold = dto.getThreshold();
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
	
			identifyAndCreateLayers(dto, softwareUnitsToIncludeInAlgorithm);

		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }

	}
		
	private void identifyAndCreateLayers(ReconstructArchitectureDTO dto, ArrayList<SoftwareUnitDTO> softwareUnitDTOsOfSelectedModule) {
		boolean firstIdentification = true;
		HashMap<Integer, ArrayList<SoftwareUnitDTO>> firstLayersIdentification = IdentifyLayersImproved(softwareUnitDTOsOfSelectedModule, dto, firstIdentification);
		ArrayList<ArrayList<SoftwareUnitDTO>> topLayers = new ArrayList<>();
		ArrayList<ArrayList<SoftwareUnitDTO>> bottomLayers = new ArrayList<>();
		ArrayList<SoftwareUnitDTO> middleLayer = new ArrayList<>();
		ArrayList<SoftwareUnitDTO> discLayer = new ArrayList<>();
		
		topLayers.add(firstLayersIdentification.get(topLayerKey));
		bottomLayers.add(firstLayersIdentification.get(bottomLayerKey));
		middleLayer = firstLayersIdentification.get(middleLayerKey);
		discLayer = firstLayersIdentification.get(discLayerKey);
		
		boolean topOrBottomAreNotEmpty = firstLayersIdentification.get(topLayerKey).size() > 0 || firstLayersIdentification.get(bottomLayerKey).size() > 0;
		while (topOrBottomAreNotEmpty){
			firstIdentification = false;
			HashMap<Integer, ArrayList<SoftwareUnitDTO>> newIdentifiedLayers = IdentifyLayersImproved(middleLayer, dto, firstIdentification);
			
			if (!newIdentifiedLayers.get(topLayerKey).isEmpty()){
				topLayers.add(newIdentifiedLayers.get(topLayerKey));
			}
			if (!newIdentifiedLayers.get(bottomLayerKey).isEmpty()){
				bottomLayers.add(newIdentifiedLayers.get(bottomLayerKey));
			}
			
			middleLayer = newIdentifiedLayers.get(middleLayerKey);
			topOrBottomAreNotEmpty = newIdentifiedLayers.get(topLayerKey).size() > 0 || newIdentifiedLayers.get(bottomLayerKey).size() > 0;
		}
		
		HashMap<Integer, ArrayList<SoftwareUnitDTO>> structuredLayers = RestructureLayers(topLayers, bottomLayers, middleLayer);
		buildStructure(structuredLayers, discLayer, selectedModule);
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

	@Override
	public ReconstructArchitectureDTO getAlgorithmParameterSettings() {
		ReconstructArchitectureDTO reconstructArchitecture = new ReconstructArchitectureDTO();
		reconstructArchitecture.approachConstant = AnalyseReconstructConstants.Algorithm.Layers_Scanniello_Improved;
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





