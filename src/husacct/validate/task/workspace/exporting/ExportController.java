package husacct.validate.task.workspace.exporting;

import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public class ExportController {

	private final ExportFactory exportFactory;

	public ExportController() {
		this.exportFactory = new ExportFactory();
	}

	public Element exportAllData(ConfigurationServiceImpl configuration) {
		Element rootValidateElement = new Element("validate");
		rootValidateElement.addContent(exportSeveritiesXML(configuration.getAllSeverities()));
		rootValidateElement.addContent(exportSeveritiesPerTypesPerProgrammingLanguagesXML(configuration.getAllSeveritiesPerTypesPerProgrammingLanguages()));
		rootValidateElement.addContent(exportActiveViolationTypesPerRuleTypes(configuration.getActiveViolationTypes()));

		return rootValidateElement;
	}

	private Element exportSeveritiesXML(List<Severity> severities) {
		return exportFactory.exportSeverities(severities);
	}

	private Element exportSeveritiesPerTypesPerProgrammingLanguagesXML(HashMap<String, HashMap<String, Severity>> allSeveritiesPerTypesPerProgrammingLanguages) {
		return exportFactory.exportSeveritiesPerTypesPerProgrammingLanguages(allSeveritiesPerTypesPerProgrammingLanguages);
	}

	private Element exportActiveViolationTypesPerRuleTypes(Map<String, List<ActiveRuleType>> activeViolationTypes) {
		return exportFactory.exportActiveViolationTypes(activeViolationTypes);
	}
}