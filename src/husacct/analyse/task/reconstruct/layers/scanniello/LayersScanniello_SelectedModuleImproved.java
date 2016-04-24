package husacct.analyse.task.reconstruct.layers.scanniello;

import java.util.ArrayList;
import java.util.HashMap;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;


public class LayersScanniello_SelectedModuleImproved extends AlgorithmScanniello{		
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService)throws Exception {
		this.queryService = queryService;
		this.threshold = dto.getThreshold();
		this.defineService = ServiceProvider.getInstance().getDefineService();
		
		ArrayList<SoftwareUnitDTO> softwareUnitDTOsOfSelectedModule = getSoftwareUnitDTOs(dto.getSelectedModule());
		boolean firstIdentification = true;
		HashMap<Integer, ArrayList<SoftwareUnitDTO>> firstLayersIdentification = IdentifyLayersImproved(softwareUnitDTOsOfSelectedModule, dto, firstIdentification);
		ArrayList<ArrayList<SoftwareUnitDTO>> topLayers = new ArrayList<>();
		ArrayList<ArrayList<SoftwareUnitDTO>> bottomLayers = new ArrayList<>();
		ArrayList<SoftwareUnitDTO> middleLayer = new ArrayList<>();
		ArrayList<SoftwareUnitDTO> discLayer = new ArrayList<>();
		
		topLayers.add(firstLayersIdentification.get(topLayerKey));
		bottomLayers.add(firstLayersIdentification.get(bottomLayerKey));
		middleLayer = firstLayersIdentification.get(middleLayerKey);
		discLayer = firstLayersIdentification.get(discLayerKey);
		
		boolean topOrBottomAreNotEmpty = firstLayersIdentification.get(topLayerKey).size() > 0 || firstLayersIdentification.get(bottomLayerKey).size() > 0;
		while (topOrBottomAreNotEmpty){
			firstIdentification = false;
			HashMap<Integer, ArrayList<SoftwareUnitDTO>> newIdentifiedLayers = IdentifyLayersImproved(middleLayer, dto, firstIdentification);
			
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
		buildStructure(structuredLayers, discLayer, dto.getSelectedModule());
	}

}





