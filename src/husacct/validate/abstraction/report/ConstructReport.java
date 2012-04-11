package husacct.validate.abstraction.report;

import husacct.validate.abstraction.fetch.UnknownStorageTypeException;
import husacct.validate.domain.violation.Violation;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ConstructReport {

	public Report generateReport(String projectName, String version, List<Violation> violations, String path) throws ParserConfigurationException, SAXException, IOException, UnknownStorageTypeException {
		Report report = new Report(projectName, version, violations, path);
		return report;
	}

}
