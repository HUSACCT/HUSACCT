package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;
import husacct.define.domain.module.ModuleStrategy;

public class AlgorithmSelectedModuleScanniello extends AlgorithmGeneral{
	
	private IModelQueryService queryService;
	private IDefineService defineService;
	private int threshold;
	private int topLayerKey = 1;
	private int middleLayerKey = 2;
	private int bottomLayerKey = 3;
	private int discLayerKey = 4;


	@Override
	public void define(ModuleDTO selectedModule, int threshold, IModelQueryService queryService) {
		this.queryService = queryService;
		this.threshold = threshold;
		this.defineService = ServiceProvider.getInstance().getDefineService();
		
		
		HashMap<Integer, ArrayList<SoftwareUnitDTO>> firstLayersIdentification = IdentifyLayers(getSoftwareUnitDTOs(selectedModule));
		ArrayList<ArrayList<SoftwareUnitDTO>> topLayers = new ArrayList<>();
		ArrayList<ArrayList<SoftwareUnitDTO>> bottomLayers = new ArrayList<>();
		
		topLayers.add(firstLayersIdentification.get(topLayerKey));
		bottomLayers.add(firstLayersIdentification.get(bottomLayerKey));
		ArrayList<SoftwareUnitDTO> middleLayer = firstLayersIdentification.get(middleLayerKey);
		
		boolean topOrBottomAreNotEmpty = firstLayersIdentification.get(topLayerKey).size() > 0 || firstLayersIdentification.get(bottomLayerKey).size() > 0;
		while (topOrBottomAreNotEmpty){
			HashMap<Integer, ArrayList<SoftwareUnitDTO>> newIdentifiedLayers = IdentifyLayers(middleLayer);
			
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
		this.buildStructure(structuredLayers, selectedModule);
	}

	@Override
	public ArrayList<SoftwareUnitDTO> getClasses(String library) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(String library, TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	private ArrayList<SoftwareUnitDTO> getSoftwareUnitDTOs(ModuleDTO selectedModule){
		ArrayList<SoftwareUnitDTO> softwareUnitsSelectedModule = new ArrayList<SoftwareUnitDTO>();
		
		for (ModuleDTO subModule : selectedModule.subModules){
			HashSet<String> softwareUnitNames = defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath);
			for(String sofwareUnitName :  softwareUnitNames){
				SoftwareUnitDTO subSoftwareUnit = queryService.getSoftwareUnitByUniqueName(sofwareUnitName);
				softwareUnitsSelectedModule.add(subSoftwareUnit);
			}
		}
		return softwareUnitsSelectedModule;
	}
	
	
	
	private HashMap<Integer, ArrayList<SoftwareUnitDTO>> IdentifyLayers(ArrayList<SoftwareUnitDTO> sofwareUnitDTOs){
		 ArrayList<SoftwareUnitDTO> topLayer = new ArrayList<SoftwareUnitDTO>();
		 ArrayList<SoftwareUnitDTO> middleLayer = new ArrayList<SoftwareUnitDTO>();
		 ArrayList<SoftwareUnitDTO> bottomLayer = new ArrayList<SoftwareUnitDTO>();
		 ArrayList<SoftwareUnitDTO> discLayer = new ArrayList<SoftwareUnitDTO>();
		 
		for(SoftwareUnitDTO softwareUnitDTO : sofwareUnitDTOs){
			//check dependencies OF the softwareUnit
			ArrayList<DependencyDTO> dependecyDTOsFromSoftwareUnit = getDependenciesFromSoftwareUnit(softwareUnitDTO, sofwareUnitDTOs);
			
			//check dependencies TO the softwareUnit
			ArrayList<DependencyDTO> dependecyDTOsToSoftwareUnit = getDependenciesToSoftwareUnit(sofwareUnitDTOs, softwareUnitDTO);
			
			//- If other softwareUnits have no dependencies with this one, the softwareUnit must be in the topLayer
			//- If the SoftwareUnits has no dependencies with other softwareUnits, the softwareUnit must be in de bottomLayer.
			//- If the SoftwareUnits has dependencies And others with it, then is must be in the middelLayer
			//- then do the same routine to the middelLayer to find more layers.
			
			int totalNumberOfDependencies = dependecyDTOsToSoftwareUnit.size() + dependecyDTOsFromSoftwareUnit.size();
			int thresHoldDependencies = totalNumberOfDependencies * (threshold/100);
			int NumberOfDependeciesToTheSoftwareUnit = dependecyDTOsToSoftwareUnit.size() - thresHoldDependencies;
			int NumberOfDependenciesFromTheSoftwareUnit = dependecyDTOsFromSoftwareUnit.size() - thresHoldDependencies;
			
			
			//the softwareUnit has (after the threshold) only dependencies with another class
			if ( NumberOfDependeciesToTheSoftwareUnit <= 0 && NumberOfDependenciesFromTheSoftwareUnit > 0){
				topLayer.add(softwareUnitDTO);
			}
			//The softwareUnit has (after the threshold) no dependencies with other classes, Only other classes with the softwareUnit.
			else if (NumberOfDependenciesFromTheSoftwareUnit <= 0 && NumberOfDependeciesToTheSoftwareUnit > 0){
				bottomLayer.add(softwareUnitDTO);
			}
			//The softwareUnit has dependencies with classes and vice versa
			else if (NumberOfDependenciesFromTheSoftwareUnit > 0 && NumberOfDependeciesToTheSoftwareUnit > 0){
				middleLayer.add(softwareUnitDTO);
			}
			//The softwareUnit has NO dependencies both ways (stand-alone)
			else{
				discLayer.add(softwareUnitDTO);
			}
		}
		
		 HashMap<Integer, ArrayList<SoftwareUnitDTO>> layers = new HashMap<Integer, ArrayList<SoftwareUnitDTO>>();
		 layers.put(topLayerKey, topLayer);
		 layers.put(middleLayerKey, middleLayer);
		 layers.put(bottomLayerKey, bottomLayer);
		 layers.put(discLayerKey, discLayer);
		return layers;
	}
	
	private ArrayList<DependencyDTO> getDependenciesFromSoftwareUnit(SoftwareUnitDTO softwareUnitDTO, ArrayList<SoftwareUnitDTO> sofwareUnitDTOs){
		ArrayList<DependencyDTO> dependecyDTOs = new ArrayList<DependencyDTO>();
		
		for (SoftwareUnitDTO possibleDependency : sofwareUnitDTOs){
			DependencyDTO[] dependencies =  queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnitDTO.uniqueName, possibleDependency.uniqueName);
			dependecyDTOs.addAll(Arrays.asList(dependencies));
		}
		return dependecyDTOs;
	}
	private ArrayList<DependencyDTO> getDependenciesToSoftwareUnit(ArrayList<SoftwareUnitDTO> sofwareUnitDTOs, SoftwareUnitDTO softwareUnitDTO){
		ArrayList<DependencyDTO> dependecyDTOs = new ArrayList<DependencyDTO>();
		
		for (SoftwareUnitDTO possibleDependency : sofwareUnitDTOs){
			DependencyDTO[] dependencies =  queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(possibleDependency.uniqueName, softwareUnitDTO.uniqueName);
			dependecyDTOs.addAll(Arrays.asList(dependencies));
		}
		return dependecyDTOs;
	}
	
	
	private HashMap<Integer, ArrayList<SoftwareUnitDTO>> RestructureLayers(ArrayList<ArrayList<SoftwareUnitDTO>> topLayers, ArrayList<ArrayList<SoftwareUnitDTO>> bottomLayers, ArrayList<SoftwareUnitDTO> middleLayer){
		HashMap<Integer, ArrayList<SoftwareUnitDTO>> structuredLayers = new HashMap<Integer, ArrayList<SoftwareUnitDTO>>();
		
		int LayerKeyCount = 1;
		for(ArrayList<SoftwareUnitDTO> topLayer : topLayers){
			structuredLayers.put(LayerKeyCount, topLayer);
			LayerKeyCount++;
		}
		structuredLayers.put(LayerKeyCount, middleLayer);
		LayerKeyCount++;
		for (int i = bottomLayers.size()-1; i>=0; i-- ){
			structuredLayers.put(LayerKeyCount, bottomLayers.get(i));
			LayerKeyCount++;
		}
		
		return structuredLayers;
	}
	
	private void buildStructure(HashMap<Integer, ArrayList<SoftwareUnitDTO>> structuredLayers, ModuleDTO selectedModule){
		IDefineSarService defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
		for (int hierarchicalLevel : structuredLayers.keySet()) {
			
			ArrayList<ModuleDTO> subModuleDTOs = new ArrayList<>();
			for (SoftwareUnitDTO subSoftwareUnitDTO : structuredLayers.get(hierarchicalLevel)){
				ModuleDTO subModule = defineService.getModule_BasedOnSoftwareUnitName(subSoftwareUnitDTO.uniqueName);
				subModuleDTOs.add(subModule);
			}
			
			ModuleStrategy layerModuleStategie = defineSarService.addModule("Layer" + hierarchicalLevel, selectedModule.logicalPath, "Layer", hierarchicalLevel, structuredLayers.get(hierarchicalLevel));
			ModuleDTO layerModuleDTO = defineSarService.parseModuleStrategy(layerModuleStategie);
			
			for (ModuleDTO subModule : subModuleDTOs){
				ArrayList<SoftwareUnitDTO> subSoftwareUnitDTOs = new ArrayList<>();
				for (String softwareUnitUniqueName : defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath)){
					subSoftwareUnitDTOs.add(queryService.getSoftwareUnitByUniqueName(softwareUnitUniqueName));
				}
				defineSarService.addModule(subModule.name, layerModuleDTO.logicalPath, subModule.type, hierarchicalLevel, subSoftwareUnitDTOs);
				defineSarService.removeModule(subModule.logicalPath);
			}
			
		}
	}
	
}





