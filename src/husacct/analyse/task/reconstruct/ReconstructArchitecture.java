package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.List;

import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.SoftwareUnitDTO;

import org.apache.log4j.Logger;

public class ReconstructArchitecture {

    private IModelQueryService queryService;
    ArrayList<String> rootPackagesWithClasses; // The unique names of the first packages (starting from the project root) that contain one or more classes.
    SoftwareUnitDTO xLibrariesRootPackage;
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);

	
	public ReconstructArchitecture(IModelQueryService queryService) {
		this.queryService = queryService;
		determineInternalRootPackagesWithClasses(); 
		identifyLayers();
		identifyComponents();
		identifySubSystems();
		IdentifyAdapters();
	}

	private void determineInternalRootPackagesWithClasses() { 
		rootPackagesWithClasses = new ArrayList<String>();
		SoftwareUnitDTO[] allRootUnits = queryService.getSoftwareUnitsInRoot();
		for (SoftwareUnitDTO rootModule : allRootUnits) {
			if (rootModule.uniqueName.equals("xLibraries")) {
				xLibrariesRootPackage = rootModule;
			} else {
				rootPackagesWithClasses.addAll(queryService.getRootPackagesWithClass(rootModule.uniqueName));
			}
		}
	}
	
	private void identifyLayers() {
		// First, identify the bottom layer. Look for packages with dependencies to external systems only.
		
		// Next, look iteratively for packages on top of the bottom layer, et cetera.  
		
	}
	
	private void identifyComponents() {
		
	}
	
	private void identifySubSystems() {
		
	}

	private void IdentifyAdapters() { // Here, and adapter is a module with a IsTheOnlyModuleAllowedToUse rule.
		
	}
	
	private void createModule() {
		
	}
	
	private void createRule() {
		
	}

}
