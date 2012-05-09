package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Violation;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ViolationRepository {
	private List<Violation> violations;
	private Calendar date;

	public ViolationRepository(){
		this.violations = new ArrayList<Violation>();
		this.date = Calendar.getInstance();
	}	

	public void addViolation(List<Violation> newViolations){
		this.violations.addAll(newViolations);
	}

	public void addViolation(Violation violation){
		this.violations.add(violation);
	}

	public List<Violation> getAllViolations(){
		return violations;
	}

	public void clear(){
		this.violations = new ArrayList<Violation>();
	}
	
	public void createCurrentDate() {
		this.date = Calendar.getInstance();
	}
}