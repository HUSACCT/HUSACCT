package husacct.validate.abstraction.fetch.xml;

import husacct.validate.abstraction.xmlutil.XMLUtils;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.iternal_tranfer_objects.ViolationsPerSeverity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ImportSeverities {
	
	public List<Severity> importSeverities(NodeList severityList) throws SAXException, IOException, ParserConfigurationException {
		List<Severity> severities = new ArrayList<Severity>();
		for (int s = 0; s < severityList.getLength(); s++) {
			Node severityNode = severityList.item(s);
			if (severityNode.getNodeType() == Node.ELEMENT_NODE) {
				Severity severity = new Severity();
				Element severityElement = (Element) severityNode;
				severity.setDefaultName(XMLUtils.getContentFromElement(severityElement, "defaultName"));
				severity.setUserName(XMLUtils.getContentFromElement(severityElement, "userName"));
				severity.setValue(Integer.parseInt(XMLUtils.getContentFromElement(severityElement, "value")));
				severity.setColor(XMLUtils.getContentFromElement(severityElement, "color"));   
				severities.add(severity);
			}
		}
		return severities;
	}
	
	public List<ViolationsPerSeverity> getSeveritiesPerViolation(List<Violation> violations, List<Severity> severities) throws SAXException, IOException, ParserConfigurationException{
		List<ViolationsPerSeverity> violationsPerSeverityList = new ArrayList<ViolationsPerSeverity>();
		for(Severity severity : severities) {
			int violationCountPerSeverity = 0;
			for(Violation violation : violations) {
				if(violation.getSeverityValue() == severity.getValue()) {
					violationCountPerSeverity++;
				}
			}
			ViolationsPerSeverity violationsPerSeverity = new ViolationsPerSeverity(violationCountPerSeverity, severity);
			violationsPerSeverityList.add(violationsPerSeverity);
		}
		return violationsPerSeverityList;
	}
	public String getSeverityNameFromValue(int value, List<Severity> severities) throws SAXException, IOException, ParserConfigurationException  {
		for(Severity severity : severities) {
			if(severity.getValue() == value) {
				return severity.getDefaultName();
			}
		}
		return "Severity cannot be found for id " + value + ", check the configuration";
	}
}
