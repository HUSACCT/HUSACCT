package husacct.validate.task.fetch;

import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.validation.Severity;
import husacct.validate.task.fetch.xml.ImportActiveViolationTypes;
import husacct.validate.task.fetch.xml.ImportSeverities;
import husacct.validate.task.fetch.xml.ImportSeveritiesPerTypesPerProgrammingLanguages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public class ImportFactory {
	private final ImportSeverities importSeverities;
	private final ImportSeveritiesPerTypesPerProgrammingLanguages importSeveritiesPerRuleTypesPerProgrammingLanguages;
	private final ImportActiveViolationTypes importActiveViolationTypes;
	
	ImportFactory(){
		this.importSeverities = new ImportSeverities();
		this.importSeveritiesPerRuleTypesPerProgrammingLanguages = new ImportSeveritiesPerTypesPerProgrammingLanguages();
		this.importActiveViolationTypes = new ImportActiveViolationTypes();
	}

	List<Severity> importSeverities(Element element) {
		return	importSeverities.importSeverities(element);
	}

	HashMap<String, HashMap<String, Severity>> importSeveritiesPerTypesPerProgrammingLanguages(Element element, List<Severity> severities) {
		return importSeveritiesPerRuleTypesPerProgrammingLanguages.importSeveritiesPerTypesPerProgrammingLanguages(element, severities);
	}

	Map<String, List<ActiveRuleType>> importActiveViolationTypes(Element activeViolationTypes) {
		return importActiveViolationTypes.importActiveViolationTypes(activeViolationTypes);
	}
}