package husacct.validate.task;

import husacct.validate.domain.ConfigurationServiceImpl;
import husacct.validate.task.report.ExportReportFactory;
import husacct.validate.task.report.UnknownStorageTypeException;

import java.io.IOException;
import java.net.URISyntaxException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

import com.itextpdf.text.DocumentException;

public class ReportServiceImpl{
	private final ConfigurationServiceImpl configuration;
	
	public ReportServiceImpl(ConfigurationServiceImpl configuration){
		this.configuration = configuration;
	}

	public void createReport(String fileType, String name, String path) throws DOMException, UnknownStorageTypeException, ParserConfigurationException, SAXException, IOException, URISyntaxException, DocumentException, TransformerException{
		ExportReportFactory reportFactory = new ExportReportFactory();
		reportFactory.exportReport(fileType, configuration.getAllViolations(), name, path, configuration.getAllSeverities());
	}
}
