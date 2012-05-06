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
	private final ExportViolations exportViolations;
	private final ExportSeverities exportSeverities;
	private final ExportSeveritiesPerTypesPerProgrammingLanguages exportSeveritiesPerTypesPerProgrammingLanguages;

	public ExportFactory(){
		this.exportViolations = new ExportViolations();
		this.exportSeverities = new ExportSeverities();
		this.exportSeveritiesPerTypesPerProgrammingLanguages = new ExportSeveritiesPerTypesPerProgrammingLanguages();
	}

	public Element exportViolations(List<Violation> violations) {
		return exportViolations.exportViolations((List<Violation>) violations);
	}

	public Element exportSeverities(List<Severity> severities)  {
		return exportSeverities.exportSeverities(severities);
	}

	public Element exportSeveritiesPerTypesPerProgrammingLanguages(HashMap<String, HashMap<String, Severity>> allSeveritiesPerTypesPerProgrammingLanguages) {
		return exportSeveritiesPerTypesPerProgrammingLanguages.exportSeveritiesPerTypesPerProgrammingLanguages(allSeveritiesPerTypesPerProgrammingLanguages);
	}
}