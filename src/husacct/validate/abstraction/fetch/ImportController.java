package husacct.validate.abstraction.fetch;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;

import java.util.HashMap;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;


public class ImportController {
	
	private List<Severity> severities;
	private List<Violation> violations;
	private HashMap<String, HashMap<String, Severity>> severitiesPerTypesPerProgrammingLanguages;
	
	public ImportController(Element element) throws DatatypeConfigurationException {
		ImportFactory importFactory = new ImportFactory();
		severities = importFactory.importSeverities(element.getChild("severities"));
		violations = importFactory.ImportViolations(element.getChild("violations"), severities);
		severitiesPerTypesPerProgrammingLanguages = importFactory.importSeveritiesPerTypesPerProgrammingLanguages(element.getChild("severitiesPerTypesPerProgrammingLanguages"), severities);
	}

	public List<Severity> getSeverities() {
		return severities;
	}
	public List<Violation> getViolations() {
		return violations;
	}

	public HashMap<String, HashMap<String, Severity>> getSeveritiesPerTypesPerProgrammingLanguages() {
		return severitiesPerTypesPerProgrammingLanguages;
	}
	
}
