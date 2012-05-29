package husacct.validate.task.fetch.xml;

import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;

import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;

public class ImportViolations extends XmlImportUtils {
	
	public List<Violation> importViolations(Element violationsElement, List<Severity> severities) throws DatatypeConfigurationException {
		List<Violation> violations = new ArrayList<Violation>();
		for(Element violationElement : violationsElement.getChildren()) {
			Violation violation = new Violation();
			violation.setLinenumber(Integer.parseInt(violationElement.getChildText("lineNumber")));
			String id = violationElement.getChildText("severityId");
			for(Severity severity : severities) {
				if(id.equals(severity.getId().toString())) {
					violation.setSeverity(severity);
					break;
				}
			}
			violation.setRuletypeKey(violationElement.getChildText("ruletypeKey"));
			violation.setViolationtypeKey(violationElement.getChildText("violationtypeKey"));
			violation.setClassPathFrom(violationElement.getChildText("classPathFrom"));
			violation.setClassPathTo(violationElement.getChildText("classPathTo"));
			violation.setLogicalModules(getLogicalModules(violationElement.getChild("logicalModules")));
			violation.setMessage(getMessage(violationElement.getChild("message")));
			violation.setIndirect(Boolean.parseBoolean(violationElement.getChildText("isIndirect")));
			
			final String stringCalendar = violationElement.getChildText("occured");
			violation.setOccured(getCalendar(stringCalendar));
			
			violations.add(violation);
		}
		return violations;
	}
}