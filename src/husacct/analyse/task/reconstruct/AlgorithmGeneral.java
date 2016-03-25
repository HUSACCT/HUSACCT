package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.TreeMap;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;

public abstract class AlgorithmGeneral {
	
	//private IDefineSarService defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
	private ArrayList<String> reverseReconstrucitonList = new ArrayList<String>();

	//DE METHODES VOOR AlgorithmSelectedModule
	public abstract void execute(ModuleDTO Module, int th, IModelQueryService qService, String library, String dependencyType);
	public abstract ArrayList<SoftwareUnitDTO> getClasses(String library);
	public abstract TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(String library, TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers);
	
	public void reverse(){
		IDefineSarService defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
		for(String logicalPath : reverseReconstrucitonList){
			defineSarService.removeModule(logicalPath);
		}
	}
	
	public void addToReverseReconstructionList(String logicalpath){
		reverseReconstrucitonList.add(logicalpath);
	}

}
