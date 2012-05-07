package husacct.validate.task.export;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;

import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

public class ExportController {

	private final ExportFactory exportFactory;

	public ExportController() {
		this.exportFactory = new ExportFactory();
	}

	public Element exportAllData(ConfigurationServiceImpl configuration){
		Element rootValidateElement = new Element("validate");
		rootValidateElement.addContent(exportViolationsXML(configuration.getAllViolations()));
		rootValidateElement.addContent(exportSeveritiesXML(configuration.getAllSeverities()));
		rootValidateElement.addContent(exportSeveritiesPerTypesPerProgrammingLanguagesXML(configuration.getAllSeveritiesPerTypesPerProgrammingLanguages()));
		return rootValidateElement;
	}

	private Element exportViolationsXML(List<Violation> violations) {
		return exportFactory.exportViolations(violations);
	}

	private Element exportSeveritiesXML(List<Severity> severities) {
		return exportFactory.exportSeverities(severities);
	}

	private Element exportSeveritiesPerTypesPerProgrammingLanguagesXML(HashMap<String, HashMap<String, Severity>> allSeveritiesPerTypesPerProgrammingLanguages) {
		return exportFactory.exportSeveritiesPerTypesPerProgrammingLanguages(allSeveritiesPerTypesPerProgrammingLanguages);
	}
}