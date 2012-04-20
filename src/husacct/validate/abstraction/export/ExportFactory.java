package husacct.validate.abstraction.export;

import husacct.validate.abstraction.export.xml.ExportSeverities;
import husacct.validate.abstraction.export.xml.ExportViolations;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;

public class ExportFactory {
	
	public Document exportXML(String type, List<?> listToStore) throws ParserConfigurationException {
		if(type.equals("violations")) {
			return new ExportViolations().exportViolations((List<Violation>) listToStore);
		} else if(type.equals("severities")) {
			return new ExportSeverities().exportSeverities((List<Severity>) listToStore);
		}
		throw new RuntimeException("An unknown type of list was given in the ExportFactory.");
	}

}
