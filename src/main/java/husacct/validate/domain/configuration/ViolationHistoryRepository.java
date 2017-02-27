package husacct.validate.domain.configuration;

import husacct.validate.domain.exception.ViolationHistoryNotFoundException;
import husacct.validate.domain.validation.ViolationHistory;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Observable;

class ViolationHistoryRepository extends Observable {

	private List<ViolationHistory> violationHistories;

	ViolationHistoryRepository() {
		this.violationHistories = new ArrayList<>();
	}

	List<ViolationHistory> getViolationHistory() {
		return violationHistories;
	}

	void setViolationHistories(List<ViolationHistory> violationhistories) {
		this.violationHistories = violationhistories;
	}

	void addViolationHistory(ViolationHistory violationHistory) {
		violationHistories.add(violationHistory);
		setChanged();
		notifyObservers();
	}

	void removeViolationHistory(Calendar date) {
		if (dateExistsInRepository(date)) {
			ViolationHistory recordToDelete = null;
			for (ViolationHistory violationHistory : violationHistories) {
				if (violationHistory.getDate().equals(date)) {
					recordToDelete = violationHistory;
					break;
				}
			}
			violationHistories.remove(recordToDelete);
			return;
		}
		throw new ViolationHistoryNotFoundException(date);
	}

	ViolationHistory getViolationHistoryByDate(Calendar date) {
		if (dateExistsInRepository(date)) {
			for (ViolationHistory violationHistory : violationHistories) {
				if (violationHistory.getDate().equals(date)) {
					return violationHistory;
				}
			}
		}
		throw new ViolationHistoryNotFoundException(date);
	}

	private boolean dateExistsInRepository(Calendar date) {
		for (ViolationHistory violationHistory : violationHistories) {
			if (violationHistory.getDate().equals(date)) {
				return true;
			}
		}
		return false;
	}
}