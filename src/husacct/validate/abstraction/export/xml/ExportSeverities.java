package husacct.validate.abstraction.export.xml;

import husacct.validate.abstraction.xmlutil.XMLUtils;
import husacct.validate.domain.validation.Severity;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExportSeverities {

	public Document exportSeverities(List<Severity> severities) throws ParserConfigurationException  {
		
		Document document = XMLUtils.createNewXMLDocument();
		Element severitiesElement = XMLUtils.createRootElementWithoutContent(document, "severities");

		for(Severity severity : severities) {
			Element severityElement = XMLUtils.createElementWithoutContent(document, "severity", severitiesElement);
			
			XMLUtils.createElementWithContent(document, "defaultName", severity.getDefaultName(), severityElement);
			XMLUtils.createElementWithContent(document, "userName", severity.getUserName(), severityElement);
			XMLUtils.createElementWithContent(document, "value", "" + severity.getValue(), severityElement);
			XMLUtils.createElementWithContent(document, "color", severity.getColor(), severityElement);
		}
		return document;
	}

}
