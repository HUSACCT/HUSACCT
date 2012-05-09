package husacct.validate.task.fetch;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.fetch.xml.ImportSeverities;
import husacct.validate.task.fetch.xml.ImportSeveritiesPerTypesPerProgrammingLanguages;
import husacct.validate.task.fetch.xml.ImportViolationsHistory;
import husacct.validate.task.fetch.xml.ImportViolations;

import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;

public class ImportFactory {
	private final ImportSeverities importSeverities;
	private final ImportViolations importViolations;
	private final ImportSeveritiesPerTypesPerProgrammingLanguages importSeveritiesPerRuleTypesPerProgrammingLanguages;
	private final ImportViolationsHistory importViolationsHistory;
	
	public ImportFactory(){
		this.importSeverities = new ImportSeverities();
		this.importViolations = new ImportViolations();
		this.importSeveritiesPerRuleTypesPerProgrammingLanguages = new ImportSeveritiesPerTypesPerProgrammingLanguages();
		this.importViolationsHistory = new ImportViolationsHistory();
	}

	public List<Severity> importSeverities(Element element) {
		return	importSeverities.importSeverities(element);
	}

	public List<Violation> importViolations(Element element, List<Severity> severities) throws DatatypeConfigurationException {
		return importViolations.importViolations(element, severities);
	}

	public HashMap<String, HashMap<String, Severity>> importSeveritiesPerTypesPerProgrammingLanguages(Element element, List<Severity> severities) {
		return importSeveritiesPerRuleTypesPerProgrammingLanguages.importSeveritiesPerTypesPerProgrammingLanguages(element, severities);
	}


	public List<ViolationHistory> importViolationHistory(
			Element violationHistoryElement) {
		return importViolationsHistory.importViolationsHistory(violationHistoryElement);
	}
}