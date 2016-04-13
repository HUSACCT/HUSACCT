package husacct.analyse.task.reconstruct;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.analyse.AnalyseConstants;
import husacct.analyse.AnalyseConstants.Algorithm;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.components.HUSACCT.ComponentsHUSACCT_SelectedModule;
import husacct.analyse.task.reconstruct.layers.goldstein.LayersGoldstein_RootImproved;
import husacct.analyse.task.reconstruct.layers.goldstein.LayersGoldstein_RootMultipleLayers;
import husacct.analyse.task.reconstruct.layers.goldstein.LayersGoldstein_RootOriginal;
import husacct.analyse.task.reconstruct.layers.goldstein.LayersGoldstein_SelectedModuleImproved;
import husacct.analyse.task.reconstruct.layers.scanniello.LayersScanniello_RootImproved;
import husacct.analyse.task.reconstruct.layers.scanniello.LayersScanniello_RootOriginal;
import husacct.analyse.task.reconstruct.layers.scanniello.LayersScanniello_SelectedModuleImproved;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;

public class ReconstructArchitecture {

	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private IModelQueryService queryService;
	private IDefineService defineService;
	private IDefineSarService defineSarService;
	// External system variables
	private String xLibrariesRootPackage = "xLibraries";
	private ArrayList<SoftwareUnitDTO> xLibrariesMainPackages = new ArrayList<SoftwareUnitDTO>();
	private IAlgorithm algorithm = null;
	private boolean algorithmSucces = true; //this variable is necessary for the JUnit tests

	public ReconstructArchitecture(IModelQueryService queryService) {
		try {
			this.queryService = queryService;
			defineService = ServiceProvider.getInstance().getDefineService();
			defineSarService = defineService.getSarService();
			identifyExternalSystems();
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }
	}

	public void reconstructArchitecture_Execute(ReconstructArchitectureDTO dto) {
		boolean moduleSelected = 	dto.getSelectedModule() != null 
				&& !dto.getSelectedModule().logicalPath.equals("**") 
				&& !dto.getSelectedModule().logicalPath.equals("");
		
		try {
			switch (dto.getApproach()) {
				case (AnalyseConstants.Algorithm.Layers_Goldstein_Multiple_Improved):
					algorithm = new LayersGoldstein_RootMultipleLayers();
					break;
				case (AnalyseConstants.Algorithm.Layers_Goldstein_SelectedModule_Improved):
					if(!moduleSelected){ //is root
						algorithm = new LayersGoldstein_RootOriginal();
					}
					else{
						algorithm = new LayersGoldstein_SelectedModuleImproved();
					}
					break;
				case (AnalyseConstants.Algorithm.Layers_Scanniello_Improved):
					if (moduleSelected){
						algorithm = new LayersScanniello_SelectedModuleImproved();
					}
					else{
						algorithm = new LayersScanniello_RootImproved();
					}
					break;
				case (AnalyseConstants.Algorithm.Layers_Scanniello_Original):
					algorithm = new LayersScanniello_RootOriginal();
					break;
				case (AnalyseConstants.Algorithm.Component_HUSACCT_SelectedModule):
					algorithm = new ComponentsHUSACCT_SelectedModule();
					break;
				default:
					algorithm = null;	
			}
			if (algorithm != null) {
				algorithm.clearReverseReconstructionList(); 
				algorithm.executeAlgorithm(dto, queryService, xLibrariesRootPackage);
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
				if (!rootModule.logicalPath.equals("ExternalSystems")) {
					defineSarService.removeModule(rootModule.logicalPath);
				}
			}
			if (algorithm != null) {
				algorithm.clearReverseReconstructionList();
			}

		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }
	}
	
	public ModuleDTO[] getGoldenStandard(){
		return defineService.getAllModules();
	}

	private void identifyExternalSystems() {
		// Create module "ExternalSystems"
		ArrayList<SoftwareUnitDTO> emptySoftwareUnitsArgument = new ArrayList<SoftwareUnitDTO>();
		defineSarService.addModule("ExternalSystems", "**", "ExternalLibrary", 0, emptySoftwareUnitsArgument);
		// Create a module for each childUnit of xLibrariesRootPackage
		int nrOfExternalLibraries = 0;
		for (SoftwareUnitDTO mainUnit : queryService.getChildUnitsOfSoftwareUnit(xLibrariesRootPackage)) {
			xLibrariesMainPackages.add(mainUnit);
			ArrayList<SoftwareUnitDTO> softwareUnitsArgument = new ArrayList<SoftwareUnitDTO>();
			softwareUnitsArgument.add(mainUnit);
			defineSarService.addModule(mainUnit.name, "ExternalSystems", "ExternalLibrary", 0, softwareUnitsArgument);
			nrOfExternalLibraries++;
		}
		logger.info(" Number of added ExternalLibraries: " + nrOfExternalLibraries);
	}
	
	public boolean getAlgorithmSucces(){
		return algorithmSucces;
	}
}

