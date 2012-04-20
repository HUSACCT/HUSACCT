package husacct.validate.abstraction.fetch.xml;

import husacct.validate.domain.validation.Severity;

import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

public class ImportSeveritiesPerRuleTypesWithJDOM {

	public HashMap<String, Severity> importSeveritiesPerRuleTypes(Element element, List<Severity> severities) {
		HashMap<String, Severity> severitiesPerRuleTypes = new HashMap<String, Severity>();
		for(Element severityPerRuleTypeElement : element.getChildren()) {
			for(Severity severity : severities) {
				if(severity.getValue() == Integer.parseInt(severityPerRuleTypeElement.getChildText("value"))) {
					severitiesPerRuleTypes.put(severityPerRuleTypeElement.getChildText("typeKey"), severity);
				}
			}			
		}
		return severitiesPerRuleTypes;
	}
}