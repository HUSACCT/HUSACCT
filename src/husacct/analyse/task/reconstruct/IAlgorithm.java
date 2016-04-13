package husacct.analyse.task.reconstruct;

import java.util.ArrayList;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.define.IDefineSarService;

public abstract class IAlgorithm {
	private ArrayList<ModuleDTO> reverseReconstructionList = new ArrayList<ModuleDTO>();

	
	public abstract void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService, String xLibrariesRootPackage)throws Exception;
	
	
	public void reverse(){
		IDefineSarService defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
		for(ModuleDTO module : reverseReconstructionList){
			defineSarService.removeModule(module.logicalPath);
		}
		clearReverseReconstructionList();
	}
	
	
	
	public void addToReverseReconstructionList(ModuleDTO newModule){
		reverseReconstructionList.add(newModule);
	}

	public void clearReverseReconstructionList() {
		reverseReconstructionList.clear();
	}
}
