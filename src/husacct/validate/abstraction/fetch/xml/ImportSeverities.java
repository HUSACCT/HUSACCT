package husacct.validate.abstraction.fetch.xml;

import husacct.validate.domain.validation.Severity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.jdom2.Element;
public class ImportSeverities {
	
	public List<Severity> importSeverities(Element element) {
		List<Severity> severities = new ArrayList<Severity>();
		for (Element severityElement : element.getChildren()) {
			Severity severity = new Severity();
			severity.setDefaultName(severityElement.getChildText("defaultName"));
			severity.setUserName(severityElement.getChildText("userName"));
			severity.setValue(Integer.parseInt(severityElement.getChildText("value")));
			severity.setColor(severityElement.getChildText("color"));
			severity.setId(UUID.fromString(severityElement.getChildText("id")));
			severities.add(severity);
		}
		return severities;
	}
}
