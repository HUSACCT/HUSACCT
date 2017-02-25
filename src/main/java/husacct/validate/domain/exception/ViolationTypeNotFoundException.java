package husacct.validate.domain.exception;

public class ViolationTypeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 5474507554120528282L;

	public ViolationTypeNotFoundException() {
		super();
	}

	public ViolationTypeNotFoundException(String violationTypeKey) {
		super(String.format("ViolationTypeKey: %s not found in the configuration of the validate component", violationTypeKey));
	}
}