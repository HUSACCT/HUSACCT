package husacct.validate.abstraction.report;

import husacct.validate.abstraction.fetch.UnknownStorageTypeException;
import husacct.validate.domain.violation.Violation;
import husacct.validate.task.MessageBuilder;
import husacct.validate.task.ViolationsPerSeverity;

import java.io.FileWriter;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class HTMLReportWriter extends ReportWriter {

	private FileWriter html;

	public HTMLReportWriter(Report report, String path, String fileName) throws IOException {
		super(report, path, fileName);
	}

	public void createReport() throws IOException, ParserConfigurationException, SAXException, UnknownStorageTypeException {
		checkDirsExist();
		html = new FileWriter(getFileName());
		html.append("<html>");
		createStatics();
		createBody();
		html.append("</html>");
		html.flush();
		html.close();
	}
	private void createStatics() throws IOException {

		html.append("<head>");
		html.append("<script type=\"text/javascript\" src=\"http://code.jquery.com/jquery-1.7.2.min.js\"></script>");
		html.append("<script type=\"text/javascript\" src=\"http://datatables.net/download/build/FixedHeader.js\"></script>");
		html.append("<script type=\"text/javascript\" src=\"http://datatables.net/download/build/jquery.dataTables.js\"></script>");
		html.append("<script type=\"text/javascript\">$(document).ready(function() {$('#example').dataTable(  { \"bPaginate\": false, bInfo : false} );}); </script>");
		html.append("<style type=\"text/css\">#example {border-collapse: collapse;border : 1px solid #4BACC6;}#example td {border: 1px solid  #4BACC6;}#example th {border : solid #4BACC6; background-color:#4BACC6;cursor: pointer;    color : white;    font-weight: bold;    padding: 3px 10px;}.image{float : right;} .stats {font-size: 14px; color : blue;  font-weight: bold;} .title {font-size : 16px; color : blue; font-weight: bold;}</style>");
		html.append("</head>");

	}
	private void createBody() throws IOException, ParserConfigurationException, SAXException, UnknownStorageTypeException {
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

	private void createStatistics() throws IOException, ParserConfigurationException, SAXException, UnknownStorageTypeException {
		html.append("<div class=\"image\">");
		html.append("<img src=\"image.png\"/>");
		html.append("</div>");
		html.append("<span class=\"title\">HUSACCT HTML REPORT</span><br/>");
		html.append("<span>Date generated: " + getCurrentDate() + "</span><br />Project: " + report.getProjectName() + "<br />Version: " + report.getVersion() + "<br />");
		html.append("<span class=\"stats\">Statistics</span><br/>");
		html.append("Total violations: " + report.getViolations().size() + "<br />"+ "<br />");
		for(ViolationsPerSeverity severityPerViolation : report.getViolationUtil().getSeveritiesPerViolation(report.getViolations())) {
			html.append(severityPerViolation.getSeverity().getDefaultName() + ": " + severityPerViolation.getAmount() + "<br />");
		}
	}
	private void createTable() throws IOException, ParserConfigurationException, SAXException, UnknownStorageTypeException {
		html.append("<span class=\"stats\">Violations</span>");
		html.append("<table id=\"example\" border=\"1\" width=\"100%\">");
		html.append("<thead>");
		html.append("<tr>");
		for(String columnHeader : report.getColumnHeaders()) {
			html.append("<th>");
			if(columnHeader != null && !columnHeader.trim().equals("")) {
				html.append(columnHeader);
			} else {
			html.append("&nbsp;");
			}
			html.append("</th>");
		}
		html.append("</tr>");
		html.append("</thead>");
		html.append("<tbody>");
		for(Violation violation : report.getViolations()) {
			html.append("<tr>");
			createColumn(violation.getClassPathFrom());
			createColumn(violation.getClassPathTo());
			createColumn("" + violation.getLinenumber());
			createColumn(report.getViolationUtil().getSeverityNameFromValue(violation.getSeverityValue()));
			createColumn(MessageBuilder.getRuleMessage(violation.getClassPathFrom(), violation.getLogicalModuleFromType(), violation.getClassPathTo(), violation.getLogicalModuleToType(), violation.getRuletypeKey()));
			createColumn(violation.getViolationtypeKey());
			createColumn(convertIsIndirectBooleanToString(violation.isIndirect()));
			html.append("</tr>");
		}
		html.append("</tbody>");
		html.append("</table>");
	}
	private void createColumn(String content) throws IOException {
		html.append("<td>");
		if(content != null && !content.trim().equals("")) {
			html.append(content);
		} else {
			html.append("&nbsp;");
		}
		html.append("</td>");
	}
}
