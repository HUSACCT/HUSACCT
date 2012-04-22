package husacct.validate.abstraction.export.xml;

import husacct.validate.domain.validation.Severity;

import java.util.HashMap;
import java.util.Map.Entry;

import org.jdom2.Attribute;
import org.jdom2.Element;

public class ExportSeveritiesPerTypesPerProgrammingLanguages {

	public Element exportSeveritiesPerTypesPerProgrammingLanguages(
			HashMap<String, HashMap<String, Severity>> allSeveritiesPerTypesPerProgrammingLanguages) {
		Element severitiesPerTypesPerProgrammingLanguagesElement = new Element("severitiesPerTypesPerProgrammingLanguages");
		for(Entry<String, HashMap<String, Severity>> programminglanguageEntry : allSeveritiesPerTypesPerProgrammingLanguages.entrySet()) {
			Element severityPerTypePerProgrammingLanguageElement = createElementWithoutContent("severityPerTypePerProgrammingLanguage", severitiesPerTypesPerProgrammingLanguagesElement);
			severityPerTypePerProgrammingLanguageElement.setAttribute(new Attribute("language", programminglanguageEntry.getKey()));
			for(Entry<String, Severity> severityPerType : programminglanguageEntry.getValue().entrySet()) {
				Element severityPerTypeElement = createElementWithoutContent("severityPerType", severityPerTypePerProgrammingLanguageElement);
				createElementWithContent("typeKey", severityPerType.getKey(), severityPerTypeElement);
				createElementWithContent("severityId", severityPerType.getValue().getId().toString(), severityPerTypeElement);
			}
		}
		return severitiesPerTypesPerProgrammingLanguagesElement;
	}

	private void createElementWithContent(String name, String content, Element destination) {
		Element element = new Element(name);
		element.addContent(content);
		destination.addContent(element);
	}
	private Element createElementWithoutContent(String name, Element destination) {
		Element element = new Element(name);
		destination.addContent(element);
		return element;
	}
}


