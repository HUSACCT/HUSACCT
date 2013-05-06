package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Violation;
import husacct.validate.task.TaskServiceImpl;

import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;

public class ViolationInformationPanel extends JPanel {

	private static final long serialVersionUID = 8505333261388149299L;
	private final TaskServiceImpl task;
	private JLabel detailLogicalModuleFromLabelValue, detailMessageLabelValue, detailLineNumberLabelValue, detailsLogicalModuleFromLabel, detailsLineNumberLabel, detailsMessageLabel, detailLogicalModuleToLabel, detailLogicalModuleToValue;

	public ViolationInformationPanel(TaskServiceImpl task) {
		this.task = task;
		createBaseLayout();
	}

	private void createBaseLayout() {
		detailsLineNumberLabel = new JLabel();
		detailsLogicalModuleFromLabel = new JLabel();
		detailsMessageLabel = new JLabel();
		detailLineNumberLabelValue = new JLabel();
		detailLogicalModuleFromLabelValue = new JLabel();
		detailMessageLabelValue = new JLabel();
		detailLogicalModuleToLabel = new JLabel();
		detailLogicalModuleToValue = new JLabel();

		GroupLayout gl_violationDetailPane = new GroupLayout(this);
		gl_violationDetailPane.setHorizontalGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING).addGroup(gl_violationDetailPane.createSequentialGroup().addContainerGap().addGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING).addComponent(detailsLogicalModuleFromLabel).addComponent(detailLogicalModuleToLabel).addComponent(detailsMessageLabel).addGroup(gl_violationDetailPane.createSequentialGroup().addComponent(detailsLineNumberLabel).addGap(53).addGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING).addComponent(detailLogicalModuleFromLabelValue).addComponent(detailLogicalModuleToValue).addComponent(detailLineNumberLabelValue).addComponent(detailMessageLabelValue)))).addContainerGap(397, Short.MAX_VALUE)));
		gl_violationDetailPane.setVerticalGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING).addGroup(gl_violationDetailPane.createSequentialGroup().addContainerGap().addGroup(gl_violationDetailPane.createParallelGroup(Alignment.BASELINE).addComponent(detailsLineNumberLabel).addComponent(detailLineNumberLabelValue)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_violationDetailPane.createParallelGroup(Alignment.BASELINE).addComponent(detailsLogicalModuleFromLabel).addComponent(detailLogicalModuleFromLabelValue)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_violationDetailPane.createParallelGroup(Alignment.BASELINE).addComponent(detailLogicalModuleToLabel).addComponent(detailLogicalModuleToValue)).addPreferredGap(ComponentPlacement.UNRELATED).addGroup(gl_violationDetailPane.createParallelGroup(Alignment.BASELINE).addComponent(detailsMessageLabel).addComponent(detailMessageLabelValue)).addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		setLayout(gl_violationDetailPane);

	}

	public void loadGuiText() {
		setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Details")));
		detailsLineNumberLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("LineNumber"));
		detailsLogicalModuleFromLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("LogicalModuleFrom"));
		detailsMessageLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Message"));
		detailLogicalModuleToLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("LogicalModuleTo"));
	}

	public void update(ListSelectionEvent arg0, JTable violationsTable, List<Violation> shownViolations) {
		if (!arg0.getValueIsAdjusting() && violationsTable.getSelectedRow() > -1) {
			int row = violationsTable.convertRowIndexToModel(violationsTable.getSelectedRow());
			Violation violation = shownViolations.get(row);
			if (violation.getLinenumber() <= 0) {
				detailLineNumberLabelValue.setText("");
			} else {
				detailLineNumberLabelValue.setText("" + violation.getLinenumber());
			}
			detailLogicalModuleFromLabelValue.setText(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
			detailLogicalModuleToValue.setText(violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath());
			String message = task.getMessage(violation.getMessage(), violation);
			detailMessageLabelValue.setText(message);
		} else {
			detailLineNumberLabelValue.setText("");
			detailLogicalModuleFromLabelValue.setText("");
			detailLogicalModuleToValue.setText("");
			detailMessageLabelValue.setText("");
		}
		updateUI();
	}
}
