package husacct.validate.abstraction.export;

import husacct.validate.domain.violation.Violation;

import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ExportViolations {

	public Document exportViolationsByXML(List<Violation> violations) throws ParserConfigurationException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document doc = parser.newDocument();

		Element violationsElement = doc.createElement("violations");
		doc.appendChild(violationsElement);

		for(Violation violation : violations) {
			Element violationElement = doc.createElement("violation");

			Element lineNumberElement = doc.createElement("lineNumber");
			lineNumberElement.setTextContent("" + violation.getLinenumber());
			violationElement.appendChild(lineNumberElement);

			Element severityValueElement = doc.createElement("severityValue");
			severityValueElement.setTextContent("" + violation.getSeverityValue());
			violationElement.appendChild(severityValueElement);

			Element ruletypeKeyElement = doc.createElement("ruletypeKey");
			ruletypeKeyElement.setTextContent(violation.getRuletypeKey());
			violationElement.appendChild(ruletypeKeyElement);

			Element violationtypeKeyElement = doc.createElement("violationtypeKey");
			violationtypeKeyElement.setTextContent(violation.getViolationtypeKey());
			violationElement.appendChild(violationtypeKeyElement);

			Element classPathFromElement = doc.createElement("classPathFrom");
			classPathFromElement.setTextContent(violation.getClassPathFrom());
			violationElement.appendChild(classPathFromElement);

			Element classPathToElement = doc.createElement("classPathTo");
			classPathToElement.setTextContent(violation.getClassPathTo());
			violationElement.appendChild(classPathToElement);

			Element logicalModuleFromElement = doc.createElement("logicalModuleFrom");
			logicalModuleFromElement.setTextContent(violation.getLogicalModuleFrom());
			violationElement.appendChild(logicalModuleFromElement);

			Element logicalModuleToElement = doc.createElement("logicalModuleTo");
			logicalModuleToElement.setTextContent(violation.getLogicalModuleTo());
			violationElement.appendChild(logicalModuleToElement);

			Element logicalModuleToTypeElement = doc.createElement("logicalModuleToType");
			logicalModuleToTypeElement.setTextContent(violation.getLogicalModuleToType());
			violationElement.appendChild(logicalModuleToTypeElement);

			Element logicalModuleFromTypeElement = doc.createElement("logicalModuleFromType");
			logicalModuleFromTypeElement.setTextContent(violation.getLogicalModuleFromType());
			violationElement.appendChild(logicalModuleFromTypeElement);

			Element messageElement = doc.createElement("message");
			messageElement.setTextContent(violation.getMessage());
			violationElement.appendChild(messageElement);

			Element isIndirectElement = doc.createElement("isIndirect");
			isIndirectElement.setTextContent("" + violation.isIndirect());
			violationElement.appendChild(isIndirectElement);

			Element occuredElement = doc.createElement("occured");
			if(violation.getOccured() != null) {
				occuredElement.setTextContent(violation.getOccured().toString());
			}
			violationElement.appendChild(occuredElement);

			violationsElement.appendChild(violationElement);
			
		}
		return doc;
	}

}
