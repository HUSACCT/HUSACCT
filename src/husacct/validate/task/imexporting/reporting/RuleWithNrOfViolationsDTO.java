package husacct.validate.task.imexporting.reporting;

import java.util.List;

public class RuleWithNrOfViolationsDTO {
	private int id; 
	private String logicalModuleFrom;
	private String ruleType;
	private String logicalModuleTo;
	private int nrOfViolations;
	private List<NrOfViolationsPerFromClassToClassDTO> violatingFromToClasses;

	public RuleWithNrOfViolationsDTO(int id, String logicalModuleFrom, String ruleType, String logicalModuleTo, int nrOfViolations, List<NrOfViolationsPerFromClassToClassDTO> violatingFromToClasses) {
		this.id = id;
		this.logicalModuleFrom = logicalModuleFrom;
		this.ruleType = ruleType;
		this.logicalModuleTo = logicalModuleTo;
		this.nrOfViolations = nrOfViolations;
		this.violatingFromToClasses = violatingFromToClasses;
	}

	public int getId() {
		return id;
	}
	
	public String getLogicalModuleFrom() {
		return logicalModuleFrom;
	}
	
	public String getRuleType() {
		return ruleType;
	}
	
	public String getLogicalModuleTo() {
		return logicalModuleTo;
	}
	
	public int getNrOfViolations() {
		return nrOfViolations;
	}

	public List<NrOfViolationsPerFromClassToClassDTO> getViolatingFromToClasses() {
		return violatingFromToClasses;
	}
	
}
