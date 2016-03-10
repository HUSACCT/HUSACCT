package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineService;

public class AlgorithmSelectedModule extends AlgorithmGeneral{
	private ModuleDTO selectedModule;
	private int layerThreshold;
	private IModelQueryService queryService;
	private ArrayList<SoftwareUnitDTO> selectedModuleWithClasses;
	
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers;
	
	
	
	@Override
	public void define(ModuleDTO Module, int th, IModelQueryService qService) {
		selectedModule = Module;
		layerThreshold = th;
		queryService = qService;
	}

	
	
	@Override
	public ArrayList<SoftwareUnitDTO> getClasses() {
		ArrayList<SoftwareUnitDTO> selectedModuleWithClasses = new ArrayList<SoftwareUnitDTO>();
		IDefineService defineService = ServiceProvider.getInstance().getDefineService();
		
		ModuleDTO[] selectedSubModules = selectedModule.subModules;
		for (ModuleDTO subModule : selectedSubModules) {
			for(String localpath : defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath))
			selectedModuleWithClasses
					.add(queryService.getSoftwareUnitByUniqueName(localpath));
		}
		System.out.println("----------");
		System.out.println(selectedModuleWithClasses);
		System.out.println("----------");
		
		return selectedModuleWithClasses;
	}
	
	
}
