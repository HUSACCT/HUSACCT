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
				final String ruleTypeKey = ruleTypeElement.getAttributeValue("type");
				ActiveRuleType activeRuleType = new ActiveRuleType(ruleTypeKey);
				activeRuleTypes.add(activeRuleType);
				List<ActiveViolationType> activeViolationTypes = new ArrayList<ActiveViolationType>();
				for(Element violationTypeElement : ruleTypeElement.getChildren()) {
					final String violationTypeKey = violationTypeElement.getChildText("type");
					final boolean enabled = Boolean.parseBoolean(violationTypeElement.getChildText("enabled"));
					ActiveViolationType activeViolationType = new ActiveViolationType(violationTypeKey, enabled);
					activeViolationTypes.add(activeViolationType);
				}
				activeRuleType.setViolationTypes(activeViolationTypes);
			}
			activeRuleTypesMap.put(activeViolationTypeElement.getAttributeValue("language"), activeRuleTypes);
		}
		return activeRuleTypesMap;
	}

}
