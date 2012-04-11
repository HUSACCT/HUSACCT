package husacct.validate.abstraction.export;

import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;

import husacct.define.DefineServiceStub;

import husacct.validate.abstraction.fetch.UnknownStorageTypeException;
import husacct.validate.abstraction.report.ConstructReport;
import husacct.validate.abstraction.report.HTMLReportWriter;
import husacct.validate.abstraction.report.PDFReportWriter;
import husacct.validate.abstraction.report.Report;
import husacct.validate.abstraction.report.ReportWriter;
import husacct.validate.abstraction.report.XMLReportWriter;
import husacct.validate.domain.violation.Violation;


public class ExportReportFactory {
	private ReportWriter writer;

	public void exportReport(String fileType, List<Violation> violations, String name, String path) throws ParserConfigurationException, SAXException, IOException, UnknownStorageTypeException, DOMException, DocumentException, JAXBException, TransformerException {
		DefineServiceStub stub = new DefineServiceStub();
		ConstructReport constructReport = new ConstructReport();
		Report report = constructReport.generateReport(stub.getApplicationDetails().name, "TODO Version", violations , path);
		if(fileType.equals("xml")) {
			writer = new XMLReportWriter(report, path, name);
		} else if(fileType.equals("html")) {
			writer = new HTMLReportWriter(report, path, name);
		} else if(fileType.equals("pdf")) {
			writer = new PDFReportWriter(report, path, name);
		}
		if(writer == null) {
			throw new UnknownStorageTypeException("Storage type " + fileType + " doesn't exist or is not implemented");
		}
		writer.createReport();
	}
}
