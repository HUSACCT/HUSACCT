package husacct.validate.task.fetch;

import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Severity;

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
		importSeverties(element.getChild("severities"));
		
		importSeveritiesPerTypesPerProgrammingLanguages(element.getChild("severitiesPerTypesPerProgrammingLanguages"));
		importActiveViolationTypes(element.getChild("activeViolationTypes"));
		
	}

	private void importSeverties(Element element) {
		this.severities = importFactory.importSeverities(element);
		configuration.setSeverities(severities);
	}

	private void importSeveritiesPerTypesPerProgrammingLanguages(Element element){
		HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguage = importFactory.importSeveritiesPerTypesPerProgrammingLanguages(element, severities);
		configuration.setSeveritiesPerTypesPerProgrammingLanguages(severitiesPerTypesPerProgrammingLanguage);
	}

	private void importActiveViolationTypes(Element element) {
		Map<String, List<ActiveRuleType>> activeViolationTypes = importFactory.importActiveViolationTypes(element);
		configuration.setActiveViolationTypes(activeViolationTypes);
	}
}