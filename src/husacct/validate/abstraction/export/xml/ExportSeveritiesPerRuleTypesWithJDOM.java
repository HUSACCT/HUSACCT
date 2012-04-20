package husacct.validate.abstraction.export.xml;

import husacct.validate.domain.validation.Severity;

import java.util.HashMap;
import java.util.Map.Entry;

import org.jdom2.Element;

public class ExportSeveritiesPerRuleTypesWithJDOM {
	
	public Element exportSeveritiesPerRuleTypes(HashMap<Severity, String> severitiesPerRuleTypes) {
		Element severitiesPerRuleTypesElement = new Element("severitiesPerRuleTypes");
		for(Entry<Severity, String> severityPerRuleTypeEntry : severitiesPerRuleTypes.entrySet()) {
			Element severityPerRuleTypeElement = new Element("severityPerRuleType");
			Element valueElement = new Element("value");
			valueElement.setText("" + severityPerRuleTypeEntry.getKey().getValue());
			Element ruleTypeKeyElement = new Element("typeKey");
			ruleTypeKeyElement.setText(severityPerRuleTypeEntry.getValue());
			severityPerRuleTypeElement.addContent(valueElement);
			severityPerRuleTypeElement.addContent(ruleTypeKeyElement);
			severitiesPerRuleTypesElement.addContent(severityPerRuleTypeElement);
		}
		return severitiesPerRuleTypesElement;
	}

}
