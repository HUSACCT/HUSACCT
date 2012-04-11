package husacct.validate.abstraction.report;

import husacct.validate.abstraction.fetch.UnknownStorageTypeException;
import husacct.validate.domain.violation.Violation;
import husacct.validate.task.MessageBuilder;
import husacct.validate.task.ViolationsPerSeverity;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;

public class XMLReportWriter extends ReportWriter {

	public XMLReportWriter(Report report, String path, String fileName) {
		super(report, path, fileName);
	}

	@Override
	public void createReport() throws IOException, DocumentException, ParserConfigurationException, TransformerException, DOMException, SAXException, UnknownStorageTypeException {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder parser = factory.newDocumentBuilder();
		Document doc = parser.newDocument();

		Element reportElement = doc.createElement("report");
		doc.appendChild(reportElement);

		Element projectName = doc.createElement("projectName");
		projectName.setTextContent(report.getProjectName());
		reportElement.appendChild(projectName);

		Element projectVersion = doc.createElement("version");
		projectVersion.setTextContent(report.getVersion());
		reportElement.appendChild(projectVersion);

		Element totalViolations = doc.createElement("totalViolations");
		totalViolations.setTextContent("" + report.getViolations().size());
		reportElement.appendChild(totalViolations);

		for(ViolationsPerSeverity violationPerSeverity : report.getViolationUtil().getSeveritiesPerViolation(report.getViolations())) {
			Element violationElement = doc.createElement(violationPerSeverity.getSeverity().getDefaultName().replace(" ", ""));
			violationElement.setTextContent("" + violationPerSeverity.getAmount());
			reportElement.appendChild(violationElement);
		}

		Element violations = doc.createElement("violations");
		reportElement.appendChild(violations);

		for(Violation violation : report.getViolations()) {
			Element xmlViolation = doc.createElement("violation");

			Element source = doc.createElement("source");
			Element target = doc.createElement("target");
			Element lineNr = doc.createElement("lineNr.");
			Element severity = doc.createElement("severity");
			Element ruleType = doc.createElement("ruleType");
			Element dependencyKind = doc.createElement("dependencyKind");
			Element isDirect = doc.createElement("isDirect");

			target.setTextContent(violation.getClassPathTo());
			source.setTextContent(violation.getClassPathFrom());
			lineNr.setTextContent("" + violation.getLinenumber());
			severity.setTextContent(report.getViolationUtil().getSeverityNameFromValue(violation.getSeverityValue()));
			ruleType.setTextContent(MessageBuilder.getRuleMessage(violation.getClassPathFrom(), violation.getLogicalModuleToType(), violation.getClassPathTo(), violation.getLogicalModuleToType(), violation.getRuletypeKey()));
			dependencyKind.setTextContent(violation.getViolationtypeKey());
			isDirect.setTextContent("" + violation.isIndirect());

			xmlViolation.appendChild(source);
			xmlViolation.appendChild(target);
			xmlViolation.appendChild(lineNr);
			xmlViolation.appendChild(severity);
			xmlViolation.appendChild(ruleType);
			xmlViolation.appendChild(dependencyKind);
			xmlViolation.appendChild(isDirect);

			violations.appendChild(xmlViolation);
		}

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");

        StringWriter sw = new StringWriter();
        StreamResult result = new StreamResult(sw);
        DOMSource source = new DOMSource(doc);
        transformer.transform(source, result);
        String xmlString = sw.toString();
        
        checkDirsExist();
        
		FileWriter fw = new FileWriter(getFileName());
		fw.append(xmlString);
		fw.flush();
		fw.close();
	}

}
