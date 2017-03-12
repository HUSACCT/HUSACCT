package husacct.analyse.task.reconstruct.algorithms.hu.layers.goldstein;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.analyse.task.reconstruct.algorithms.Algorithm_SuperClass;
import husacct.analyse.task.reconstruct.dto.ReconstructArchitectureDTO;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.SoftwareUnitDTO;

public class Layers_Goldstein_Root_Initial extends Algorithm_SuperClass{
		private int layerThreshold;
		private ArrayList<SoftwareUnitDTO> internalRootPackagesWithClasses;
		private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
		private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
		
		public Layers_Goldstein_Root_Initial (IModelQueryService queryService) {
			super(queryService);
		}
			
		@Override
		public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) {
			layerThreshold = dto.getThreshold();
			determineInternalRootPackagesWithClasses();
			//identifyLayers();
			identifyLayersAtRootLevel(dto.getRelationType());
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
				// Temporal solution useful for HUSACCT20 test. To be improved! E.g., classes in root are excluded from the process. 
				String newRoot = internalRootPackagesWithClasses. get(0).uniqueName;
				internalRootPackagesWithClasses = new ArrayList<SoftwareUnitDTO>();
				for (SoftwareUnitDTO child : queryService.getChildUnitsOfSoftwareUnit(newRoot)) {
					if (child.type.equalsIgnoreCase("package")) {
						internalRootPackagesWithClasses.add(child);
					}
				}
			}
		}

	// Code below is not used in version 28/03/2016	
		
		public ArrayList<SoftwareUnitDTO> getClasses(String library) {
			internalRootPackagesWithClasses = new ArrayList<SoftwareUnitDTO>();
			SoftwareUnitDTO[] allRootUnits = queryService.getSoftwareUnitsInRoot();
			for (SoftwareUnitDTO rootModule : allRootUnits) {
				if (!rootModule.uniqueName.equals(library)) {
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
			return internalRootPackagesWithClasses;
		}

		private void identifyLayersAtRootLevel(String dependencyType) {
			determineInternalRootPackagesWithClasses();
			identifyLayers(internalRootPackagesWithClasses, dependencyType);
			
			int numberOfAddedLayers = 0;
			
			for (Integer hierarchicalLevel : layers.keySet()) {
				ModuleDTO newModule = defineSarService.addModule("Layer" + hierarchicalLevel, "**", "Layer", hierarchicalLevel, layers.get(hierarchicalLevel));
				if (!newModule.logicalPath.equals("")) {
					numberOfAddedLayers ++;
					addToReverseReconstructionList(newModule); //add to cache for reverse
				}
			}
			logger.info(" Number of added Layers: " + numberOfAddedLayers);
		}
		
		private void identifyLayers(ArrayList<SoftwareUnitDTO> units, String depedencyType) {
			// 1) Assign all internalRootPackages to bottom layer
			int layerId = 1;
			ArrayList<SoftwareUnitDTO> assignedUnits = new ArrayList<SoftwareUnitDTO>();
			assignedUnits.addAll(units);
			layers.put(layerId, assignedUnits);

			// 2) Identify the bottom layer. Look for packages with dependencies to
			// external systems only.
			identifyTopLayerBasedOnUnitsInBottomLayer(layerId, depedencyType);

			// 3) Look iteratively for packages on top of the bottom layer, et
			// cetera.
			while (layers.lastKey() > layerId) {
				layerId++;
				identifyTopLayerBasedOnUnitsInBottomLayer(layerId, depedencyType);
			}

			// 4) Add the layers to the intended architecture
			int highestLevelLayer = layers.size();
			if (highestLevelLayer > 1) {
				// Reverse the layer levels. The numbering of the layers within the
				// intended architecture is different: the highest level layer has
				// hierarchcalLevel = 1
				int lowestLevelLayer = 1;
				int raise = highestLevelLayer - lowestLevelLayer;
				TreeMap<Integer, ArrayList<SoftwareUnitDTO>> tempLayers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
				for (int i = lowestLevelLayer; i <= highestLevelLayer; i++) {
					ArrayList<SoftwareUnitDTO> unitsOfLayer = layers.get(i);
					int level = lowestLevelLayer + raise;
					tempLayers.put(level, unitsOfLayer);
					raise--;
				}
				layers = tempLayers;
				
			}
		}
		
		private void identifyTopLayerBasedOnUnitsInBottomLayer(int bottomLayerId, String dependencyType) {
			ArrayList<SoftwareUnitDTO> assignedUnitsOriginalBottomLayer = layers.get(bottomLayerId);
			@SuppressWarnings("unchecked")
			ArrayList<SoftwareUnitDTO> assignedUnitsBottomLayerClone = (ArrayList<SoftwareUnitDTO>) assignedUnitsOriginalBottomLayer
					.clone();
			ArrayList<SoftwareUnitDTO> assignedUnitsNewBottomLayer = new ArrayList<SoftwareUnitDTO>();
			ArrayList<SoftwareUnitDTO> assignedUnitsTopLayer = new ArrayList<SoftwareUnitDTO>();
			
			for (SoftwareUnitDTO softwareUnit : assignedUnitsOriginalBottomLayer) {
				boolean rootPackageDoesNotUseOtherPackage = true;
				
				for (SoftwareUnitDTO otherSoftwareUnit : assignedUnitsBottomLayerClone) {
					if (!otherSoftwareUnit.uniqueName.equals(softwareUnit.uniqueName)) {
						int nrOfDependenciesFromsoftwareUnitToOther =0;
						int nrOfDependenciesFromOtherTosoftwareUnit=0;
						
						switch(dependencyType){
						case AnalyseReconstructConstants.RelationTypes.umlLinks:
							nrOfDependenciesFromsoftwareUnitToOther = queryService.getUmlLinksFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName).length;
							nrOfDependenciesFromOtherTosoftwareUnit = queryService.getUmlLinksFromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName).length;
							break;
							
						case AnalyseReconstructConstants.RelationTypes.accessCallReferenceDependencies:
							nrOfDependenciesFromsoftwareUnitToOther = queryService.getDependencies_OnlyAccessCallAndReferences_FromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName).length;
							nrOfDependenciesFromOtherTosoftwareUnit = queryService.getDependencies_OnlyAccessCallAndReferences_FromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName).length;
							break;
							
						case AnalyseReconstructConstants.RelationTypes.allDependencies:
							nrOfDependenciesFromsoftwareUnitToOther = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName).length;
							nrOfDependenciesFromOtherTosoftwareUnit = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName).length;
							break;
						}
						
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
				layers.remove(bottomLayerId);
				layers.put(bottomLayerId, assignedUnitsNewBottomLayer);
				bottomLayerId++;
				layers.put(bottomLayerId, assignedUnitsTopLayer);
			}
		}

		@Override
		public ReconstructArchitectureDTO getAlgorithmParameterSettings() {
			ReconstructArchitectureDTO reconstructArchitecture = new ReconstructArchitectureDTO();
			reconstructArchitecture.approachConstant = AnalyseReconstructConstants.Algorithm.Layers_Goldstein_Root_Original;
			reconstructArchitecture.threshold = 10;
			reconstructArchitecture.relationType = AnalyseReconstructConstants.RelationTypes.allDependencies;
			reconstructArchitecture.granularity = AnalyseReconstructConstants.Granularities.Packages;
			reconstructArchitecture.parameterDTOs = createParameterPanels();
			return reconstructArchitecture;
		}
		
		private ArrayList<ReconstructArchitectureParameterDTO> createParameterPanels(){
			ArrayList<ReconstructArchitectureParameterDTO> parameterDTOs = new ArrayList<>();
			parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createThresholdParameter(10));
			parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createRelationTypeParameter(AnalyseReconstructConstants.RelationTypes.allDependencies));
			parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createGranularityPanel(AnalyseReconstructConstants.Granularities.Packages));
			return parameterDTOs;
		}

}
