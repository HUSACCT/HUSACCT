package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.TreeMap;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;

public class AlgorithmTwo extends AlgorithmGeneral{

	@Override
	public void define(ModuleDTO selectedModule, int threshold, IModelQueryService queryService) {
		System.out.println("second algorithm");

	}

	@Override
	public void identifyLayers(ArrayList<SoftwareUnitDTO> units) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void identifyTopLayerBasedOnUnitsInBottomLayer(int bottomLayerId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public ArrayList<SoftwareUnitDTO> determineSelectedModuleWithClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void identifyLayersAtSelectedModule() {
		// TODO Auto-generated method stub
		
	}


}
