package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.common.dto.ModuleDTO;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.domain.validation.ruletype.RuleTypes;
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
				gl_violationDetailPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
						.addComponent(detailsLogicalModuleFromLabel, 160, 160, 160)
						.addComponent(detailLogicalModuleToLabel, 160, 160, 160)
						.addComponent(detailsMessageLabel, 160, 160, 160)
						.addComponent(detailsSeverityLabel, 160, 160, 160)
						.addComponent(detailShowSourceCodeBtn))
					.addGap(10)
					.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
							.addComponent(detailLogicalModuleFromValue)
							.addComponent(detailLogicalModuleToValue)
							.addComponent(detailMessageValue)
							.addComponent(detailSeverityValue)))
					.addGap(10));
		
		gl_violationDetailPane.setVerticalGroup(
			gl_violationDetailPane.createParallelGroup(Alignment.LEADING).addGroup(
				gl_violationDetailPane.createSequentialGroup()
				.addGap(3).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailsLogicalModuleFromLabel)
						.addComponent(detailLogicalModuleFromValue))
				.addGap(3).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailLogicalModuleToLabel)
						.addComponent(detailLogicalModuleToValue))
				.addGap(3).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailsMessageLabel)
						.addComponent(detailMessageValue))
				.addGap(3).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailsSeverityLabel)
						.addComponent(detailSeverityValue))
				.addGap(7).addGroup(
					gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(detailShowSourceCodeBtn))
				.addGap(5)));
		
		setLayout(gl_violationDetailPane);
	}

	public void loadGuiText() {
		setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Details")));
		detailsLogicalModuleFromLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("LogicalModuleFrom") + ":");
		detailLogicalModuleToLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("LogicalModuleTo") + ":");
		detailsMessageLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Message") + ":");
		detailsSeverityLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Severity") + ":");
		detailShowSourceCodeBtn.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ShowCode"));
	}

	public void update(ListSelectionEvent arg0, JTable violationsTable, List<Violation> shownViolations) {
		if (!arg0.getValueIsAdjusting() && violationsTable.getSelectedRow() > -1) {
			int row = violationsTable.convertRowIndexToModel(violationsTable.getSelectedRow());
			Violation violation = shownViolations.get(row);
			String logicalModulePathOfFromClass = "";
			String logicalModulePathOfToClass = "";
			if (violation.getRuletypeKey().toLowerCase().equals(RuleTypes.MUST_USE.toString().toLowerCase())) {
				// Get the logical modules of the rule since there are no classPaths
				logicalModulePathOfFromClass = violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath();
				logicalModulePathOfToClass = violation.getLogicalModules().getLogicalModuleTo().getLogicalModulePath();
			} else {
				if (violation.getClassPathFrom() != null && !violation.getClassPathFrom().equals("")) {
					ModuleDTO logicalModuleOfFromClass = ServiceProvider.getInstance().getDefineService().getModule_BasedOnSoftwareUnitName(violation.getClassPathFrom());
					if (logicalModuleOfFromClass != null) {
						logicalModulePathOfFromClass = logicalModuleOfFromClass.logicalPath;
					}
				}
				if (violation.getClassPathTo() != null && !violation.getClassPathTo().equals("")) {
					ModuleDTO logicalModuleOfToClass = ServiceProvider.getInstance().getDefineService().getModule_BasedOnSoftwareUnitName(violation.getClassPathTo());
					if (logicalModuleOfToClass != null) {
						logicalModulePathOfToClass = logicalModuleOfToClass.logicalPath;
					}
				}
			}
			detailLogicalModuleFromValue.setText(logicalModulePathOfFromClass);
			detailLogicalModuleToValue.setText(logicalModulePathOfToClass);
			detailMessageValue.setText(task.getMessage(violation));
			detailSeverityValue.setText("" + violation.getSeverity());
			// Set data needed in case the source code viewer is activated
			sourceClassPath = violation.getClassPathFrom();
			sourceLine = violation.getLinenumber();
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
