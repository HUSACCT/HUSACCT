package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Violation;

import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

class ViolationRepository {

	private List<Violation> violations;
	private Calendar date;

	public ViolationRepository() {
		this.violations = new ArrayList<Violation>();
		this.date = Calendar.getInstance();
	}

	void addViolation(List<Violation> newViolations) {
		this.violations = newViolations;
	}

	void addViolation(Violation violation) {
		this.violations.add(violation);
	}

	SimpleEntry<Calendar, List<Violation>> getAllViolations() {
		return new SimpleEntry<Calendar, List<Violation>>(date, violations);
	}

	void clear() {
		this.violations = new ArrayList<Violation>();
	}
}