package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.ViolationHistory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;

class ViolationHistoryRepository extends Observable {	
	private List<ViolationHistory> violationHistories;

	ViolationHistoryRepository() {
		this.violationHistories = new ArrayList<ViolationHistory>();
	}

	List<ViolationHistory> getViolationHistory() {
		return violationHistories;
	}

	void setViolationHistories(List<ViolationHistory> violationhistories){
		this.violationHistories = violationhistories;
	}
	
	void addViolationHistory(ViolationHistory violationHistory){
		violationHistories.add(violationHistory);
	}

	void removeViolationHistory(Calendar date) {
		ViolationHistory recordToDelete = null;
		for(ViolationHistory violationHistory : violationHistories) {
			if(violationHistory.getDate().equals(date)) {
				recordToDelete = violationHistory;
				break;
			}
		}
		violationHistories.remove(recordToDelete);
	}

	ViolationHistory getViolationHistoryByDate(Calendar date) {
		for(ViolationHistory violationHistory : violationHistories) {
			if(violationHistory.getDate().equals(date)) {
				return violationHistory;
			}
		}
		throw new NullPointerException("violationHistory not found");
	}
}