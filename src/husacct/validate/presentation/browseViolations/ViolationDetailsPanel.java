package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.task.TaskServiceImpl;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;

public class ViolationDetailsPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 8505333261388149299L;
	private final TaskServiceImpl task;
	private JLabel detailLogicalModuleFromValue, detailMessageValue, detailSeverityValue, detailsLogicalModuleFromLabel, detailsSeverityLabel, detailsMessageLabel, detailLogicalModuleToLabel, detailLogicalModuleToValue;
	private JButton detailShowSourceCodeBtn;
	private String sourceClassPath = "";
	private int sourceLine = 1;

	public ViolationDetailsPanel(TaskServiceImpl task) {
		this.task = task;
		createBaseLayout();
	}

	private void createBaseLayout() {
		detailsLogicalModuleFromLabel = new JLabel();
		detailLogicalModuleFromValue = new JLabel();
		detailsMessageLabel = new JLabel();
		detailMessageValue = new JLabel();
		detailLogicalModuleToLabel = new JLabel();
		detailLogicalModuleToValue = new JLabel();
		detailsSeverityLabel = new JLabel();
		detailSeverityValue = new JLabel();
		detailShowSourceCodeBtn = new JButton();
		detailShowSourceCodeBtn.addActionListener(this);

		GroupLayout gl_violationDetailPane = new GroupLayout(this);
		gl_violationDetailPane.setHorizontalGroup(
			gl_violationDetailPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_violationDetailPane.createSequentialGroup().addContainerGap().addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
						.addComponent(detailLogicalModuleToLabel)
						.addComponent(detailsMessageLabel)
						.addComponent(detailsSeverityLabel)
						.addComponent(detailShowSourceCodeBtn).addGroup(
					gl_violationDetailPane.createSequentialGroup().addComponent(detailsLogicalModuleFromLabel).addGap(53).addGroup(
						gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
							.addComponent(detailLogicalModuleFromValue)
							.addComponent(detailLogicalModuleToValue)
							.addComponent(detailMessageValue)
							.addComponent(detailSeverityValue))))
					.addContainerGap(397, Short.MAX_VALUE)));
		
		gl_violationDetailPane.setVerticalGroup(
			gl_violationDetailPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_violationDetailPane.createSequentialGroup().addContainerGap().addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailsLogicalModuleFromLabel)
						.addComponent(detailLogicalModuleFromValue))
				.addPreferredGap(ComponentPlacement.UNRELATED).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailLogicalModuleToLabel)
						.addComponent(detailLogicalModuleToValue))
				.addPreferredGap(ComponentPlacement.UNRELATED).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailsMessageLabel)
						.addComponent(detailMessageValue))
				.addPreferredGap(ComponentPlacement.UNRELATED).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailsSeverityLabel)
						.addComponent(detailSeverityValue))
				.addPreferredGap(ComponentPlacement.UNRELATED).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailShowSourceCodeBtn))
				.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		
		setLayout(gl_violationDetailPane);
	}

	public void loadGuiText() {
		setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Details")));
		detailsLogicalModuleFromLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("LogicalModuleFrom"));
		detailLogicalModuleToLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("LogicalModuleTo"));
		detailsMessageLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Message"));
		detailsSeverityLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Severity"));
		detailShowSourceCodeBtn.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ShowCode"));
	}

	public void update(ListSelectionEvent arg0, JTable violationsTable, List<Violation> shownViolations) {
		if (!arg0.getValueIsAdjusting() && violationsTable.getSelectedRow() > -1) {
			int row = violationsTable.convertRowIndexToModel(violationsTable.getSelectedRow());
			Violation violation = shownViolations.get(row);
			detailLogicalModuleFromValue.setText(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
			detailLogicalModuleToValue.setText(violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath());
			detailSeverityValue.setText("" + violation.getSeverity());

			sourceClassPath = violation.getClassPathFrom();
			sourceLine = violation.getLinenumber();
			
			String message = task.getMessage(violation.getMessage(), violation);
			detailMessageValue.setText(message);
		} else {
			detailSeverityValue.setText("");
			detailLogicalModuleFromValue.setText("");
			detailLogicalModuleToValue.setText("");
			detailMessageValue.setText("");
		}
		updateUI();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!sourceClassPath.equals("")){
			ServiceProvider.getInstance().getControlService().displayErrorInFile(sourceClassPath, sourceLine, new Severity("test", Color.ORANGE));
		}
	}
}
