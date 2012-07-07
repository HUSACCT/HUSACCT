package husacct.validate.task.report.writer;

import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.validation.Message;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.internal_transfer_objects.ViolationsPerSeverity;
import husacct.validate.domain.validation.report.Report;
import husacct.validate.task.extensiontypes.ExtensionTypes.ExtensionType;

import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Attribute;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class XMLReportWriter extends ReportWriter {

	public XMLReportWriter(Report report, String path, String fileName) {
		super(report, path, fileName, ExtensionType.XML);
	}

	@Override
	public void createReport() throws IOException {		
		Document document = new Document();

		Element reportElement = new Element("report");
		document.setRootElement(reportElement);

		Element projectName = new Element("projectName");
		projectName.setText(report.getProjectName());
		reportElement.addContent(projectName);

		Element projectVersion = new Element("version");
		projectVersion.setText(report.getVersion());
		reportElement.addContent(projectVersion);

		Element totalViolations = new Element("totalViolations");
		totalViolations.setText("" + report.getViolations().getValue().size());
		reportElement.addContent(totalViolations);

		Element violationGeneratedOn = new Element("violationsGeneratedOn");
		violationGeneratedOn.setText(report.getFormattedDate());
		reportElement.addContent(violationGeneratedOn);

		Element violationsSeverities = new Element("violations");
		violationsSeverities.setAttribute(new Attribute("totalViolations" , "" +  report.getViolations().getValue().size()));
		for(ViolationsPerSeverity violationPerSeverity : report.getViolationsPerSeverity()) {
			Element violationElement = new Element(violationPerSeverity.getSeverity().getSeverityKey());
			violationElement.setText("" + violationPerSeverity.getAmount());
			violationsSeverities.addContent(violationElement);
		}
		reportElement.addContent(violationsSeverities);

		Element violations = new Element("violations");
		reportElement.addContent(violations);

		for(Violation violation : report.getViolations().getValue()) {
			Element xmlViolation = new Element("violation");

			Element source = new Element("source");
			Element target = new Element("target");
			Element lineNr = new Element("lineNr");
			Element severity = new Element("severity");
			Element ruleType = new Element("ruleType");
			Element dependencyKind = new Element("dependencyKind");
			Element isDirect = new Element("isDirect");

			target.setText(violation.getClassPathTo());
			source.setText(violation.getClassPathFrom());
			lineNr.setText("" + violation.getLinenumber());
			severity.setText(violation.getSeverity().getSeverityName());
			if(violation.getLogicalModules() != null) {
				Message messageObject = violation.getMessage();
				String message = new Messagebuilder().createMessage(messageObject);
				ruleType.setText(message);
			}
			dependencyKind.setText(violation.getViolationtypeKey());
			isDirect.setText("" + violation.isIndirect());

			xmlViolation.addContent(source);
			xmlViolation.addContent(target);
			xmlViolation.addContent(lineNr);
			xmlViolation.addContent(severity);
			xmlViolation.addContent(ruleType);
			xmlViolation.addContent(dependencyKind);
			xmlViolation.addContent(isDirect);

			violations.addContent(xmlViolation);
		}
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		FileWriter fileWriter = new FileWriter(getFileName());
		outputter.output(document, fileWriter);
		fileWriter.close();		
	}
}