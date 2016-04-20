package husacct.analyse.task.reconstruct.gateways.HUSACCT;

import java.util.ArrayList;
import java.util.Arrays;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.IAlgorithm;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.RelationTypes;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.SoftwareUnitDTO;

public abstract class AlgorithmHUSACCT extends IAlgorithm{
	protected IModelQueryService queryService;
	protected int threshold; 
	
	
	protected ArrayList<DependencyDTO> getDependencies_From_SoftwareUnit(SoftwareUnitDTO softwareUnitDTO, ArrayList<SoftwareUnitDTO> sofwareUnitDTOs, String relationType){
		ArrayList<DependencyDTO> dependecyDTOs = new ArrayList<DependencyDTO>();
		for (SoftwareUnitDTO possibleDependency : sofwareUnitDTOs){
			DependencyDTO[] dependencies = null;
			switch (relationType) {
				case RelationTypes.umlLinks:
					dependencies = queryService.getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(softwareUnitDTO.uniqueName, possibleDependency.uniqueName);
					break;
				case RelationTypes.accessCallReferenceDependencies:
					dependencies = queryService.getDependencies_OnlyAccessCallAndReferences_FromSoftwareUnitToSoftwareUnit(softwareUnitDTO.uniqueName, possibleDependency.uniqueName);
					break;
				default : //RelationTypes.allDependencies
					dependencies = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(softwareUnitDTO.uniqueName, possibleDependency.uniqueName);
					break;
			}
			dependecyDTOs.addAll(Arrays.asList(dependencies));
		}
		return dependecyDTOs;
	}
	
	protected ArrayList<DependencyDTO> getDependencies_Towards_SoftwareUnit(SoftwareUnitDTO softwareUnitDTO, ArrayList<SoftwareUnitDTO> sofwareUnitDTOs, String relationType){
		ArrayList<DependencyDTO> dependecyDTOs = new ArrayList<DependencyDTO>();
		for (SoftwareUnitDTO possibleDependency : sofwareUnitDTOs){
			DependencyDTO[] dependencies = null;
			switch (relationType) {
				case RelationTypes.umlLinks:
					dependencies = queryService.getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(possibleDependency.uniqueName, softwareUnitDTO.uniqueName);
					break;
				case RelationTypes.accessCallReferenceDependencies:
					dependencies = queryService.getDependencies_OnlyAccessCallAndReferences_FromSoftwareUnitToSoftwareUnit(possibleDependency.uniqueName, softwareUnitDTO.uniqueName);
					break;
				default : //RelationTypes.allDependencies
					dependencies = queryService.getDependenciesFromSoftwareUnitToSoftwareUnit(possibleDependency.uniqueName, softwareUnitDTO.uniqueName);
					break;
			}
			dependecyDTOs.addAll(Arrays.asList(dependencies));
		}
		return dependecyDTOs;
	}
}
