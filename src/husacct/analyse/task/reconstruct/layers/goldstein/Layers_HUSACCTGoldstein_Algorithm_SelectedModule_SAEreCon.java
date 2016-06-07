package husacct.analyse.task.reconstruct.layers.goldstein;

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
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.AlgorithmParameter;
import husacct.analyse.task.reconstruct.parameters.NumberFieldPanel;
import husacct.analyse.task.reconstruct.parameters.ParameterPanel;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.enums.ModuleTypes;

public class Layers_HUSACCTGoldstein_Algorithm_SelectedModule_SAEreCon extends IAlgorithm{
	private ModuleDTO selectedModule;
	private int backCallThreshold;
	private String typesOfDependencies;
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private ArrayList<SoftwareUnitDTO> softwareUnitsToIncludeInAlgorithm = new ArrayList<SoftwareUnitDTO>();
	private HashMap<String, SoftwareUnitDTO> softwareUnitsToExclude = new HashMap<String, SoftwareUnitDTO>();
	private GraphOfSuClusters graphOfSuClusters; // Each node in the graph represents 1-n SoftwareUnits. If *, it is a cohesive cluster of SUs.
	private TreeMap<Integer, Set<Integer>> layersWithNodesMap = new TreeMap<Integer, Set<Integer>>();
	private HashSet<Integer> notAssignedNodes;

	public Layers_HUSACCTGoldstein_Algorithm_SelectedModule_SAEreCon (IModelQueryService queryService) {
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
		notAssignedNodes = new HashSet<Integer>(allNodes);
		// 1) Assign to bottom layer all nodes without dependencies on other nodes. 
		int layerId = 0;
		assignNodesToBottomLayer(layerId, allNodes);
		if (!layersWithNodesMap.isEmpty()) {
			// 2) Iteratively create a new layer with dependencies on the lower layer and no dependencies on other 
			//		nodes in notAssignedNodes (or, in case of cyclic dependencies not above the backCallThreshold)..
			while (layersWithNodesMap.lastKey() > layerId) {
				layerId ++;
				identifyNewTopLayer(layerId);
			}
		}
	}

	private void assignNodesToBottomLayer(int layerId, Set<Integer> allNodes) {
		HashSet<Integer> assignedNodesToBottomLayer = new HashSet<Integer>();
		for (int fromNodeId : allNodes) {
			boolean fromNodeUsesAnotherNode = false;
			for (int toNodeId : allNodes) {
				if (fromNodeId != toNodeId) {
					int nrOfDependenciesFromNodeToToNode = graphOfSuClusters.getNumberOfDependencies(fromNodeId, toNodeId);
					int nrOfDependenciesFromToNodeToNode = graphOfSuClusters.getNumberOfDependencies(toNodeId, fromNodeId);
					if (nrOfDependenciesFromNodeToToNode > 0) {
						if (nrOfDependenciesFromNodeToToNode > nrOfDependenciesFromToNodeToNode) { // Filters out dependencies below backCallThreshold.
							fromNodeUsesAnotherNode = true;
						}
					}
				}
			}
			if (!fromNodeUsesAnotherNode) {
				// Assign unit to the bottom layer and remove it from notAssignedNodes
				assignedNodesToBottomLayer.add(fromNodeId);
				notAssignedNodes.remove(fromNodeId);
			}				
		}
		if (!assignedNodesToBottomLayer.isEmpty()) {
			layerId ++;
			layersWithNodesMap.put(layerId, assignedNodesToBottomLayer);
		}
	}
	
	private void identifyNewTopLayer(int bottomLayerId) {
		HashSet<Integer> assignedNodesNewTopLayer = new HashSet<Integer>();
		for (int fromNodeId : notAssignedNodes) {
			// Determine if fromNode uses a node in the bottom layer.
			boolean fromNodeUsesNodeInBottomLayer = false;
			for (int bottomLayerNodeId : layersWithNodesMap.get(bottomLayerId)) {
				int nrOfDependenciesFromNodeToBootomLayerNode = graphOfSuClusters.getNumberOfDependencies(fromNodeId, bottomLayerNodeId);
				if (nrOfDependenciesFromNodeToBootomLayerNode > 0) {
					fromNodeUsesNodeInBottomLayer = true;
				}
			}
			// Determine if fromNode uses a another node in notAssignedNodes.
			boolean fromNodeUsesAnotherNotAssignedNode = false;
			for (int toNodeId : notAssignedNodes) {
				if (fromNodeId != toNodeId) {
					int nrOfDependenciesFromNodeToToNode = graphOfSuClusters.getNumberOfDependencies(fromNodeId, toNodeId);
					int nrOfDependenciesFromToNodeToNode = graphOfSuClusters.getNumberOfDependencies(toNodeId, fromNodeId);
					if (nrOfDependenciesFromNodeToToNode > 0) {
						if (nrOfDependenciesFromNodeToToNode > nrOfDependenciesFromToNodeToNode) { // Filters out dependencies below backCallThreshold.
							fromNodeUsesAnotherNotAssignedNode = true;
						}
					}
				}
			}
			if (fromNodeUsesNodeInBottomLayer && !fromNodeUsesAnotherNotAssignedNode) {
				// Assign unit to the new top layer
				assignedNodesNewTopLayer.add(fromNodeId);
			} 
		}

		// Create a new top layer only if the top layer contain at least one node. 
		if (!assignedNodesNewTopLayer.isEmpty()) {
			int newTopLayerId = bottomLayerId + 1;
			layersWithNodesMap.put(newTopLayerId, assignedNodesNewTopLayer);
			// Remove added nodes from 
			for (int nodeId : assignedNodesNewTopLayer) {
				notAssignedNodes.remove(nodeId);
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
			// Reverse the layer levels. The numbering of the layers within the
			// intended architecture is different: the highest level layer has hierarchicalLevel = 1
			int lowestLevelLayer = 1;
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
			
			int numberOfAddedLayers = 0;
			for (int level : layersWithSoftwareUnitsMap.keySet()) {
				ModuleDTO newModule = createModule_andAddItToReverseList("Layer" + level, selectedModule.logicalPath, "Layer", level, layersWithSoftwareUnitsMap.get(level));
				if (!newModule.logicalPath.equals("")) {
					numberOfAddedLayers ++;
				}
			}
			logger.info(" Number of added Layers: " + numberOfAddedLayers);
		}
	}

	@Override
	public ReconstructArchitectureDTO getAlgorithmThresholdSettings() {
		ReconstructArchitectureDTO reconstructArchitecture = new ReconstructArchitectureDTO();
		reconstructArchitecture.approachConstant = AnalyseReconstructConstants.Algorithm.Layers_Goldstein_HUSACCT_SelectedModule;
		reconstructArchitecture.parameterPanels = createParameterPanels();
		reconstructArchitecture.threshold = 10;
		reconstructArchitecture.relationType = AnalyseReconstructConstants.RelationTypes.allDependencies;
		reconstructArchitecture.granularity = AnalyseReconstructConstants.Granularities.PackagesWithAllClasses;

		return reconstructArchitecture;
	}
	
	private ArrayList<ParameterPanel> createParameterPanels(){
		ArrayList<ParameterPanel> parameterPanels = new ArrayList<>();
		
		ParameterPanel numberField = new NumberFieldPanel("Threshold", AlgorithmParameter.Threshold, 10);
		numberField.defaultValue = 10;
		numberField.minimumValue = 0;
		numberField.maximumValue = 100;
		parameterPanels.add(numberField);
		
		return parameterPanels;
	}
}
