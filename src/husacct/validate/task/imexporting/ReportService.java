package husacct.validate.task.imexporting;

import husacct.common.enums.ExtensionTypes;
import husacct.validate.domain.exception.FileNotAccessibleException;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.TaskServiceImpl;
import husacct.validate.task.imexporting.reporting.ExportReportFactory;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.List;

import org.jdom2.Document;

public class ReportService {

	private final ExportReportFactory reportFactory;
	private final TaskServiceImpl taskServiceImpl;

	public ReportService(TaskServiceImpl taskServiceImpl) {
		this.reportFactory = new ExportReportFactory(taskServiceImpl);
		this.taskServiceImpl = taskServiceImpl;
	}

	public String[] getExportExtentions() {
		return ExtensionTypes.getExtensionTypes();
	}

	public void createReport(File file, String fileType) {
		try {
			if (file.createNewFile()) {
				reportFactory.exportReport(fileType, taskServiceImpl.getAllViolations(), file.getName(), file.getParent(), taskServiceImpl.getAllSeverities());
				return;
			}
		} catch (IOException e) {
			throw new FileNotAccessibleException(file);
		}
		throw new FileNotAccessibleException(file);
	}

	public void createReport(File file, String fileType, Calendar date) {
		try {
			if (file.createNewFile()) {
				ViolationHistory violationHistory = taskServiceImpl.getViolationHistoryByDate(date);
				reportFactory.exportReport(fileType, new SimpleEntry<Calendar, List<Violation>>(violationHistory.getDate(), violationHistory.getViolations()), file.getName(), file.getParent(), violationHistory.getSeverities());
				return;
			}
		} catch (IOException e) {
			throw new FileNotAccessibleException(file);
		}
		throw new FileNotAccessibleException(file);
	}

	public Document createAllViolationsXmlDocument(SimpleEntry<Calendar, List<Violation>> violations) {
		Document document = reportFactory.createAllViolationsXmlDocument(violations, taskServiceImpl.getAllSeverities());
		return document;
	}
}