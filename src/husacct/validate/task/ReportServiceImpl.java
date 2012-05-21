package husacct.validate.task;

import husacct.validate.domain.exception.ViolationsNotFoundAtDateException;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.extensiontypes.ExtensionTypes;
import husacct.validate.task.report.ExportReportFactory;

import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.List;

public class ReportServiceImpl implements IReportService{
	ExportReportFactory reportFactory; 
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
	public void createReport(String fileType, String name, String path) {
		reportFactory.exportReport(fileType, taskServiceImpl.getAllViolations(), name, path, taskServiceImpl.getAllSeverities());
	}
	
	@Override
	public void createReport(String fileType, String name, String path,
			ViolationHistory violationHistory) {
		reportFactory.exportReport(fileType, new SimpleEntry<Calendar, List<Violation>>(violationHistory.getDate(), violationHistory.getViolations()), name, path, violationHistory.getSeverities());
		
	}

	public void createReport(File file, String fileType, Calendar date) {
		if(date == null) {
			reportFactory.exportReport(fileType, taskServiceImpl.getAllViolations(), file.getName(), file.getParent(), taskServiceImpl.getAllSeverities());
		} else {
			for(Calendar existingDate : taskServiceImpl.getViolationHistoryDates()) {
				if(existingDate.equals(date)) {
					ViolationHistory violationHistory = taskServiceImpl.getViolationHistoryByDate(date);
					reportFactory.exportReport(fileType, new SimpleEntry<Calendar, List<Violation>>(violationHistory.getDate(), violationHistory.getViolations()), file.getName(), file.getParent(), violationHistory.getSeverities());
					return;
				}
			}
			throw new ViolationsNotFoundAtDateException(date);
		}
	}
}