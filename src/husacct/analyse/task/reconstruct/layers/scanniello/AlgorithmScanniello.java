package husacct.analyse.task.reconstruct.layers.scanniello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.IAlgorithm;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;

public abstract class AlgorithmScanniello extends IAlgorithm{
	protected static final int topLayerKey = 1;
	protected static final int middleLayerKey = 2;
	protected static final int bottomLayerKey = 3;
	protected static final int discLayerKey = 4;
	
	protected IModelQueryService queryService;
	protected IDefineService defineService;
	protected int threshold;

	
	protected ArrayList<SoftwareUnitDTO> getSoftwareUnitDTOs(ModuleDTO selectedModule){
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
	
	
	
	protected HashMap<Integer, ArrayList<SoftwareUnitDTO>> IdentifyLayersImproved(ArrayList<SoftwareUnitDTO> sofwareUnitDTOs){
		 ArrayList<SoftwareUnitDTO> topLayer = new ArrayList<SoftwareUnitDTO>();
		 ArrayList<SoftwareUnitDTO> middleLayer = new ArrayList<SoftwareUnitDTO>();
		 ArrayList<SoftwareUnitDTO> bottomLayer = new ArrayList<SoftwareUnitDTO>();
		 ArrayList<SoftwareUnitDTO> discLayer = new ArrayList<SoftwareUnitDTO>();
		 
		for(SoftwareUnitDTO softwareUnitDTO : sofwareUnitDTOs){
			//check dependencies OF the softwareUnit
			ArrayList<DependencyDTO> dependecyDTOsFromSoftwareUnit = getDependenciesFromSoftwareUnit(softwareUnitDTO, sofwareUnitDTOs);
			
			//check dependencies TO the softwareUnit
			ArrayList<DependencyDTO> dependecyDTOsTowardsSoftwareUnit = getDependenciesTowardsSoftwareUnit(sofwareUnitDTOs, softwareUnitDTO);
			
			//- If other softwareUnits have no dependencies with this one, the softwareUnit must be in the topLayer
			//- If the SoftwareUnits has no dependencies with other softwareUnits, the softwareUnit must be in de bottomLayer.
			//- If the SoftwareUnits has dependencies And others with it, then is must be in the middelLayer
			//- then do the same routine to the middelLayer to find more layers.
			
			int totalNumberOfDependencies = dependecyDTOsTowardsSoftwareUnit.size() + dependecyDTOsFromSoftwareUnit.size();
			int thresHoldDependencies = (int) (totalNumberOfDependencies * (threshold*0.01));
			int NumberOfDependeciesToTheSoftwareUnit = dependecyDTOsTowardsSoftwareUnit.size() - thresHoldDependencies;
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
	private ArrayList<DependencyDTO> getDependenciesTowardsSoftwareUnit(ArrayList<SoftwareUnitDTO> sofwareUnitDTOs, SoftwareUnitDTO softwareUnitDTO){
		ArrayList<DependencyDTO> dependecyDTOs = new ArrayList<DependencyDTO>();
		
		for (SoftwareUnitDTO possibleDependency : sofwareUnitDTOs){
			DependencyDTO[] dependencies =  queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(possibleDependency.uniqueName, softwareUnitDTO.uniqueName);
			dependecyDTOs.addAll(Arrays.asList(dependencies));
		}
		return dependecyDTOs;
	}
	
	protected HashMap<Integer, ArrayList<SoftwareUnitDTO>> RestructureLayers(ArrayList<ArrayList<SoftwareUnitDTO>> topLayers, ArrayList<ArrayList<SoftwareUnitDTO>> bottomLayers, ArrayList<SoftwareUnitDTO> middleLayer){
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
	
		
	protected void buildStructure(HashMap<Integer, ArrayList<SoftwareUnitDTO>> structuredLayers, ArrayList<SoftwareUnitDTO> discLayer){
		IDefineSarService defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
		int layerCounter = 0;
		for (int hierarchicalLevel : structuredLayers.keySet()) {
			if (!structuredLayers.get(hierarchicalLevel).isEmpty()){
				layerCounter++;
				ModuleDTO newModule = defineSarService.addModule("Layer" + layerCounter, "**", "Layer", hierarchicalLevel, structuredLayers.get(hierarchicalLevel));
				addToReverseReconstructionList(newModule); //add to cache for reverse
			}
		}
		if (!discLayer.isEmpty()){
			ModuleDTO newModule = defineSarService.addModule("discLayer", "**", "Layer", 1, discLayer);
			addToReverseReconstructionList(newModule); //add to cache for reverse
		}
	}
	protected void buildStructure (HashMap<Integer, ArrayList<SoftwareUnitDTO>> structuredLayers, ArrayList<SoftwareUnitDTO> discLayer, ModuleDTO selectedModule){
		IDefineSarService defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
		int layerCount = 0;
		for (int hierarchicalLevel : structuredLayers.keySet()) {
			if (!structuredLayers.get(hierarchicalLevel).isEmpty()){
				layerCount++;
				ModuleDTO newModule = defineSarService.addModule("Layer" + layerCount, selectedModule.logicalPath, "Layer", hierarchicalLevel, structuredLayers.get(hierarchicalLevel));
				addToReverseReconstructionList(newModule); //add to cache for reverse
			}
		}
		if (!discLayer.isEmpty()){
			ModuleDTO newModule = defineSarService.addModule("discLayer", selectedModule.logicalPath, "Layer", layerCount, discLayer);
			addToReverseReconstructionList(newModule); //add to cache for reverse
		}
	}
}
