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
		for (Element activeViolationTypeElement : activeViolationTypesElement.getChildren()) {
			List<ActiveRuleType> activeRuleTypes = new ArrayList<ActiveRuleType>();
			for (Element ruleTypeElement : activeViolationTypeElement.getChildren()) {
				String ruleTypeKey = ruleTypeElement.getAttributeValue("type");
				// In version 3.1, InheritanceConvention was introduced instead of SuperClassInheritanceConvention
				if (ruleTypeKey.equals("SuperClassInheritanceConvention")){
					ruleTypeKey = "InheritanceConvention";
				}

				List<ActiveViolationType> activeViolationTypes = new ArrayList<ActiveViolationType>();
				Element violationTypesElement = ruleTypeElement.getChild("violationTypes");
				if (violationTypesElement != null) {
					for (Element violationTypeElement : violationTypesElement.getChildren("violationType")) {
						final String violationTypeKey = violationTypeElement.getChildText("violationKey");
						final boolean enabled = Boolean.parseBoolean(violationTypeElement.getChildText("enabled"));
						ActiveViolationType activeViolationType = new ActiveViolationType(violationTypeKey, enabled);
						activeViolationTypes.add(activeViolationType);
					}
				}

				// In version 3.1, InterfaceInheritanceConvention was removed
				if (!ruleTypeKey.equals("InterfaceInheritanceConvention")){
					ActiveRuleType activeRuleType = new ActiveRuleType(ruleTypeKey, activeViolationTypes);
					activeRuleTypes.add(activeRuleType);
				}
			}
			activeRuleTypesMap.put(activeViolationTypeElement.getAttributeValue("language"), activeRuleTypes);
		}
		return activeRuleTypesMap;
	}
}
