package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import javax.swing.GroupLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.GroupLayout.Alignment;
import javax.swing.border.TitledBorder;

public class StatisticsPanel extends JPanel {
	private static final long serialVersionUID = -7741400148880504572L;
	private JLabel totalViolationLabel, totalViolationNumber, shownViolationsLabel, shownViolationsNumber, violationSeverity, violationSeverityTotals;

	public StatisticsPanel() {
		createBaseLayout();
	}

	private void createBaseLayout() {
		totalViolationLabel = new JLabel();
		totalViolationNumber = new JLabel("0");
		shownViolationsLabel = new JLabel();
		shownViolationsNumber = new JLabel("0");
		violationSeverity = new JLabel("Severity (Low/Medium/High):");
		violationSeverityTotals = new JLabel("0");
		
		GroupLayout gl_violationDetailPane = new GroupLayout(this);
		gl_violationDetailPane.setHorizontalGroup(
			gl_violationDetailPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_violationDetailPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
						.addComponent(totalViolationLabel, 160, 160, 160)
						.addComponent(shownViolationsLabel, 160, 160, 160)
						.addComponent(violationSeverity, 160, 160, 160))
					.addGap(10)
					.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
							.addComponent(totalViolationNumber)
							.addComponent(shownViolationsNumber)
							.addComponent(violationSeverityTotals))
					.addGap(10)));
		
		gl_violationDetailPane.setVerticalGroup(
			gl_violationDetailPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_violationDetailPane.createSequentialGroup()
				.addGap(3).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(totalViolationLabel)
						.addComponent(totalViolationNumber))
				.addGap(3).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(shownViolationsLabel)
						.addComponent(shownViolationsNumber))
				.addGap(3).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(violationSeverity)
						.addComponent(violationSeverityTotals))
				.addGap(3)));
		
		setLayout(gl_violationDetailPane);
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
		int low = 0;
		int medium = 0;
		int high = 0;
		for (Entry<Severity, Integer> violationsPerSeverity : severitiesCountPerSeverity.entrySet()) {
			if ((violationsPerSeverity.getKey().toString()).equalsIgnoreCase("low")) {
				low = violationsPerSeverity.getValue();
			} else if ((violationsPerSeverity.getKey().toString()).equalsIgnoreCase("medium")) {
				medium = violationsPerSeverity.getValue();
			} else if ((violationsPerSeverity.getKey().toString()).equalsIgnoreCase("high")) {
				high = violationsPerSeverity.getValue();
			}
		}
		violationSeverityTotals.setText((String) (low + " / " + medium + " / " + high));
		revalidate();
		updateUI();
	}
}
