package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.ViolationHistory;

import java.util.ArrayList;
import java.util.List;

public class ViolationHistoryRepository {
	
	private List<ViolationHistory> violationHistory;
	
	public ViolationHistoryRepository() {
		violationHistory = new ArrayList<ViolationHistory>();
	}

	public List<ViolationHistory> getViolationHistory() {
		return violationHistory;
	}

	public void setViolationHistory(List<ViolationHistory> violationHistory) {
		this.violationHistory = violationHistory;
	}
	

	
}
