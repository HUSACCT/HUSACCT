package husacct.control.task.configuration;

@SuppressWarnings("serial")
public class NonExistingSettingException extends Exception {

	public NonExistingSettingException() {
		super();
	}
	
	public NonExistingSettingException(String message) {
		super(message);
	}
	
	public NonExistingSettingException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public NonExistingSettingException(Throwable cause) {
		super(cause);
	}
	
}
