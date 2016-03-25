package husacct.analyse.task.reconstruct;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineSarService;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class Algorithm_Goldstein_MultiLayer extends AlgorithmGeneral{

	private ModuleDTO selectedModule;
	private int layerThreshold;
	private IModelQueryService queryService;
	private ArrayList<SoftwareUnitDTO> internalRootPackagesWithClasses;
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> identifiedLayers;
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	IDefineSarService defineSarService = husacct.ServiceProvider.getInstance().getDefineService().getSarService();
	private String xLibrariesRootPackage = "xLibraries";
	
	@Override
	public void define(ModuleDTO Module, int th, IModelQueryService qService, String library, String dependencyType) {
		selectedModule = Module;
		layerThreshold = th;
		queryService = qService;
		
		identifyMultipleLayers(dependencyType);
	}

	@Override
	public TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(String library, TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers) {
		getClasses(library);
		identifiedLayers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
		identifiedLayers = layers;	
		
		return identifiedLayers;
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
	}
	
	
	private void identifyLayersAtRootLevel(String dependencyType) {
		determineInternalRootPackagesWithClasses();
		identifyLayers(internalRootPackagesWithClasses, dependencyType);
		for (Integer herarchicalLevel : layers.keySet()) {
			defineSarService.addModule("Layer" + herarchicalLevel, "**", "Layer", herarchicalLevel, layers.get(herarchicalLevel));
			String logicalPathReversable = "Layer" + herarchicalLevel;
			addToReverseReconstructionList(logicalPathReversable); //add to cache for reverse
		}
	}
	
	private void identifyMultipleLayers(String dependencyType) {
		identifyLayersAtRootLevel(dependencyType);
		identifiedLayers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
		identifiedLayers = layers;	
		defineSarService.getModule_SelectedInGUI();
		layers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
		
		for(int i : identifiedLayers.keySet()){
			identifyLayers(identifiedLayers.get(i), dependencyType);
			logger.info(layers);	
			if(layers.keySet().size() > 1){
				for (Integer herarchicalLevel : layers.keySet()) {
					defineSarService.addModule("Layerrr" + herarchicalLevel, "Layer" + i, "Layer", herarchicalLevel, layers.get(herarchicalLevel));	
				
				}
			}	
		}
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
	public ArrayList<SoftwareUnitDTO> getClasses(String library) {
		// TODO Auto-generated method stub
		return null;
	}


}
