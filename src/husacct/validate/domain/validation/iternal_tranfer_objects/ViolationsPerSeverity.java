package husacct.validate.domain.validation.iternal_tranfer_objects;

import husacct.validate.domain.validation.Severity;


public class ViolationsPerSeverity {
	private int amount;
	private Severity severity;

	public ViolationsPerSeverity(int amount, Severity severity) {
		this.amount = amount;
		this.severity = severity;
	}

	public int getAmount() {
		return amount;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public Severity getSeverity() {
		return severity;
	}
	public void setSeverity(Severity severity) {
		this.severity = severity;
	}
}