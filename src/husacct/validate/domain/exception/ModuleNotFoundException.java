package husacct.validate.domain.exception;

@SuppressWarnings("serial")
public class ModuleNotFoundException extends RuntimeException {
	public ModuleNotFoundException() {
		super();
	}

	public ModuleNotFoundException(String moduleType) {
		super(String.format("ModuleType %s is not supported by the validator.", moduleType));
	}

	public String getMessage() {
		return "Module type is not supported by the validator.";
	}
}