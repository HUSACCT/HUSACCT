package husacct.validate.task;

import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import org.jdom2.Element;

public class XMLUtils {
	
	public static void createElementWithContent(String name, String content, Element destination) {
		Element element = new Element(name);
		element.addContent(content);
		destination.addContent(element);
	}
	public static Element createElementWithoutContent(String name, Element destination) {
		Element element = new Element(name);
		destination.addContent(element);
		return element;
	}
	
	public static void createLogicalModulesElement(LogicalModules logicalModules, Element destinationElement) {
		Element logicalModulesElement =XMLUtils.createElementWithoutContent("logicalModules", destinationElement);
		Element logicalModuleFrom = XMLUtils.createElementWithoutContent("logicalModuleFrom", logicalModulesElement);
		Element logicalModuleTo = XMLUtils.createElementWithoutContent("logicalModuleTo", logicalModulesElement);

		XMLUtils.createElementWithContent("logicalModulePath", logicalModules.getLogicalModuleFrom().getLogicalModulePath(), logicalModuleFrom);
		XMLUtils.createElementWithContent("logicalModuleType", logicalModules.getLogicalModuleFrom().getLogicalModuleType(), logicalModuleFrom);
		XMLUtils.createElementWithContent("logicalModulePath", logicalModules.getLogicalModuleTo().getLogicalModulePath(), logicalModuleTo);
		XMLUtils.createElementWithContent("logicalModuleType", logicalModules.getLogicalModuleTo().getLogicalModuleType(), logicalModuleTo);
	}

	public static void addMessage(Element destination, Message message) {
		Element messageElement = XMLUtils.createElementWithoutContent("message", destination);
		createLogicalModulesElement(message.getLogicalModules(), messageElement);
		XMLUtils.createElementWithContent("ruleKey", message.getRuleKey(), messageElement);
		Element violationTypeKeysElement = XMLUtils.createElementWithoutContent("violationTypeKeys", messageElement);
		for(String violationTypeKey : message.getViolationTypeKeys()) {
			XMLUtils.createElementWithContent("violationTypeKey", violationTypeKey, violationTypeKeysElement);
		}
		Element exceptionMessages = XMLUtils.createElementWithoutContent("exceptionMessages", messageElement);
		for(Message exceptionMessage : message.getExceptionMessage()) {
			addMessage(exceptionMessages, exceptionMessage);
		}
	}

}
