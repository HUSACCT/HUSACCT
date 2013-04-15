package husacct.validate.domain.exception;

@SuppressWarnings("serial")
public class ModuleNotFoundException extends RuntimeException {
	public ModuleNotFoundException() {
		super();
	}

	public ModuleNotFoundException(String type) {
		super(String.format("ModuleType %s is not supported by the validator.", type));
	}

	public String getMessage() {
		return "Module type is not supported by the validator.";
	}
}