package husacct.validate.abstraction.export;

import husacct.validate.abstraction.export.xml.ExportSeverities;
import husacct.validate.abstraction.export.xml.ExportSeveritiesPerTypes;
import husacct.validate.abstraction.export.xml.ExportViolations;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;

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
	
	public Element exportSeveritiesPerTypes(HashMap<String, Severity> severitiesPerTypes) {
		return new ExportSeveritiesPerTypes().exportSeveritiesPerTypes(severitiesPerTypes);
	}

}
