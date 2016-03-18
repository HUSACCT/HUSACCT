package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;
import husacct.define.domain.module.ModuleStrategy;

public class Algorithm_Goldstein_SelectedModule extends AlgorithmGeneral{
	private ModuleDTO selectedModule;
	private int layerThreshold;
	private IModelQueryService queryService;
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
	
	
	
	@Override
	public void define(ModuleDTO Module, int th, IModelQueryService qService, String library, String dependencyType) {
		selectedModule = Module;
		layerThreshold = th;
		queryService = qService;
		IDefineSarService defineSarService = husacct.ServiceProvider.getInstance().getDefineService().getSarService();
		IDefineService defineService = husacct.ServiceProvider.getInstance().getDefineService();
		identifyLayers(getClasses(library), dependencyType);
		identifyLayers(getClasses(library), dependencyType);
		for (int level : layers.keySet()) {
			ArrayList<ModuleDTO> modulesToBeMoved = new ArrayList<ModuleDTO>();
			for(SoftwareUnitDTO softwareUnitDTO : layers.get(level)){
				modulesToBeMoved.add(defineService.getModule_BasedOnSoftwareUnitName(softwareUnitDTO.uniqueName));
			}
			
			ModuleStrategy addedModuleStrategy = defineSarService.addModule("Layer" + level, selectedModule.logicalPath, "Layer", level, layers.get(level));	
			ModuleDTO layerModuleDTO = defineSarService.parseModuleStrategy(addedModuleStrategy);
			
			for(ModuleDTO moduleDTOInLayer : modulesToBeMoved){
				ArrayList<SoftwareUnitDTO> assignedSoftwareUnits = new ArrayList<SoftwareUnitDTO>();
				
				for(String logicalPath : defineService.getAssignedSoftwareUnitsOfModule(moduleDTOInLayer.logicalPath)){
					assignedSoftwareUnits.add(queryService.getSoftwareUnitByUniqueName(logicalPath));
				}
				defineSarService.addModule(moduleDTOInLayer.name, layerModuleDTO.logicalPath, moduleDTOInLayer.type, level, assignedSoftwareUnits);
				defineSarService.removeModule(moduleDTOInLayer.logicalPath);
			}
			
		}
	}

	
	
	@Override
	public ArrayList<SoftwareUnitDTO> getClasses(String library) {
		ArrayList<SoftwareUnitDTO> selectedSubmoduleWithClasses = new ArrayList<SoftwareUnitDTO>();
		IDefineService defineService = husacct.ServiceProvider.getInstance().getDefineService();

		
		ModuleDTO[] subModuleDTOs = selectedModule.subModules;
		for(ModuleDTO subModule : subModuleDTOs){
			for(String logicalSoftwarePath : defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath)){
				selectedSubmoduleWithClasses.add(queryService.getSoftwareUnitByUniqueName(logicalSoftwarePath));
			}
		}
		System.out.println("----------");
		System.out.println(selectedSubmoduleWithClasses);
		System.out.println("----------");
		
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
	public TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(
			String library, TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}	
