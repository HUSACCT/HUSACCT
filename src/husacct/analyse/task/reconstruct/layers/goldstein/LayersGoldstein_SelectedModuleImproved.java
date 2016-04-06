package husacct.analyse.task.reconstruct.layers.goldstein;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;

public class LayersGoldstein_SelectedModuleImproved extends AlgorithmGoldstein{

	
	private ModuleDTO selectedModule;
	private int layerThreshold;
	private IModelQueryService queryService;
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
	
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService, String xLibrariesRootPackage) {
		selectedModule = dto.getSelectedModule();
		layerThreshold = dto.getThreshold();
		this.queryService = queryService;
		IDefineSarService defineSarService = husacct.ServiceProvider.getInstance().getDefineService().getSarService();
		IDefineService defineService = husacct.ServiceProvider.getInstance().getDefineService();
		identifyLayers(getClasses(xLibrariesRootPackage), dto.getRelationType());
		for (int level : layers.keySet()) {
			ArrayList<ModuleDTO> modulesToBeMoved = new ArrayList<ModuleDTO>();
			for(SoftwareUnitDTO softwareUnitDTO : layers.get(level)){
				modulesToBeMoved.add(defineService.getModule_BasedOnSoftwareUnitName(softwareUnitDTO.uniqueName));
			}
			
			ModuleDTO newModule = defineSarService.addModule("Layer" + level, selectedModule.logicalPath, "Layer", level, layers.get(level));	
			addToReverseReconstructionList(newModule); //add to cache for reverse
		}
	}
	
	public ArrayList<SoftwareUnitDTO> getClasses(String library) {
		ArrayList<SoftwareUnitDTO> selectedSubmoduleWithClasses = new ArrayList<SoftwareUnitDTO>();
		IDefineService defineService = husacct.ServiceProvider.getInstance().getDefineService();
		
		ModuleDTO[] subModuleDTOs = selectedModule.subModules;
		for(ModuleDTO subModule : subModuleDTOs){
			for(String logicalSoftwarePath : defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath)){
				selectedSubmoduleWithClasses.add(queryService.getSoftwareUnitByUniqueName(logicalSoftwarePath));
			}
		}
		return selectedSubmoduleWithClasses;
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

		logger.info(" Number of added Layers: " + layers.size());
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
					case "umlDependency":
						nrOfDependenciesFromsoftwareUnitToOther = queryService.getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName).length;
						nrOfDependenciesFromOtherTosoftwareUnit = queryService.getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName).length;
						break;
						
					case "softwareUnitDependency":
						nrOfDependenciesFromsoftwareUnitToOther = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName).length;
						nrOfDependenciesFromOtherTosoftwareUnit = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName).length;
						break;
					}
					
					if (nrOfDependenciesFromsoftwareUnitToOther > ((nrOfDependenciesFromOtherTosoftwareUnit * 0.01) * layerThreshold)) {
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

}
