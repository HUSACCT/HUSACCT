package husacct.validate.task.imexporting.exporting;

import husacct.common.dto.ViolationImExportDTO;
import husacct.common.imexport.XmlConversionUtils;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import org.jdom2.Comment;
import org.jdom2.Document;
import org.jdom2.Element;

public class ExportNewViolations {
	private final SimpleDateFormat dateFormat;

	public ExportNewViolations() {
		dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
	}

	public Document createReport(List<ViolationImExportDTO> newViolationsList, Calendar saccMoment) throws IOException {
		Document document = new Document();

		Element reportElement = new Element("report");
		document.setRootElement(reportElement);

		Element totalViolations = new Element("totalNewViolations");
		totalViolations.setText("" + newViolationsList.size());
		reportElement.addContent(totalViolations);

		Element violationGeneratedOn = new Element("violationsGeneratedOn");
		violationGeneratedOn.setText(dateFormat.format(saccMoment.getTime()));
		reportElement.addContent(violationGeneratedOn);

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

		for (ViolationImExportDTO violationImExportDTO : newViolationsList) {
			Element xmlViolation = XmlConversionUtils.writeDtoToXml("violation", violationImExportDTO);
			violations.addContent(xmlViolation);
		}
		return document;
	}
}