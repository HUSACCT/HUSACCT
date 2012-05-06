package husacct.validate.task.export;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.task.export.xml.ExportSeverities;
import husacct.validate.task.export.xml.ExportSeveritiesPerTypesPerProgrammingLanguages;
import husacct.validate.task.export.xml.ExportViolations;

import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

public class ExportFactory {

	public Element exportViolations(List<Violation> violations) {
		return new ExportViolations().exportViolations((List<Violation>) violations);
	}
	
	public Element exportSeverities(List<Severity> severities)  {
		return new ExportSeverities().exportSeverities(severities);
	}
	

	public Element exportSeveritiesPerTypesPerProgrammingLanguages(
			HashMap<String, HashMap<String, Severity>> allSeveritiesPerTypesPerProgrammingLanguages) {
		return new ExportSeveritiesPerTypesPerProgrammingLanguages().exportSeveritiesPerTypesPerProgrammingLanguages(allSeveritiesPerTypesPerProgrammingLanguages);
	}

}
