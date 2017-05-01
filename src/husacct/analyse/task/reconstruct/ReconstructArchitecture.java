package husacct.analyse.task.reconstruct;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithms;
import husacct.analyse.task.reconstruct.algorithms.Algorithm_SuperClass;
import husacct.analyse.task.reconstruct.algorithms.hu.combined.CombinedAndIterative_Layers_Components_Subsystems;
import husacct.analyse.task.reconstruct.algorithms.hu.components.ComponentsAndSubSystems_HUSACCT;
import husacct.analyse.task.reconstruct.algorithms.hu.externals.ExternalSystemAlgorithm;
import husacct.analyse.task.reconstruct.algorithms.hu.gateways.GatewayHUSACCT_Root;
import husacct.analyse.task.reconstruct.algorithms.hu.layers.Layers_HUSACCT_Algorithm_SelectedModule;
import husacct.analyse.task.reconstruct.algorithms.hu.layers.Layers_HUSACCT_Algorithm_SAEroCon2016;
import husacct.analyse.task.reconstruct.algorithms.hu.layers.goldstein.Layers_Goldstein_Root_Initial;
import husacct.analyse.task.reconstruct.algorithms.hu.layers.goldstein.Layers_Goldstein_HUSACCT_Algorithm_SelectedModule;
import husacct.analyse.task.reconstruct.algorithms.hu.layers.scanniello.Layers_Scanniello_Root_Initial;
import husacct.analyse.task.reconstruct.algorithms.hu.layers.scanniello.Layers_Scanniello_SelectedModule_Improved;
import husacct.analyse.task.reconstruct.dto.ReconstructArchitectureDTO;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;

public class ReconstructArchitecture {

	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private IModelQueryService queryService;
	private IDefineService defineService;
	private IDefineSarService defineSarService;
	private Algorithm_SuperClass algorithm = null;
	private boolean algorithmSucces = true; //this variable is necessary for the JUnit tests

	public ReconstructArchitecture(IModelQueryService queryService) {
		try {
			this.queryService = queryService;
			defineService = ServiceProvider.getInstance().getDefineService();
			defineSarService = defineService.getSarService();
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }
	}

	public void reconstructArchitecture_Execute(ReconstructArchitectureDTO dto) {
		try {
			algorithm = findAlgorithm(dto.approachId);
			if (algorithm != null) {
				algorithm.clearReverseReconstructionLists(); 
				algorithm.executeAlgorithm(dto, queryService);
			}
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	        algorithmSucces = false;
	    }
	}
	
	public void reverseReconstruction(){
		try {
			if (algorithm != null) {
				algorithm.reverse();
			}
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }
	}

	public void clearAllModules() {
		try {
			ModuleDTO[] rootModules = defineService.getModule_AllRootModules(); 
			for(ModuleDTO rootModule : rootModules){
				defineSarService.removeModule(rootModule.logicalPath);
			}
			if (algorithm != null) {
				algorithm.clearReverseReconstructionLists();
			}

		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }
	}
	
	public boolean getAlgorithmSucces(){
		return algorithmSucces;
	}
	
	public ReconstructArchitectureDTO getReconstructArchitectureDTO(String approachId){
		ReconstructArchitectureDTO raDTO = null;
		Algorithm_SuperClass foundAlgorithm = findAlgorithm(approachId);
		raDTO = foundAlgorithm.getAlgorithmParameterSettings();
		return raDTO;
	}

	
	private Algorithm_SuperClass findAlgorithm(String approachId) {
		Algorithm_SuperClass foundAlgorithm = null;
		try {
			switch (approachId) {
				case (Algorithms.Layers_HUSACCT_SelectedModule):
					foundAlgorithm = new Layers_HUSACCT_Algorithm_SelectedModule(queryService);
					break;
				case (Algorithms.Component_HUSACCT_SelectedModule):
					foundAlgorithm = new ComponentsAndSubSystems_HUSACCT(queryService);
					break;
				case (Algorithms.Externals_Recognition):
					foundAlgorithm = new ExternalSystemAlgorithm(queryService);
					break;
				case (Algorithms.CombinedAndIterative_HUSACCT_SelectedModule):
					foundAlgorithm = new CombinedAndIterative_Layers_Components_Subsystems(queryService);
					break;
				case (Algorithms.Layers_HUSACCT_SAEroCon2016):
					foundAlgorithm = new Layers_HUSACCT_Algorithm_SAEroCon2016(queryService);
					break;
				case (Algorithms.Layers_Goldstein_Root_Original):
					foundAlgorithm = new Layers_Goldstein_Root_Initial(queryService);
					break;
				case (Algorithms.Layers_Goldstein_HUSACCT_SelectedModule):
					foundAlgorithm = new Layers_Goldstein_HUSACCT_Algorithm_SelectedModule(queryService);
					break;
				case (Algorithms.Layers_Scanniello_Improved):
					foundAlgorithm = new Layers_Scanniello_SelectedModule_Improved(queryService);
					break;
				case (Algorithms.Layers_Scanniello_Original):
					foundAlgorithm = new Layers_Scanniello_Root_Initial(queryService);
					break;
				case (Algorithms.Gateways_HUSACCT_Root):
					foundAlgorithm = new GatewayHUSACCT_Root(queryService);
					break;
				default:
					foundAlgorithm = null;	
			}
		} catch (Exception e) {
	        logger.warn(" Exception while finding algotitm: "  + e );
	    }
		return foundAlgorithm;
	}
}

