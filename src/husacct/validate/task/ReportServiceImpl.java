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

	public void createReport(String fileType, String name, String path) {
		ExportReportFactory reportFactory = new ExportReportFactory();
		try {
			reportFactory.exportReport(fileType, configuration.getAllViolations(), name, path, configuration.getAllSeverities());
		} catch (UnknownStorageTypeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (URISyntaxException e) {
			e.printStackTrace();
		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}
}
