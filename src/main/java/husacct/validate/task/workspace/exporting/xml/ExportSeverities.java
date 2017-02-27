package husacct.validate.task.workspace.exporting.xml;

import husacct.validate.domain.validation.Severity;

import java.util.List;

import org.jdom2.Element;

public class ExportSeverities {

	public Element exportSeverities(List<Severity> severities) {
		Element severitiesElement = new Element("severities");
		for (Severity severity : severities) {
			Element severityElement = new Element("severity");
			createElementWithContent("severityKey", severity.getSeverityKey(), severityElement);
			createElementWithContent("id", "" + severity.getId().toString(), severityElement);
			createElementWithContent("color", "" + severity.getColor().getRGB(), severityElement);
			severitiesElement.addContent(severityElement);
		}
		return severitiesElement;
	}

	private void createElementWithContent(String name, String content, Element destination) {
		Element element = new Element(name);
		element.setText(content);
		destination.addContent(element);
	}
}
