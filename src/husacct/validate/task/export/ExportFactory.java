package husacct.validate.task.export;

import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.export.xml.ExportActiveViolationTypes;
import husacct.validate.task.export.xml.ExportSeverities;
import husacct.validate.task.export.xml.ExportSeveritiesPerTypesPerProgrammingLanguages;
import husacct.validate.task.export.xml.ExportViolations;
import husacct.validate.task.export.xml.ExportViolationsHistory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public class ExportFactory {
	private final ExportViolations exportViolations;
	private final ExportSeverities exportSeverities;
	private final ExportSeveritiesPerTypesPerProgrammingLanguages exportSeveritiesPerTypesPerProgrammingLanguages;
	private final ExportViolationsHistory exportViolationsHistory;
	private final ExportActiveViolationTypes exportActiveViolationTypes;

	public ExportFactory(){
		this.exportViolations = new ExportViolations();
		this.exportSeverities = new ExportSeverities();
		this.exportSeveritiesPerTypesPerProgrammingLanguages = new ExportSeveritiesPerTypesPerProgrammingLanguages();
		this.exportViolationsHistory = new ExportViolationsHistory();
		this.exportActiveViolationTypes = new ExportActiveViolationTypes();
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

	public Element exportViolationHistory(List<ViolationHistory> violationHistories) {
		return exportViolationsHistory.exportViolationsHistory(violationHistories);
	}

	public Element exportActiveViolationTypes(
			Map<String, List<ActiveRuleType>> activeViolationTypes) {
		return exportActiveViolationTypes.exportActiveViolationTypes(activeViolationTypes);
	}
}