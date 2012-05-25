package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.TaskServiceImpl;

import java.awt.GridLayout;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class StatisticsPanel extends JPanel{

	private static final long serialVersionUID = -7741400148880504572L;

	private JLabel totalViolationLabel, totalViolationNumber, shownViolationsLabel, shownViolationsNumber;

	public StatisticsPanel() {
		createBaseLayout();
	}

	public void loadGuiText() {
		setBorder(new TitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Information")));
	}

	public void createBaseLayout() {
		setLayout(new GridLayout(0, 2));
		setBorder(new TitledBorder(null, "Information panel", TitledBorder.LEADING, TitledBorder.TOP, null, null));


		totalViolationLabel = new JLabel();
		totalViolationLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("TotalViolations") + ":");
		add(totalViolationLabel);

		totalViolationNumber = new JLabel();
		add(totalViolationNumber);

		shownViolationsLabel = new JLabel();
		shownViolationsLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("ShownViolations") + ":");
		add(shownViolationsLabel);

		shownViolationsNumber = new JLabel();
		add(shownViolationsNumber);
	}

	public void update(ViolationHistory selectedViolationHistory, List<Violation> shownViolations, TaskServiceImpl taskServiceImpl, JCheckBox applyFilter) {
		removeAll();
		if(selectedViolationHistory == null)
			totalViolationNumber.setText("" + taskServiceImpl.getAllViolations().getValue().size());
		else 
			totalViolationNumber.setText("" + selectedViolationHistory.getViolations().size());

		shownViolationsNumber.setText("" + shownViolations.size());

		add(totalViolationLabel);
		add(totalViolationNumber);

		add(shownViolationsLabel);
		add(shownViolationsNumber);

		for(Entry<Severity, Integer> violationPerSeverity: taskServiceImpl.getViolationsPerSeverity(selectedViolationHistory, shownViolations, applyFilter.isSelected()).entrySet()) {
			add(new JLabel(violationPerSeverity.getKey().toString()));
			add(new JLabel("" + violationPerSeverity.getValue()));
		}
		updateUI();
	}
}
