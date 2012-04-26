package husacct.validate.domain.configuration;

import husacct.validate.domain.validation.Violation;

import java.util.ArrayList;
import java.util.List;

public class ViolationRepository {
	private List<Violation> violations;

	public ViolationRepository(){
		violations = new ArrayList<Violation>();
	}	

	public void addViolation(List<Violation> newViolations){
		this.violations.addAll(newViolations);
	}

	public void addViolation(Violation violation){
		violations.add(violation);
	}

	public List<Violation> getAllViolations(){
		return violations;
	}

	public void clear(){
		violations = new ArrayList<Violation>();
	}
}