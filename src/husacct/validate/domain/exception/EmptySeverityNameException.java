package husacct.validate.domain.exception;

public class EmptySeverityNameException extends RuntimeException {

	private static final long serialVersionUID = 8678705345064252922L;

	public EmptySeverityNameException(){
		super("Severityname is not allowed to be empty or null");
	}
}