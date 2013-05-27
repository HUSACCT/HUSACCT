package husacct.validate.domain.exception;

import husacct.validate.domain.validation.module.ModuleTypes;

@SuppressWarnings("serial")
public class ModuleNotFoundException extends RuntimeException {
	public ModuleNotFoundException() {
		super();
	}

	public ModuleNotFoundException(ModuleTypes moduleType) {
		super(String.format("ModuleType %s is not supported by the validator.", moduleType.toString()));
	}

	public String getMessage() {
		return "Module type is not supported by the validator.";
	}
}