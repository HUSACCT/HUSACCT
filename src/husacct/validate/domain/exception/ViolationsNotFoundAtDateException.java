package husacct.validate.domain.exception;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViolationsNotFoundAtDateException extends RuntimeException {
	
	private static final long serialVersionUID = 8593004510500308998L;

	public ViolationsNotFoundAtDateException(Calendar date) {
		super("the violations were not found at the given date: " + new SimpleDateFormat("dd-MM-yyyy kk:mm:ss").format(date.getTime()));
	}

}
