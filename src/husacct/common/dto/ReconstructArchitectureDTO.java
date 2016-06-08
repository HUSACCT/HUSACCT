package husacct.common.dto;

import java.util.ArrayList;

import husacct.ServiceProvider;
import husacct.analyse.task.reconstruct.AnalyseReconstructConstants;
import husacct.analyse.task.reconstruct.parameters.ReconstructArchitectureParameterDTO;
import husacct.common.locale.ILocaleService;

//Owner: Analyse

public class ReconstructArchitectureDTO extends AbstractDTO{
	private ModuleDTO selectedModule = null; 
	private String approach = "";
	public int threshold = 0;
	public String relationType = ""; 
	private String name = "";
	public String approachConstant = "";
	public ArrayList<ReconstructArchitectureParameterDTO> parameterDTOs;
	public String granularity;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

    public ReconstructArchitectureDTO() {
    	setAbsoluteDefaults();
    }
    
    public ModuleDTO getSelectedModule() {
		return selectedModule;
	}

	public void setSelectedModule(ModuleDTO selectedModule) {
		this.selectedModule = selectedModule;
	}

	public String getApproach() {
		return approach;
	}

	public void setApproach(String approach) {
		this.approach = approach;
	}

	public int getThreshold() {
		return threshold;
	}

	public void setThreshold(int threshold) {
		this.threshold = threshold;
	}

	public String getRelationType() {
		return relationType;
	}

	public void setRelationType(String relationType) {
		this.relationType = relationType;
	}

	public String getName() {
		return name;
	}

	public void setName(String algorithmTestName) {
		this.name = algorithmTestName;
	}
	
	public String getTranslation(){
		return localeService.getTranslatedString(approachConstant);
	}
	
	private void setAbsoluteDefaults(){
		parameterDTOs = new ArrayList<>();
		threshold = 10;
		granularity = AnalyseReconstructConstants.Granularities.Classes;
		relationType = AnalyseReconstructConstants.RelationTypes.allDependencies;
	}
	
	public String toString() {
        String s = "";
        s += "\nselectedModule: " + selectedModule.logicalPath;
        s += "\napproach: " + approach;
        s += "\nthreshold: " + threshold;
        s += "\nrelationType: " + relationType;
        s += "\nname: " + name;
        s += "\n\n";
        return s;
    }

}
