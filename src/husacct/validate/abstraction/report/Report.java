package husacct.validate.abstraction.report;

import husacct.validate.abstraction.fetch.UnknownStorageTypeException;
import husacct.validate.domain.violation.Violation;
import husacct.validate.task.ViolationUtil;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Report {

	private String projectName;
	private String version;
	private List<Violation> violations;
	private String[] columnHeaders = {"Source", "Target", "Line Nr.","Severity","Rule","Dependency Kind",""};
	private ViolationUtil violationUtil;
	private String imagePath;

	public Report(String projectName, String version, List<Violation> violations, String path) throws ParserConfigurationException, SAXException, IOException, UnknownStorageTypeException {
		this.projectName = projectName;
		this.version = version;
		this.violations = violations;
		this.violationUtil = new ViolationUtil();
		imagePath = path;
		new StatisticsImage().createStatisticsImage(path, violationUtil.getSeveritiesPerViolation(violations));
	}
	
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectName() {
		return projectName;
	}

	public void setViolations(List<Violation> violations) {
		this.violations = violations;
	}
	public List<Violation> getViolations() {
		return violations;
	}
	public void setColumnHeaders(String[] columnHeaders) {
		this.columnHeaders = columnHeaders;
	}
	public String[] getColumnHeaders() {
		return columnHeaders;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersion() {
		return version;
	}


	public ViolationUtil getViolationUtil() {
		return violationUtil;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}
	

}
