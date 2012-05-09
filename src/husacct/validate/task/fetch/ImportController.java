package husacct.validate.task.fetch;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;

public class ImportController {

	private List<Severity> severities;
	private final ImportFactory importFactory;
	private final ConfigurationServiceImpl configuration;

	public ImportController(ConfigurationServiceImpl configuration) {
		this.configuration = configuration;
		this.importFactory = new ImportFactory();
		this.severities = new ArrayList<Severity>();
	}

	public void importWorkspace(Element element) throws DatatypeConfigurationException{
		importSeverties(element);
		importViolations(element);
		importSeveritiesPerTypesPerProgrammingLanguages(element);
		importViolationHistory(element);
		importActiveViolationTypes(element);
	}

	private void importSeverties(Element element) {
		Element severityElement = element.getChild("severities");
		this.severities = importFactory.importSeverities(severityElement);
		configuration.addSeverities(severities);
	}

	private void importViolations(Element element) throws DatatypeConfigurationException{
		Element violationElement = element.getChild("violations");
		List<Violation> violations = importFactory.importViolations(violationElement, severities);
		configuration.addViolations(violations);
	}

	private void importSeveritiesPerTypesPerProgrammingLanguages(Element element){
		Element severitiesPerTypesPerProgrammingLanguagesElement = element.getChild("severitiesPerTypesPerProgrammingLanguages");
		HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguage = importFactory.importSeveritiesPerTypesPerProgrammingLanguages(severitiesPerTypesPerProgrammingLanguagesElement, severities);
		configuration.setSeveritiesPerTypesPerProgrammingLanguages(severitiesPerTypesPerProgrammingLanguage);
	}

	private void importViolationHistory(Element element) {
		Element violationHistoryElement = element.getChild("violationHistories");
		List<ViolationHistory> violationHistory = importFactory.importViolationHistory(violationHistoryElement);
		configuration.setViolationHistory(violationHistory);
	}

	private void importActiveViolationTypes(Element element) {
		Element activeViolationTypesElement = element.getChild("activeViolationTypes");
		Map<String, List<ActiveRuleType>> activeViolationTypes = importFactory.importActiveViolationTypes(activeViolationTypesElement);
		configuration.setActiveViolationTypes(activeViolationTypes);
	}

}
