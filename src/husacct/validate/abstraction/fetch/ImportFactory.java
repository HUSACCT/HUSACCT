package husacct.validate.abstraction.fetch;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import husacct.validate.abstraction.fetch.xml.ImportSeveritiesPerTypes;
import husacct.validate.abstraction.fetch.xml.ImportSeverities;
import husacct.validate.abstraction.fetch.xml.ImportViolationsWithJDOM;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;

public class ImportFactory {

	public List<?> ImportXML(Element element) throws DatatypeConfigurationException {
		if(element.getName().equals("violations")) {
			ImportViolationsWithJDOM importViolations = new ImportViolationsWithJDOM();
			return importViolations.importViolations(element);
		} else if(element.getName().equals("severities")) {
			ImportSeverities importSeverities = new ImportSeverities();
			return	importSeverities.importSeverities(element);
		}
		return Collections.emptyList();
	}

	public List<Severity> importSeverities(Element element) {
		ImportSeverities importSeverities = new ImportSeverities();
		return	importSeverities.importSeverities(element);
	}

	public List<Violation> ImportViolations(Element element) throws DatatypeConfigurationException {
		ImportViolationsWithJDOM importViolations = new ImportViolationsWithJDOM();
		return importViolations.importViolations(element);
	}

	public HashMap<String, Severity> importSeveritiesPerRuleTypes(Element element, List<Severity> severities) {
		ImportSeveritiesPerTypes importSeveritiesPerRuleTypes = new ImportSeveritiesPerTypes();
		return importSeveritiesPerRuleTypes.importSeveritiesPerTypes(element, severities);
	}

}
