package husacct.validate.abstraction.export.xml;

import husacct.validate.abstraction.xmlutil.XMLUtils;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.util.GregorianCalendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExportViolations {

	private Document document;

	public ExportViolations() throws ParserConfigurationException {
		document = XMLUtils.createNewXMLDocument();
	}

	public Document exportViolations(List<Violation> violations)  {
		Element violationsElement = XMLUtils.createRootElementWithoutContent(document, "violations");

		for(Violation violation : violations) {
			Element violationElement = XMLUtils.createElementWithoutContent(document, "violation", violationsElement);

			XMLUtils.createElementWithContent(document, "lineNumber", "" + violation.getLinenumber(), violationElement);
			XMLUtils.createElementWithContent(document, "severityValue", "" + violation.getSeverityValue(), violationElement);
			XMLUtils.createElementWithContent(document, "ruletypeKey", violation.getRuletypeKey(), violationElement);
			XMLUtils.createElementWithContent(document, "violationtypeKey",violation.getViolationtypeKey(), violationElement);
			XMLUtils.createElementWithContent(document, "classPathFrom",violation.getClassPathFrom(), violationElement);
			XMLUtils.createElementWithContent(document, "classPathTo",violation.getClassPathTo(), violationElement);

			createLogicalModulesElement(violation.getLogicalModules(), violationElement);

			addMessage(violationElement, violation.getMessage());

			XMLUtils.createElementWithContent(document, "isIndirect",""+violation.isIndirect(), violationElement);
			try {
				XMLUtils.createElementWithContent(document, "occured", DatatypeFactory.newInstance().newXMLGregorianCalendar((GregorianCalendar)violation.getOccured()).toString(), violationElement);
			} catch (DatatypeConfigurationException e) {
				Logger.getLogger(ExportViolations.class.getName()).log(Level.SEVERE, "There was a error creating a new date in ExportViolations", e);
				e.printStackTrace();
			}
		}
		return document;
	}

	private void createLogicalModulesElement(LogicalModules logicalModules, Element destinationElement) {
		Element logicalModulesElement = XMLUtils.createElementWithoutContent(document, "logicalModules", destinationElement);
		Element logicalModuleFrom = XMLUtils.createElementWithoutContent(document, "logicalModuleFrom", logicalModulesElement);
		Element logicalModuleTo = XMLUtils.createElementWithoutContent(document, "logicalModuleTo", logicalModulesElement);

		XMLUtils.createElementWithContent(document, "logicalModulePath", logicalModules.getLogicalModuleFrom().getLogicalModulePath(), logicalModuleFrom);
		XMLUtils.createElementWithContent(document, "logicalModuleType", logicalModules.getLogicalModuleFrom().getLogicalModuleType(), logicalModuleFrom);
		XMLUtils.createElementWithContent(document, "logicalModulePath", logicalModules.getLogicalModuleTo().getLogicalModulePath(), logicalModuleTo);
		XMLUtils.createElementWithContent(document, "logicalModuleType", logicalModules.getLogicalModuleTo().getLogicalModuleType(), logicalModuleTo);
	}

	private void addMessage(Element destination, Message message) {
		Element messageElement = XMLUtils.createElementWithoutContent(document, "message", destination);
		createLogicalModulesElement(message.getLogicalModules(), messageElement);
		XMLUtils.createElementWithContent(document, "ruleKey", message.getRuleKey(), messageElement);
		Element violationTypeKeysElement = XMLUtils.createElementWithoutContent(document, "violationTypeKeys", messageElement);
		for(String violationTypeKey : message.getViolationTypeKeys()) {
			XMLUtils.createElementWithContent(document, "violationTypeKey", violationTypeKey, violationTypeKeysElement);
		}
		if(message.getExceptionMessage() != null && !message.getExceptionMessage().isEmpty()) {
			Element exceptionMessages = XMLUtils.createElementWithoutContent(document, "exceptionMessage", messageElement);
			for(Message exceptionMessage : message.getExceptionMessage()) {
				addMessage(exceptionMessages, exceptionMessage);
			}
		}
	}

}
