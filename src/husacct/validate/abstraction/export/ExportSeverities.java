package husacct.validate.abstraction.export;

import husacct.validate.domain.severity.Severity;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExportSeverities {

	public Document exportSeveritiesByXML(List<Severity> severities) throws ParserConfigurationException  {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document document = parser.newDocument();

		Element severitiesElement = document.createElement("severities");
		document.appendChild(severitiesElement);

		for(Severity severity : severities) {
			Element severityElement = document.createElement("severity");

			Element defaultNameElement = document.createElement("defaultName");
			defaultNameElement.setTextContent(severity.getDefaultName());
			severityElement.appendChild(defaultNameElement);

			Element usernameElement = document.createElement("username");
			usernameElement.setTextContent(severity.getUserName());
			severityElement.appendChild(usernameElement);

			Element valueElement = document.createElement("value");
			valueElement.setTextContent("" + severity.getValue());
			severityElement.appendChild(valueElement);

			Element colorElement = document.createElement("color");
			colorElement.setTextContent(severity.getColor());
			severityElement.appendChild(colorElement);

			severitiesElement.appendChild(severityElement);
		}
		return document;
	}

}
