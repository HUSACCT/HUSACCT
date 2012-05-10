package husacct.validate.task.fetch.xml;

import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ActiveViolationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public class ImportActiveViolationTypes {

	public Map<String, List<ActiveRuleType>> importActiveViolationTypes(Element activeViolationTypesElement) {
		Map<String, List<ActiveRuleType>> activeRuleTypesMap = new HashMap<String, List<ActiveRuleType>>();
		for(Element activeViolationTypeElement : activeViolationTypesElement.getChildren()) {
			List<ActiveRuleType> activeRuleTypes = new ArrayList<ActiveRuleType>();
			for(Element ruleTypeElement : activeViolationTypeElement.getChildren()) {
				ActiveRuleType activeRuleType = new ActiveRuleType();
				activeRuleType.setRuleType(ruleTypeElement.getAttributeValue("type"));
				List<ActiveViolationType> activeViolationTypes = new ArrayList<ActiveViolationType>();
				for(Element violationTypeElement : ruleTypeElement.getChildren()) {
					ActiveViolationType activeViolationType = new ActiveViolationType();
					activeViolationType.setEnabled(Boolean.parseBoolean(violationTypeElement.getChildText("enabled")));
					activeViolationType.setType(violationTypeElement.getChildText("type"));
				}
				activeRuleType.setViolationTypes(activeViolationTypes);
			}
			activeRuleTypesMap.put(activeViolationTypeElement.getAttributeValue("language"), activeRuleTypes);
		}
		return activeRuleTypesMap;
	}

}
