package husacct.analyse.task.analyser;

public class ParserException extends RuntimeException {

	private static final long serialVersionUID = 7193037512975053282L;

	public ParserException() {
		super();
	}

	public ParserException(String message) {
		super(message);
	}

	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}
}