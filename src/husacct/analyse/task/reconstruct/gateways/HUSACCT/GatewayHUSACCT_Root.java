package husacct.analyse.task.reconstruct.gateways.HUSACCT;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.AlgorithmParameter;
import husacct.analyse.task.reconstruct.parameters.NumberFieldPanel;
import husacct.analyse.task.reconstruct.parameters.ParameterPanel;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.enums.ModuleTypes;

public class GatewayHUSACCT_Root extends AlgorithmHUSACCT{
	private String relationType;
	private final Logger logger = Logger.getLogger(GatewayHUSACCT_Root.class);
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
		createModule(gateways);
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
		Set<SoftwareUnitDTO> set = new HashSet<SoftwareUnitDTO>();
		DependencyDTO[] allDependecies = queryService.getAllDependencies();
		for (SoftwareUnitDTO softwareUnitDTO : bottomLayer){
			ArrayList<DependencyDTO> unitDependencies = new ArrayList<>();
			ArrayList<DependencyDTO> unitExternalDependencies = new ArrayList<>();
			for(DependencyDTO dep : allDependecies){
				if(dep.from.equals(softwareUnitDTO.uniqueName)){
					unitDependencies.add(dep);
					SoftwareUnitDTO depTo = queryService.getSoftwareUnitByUniqueName(dep.to);
					if (depTo.type.toUpperCase().equals("LIBRARY")){
						unitExternalDependencies.add(dep);
					}
				}
			}
			int totalNumberOfDep = unitDependencies.size();
			double thresHoldDependencies = (double) (totalNumberOfDep * (threshold*0.01));
			if(unitExternalDependencies.size() > thresHoldDependencies){
				set.add(softwareUnitDTO);
			}
		}
		gateways.addAll(set);
		return gateways;
	}
	private void createModule(ArrayList<SoftwareUnitDTO> softwareUnits){
		ModuleDTO newModule = defineSarService.addModule("Gateway", "**", ModuleTypes.COMPONENT.toString(), 0, softwareUnits);
		addToReverseReconstructionList(newModule);
	}

	@Override
	public ReconstructArchitectureDTO getAlgorithmThresholdSettings() {
		ReconstructArchitectureDTO reconstructArchitecture = new ReconstructArchitectureDTO();
		reconstructArchitecture.approachConstant = AnalyseReconstructConstants.Algorithm.Gateways_HUSACCT_Root;
		reconstructArchitecture.parameterPanels = createParameterPanels();
		reconstructArchitecture.threshold = 10;
		return reconstructArchitecture;
	}
	
	private ArrayList<ParameterPanel> createParameterPanels(){
		ArrayList<ParameterPanel> parameterPanels = new ArrayList<>();
		
		ParameterPanel numberField = new NumberFieldPanel("Threshold", AlgorithmParameter.Threshold, 10);
		numberField.value = 10;
		numberField.minimumValue = 0;
		numberField.maximumValue = 100;
		parameterPanels.add(numberField);
		
		return parameterPanels;
	}
}
