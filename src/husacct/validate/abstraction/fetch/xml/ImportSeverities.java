package husacct.validate.abstraction.fetch.xml;

import husacct.validate.domain.validation.Severity;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jdom2.Element;
public class ImportSeverities {
	
	public List<Severity> importSeverities(Element element) {
		List<Severity> severities = new ArrayList<Severity>();
		for (Element severityElement : element.getChildren()) {
			UUID id = UUID.fromString(severityElement.getChildText("id"));
			String defaultname = severityElement.getChildText("defaultName");
			String username = severityElement.getChildText("userName");
			Color color = new Color(Integer.parseInt(severityElement.getChildText("color")));
			Severity severity = new Severity(id, defaultname, username, color);
			severities.add(severity);
		}
		return severities;
	}
}
