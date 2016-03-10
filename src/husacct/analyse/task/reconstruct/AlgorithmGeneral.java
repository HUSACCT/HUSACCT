package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.TreeMap;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;

public abstract class AlgorithmGeneral {
	
	public abstract void identifyLayers(ArrayList<SoftwareUnitDTO> units);
	public abstract void identifyTopLayerBasedOnUnitsInBottomLayer(int bottomLayerId);
	//DE METHODES VOOR AlgorithmSelectedModule
	public abstract void define(ModuleDTO selectedModule, int threshold, IModelQueryService queryService);
	public abstract ArrayList<SoftwareUnitDTO> determineSelectedModuleWithClasses();
	public abstract void identifyLayersAtSelectedModule();
	

}
