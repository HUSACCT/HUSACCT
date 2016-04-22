package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.HashMap;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.define.IDefineSarService;

public abstract class IAlgorithm {
	private ArrayList<ModuleDTO> reverseReconstructionList = new ArrayList<ModuleDTO>();
	private HashMap<String, String> reverseEditModuleTypeList = new HashMap<String, String>(); // logicatPath (of the module), moduleTypeToBeReversedTo

	
	public abstract void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService, String xLibrariesRootPackage)throws Exception;
	
	
	public void reverse(){
		IDefineSarService defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
		for(ModuleDTO module : reverseReconstructionList){
			defineSarService.removeModule(module.logicalPath);
		}
		for (String logicalPath : reverseEditModuleTypeList.keySet()) {
			defineSarService.editModule(logicalPath, reverseEditModuleTypeList.get(logicalPath), null, 0, null);
		}
		clearReverseReconstructionLists();
	}
	
	
	
	protected void addToReverseEditModuleList(String logicalPath, String moduleTypeToBeReversedTo) {
		reverseEditModuleTypeList.put(logicalPath, moduleTypeToBeReversedTo);
	}

	protected void addToReverseReconstructionList(ModuleDTO newModule){
		reverseReconstructionList.add(newModule);
	}

	protected void clearReverseReconstructionLists() {
		reverseReconstructionList.clear();
		reverseEditModuleTypeList.clear();
	}
}
