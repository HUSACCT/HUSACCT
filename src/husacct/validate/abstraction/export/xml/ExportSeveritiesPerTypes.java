package husacct.validate.abstraction.export.xml;

import husacct.validate.domain.validation.Severity;

import java.util.HashMap;
import java.util.Map.Entry;

import org.jdom2.Element;

public class ExportSeveritiesPerTypes {
	
	public Element exportSeveritiesPerTypes(HashMap<String, Severity> severitiesPerRuleTypes) {
		Element severitiesPerRuleTypesElement = new Element("severitiesPerTypes");
		for(Entry<String , Severity> severityPerRuleTypeEntry : severitiesPerRuleTypes.entrySet()) {
			Element severityPerRuleTypeElement = new Element("severityPerType");
			Element valueElement = new Element("value");
			valueElement.setText("" + severityPerRuleTypeEntry.getValue().getValue());
			Element ruleTypeKeyElement = new Element("typeKey");
			ruleTypeKeyElement.setText(severityPerRuleTypeEntry.getKey());
			severityPerRuleTypeElement.addContent(valueElement);
			severityPerRuleTypeElement.addContent(ruleTypeKeyElement);
			severitiesPerRuleTypesElement.addContent(severityPerRuleTypeElement);
		}
		return severitiesPerRuleTypesElement;
	}

}
