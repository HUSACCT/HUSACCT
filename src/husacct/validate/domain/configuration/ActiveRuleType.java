package husacct.validate.domain.configuration;

import java.util.ArrayList;
import java.util.List;

public class ActiveRuleType {

	private final String ruleType;
	private List<ActiveViolationType> violationTypes;
	
	public ActiveRuleType(String ruleType){
		this.ruleType = ruleType;
	}

	public String getRuleType() {
		return ruleType;
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