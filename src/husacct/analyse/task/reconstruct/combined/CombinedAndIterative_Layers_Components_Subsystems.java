package husacct.analyse.task.reconstruct.combined;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.IAlgorithm;
import husacct.analyse.task.reconstruct.ReconstructArchitecture;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.AlgorithmParameter;
import husacct.analyse.task.reconstruct.components.HUSACCT.ComponentsAndSubSystems_HUSACCT;
import husacct.analyse.task.reconstruct.layers.Layers_HUSACCT_Algorithm_SelectedModule_SAEreCon;
import husacct.analyse.task.reconstruct.parameters.NumberFieldPanel;
import husacct.analyse.task.reconstruct.parameters.ParameterPanel;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.enums.ModuleTypes;

public class CombinedAndIterative_Layers_Components_Subsystems extends IAlgorithm{
	private ModuleDTO selectedModule;
	private IAlgorithm algorithm = null;
	private final Logger logger = Logger.getLogger(ReconstructArchitecture.class);

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
			applyAlgorithmOnChildModules(dto);
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
			if (childModules.length < 3) { 
				// c) If number of layers < 3, than: i) reverse; ii) identify C&S; iii) continue
				reverseLayers(childModules);
				algorithm = new ComponentsAndSubSystems_HUSACCT(queryService);
				algorithm.executeAlgorithm(dto, queryService);
			}			
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
	    }
	}
	
	private void applyAlgorithmOnChildModules(ReconstructArchitectureDTO dto) {
		try {
			ModuleDTO[] childModules = defineService.getModule_TheChildrenOfTheModule(dto.getSelectedModule().logicalPath); 
			for(ModuleDTO childModule : childModules){
				if (!childModule.type.equals(ModuleTypes.FACADE)) {
					ReconstructArchitectureDTO childDto = new ReconstructArchitectureDTO();
					childDto.threshold = dto.threshold;
					childDto.relationType = dto.relationType;
					childDto.granularity = dto.granularity;
					childDto.setSelectedModule(childModule);
					identifyLayersComponentsOrSubsystems(childDto);
					applyAlgorithmOnChildModules(childDto);
				}
			}
		} catch (Exception e) {
	        logger.warn(" Exception: "  + e );
		}
	}

	private void reverseLayers(ModuleDTO[] childModules) {
		for(ModuleDTO module : childModules){
			defineSarService.removeModule(module.logicalPath);
		}
	}

	
	@Override
	public ReconstructArchitectureDTO getAlgorithmThresholdSettings() {
		ReconstructArchitectureDTO reconstructArchitecture = new ReconstructArchitectureDTO();
		reconstructArchitecture.approachConstant = AnalyseReconstructConstants.Algorithm.CombinedAndIterative_HUSACCT_SelectedModule;
		reconstructArchitecture.parameterPanels = createParameterPanels();
		reconstructArchitecture.threshold = 5;
		reconstructArchitecture.relationType = AnalyseReconstructConstants.RelationTypes.allDependencies;
		reconstructArchitecture.granularity = AnalyseReconstructConstants.Granularities.PackagesWithAllClasses;

		return reconstructArchitecture;
	}
	
	private ArrayList<ParameterPanel> createParameterPanels(){
		ArrayList<ParameterPanel> parameterPanels = new ArrayList<>();
		
		ParameterPanel numberField = new NumberFieldPanel("Back Call threshold", AlgorithmParameter.Threshold, 10);
		numberField.value = 10;
		numberField.minimumValue = 0;
		numberField.maximumValue = 100;
		parameterPanels.add(numberField);
		
		return parameterPanels;
	}
}
