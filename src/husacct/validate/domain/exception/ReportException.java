package husacct.validate.domain.exception;

public class ReportException extends RuntimeException {
	private static final long serialVersionUID = 7193037512975053282L;

	public ReportException(){
		super();
	}

	public ReportException(String message){
		super(message);		
	}
	
	public ReportException(String message, Throwable cause){
		super(message, cause);		
	}
}