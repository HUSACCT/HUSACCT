package husacct.validate.abstraction.fetch;

import husacct.validate.abstraction.fetch.xml.ImportSeverities;
import husacct.validate.abstraction.fetch.xml.ImportSeveritiesPerTypesPerProgrammingLanguages;
import husacct.validate.abstraction.fetch.xml.ImportViolations;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;

import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;

public class ImportFactory {

	public List<Severity> importSeverities(Element element) {
		ImportSeverities importSeverities = new ImportSeverities();
		return	importSeverities.importSeverities(element);
	}

	public List<Violation> ImportViolations(Element element, List<Severity> severities) throws DatatypeConfigurationException {
		ImportViolations importViolations = new ImportViolations();
		return importViolations.importViolations(element, severities);
	}

	public HashMap<String, HashMap<String, Severity>> importSeveritiesPerTypesPerProgrammingLanguages(Element element, List<Severity> severities) {
		ImportSeveritiesPerTypesPerProgrammingLanguages importSeveritiesPerRuleTypesPerProgrammingLanguages = new ImportSeveritiesPerTypesPerProgrammingLanguages();
		return importSeveritiesPerRuleTypesPerProgrammingLanguages.importSeveritiesPerTypesPerProgrammingLanguages(element, severities);
	}

}
