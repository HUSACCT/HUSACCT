package husacct.validate.task.report.writer;

import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.internal_transfer_objects.ViolationsPerSeverity;
import husacct.validate.domain.validation.report.Report;
import husacct.validate.task.extensiontypes.ExtensionTypes.ExtensionType;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;

public class HTMLReportWriter extends ReportWriter {

	private FileWriter writer;
	private BufferedWriter html;

	public HTMLReportWriter(Report report, String path, String fileName) throws IOException {
		super(report, path, fileName, ExtensionType.HTML);
	}

	@Override
	public void createReport() throws IOException, URISyntaxException {
		checkDirsExist();
		createResources();
		writer = new FileWriter(getFileName());
		html = new BufferedWriter(writer);
		html.append("<html>");
		createStatics();
		createBody();
		html.append("</html>");
		html.flush();
		html.close();
	}

	private void createResources() throws IOException, URISyntaxException {
		File resourcesDir = new File(path + "/HUSACCT Report Resources");
		resourcesDir.mkdir();

		File javascriptDir = new File(resourcesDir + "/js");
		javascriptDir.mkdir();

		File jqueryJSOutput = new File(javascriptDir + "/jquery-1.7.2.min.js");
		File dataTableJSOutput = new File(javascriptDir + "/jquery.dataTables.js");
		copyfile(getClass().getClassLoader().getResourceAsStream("husacct/common/resources/validate/jquery-1.7.2.min.js"), jqueryJSOutput);

		copyfile(getClass().getClassLoader().getResourceAsStream("husacct/common/resources/validate/jquery.dataTables.js"), dataTableJSOutput);

		File imageold = new File(report.getImagePath());
		FileInputStream fis = new FileInputStream(imageold);
		File imagenew = new File(resourcesDir + "/image.png");
		copyfile(fis, imagenew);
		fis.close();
		imageold.delete();

		File cssDir = new File(resourcesDir + "/css");
		cssDir.mkdir();
		InputStream cssResource = getClass().getClassLoader().getResourceAsStream("husacct/common/resources/validate/style.css");
		File cssDestination = new File(cssDir + "/style.css");
		copyfile(cssResource, cssDestination);
	}

	private void createStatics() throws IOException {

		html.append("<head>");
		html.append("<script type=\"text/javascript\" src=\"./HUSACCT Report Resources/js/jquery-1.7.2.min.js\"></script>");
		html.append("<script type=\"text/javascript\" src=\"./HUSACCT Report Resources/js/jquery.dataTables.js\"></script>");
		html.append("<script type=\"text/javascript\">$(document).ready(function() {$('#example').dataTable(  { \"bPaginate\": false, bInfo : false} );}); </script>");
		html.append("<link rel=\"stylesheet\" href=\"./HUSACCT Report Resources/css/style.css\" type=\"text/css\" />");
		html.append("</head>");

	}

	private void createBody() throws IOException {
		html.append("<body>");
		html.append("<table width=\"100%\">");
		html.append("<tr>");
		html.append("<td>");
		createStatistics();
		html.append("</td>");
		html.append("</tr>");
		html.append("<tr>");
		html.append("<td>");
		createTable();
		html.append("</td>");
		html.append("</tr>");
		html.append("</table>");
		html.append("</body>");
	}

	private void createStatistics() throws IOException {
		html.append("<div class=\"image\">");
		html.append("<img src=\"./HUSACCT Report Resources/image.png\"/>");
		html.append("</div>");
		html.append("<span class=\"title\">HUSACCT HTML REPORT</span><br/>");
		html.append("<span>Date generated: " + getCurrentDate() + "</span><br />Project: " + report.getProjectName() + "<br />Version: " + report.getVersion() + "<br />");
		html.append("<span>Violations generated on: " + report.getFormattedDate() + "</span><br />");
		html.append("<span class=\"stats\">Statistics</span><br/>");
		html.append("Total violations: " + report.getViolations().getValue().size() + "<br />" + "<br />");
		for (ViolationsPerSeverity severityPerViolation : report.getViolationsPerSeverity()) {
			html.append(severityPerViolation.getSeverity().getSeverityName() + ": " + severityPerViolation.getAmount() + "<br />");
		}
	}

	private void createTable() throws IOException {
		html.append("<span class=\"stats\">Violations</span>");
		html.append("<table id=\"example\" border=\"1\" width=\"100%\">");
		html.append("<thead>");
		html.append("<tr>");
		for (String columnHeader : report.getLocaleColumnHeaders()) {
			html.append("<th>");
			if (columnHeader != null && !columnHeader.trim().equals("")) {
				html.append(columnHeader);
			} else {
				html.append("&nbsp;");
			}
			html.append("</th>");
		}
		html.append("</tr>");
		html.append("</thead>");
		html.append("<tbody>");
		for (Violation violation : report.getViolations().getValue()) {
			html.append("<tr>");
			// Source
			if (violation.getClassPathFrom() != null) {
				createColumn(violation.getClassPathFrom());
			} else {
				createColumn("");
			}

			// Rule
			if (violation.getMessage() != null) {
				String message = new Messagebuilder().createMessage(violation.getMessage(), violation);
				createColumn(message);
			} else {
				createColumn("");
			}

			// LineNumber
			if (violation.getLinenumber() > 0) {
				createColumn("" + violation.getLinenumber());
			} else {
				createColumn("");
			}

			// Dependency Kind
			if (violation.getViolationtypeKey() != null) {
				createColumn(getDependencyKindValue(violation.getViolationtypeKey(), violation.isIndirect()));
			} else {
				createColumn("");
			}

			// Target
			if (violation.getClassPathTo() != null) {
				createColumn(violation.getClassPathTo());
			} else {
				createColumn("");
			}

			// Severity
			if (violation.getSeverity() != null) {
				createColumn(violation.getSeverity().getSeverityName());
			} else {
				createColumn("");
			}
			html.append("</tr>");
		}
		html.append("</tbody>");
		html.append("</table>");
	}

	private void createColumn(String content) throws IOException {
		html.append("<td>");
		if (content != null && !content.trim().equals("")) {
			html.append(content);
		} else {
			html.append("&nbsp;");
		}
		html.append("</td>");
	}

	private void copyfile(InputStream inputStream, File destination) throws IOException {
		OutputStream out = new FileOutputStream(destination);
		byte[] buf = new byte[1024];
		int len;
		while ((len = inputStream.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		inputStream.close();
		out.close();
	}
}