package husacct.analyse.task.reconstruct;

import husacct.common.dto.SoftwareUnitDTO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class GraphOfSuClusters {
	
	HashMap<Integer, HashSet<SoftwareUnitDTO>> nodes; 	// Key = clusterIdx, Value = SU assigned to cohesive cluster of SU's.
	HashMap<String, Integer> edges; 					// Key = clusterId1 + | + clusterId2, Value = number of dependencies from cluster1 to cluster2.
	IAlgorithm iAlgoritm;
	
	public GraphOfSuClusters(IAlgorithm iAlgoritm) {
		nodes = new HashMap<Integer, HashSet<SoftwareUnitDTO>>();
		edges = new HashMap<String, Integer>();
		this.iAlgoritm = iAlgoritm;
	}

	public void initializeGraph(ArrayList<SoftwareUnitDTO> softwareUnitsToInclude, String typesOfDependencies, int backCallThreshold) {
		createNodesInClusteredGraph(softwareUnitsToInclude, typesOfDependencies, backCallThreshold);
		setEdges(typesOfDependencies);
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
	 * A SoftwareUnit without cyclic dependencies (or a cyclic dependency below the backCallThreshold) gets its own node.
	 * If SoftwareUnit su1 is using su2 and su2 is using su1 as well and the backCallThreshold ((su2-->su1 / su1-->su2) x 100) is bigger than 
	 * the callBackThreshold percentage, than su1 and su2 are clustered into one node, since these software units need to stay in the same layer.
	 * If su3 is also tightly coupled with su1 or su2, it is added to this cluster as well.   
	 */
	private void createNodesInClusteredGraph(ArrayList<SoftwareUnitDTO> softwareUnitsToInclude, String typesOfDependencies, int backCallThreshold) {
		int highestNodeId = 0;
		@SuppressWarnings("unchecked")
		ArrayList<SoftwareUnitDTO> softwareUnitsToIncludeClone = (ArrayList<SoftwareUnitDTO>) softwareUnitsToInclude.clone();
		
		for (SoftwareUnitDTO fromSoftwareUnit : softwareUnitsToInclude) {
			/* Test code
			if (softwareUnit.name.equals("Service")) {
				boolean breakpoint = true;
			} */
			for (SoftwareUnitDTO toSoftwareUnit : softwareUnitsToIncludeClone) {
				if (!toSoftwareUnit.uniqueName.equals(fromSoftwareUnit.uniqueName)) {
					int nrOfDependenciesFromsoftwareUnitToOther = iAlgoritm.getRelationsBetweenSoftwareUnits(fromSoftwareUnit.uniqueName, toSoftwareUnit.uniqueName, typesOfDependencies).size();
					int nrOfDependenciesFromOtherTosoftwareUnit = iAlgoritm.getRelationsBetweenSoftwareUnits(toSoftwareUnit.uniqueName, fromSoftwareUnit.uniqueName, typesOfDependencies).size();
					if (nrOfDependenciesFromsoftwareUnitToOther > 0) {
						if (nrOfDependenciesFromOtherTosoftwareUnit > 0) {
							if (nrOfDependenciesFromsoftwareUnitToOther >= nrOfDependenciesFromOtherTosoftwareUnit) { // Prevents erroneously added highly coupled units.
								int backCallPercentage = ((nrOfDependenciesFromOtherTosoftwareUnit * 100) / nrOfDependenciesFromsoftwareUnitToOther);
								if (backCallPercentage > backCallThreshold) {
									// SoftwareUnit and otherSoftwareUnit are highly coupled.
									// Per from and to: Find node that contains the unit
									int nodeIdFrom = findClusterThatContainsSoftwareUnit(fromSoftwareUnit);
									int nodeIdTo = findClusterThatContainsSoftwareUnit(fromSoftwareUnit);
									if ((nodeIdFrom == 0) && (nodeIdTo == 0)) {
										// If no cluster is found: a) raise highestNodeId; create new node with both units. 
										highestNodeId ++;
										addNode(highestNodeId);
										addSoftwareUnitToNode(highestNodeId, fromSoftwareUnit);
										addSoftwareUnitToNode(highestNodeId, toSoftwareUnit);
									} else {
										// If a node is found, add units to node.
										if (nodeIdFrom != 0) {
											addSoftwareUnitToNode(nodeIdFrom, toSoftwareUnit);
										} else if (nodeIdTo != 0){
											addSoftwareUnitToNode(nodeIdTo, fromSoftwareUnit);
										} 
									}
								}
							}
						}
					}
				}
			}
		}

		// Add non-coupled units as nodes to graph 
		for	(SoftwareUnitDTO softwareUnit : softwareUnitsToIncludeClone) {
			int clusterId = findClusterThatContainsSoftwareUnit(softwareUnit);
			if (clusterId == 0) {
				highestNodeId ++;
				addNode(highestNodeId);
				addSoftwareUnitToNode(highestNodeId, softwareUnit);
				
			}
		}
	}
	
	private void setEdges(String typesOfDependencies) {
		for (int fromNode : getNodes()) {
			/* Test code
			if (fromSoftwareUnit.equals("Service")) {
				boolean breakpoint = true;
			} */
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

	// Returns clusterId if cluster that contains softwareUnit, or 0 if it is not contained in a cluster. 
	private int findClusterThatContainsSoftwareUnit(SoftwareUnitDTO softareUnit) {
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
