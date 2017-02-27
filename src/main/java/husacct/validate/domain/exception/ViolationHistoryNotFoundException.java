package husacct.validate.domain.exception;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViolationHistoryNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6735471839503937206L;
	private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");

	;

	public ViolationHistoryNotFoundException(Calendar date) {
		super(String.format("%s not found in the history of saved violations", dateFormat.format(date.getTime())));
	}
}