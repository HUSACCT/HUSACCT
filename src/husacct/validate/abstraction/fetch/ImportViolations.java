package husacct.validate.abstraction.fetch;

import husacct.validate.abstraction.xmlpaths.XMLPaths;
import husacct.validate.domain.violation.Violation;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ImportViolations {


	public List<Violation> getViolationsByXML() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		List<Violation> violations = new ArrayList<Violation>();
		Document doc = dBuilder.parse(new File(XMLPaths.VIOLATIONS_PATH));
		doc.getDocumentElement().normalize();
		NodeList violationList = doc.getElementsByTagName("violation");
		for (int s = 0; s < violationList.getLength(); s++) {
			Node violationNode = violationList.item(s);
			if (violationNode.getNodeType() == Node.ELEMENT_NODE) {
				Violation violation = new Violation();
				Element violationElement = (Element) violationNode;

				NodeList linenumberElementList = violationElement.getElementsByTagName("linenumber");
				Element linenumberElement = (Element) linenumberElementList.item(0);
				NodeList linenumberList = linenumberElement.getChildNodes();
				violation.setLinenumber(Integer.parseInt((linenumberList.item(0)).getNodeValue()));

				NodeList severityValueElementList = violationElement.getElementsByTagName("severityValue");
				Element severityValueElement = (Element) severityValueElementList.item(0);
				if(severityValueElement != null && severityValueElement.getChildNodes() != null ) {
					NodeList severityValueList = severityValueElement.getChildNodes();
					violation.setSeverityValue(Integer.parseInt((severityValueList.item(0)).getNodeValue()));
				}


				NodeList ruletypeKeyElementList = violationElement.getElementsByTagName("ruletypeKey");
				Element ruletypeKeyElement = (Element) ruletypeKeyElementList.item(0);
				NodeList ruletypeKeyList = ruletypeKeyElement.getChildNodes();
				violation.setRuletypeKey((ruletypeKeyList.item(0)).getNodeValue());

				NodeList violationtypeKeyElementList = violationElement.getElementsByTagName("violationtypeKey");
				Element violationtypeKeyElement = (Element) violationtypeKeyElementList.item(0);
				NodeList violationtypeKeyList = violationtypeKeyElement.getChildNodes();
				violation.setViolationtypeKey((violationtypeKeyList.item(0)).getNodeValue());		     

				NodeList classPathFromElementList = violationElement.getElementsByTagName("classPathFrom");
				Element classPathFromElement = (Element) classPathFromElementList.item(0);
				NodeList classPathFromList = classPathFromElement.getChildNodes();
				violation.setClassPathFrom((classPathFromList.item(0)).getNodeValue());		

				NodeList classPathToElementList = violationElement.getElementsByTagName("classPathTo");
				Element classPathToElement = (Element) classPathToElementList.item(0);
				NodeList classPathToList = classPathToElement.getChildNodes();
				violation.setClassPathTo((classPathToList.item(0)).getNodeValue());		

				NodeList logicalModuleToElementList = violationElement.getElementsByTagName("logicalModuleTo");
				Element logicalModuleToElement = (Element) logicalModuleToElementList.item(0);
				NodeList logicalModuleToList = logicalModuleToElement.getChildNodes();
				violation.setLogicalModuleTo((logicalModuleToList.item(0)).getNodeValue());	

				NodeList logicalModuleFromElementList = violationElement.getElementsByTagName("logicalModuleFrom");
				Element logicalModuleFromElement = (Element) logicalModuleFromElementList.item(0);
				NodeList logicalModuleFromList = logicalModuleFromElement.getChildNodes();
				violation.setLogicalModuleFrom((logicalModuleFromList.item(0)).getNodeValue());	

				NodeList logicalModuleFromTypeElementList = violationElement.getElementsByTagName("logicalModuleFromType");
				Element logicalModuleFromTypeElement = (Element) logicalModuleFromTypeElementList.item(0);
				NodeList logicalModuleFromTypeList = logicalModuleFromTypeElement.getChildNodes();
				violation.setLogicalModuleFromType((logicalModuleFromTypeList.item(0)).getNodeValue());	

				NodeList logicalModuleToTypeElementList = violationElement.getElementsByTagName("logicalModuleToType");
				Element logicalModuleToTypeElement = (Element) logicalModuleToTypeElementList.item(0);
				NodeList logicalModuleToTypeList = logicalModuleToTypeElement.getChildNodes();
				violation.setLogicalModuleToType((logicalModuleToTypeList.item(0)).getNodeValue());	

				NodeList messageElementList = violationElement.getElementsByTagName("message");
				Element messageElement = (Element) messageElementList.item(0);
				NodeList messageList = messageElement.getChildNodes();
				violation.setMessage((messageList.item(0)).getNodeValue());	

				NodeList isIndirectElementList = violationElement.getElementsByTagName("isIndirect");
				Element isIndirectElement = (Element) isIndirectElementList.item(0);
				NodeList isIndirectList = isIndirectElement.getChildNodes();
				violation.setIndirect(Boolean.parseBoolean((isIndirectList.item(0)).getNodeValue()));	

				NodeList occuredElementList = violationElement.getElementsByTagName("occured");
				Element occuredElement = (Element) occuredElementList.item(0);
				NodeList occuredList = occuredElement.getChildNodes();
				if(occuredList != null && occuredList.item(0) != null) {
					violation.setOccured(Date.valueOf((occuredList.item(0)).getNodeValue()));	
				}
				violations.add(violation);
			}

		}
		return violations;
	}
	

}
