package husacct.validate.task;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.task.extensiontypes.ExtensionTypes;
import husacct.validate.task.report.ExportReportFactory;

public class ReportServiceImpl{
	private final ConfigurationServiceImpl configuration;
	ExportReportFactory reportFactory; 

	public ReportServiceImpl(ConfigurationServiceImpl configuration){
		this.reportFactory = new ExportReportFactory();
		this.configuration = configuration;
	}
	
	public String[] getExportExtentions() {
		return new ExtensionTypes().getExtensionTypes();
	}

	public void createReport(String fileType, String name, String path) {
		reportFactory.exportReport(fileType, configuration.getAllViolations(), name, path, configuration.getAllSeverities());
	}
}