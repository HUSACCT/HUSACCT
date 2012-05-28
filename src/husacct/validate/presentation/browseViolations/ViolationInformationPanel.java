package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.validate.domain.factory.message.Messagebuilder;
import husacct.validate.domain.validation.Violation;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

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

	private JLabel detailLogicalModuleLabelValue, detailMessageLabelValue, detailLineNumberLabelValue, 
					detailsLogicalModuleLabel, detailsLineNumberLabel, detailsMessageLabel;
	private JTable violationsTable;
	private List<Violation> shownViolations;
	private ListSelectionEvent arg0;

	public ViolationInformationPanel() {
		createBaseLayout();
	}

	private void createBaseLayout() {
		detailsLineNumberLabel = new JLabel();
		detailsLogicalModuleLabel = new JLabel();
		detailsMessageLabel = new JLabel();
		detailLineNumberLabelValue = new JLabel();
		detailLogicalModuleLabelValue = new JLabel();
		detailMessageLabelValue = new JLabel();
		GroupLayout gl_violationDetailPane = new GroupLayout(this);
		gl_violationDetailPane.setHorizontalGroup(
				gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_violationDetailPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
								.addComponent(detailsLogicalModuleLabel)
								.addComponent(detailsMessageLabel)
								.addGroup(gl_violationDetailPane.createSequentialGroup()
										.addComponent(detailsLineNumberLabel)
										.addGap(53)
										.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
												.addComponent(detailLogicalModuleLabelValue)
												.addComponent(detailLineNumberLabelValue)
												.addComponent(detailMessageLabelValue))))
												.addContainerGap(397, Short.MAX_VALUE))
		);
		gl_violationDetailPane.setVerticalGroup(
				gl_violationDetailPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_violationDetailPane.createSequentialGroup()
						.addContainerGap()
						.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(detailsLineNumberLabel)
								.addComponent(detailLineNumberLabelValue))
								.addPreferredGap(ComponentPlacement.UNRELATED)
								.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
										.addComponent(detailsLogicalModuleLabel)
										.addComponent(detailLogicalModuleLabelValue))
										.addPreferredGap(ComponentPlacement.UNRELATED)
										.addGroup(gl_violationDetailPane.createParallelGroup(Alignment.BASELINE)
												.addComponent(detailsMessageLabel)
												.addComponent(detailMessageLabelValue))
												.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
		);
		setLayout(gl_violationDetailPane);
		
		addComponentListener(new ComponentListener() {  
			@Override
				public void componentResized(ComponentEvent evt) {
					if(arg0 != null){
						update(arg0, violationsTable, shownViolations);
					}
				}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
	}

	public void loadGuiText() {
		setBorder(new TitledBorder(ServiceProvider.getInstance().getControlService().getTranslatedString("Details")));
		detailsLineNumberLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("LineNumber"));
		detailsLogicalModuleLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("LogicalModule"));
		detailsMessageLabel.setText(ServiceProvider.getInstance().getControlService().getTranslatedString("Message"));
	}

	public void update(ListSelectionEvent arg0, JTable violationsTable, List<Violation> shownViolations) {
		this.arg0 = arg0;
		this.violationsTable = violationsTable;
		this.shownViolations = shownViolations;
		if(!arg0.getValueIsAdjusting() && violationsTable.getSelectedRow() > -1) {
			int row = violationsTable.convertRowIndexToModel(violationsTable.getSelectedRow());
			Violation violation = shownViolations.get(row);
			detailLineNumberLabelValue.setText("" + violation.getLinenumber());
			detailLogicalModuleLabelValue.setText(violation.getLogicalModules().getLogicalModuleFrom().getLogicalModulePath());
			String message = new Messagebuilder().createMessage(violation.getMessage());
			detailMessageLabelValue.setText(message);
		} else {
			detailLineNumberLabelValue.setText("");
			detailLogicalModuleLabelValue.setText("");
			detailMessageLabelValue.setText("");
		}
	}

}
