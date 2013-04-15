package husacct.validate.task.export.xml;

import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ActiveViolationType;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class ExportActiveViolationTypes extends XmlExportUtils {

	public Element exportActiveViolationTypes(Map<String, List<ActiveRuleType>> activeViolationTypes) {
		Element activeViolationTypesElement = new Element("activeViolationTypes");
		for (Entry<String, List<ActiveRuleType>> activeViolationType : activeViolationTypes.entrySet()) {
			activeViolationTypesElement.addContent(createActiveViolationTypeElement(activeViolationType));
		}
		return activeViolationTypesElement;
	}

	private Element createActiveViolationTypeElement(Entry<String, List<ActiveRuleType>> activeViolationType) {
		Element activeViolationTypeElement = new Element("activeViolationType");
		activeViolationTypeElement.setAttribute(new Attribute("language", activeViolationType.getKey()));
		for (ActiveRuleType activeRuleType : activeViolationType.getValue()) {
			activeViolationTypeElement.addContent(createActiveRuleTypeElement(activeRuleType));
		}
		return activeViolationTypeElement;
	}

	private Element createActiveRuleTypeElement(ActiveRuleType activeRuleType) {
		Element ruleTypeElement = new Element("ruleType");
		ruleTypeElement.setAttribute(new Attribute("type", activeRuleType.getRuleType()));
		Element violationTypesElement = new Element("violationTypes");
		ruleTypeElement.addContent(violationTypesElement);
		for (ActiveViolationType violationType : activeRuleType.getViolationTypes()) {
			Element violationTypeElement = new Element("violationType");
			violationTypeElement.addContent(createElementWithContent("violationKey", violationType.getType()));
			violationTypeElement.addContent(createElementWithContent("enabled", "" + violationType.isEnabled()));
			violationTypesElement.addContent(violationTypeElement);
		}
		return ruleTypeElement;
	}
}
