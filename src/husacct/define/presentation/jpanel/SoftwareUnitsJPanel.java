package husacct.define.presentation.jpanel;

import husacct.define.presentation.tables.JTableSoftwareUnits;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class SoftwareUnitsJPanel extends AbstractDefinitionJPanel {

	private static final long serialVersionUID = 8086576683923713276L;

	public SoftwareUnitsJPanel() {
		super();
	}

	@Override
	public void initGui() {
		BorderLayout softwareUnitsPanelLayout = new BorderLayout();
		this.setLayout(softwareUnitsPanelLayout);
		this.setBorder(BorderFactory.createTitledBorder("Software units which are assigned to this layer"));
		this.add(this.addSoftwareUnitsTable(), BorderLayout.CENTER);
		this.add(this.addButtonPanel(), BorderLayout.EAST);
	}
	
	private JScrollPane addSoftwareUnitsTable() {
		JScrollPane softwareUnitsPane = new JScrollPane();
		softwareUnitsPane.setPreferredSize(new java.awt.Dimension(227, 249));
		JTableSoftwareUnits softwareUnitsTable = new JTableSoftwareUnits();
		softwareUnitsPane.setViewportView(softwareUnitsTable);
		return softwareUnitsPane;
	}
	
	@Override
	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(this.createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
		
		JButton addSoftwareUnitButton = new JButton();
		buttonPanel.add(addSoftwareUnitButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		addSoftwareUnitButton.setText("Add");
		
		JButton editSoftwareUnitButton = new JButton();
		buttonPanel.add(editSoftwareUnitButton, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		editSoftwareUnitButton.setText("Edit");
			
		JButton removeSoftwareUnitButton = new JButton();
		buttonPanel.add(removeSoftwareUnitButton, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
		removeSoftwareUnitButton.setText("Remove");
		
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
}
