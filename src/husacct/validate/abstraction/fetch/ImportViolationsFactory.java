package husacct.validate.abstraction.fetch;

import husacct.validate.domain.violation.Violation;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ImportViolationsFactory {
	
	private ImportViolations importViolations = new ImportViolations();
	
	public List<Violation> importViolations(String storageType) throws ParserConfigurationException, SAXException, IOException, UnknownStorageTypeException {
		if(storageType.equals("xml")) {
			importViolations.getViolationsByXML();
		}
		throw new UnknownStorageTypeException("The storage type " + storageType + " does not exist yet or has not been implemented yet.");
	}
}
