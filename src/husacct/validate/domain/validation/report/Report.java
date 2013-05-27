package husacct.validate.domain.validation.report;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.internaltransferobjects.ViolationsPerSeverity;

import java.text.SimpleDateFormat;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Report {

	private String projectName;
	private String version;
	private List<Severity> severities;
	private String imagePath;
	private SimpleEntry<Calendar, List<Violation>> violations;
	private final SimpleDateFormat dateFormat;
	private ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

	public Report(String projectName, String version, SimpleEntry<Calendar, List<Violation>> violations, String path, List<Severity> severities) {
		this.projectName = projectName;
		this.version = version;
		this.violations = violations;
		this.imagePath = path + "/image.png";
		this.severities = severities;
		new StatisticsImage().createStatisticsImage(imagePath, getViolationsPerSeverity());
		dateFormat = new SimpleDateFormat("dd-MM-yyyy kk:mm:ss");
	}

	public String getFormattedDate() {
		return dateFormat.format(violations.getKey().getTime());
	}

	public List<ViolationsPerSeverity> getViolationsPerSeverity() {
		List<ViolationsPerSeverity> violationsPerSeverity = new ArrayList<ViolationsPerSeverity>();
		for (Severity severity : severities) {
			int violationsCount = 0;
			for (Violation violation : violations.getValue()) {
				if (violation.getSeverity().getSeverityKey().equals(severity.getSeverityKey())) {
					violationsCount++;
				}
			}
			violationsPerSeverity.add(new ViolationsPerSeverity(violationsCount, severity));
		}
		return violationsPerSeverity;
	}

	public String[] getLocaleColumnHeaders() {
		String[] headers = new String[] {localeService.getTranslatedString("Source"), localeService.getTranslatedString("Rule"), localeService.getTranslatedString("LineNumber"), localeService.getTranslatedString("DependencyKind"), localeService.getTranslatedString("Target"), localeService.getTranslatedString("Severity")};
		return headers;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
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

	public List<Severity> getSeverities() {
		return severities;
	}

	public void setSeverities(List<Severity> severities) {
		this.severities = severities;
	}

	public SimpleEntry<Calendar, List<Violation>> getViolations() {
		return violations;
	}

	public void setViolations(SimpleEntry<Calendar, List<Violation>> violations) {
		this.violations = violations;
	}
}