package husacct.validate.task.export.xml;

import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ActiveViolationType;
import husacct.validate.task.XMLUtils;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class ExportActiveViolationTypes {

	public Element exportActiveViolationTypes(Map<String, List<ActiveRuleType>> activeViolationTypes) {
		Element activeViolationTypesElement = new Element("activeViolationTypes");
		for(Entry<String, List<ActiveRuleType>> activeViolationType : activeViolationTypes.entrySet()) {
			Element activeViolationTypeElement = XMLUtils.createElementWithoutContent("activeViolationType", activeViolationTypesElement);
			activeViolationTypeElement.setAttribute(new Attribute("language", activeViolationType.getKey()));
			for(ActiveRuleType activeRuleType : activeViolationType.getValue()) {
				Element ruleTypeElement = XMLUtils.createElementWithoutContent("ruleType", activeViolationTypeElement);
				ruleTypeElement.setAttribute(new Attribute("type", activeRuleType.getRuleType()));
				Element violationTypesElement = XMLUtils.createElementWithoutContent("violationTypes", ruleTypeElement);
				for(ActiveViolationType violationType : activeRuleType.getViolationTypes()) {
					Element violationTypeElement = XMLUtils.createElementWithoutContent("violationType", violationTypesElement);
					XMLUtils.createElementWithContent("violationKey", violationType.getType(), violationTypeElement);
					XMLUtils.createElementWithContent("enabled", "" +  violationType.isEnabled(), violationTypeElement);
				}
			}
		}
		return activeViolationTypesElement;
	}

}
