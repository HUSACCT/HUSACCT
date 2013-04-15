package husacct.validate.domain.exception;

public class LanguageNotFoundException extends Exception {

	private static final long serialVersionUID = 780096305107666489L;

	public LanguageNotFoundException() {
		super();
	}

	public LanguageNotFoundException(String language) {
		super(String.format("Programminglanguage %s not configured programmatically in validate component", language));
	}

	public String getMessage() {
		return "Programminglanguage not configured programmatically in validate component";
	}
}