package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.TreeMap;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;

public abstract class AlgorithmGeneral {
	
	//DE METHODES VOOR AlgorithmSelectedModule
	public abstract void define(ModuleDTO Module, int th, IModelQueryService qService, String library, String dependencyType);
	public abstract ArrayList<SoftwareUnitDTO> getClasses(String library);
	public abstract TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(String library, TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers);
		
	

}
