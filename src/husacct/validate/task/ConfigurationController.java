package husacct.validate.task;

import husacct.validate.domain.validation.Severity;
import java.awt.Color;
import java.util.List;
import java.util.UUID;

public class ConfigurationController {
	private TaskServiceImpl ts;
	private List<Severity> severities;

	public ConfigurationController(TaskServiceImpl ts) {
		this.ts = ts;
	}

//	public Severity findSeverity(String severityName, List<Severity> severities){
//		for(Severity severity : severities){
//			if(severity.toString().equals(severityName)){
//				return severity;
//			}
//		}
//		return null;
//	}

	public Severity SererityChecker(String id) {
		severities = ts.getAllSeverities();

		for(Severity severity : severities){
			if(id.equals(severity.getId().toString())){
				return severity;
			}
		}

		return new Severity("","",Color.red);

	}
}