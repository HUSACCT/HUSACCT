package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineService;

public class AlgorithmSelectedModule extends AlgorithmGeneral{
	private ModuleDTO selectedModule;
	private int layerThreshold;
	private IModelQueryService queryService;
	private ArrayList<SoftwareUnitDTO> selectedModuleWithClasses;
	
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers;
	
	
	
	@Override
	public void define(ModuleDTO Module, int th, IModelQueryService qService) {
		selectedModule = Module;
		layerThreshold = th;
		queryService = qService;
		
		//identifyLayersAtSelectedModule();
	}

	
	
	@Override
	public ArrayList<SoftwareUnitDTO> determineSelectedModuleWithClasses() {
		ArrayList<SoftwareUnitDTO> selectedModuleWithClasses = new ArrayList<SoftwareUnitDTO>();
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		
		ModuleDTO[] selectedSubModules = selectedModule.subModules;
		for (ModuleDTO subModule : selectedSubModules) {
			for(String localpath : defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath))
			selectedModuleWithClasses
					.add(queryService.getSoftwareUnitByUniqueName(localpath));
		}
		System.out.println("----------");
		System.out.println(selectedModuleWithClasses);
		System.out.println("----------");
		
		return selectedModuleWithClasses;
	}
	
	public void identifyLayersAtSelectedModule(){
		determineSelectedModuleWithClasses();

		// Identify the layers within the selected module
		identifyLayers(selectedModuleWithClasses);
		
	}
	
	public void identifyLayers(ArrayList<SoftwareUnitDTO> units) {
		// 1) Assign all internalRootPackages to bottom layer
		int layerId = 1;
		ArrayList<SoftwareUnitDTO> assignedUnits = new ArrayList<SoftwareUnitDTO>();
		assignedUnits.addAll(units);
		layers.put(layerId, assignedUnits);

		// 2) Identify the bottom layer. Look for packages with dependencies to
		// external systems only.
		identifyTopLayerBasedOnUnitsInBottomLayer(layerId);

		// 3) Look iteratively for packages on top of the bottom layer, et
		// cetera.
		while (layers.lastKey() > layerId) {
			layerId++;
			identifyTopLayerBasedOnUnitsInBottomLayer(layerId);
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
	
	public void identifyTopLayerBasedOnUnitsInBottomLayer(int bottomLayerId) {
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
					int nrOfDependenciesFromsoftwareUnitToOther = queryService
							.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName,
									otherSoftwareUnit.uniqueName).length;
					int nrOfDependenciesFromOtherTosoftwareUnit = queryService
							.getDependenciesFromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName,
									softwareUnit.uniqueName).length;
					if (nrOfDependenciesFromsoftwareUnitToOther > ((nrOfDependenciesFromOtherTosoftwareUnit / 100)
							* layerThreshold)) {
						rootPackageDoesNotUseOtherPackage = false;
					}
				}
			}
			if (rootPackageDoesNotUseOtherPackage) { // Leave unit in the lower
														// layer
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
