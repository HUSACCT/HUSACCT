package husacct.validate.abstraction.fetch.xml;

import husacct.validate.domain.validation.Severity;

import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

public class ImportSeveritiesPerTypesPerProgrammingLanguages {

	public HashMap<String, HashMap<String, Severity>> importSeveritiesPerTypesPerProgrammingLanguages(Element element, List<Severity> severities) {
		HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages = new HashMap<String, HashMap<String,Severity>>();
		for(Element severityPerTypePerProgrammingLanguageElement : element.getChildren()) {
			String language = severityPerTypePerProgrammingLanguageElement.getAttributeValue("language");
			HashMap<String, Severity> severitiesPerTypes = new HashMap<String, Severity>();
			for(Element severityPerTypeElement : severityPerTypePerProgrammingLanguageElement.getChildren("severityPerType")) {
				for(Severity severity : severities) {
					if(severity.getId().toString().trim().equals(severityPerTypeElement.getChildText("severityId").trim())) {
						severitiesPerTypes.put(severityPerTypeElement.getChildText("typeKey"), severity);
					}
				}
			}
			severitiesPerTypesPerProgrammingLanguages.put(language, severitiesPerTypes);
		}
		return severitiesPerTypesPerProgrammingLanguages;
	}
}