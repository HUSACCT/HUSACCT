package husacct.define.presentation.jpanel;

import husacct.define.presentation.tables.JTableSoftwareUnits;
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
public class SoftwareUnitsJPanel extends AbstractDefinitionJPanel implements ActionListener,  Observer{

	private static final long serialVersionUID = 8086576683923713276L;
	private JTableSoftwareUnits softwareUnitsTable;
	private JScrollPane softwareUnitsPane;
	
	private JButton addSoftwareUnitButton;
	private JButton removeSoftwareUnitButton;

	public SoftwareUnitsJPanel() {
		super();
	}
	
	/**
	 * Creating Gui
	 */
	@Override
	public void initGui() {
		DefinitionController.getInstance().addObserver(this);
		BorderLayout softwareUnitsPanelLayout = new BorderLayout();
		this.setLayout(softwareUnitsPanelLayout);
		this.setBorder(BorderFactory.createTitledBorder("Software units which are assigned to this module"));
		this.add(this.addSoftwareUnitsTable(), BorderLayout.CENTER);
		this.add(this.addButtonPanel(), BorderLayout.EAST);
	}
	
	private JScrollPane addSoftwareUnitsTable() {
		softwareUnitsPane = new JScrollPane();
		softwareUnitsPane.setPreferredSize(new java.awt.Dimension(227, 249));
		softwareUnitsTable = new JTableSoftwareUnits();
		softwareUnitsPane.setViewportView(softwareUnitsTable);
		return softwareUnitsPane;
	}
	
	@Override
	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(this.createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		
		addSoftwareUnitButton = new JButton();
		buttonPanel.add(addSoftwareUnitButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		addSoftwareUnitButton.setText("Add");
		addSoftwareUnitButton.addActionListener(this);
			
		removeSoftwareUnitButton = new JButton();
		buttonPanel.add(removeSoftwareUnitButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		removeSoftwareUnitButton.setText("Remove");
		removeSoftwareUnitButton.addActionListener(this);
		
		return buttonPanel;
	}
	
	private GridBagLayout createButtonPanelLayout() {
		GridBagLayout buttonPanelLayout = new GridBagLayout();
		buttonPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
		buttonPanelLayout.rowHeights = new int[] { 13, 13, 7 };
		buttonPanelLayout.columnWeights = new double[] { 0.1 };
		buttonPanelLayout.columnWidths = new int[] { 7 };
		return buttonPanelLayout;
	}

	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.addSoftwareUnitButton) {
			this.addSoftwareUnit();
		} else if (action.getSource() == this.removeSoftwareUnitButton) {
			this.removeSoftwareUnit();
		}
	}
	
	private void addSoftwareUnit(){
		DefinitionController.getInstance().createSoftwareUnitGUI();
	}
	private void removeSoftwareUnit(){
		String softwareUnitName = (String)softwareUnitsTable.getValueAt(getSelectedRow(), 0);
		DefinitionController.getInstance().removeSoftwareUnit(softwareUnitName);
	}
		
	/**
	 * Observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		updateSoftwareUnitTable();
	}
	
	public void updateSoftwareUnitTable() {
		DefinitionController.getInstance().updateSoftwareUnitTable(this.softwareUnitsTable);
	}
	
	public TableModel getModel(){
		return softwareUnitsTable.getModel();
	}

	public int getSelectedRow() {
		return softwareUnitsTable.getSelectedRow();
	}
}
