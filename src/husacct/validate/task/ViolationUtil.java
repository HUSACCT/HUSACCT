package husacct.validate.task;

import husacct.validate.abstraction.fetch.ImportConfigurationFactory;
import husacct.validate.abstraction.fetch.UnknownStorageTypeException;
import husacct.validate.domain.severity.Severity;
import husacct.validate.domain.violation.Violation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ViolationUtil {
	//Naar abstraction
	public List<ViolationsPerSeverity> getSeveritiesPerViolation(List<Violation> violations) throws ParserConfigurationException, SAXException, IOException, UnknownStorageTypeException {
		List<ViolationsPerSeverity> violationsPerSeverityList = new ArrayList<ViolationsPerSeverity>();
		
		ImportConfigurationFactory importConfigurationFactory = new ImportConfigurationFactory();
		List<Severity> severities = importConfigurationFactory.importSeverities("xml");
		for(Severity severity : severities) {
			int violationCountPerSeverity = 0;
			for(Violation violation : violations) {
				if(violation.getSeverityValue() == severity.getValue()) {
					violationCountPerSeverity++;
				}
			}
			ViolationsPerSeverity violationsPerSeverity = new ViolationsPerSeverity(violationCountPerSeverity, severity);
			violationsPerSeverityList.add(violationsPerSeverity);
		}
		return violationsPerSeverityList;
	}

	//Naar abstraction
	public String getSeverityNameFromValue(int value) throws ParserConfigurationException, SAXException, IOException, UnknownStorageTypeException {
		ImportConfigurationFactory importConfigurationFactory = new ImportConfigurationFactory();
		List<Severity> severities = importConfigurationFactory.importSeverities("xml");
		for(Severity severity : severities) {
			if(severity.getValue() == value) {
				return severity.getDefaultName();
			}
		}
		return "Severity cannot be found for id " + value + ", check the configuration";
	}
}