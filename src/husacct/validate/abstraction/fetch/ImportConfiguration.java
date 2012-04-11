package husacct.validate.abstraction.fetch;

import husacct.validate.domain.severity.Severity;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
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

public class ImportConfiguration {
	public List<Severity> getSeveritiesByXML() throws SAXException, IOException, ParserConfigurationException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		List<Severity> severities = new ArrayList<Severity>();
		
		Document doc = null;
		try {
			doc = dBuilder.parse(new File(ClassLoader.getSystemResource("husacct/validate/abstraction/fetch/severities.xml").toURI()).getAbsolutePath());
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
		NodeList severityList = doc.getElementsByTagName("severity");

		for (int s = 0; s < severityList.getLength(); s++) {
			Severity severity = new Severity();
			Node severityNode = severityList.item(s);

			if (severityNode.getNodeType() == Node.ELEMENT_NODE) {

				Element severityElement = (Element) severityNode;
				
				NodeList defaultNameElementList = severityElement.getElementsByTagName("defaultName");
				Element defaultNameElement = (Element) defaultNameElementList.item(0);
				NodeList defaultNameList = defaultNameElement.getChildNodes();
				severity.setDefaultName((defaultNameList.item(0)).getNodeValue());

				NodeList userNameElementList = severityElement.getElementsByTagName("userName");
				Element userNameElement = (Element) userNameElementList.item(0);
				if(userNameElement != null && userNameElement.getChildNodes() != null ) {
					NodeList userNameList = userNameElement.getChildNodes();
					severity.setUserName((userNameList.item(0)).getNodeValue());
				}


				NodeList valueElementList = severityElement.getElementsByTagName("value");
				Element valueElement = (Element) valueElementList.item(0);
				NodeList valueList = valueElement.getChildNodes();
				severity.setValue(Integer.parseInt((valueList.item(0)).getNodeValue()));

				NodeList colorElementList = severityElement.getElementsByTagName("color");
				Element colorElement = (Element) colorElementList.item(0);
				NodeList colorList = colorElement.getChildNodes();
				severity.setColor((colorList.item(0)).getNodeValue());		      
			}
			severities.add(severity);
		}
		return severities;
	}
}
