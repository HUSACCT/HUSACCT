package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Severity;
import java.util.ArrayList;

import java.util.Collections;
import java.util.List;

public class SeverityConfigRepository {
	private List<Severity> severities;

	public SeverityConfigRepository(){
		this.severities = new ArrayList<Severity>();
	}

	public List<Severity> getAllSeverities(){
		return severities;
	}

	public void addSeverities(List<Severity> severities){
		this.severities = severities;
	}
}