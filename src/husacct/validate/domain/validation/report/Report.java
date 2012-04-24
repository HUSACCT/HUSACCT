package husacct.validate.domain.validation.report;

import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.iternal_tranfer_objects.ViolationsPerSeverity;
import husacct.validate.task.report.UnknownStorageTypeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Report {

	private String projectName;
	private String version;
	private List<Violation> violations;
	private List<Severity> severities;
	private String imagePath;

	public Report(String projectName, String version, List<Violation> violations, String path, List<Severity> severities) throws ParserConfigurationException, SAXException, IOException, UnknownStorageTypeException {
		this.projectName = projectName;
		this.version = version;
		this.violations = violations;
		this.imagePath = path + "/image.png";
		this.severities = severities;
		new StatisticsImage().createStatisticsImage(imagePath, getViolationsPerSeverity());
	}
	
	public List<ViolationsPerSeverity> getViolationsPerSeverity() {
		List<ViolationsPerSeverity> violationsPerSeverity = new ArrayList<ViolationsPerSeverity>();
		for(Severity severity : severities) {
			int violationsCount = 0;
			for(Violation violation : violations) {
				if(violation.getSeverityValue() == severity.getValue()) {
					violationsCount++;
				}
			}
			violationsPerSeverity.add(new ViolationsPerSeverity(violationsCount, severity));
		}
		return violationsPerSeverity;
	}

	public String[] getLocaleColumnHeaders() {
		String[] headers = new String[] {
		ResourceBundles.getValue("Source"),
		ResourceBundles.getValue("Target"),
		ResourceBundles.getValue("LineNumber"),
		ResourceBundles.getValue("Severity"),
		ResourceBundles.getValue("Rule"),
		ResourceBundles.getValue("DependencyKind"),
		""};
		return headers;
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

	public String getSeverityNameFromValue(int severityValue) {
		for(Severity severity : severities) {
			if(severity.getValue() == severityValue) {
				return severity.getDefaultName();
			}
		}
		throw new NullPointerException("Severity value was not found, please check if the configuration is correct");
	}

}
