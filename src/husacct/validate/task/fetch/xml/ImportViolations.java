package husacct.validate.task.fetch.xml;

import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ImportViolations {
	private Logger logger = Logger.getLogger(ImportViolations.class);
	
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

	private Calendar getCalendar(String stringCalendar){
		Calendar calendar = Calendar.getInstance();
		try{
			calendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(stringCalendar).toGregorianCalendar();
		}
		catch(IllegalArgumentException e){
			logger.error(String.format("%s is not a valid datetime, switching back to current datetime", stringCalendar));
		} catch (DatatypeConfigurationException e) {
			logger.error(e.getMessage());
		}
		return calendar;
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
		String regex = messageElement.getChildText("regex");
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
		Message message = new Message(logicalModules, ruleKey, violationTypeKeysList, regex, exceptionMessages);
		return message;
	}

}
