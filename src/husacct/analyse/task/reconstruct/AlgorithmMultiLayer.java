package husacct.analyse.task.reconstruct;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;

import java.util.ArrayList;
import java.util.TreeMap;

public class AlgorithmMultiLayer extends AlgorithmGeneral{

	private ModuleDTO selectedModule;
	private int layerThreshold;
	private IModelQueryService queryService;
	private ArrayList<SoftwareUnitDTO> internalRootPackagesWithClasses;
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> identifiedLayers;
	
	@Override
	public void define(ModuleDTO Module, int th,
			IModelQueryService qService) {
		selectedModule = Module;
		layerThreshold = th;
		queryService = qService;
		
	}

	@Override
	public TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(String library, TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers) {
		AlgorithmGeneral algorithm = new AlgorithmRoot();
		algorithm.define(selectedModule, layerThreshold, queryService);
		algorithm.getClasses(library);
		identifiedLayers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
		identifiedLayers = layers;	
		
		return identifiedLayers;
	}


	@Override
	public ArrayList<SoftwareUnitDTO> getClasses(String library) {
		// TODO Auto-generated method stub
		return null;
	}

}
