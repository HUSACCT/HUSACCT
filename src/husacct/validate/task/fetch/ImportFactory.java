package husacct.validate.task.fetch;

import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.fetch.xml.ImportActiveViolationTypes;
import husacct.validate.task.fetch.xml.ImportSeverities;
import husacct.validate.task.fetch.xml.ImportSeveritiesPerTypesPerProgrammingLanguages;
import husacct.validate.task.fetch.xml.ImportViolations;
import husacct.validate.task.fetch.xml.ImportViolationsHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;

public class ImportFactory {
	private final ImportSeverities importSeverities;
	private final ImportViolations importViolations;
	private final ImportSeveritiesPerTypesPerProgrammingLanguages importSeveritiesPerRuleTypesPerProgrammingLanguages;
	private final ImportViolationsHistory importViolationsHistory;
	private final ImportActiveViolationTypes importActiveViolationTypes;
	
	ImportFactory(){
		this.importSeverities = new ImportSeverities();
		this.importViolations = new ImportViolations();
		this.importSeveritiesPerRuleTypesPerProgrammingLanguages = new ImportSeveritiesPerTypesPerProgrammingLanguages();
		this.importViolationsHistory = new ImportViolationsHistory();
		this.importActiveViolationTypes = new ImportActiveViolationTypes();
	}

	List<Severity> importSeverities(Element element) {
		return	importSeverities.importSeverities(element);
	}

	List<Violation> importViolations(Element element, List<Severity> severities) throws DatatypeConfigurationException {
		return importViolations.importViolations(element, severities);
	}

	HashMap<String, HashMap<String, Severity>> importSeveritiesPerTypesPerProgrammingLanguages(Element element, List<Severity> severities) {
		return importSeveritiesPerRuleTypesPerProgrammingLanguages.importSeveritiesPerTypesPerProgrammingLanguages(element, severities);
	}


	List<ViolationHistory> importViolationHistory(
			Element violationHistoryElement) {
		return importViolationsHistory.importViolationsHistory(violationHistoryElement);
	}

	Map<String, List<ActiveRuleType>> importActiveViolationTypes(Element activeViolationTypes) {
		return importActiveViolationTypes.importActiveViolationTypes(activeViolationTypes);
	}
}