package husacct.validate.abstraction.report;

import husacct.validate.abstraction.fetch.UnknownStorageTypeException;
import husacct.validate.domain.violation.Violation;
import husacct.validate.task.MessageBuilder;
import husacct.validate.task.ViolationsPerSeverity;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class PDFReportWriter extends ReportWriter {

	private Document document;

	public PDFReportWriter(Report report, String path, String fileName) {
		super(report, path, fileName);
	}

	@Override
	public void createReport() throws DocumentException, MalformedURLException, IOException, ParserConfigurationException, SAXException, UnknownStorageTypeException  {
		document = new Document();
		checkDirsExist();
		PdfWriter.getInstance(document, new FileOutputStream(getFileName()));
		document.open();
		document.setPageSize(new Rectangle(1280, 600));
		document.newPage();

		Image image = Image.getInstance(report.getImagePath() + "/image.png");
		image.setAlignment(Image.RIGHT | Image.TEXTWRAP);
		document.add(image);

		createApplicationInfo();
		createStatistics();
		createTable();
		document.close();
	}
	private void createApplicationInfo() throws DocumentException {
		Phrase title = new Phrase();
		title.setFont(new Font(FontFamily.HELVETICA,15F,Font.BOLD,BaseColor.BLUE));
		title.add("HUSACCT PDF REPORT");
		document.add(new Paragraph(title));

		document.add(new Paragraph("Generated on: " + getCurrentDate()));
		document.add(new Paragraph("Project: " + report.getProjectName()));
		document.add(new Paragraph("Version: " + report.getVersion()));

		document.add(new Paragraph(" "));
	}
	private void createStatistics() throws DocumentException, MalformedURLException, IOException, ParserConfigurationException, SAXException, UnknownStorageTypeException {
		Phrase title = new Phrase();
		title.setFont(new Font(FontFamily.HELVETICA,13F,Font.BOLD,BaseColor.BLUE));
		title.add("Statistics");
		document.add(new Paragraph(title));


		document.add(new Paragraph("Total violations: " + report.getViolations().size()));
		document.add(new Paragraph(" "));

		for(ViolationsPerSeverity violationPerSeverity : report.getViolationUtil().getSeveritiesPerViolation(report.getViolations())) {
			document.add(new Paragraph(violationPerSeverity.getSeverity().getDefaultName() + ": " + violationPerSeverity.getAmount()));
		}
		document.add(new Paragraph(" "));
	}
	private void createTable() throws DocumentException, ParserConfigurationException, SAXException, IOException, UnknownStorageTypeException {
		Phrase title = new Phrase();
		title.setFont(new Font(FontFamily.HELVETICA,13F,Font.BOLD,BaseColor.BLUE));
		title.add("Violations");
		document.add(new Paragraph(title));
		document.add(new Paragraph(" "));

		PdfPTable pdfTable = new PdfPTable(report.getColumnHeaders().length);
		pdfTable.setWidthPercentage(100);

		for(String columnHeader : report.getColumnHeaders()) {
			addCellToTable(pdfTable, columnHeader, BaseColor.GRAY, true);
		}

		for(Violation violation : report.getViolations()) {
			addCellToTable(pdfTable,violation.getClassPathFrom(), BaseColor.WHITE, false);
			addCellToTable(pdfTable,violation.getClassPathTo(), BaseColor.WHITE, false);
			addCellToTable(pdfTable,"" + violation.getLinenumber(), BaseColor.WHITE, false);
			addCellToTable(pdfTable,"" + report.getViolationUtil().getSeverityNameFromValue(violation.getSeverityValue()), BaseColor.WHITE, false);
			addCellToTable(pdfTable, MessageBuilder.getRuleMessage(violation.getClassPathFrom(), violation.getLogicalModuleFromType(), violation.getClassPathTo(), violation.getLogicalModuleToType(), violation.getRuletypeKey()), BaseColor.WHITE, false);
			addCellToTable(pdfTable, violation.getViolationtypeKey(), BaseColor.WHITE, false);
			addCellToTable(pdfTable, convertIsIndirectBooleanToString(violation.isIndirect()), BaseColor.WHITE, false);
		}

		document.add(pdfTable);
	}

	private void addCellToTable(PdfPTable table, String content, BaseColor color, boolean header) {

		Phrase phrase = new Phrase(content);
		PdfPCell cell = new PdfPCell(phrase);
		cell.setBorderWidth(1.5F);
		cell.setBackgroundColor(color);

		if(header) {
			phrase.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		}
		else {
			phrase.setFont(FontFactory.getFont(FontFactory.HELVETICA, 7));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		}
		table.addCell(cell);
	}


}
