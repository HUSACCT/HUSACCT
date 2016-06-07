package husacct.analyse.task.reconstruct.externals;

import java.util.ArrayList;

import org.apache.log4j.Logger;

import husacct.analyse.domain.IModelQueryService;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants.AlgorithmParameter;
import husacct.analyse.task.reconstruct.parameters.NumberFieldPanel;
import husacct.analyse.task.reconstruct.parameters.ParameterPanel;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ReconstructArchitectureDTO;
import husacct.common.dto.SoftwareUnitDTO;

public class ExternalSystemAlgorithm extends AlgorithmExternal{
	
	private ArrayList<SoftwareUnitDTO> xLibrariesMainPackages = new ArrayList<SoftwareUnitDTO>();
	private final Logger logger = Logger.getLogger(ExternalSystemAlgorithm.class);
	int threshold;
	String relationType;

	public ExternalSystemAlgorithm (IModelQueryService queryService) {
		super(queryService);
	}
	
	@Override
	public void executeAlgorithm(ReconstructArchitectureDTO dto, IModelQueryService queryService) throws Exception {
		this.threshold = dto.getThreshold();
		this.relationType = dto.getRelationType();
		identifyExternalSystems();
	}
	
	private void identifyExternalSystems() {
		ArrayList<SoftwareUnitDTO> emptySoftwareUnitsArgument = new ArrayList<SoftwareUnitDTO>();
		try {
			createModule_andAddItToReverseList("ExternalSystems", "**", "ExternalLibrary", 0, emptySoftwareUnitsArgument);
			// Create a module for each childUnit of xLibrariesRootPackage
			int nrOfExternalLibraries = 0;
			for (SoftwareUnitDTO mainUnit : queryService.getChildUnitsOfSoftwareUnit(xLibrariesRootPackage)) {
				xLibrariesMainPackages.add(mainUnit);
				ArrayList<SoftwareUnitDTO> softwareUnitsArgument = new ArrayList<SoftwareUnitDTO>();
				softwareUnitsArgument.add(mainUnit);
				ModuleDTO newModule = createModule_andAddItToReverseList(mainUnit.name, "ExternalSystems", "ExternalLibrary", 0, softwareUnitsArgument);
				if (!newModule.logicalPath.equals("")) {
					nrOfExternalLibraries++;
				}
			}
			logger.info(" Number of added ExternalLibraries: " + nrOfExternalLibraries);
		} catch (Exception e) {
	        logger.error(" Exception: "  + e );
	    }
	}

	@Override
	public ReconstructArchitectureDTO getAlgorithmThresholdSettings() {
		ReconstructArchitectureDTO reconstructArchitecture = new ReconstructArchitectureDTO();
		reconstructArchitecture.approachConstant = AnalyseReconstructConstants.Algorithm.Externals_Recognition;
		reconstructArchitecture.parameterPanels = createParameterPanels();
		reconstructArchitecture.threshold = 10;
		return reconstructArchitecture;
	}
	
	private ArrayList<ParameterPanel> createParameterPanels(){
		ArrayList<ParameterPanel> parameterPanels = new ArrayList<>();
		
		ParameterPanel numberField = new NumberFieldPanel("Threshold", AlgorithmParameter.Threshold, 10);
		numberField.defaultValue = 10;
		numberField.minimumValue = 0;
		numberField.maximumValue = 100;
		parameterPanels.add(numberField);
		
		return parameterPanels;
	}

}
