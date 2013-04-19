package husacct.validate.task.export.xml;

import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import org.jdom2.Element;

public abstract class XmlExportUtils {

	protected Element createElementWithContent(String name, String content) {
		Element element = new Element(name);
		element.addContent(content);
		return element;
	}

	protected Element createLogicalModulesElement(LogicalModules logicalModules) {
		Element logicalModulesElement = new Element("logicalModules");
		Element logicalModuleFrom = new Element("logicalModuleFrom");
		Element logicalModuleTo = new Element("logicalModuleTo");

		logicalModulesElement.addContent(logicalModuleFrom);
		logicalModulesElement.addContent(logicalModuleTo);

		logicalModuleFrom.addContent(createElementWithContent("logicalModulePath", logicalModules.getLogicalModuleFrom().getLogicalModulePath()));
		logicalModuleFrom.addContent(createElementWithContent("logicalModuleType", logicalModules.getLogicalModuleFrom().getLogicalModuleType()));
		logicalModuleTo.addContent(createElementWithContent("logicalModulePath", logicalModules.getLogicalModuleTo().getLogicalModulePath()));
		logicalModuleTo.addContent(createElementWithContent("logicalModuleType", logicalModules.getLogicalModuleTo().getLogicalModuleType()));

		return logicalModulesElement;
	}

	protected Element createMessageElementFromMessageObject(Message message) {
		Element messageElement = new Element("message");
		messageElement.addContent(createLogicalModulesElement(message.getLogicalModules()));
		messageElement.addContent(createElementWithContent("ruleKey", message.getRuleKey()));
		messageElement.addContent(createElementWithContent("regex", message.getRegex()));
		Element violationTypeKeysElement = new Element("violationTypeKeys");
		messageElement.addContent(violationTypeKeysElement);
		for (String violationTypeKey : message.getViolationTypeKeys()) {
			violationTypeKeysElement.addContent(createElementWithContent("violationTypeKey", violationTypeKey));
		}
		Element exceptionMessages = new Element("exceptionMessages");
		messageElement.addContent(exceptionMessages);
		if (message.getExceptionMessage() != null) {
			for (Message exceptionMessage : message.getExceptionMessage()) {
				exceptionMessages.addContent(createMessageElementFromMessageObject(exceptionMessage));
			}
		}
		return messageElement;
	}
}