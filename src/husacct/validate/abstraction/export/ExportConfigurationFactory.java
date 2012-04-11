package husacct.validate.abstraction.export;

import husacct.validate.domain.severity.Severity;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class ExportConfigurationFactory {

	public void exportSeverities(String storageType, List<Severity> severities) throws ParserConfigurationException, TransformerException, IOException {
		if(storageType.equals("xml")) {
			ExportSeverities exportSeverities = new ExportSeverities();
			exportSeverities.exportSeveritiesByXML(severities);
		}
	}
}
