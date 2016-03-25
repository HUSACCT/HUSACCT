package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.TreeMap;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.analyse.serviceinterface.dto.UmlLinkDTO;
import husacct.common.dto.ModuleDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;
import husacct.define.domain.module.ModuleStrategy;

import org.apache.log4j.Logger;
import org.jfree.ui.Layer;

public class ReconstructArchitecture {

	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);
	private IModelQueryService queryService;
	private IDefineSarService defineSarService;
	private IDefineService defineService;
	
	private ArrayList<String> reverseReconstrucitonList = new ArrayList<String>();

	// The first packages (starting from the project root) that contain one or more classes.
	private ArrayList<SoftwareUnitDTO> internalRootPackagesWithClasses; 

	// This array contains the classes of the selected Module
	private ArrayList<SoftwareUnitDTO> selectedModuleWithClasses;

	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> identifiedLayers;

	// External system variables
	private String xLibrariesRootPackage = "xLibraries";
	private ArrayList<SoftwareUnitDTO> xLibrariesMainPackages = new ArrayList<SoftwareUnitDTO>();
	// Layer variables
	private TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
	private int layerThreshold = 10; // Percentage of allowed violating
										// dependencies
	
	private AlgorithmGeneral algorithm;

	public ReconstructArchitecture(IModelQueryService queryService) {

		this.queryService = queryService;
		this.defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
		this.defineService = ServiceProvider.getInstance().getDefineService();
		identifyExternalSystems();
		//ModuleDTO selectedModule = defineSarService.getModule_SelectedInGUI();
		// identifyLayersAtRootLevel();
		// identifyMultipleLayers();
		
		//identifyLayersAtRootLevel();
		identifyComponents();
		identifySubSystems();
		IdentifyAdapters();

		// Example to retrieve UmlLink
		getUmlLinks();
	}

	public void startReconstruction(ModuleDTO selectedModule, String approach, int threshold, String dependencyType) {
		this.layerThreshold = threshold;
		algorithm = new AlgorithmGeneral() {
			
			@Override
			public TreeMap<Integer, ArrayList<SoftwareUnitDTO>> getClasses(String library,
					TreeMap<Integer, ArrayList<SoftwareUnitDTO>> layers) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public ArrayList<SoftwareUnitDTO> getClasses(String library) {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public void define(ModuleDTO Module, int th, IModelQueryService qService, String library, String dependencyType) {
				logger.info("Error: No approach selected");
				
			}
		};
		
		/*
		 * DependencyType is de soort relaties waarop het algorithme gaat checken
		 * tot nu toe zijn er alleen deze twee:
		 * "umlDependency"
		 * "softwareUnitDependency"
		 */
		
		switch (approach) {
		case ("Goldstein - multipleLayerApproach"):
			algorithm = new Algorithm_Goldstein_MultiLayer();
			break;
		case ("Goldstein - selectedModuleApproach"):
			if(selectedModule.logicalPath.equals("**") || selectedModule == null || selectedModule.logicalPath.equals("")){ //is root
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
		case ("Component recognition")://micheals approach
			algorithm = new AlgorithmComponents(queryService);
			break;
		}
		algorithm.define(selectedModule, threshold, queryService, xLibrariesRootPackage, dependencyType);
		
	}
	
	public void reverseReconstruction(){
		algorithm.reverse();
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
	/*
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
	
	
	private void identifyLayersAtRootLevel(String dependencyType) {
		determineInternalRootPackagesWithClasses();
		identifyLayers(internalRootPackagesWithClasses, dependencyType);
		for (Integer herarchicalLevel : layers.keySet()) {
			defineSarService.addModule("Layer" + herarchicalLevel, "**", "Layer", herarchicalLevel, layers.get(herarchicalLevel));
		}
	}
	
	*/
	
	
	
	/*
	private void identifyMultipleLayers(String dependencyType) {
		identifyLayersAtRootLevel(dependencyType);
		identifiedLayers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
		identifiedLayers = layers;	
		defineSarService.getModule_SelectedInGUI();
		layers = new TreeMap<Integer, ArrayList<SoftwareUnitDTO>>();
		
		for(int i : identifiedLayers.keySet()){
			identifyLayers(identifiedLayers.get(i), dependencyType);
			logger.info(layers);	
			if(layers.keySet().size() > 1){
				for (Integer herarchicalLevel : layers.keySet()) {
					defineSarService.addModule("Layerrr" + herarchicalLevel, "Layer" + i, "Layer", herarchicalLevel, layers.get(herarchicalLevel));	
				}
			}	
		}
	}
	
	
	private void identifyLayers(ArrayList<SoftwareUnitDTO> units, String depedencyType) {
		// 1) Assign all internalRootPackages to bottom layer
		int layerId = 1;
		ArrayList<SoftwareUnitDTO> assignedUnits = new ArrayList<SoftwareUnitDTO>();
		assignedUnits.addAll(units);
		layers.put(layerId, assignedUnits);

		// 2) Identify the bottom layer. Look for packages with dependencies to
		// external systems only.
		identifyTopLayerBasedOnUnitsInBottomLayer(layerId, depedencyType);

		// 3) Look iteratively for packages on top of the bottom layer, et
		// cetera.
		while (layers.lastKey() > layerId) {
			layerId++;
			identifyTopLayerBasedOnUnitsInBottomLayer(layerId, depedencyType);
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
			
		}

		logger.info(" Number of added Layers: " + layers.size());
	}

	
	
	private void identifyTopLayerBasedOnUnitsInBottomLayer(int bottomLayerId, String dependencyType) {
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
					int nrOfDependenciesFromsoftwareUnitToOther =0;
					int nrOfDependenciesFromOtherTosoftwareUnit=0;
					
					switch(dependencyType){
					case "umlDependency":
						nrOfDependenciesFromsoftwareUnitToOther = queryService.getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName).length;
						nrOfDependenciesFromOtherTosoftwareUnit = queryService.getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName).length;
						break;
						
					case "softwareUnitDependency":
						nrOfDependenciesFromsoftwareUnitToOther = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnit.uniqueName, otherSoftwareUnit.uniqueName).length;
						nrOfDependenciesFromOtherTosoftwareUnit = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(otherSoftwareUnit.uniqueName, softwareUnit.uniqueName).length;
						break;
					}
					
					if (nrOfDependenciesFromsoftwareUnitToOther > ((nrOfDependenciesFromOtherTosoftwareUnit / 100) * layerThreshold)) {
						rootPackageDoesNotUseOtherPackage = false;
					}
				}
			}
			
			if (rootPackageDoesNotUseOtherPackage) { // Leave unit in the lower layer
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
	*/
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

