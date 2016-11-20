package husacct.analyse.task.reconstruct.algorithms;

import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Granularities;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

public class GraphOfSuClusters {
	
	HashMap<Integer, HashSet<SoftwareUnitDTO>> nodes; 	// Key = nodeId, Value = SU assigned to cohesive cluster of SU's.
	HashMap<String, Integer> edges; 					// Key = fromNodeId1 + "." + toNodeId2, Value = number of dependencies from node1 to node2.
	Algorithm_SuperClass iAlgoritm;
	private int backCallThreshold;
	private String typesOfDependencies;
	private String granularity;
	private String selectedModule;
	private final Logger logger = Logger.getLogger(GraphOfSuClusters.class);
	
	public GraphOfSuClusters(Algorithm_SuperClass iAlgoritm) {
		nodes = new HashMap<Integer, HashSet<SoftwareUnitDTO>>();
		edges = new HashMap<String, Integer>();
		this.iAlgoritm = iAlgoritm;
	}

	public void initializeGraph(ArrayList<SoftwareUnitDTO> softwareUnitsToInclude, ReconstructArchitectureDTO dto) {
		try {
			backCallThreshold = dto.getThreshold();
			typesOfDependencies = dto.getRelationType();
			granularity = dto.granularity;
			selectedModule = dto.getSelectedModule().name;
			createNodesInClusteredGraph(softwareUnitsToInclude);
			setEdges(typesOfDependencies);
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }
	}
	
	// The returned set is backed by the map, so changes to the map are reflected in the set, and vice-versa. Beware of concurrent modifications.
	public Set<Integer> getNodes() {
		return nodes.keySet();
	}
	
	// Returns null if this map contains no mapping for uniqueName.
	public HashSet<SoftwareUnitDTO> getSoftwareUnitsOfNode(int nodeId) {
		return nodes.get(nodeId);
	}

	public int getNumberOfDependencies(int fromNodeId, int toNodeId) {
		int returnValue = 0;
		String searchKey = fromNodeId + "." + toNodeId; 
		if (edges.containsKey(searchKey)) {
			returnValue = edges.get(searchKey);
		}
		return returnValue;
	}
	
	/**
	 * Creates a graph of clustered SotwareUnits. Each node is a software unit, or a cluster of units that are highly coupled.
	 * Individual types may be clustered into one node, based on the granularity setting. 
	 * A SoftwareUnit without cyclic dependencies (or a cyclic dependency below the backCallThreshold) gets its own node.
	 * If SoftwareUnit su1 is using su2 and su2 is using su1 as well and the backCallThreshold ((su2-->su1 / su1-->su2) x 100) is bigger than 
	 * the callBackThreshold percentage, than su1 and su2 are clustered into one node, since these software units need to stay in the same layer.
	 * If su3 is also tightly coupled with su1 or su2, it is added to this cluster as well.   
	 */
	private void createNodesInClusteredGraph(ArrayList<SoftwareUnitDTO> softwareUnitsToInclude) {
		int nodeIdofNodeWithClassesInRoot = 1;
		int highestNodeId = 1;
		ArrayList<SoftwareUnitDTO> softwareUnitsToIncludeClone = new ArrayList<SoftwareUnitDTO> (softwareUnitsToInclude);
		
		// 1) If fromSoftwareUnit is not of type "package", while the granularity is set to packages, than add this SU to NodeWithClassesInRoot
		for (SoftwareUnitDTO fromSoftwareUnit : softwareUnitsToInclude) {
			// If fromSoftwareUnit is of type "class", while the granularity setting excludes classes, than add this SU to NodeWithClassesInRoot
			if (!fromSoftwareUnit.type.toLowerCase().equals("package")) {
				if (granularity.equals(AnalyseReconstructConstants.Granularities.Packages)) {
					if (!nodes.containsKey(nodeIdofNodeWithClassesInRoot)) {
						addNode(nodeIdofNodeWithClassesInRoot);
					}
					addSoftwareUnitToNode(nodeIdofNodeWithClassesInRoot, fromSoftwareUnit);
				}
			}
		}
		// 2) Add all software units to a node and cluster units with direct cyclic dependencies above backCallThreshold into one node.
		for (SoftwareUnitDTO fromSoftwareUnit : softwareUnitsToInclude) {
			/* Test code
			if (fromSoftwareUnit.name.contains("productdispatcher")) {
				boolean breakpoint = true;
			} */
			
			for (SoftwareUnitDTO toSoftwareUnit : softwareUnitsToIncludeClone) {
				if (!toSoftwareUnit.uniqueName.equals(fromSoftwareUnit.uniqueName)) {
					int nrOfDependenciesFromTo = iAlgoritm.getRelationsBetweenSoftwareUnits(fromSoftwareUnit.uniqueName, toSoftwareUnit.uniqueName, typesOfDependencies).size();
					int nrOfDependenciesToFrom = iAlgoritm.getRelationsBetweenSoftwareUnits(toSoftwareUnit.uniqueName, fromSoftwareUnit.uniqueName, typesOfDependencies).size();
					if (nrOfDependenciesFromTo > 0) {
						if (nrOfDependenciesToFrom > 0) {
							if (nrOfDependenciesFromTo >= nrOfDependenciesToFrom) { // Prevents erroneously added highly coupled units.
								int backCallPercentage = ((nrOfDependenciesToFrom * 100) / nrOfDependenciesFromTo);
								if (backCallPercentage > backCallThreshold) {
									// SoftwareUnit and otherSoftwareUnit are highly coupled.
									// Per from and to: Find node that contains the unit
									int nodeIdFrom = findNodeThatContainsSoftwareUnit(fromSoftwareUnit);
									int nodeIdTo = findNodeThatContainsSoftwareUnit(toSoftwareUnit);
									if ((nodeIdFrom == 0) && (nodeIdTo == 0)) {
										// If no node is found: a) raise highestNodeId; b) create new node with both units. 
										highestNodeId ++;
										addNode(highestNodeId);
										addSoftwareUnitToNode(highestNodeId, fromSoftwareUnit);
										addSoftwareUnitToNode(highestNodeId, toSoftwareUnit);
									} else if ((nodeIdFrom != 0) && (nodeIdTo == 0)) {
										// If one node is found, add units to node.
										addSoftwareUnitToNode(nodeIdFrom, toSoftwareUnit);
									} else if ((nodeIdFrom == 0) && (nodeIdTo != 0)){
										addSoftwareUnitToNode(nodeIdTo, fromSoftwareUnit);
									} else if ((nodeIdFrom != 0) && (nodeIdTo != 0) && (nodeIdFrom != nodeIdTo)) {
										// If both nodes are found, add the units of one node to the other node, and remove the empty node.
										for (SoftwareUnitDTO su : getSoftwareUnitsOfNode(nodeIdTo)) {
											addSoftwareUnitToNode(nodeIdFrom, su);
										}
										nodes.remove(nodeIdTo);
									}
								}
							}
						}
					}
				}
			}
		}

		// 3) Add non-coupled units as nodes to graph 
		for	(SoftwareUnitDTO softwareUnit : softwareUnitsToIncludeClone) {
			int clusterId = findNodeThatContainsSoftwareUnit(softwareUnit);
			if (clusterId == 0) {
				highestNodeId ++;
				addNode(highestNodeId);
				addSoftwareUnitToNode(highestNodeId, softwareUnit);
				
			}
		}
		// 4) Iteratively, merge nodes with direct cyclic dependencies above backCallThreshold.
		try {
			HashMap<Integer, Integer> nodesToMerge = new HashMap<Integer, Integer>();
			boolean repeatClusteringOverAllNodes = true;
			while (repeatClusteringOverAllNodes) {
				setEdges(typesOfDependencies);
				repeatClusteringOverAllNodes = false;
				for (int fromNodeId : getNodes()) {
					for (int toNodeId : getNodes()) {
						if (fromNodeId != toNodeId) {
							int nrOfDependenciesFromTo = getNumberOfDependencies(fromNodeId, toNodeId);
							int nrOfDependenciesToFrom = getNumberOfDependencies(toNodeId, fromNodeId);
							if (nrOfDependenciesFromTo > 0) {
								if (nrOfDependenciesToFrom > 0) {
									if (nrOfDependenciesFromTo >= nrOfDependenciesToFrom) { // Prevents erroneously added highly coupled units.
										int backCallPercentage = ((nrOfDependenciesToFrom * 100) / nrOfDependenciesFromTo);
										if (backCallPercentage > backCallThreshold) {
											// The two nodes are highly coupled, so merge them and repeat the procedure
											nodesToMerge.put(fromNodeId, toNodeId);
											repeatClusteringOverAllNodes = true;
											logger.info(" Merging: " + selectedModule + " " + fromNodeId  + " " + toNodeId );
										}
									}
								}
							}
						}
					}
					for (int fromNode : nodesToMerge.keySet()) {
						int toNode = nodesToMerge.get(fromNode);
						if (nodes.containsKey(toNode)) {
							for (SoftwareUnitDTO su : getSoftwareUnitsOfNode(toNode)) {
								addSoftwareUnitToNode(fromNode, su);
							}
							nodes.remove(toNode);
						}
					}
					if (nodesToMerge.size() > 0) {
						break;
					}
				}
			}
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	        //e.printStackTrace();
	    }
	}
	
	private void setEdges(String typesOfDependencies) {
		for (int fromNode : getNodes()) {
			for (int toNode : getNodes()) {
				int totalNumber = 0;
				for (SoftwareUnitDTO fromSoftwareUnit : nodes.get(fromNode)) {
					for (SoftwareUnitDTO toSoftwareUnit : nodes.get(toNode)) {
						if (!toSoftwareUnit.uniqueName.equals(fromSoftwareUnit.uniqueName)) {
							int nrOfDependenciesFromTo = iAlgoritm.getRelationsBetweenSoftwareUnits(fromSoftwareUnit.uniqueName, toSoftwareUnit.uniqueName, typesOfDependencies).size();
							totalNumber = totalNumber + nrOfDependenciesFromTo;
						}
					}
				}
				String searchKey = fromNode + "." + toNode; 
				edges.put(searchKey, totalNumber);
			}
		}
	}

	// Returns nodeId if node that contains softwareUnit, or 0 if it is not contained in a node. 
	private int findNodeThatContainsSoftwareUnit(SoftwareUnitDTO softareUnit) {
		int returnValue = 0;
		for (int nodeId : getNodes()) {
			for (SoftwareUnitDTO unitOfNode : getSoftwareUnitsOfNode(nodeId)) {
				if (unitOfNode.uniqueName.equals(softareUnit.uniqueName)) {
					return nodeId;
				}
			}
		}
		return returnValue;
	}
	
	private void addNode(int nodeId) {
		HashSet<SoftwareUnitDTO> softwareUnits = new HashSet<SoftwareUnitDTO>();
		nodes.put(nodeId, softwareUnits);
	}
	
	private void addSoftwareUnitToNode(int nodeId, SoftwareUnitDTO softwareUnit) {
		if (nodes.containsKey(nodeId)) {
			HashSet<SoftwareUnitDTO> softwareUnits = nodes.get(nodeId);
			softwareUnits.add(softwareUnit);
			nodes.put(nodeId, softwareUnits);
		}
	}


}
