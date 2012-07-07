package husacct.validate.domain.exception;

public class SeverityChangedException extends RuntimeException {

	private static final long serialVersionUID = -2506272933358156212L;
	
	public SeverityChangedException(String severityKey){
		super(String.format("Key of severity: %s not found in the list of default severities", severityKey));
	}
}