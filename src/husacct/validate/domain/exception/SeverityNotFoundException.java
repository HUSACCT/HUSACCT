package husacct.validate.domain.exception;

public class SeverityNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -3801007757829586776L;

	public SeverityNotFoundException() {
		super();
	}

	public SeverityNotFoundException(String message) {
		super(message);
	}
}
