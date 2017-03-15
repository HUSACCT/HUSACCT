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

import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

public class ExportAllViolations extends ReportWriter {

	public ExportAllViolations(Report report, String path, String fileName, TaskServiceImpl taskServiceImpl) {
		super(report, path, fileName, ExtensionTypes.XML, taskServiceImpl);
	}

	@Override
	public void createReport() throws IOException {
		Document document = createReportDocument();
		XMLOutputter outputter = new XMLOutputter(Format.getPrettyFormat());
		FileWriter fileWriter = new FileWriter(getFileName());
		outputter.output(document, fileWriter);
		fileWriter.close();
	}

	public Document createReportDocument() {
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

		Element violationsPerSeverity = new Element("violationsPerSeverity");
		// violationsPerSeverity.setAttribute(new Attribute("totalViolations", "" + report.getViolations().getValue().size()));
		for (ViolationsPerSeverity violationPerSeverity : report.getViolationsPerSeverity()) {
			Element violationPerSeverityElement = new Element(violationPerSeverity.getSeverity().getSeverityKey());
			violationPerSeverityElement.setText("" + violationPerSeverity.getAmount());
			violationsPerSeverity.addContent(violationPerSeverityElement);
		}
		reportElement.addContent(violationsPerSeverity);

		Element violations = new Element("violations");
		reportElement.addContent(violations);
		Comment comment1 = new Comment("from = path of from-class");
		violations.addContent(comment1);
		Comment comment2 = new Comment("to = path of to-class");
		violations.addContent(comment2);
		Comment comment3 = new Comment("line = Line in the source of the fromClass that contains the violating code construct");
		violations.addContent(comment3);
		Comment comment4 = new Comment("depType = DependencyType (for dependency-related rule types) or visibilityType, etc.");
		violations.addContent(comment4);
		Comment comment5 = new Comment("depSubType = DependencySubType (for dependency-related rule types)");
		violations.addContent(comment5);
		Comment comment6 = new Comment("indirect = Direct/indirect dependency (for dependency-related rule types)");
		violations.addContent(comment6);
		Comment comment7 = new Comment("severity = Key of the severity");
		violations.addContent(comment7);
		Comment comment8 = new Comment("message = Short explanation of the violated rule");
		violations.addContent(comment8);
		Comment comment9 = new Comment("The following three identify the violated rule: ruleTypeKey + logicalModuleFrom + logicalModuleTo");
		violations.addContent(comment9);
		Comment comment10 = new Comment("ruleType = Identifier of RuleType; the type of violated rule");
		violations.addContent(comment10);
		Comment comment11 = new Comment("fromMod = ModuleFrom of the violated rule; not of the from-to software units");
		violations.addContent(comment11);
		Comment comment12 = new Comment("toMod = ModuleTo of the violated rule; not of the from-to software units");
		violations.addContent(comment12);

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
			violationImExportDTO.setMessage(taskServiceImpl.getMessage(violation));
			violationImExportDTO.setRuleType(violation.getRuletypeKey());
			violationImExportDTO.setFromMod(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
			violationImExportDTO.setToMod(violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath());

			Element xmlViolation = XmlConversionUtils.writeDtoToXml("violation", violationImExportDTO);
			violations.addContent(xmlViolation);
		}
		return document;
	}

}