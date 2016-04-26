package husacct.analyse.task.reconstruct.externals;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;

public class ExternalSystemAlgorithm extends ExternalAlgorithm_Super{
	
	private ArrayList<SoftwareUnitDTO> xLibrariesMainPackages = new ArrayList<SoftwareUnitDTO>();
	private final Logger logger = Logger.getLogger(ExternalSystemAlgorithm.class);

	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) throws Exception {
		this.queryService = queryService;
		this.threshold = dto.getThreshold();
		this.relationType = dto.getRelationType();
		identifyExternalSystems();
	}
	
	private void identifyExternalSystems() {
		ArrayList<SoftwareUnitDTO> emptySoftwareUnitsArgument = new ArrayList<SoftwareUnitDTO>();
		try {
			ModuleDTO newGroupModule = defineSarService.addModule("ExternalSystems", "**", "ExternalLibrary", 0, emptySoftwareUnitsArgument);
			addToReverseReconstructionList(newGroupModule); //add to cache for reverse
			// Create a module for each childUnit of xLibrariesRootPackage
			int nrOfExternalLibraries = 0;
			for (SoftwareUnitDTO mainUnit : queryService.getChildUnitsOfSoftwareUnit(xLibrariesRootPackage)) {
				xLibrariesMainPackages.add(mainUnit);
				ArrayList<SoftwareUnitDTO> softwareUnitsArgument = new ArrayList<SoftwareUnitDTO>();
				softwareUnitsArgument.add(mainUnit);
				ModuleDTO newModule = defineSarService.addModule(mainUnit.name, "ExternalSystems", "ExternalLibrary", 0, softwareUnitsArgument);
				if (!newModule.logicalPath.equals("")) {
					nrOfExternalLibraries++;
					addToReverseReconstructionList(newModule); //add to cache for reverse
				}
			}
			logger.info(" Number of added ExternalLibraries: " + nrOfExternalLibraries);
		} catch (Exception e) {
	        logger.error(" Exception: "  + e );
	    }
	}

}
