package husacct.validate.task;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.task.report.ExportReportFactory;
import husacct.validate.task.report.UnknownStorageTypeException;

import java.io.IOException;
import java.net.URISyntaxException;

import com.itextpdf.text.DocumentException;

public class ReportServiceImpl{
	private final ConfigurationServiceImpl configuration;
	
	public ReportServiceImpl(ConfigurationServiceImpl configuration){
		this.configuration = configuration;
	}

	public void createReport(String fileType, String name, String path) throws UnknownStorageTypeException, IOException, URISyntaxException, DocumentException {
		ExportReportFactory reportFactory = new ExportReportFactory();
		reportFactory.exportReport(fileType, configuration.getAllViolations(), name, path, configuration.getAllSeverities());
	}
}
