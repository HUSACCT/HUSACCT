package husacct.validate.domain.exception;

public class KeyNotFoundException extends RuntimeException {
	private static final long serialVersionUID = -3211747097336194318L;
	
	public KeyNotFoundException(String key){
		super(key);
	}
}