package husacct.validate.abstraction.fetch;

import husacct.validate.domain.severity.Severity;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ImportConfigurationFactory {

	private ImportConfiguration importConfiguration = new ImportConfiguration();

	public List<Severity> importSeverities(String storageType) throws ParserConfigurationException, SAXException, IOException, UnknownStorageTypeException {
		if(storageType.equals("xml")) {
			return importConfiguration.getSeveritiesByXML();
		}
		throw new UnknownStorageTypeException("The storage type " + storageType + " does not exist yet or has not been implemented yet.");
	}
	

}
