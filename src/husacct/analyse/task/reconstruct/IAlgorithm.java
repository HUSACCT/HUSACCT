package husacct.analyse.task.reconstruct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import husacct.ServiceProvider;
import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.RelationTypes;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.define.IDefineSarService;
import husacct.define.IDefineService;

public abstract class IAlgorithm {
	protected IModelQueryService queryService;
	protected IDefineService defineService = ServiceProvider.getInstance().getDefineService();
	protected IDefineSarService defineSarService = defineService.getSarService();
	protected final String xLibrariesRootPackage = "xLibraries";

	private ArrayList<ModuleDTO> reverseReconstructionList = new ArrayList<ModuleDTO>();
	private HashMap<String, String> reverseEditModuleTypeList = new HashMap<String, String>(); // logicatPath (of the module), moduleTypeToBeReversedTo

	protected IAlgorithm(IModelQueryService queryService) {
		this.queryService = queryService;
	}
	
	public abstract void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) throws Exception;
	
	/*
	 * getAlgorithmSettings returns a ReconstructArchitectureDTO with all the info of the algorithm
	 * -	witch thresholds it is going to use
	 * -	the default values of the thresholds
	 * - 	the constant of the algorithm
	 */
	public abstract ReconstructArchitectureDTO getAlgorithmParameterSettings();
	
	
	protected ModuleDTO createModule_andAddItToReverseList(String name, String parentLogicalPath, String moduleType, int hierarchicalLevel, ArrayList<SoftwareUnitDTO> softwareUnits) {
		ModuleDTO newModule = defineSarService.addModule(name, parentLogicalPath, moduleType, hierarchicalLevel, softwareUnits);
		if (!newModule.logicalPath.equals("")) {
			addToReverseReconstructionList(newModule); //add to cache for reverse
		}
		return newModule;
	}
	
	public void reverse(){
		IDefineSarService defineSarService = ServiceProvider.getInstance().getDefineService().getSarService();
		for(ModuleDTO module : reverseReconstructionList){
			defineSarService.removeModule(module.logicalPath);
		}
		for (String logicalPath : reverseEditModuleTypeList.keySet()) {
			defineSarService.editModule(logicalPath, reverseEditModuleTypeList.get(logicalPath), null, 0, null);
		}
		clearReverseReconstructionLists();
	}
	
	protected void addToReverseEditModuleList(String logicalPath, String moduleTypeToBeReversedTo) {
		reverseEditModuleTypeList.put(logicalPath, moduleTypeToBeReversedTo);
	}

	protected void addToReverseReconstructionList(ModuleDTO newModule){
		reverseReconstructionList.add(newModule);
	}

	protected void clearReverseReconstructionLists() {
		reverseReconstructionList.clear();
		reverseEditModuleTypeList.clear();
	}

	protected ArrayList<DependencyDTO> getRelationsBetweenSoftwareUnits(String softwareUnitFrom, String softwareUnitTo, String relationType){
		ArrayList<DependencyDTO> dependecyDTOs = new ArrayList<DependencyDTO>();
		DependencyDTO[] dependencies = null;
		switch (relationType) {
			case RelationTypes.umlLinks:
				dependencies = queryService.getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(softwareUnitFrom, softwareUnitTo);
				break;
			case RelationTypes.accessCallReferenceDependencies:
				dependencies = queryService.getDependencies_OnlyAccessCallAndReferences_FromSoftwareUnitToSoftwareUnit(softwareUnitFrom, softwareUnitTo);
				break;
			default : //RelationTypes.allDependencies
				dependencies = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnitFrom, softwareUnitTo);
				break;
		}
		dependecyDTOs.addAll(Arrays.asList(dependencies));
		return dependecyDTOs;
	}
}
