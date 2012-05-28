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

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ImportViolationsHistory {
	private Logger logger = Logger.getLogger(ImportViolationsHistory.class);

	public List<ViolationHistory> importViolationsHistory(Element violationHistoriesElement) {


		List<ViolationHistory> violationHistories = new ArrayList<ViolationHistory>();
		for(Element violationHistoryElement : violationHistoriesElement.getChildren("violationHistory")) {
			List<Severity> severities = new ArrayList<Severity>();
			List<Violation> violations = new ArrayList<Violation>();
			//severities

			for(Element severityElement : violationHistoryElement.getChild("severities").getChildren()) {
				String stringUUID = severityElement.getChildText("id");
				if(isValidUUID(stringUUID)){
					Severity severity = new Severity(UUID.fromString(severityElement.getChildText("id")), severityElement.getChildText("defaultName"), severityElement.getChildText("userName"), new Color(Integer.parseInt(severityElement.getChildText("color"))));
					severities.add(severity);
				}
				else{
					logger.error(String.format("%s is not a valid UUID severity will be ignored", stringUUID));
				}
			}

			//date
			final String validationDate = violationHistoryElement.getAttributeValue("date");
			Calendar date = getCalendar(validationDate);

			//description 
			final String description = violationHistoryElement.getChildText("description");

			//violations
			for(Element violationElement : violationHistoryElement.getChild("violations").getChildren()) {
				Violation violation = new Violation();					
				violation.setLinenumber(Integer.parseInt(violationElement.getChildText("lineNumber")));
				violation.setRuletypeKey(violationElement.getChildText("ruletypeKey"));
				violation.setViolationtypeKey(violationElement.getChildText("violationtypeKey"));
				violation.setClassPathFrom(violationElement.getChildText("classPathFrom"));
				violation.setClassPathTo(violationElement.getChildText("classPathTo"));
				violation.setLogicalModules(getLogicalModules(violationElement.getChild("logicalModules")));
				violation.setMessage(getMessage(violationElement.getChild("message")));
				violation.setIndirect(Boolean.parseBoolean(violationElement.getChildText("isIndirect")));
				
				final String stringCalendar = violationElement.getChildText("occured");
				violation.setOccured(getCalendar(stringCalendar));

				//search the appropiate severity of the violation by the uuid.
				final String stringUUID = violationElement.getChildText("severityId");
                boolean found = false;
                for(Severity severity : severities) {
                    
                    if(isValidUUID(stringUUID)){
                        UUID id = UUID.fromString(stringUUID);
                        if(id.equals(severity.getId())) {
                            violation.setSeverity(severity);
                            violations.add(violation);
                            found = true;
                            break;
                        }                            
                    } else{
                        logger.error(String.format("%s is not a valid severity UUID, violation will not be added", stringUUID));
                        break;
                    }
                    
                } 
                if(!found) {
                    logger.error("Severity for the violation " + violation.getLinenumber() + "was not found (UUID: "+ stringUUID);
                } 
			
			}				
			violationHistories.add(new ViolationHistory(violations, severities, date, description));
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
		Message message = new Message(logicalModules,ruleKey, violationTypeKeysList,regex, exceptionMessages);
		return message;
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

	private boolean isValidUUID(String stringUUID){
		try{
			UUID.fromString(stringUUID);
		}catch(IllegalArgumentException e){
			return false;
		}
		return true;
	}
}