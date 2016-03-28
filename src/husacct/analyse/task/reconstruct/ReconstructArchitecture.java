package husacct.analyse.task.reconstruct;

import java.util.ArrayList;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;

import org.apache.log4j.Logger;

public class ReconstructArchitecture {

	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private IModelQueryService queryService;
	private IDefineService defineService;
	private IDefineSarService defineSarService;
	// External system variables
	private String xLibrariesRootPackage = "xLibraries";
	private ArrayList<SoftwareUnitDTO> xLibrariesMainPackages = new ArrayList<SoftwareUnitDTO>();
	private AlgorithmGeneral algorithm = null;

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
		try {
			switch (dto.getApproach()) {
				case ("Goldstein - multipleLayerApproach"):
					algorithm = new Algorithm_Goldstein_MultiLayer();
					break;
				case ("Goldstein - selectedModuleApproach"):
					if(dto.getSelectedModule() == null || dto.getSelectedModule().logicalPath.equals("**") || dto.getSelectedModule().logicalPath.equals("")){ //is root
						algorithm = new Algorithm_Goldstein_Root();
					}
					else{
						algorithm = new Algorithm_Goldstein_SelectedModule();
					}
					break;
				case ("Scanniello - selectedModuleApproach"): //second approach for Gui-team
					algorithm = new Algorithm_Scanniello_SelectedModule();
					break;
				case ("Scanniello - originalRoot"):
					algorithm = new Algorithm_Scanniello_Original_Root();
					break;
				case ("Scanniello - improvedRoot"):
					algorithm = new Algorithm_Scanniello_Improved_Root();
					break;
				case ("Component Recognition in Root of Selected Module"):
					algorithm = new AlgorithmComponents(queryService);
					break;
				default:
					algorithm = new Algorithm_Goldstein_Root();	
			}
			if (algorithm != null) {
				algorithm.clearReverseReconstructionList(); 
				algorithm.execute(dto.getSelectedModule(), dto.getThreshold(), queryService, xLibrariesRootPackage, dto.getRelationType());
			}
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
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
	
}

