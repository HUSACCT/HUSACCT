package husacct.analyse.serviceinterface.dto;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ModuleDTO;

public class ReconstructArchitectureDTO extends AbstractDTO{
	private ModuleDTO selectedModule = null; 
	private String approach = "";
	private int threshold = 0;
	private String relationType = ""; 

    public ReconstructArchitectureDTO() {
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

	public String toString() {
        String s = "";
        s += "\nselectedModule: " + selectedModule.logicalPath;
        s += "\napproach: " + approach;
        s += "\nthreshold: " + threshold;
        s += "\n\n";
        return s;
    }
}
