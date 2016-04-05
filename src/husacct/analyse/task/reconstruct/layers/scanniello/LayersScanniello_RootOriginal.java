package husacct.analyse.task.reconstruct.layers.scanniello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;

public class LayersScanniello_RootOriginal extends AlgorithmScanniello{
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService, String xLibrariesRootPackage) {
		this.queryService = queryService;
		this.threshold = dto.getThreshold();
		ServiceProvider.getInstance().getDefineService();
		
		List<SoftwareUnitDTO> classes = queryService.getAllClasses();
		ArrayList<SoftwareUnitDTO> classesArray = new ArrayList<SoftwareUnitDTO>(classes);
		HashMap<Integer, ArrayList<SoftwareUnitDTO>> firstIdentifiedLayers = identifyLayersOriginal(classesArray);
		
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
			HashMap<Integer, ArrayList<SoftwareUnitDTO>> newIdentifiedLayers = identifyLayersOriginal(middleLayer);
			
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

	
}
