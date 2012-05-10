package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.presentation.jframe.JFrameAppliedRule;
import husacct.define.presentation.tables.JTableAppliedRule;
import husacct.define.task.DefinitionController;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.table.TableModel;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public class AppliedRulesJPanel extends AbstractDefinitionJPanel  implements ActionListener, Observer, ILocaleChangeListener {
	
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
	public void initGui() {
		DefinitionController.getInstance().addObserver(this);
		BorderLayout appliedRulesPanelLayout = new BorderLayout();
		this.setLayout(appliedRulesPanelLayout);
		this.setBorder(BorderFactory.createTitledBorder("Applied rules for this module"));
		this.add(this.addAppliedRulesTable(), BorderLayout.CENTER);
		this.add(this.addButtonPanel(), BorderLayout.EAST);
		ServiceProvider.getInstance().getControlService().addLocaleChangeListener(this);
	}
	
	private JScrollPane addAppliedRulesTable() {
		appliedRulesPane = new JScrollPane();
		appliedRulesTable = new JTableAppliedRule();
		appliedRulesPane.setViewportView(appliedRulesTable);
		return appliedRulesPane;
	}
	
	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(this.createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 4));
		buttonPanel.setPreferredSize(new java.awt.Dimension(90, 156));
		
		addRuleButton = new JButton();
		buttonPanel.add(addRuleButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		addRuleButton.addActionListener(this);
		
		editRuleButton = new JButton();
		buttonPanel.add(editRuleButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		editRuleButton.addActionListener(this);
			
		removeRuleButton = new JButton();
		buttonPanel.add(removeRuleButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		removeRuleButton.addActionListener(this);
		
		this.setButtonTexts();
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
		long moduleId = DefinitionController.getInstance().getSelectedModuleId();
		if (moduleId != -1) {
			JFrameAppliedRule appliedRuleFrame = new JFrameAppliedRule(moduleId, -1L);
			appliedRuleFrame.setLocationRelativeTo(appliedRuleFrame.getRootPane());
			appliedRuleFrame.setVisible(true);
			//DefinitionController.getInstance().createRuleGUI();
		} else {
			JOptionPane.showMessageDialog(this, "Please select a module", "Wrong selection!", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void editRule() {
		long moduleId = DefinitionController.getInstance().getSelectedModuleId();
		if (moduleId != -1) {
			JOptionPane.showMessageDialog(this, "Currently under construction", "Under Construction", JOptionPane.ERROR_MESSAGE);
			//FIXME
//			long selectedAppliedRuleId = (Long) appliedRulesTable.getValueAt(getSelectedRow(), 0);
//			DefinitionController.getInstance().createRuleGUI(selectedAppliedRuleId);
		} else {
			JOptionPane.showMessageDialog(this, "Please select a module", "Wrong selection!", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void removeRule() {
		long selectedAppliedRuleId = appliedRulesTable.getValueAt(getSelectedRow(), 0);
		if(selectedAppliedRuleId != -1) {
			DefinitionController.getInstance().removeRule(selectedAppliedRuleId);
		} else {
			JOptionPane.showMessageDialog(this, "Please select a rule", "Wrong selection!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		updateAppliedRuleTable();
	}
	
	public void updateAppliedRuleTable() {
		DefinitionController.getInstance().updateAppliedRulesTable(this.appliedRulesTable);
	}
	
	public TableModel getModel() {
		return appliedRulesTable.getModel();
	}
	
	public int getSelectedRow() {
		return appliedRulesTable.getSelectedRow();
	}

	@Override
	public void update(Locale newLocale) {
		this.setButtonTexts();
		this.appliedRulesTable.changeColumnHeaders();
	}
	
	private void setButtonTexts() {
		addRuleButton.setText(DefineTranslator.translate("Add"));
		editRuleButton.setText(DefineTranslator.translate("Edit"));
		removeRuleButton.setText(DefineTranslator.translate("Remove"));
	}
}
