package husacct.validate.task.report.writer;

import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.internal_transfer_objects.ViolationsPerSeverity;
import husacct.validate.domain.validation.report.Report;
import husacct.validate.task.extensiontypes.ExtensionTypes.ExtensionType;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
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
		super(report, path, fileName, ExtensionType.PDF);
	}

	@Override
	public void createReport() throws DocumentException, MalformedURLException, IOException {
		document = new Document();
		checkDirsExist();

		final String fileName = getFileName();
		PdfWriter.getInstance(document, new FileOutputStream(fileName));

		document.open();
		document.setPageSize(new Rectangle(1280, 600));
		document.newPage();

		Image image = Image.getInstance(report.getImagePath());
		image.setAlignment(Image.RIGHT | Image.TEXTWRAP);
		document.add(image);

		createApplicationInfo();
		createStatistics();
		createTable();
		document.close();

		File imageFile = new File(report.getImagePath());
		imageFile.delete();

	}

	private void createApplicationInfo() throws DocumentException {
		Phrase title = new Phrase();
		title.setFont(new Font(FontFamily.HELVETICA, 15F, Font.BOLD, BaseColor.BLUE));
		title.add("HUSACCT PDF REPORT");
		document.add(new Paragraph(title));

		document.add(new Paragraph("Generated on: " + getCurrentDate()));
		document.add(new Paragraph("Project: " + report.getProjectName()));
		document.add(new Paragraph("Version: " + report.getVersion()));

		document.add(new Paragraph(" "));
	}

	private void createStatistics() throws DocumentException {
		Phrase title = new Phrase();
		title.setFont(new Font(FontFamily.HELVETICA, 13F, Font.BOLD, BaseColor.BLUE));
		title.add("Statistics");
		document.add(new Paragraph(title));

		document.add(new Paragraph("Total violations: " + report.getViolations().getValue().size()));
		document.add(new Paragraph("Violations generated on: " + report.getFormattedDate()));
		document.add(new Paragraph(" "));
		List<ViolationsPerSeverity> violationsPerSeverity = report.getViolationsPerSeverity();
		if (violationsPerSeverity.isEmpty()) {
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
			document.add(Chunk.NEWLINE);
		} else {
			for (ViolationsPerSeverity violationPerSeverity : violationsPerSeverity) {
				document.add(new Paragraph(violationPerSeverity.getSeverity().getSeverityName() + ": " + violationPerSeverity.getAmount()));
			}
			for (int i = violationsPerSeverity.size(); i < 4; i++) {
				document.add(Chunk.NEWLINE);
			}
		}
	}

	private void createTable() throws DocumentException {
		Phrase title = new Phrase();
		title.setFont(new Font(FontFamily.HELVETICA, 13F, Font.BOLD, BaseColor.BLUE));
		title.add("Violations");
		document.add(new Paragraph(title));
		document.add(new Paragraph(" "));

		PdfPTable pdfTable = new PdfPTable(report.getLocaleColumnHeaders().length);
		pdfTable.setWidths(new int[] {3, 4, 1, 2, 2, 1});
		pdfTable.setWidthPercentage(100);

		for (String columnHeader : report.getLocaleColumnHeaders()) {
			addCellToTable(pdfTable, columnHeader, BaseColor.GRAY, true);
		}

		for (Violation violation : report.getViolations().getValue()) {
			// Source
			if (violation.getClassPathFrom() != null && !violation.getClassPathFrom().trim().equals("")) {
				addCellToTable(pdfTable, violation.getClassPathFrom(), BaseColor.WHITE, false);
			} else {
				addCellToTable(pdfTable, "", BaseColor.WHITE, false);
			}

			// Rule
			if (violation.getMessage() != null) {
				String message = new Messagebuilder().createMessage(violation.getMessage(), violation);
				addCellToTable(pdfTable, message, BaseColor.WHITE, false);
			} else {
				addCellToTable(pdfTable, "", BaseColor.WHITE, false);
			}

			// LineNumber
			if (!(violation.getLinenumber() == 0)) {
				addCellToTable(pdfTable, "" + violation.getLinenumber(), BaseColor.WHITE, false);
			} else {
				addCellToTable(pdfTable, "", BaseColor.WHITE, false);
			}

			// DependencyKind
			if (violation.getViolationtypeKey() != null) {
				addCellToTable(pdfTable, getDependencyKindValue(violation.getViolationtypeKey(), violation.isIndirect()), BaseColor.WHITE, false);
			} else {
				addCellToTable(pdfTable, "", BaseColor.WHITE, false);
			}
			// Target
			if (violation.getClassPathFrom() != null && !violation.getClassPathFrom().trim().equals("")) {
				addCellToTable(pdfTable, violation.getClassPathTo(), BaseColor.WHITE, false);
			} else {
				addCellToTable(pdfTable, "", BaseColor.WHITE, false);
			}

			// Severity
			if (violation.getSeverity() != null) {
				addCellToTable(pdfTable, "" + violation.getSeverity().getSeverityName(), BaseColor.WHITE, false);
			} else {
				addCellToTable(pdfTable, "", BaseColor.WHITE, false);
			}
		}

		document.add(pdfTable);
	}

	private void addCellToTable(PdfPTable table, String content, BaseColor color, boolean header) {

		Phrase phrase = new Phrase(content);
		PdfPCell cell = new PdfPCell(phrase);
		cell.setBorderWidth(1.5F);
		cell.setBackgroundColor(color);

		if (header) {
			phrase.setFont(FontFactory.getFont(FontFactory.HELVETICA_BOLD, 7));
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		} else {
			phrase.setFont(FontFactory.getFont(FontFactory.HELVETICA, 7));
			cell.setHorizontalAlignment(Element.ALIGN_LEFT);
		}
		table.addCell(cell);
	}
}