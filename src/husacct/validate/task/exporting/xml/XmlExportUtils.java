package husacct.validate.task.exporting.xml;

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

}