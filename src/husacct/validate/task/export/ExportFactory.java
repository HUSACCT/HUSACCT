package husacct.validate.task.export;

import husacct.validate.domain.configuration.ActiveRuleType;
import husacct.validate.domain.validation.Severity;
import husacct.validate.task.export.xml.ExportActiveViolationTypes;
import husacct.validate.task.export.xml.ExportSeverities;
import husacct.validate.task.export.xml.ExportSeveritiesPerTypesPerProgrammingLanguages;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jdom2.Element;

public class ExportFactory {
	private final ExportSeverities exportSeverities;
	private final ExportSeveritiesPerTypesPerProgrammingLanguages exportSeveritiesPerTypesPerProgrammingLanguages;
	private final ExportActiveViolationTypes exportActiveViolationTypes;

	public ExportFactory(){
		this.exportSeverities = new ExportSeverities();
		this.exportSeveritiesPerTypesPerProgrammingLanguages = new ExportSeveritiesPerTypesPerProgrammingLanguages();
		this.exportActiveViolationTypes = new ExportActiveViolationTypes();
	}

	public Element exportSeverities(List<Severity> severities)  {
		return exportSeverities.exportSeverities(severities);
	}

	public Element exportSeveritiesPerTypesPerProgrammingLanguages(HashMap<String, HashMap<String, Severity>> allSeveritiesPerTypesPerProgrammingLanguages) {
		return exportSeveritiesPerTypesPerProgrammingLanguages.exportSeveritiesPerTypesPerProgrammingLanguages(allSeveritiesPerTypesPerProgrammingLanguages);
	}

	public Element exportActiveViolationTypes(
			Map<String, List<ActiveRuleType>> activeViolationTypes) {
		return exportActiveViolationTypes.exportActiveViolationTypes(activeViolationTypes);
	}
}