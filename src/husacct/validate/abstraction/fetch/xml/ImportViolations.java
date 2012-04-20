package husacct.validate.abstraction.fetch.xml;

import husacct.validate.abstraction.xmlutil.XMLUtils;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.logicalmodule.LogicalModule;
import husacct.validate.domain.validation.logicalmodule.LogicalModules;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ImportViolations {
	public List<Violation> importViolations(NodeList violationList) throws SAXException, IOException, ParserConfigurationException, DatatypeConfigurationException, ParseException {
		List<Violation> violations = new ArrayList<Violation>();
		for (int s = 0; s < violationList.getLength(); s++) {
			Node violationNode = violationList.item(s);
			if (violationNode.getNodeType() == Node.ELEMENT_NODE) {
				Element violationElement = (Element) violationNode;
				Violation violation = new Violation();

				violation.setLinenumber(Integer.parseInt(XMLUtils.getContentFromElement(violationElement, "lineNumber")));
				violation.setSeverityValue(Integer.parseInt(XMLUtils.getContentFromElement(violationElement, "severityValue")));
				violation.setRuletypeKey(XMLUtils.getContentFromElement(violationElement, "ruletypeKey"));
				violation.setViolationtypeKey(XMLUtils.getContentFromElement(violationElement, "violationtypeKey"));
				violation.setClassPathFrom(XMLUtils.getContentFromElement(violationElement, "classPathFrom"));
				violation.setClassPathTo(XMLUtils.getContentFromElement(violationElement, "classPathTo"));

				Element logicalModulesElement = (Element) violationElement.getElementsByTagName("logicalModules").item(0);
				violation.setLogicalModules(getLogicalModules(logicalModulesElement));
				
				Element messageElement = (Element) violationElement.getElementsByTagName("message").item(0);
				getMessage(messageElement);
				
				//violation.setMessage(XMLUtils.getContentFromElement(violationElement, "message"));
				violation.setIndirect(Boolean.parseBoolean(XMLUtils.getContentFromElement(violationElement, "isIndirect")));
				violation.setOccured(DatatypeFactory.newInstance().newXMLGregorianCalendar(XMLUtils.getContentFromElement(violationElement, "occured")).toGregorianCalendar());
				violations.add(violation);
			}

		}
		return violations;
	}
	private LogicalModules getLogicalModules(Element logicalModulesElement) {
		Element logicalModuleFromElement = (Element) logicalModulesElement.getElementsByTagName("logicalModuleFrom").item(0);
		Element logicalModuleToElement = (Element) logicalModulesElement.getElementsByTagName("logicalModuleTo").item(0);

		LogicalModule logicalModuleFrom = new LogicalModule(XMLUtils.getContentFromElement(logicalModuleFromElement, "logicalModulePath"), XMLUtils.getContentFromElement(logicalModuleFromElement, "logicalModuleType"));
		LogicalModule logicalModuleTo = new LogicalModule(XMLUtils.getContentFromElement(logicalModuleToElement, "logicalModulePath"), XMLUtils.getContentFromElement(logicalModuleToElement, "logicalModuleType"));
		LogicalModules logicalModules = new LogicalModules(logicalModuleFrom, logicalModuleTo);
		return logicalModules;
	}
	
	private Message getMessage(Element messageElement) {
		Element logicalModulesElement = (Element) messageElement.getElementsByTagName("logicalModules").item(0);
		LogicalModules logicalModules = getLogicalModules(logicalModulesElement);
		String ruleKey = XMLUtils.getContentFromElement(messageElement, "ruleKey");
		
		NodeList violationNodeList = messageElement.getElementsByTagName("violationTypeKeys");
		System.out.println();
		for (int s = 0; s < violationNodeList.getLength(); s++) {
			Node violationTypeKeyNode = violationNodeList.item(s);
			if (violationTypeKeyNode.getNodeType() == Node.ELEMENT_NODE) {
				Element violationTypeKeyElement = (Element) violationTypeKeyNode;
				
			}
		}
		
		Message message = new Message(logicalModules,ruleKey, new ArrayList<String>());
		return message;
	}
	
}
