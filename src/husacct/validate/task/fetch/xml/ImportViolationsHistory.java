package husacct.validate.task.fetch.xml;

import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.DatatypeFactory;

import org.jdom2.Element;

public class ImportViolationsHistory {

	public List<ViolationHistory> importViolationsHistory(Element violationHistoriesElement) {

		List<ViolationHistory> violationHistories = new ArrayList<ViolationHistory>();
		try {
			for(Element violationHistoryElement : violationHistoriesElement.getChildren()) {
				List<Severity> severities = new ArrayList<Severity>();
				List<Violation> violations = new ArrayList<Violation>();
				//severities

				for(Element severityElement : violationHistoryElement.getChild("severities").getChildren()) {
					Severity severity = new Severity(UUID.fromString(severityElement.getChildText("id")), severityElement.getChildText("defaultName"), severityElement.getChildText("userName"), new Color(Integer.parseInt(severityElement.getChildText("color"))));
					severities.add(severity);
				}

				//date
				Calendar date = DatatypeFactory.newInstance().newXMLGregorianCalendar(violationHistoryElement.getAttributeValue("date")).toGregorianCalendar();

				//description 
				final String description = violationHistoryElement.getChildText("description");

				//violations
				for(Element violationElement : violationHistoryElement.getChild("violations").getChildren()) {
					Violation violation = new Violation();
					violations.add(violation);
					violation.setLinenumber(Integer.parseInt(violationElement.getChildText("lineNumber")));

					//search the appropiate severity of the violation by the uuid.
					for(Severity severity : severities) {
						UUID id = UUID.fromString(violationElement.getChildText("severityId"));
						if(id.equals(severity.getId())) {
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
					violation.setOccured(DatatypeFactory.newInstance().newXMLGregorianCalendar(violationElement.getChildText("occured")).toGregorianCalendar());
				}				
				violationHistories.add(new ViolationHistory(violations, severities, date, description));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return violationHistories;
	}

	private LogicalModules getLogicalModules(Element logicalModulesElement) {
		Element logicalModuleFromElement = logicalModulesElement.getChild("logicalModuleFrom");
		Element logicalModuleToElement = logicalModulesElement.getChild("logicalModuleTo");

		LogicalModule logicalModuleFrom = new LogicalModule(logicalModuleFromElement.getChildText("logicalModulePath"), logicalModuleFromElement.getChildText("logicalModuleType"));
		LogicalModule logicalModuleTo = new LogicalModule(logicalModuleToElement.getChildText("logicalModulePath"), logicalModuleToElement.getChildText("logicalModuleType"));
		LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);
		return logicalModules;
	}

	private Message getMessage(Element messageElement) {
		Element logicalModulesElement = messageElement.getChild("logicalModules");
		LogicalModules logicalModules = getLogicalModules(logicalModulesElement);
		String ruleKey = messageElement.getChildText("ruleKey");
		Element violationTypeKeys = messageElement.getChild("violationTypeKeys");
		List<String> violationTypeKeysList = new ArrayList<String>();
		for(Element violationTypeKey : violationTypeKeys.getChildren()) {
			violationTypeKeysList.add(violationTypeKey.getText());
		}
		List<Message> exceptionMessages = new ArrayList<Message>();
		Element exceptionMessagesElement = messageElement.getChild("exceptionMessages");
		if(exceptionMessagesElement != null) {
			for(Element exceptionMessageElement : exceptionMessagesElement.getChildren()) {
				exceptionMessages.add(getMessage(exceptionMessageElement));
			}
		}
		Message message = new Message(logicalModules,ruleKey, violationTypeKeysList, exceptionMessages);
		return message;
	}
}