package husacct.analyse.task.reconstruct.gateways.HUSACCT;

import java.util.ArrayList;
import java.util.List;

import husacct.analyse.domain.IModelQueryService;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;

public class GatewayHUSACCT_Root extends AlgorithmHUSACCT{
	private String relationType;
	//Gateway finder.
	//Gateway: only has inside-dependencies towards it, so it is located in the bottom layer
	//Gateway: is the only class with imports towards an external application 

	public GatewayHUSACCT_Root (IModelQueryService queryService) {
		super(queryService);
	}
		
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) throws Exception {
		this.threshold = dto.getThreshold();
		this.relationType = dto.getRelationType();
		ArrayList<SoftwareUnitDTO> bottomLayer = identifyBottomLayer();
		ArrayList<SoftwareUnitDTO> gateways = identifyGateWays(bottomLayer);
		gateways.getClass();//is alleen om de "gateways is unused" warning weg te halen.
	}
	
	private ArrayList<SoftwareUnitDTO> identifyBottomLayer(){
		ArrayList<SoftwareUnitDTO> bottomLayer = new ArrayList<SoftwareUnitDTO>();
		
		List<SoftwareUnitDTO> classes = queryService.getAllClasses();
		ArrayList<SoftwareUnitDTO> classesArray = new ArrayList<SoftwareUnitDTO>(classes);
		for (SoftwareUnitDTO softwareUnitDTO : classesArray){
			ArrayList<DependencyDTO> dependecyDTOsFromSoftwareUnit = getDependencies_From_SoftwareUnit(softwareUnitDTO, classesArray, relationType);
			ArrayList<DependencyDTO> dependecyDTOsToSoftwareUnit = getDependencies_Towards_SoftwareUnit(softwareUnitDTO, classesArray, relationType);
			
			int totalNumberOfDependencies = dependecyDTOsToSoftwareUnit.size() + dependecyDTOsFromSoftwareUnit.size();
			int thresHoldDependencies = (int) (totalNumberOfDependencies * (threshold*0.01));
			int NumberOfDependeciesToTheSoftwareUnit = dependecyDTOsToSoftwareUnit.size() - thresHoldDependencies;
			int NumberOfDependenciesFromTheSoftwareUnit = dependecyDTOsFromSoftwareUnit.size() - thresHoldDependencies;
			
			//The softwareUnit has (after the threshold) no dependencies with other classes, Only other classes with the softwareUnit.
			if (NumberOfDependenciesFromTheSoftwareUnit <= 0 && NumberOfDependeciesToTheSoftwareUnit > 0){
				bottomLayer.add(softwareUnitDTO);
			}
		}
		return bottomLayer;
	}
	
	private ArrayList<SoftwareUnitDTO> identifyGateWays(ArrayList<SoftwareUnitDTO> bottomLayer){
		ArrayList<SoftwareUnitDTO> gateways = new ArrayList<SoftwareUnitDTO>();
		DependencyDTO[] allDependecies = queryService.getAllDependencies();
		for (SoftwareUnitDTO softwareUnitDTO : bottomLayer){
			ArrayList<DependencyDTO> unitDependencies = new ArrayList<>();
			ArrayList<DependencyDTO> unitExternalDependencies = new ArrayList<>();
			for(DependencyDTO dep : allDependecies){
				if(dep.from.equals(softwareUnitDTO.uniqueName)){
					unitDependencies.add(dep);
					SoftwareUnitDTO depTo = queryService.getSoftwareUnitByUniqueName(dep.to);
					if (depTo.type.toUpperCase().equals("EXTERNALLIBRARY")){
						unitExternalDependencies.add(dep);
					}
				}
			}
			int totalNumberOfDep = unitDependencies.size();
			int thresHoldDependencies = (int) (totalNumberOfDep * (threshold*0.01));
			int unitNumberOfExternalDep = unitExternalDependencies.size() - thresHoldDependencies;
			if (unitNumberOfExternalDep > 0){
				gateways.add(softwareUnitDTO);
			}
		}
		
		return gateways;
	}

}
