package husacct.validate.abstraction.export;

import husacct.validate.abstraction.fetch.UnknownStorageTypeException;
import husacct.validate.domain.violation.Violation;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class ExportViolationsFactory {

	public void exportViolations(String type, List<Violation> violations) throws ParserConfigurationException, UnknownStorageTypeException, TransformerException, IOException {
		if(type.equals("xml")) {
			ExportViolations exportViolations = new ExportViolations();
			exportViolations.exportViolationsByXML(violations);
		} else {
			throw new UnknownStorageTypeException("Storage type " + type + " is unknown or not implemented");
		}
	}

}
