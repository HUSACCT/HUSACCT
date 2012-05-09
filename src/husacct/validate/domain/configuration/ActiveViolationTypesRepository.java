package husacct.validate.domain.configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ActiveViolationTypesRepository {
	
	private Map<String, List<ActiveRuleType>> activeViolationTypes;
	
	public ActiveViolationTypesRepository() {
		activeViolationTypes = new HashMap<String, List<ActiveRuleType>>();
	}

	public Map<String, List<ActiveRuleType>> getActiveViolationTypes() {
		return activeViolationTypes;
	}

	public void setActiveViolationTypes(Map<String, List<ActiveRuleType>> activeViolationTypes) {
		this.activeViolationTypes = activeViolationTypes;
	}



}
