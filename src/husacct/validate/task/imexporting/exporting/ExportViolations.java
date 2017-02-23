package husacct.validate.task.imexporting.exporting;

import husacct.common.dto.ViolationImExportDTO;
import husacct.common.enums.ExtensionTypes;
import husacct.common.imexport.XmlConversionUtils;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.internaltransferobjects.ViolationsPerSeverity;
import husacct.validate.task.TaskServiceImpl;
import husacct.validate.task.imexporting.reporting.Report;
import husacct.validate.task.imexporting.reporting.ReportWriter;

import java.io.FileWriter;
import java.io.IOException;

import org.jdom2.Attribute;
import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ExportViolations extends ReportWriter {

	public ExportViolations(Report report, String path, String fileName, TaskServiceImpl taskServiceImpl) {
		super(report, path, fileName, ExtensionTypes.XML, taskServiceImpl);
	}

	@Override
	public void createReport() throws IOException {
		Document document = new Document();
		Comment comment = new Comment("from = path of from-class");
		document.addContent(comment);

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
		violationsSeverities.setAttribute(new Attribute("totalViolations", "" + report.getViolations().getValue().size()));
		
		for (ViolationsPerSeverity violationPerSeverity : report.getViolationsPerSeverity()) {
			Element violationElement = new Element(violationPerSeverity.getSeverity().getSeverityKey());
			violationElement.setText("" + violationPerSeverity.getAmount());
			violationsSeverities.addContent(violationElement);
		}
		reportElement.addContent(violationsSeverities);

		Element violations = new Element("violations");
		reportElement.addContent(violations);

		for (Violation violation : report.getViolations().getValue()) {
			ViolationImExportDTO violationImExportDTO = new ViolationImExportDTO();
			violationImExportDTO.setFrom(violation.getClassPathFrom());
			violationImExportDTO.setTo(violation.getClassPathTo());
			violationImExportDTO.setLine(violation.getLinenumber());
			violationImExportDTO.setDepType(violation.getViolationTypeKey());
			violationImExportDTO.setDepSubType(violation.getDependencySubType());
			violationImExportDTO.setFrom(violation.getClassPathFrom());
			violationImExportDTO.setIndirect(violation.getIsIndirect());
			violationImExportDTO.setSeverity(violation.getSeverity().getSeverityKey());
			violationImExportDTO.setRuleType(violation.getRuletypeKey());
			violationImExportDTO.setFromMod(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
			violationImExportDTO.setToMod(violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath());

			Element xmlViolation = XmlConversionUtils.writeDtoToXml("violation", violationImExportDTO);
			violations.addContent(xmlViolation);
		}
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		FileWriter fileWriter = new FileWriter(getFileName());
		outputter.output(document, fileWriter);
		fileWriter.close();
	}
}