package husacct.validate.task.export;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;

import java.util.HashMap;
import java.util.List;

import org.jdom2.Element;

public class ExportController {
	
	private ExportFactory exportFactory;
	
	public ExportController() {
		exportFactory = new ExportFactory();
	}
	
	public Element exportViolationsXML(List<Violation> violations) {
		return exportFactory.exportViolations(violations);
	}
	
	public Element exportSeveritiesXML(List<Severity> severities) {
		return exportFactory.exportSeverities(severities);
	}

	public Element exportSeveritiesPerTypesXML(
			HashMap<String, HashMap<String, Severity>> allSeveritiesPerTypesPerProgrammingLanguages) {
		return exportFactory.exportSeveritiesPerTypesPerProgrammingLanguages(allSeveritiesPerTypesPerProgrammingLanguages);
	}

}
