package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.presentation.ViolationHistoryRepositoryObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;

class ViolationHistoryRepository extends Observable {	
	private List<ViolationHistory> violationHistories;
	private List<ViolationHistoryRepositoryObserver> observers = new ArrayList<ViolationHistoryRepositoryObserver>();

	void attachObserver(ViolationHistoryRepositoryObserver observer) {
		observers.add(observer);
	}

	ViolationHistoryRepository() {
		this.violationHistories = new ArrayList<ViolationHistory>();
	}

	List<ViolationHistory> getViolationHistory() {
		return violationHistories;
	}

	void addViolationHistory(ViolationHistory violationHistory){
		this.violationHistories.add(violationHistory);
		for(ViolationHistoryRepositoryObserver observer : observers) {
			observer.updateViolationHistories();
		}
	}

	void setViolationHistory(List<ViolationHistory> violationhistories){
		this.violationHistories = violationhistories;
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