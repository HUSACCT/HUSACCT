package husacct.validate.task.report;

public class RuleWithNrOfViolationsDTO {
	private int id; 
	private String logicalModuleFrom;
	private String ruleType;
	private String logicalModuleTo;
	private int nrOfViolations;

	public RuleWithNrOfViolationsDTO(int id, String logicalModuleFrom, String ruleType, String logicalModuleTo, int nrOfViolations) {
		this.id = id;
		this.logicalModuleFrom = logicalModuleFrom;
		this.ruleType = ruleType;
		this.logicalModuleTo = logicalModuleTo;
		this.nrOfViolations = nrOfViolations;
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
	
}
