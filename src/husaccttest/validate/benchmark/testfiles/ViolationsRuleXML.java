package husaccttest.validate.benchmark.testfiles;

import husacct.validate.domain.validation.Violation;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class ViolationsRuleXML {
	private static final String FILE_PRE_NAME = "src/husaccttest/validate/benchmark/testfiles/violations_rule_";
	private static final String FILE_EXTENSION = ".xml";

	public ViolationsRuleXML() {

	}

	public ArrayList<Violation> giveViolationsOutXMLFile(int fileNumber) {
		ArrayList<Violation> foundedViolations = new ArrayList<Violation>();
		if(checkIfXMLFileExists(fileNumber)) {
			String filePath = getFileNameFromFileNumber(fileNumber);
			foundedViolations = readXMLFile(filePath);
		}

		return foundedViolations;
	}

	private ArrayList<Violation> readXMLFile(String filePath) {
		ArrayList<Violation> xmlViolations = new ArrayList<Violation>();


		// Create a factory
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		// Use the factory to create a builder
		DocumentBuilder builder;
		try {
			builder = factory.newDocumentBuilder();
			Document doc = builder.parse(filePath);

			// Get a list of all elements in the document
			NodeList list = doc.getElementsByTagName("*");
			System.out.println("=> XML Elements: ");

			for (int counter = 0; counter < list.getLength(); counter++) {
				// Get element
				Element element = (Element)list.item(counter);
				System.out.println(element.getNodeName());
			}
		}
		catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}

		return xmlViolations;
	}


	private boolean checkIfXMLFileExists(int fileNumber) {
		boolean returnCondition = false;

		try {
			File file = new File(getFileNameFromFileNumber(fileNumber));

			if(file.exists()) {
				returnCondition = true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return returnCondition;
	}

	private String getFileNameFromFileNumber(int fileNumber) {
		if(fileNumber < 10) {
			return FILE_PRE_NAME + "0" + fileNumber + FILE_EXTENSION;
		}
		else {
			return FILE_PRE_NAME + fileNumber + FILE_EXTENSION;
		}
	}

}
