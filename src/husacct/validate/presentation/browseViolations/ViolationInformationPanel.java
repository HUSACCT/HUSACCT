package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.task.TaskServiceImpl;

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

import org.apache.log4j.Logger;

public class ViolationInformationPanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 8505333261388149299L;
	private final TaskServiceImpl task;
	private JLabel detailLogicalModuleFromLabelValue, detailMessageLabelValue, detailLineNumberLabelValue, detailsLogicalModuleFromLabel, detailsLineNumberLabel, detailsMessageLabel, detailLogicalModuleToLabel, detailLogicalModuleToValue;
	private JButton detailShowErrorBtn;
	private String sourceClassPath = "";
	private int sourceLine = 1;
	private Severity sourceSeverity;
	private static Logger logger = Logger.getLogger(ServiceProvider.class);

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
		detailShowErrorBtn = new JButton();
		detailShowErrorBtn.addActionListener(this);

		GroupLayout gl_violationDetailPane = new GroupLayout(this);
		gl_violationDetailPane
			.setHorizontalGroup(
				gl_violationDetailPane
					.createParallelGroup(Alignment.LEADING)
					.addGroup(
						gl_violationDetailPane
							.createSequentialGroup()
							.addContainerGap()
							.addGroup(
								gl_violationDetailPane
									.createParallelGroup(Alignment.LEADING)
									.addComponent(detailsLogicalModuleFromLabel)
									.addComponent(detailLogicalModuleToLabel)
									.addComponent(detailsMessageLabel)
									.addComponent(detailShowErrorBtn)
									.addGroup(
										gl_violationDetailPane
											.createSequentialGroup()
											.addComponent(detailsLineNumberLabel)
											.addGap(53)
											.addGroup(
												gl_violationDetailPane
													.createParallelGroup(Alignment.LEADING)
													.addComponent(detailLogicalModuleFromLabelValue)
													.addComponent(detailLogicalModuleToValue)
													.addComponent(detailLineNumberLabelValue)
													.addComponent(detailMessageLabelValue)
											)
									)
							).addContainerGap(397, Short.MAX_VALUE)
					)
			);
		gl_violationDetailPane
			.setVerticalGroup(
				gl_violationDetailPane
					.createParallelGroup(Alignment.LEADING)
					.addGroup(
						gl_violationDetailPane
							.createSequentialGroup()
							.addContainerGap()
							.addGroup(
								gl_violationDetailPane
									.createParallelGroup(Alignment.BASELINE)
									.addComponent(detailsLineNumberLabel)
									.addComponent(detailLineNumberLabelValue)
							)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(
								gl_violationDetailPane
									.createParallelGroup(Alignment.BASELINE)
									.addComponent(detailsLogicalModuleFromLabel)
									.addComponent(detailLogicalModuleFromLabelValue)
							)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(
								gl_violationDetailPane
									.createParallelGroup(Alignment.BASELINE)
									.addComponent(detailLogicalModuleToLabel)
									.addComponent(detailLogicalModuleToValue)
							)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(
								gl_violationDetailPane
								.createParallelGroup(Alignment.BASELINE)
								.addComponent(detailsMessageLabel)
								.addComponent(detailMessageLabelValue)
							)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addGroup(
								gl_violationDetailPane
								.createParallelGroup(Alignment.BASELINE)
								.addComponent(detailShowErrorBtn)
							)
							.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					)
			);
		setLayout(gl_violationDetailPane);

	}

	public void loadGuiText() {
		setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Details")));
		detailsLineNumberLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("LineNumber"));
		detailsLogicalModuleFromLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("LogicalModuleFrom"));
		detailsMessageLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Message"));
		detailLogicalModuleToLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("LogicalModuleTo"));
		detailShowErrorBtn.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ShowCode"));
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

			//sourceClassPath = violation.getClassPathFrom();
			sourceClassPath = violation.getClassPathFrom();
			sourceSeverity = violation.getSeverity();
			sourceLine = violation.getLinenumber();
			
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

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(!sourceClassPath.equals("") && !sourceClassPath.equals(".java")){
			logger.info("Opening code viewer: " + sourceClassPath + " at line " + sourceLine);
			ServiceProvider.getInstance().getControlService().displayErrorInFile(sourceClassPath, sourceLine, sourceSeverity);
		}
	}
}
