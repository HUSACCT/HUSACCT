package husacct.validate.abstraction.export.xml;

import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jdom2.Element;

public class ExportViolations {

	public Element exportViolations(List<Violation> violations) {
		Element violationsElement = new Element("violations");
		for(Violation violation : violations) {
			Element violationElement = new Element("violation");

			createElementWithContent("lineNumber", "" + violation.getLinenumber(), violationElement);
			createElementWithContent("severityId", "" + violation.getSeverity().toString(), violationElement);
			createElementWithContent("ruletypeKey", violation.getRuletypeKey(), violationElement);
			createElementWithContent("violationtypeKey",violation.getViolationtypeKey(), violationElement);
			createElementWithContent("classPathFrom",violation.getClassPathFrom(), violationElement);
			createElementWithContent("classPathTo",violation.getClassPathTo(), violationElement);

			createLogicalModulesElement(violation.getLogicalModules(), violationElement);
			addMessage(violationElement, violation.getMessage());
			createElementWithContent("isIndirect",""+violation.isIndirect(), violationElement);
			try {
				createElementWithContent("occured", DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violation.getOccured()).toXMLFormat(), violationElement);
			} catch (DatatypeConfigurationException e) {
				Logger.getLogger(ExportViolations.class.getName()).log(Level.ERROR, "There was a error creating a new date in ExportViolations", e);
			}
			violationsElement.addContent(violationElement);
		}
		return violationsElement;
	}

	private void createElementWithContent(String name, String content, Element destination) {
		Element element = new Element(name);
		element.addContent(content);
		destination.addContent(element);
	}
	private Element createElementWithoutContent(String name, Element destination) {
		Element element = new Element(name);
		destination.addContent(element);
		return element;
	}

	private void createLogicalModulesElement(LogicalModules logicalModules, Element destinationElement) {
		Element logicalModulesElement = createElementWithoutContent("logicalModules", destinationElement);
		Element logicalModuleFrom = createElementWithoutContent("logicalModuleFrom", logicalModulesElement);
		Element logicalModuleTo = createElementWithoutContent("logicalModuleTo", logicalModulesElement);

		createElementWithContent("logicalModulePath", logicalModules.getLogicalModuleFrom().getLogicalModulePath(), logicalModuleFrom);
		createElementWithContent("logicalModuleType", logicalModules.getLogicalModuleFrom().getLogicalModuleType(), logicalModuleFrom);
		createElementWithContent("logicalModulePath", logicalModules.getLogicalModuleTo().getLogicalModulePath(), logicalModuleTo);
		createElementWithContent("logicalModuleType", logicalModules.getLogicalModuleTo().getLogicalModuleType(), logicalModuleTo);
	}

	private void addMessage(Element destination, Message message) {
		Element messageElement = createElementWithoutContent("message", destination);
		createLogicalModulesElement(message.getLogicalModules(), messageElement);
		createElementWithContent("ruleKey", message.getRuleKey(), messageElement);
		Element violationTypeKeysElement = createElementWithoutContent("violationTypeKeys", messageElement);
		for(String violationTypeKey : message.getViolationTypeKeys()) {
			createElementWithContent("violationTypeKey", violationTypeKey, violationTypeKeysElement);
		}
		Element exceptionMessages = createElementWithoutContent("exceptionMessages", messageElement);
		for(Message exceptionMessage : message.getExceptionMessage()) {
			addMessage(exceptionMessages, exceptionMessage);
		}
	}

}
