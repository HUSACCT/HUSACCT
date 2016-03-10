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
	public ArrayList<SoftwareUnitDTO> getClasses() {
		// TODO Auto-generated method stub
		return null;
	}



}
