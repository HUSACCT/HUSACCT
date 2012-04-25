package husacct.validate.domain.validation.violationtype;

public interface IViolationType {
	public String toString();
	public int getDefaultSeverity();
	public String getCategory();
}