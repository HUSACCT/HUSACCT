package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.task.TaskServiceImpl;

import java.awt.GridLayout;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

public class StatisticsPanel extends JPanel{
	
	private final TaskServiceImpl taskServiceImpl;

	private static final long serialVersionUID = -7741400148880504572L;

	private JLabel totalViolationLabel, totalViolationNumber, shownViolationsLabel, shownViolationsNumber;

	public StatisticsPanel(TaskServiceImpl taskServiceImpl) {
		this.taskServiceImpl= taskServiceImpl;
		
		inintComponents();
	}

	private void inintComponents() {
		totalViolationLabel = new JLabel();
		totalViolationNumber = new JLabel();
		shownViolationsLabel = new JLabel();
		shownViolationsNumber = new JLabel();

		add(totalViolationLabel);
		add(totalViolationNumber);
		add(shownViolationsLabel);
		add(shownViolationsNumber);
		
		createBaseLayout();
	}
	
	private void createBaseLayout(){
		setLayout(new GridLayout(0, 2));
	}
	
	public void loadAfterChange(){
		loadText();
	}
	
	private void loadText() {
		setBorder(new TitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Information")));
		totalViolationLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("TotalViolations") + ":");
		shownViolationsLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("ShownViolations") + ":");
	}
	
	public void loadStatistics(LinkedHashMap<Severity, Integer> violationHistory, int totalSize, int shownSize){
		removeAll();
		
		totalViolationNumber.setText("" + totalSize);

		shownViolationsNumber.setText("" + shownSize);

		add(totalViolationLabel);
		add(totalViolationNumber);

		add(shownViolationsLabel);
		add(shownViolationsNumber);

		for(Entry<Severity, Integer> violationPerSeverity: violationHistory.entrySet()){
			add(new JLabel(violationPerSeverity.getKey().toString()));
			add(new JLabel("" + violationPerSeverity.getValue()));
		}
	}
}
