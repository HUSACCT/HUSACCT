package husacct.validate.domain.configuration;

import java.util.ArrayList;
import java.util.List;

public class ActiveRuleType {

	private String ruleType;
	private List<ActiveViolationType> violationTypes;
	
	public String getRuleType() {
		return ruleType;
	}
	public void setRuleType(String ruleType) {
		this.ruleType = ruleType;
	}
	public List<ActiveViolationType> getViolationTypes() {
		if(violationTypes == null) {
			violationTypes = new ArrayList<ActiveViolationType>();
		}
		return violationTypes;
	}
	public void setViolationTypes(List<ActiveViolationType> violationTypes) {
		this.violationTypes = violationTypes;
	}
	
	
	
}
