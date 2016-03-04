package husacct.validate.task.report;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.define.IDefineService;
import husacct.validate.domain.exception.ReportException;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.report.Report;
import husacct.validate.task.TaskServiceImpl;
import husacct.validate.task.extensiontypes.ExtensionTypes.ExtensionType;
import husacct.validate.task.report.writer.ExcelReportWriter;
import husacct.validate.task.report.writer.HTMLReportWriter;
import husacct.validate.task.report.writer.PDFReportWriter;
import husacct.validate.task.report.writer.ReportWriter;
import husacct.validate.task.report.writer.XMLReportWriter;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.List;

import com.itextpdf.text.DocumentException;

public class ExportReportFactory {

	private final TaskServiceImpl taskServiceImpl;
	private ReportWriter writer;
	private final IDefineService defineService = ServiceProvider.getInstance().getDefineService();

	public ExportReportFactory(TaskServiceImpl taskServiceImpl) {
		this.taskServiceImpl = taskServiceImpl;
	}

	public void exportReport(String fileType, SimpleEntry<Calendar, List<Violation>> violations, String name, String path, List<Severity> severities) {
		final ApplicationDTO applicationDetails = defineService.getApplicationDetails();
		Report report = new Report(applicationDetails.name, applicationDetails.version, violations, path, severities);

		try {
			if (fileType.toLowerCase().equals(ExtensionType.XML.getExtension().toLowerCase())) {
				writer = new XMLReportWriter(report, path, name);
			} else if (fileType.toLowerCase().equals(ExtensionType.XLS.getExtension().toLowerCase())) {
				writer = new ExcelReportWriter(report, path, name, taskServiceImpl);
			} else if (fileType.toLowerCase().equals(ExtensionType.HTML.getExtension().toLowerCase())) {
				writer = new HTMLReportWriter(report, path, name);
			} else if (fileType.toLowerCase().equals(ExtensionType.PDF.getExtension().toLowerCase())) {
				writer = new PDFReportWriter(report, path, name);
			}
			if (writer == null) {
				throw new UnknownStorageTypeException("Storage type " + fileType + " doesn't exist or is not implemented");
			}

			writer.createReport();
		} catch (IOException e) {
			createException(e);
		} catch (UnknownStorageTypeException e) {
			createException(e);
		} catch (URISyntaxException e) {
			createException(e);
		} catch (DocumentException e) {
			createException(e);
		}
	}

	private void createException(Exception exception) {
		throw new ReportException(exception.getMessage(), exception);
	}
}