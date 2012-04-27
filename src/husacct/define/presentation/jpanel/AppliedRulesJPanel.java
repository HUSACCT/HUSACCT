package husacct.define.presentation.jpanel;

import husacct.define.presentation.tables.JTableAppliedRule;
import husacct.define.task.DefinitionController;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public class AppliedRulesJPanel extends AbstractDefinitionJPanel  implements ActionListener,  Observer{
	
	private static final long serialVersionUID = -2052083182258803790L;
	private JTableAppliedRule appliedRulesTable;
	private JScrollPane appliedRulesPane;
	
	private JButton addRuleButton;
	private JButton editRuleButton;
	private JButton removeRuleButton;

	public AppliedRulesJPanel() {
		super();
	}

	/**
	 * Creating Gui
	 */
	@Override
	public void initGui() {
		DefinitionController.getInstance().addObserver(this);
		BorderLayout appliedRulesPanelLayout = new BorderLayout();
		this.setLayout(appliedRulesPanelLayout);
		this.setBorder(BorderFactory.createTitledBorder("Applied rules for this layer"));
		this.add(this.addAppliedRulesTable(), BorderLayout.CENTER);
		this.add(this.addButtonPanel(), BorderLayout.EAST);
	}
	
	private JScrollPane addAppliedRulesTable() {
		appliedRulesPane = new JScrollPane();
		appliedRulesPane.setPreferredSize(new java.awt.Dimension(278, 32));
		appliedRulesTable = new JTableAppliedRule();
		appliedRulesPane.setViewportView(appliedRulesTable);
		return appliedRulesPane;
	}
	
	@Override
	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(this.createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 4));
		buttonPanel.setPreferredSize(new java.awt.Dimension(78, 156));
		
		addRuleButton = new JButton();
		buttonPanel.add(addRuleButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		addRuleButton.setText("Add");
		addRuleButton.addActionListener(this);
		
		editRuleButton = new JButton();
		buttonPanel.add(editRuleButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		editRuleButton.setText("Edit");
		editRuleButton.addActionListener(this);
			
		removeRuleButton = new JButton();
		buttonPanel.add(removeRuleButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		removeRuleButton.setText("Remove");
		removeRuleButton.addActionListener(this);
		
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
	
	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.addRuleButton) {
			this.addRule();
		} else if (action.getSource() == this.editRuleButton) {
			this.editRule();
		} else if (action.getSource() == this.removeRuleButton) {
			this.removeRule();
		}
	}
	private void addRule() {
		DefinitionController.getInstance().createRuleGUI();
	}

	private void editRule() {
		//FIXME
//		long selectedAppliedRuleId = (Long) appliedRulesTable.getValueAt(getSelectedRow(), 0);
//		DefinitionController.getInstance().createRuleGUI(selectedAppliedRuleId);
	}

	private void removeRule() {
		long selectedAppliedRuleId = (Long) appliedRulesTable.getValueAt(getSelectedRow(), 0);
		DefinitionController.getInstance().removeRule(selectedAppliedRuleId);
	}
	
	/**
	 * Observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		updateSoftwareUnitTable();
	}
	
	public void updateSoftwareUnitTable() {
		DefinitionController.getInstance().updateAppliedRulesTable(this.appliedRulesTable);
	}
	
	public TableModel getModel() {
		return appliedRulesTable.getModel();
	}
	
	public int getSelectedRow() {
		return appliedRulesTable.getSelectedRow();
	}
	
}
