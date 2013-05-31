package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;

import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class StatisticsPanel extends JPanel {
	private static final long serialVersionUID = -7741400148880504572L;
	private JLabel totalViolationLabel, totalViolationNumber, shownViolationsLabel, shownViolationsNumber, violationSeverityLow, violationSeverityMedium, violationSeverityHigh, violationSeverityLowTotal, violationSeverityMediumTotal, violationSeverityHighTotal;

	public StatisticsPanel() {
		initComponents();
	}

	private void initComponents() {
		totalViolationLabel = new JLabel();
		totalViolationNumber = new JLabel("0");
		shownViolationsLabel = new JLabel();
		shownViolationsNumber = new JLabel("0");
		violationSeverityLow = new JLabel("Low:");
		violationSeverityLowTotal = new JLabel("0");
		violationSeverityMedium = new JLabel("Medium:");
		violationSeverityMediumTotal = new JLabel("0");
		violationSeverityHigh = new JLabel("High:");
		violationSeverityHighTotal = new JLabel("0");
		
		add(totalViolationLabel);
		add(totalViolationNumber);
		add(shownViolationsLabel);
		add(shownViolationsNumber);
		add(violationSeverityLow);
		add(violationSeverityLowTotal);
		add(violationSeverityMedium);
		add(violationSeverityMediumTotal);
		add(violationSeverityHigh);
		add(violationSeverityHighTotal);

		createBaseLayout();
	}

	private void createBaseLayout() {
		setLayout(new GridLayout(0, 2));
	}

	public void loadAfterChange() {
		loadText();
	}

	private void loadText() {
		setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Information")));
		totalViolationLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("TotalViolations") + ":");
		shownViolationsLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ShownViolations") + ":");
	}

	public void loadStatistics(LinkedHashMap<Severity, Integer> severitiesCountPerSeverity, int totalSize, int shownSize) {
		totalViolationNumber.setText("" + totalSize);
		shownViolationsNumber.setText("" + shownSize);

		for (Entry<Severity, Integer> violationPerSeverity : severitiesCountPerSeverity.entrySet()) {
			if ((violationPerSeverity.getKey().toString()).equalsIgnoreCase("low")) {
				violationSeverityLowTotal.setText(violationPerSeverity.getValue().toString());
			} else if ((violationPerSeverity.getKey().toString()).equalsIgnoreCase("medium")) {
				violationSeverityMediumTotal.setText(violationPerSeverity.getValue().toString());
			} else if ((violationPerSeverity.getKey().toString()).equalsIgnoreCase("high")) {
				violationSeverityHighTotal.setText(violationPerSeverity.getValue().toString());
			}
		}
		revalidate();
		updateUI();
	}
}
