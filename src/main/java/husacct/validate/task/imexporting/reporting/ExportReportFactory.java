package husacct.validate.task.imexporting.reporting;

import husacct.ServiceProvider;
import husacct.common.dto.ApplicationDTO;
import husacct.common.enums.ExtensionTypes;
import husacct.define.IDefineService;
import husacct.validate.domain.exception.ReportException;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.task.TaskServiceImpl;
import husacct.validate.task.imexporting.UnknownStorageTypeException;
import husacct.validate.task.imexporting.exporting.ExportViolations;

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
			if (fileType.toLowerCase().equals(ExtensionTypes.XML.getExtension().toLowerCase())) {
				writer = new ExportViolations(report, path, name, taskServiceImpl);
			} else if (fileType.toLowerCase().equals(ExtensionTypes.XLS.getExtension().toLowerCase())) {
				writer = new ExcelReportWriter(report, path, name, taskServiceImpl);
			} else if (fileType.toLowerCase().equals(ExtensionTypes.HTML.getExtension().toLowerCase())) {
				writer = new HTMLReportWriter(report, path, name, taskServiceImpl);
			} else if (fileType.toLowerCase().equals(ExtensionTypes.PDF.getExtension().toLowerCase())) {
				writer = new PDFReportWriter(report, path, name, taskServiceImpl);
			}
			if (writer == null) {
				throw new UnknownStorageTypeException("Storage type " + fileType + " doesn't exist or is not implemented");
			}

			writer.createReport();
		} catch (IOException | DocumentException | URISyntaxException | UnknownStorageTypeException e) {
			createException(e);
		}
    }

	private void createException(Exception exception) {
		throw new ReportException(exception.getMessage(), exception);
	}
}