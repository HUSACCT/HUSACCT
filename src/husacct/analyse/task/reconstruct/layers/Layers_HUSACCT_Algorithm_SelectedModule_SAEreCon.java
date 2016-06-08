package husacct.analyse.task.reconstruct.layers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.GraphOfSuClusters;
import husacct.analyse.task.reconstruct.IAlgorithm;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.enums.ModuleTypes;

public class Layers_HUSACCT_Algorithm_SelectedModule_SAEreCon extends IAlgorithm{
	private ModuleDTO selectedModule;
	private int backCallThreshold;
	private String typesOfDependencies;
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private ArrayList<SoftwareUnitDTO> softwareUnitsToIncludeInAlgorithm = new ArrayList<SoftwareUnitDTO>();
	private HashMap<String, SoftwareUnitDTO> softwareUnitsToExclude = new HashMap<String, SoftwareUnitDTO>();
	private GraphOfSuClusters graphOfSuClusters; // Each node in the graph represents 1-n SoftwareUnits. If *, it is a cohesive cluster of SUs. 
	private TreeMap<Integer, Set<Integer>> layersWithNodesMap = new TreeMap<Integer, Set<Integer>>();

	public Layers_HUSACCT_Algorithm_SelectedModule_SAEreCon (IModelQueryService queryService) {
		super(queryService);
		graphOfSuClusters = new GraphOfSuClusters(this);
	}
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) {
		try {
			selectedModule = dto.getSelectedModule();
			if (selectedModule.logicalPath.equals("")) {
				selectedModule.logicalPath = "**"; // Root of intended software architecture
				selectedModule.type = "Root"; // Root of intended software architecture
			}
			backCallThreshold = dto.getThreshold();
			typesOfDependencies = dto.getRelationType();

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
			
			graphOfSuClusters.initializeGraph(softwareUnitsToIncludeInAlgorithm, typesOfDependencies, backCallThreshold);
			Set<Integer> allNodes = graphOfSuClusters.getNodes();
	
			identifyLayers(allNodes);
			addIdentifiedLayersToIntendedArchitecture();
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }

	}
	
	private void identifyLayers(Set<Integer> allNodes) {
		// 1) Assign all internalRootPackages to bottom layer
		int layerId = 1;
		layersWithNodesMap.put(layerId, allNodes);

		// 2) Split the original bottom layer in a top layer and a (reduced) bottom layer, if appropriate.
		identifyTopLayerBasedOnUnitsInBottomLayer(layerId);

		// 3) Look iteratively for a new layer of nodes on top of the bottom layer, etc.
		while (layersWithNodesMap.lastKey() > layerId) {
			layerId++;
			identifyTopLayerBasedOnUnitsInBottomLayer(layerId);
		}
	}

	/**
	 * Logic: Move a software unit su1 from the current bottom layer to a new top layer
	 * if the software unit is using another software unit su2 in the current bottom layer.
	 * Complication: If su2 is using su1 as well and the backCallPercentage ((su2-->su1 / su1-->su2) x 100) is bigger than 
	 * the callBackThreshold percentage, than su1 and su2 need to stay in the bottom layer (or both be moved to the top layer in case
	 * su1 and/or su2 is using another software unit su3 in the current bottom layer).   
	 * @param bottomLayerId
	 */
	private void identifyTopLayerBasedOnUnitsInBottomLayer(int bottomLayerId) {
		HashSet<Integer> assignedNodesNewBottomLayer = new HashSet<Integer>();
		HashSet<Integer> assignedNodesNewTopLayer = new HashSet<Integer>();
		for (int fromNodeId : layersWithNodesMap.get(bottomLayerId)) {
			boolean fromNodeUsesAnotherNode = false;
			for (int toNodeId : layersWithNodesMap.get(bottomLayerId)) {
				if (fromNodeId != toNodeId) {
					int nrOfDependenciesFromNodeToToNode = graphOfSuClusters.getNumberOfDependencies(fromNodeId, toNodeId);
					int nrOfDependenciesFromToNodeToNode = graphOfSuClusters.getNumberOfDependencies(toNodeId, fromNodeId);
					if (nrOfDependenciesFromNodeToToNode > 0) {
						if (nrOfDependenciesFromNodeToToNode > nrOfDependenciesFromToNodeToNode) {
							fromNodeUsesAnotherNode = true;
						}
					}
				}
			}
			if (fromNodeUsesAnotherNode) {
				// Assign unit to the new top layer
				assignedNodesNewTopLayer.add(fromNodeId);
			} else { 
				// Leave unit in the lower layer
				assignedNodesNewBottomLayer.add(fromNodeId);
			}
		}

		// Create a new top layer only if both new bottom layer and top layer contain at least one node. 
		if (!assignedNodesNewTopLayer.isEmpty() && !assignedNodesNewBottomLayer.isEmpty()) {
			layersWithNodesMap.remove(bottomLayerId);
			layersWithNodesMap.put(bottomLayerId, assignedNodesNewBottomLayer);
			int newTopLayerId = bottomLayerId + 1;
			layersWithNodesMap.put(newTopLayerId, assignedNodesNewTopLayer);
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

	private void addIdentifiedLayersToIntendedArchitecture() {
		int highestLevelLayer = layersWithNodesMap.size();
		if (highestLevelLayer >= 2) {
			// Determine the hierarchicalLevel of the layers. The hierarchicalLevel of the layers within the
			// intended architecture is reversed to the one presented in the name to the users: the highest level layer has hierarchicalLevel = 1.
			int hierarchicalLevel = highestLevelLayer;
			TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layersWithSoftwareUnitsMap = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
			for (int levelInName : layersWithNodesMap.keySet()) {
				Set<Integer> nodesOfLayer = layersWithNodesMap.get(levelInName);
				ArrayList<SoftwareUnitDTO> unitsOfLayer = new ArrayList<SoftwareUnitDTO>();
				for (int nodeId : nodesOfLayer) {
					unitsOfLayer.addAll(graphOfSuClusters.getSoftwareUnitsOfNode(nodeId));
				}
				layersWithSoftwareUnitsMap.put(hierarchicalLevel, unitsOfLayer);
				hierarchicalLevel --;
			}
			
/*			int lowestLevelLayer = 1;
			int raise = highestLevelLayer - lowestLevelLayer;
			TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layersWithSoftwareUnitsMap = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
			for (int i = lowestLevelLayer; i <= highestLevelLayer; i++) {
				Set<Integer> nodesOfLayer = layersWithNodesMap.get(i);
				ArrayList<SoftwareUnitDTO> unitsOfLayer = new ArrayList<SoftwareUnitDTO>();
				for (int nodeId : nodesOfLayer) {
					unitsOfLayer.addAll(graphOfSuClusters.getSoftwareUnitsOfNode(nodeId));
				}
				int level = lowestLevelLayer + raise;
				layersWithSoftwareUnitsMap.put(level, unitsOfLayer);
				raise--;
			}
*/			
			int numberOfAddedLayers = 0;
			int levelInName = highestLevelLayer;
			for (int hierarchyLevel : layersWithSoftwareUnitsMap.keySet()) {
				ArrayList<SoftwareUnitDTO> unitsOfLayer = layersWithSoftwareUnitsMap.get(hierarchyLevel);
				String nameOfLayer = "Layer" + levelInName + determineLayerNameExtension(unitsOfLayer);
				ModuleDTO newModule = createModule_andAddItToReverseList(nameOfLayer, selectedModule.logicalPath, "Layer", hierarchyLevel, unitsOfLayer);
				if (!newModule.logicalPath.equals("")) {
					numberOfAddedLayers ++;
					levelInName --;
				}
			}

			logger.info(" Number of added Layers: " + numberOfAddedLayers);
		}
	}

	private String determineLayerNameExtension(ArrayList<SoftwareUnitDTO> unitsOfLayer) {
		String nameExtension = "";
		if (unitsOfLayer.size() == 1) {
			SoftwareUnitDTO unit = unitsOfLayer.get(0);
			if (!unit.name.equals("")) {
				if (unit.name.length() > 12) {
					nameExtension = "_" + unit.name.substring(0, 11);
				} else {
					nameExtension = "_" + unit.name;
				}
			}
		} else {
			String nameLargestUnit = "";
			int highestLinesOfCode = 0; 
			for (SoftwareUnitDTO unit : unitsOfLayer) {
				int linesOfCode = queryService.getAnalysisStatistics(unit).selectionNrOfLinesOfCode;
				if (linesOfCode > highestLinesOfCode) {
					highestLinesOfCode = linesOfCode;
					nameLargestUnit = unit.name;
				}
			}
			if (!nameLargestUnit.equals("")) {
				if (nameLargestUnit.length() > 12) {
					nameLargestUnit = nameLargestUnit.substring(0, 11);
				}
				nameExtension = "_" + nameLargestUnit + "_etc"; 
			}
		}
		return nameExtension;
	}
	
	@Override
	public ReconstructArchitectureDTO getAlgorithmThresholdSettings() {
		ReconstructArchitectureDTO reconstructArchitecture = new ReconstructArchitectureDTO();
		reconstructArchitecture.approachConstant = AnalyseReconstructConstants.Algorithm.Layers_HUSACCT_SelectedModule;
		reconstructArchitecture.threshold = 10;
		reconstructArchitecture.relationType = AnalyseReconstructConstants.RelationTypes.allDependencies;
		reconstructArchitecture.granularity = AnalyseReconstructConstants.Granularities.PackagesWithAllClasses;
		reconstructArchitecture.parameterDTOs = createParameterPanels();
		return reconstructArchitecture;
	}
	
	private ArrayList<ReconstructArchitectureParameterDTO> createParameterPanels(){
		ArrayList<ReconstructArchitectureParameterDTO> parameterDTOs = new ArrayList<>();
		parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createThresholdParameter(10));
		parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createRelationTypeParameter(AnalyseReconstructConstants.RelationTypes.allDependencies));
		parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createGranularityPanel(AnalyseReconstructConstants.Granularities.PackagesWithAllClasses));
		return parameterDTOs;
	}
}
