package husacct.validate.domain.exception;

public class CloneNotSupportedException extends RuntimeException {
	private static final long serialVersionUID = -8306883828643570836L;

	public CloneNotSupportedException(Throwable t) {
		super(t);
	}
}