package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.analyse.serviceinterface.dto.UmlLinkDTO;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineService;

import org.apache.log4j.Logger;

public class ReconstructArchitecture {

	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private IModelQueryService queryService;
	private IDefineService defineService;

	// The first packages (starting from the project root) that contain one or more classes.
	private ArrayList<SoftwareUnitDTO> internalRootPackagesWithClasses; 

	// This array contains the classes of the selected Module
	private ArrayList<SoftwareUnitDTO> selectedModuleWithClasses;

	private ArrayList<SoftwareUnitDTO> identifiedLayers;

	// External system variables
	private String xLibrariesRootPackage = "xLibraries";
	private ArrayList<SoftwareUnitDTO> xLibrariesMainPackages = new ArrayList<SoftwareUnitDTO>();
	// Layer variables
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
	private int layerThreshold = 10; // Percentage of allowed violating
										// dependencies

	public ReconstructArchitecture(IModelQueryService queryService) {

		this.queryService = queryService;
		defineService = ServiceProvider.getInstance().getDefineService();
		identifyExternalSystems();
		ModuleDTO selectedModule = defineService.getModule_SelectedInGUI();
		identifyLayersAtSelectedModule(selectedModule);
		// identifyLayersAtRootLevel();
		// identifyMultipleLayers();

		identifyComponents();
		identifySubSystems();
		IdentifyAdapters();

		// Example to retrieve UmlLink
		getUmlLinks();
	}

	public void startReconstruction(ModuleDTO selectedModule, int layerThreshold, String approach) {
		this.layerThreshold = layerThreshold;

		switch (approach) {
		case ("layerApproach"):
			// run multiple layers
			break;
		case ("selectedModuleApproach"):
			identifyLayersAtSelectedModule(selectedModule);
			break;
		}
	}

	private void identifyExternalSystems() {
		// Create module "ExternalSystems"
		ArrayList<SoftwareUnitDTO> emptySoftwareUnitsArgument = new ArrayList<SoftwareUnitDTO>();
		defineService.addModule("ExternalSystems", "**", "ExternalLibrary", 0, emptySoftwareUnitsArgument);
		// Create a module for each childUnit of xLibrariesRootPackage
		int nrOfExternalLibraries = 0;
		for (SoftwareUnitDTO mainUnit : queryService.getChildUnitsOfSoftwareUnit(xLibrariesRootPackage)) {
			xLibrariesMainPackages.add(mainUnit);
			ArrayList<SoftwareUnitDTO> softwareUnitsArgument = new ArrayList<SoftwareUnitDTO>();
			softwareUnitsArgument.add(mainUnit);
			defineService.addModule(mainUnit.name, "ExternalSystems", "ExternalLibrary", 0, softwareUnitsArgument);
			nrOfExternalLibraries++;
		}
		logger.info(" Number of added ExternalLibraries: " + nrOfExternalLibraries);
	}

	private void determineInternalRootPackagesWithClasses() {
		internalRootPackagesWithClasses = new ArrayList<SoftwareUnitDTO>();
		SoftwareUnitDTO[] allRootUnits = queryService.getSoftwareUnitsInRoot();
		for (SoftwareUnitDTO rootModule : allRootUnits) {
			if (!rootModule.uniqueName.equals(xLibrariesRootPackage)) {
				for (String internalPackage : queryService.getRootPackagesWithClass(rootModule.uniqueName)) {
					internalRootPackagesWithClasses.add(queryService.getSoftwareUnitByUniqueName(internalPackage));

				}
			}
		}
		if (internalRootPackagesWithClasses.size() == 1) {
			// Temporal solution useful for HUSACCT20 test. To be improved!
			// E.g., classes in root are excluded from the process.
			String newRoot = internalRootPackagesWithClasses.get(0).uniqueName;
			internalRootPackagesWithClasses = new ArrayList<SoftwareUnitDTO>();
			for (SoftwareUnitDTO child : queryService.getChildUnitsOfSoftwareUnit(newRoot)) {
				if (child.type.equalsIgnoreCase("package")) {
					internalRootPackagesWithClasses.add(child);
				}
			}
		}
	}

	private void determineSelectedModuleWithClasses(ModuleDTO selectedModule) {
		selectedModuleWithClasses = new ArrayList<SoftwareUnitDTO>();
		ModuleDTO[] selectedSubModules = selectedModule.subModules;
		for (ModuleDTO subModule : selectedSubModules) {
			selectedModuleWithClasses
					.add(queryService.getSoftwareUnitByUniqueName(subModule.logicalPath.toLowerCase()));
		}
		logger.info("HELLUEUEUEUEUEUUEUEUEUEUEUUEUEUEUEUEUUE: " + selectedModuleWithClasses);
	}

	private void identifyLayersAtRootLevel() {
		determineInternalRootPackagesWithClasses();
		identifyLayers(internalRootPackagesWithClasses);
	}

	private void identifyMultipleLayers() {
		identifiedLayers = new ArrayList<SoftwareUnitDTO>();

		identifyLayersAtRootLevel();

		identifyLayers(identifiedLayers);
	}

	private void identifyLayersAtSelectedModule(ModuleDTO selectedModule) {
		// This array will be filled with the classes in one module
		// selectedModuleWithClasses = new ArrayList<SoftwareUnitDTO>();

		determineSelectedModuleWithClasses(selectedModule);

		// Identify the layers within the selected module
		identifyLayers(selectedModuleWithClasses);
	}

	private void identifyLayers(ArrayList<SoftwareUnitDTO> units) {
		logger.info(" HALLO JUMBOOOOO UNIIIITSSSSS: " + units);
		// 1) Assign all internalRootPackages to bottom layer
		int layerId = 1;
		ArrayList<SoftwareUnitDTO> assignedUnits = new ArrayList<SoftwareUnitDTO>();
		assignedUnits.addAll(units);
		layers.put(layerId, assignedUnits);

		// 2) Identify the bottom layer. Look for packages with dependencies to
		// external systems only.
		identifyTopLayerBasedOnUnitsInBottomLayer(layerId);

		// 3) Look iteratively for packages on top of the bottom layer, et
		// cetera.
		while (layers.lastKey() > layerId) {
			layerId++;
			identifyTopLayerBasedOnUnitsInBottomLayer(layerId);
		}

		// 4) Add the layers to the intended architecture
		int highestLevelLayer = layers.size();
		if (highestLevelLayer > 1) {
			// Reverse the layer levels. The numbering of the layers within the
			// intended architecture is different: the highest level layer has
			// hierarchcalLevel = 1
			int lowestLevelLayer = 1;
			int raise = highestLevelLayer - lowestLevelLayer;
			TreeMap<Integer, ArrayList<SoftwareUnitDTO>> tempLayers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
			for (int i = lowestLevelLayer; i <= highestLevelLayer; i++) {
				ArrayList<SoftwareUnitDTO> unitsOfLayer = layers.get(i);
				int level = lowestLevelLayer + raise;
				tempLayers.put(level, unitsOfLayer);
				raise--;
			}
			layers = tempLayers;
			for (Integer herarchicalLevel : layers.keySet()) {
				defineService.addModule("Layer" + herarchicalLevel, "**", "Layer", herarchicalLevel,
						layers.get(herarchicalLevel));
				identifiedLayers.addAll(layers.get(herarchicalLevel));
			}
		}

		logger.info(" Number of added Layers: " + layers.size());
	}

	private void identifyTopLayerBasedOnUnitsInBottomLayer(int bottomLayerId) {
		ArrayList<SoftwareUnitDTO> assignedUnitsOriginalBottomLayer = layers.get(bottomLayerId);
		@SuppressWarnings("unchecked")
		ArrayList<SoftwareUnitDTO> assignedUnitsBottomLayerClone = (ArrayList<SoftwareUnitDTO>) assignedUnitsOriginalBottomLayer
				.clone();
		ArrayList<SoftwareUnitDTO> assignedUnitsNewBottomLayer = new ArrayList<SoftwareUnitDTO>();
		ArrayList<SoftwareUnitDTO> assignedUnitsTopLayer = new ArrayList<SoftwareUnitDTO>();
		for (SoftwareUnitDTO softwareUnit : assignedUnitsOriginalBottomLayer) {
			boolean rootPackageDoesNotUseOtherPackage = true;
			for (SoftwareUnitDTO otherSoftwareUnit : assignedUnitsBottomLayerClone) {
				if (!otherSoftwareUnit.uniqueName.equals(softwareUnit.uniqueName)) {
					int nrOfDependenciesFromsoftwareUnitToOther = queryService
							.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName,
									otherSoftwareUnit.uniqueName).length;
					int nrOfDependenciesFromOtherTosoftwareUnit = queryService
							.getDependenciesFromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName,
									softwareUnit.uniqueName).length;
					if (nrOfDependenciesFromsoftwareUnitToOther > ((nrOfDependenciesFromOtherTosoftwareUnit / 100)
							* layerThreshold)) {
						rootPackageDoesNotUseOtherPackage = false;
					}
				}
			}
			if (rootPackageDoesNotUseOtherPackage) { // Leave unit in the lower
														// layer
				assignedUnitsNewBottomLayer.add(softwareUnit);
			} else { // Assign unit to the higher layer
				assignedUnitsTopLayer.add(softwareUnit);
			}

		}
		if ((assignedUnitsTopLayer.size() > 0) && (assignedUnitsNewBottomLayer.size() > 0)) {
			layers.remove(bottomLayerId);
			layers.put(bottomLayerId, assignedUnitsNewBottomLayer);
			bottomLayerId++;
			layers.put(bottomLayerId, assignedUnitsTopLayer);
		}
	}

	private void identifyComponents() {

	}

	private void identifySubSystems() {

	}

	private void IdentifyAdapters() { // Here, an adapter is a module with a
										// IsTheOnlyModuleAllowedToUse rule.

	}

	private void createModule() {

	}

	private void createRule() {

	}

	private void getUmlLinks() {
		// Test example for UmlLinks while running the Java AccuracyTest
		HashSet<UmlLinkDTO> umlLinks = queryService
				.getUmlLinksFromClassToOtherClasses("domain.direct.violating.InheritanceExtends");
		for (UmlLinkDTO umlLink : umlLinks) {
			String umlLinkFrom = umlLink.from;
			logger.info(" UmlLink From, To, atributeFrom, isComposite, type: " + umlLinkFrom + ", " + umlLink.to + ", "
					+ umlLink.attributeFrom + ", " + umlLink.isComposite + ", " + umlLink.type);
		}
	}

}
