package husacct.validate.task.fetch.xml;

import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ImportViolationsHistory {

	public LinkedHashMap<Calendar, List<Violation>> importViolationsHistory(Element violationHistoryElement) {
		LinkedHashMap<Calendar, List<Violation>> violationHistory = new LinkedHashMap<Calendar, List<Violation>>();
		for(Element violationListElements : violationHistoryElement.getChildren()) {
			List<Violation> violations = new ArrayList<Violation>();
			try {
				violationHistory.put(DatatypeFactory.newInstance().newXMLGregorianCalendar(violationListElements.getAttribute("date").getValue()).toGregorianCalendar(), violations);
				for(Element violationElement : violationListElements.getChildren()) {
					Violation violation = new Violation();
					violations.add(violation);
					violation.setLinenumber(Integer.parseInt(violationElement.getChildText("lineNumber")));
					
					Element severityElement = violationElement.getChild("severity");
					UUID id = UUID.fromString(severityElement.getChildText("id"));
					String defaultname = severityElement.getChildText("defaultName");
					String username = severityElement.getChildText("userName");
					Color color = new Color(Integer.parseInt(severityElement.getChildText("color")));
					Severity severity = new Severity(id, defaultname, username, color);
					violation.setSeverity(severity);
					
					violation.setRuletypeKey(violationElement.getChildText("ruletypeKey"));
					violation.setViolationtypeKey(violationElement.getChildText("violationtypeKey"));
					violation.setClassPathFrom(violationElement.getChildText("classPathFrom"));
					violation.setClassPathTo(violationElement.getChildText("classPathTo"));
					violation.setLogicalModules(getLogicalModules(violationElement.getChild("logicalModules")));
					violation.setMessage(getMessage(violationElement.getChild("message")));
					violation.setIndirect(Boolean.parseBoolean(violationElement.getChildText("isIndirect")));
					violation.setOccured(DatatypeFactory.newInstance().newXMLGregorianCalendar(violationElement.getChildText("occured")).toGregorianCalendar());
					violations.add(violation);
				}
			} catch (DatatypeConfigurationException e) {
				Logger.getLogger(ImportViolationsHistory.class).log(Level.FATAL, "Error creating calendar in importviolationhistory");
			}
		}
		return violationHistory;
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
