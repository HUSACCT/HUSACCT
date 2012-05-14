package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.presentation.ViolationHistoryRepositoryObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;

public class ViolationHistoryRepository extends Observable {	
	private List<ViolationHistory> violationHistories;
	private List<ViolationHistoryRepositoryObserver> observers = new ArrayList<ViolationHistoryRepositoryObserver>();
	
	public void attachObserver(ViolationHistoryRepositoryObserver observer) {
		observers.add(observer);
	}
	
	public ViolationHistoryRepository() {
		this.violationHistories = new ArrayList<ViolationHistory>();
	}

	public List<ViolationHistory> getViolationHistory() {
		return violationHistories;
	}

	public void addViolationHistory(ViolationHistory violationHistory){
		this.violationHistories.add(violationHistory);
		for(ViolationHistoryRepositoryObserver observer : observers) {
			observer.updateViolationHistories();
		}
	}

	public void setViolationHistory(List<ViolationHistory> violationhistories){
		this.violationHistories = violationhistories;
	}

	public void removeViolationHistory(Calendar date) {
		ViolationHistory recordToDelete = null;
		for(ViolationHistory violationHistory : violationHistories) {
			if(violationHistory.getDate().equals(date)) {
				recordToDelete = violationHistory;
				break;
			}
		}
		violationHistories.remove(recordToDelete);
	}

	public ViolationHistory getViolationHistoryByDate(Calendar date) {
		for(ViolationHistory violationHistory : violationHistories) {
			if(violationHistory.getDate().equals(date)) {
				return violationHistory;
			}
		}
		throw new NullPointerException("violationHistory not found");
	}
}