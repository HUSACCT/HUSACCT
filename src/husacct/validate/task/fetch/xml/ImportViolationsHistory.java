package husacct.validate.task.fetch.xml;

import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ImportViolationsHistory extends XmlImportUtils {

	private Logger logger = Logger.getLogger(ImportViolationsHistory.class);

	public List<ViolationHistory> importViolationsHistory(Element violationHistoriesElement) {
		List<ViolationHistory> violationHistories = new ArrayList<ViolationHistory>();
		for (Element violationHistoryElement : violationHistoriesElement.getChildren("violationHistory")) {
			List<Severity> severities = new ArrayList<Severity>();
			List<Violation> violations = new ArrayList<Violation>();
			
			// severities
			for (Element severityElement : violationHistoryElement.getChild("severities").getChildren()) {
				String stringUUID = severityElement.getChildText("id");
				if (isValidUUID(stringUUID)) {
					Severity severity = new Severity(UUID.fromString(severityElement.getChildText("id")), severityElement.getChildText("severityKey"), new Color(Integer.parseInt(severityElement.getChildText("color"))));
					severities.add(severity);
				} else {
					logger.error(String.format("%s is not a valid UUID severity will be ignored", stringUUID));
				}
			}

			// date
			final String validationDateString = violationHistoryElement.getAttributeValue("date");
			Calendar validationDate = getCalendar(validationDateString);

			// description
			final String description = violationHistoryElement.getChildText("description");

			// violations
			for (Element violationElement : violationHistoryElement.getChild("violations").getChildren()) {
				final int lineNumber = Integer.parseInt(violationElement.getChildText("lineNumber"));
				final String ruleTypeKey = violationElement.getChildText("ruletypeKey");
				final String violationTypeKey = violationElement.getChildText("violationtypeKey");
				final String classPathFrom = violationElement.getChildText("classPathFrom");
				final String classPathTo = violationElement.getChildText("classPathTo");
				final LogicalModules logicalModules = getLogicalModules(violationElement.getChild("logicalModules"));
				final Message message = getMessage(violationElement.getChild("message"));
				final boolean isIndirect = Boolean.parseBoolean(violationElement.getChildText("isIndirect"));
				final String stringCalendar = violationElement.getChildText("occured");
				final Calendar date = getCalendar(stringCalendar);

				// search the appropiate severity of the violation by the uuid.
				final String stringUUID = violationElement.getChildText("severityId");
				boolean found = false;
				for (Severity severity : severities) {
					if (isValidUUID(stringUUID)) {
						UUID id = UUID.fromString(stringUUID);
						if (id.equals(severity.getId())) {
							Violation violation = new Violation();
							violation.setOccured(date)
								.setLineNumber(lineNumber)
								.setSeverity(severity.clone())
								.setRuletypeKey(ruleTypeKey)
								.setViolationtypeKey(violationTypeKey)
								.setClassPathFrom(classPathFrom)
								.setClassPathTo(classPathTo)
								.setInDirect(isIndirect)
								.setMessage(message)
								.setLogicalModules(logicalModules);
							violations.add(violation);
							found = true;
							break;
						}
					} else {
						logger.error(String.format("%s is not a valid severity UUID, violation will not be added", stringUUID));
						break;
					}

				}
				if (!found) {
					logger.error("Severity for the violation was not found (UUID: " + stringUUID);
				}

			}
			violationHistories.add(new ViolationHistory(violations, severities, validationDate, description));
		}
		return violationHistories;
	}

	private boolean isValidUUID(String stringUUID) {
		try {
			UUID.fromString(stringUUID);
		} catch (IllegalArgumentException e) {
			return false;
		}
		return true;
	}
}