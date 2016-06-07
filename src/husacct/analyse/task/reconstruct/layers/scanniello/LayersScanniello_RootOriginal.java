package husacct.analyse.task.reconstruct.layers.scanniello;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.AlgorithmParameter;
import husacct.analyse.task.reconstruct.parameters.NumberFieldPanel;
import husacct.analyse.task.reconstruct.parameters.ParameterPanel;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;

public class LayersScanniello_RootOriginal extends AlgorithmScanniello{
	
	public LayersScanniello_RootOriginal (IModelQueryService queryService) {
		super(queryService);
	}
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) {
		this.threshold = dto.getThreshold();
		
		List<SoftwareUnitDTO> classes = queryService.getAllClasses();
		ArrayList<SoftwareUnitDTO> classesArray = new ArrayList<SoftwareUnitDTO>(classes);
		boolean firstIdentification = true;
		HashMap<Integer, ArrayList<SoftwareUnitDTO>> firstIdentifiedLayers = identifyLayersOriginal(classesArray, firstIdentification);
		
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
			firstIdentification = false;
			HashMap<Integer, ArrayList<SoftwareUnitDTO>> newIdentifiedLayers = identifyLayersOriginal(middleLayer, firstIdentification);
			
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

	@Override
	public ReconstructArchitectureDTO getAlgorithmThresholdSettings() {
		ReconstructArchitectureDTO reconstructArchitecture = new ReconstructArchitectureDTO();
		reconstructArchitecture.approachConstant = AnalyseReconstructConstants.Algorithm.Layers_Scanniello_Original;
		reconstructArchitecture.parameterPanels = createParameterPanels();
		reconstructArchitecture.threshold = 10;
		return reconstructArchitecture;
	}
	
	private ArrayList<ParameterPanel> createParameterPanels(){
		ArrayList<ParameterPanel> parameterPanels = new ArrayList<>();
		
		ParameterPanel numberField = new NumberFieldPanel("Threshold", AlgorithmParameter.Threshold, 10);
		numberField.defaultValue = 10;
		numberField.minimumValue = 0;
		numberField.maximumValue = 100;
		parameterPanels.add(numberField);
		
		return parameterPanels;
	}

	
}
