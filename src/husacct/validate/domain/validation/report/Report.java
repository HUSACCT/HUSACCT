package husacct.validate.domain.validation.report;

import husacct.validate.abstraction.fetch.xml.ImportSeverities;
import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.iternal_tranfer_objects.ViolationsPerSeverity;
import husacct.validate.task.report.UnknownStorageTypeException;

import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Report {

	private String projectName;
	private String version;
	private List<Violation> violations;
	private List<Severity> severities;
	private String imagePath;
	private ImportSeverities importConfiguration;

	public Report(String projectName, String version, List<Violation> violations, String path, List<Severity> severities) throws ParserConfigurationException, SAXException, IOException, UnknownStorageTypeException {
		this.projectName = projectName;
		this.version = version;
		this.violations = violations;
		this.importConfiguration = new ImportSeverities();
		this.imagePath = path + "/image.png";
		this.severities = severities;
		new StatisticsImage().createStatisticsImage(imagePath, importConfiguration.getSeveritiesPerViolation(violations, severities));
	}

	public String[] getLocaleColumnHeaders() {
		String[] headers = new String[7];
		headers[0] = ResourceBundles.getValue("Source");
		headers[1] = ResourceBundles.getValue("Target");
		headers[2] = ResourceBundles.getValue("LineNr");
		headers[3] = ResourceBundles.getValue("Severity");
		headers[4] = ResourceBundles.getValue("Rule");
		headers[5] = ResourceBundles.getValue("DependencyKind");
		headers[6] = "";
		return headers;
	}

	public List<ViolationsPerSeverity> getSeveritiesPerViolation(List<Violation> violations) throws SAXException, IOException, ParserConfigurationException {
		return importConfiguration.getSeveritiesPerViolation(violations, severities);
	}
	public String getSeverityNameFromValue(int value) throws SAXException, IOException, ParserConfigurationException {
		return importConfiguration.getSeverityNameFromValue(value, severities);
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

	public void setVersion(String version) {
		this.version = version;
	}
	public String getVersion() {
		return version;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}


}
