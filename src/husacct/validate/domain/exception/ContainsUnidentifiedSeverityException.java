package husacct.validate.domain.exception;

public class ContainsUnidentifiedSeverityException extends RuntimeException {

	private static final long serialVersionUID = 4534177843372770597L;

	public ContainsUnidentifiedSeverityException(){
		super("The new severities that will be applied contains the severity \"Unidentified\" which is a reserved keyword");
	}
}