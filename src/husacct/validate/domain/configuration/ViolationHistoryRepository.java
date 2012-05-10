package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.ViolationHistory;

import java.util.ArrayList;
import java.util.List;

public class ViolationHistoryRepository {	
	private List<ViolationHistory> violationHistories;

	public ViolationHistoryRepository() {
		this.violationHistories = new ArrayList<ViolationHistory>();
	}

	public List<ViolationHistory> getViolationHistory() {
		return violationHistories;
	}

	public void addViolationHistory(ViolationHistory violationHistory){
		this.violationHistories.add(violationHistory);
	}

	public void setViolationHistory(List<ViolationHistory> violationhistories){
		this.violationHistories = violationhistories;
	}
}