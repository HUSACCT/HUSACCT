package husacct.analyse.task.reconstruct.layers.scanniello;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.define.IDefineSarService;

public class LayersScanniello_RootOriginal extends AlgorithmScanniello{
	private static final int topLayerKey = 1;
	private static final int middleLayerKey = 2;
	private static final int bottomLayerKey = 3;
	private static final int discLayerKey = 4;

	private IModelQueryService queryService;
	private int threshold;
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService, String xLibrariesRootPackage) {
		this.queryService = queryService;
		this.threshold = dto.getThreshold();
		ServiceProvider.getInstance().getDefineService();
		
		List<SoftwareUnitDTO> classes = queryService.getAllClasses();
		ArrayList<SoftwareUnitDTO> classesArray = new ArrayList<SoftwareUnitDTO>(classes);
		HashMap<Integer, ArrayList<SoftwareUnitDTO>> firstIdentifiedLayers = IdentifyLayers(classesArray);
		
		ArrayList<ArrayList<SoftwareUnitDTO>> topLayers = new ArrayList<>();
		ArrayList<ArrayList<SoftwareUnitDTO>> bottomLayers = new ArrayList<>();
		ArrayList<SoftwareUnitDTO> middleLayer = new ArrayList<>();
		ArrayList<SoftwareUnitDTO> discLayer = new ArrayList<>();
		
		topLayers.add(firstIdentifiedLayers.get(topLayerKey));
		bottomLayers.add(firstIdentifiedLayers.get(bottomLayerKey));
		middleLayer = firstIdentifiedLayers.get(middleLayerKey);
		discLayer = firstIdentifiedLayers.get(discLayerKey);
		
		boolean topOrBottomAreNotEmpty = firstIdentifiedLayers.get(topLayerKey).size() > 0 || firstIdentifiedLayers.get(bottomLayerKey).size() > 0;
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
		this.buildStructure(structuredLayers, discLayer);
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
			int thresHoldDependencies = (int) (totalNumberOfDependencies * (threshold*0.01));
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
			DependencyDTO[] dependencies =  queryService.getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(softwareUnitDTO.uniqueName, possibleDependency.uniqueName);
			dependecyDTOs.addAll(Arrays.asList(dependencies));
		}
		return dependecyDTOs;
	}
	private ArrayList<DependencyDTO> getDependenciesToSoftwareUnit(ArrayList<SoftwareUnitDTO> sofwareUnitDTOs, SoftwareUnitDTO softwareUnitDTO){
		ArrayList<DependencyDTO> dependecyDTOs = new ArrayList<DependencyDTO>();
		
		for (SoftwareUnitDTO possibleDependency : sofwareUnitDTOs){
			DependencyDTO[] dependencies =  queryService.getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(possibleDependency.uniqueName, softwareUnitDTO.uniqueName);
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
	
	private void buildStructure(HashMap<Integer, ArrayList<SoftwareUnitDTO>> structuredLayers, ArrayList<SoftwareUnitDTO> discLayer){
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
	
	
	
}
