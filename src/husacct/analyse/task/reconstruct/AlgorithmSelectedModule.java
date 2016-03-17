package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.ServiceProvider;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineService;

public class AlgorithmSelectedModule extends AlgorithmGeneral{
	private ModuleDTO selectedModule;
	private int layerThreshold;
	private IModelQueryService queryService;
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers;
	
	
	
	@Override
	public void define(ModuleDTO Module, int th, IModelQueryService qService) {
		selectedModule = Module;
		layerThreshold = th;
		queryService = qService;
	}

	
	
	@Override

	public ArrayList<SoftwareUnitDTO> getClasses(String library) {
		ArrayList<SoftwareUnitDTO> selectedSubmoduleWithClasses = new ArrayList<SoftwareUnitDTO>();
		IDefineService defineService = husacct.ServiceProvider.getInstance().getDefineService();

		
		ModuleDTO[] subModuleDTOs = selectedModule.subModules;
		for(ModuleDTO subModule : subModuleDTOs){
			for(String logicalSoftwarePath : defineService.getAssignedSoftwareUnitsOfModule(subModule.logicalPath)){
				selectedSubmoduleWithClasses.add(queryService.getSoftwareUnitByUniqueName(logicalSoftwarePath));
			}
		}
		System.out.println("----------");
		System.out.println(selectedSubmoduleWithClasses);
		System.out.println("----------");
		
		return selectedSubmoduleWithClasses;
	}



	@Override
	public TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(
			String library, TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
}
