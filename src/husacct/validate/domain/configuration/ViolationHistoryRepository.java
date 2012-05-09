package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Violation;

import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ViolationHistoryRepository {
	
	private Map<Calendar, List<Violation>> violationsHistory;
	
	public ViolationHistoryRepository() {
		violationsHistory = new LinkedHashMap<Calendar, List<Violation>>();
	}

	public Map<Calendar, List<Violation>> getViolationsHistory() {
		return violationsHistory;
	}

	public void setViolationsHistory(Map<Calendar, List<Violation>> violationsHistory) {
		this.violationsHistory = violationsHistory;
	}
	
}
