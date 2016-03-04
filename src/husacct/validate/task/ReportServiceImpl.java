package husacct.validate.task;

import husacct.validate.domain.exception.FileNotAccessibleException;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.extensiontypes.ExtensionTypes;
import husacct.validate.task.report.ExportReportFactory;

import java.io.File;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.List;

public class ReportServiceImpl implements IReportService {

	private final ExportReportFactory reportFactory;
	private final TaskServiceImpl taskServiceImpl;

	public ReportServiceImpl(TaskServiceImpl taskServiceImpl) {
		this.reportFactory = new ExportReportFactory(taskServiceImpl);
		this.taskServiceImpl = taskServiceImpl;
	}

	@Override
	public String[] getExportExtentions() {
		return new ExtensionTypes().getExtensionTypes();
	}

	@Override
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

	@Override
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
}