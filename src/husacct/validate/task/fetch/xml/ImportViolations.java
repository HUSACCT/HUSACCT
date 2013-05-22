package husacct.validate.task.fetch.xml;

import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;

import org.jdom2.Element;

public class ImportViolations extends XmlImportUtils {
	public List<Violation> importViolations(Element violationsElement, List<Severity> severities) throws DatatypeConfigurationException {
		List<Violation> violations = new ArrayList<Violation>();
		for (Element violationElement : violationsElement.getChildren()) {
			Severity violationSeverity = null;

			final String severityId = violationElement.getChildText("severityId");
			for (Severity severity : severities) {
				if (severityId.equals(severity.getId().toString())) {
					violationSeverity = severity;
					break;
				}
			}

			final int lineNumber = Integer.parseInt(violationElement.getChildText("lineNumber"));
			final String ruleTypeKey = violationElement.getChildText("ruletypeKey");
			final String violationTypeKey = violationElement.getChildText("violationtypeKey");
			final String classPathFrom = violationElement.getChildText("classPathFrom");
			final String classPathTo = violationElement.getChildText("classPathTo");
			final LogicalModules logicalModules = getLogicalModules(violationElement.getChild("logicalModules"));
			final Message message = getMessage(violationElement.getChild("message"));
			final boolean isIndirect = Boolean.parseBoolean(violationElement.getChildText("isIndirect"));

			final String stringCalendar = violationElement.getChildText("occured");
			final Calendar occured = getCalendar(stringCalendar);

			Violation violation = new Violation()
				.setOccured(occured)
				.setLineNumber(lineNumber)
				.setSeverity(violationSeverity)
				.setRuletypeKey(ruleTypeKey)
				.setViolationtypeKey(violationTypeKey)
				.setClassPathFrom(classPathFrom)
				.setClassPathTo(classPathTo)
				.setInDirect(isIndirect)
				.setMessage(message)
				.setLogicalModules(logicalModules);

			violations.add(violation);
		}
		return violations;
	}
}