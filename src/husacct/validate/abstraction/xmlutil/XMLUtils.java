package husacct.validate.abstraction.xmlutil;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public abstract class XMLUtils {
	public static Element createElementWithContent(Document document, String name, String content, Element destination) {
		Element element = document.createElement(name);
		element.setTextContent(content);
		destination.appendChild(element);
		return element;
	}
	
	public static Element createElementWithoutContent(Document document, String name, Element destination) {
		Element element = document.createElement(name);
		destination.appendChild(element);
		return element;
	}
	
	public static Element createRootElementWithoutContent(Document document, String name) {
		Element element = document.createElement(name);
		document.appendChild(element);
		return element;
	}
	
	public static Document createNewXMLDocument() throws ParserConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document document = parser.newDocument();
		return document;
	}
	
	public static String getContentFromElement(Element element, String tag) {
		String returnValue;
		NodeList nlList = element.getElementsByTagName(tag).item(0).getChildNodes();
		Node nValue = (Node) nlList.item(0);

		if(nValue != null && nValue.getNodeValue() != null) {
			returnValue = nValue.getNodeValue();
		} else {
			returnValue = "";
		}
		return returnValue;

	}
	
	//FIXME only for test purposes
	public static Document getDocumentFromFile(File file) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
		return dBuilder.parse(file);
	}	
}