package husacct.validate.abstraction.fetch.xml;

import husacct.validate.domain.validation.Severity;

import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

public class ImportSeveritiesPerTypes {

	public HashMap<String, Severity> importSeveritiesPerTypes(Element element, List<Severity> severities) {
		HashMap<String, Severity> severitiesPerTypes = new HashMap<String, Severity>();
		for(Element severityPerTypesElement : element.getChildren()) {
			for(Severity severity : severities) {
				if(severity.getValue() == Integer.parseInt(severityPerTypesElement.getChildText("value"))) {
					severitiesPerTypes.put(severityPerTypesElement.getChildText("typeKey"), severity);
				}
			}	
			
		}
		return severitiesPerTypes;
	}
}