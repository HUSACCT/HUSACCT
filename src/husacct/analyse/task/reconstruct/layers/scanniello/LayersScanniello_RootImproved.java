package husacct.analyse.task.reconstruct.layers.scanniello;

import java.util.ArrayList;
import java.util.HashMap;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;

public class LayersScanniello_RootImproved extends AlgorithmScanniello{
	private static final String xLibrariesRootPackage = "xLibraries";
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService, String xLibrariesRootPackage) {
		this.queryService = queryService;
		this.threshold = dto.getThreshold();
		ServiceProvider.getInstance().getDefineService();
		
		ArrayList<SoftwareUnitDTO> softwareUnitDTOs = determineInternalRootPackagesWithClasses();
		boolean firstIdentification = true;
		HashMap<Integer, ArrayList<SoftwareUnitDTO>> firstIdentifiedLayers = IdentifyLayersImproved(softwareUnitDTOs, dto, firstIdentification);
		
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
		this.buildStructure(structuredLayers, discLayer);
	}

	
	private ArrayList<SoftwareUnitDTO> determineInternalRootPackagesWithClasses() {
		ArrayList<SoftwareUnitDTO> internalRootPackagesWithClasses = new ArrayList<SoftwareUnitDTO>();
		SoftwareUnitDTO[] allRootUnits = queryService.getSoftwareUnitsInRoot();
		for (SoftwareUnitDTO rootModule : allRootUnits) {
			if (!rootModule.uniqueName.equals(xLibrariesRootPackage)) {
				for (String internalPackage : queryService.getRootPackagesWithClass(rootModule.uniqueName)) {
					internalRootPackagesWithClasses.add(queryService.getSoftwareUnitByUniqueName(internalPackage));
				}
			}
		}
		if (internalRootPackagesWithClasses.size() == 1) {
			String newRoot = internalRootPackagesWithClasses.get(0).uniqueName;
			internalRootPackagesWithClasses = new ArrayList<SoftwareUnitDTO>();
			for (SoftwareUnitDTO child : queryService.getChildUnitsOfSoftwareUnit(newRoot)) {
				// if (child.type.equalsIgnoreCase("class")) {
				internalRootPackagesWithClasses.add(child);
				// }
			}
		}
		return internalRootPackagesWithClasses;
	}
}
