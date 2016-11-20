package husacct.analyse.task.reconstruct.algorithms.hu.combined;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.AlgorithmParameter;
import husacct.analyse.task.reconstruct.algorithms.Algorithm_SuperClass;
import husacct.analyse.task.reconstruct.algorithms.hu.components.ComponentsAndSubSystems_HUSACCT;
import husacct.analyse.task.reconstruct.algorithms.hu.layers.Layers_HUSACCT_Algorithm_SelectedModule_SAEreCon;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.enums.ModuleTypes;

public class CombinedAndIterative_Layers_Components_Subsystems extends Algorithm_SuperClass{
	private ModuleDTO selectedModule;
	private Algorithm_SuperClass algorithm = null;
	private final Logger logger = Logger.getLogger(CombinedAndIterative_Layers_Components_Subsystems.class);
	private int maxDepth = 4;

	public CombinedAndIterative_Layers_Components_Subsystems (IModelQueryService queryService) {
		super(queryService);
	}
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) {
		try {
			selectedModule = dto.getSelectedModule();
			if (selectedModule.logicalPath.equals("")) {
				selectedModule.logicalPath = "**"; // Root of intended software architecture
				selectedModule.type = "Root"; // Root of intended software architecture
				dto.setSelectedModule(selectedModule);
			}

			// If the selectedModule is of type Facade or ExternalLibrary, nothing is done.
			if ((selectedModule == null) || selectedModule.type.equals(ModuleTypes.EXTERNAL_LIBRARY.toString()) || selectedModule.type.equals(ModuleTypes.FACADE.toString())) {
				return;
			}
			
			// 1) Apply layer, component and subsystem algorithms on selected module
			identifyLayersComponentsOrSubsystems(dto);
			// 2) Iteratively, for each child module if not of type Interface, apply the combined algorithms
			applyAlgorithmOnChildModules(dto, 1);
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }
	}

	private void identifyLayersComponentsOrSubsystems(ReconstructArchitectureDTO dto) {
		try {
			// a) First identify layers
			algorithm = new Layers_HUSACCT_Algorithm_SelectedModule_SAEreCon(queryService);
			algorithm.executeAlgorithm(dto, queryService);
			// b) If number of layers >= 3, than continue
			ModuleDTO[] childModules = defineService.getModule_TheChildrenOfTheModule(dto.getSelectedModule().logicalPath);
			ArrayList<ModuleDTO> layers = new ArrayList<ModuleDTO>();
			for (ModuleDTO childModule : childModules) {
				if (childModule.type.equals(ModuleTypes.LAYER.toString())) {
					layers.add(childModule);
				}
			}
			if (layers.size() < 3) { 
				// c) If number of layers < 3, than: i) reverse; ii) identify C&S; iii) continue
				reverseLayers(layers);
				algorithm = new ComponentsAndSubSystems_HUSACCT(queryService);
				algorithm.executeAlgorithm(dto, queryService);
			}
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }
	}
	
	private void applyAlgorithmOnChildModules(ReconstructArchitectureDTO dto, int depth) {
		try {
			if (depth < maxDepth) {
				ModuleDTO[] childModules = defineService.getModule_TheChildrenOfTheModule(dto.getSelectedModule().logicalPath); 
				for(ModuleDTO childModule : childModules){
					if (!childModule.type.equals(ModuleTypes.FACADE)) {
						ReconstructArchitectureDTO childDto = new ReconstructArchitectureDTO();
						childDto.threshold = dto.threshold;
						childDto.relationType = dto.relationType;
						childDto.granularity = dto.granularity;
						childDto.setSelectedModule(childModule);
						identifyLayersComponentsOrSubsystems(childDto);
						int deptOfChild = depth + 1;
						applyAlgorithmOnChildModules(childDto, deptOfChild);
					}
				}
			}
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
		}
	}

	private void reverseLayers(ArrayList<ModuleDTO> layers) {
		for(ModuleDTO layer : layers){
			defineSarService.removeModule(layer.logicalPath);
		}
	}

	
	@Override
	public ReconstructArchitectureDTO getAlgorithmParameterSettings() {
		ReconstructArchitectureDTO reconstructArchitecture = new ReconstructArchitectureDTO();
		reconstructArchitecture.approachConstant = AnalyseReconstructConstants.Algorithm.CombinedAndIterative_HUSACCT_SelectedModule;
		reconstructArchitecture.threshold = 5;
		reconstructArchitecture.relationType = AnalyseReconstructConstants.RelationTypes.allDependencies;
		reconstructArchitecture.granularity = AnalyseReconstructConstants.Granularities.Packages;
		reconstructArchitecture.parameterDTOs = createParameterPanels();
		return reconstructArchitecture;
	}
	
	private ArrayList<ReconstructArchitectureParameterDTO> createParameterPanels(){
		ArrayList<ReconstructArchitectureParameterDTO> parameterDTOs = new ArrayList<>();
		parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createThresholdParameter(5));
		parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createRelationTypeParameter(AnalyseReconstructConstants.RelationTypes.allDependencies));
		parameterDTOs.add(ReconstructArchitectureParameterDTO.DefaultParameterDTOs.createGranularityPanel(AnalyseReconstructConstants.Granularities.Packages));
		return parameterDTOs;
	}

}
