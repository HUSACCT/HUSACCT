package husacct.validate.task.report;
//TODO decide whether the exception  is runtime or not
@SuppressWarnings("serial")
public class UnknownStorageTypeException extends Exception {
	
	public UnknownStorageTypeException(String message) {
		super(message);
	}
}
