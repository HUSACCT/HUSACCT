package husacct.define.presentation.jpanel;

import husacct.define.presentation.tables.JTableAppliedRule;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AppliedRulesJPanel extends AbstractDefinitionJPanel {
	
	private static final long serialVersionUID = -2052083182258803790L;

	public AppliedRulesJPanel() {
		super();
	}

	@Override
	public void initGui() {
		BorderLayout appliedRulesPanelLayout = new BorderLayout();
		this.setLayout(appliedRulesPanelLayout);
		this.setBorder(BorderFactory.createTitledBorder("Applied rules for this layer"));
		this.add(this.addAppliedRulesTable(), BorderLayout.CENTER);
		this.add(this.addButtonPanel(), BorderLayout.EAST);
	}
	
	private JScrollPane addAppliedRulesTable() {
		JScrollPane appliedRulesPane = new JScrollPane();
		appliedRulesPane.setPreferredSize(new java.awt.Dimension(278, 32));
		JTableAppliedRule appliedRulesTable = new JTableAppliedRule();
		appliedRulesPane.setViewportView(appliedRulesTable);
		return appliedRulesPane;
	}
	
	@Override
	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(this.createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 4));
		buttonPanel.setPreferredSize(new java.awt.Dimension(78, 156));
		
		JButton addRuleButton = new JButton();
		buttonPanel.add(addRuleButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		addRuleButton.setText("Add");
		
		JButton editRuleButton = new JButton();
		buttonPanel.add(editRuleButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		editRuleButton.setText("Edit");
			
		JButton removeRuleButton = new JButton();
		buttonPanel.add(removeRuleButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		removeRuleButton.setText("Remove");
		
		return buttonPanel;
	}
	
	private GridBagLayout createButtonPanelLayout() {
		GridBagLayout buttonPanelLayout = new GridBagLayout();
		buttonPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
		buttonPanelLayout.rowHeights = new int[] { 0, 11, 7 };
		buttonPanelLayout.columnWeights = new double[] { 0.1 };
		buttonPanelLayout.columnWidths = new int[] { 7 };
		return buttonPanelLayout;
	}
}
