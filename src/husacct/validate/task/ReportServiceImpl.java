package husacct.validate.task;

import husacct.validate.domain.configuration.ConfigurationServiceImpl;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.extensiontypes.ExtensionTypes;
import husacct.validate.task.report.ExportReportFactory;

import java.util.AbstractMap.SimpleEntry;
import java.util.Calendar;
import java.util.List;

public class ReportServiceImpl implements IReportService{
	private final ConfigurationServiceImpl configuration;
	ExportReportFactory reportFactory; 

	public ReportServiceImpl(ConfigurationServiceImpl configuration){
		this.reportFactory = new ExportReportFactory();
		this.configuration = configuration;
	}
	
	@Override
	public String[] getExportExtentions() {
		return new ExtensionTypes().getExtensionTypes();
	}
	
	@Override
	public void createReport(String fileType, String name, String path) {
		reportFactory.exportReport(fileType, configuration.getAllViolations(), name, path, configuration.getAllSeverities());
	}
	
	@Override
	public void createReport(String fileType, String name, String path,
			ViolationHistory violationHistory) {
		reportFactory.exportReport(fileType, new SimpleEntry<Calendar, List<Violation>>(violationHistory.getDate(), violationHistory.getViolations()), name, path, violationHistory.getSeverities());
		
	}
}