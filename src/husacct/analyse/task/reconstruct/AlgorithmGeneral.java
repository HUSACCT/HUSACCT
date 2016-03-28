package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.TreeMap;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.define.IDefineSarService;

public abstract class AlgorithmGeneral {
	
	//private IDefineSarService defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
	private ArrayList<ModuleDTO> reverseReconstructionList = new ArrayList<ModuleDTO>();

	//DE METHODES VOOR AlgorithmSelectedModule
	public abstract void execute(ModuleDTO Module, int th, IModelQueryService qService, String library, String dependencyType);
	public abstract ArrayList<SoftwareUnitDTO> getClasses(String library);
	public abstract TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(String library, TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers);
	
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
