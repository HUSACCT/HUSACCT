package husacct.analyse.task.reconstruct;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.Algorithm;
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
			switch (dto.approachConstant) {
				case (Algorithm.Layers_HUSACCT_SelectedModule):
					algorithm = new Layers_HUSACCT_Algorithm_SelectedModule(queryService);
					break;
				case (Algorithm.Component_HUSACCT_SelectedModule):
					algorithm = new ComponentsAndSubSystems_HUSACCT(queryService);
					break;
				case (Algorithm.Externals_Recognition):
					algorithm = new ExternalSystemAlgorithm(queryService);
					break;
				case (Algorithm.CombinedAndIterative_HUSACCT_SelectedModule):
					algorithm = new CombinedAndIterative_Layers_Components_Subsystems(queryService);
					break;
				case (Algorithm.Layers_HUSACCT_SAEroCon2016):
					algorithm = new Layers_HUSACCT_Algorithm_SAEroCon2016(queryService);
					break;
				case (Algorithm.Layers_Goldstein_Root_Original):
					algorithm = new Layers_Goldstein_Root_Initial(queryService);
					break;
				case (Algorithm.Layers_Goldstein_HUSACCT_SelectedModule):
					algorithm = new Layers_Goldstein_HUSACCT_Algorithm_SelectedModule(queryService);
					break;
				case (Algorithm.Layers_Scanniello_Improved):
					algorithm = new Layers_Scanniello_SelectedModule_Improved(queryService);
					break;
				case (Algorithm.Layers_Scanniello_Original):
					algorithm = new Layers_Scanniello_Root_Initial(queryService);
					break;
				case (Algorithm.Gateways_HUSACCT_Root):
					algorithm = new GatewayHUSACCT_Root(queryService);
					break;
				default:
					algorithm = null;	
			}
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
}

