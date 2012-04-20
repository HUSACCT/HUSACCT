package husacct.validate.task.report;

import husacct.define.DefineServiceStub;
import husacct.validate.abstraction.extensiontypes.ExtensionTypes.ExtensionType;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.report.Report;
import husacct.validate.task.report.writer.HTMLReportWriter;
import husacct.validate.task.report.writer.PDFReportWriter;
import husacct.validate.task.report.writer.ReportWriter;
import husacct.validate.task.report.writer.XMLReportWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;

public class ExportReportFactory {
	private ReportWriter writer;

	public void exportReport(String fileType, List<Violation> violations, String name, String path, List<Severity> severities) throws UnknownStorageTypeException, ParserConfigurationException, SAXException, IOException, DOMException, URISyntaxException, DocumentException, TransformerException {
		DefineServiceStub stub = new DefineServiceStub();
		
		Report report = new Report(stub.getApplicationDetails().name, "TODO Version", violations, path, severities);
		
		if(fileType.equals(ExtensionType.XML.getExtension())) {
			writer = new XMLReportWriter(report, path, name);
		} else if(fileType.equals(ExtensionType.HTML.getExtension())) {
			writer = new HTMLReportWriter(report, path, name);
		} else if(fileType.equals(ExtensionType.PDF.getExtension())) {
			writer = new PDFReportWriter(report, path, name);
		}
		if(writer == null) {
			throw new UnknownStorageTypeException("Storage type " + fileType + " doesn't exist or is not implemented");
		}
		writer.createReport();
	}
}
