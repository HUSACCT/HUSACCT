package husacct.validate.domain.exception;

import java.util.UUID;

public class DuplicateSeverityException extends RuntimeException {
	private static final long serialVersionUID = 2156064307240062289L;
	
	public DuplicateSeverityException(String severityName, UUID id){
		super(String.format("Severityname %s or UUID %s already exists", severityName, id.toString()));
	}
}