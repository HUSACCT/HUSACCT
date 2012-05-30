package husacct.validate.task;

import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.extensiontypes.ExtensionTypes;
import husacct.validate.task.report.ExportReportFactory;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.List;

public class ReportServiceImpl implements IReportService{
	private final ExportReportFactory reportFactory; 
	private final TaskServiceImpl taskServiceImpl;

	public ReportServiceImpl(TaskServiceImpl taskServiceImpl){
		this.reportFactory = new ExportReportFactory();
		this.taskServiceImpl = taskServiceImpl;
	}

	@Override
	public String[] getExportExtentions() {
		return new ExtensionTypes().getExtensionTypes();
	}

	@Override
	public void createReport(File file, String fileType){
		reportFactory.exportReport(fileType, taskServiceImpl.getAllViolations(), file.getName(), file.getParent(), taskServiceImpl.getAllSeverities());
	}

	@Override
	public void createReport(File file, String fileType, Calendar date) {
		ViolationHistory violationHistory = taskServiceImpl.getViolationHistoryByDate(date);
		reportFactory.exportReport(fileType, new SimpleEntry<Calendar, List<Violation>>(violationHistory.getDate(), violationHistory.getViolations()), file.getName(), file.getParent(), violationHistory.getSeverities());
	}
}