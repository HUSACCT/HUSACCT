package husacct.validate.task.imexporting.reporting;

public class NrOfViolationsPerFromClassToClassDTO {
	private String fromClass;
	private String toClass;
	private int nrOfViolations;

	public NrOfViolationsPerFromClassToClassDTO(String fromClass, String toClass, int nrOfViolations) {
		this.fromClass = fromClass;
		this.toClass = toClass;
		this.nrOfViolations = nrOfViolations;
	}

	public String getFromClass() {
		return fromClass;
	}
	
	public String getToClass() {
		return toClass;
	}
	
	public int getNrOfViolations() {
		return nrOfViolations;
	}
	
}
