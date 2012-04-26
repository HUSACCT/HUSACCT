package husacct.validate.domain.validation.report;

import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.iternal_tranfer_objects.ViolationsPerSeverity;

import java.util.ArrayList;
import java.util.List;

public class Report {

	private String projectName;
	private String version;
	private List<Violation> violations;
	private List<Severity> severities;
	private String imagePath;

	public Report(String projectName, String version, List<Violation> violations, String path, List<Severity> severities) {
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
				if(violation.getSeverity().equals(severity)) {
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

}
