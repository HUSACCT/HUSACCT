package husacct.define.presentation.jpanel;

import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.tables.JTableTableModel;
import husacct.define.presentation.utils.DefaultMessages;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;


/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class DefinitionJPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 7442552399461704491L;
	public JTable jTableSoftwareUnits;
	public JButton jButtonNext;

	public JCheckBox jCheckBoxAccess;
	public JTable jTableAppliedRules;
	public JList jListLayers;
	
	public JTextArea jTextAreaLayerDescription;
	public JTextField jTextFieldLayerName;
	//SoftwareUnits
	public JButton jButtonAddSoftwareUnit;
	public JButton jButtonRemoveSoftwareUnit;
//	public JButton jButtonEditSoftwareUnit;
	//AppliedRules
	public JButton jButtonAddRule;
	public JButton jButtonRemoveRule;
	public JButton jButtonEditRule;
	
	//DefineModules
	public JButton jButtonMoveLayerDown;
	public JButton jButtonMoveLayerUp;
	public JButton jButtonNewLayer;
	public JButton jButtonRemoveLayer;
	
	private JPanel mappingPanel;
	
	public DefinitionJPanel() {
		super();
		initUI();
	}

	public final void initUI() {
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
			
			BorderLayout thisLayout = new BorderLayout();
			this.setLayout(thisLayout);
			this.setPreferredSize(new java.awt.Dimension(753, 476));
			
			JSplitPane mainSplitPane = new JSplitPane();
			mainSplitPane.setDividerLocation(300);
			this.add(mainSplitPane, BorderLayout.CENTER);
			mainSplitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			
			mainSplitPane.add(this.createModulePanel(), JSplitPane.LEFT);
			mainSplitPane.add(this.createRightPanel(), JSplitPane.RIGHT);
			mainSplitPane.add(this.addBottomPanel(), JSplitPane.BOTTOM);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JPanel createModulePanel() {
		ModuleJPanel modulePanel = new ModuleJPanel();
		modulePanel.initGui();
		return modulePanel;
	}
	
	private JPanel createRightPanel() {
		JPanel rightPanel = new JPanel();
		BorderLayout jPanel1Layout = new BorderLayout();
		rightPanel.setLayout(jPanel1Layout);
		rightPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		rightPanel.add(this.createEditModulePanel(), BorderLayout.NORTH);
		rightPanel.add(this.createDefaultMappingPanel(), BorderLayout.CENTER);
		return rightPanel;
	}
	
	private JPanel createEditModulePanel() {
		EditModuleJPanel modulePanel = new EditModuleJPanel();
		modulePanel.initGui();
		return modulePanel;
	}

	private JPanel createDefaultMappingPanel() {
		mappingPanel = new JPanel();
		mappingPanel.setLayout(this.createMappingPanelLayout());
		mappingPanel.add(this.createSoftwareUnitsPanel());
		mappingPanel.add(this.createAppliedRulesPanel());
		return mappingPanel;
	}
	
	private GridLayout createMappingPanelLayout() {
		GridLayout mappingPanelLayout = new GridLayout(2, 1);
		mappingPanelLayout.setColumns(1);
		mappingPanelLayout.setRows(2);
		mappingPanelLayout.setHgap(5);
		mappingPanelLayout.setVgap(5);
		return mappingPanelLayout;
	}
	
	private JPanel createAppliedRulesPanel() {
		AppliedRulesJPanel appliedRulesPanel = new AppliedRulesJPanel();
		appliedRulesPanel.initGui();
		return appliedRulesPanel;
	}
	
	private JPanel createSoftwareUnitsPanel() {
		SoftwareUnitsJPanel sofwareUnitsPanel = new SoftwareUnitsJPanel();
		sofwareUnitsPanel.initGui();
		return sofwareUnitsPanel;
	}
	
	@Deprecated
	private JPanel addBottomPanel() {
		JPanel bottomPanel = new JPanel();
		BorderLayout bottomPanelLayout = new BorderLayout();
		bottomPanel.setLayout(bottomPanelLayout);
		bottomPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
//		JButton nextButton = new JButton();
//		bottomPanel.add(nextButton, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//		nextButton.setText("Next");
		
		return bottomPanel;
	}

	public long getSelectedLayer() {
		Object selected = jListLayers.getSelectedValue();
		if (selected instanceof DataHelper) {
			long id = ((DataHelper) selected).getId();
			return id;
		}
		return -1;
	}

	public long getSelectedSoftwareUnit() {
		int selectedRow = jTableSoftwareUnits.getSelectedRow();
		if (selectedRow >= 0) {
			JTableTableModel c = (JTableTableModel) jTableSoftwareUnits.getModel();

			Object selected = c.getValueAt(selectedRow, 0);
			if (selected instanceof DataHelper) {
				return ((DataHelper) selected).getId();
			}
		}
		return -1L;
	}
	
	public String getSelectedSoftwareUnitName() {
		String selectedValue = "";
		int selectedRow = jTableSoftwareUnits.getSelectedRow();
		if (selectedRow >= 0) {
			JTableTableModel c = (JTableTableModel) jTableSoftwareUnits.getModel();

			Object selected = c.getValueAt(selectedRow, 0);
			selectedValue = selected.toString();
			
//			if (selected instanceof DataHelper) {
//				selectedValue = ((DataHelper) selected).getValue();
//			}
		}
		return selectedValue;
	}

	public long getSelectedAppliedRule() {
		int selectedRow = jTableAppliedRules.getSelectedRow();
		if (selectedRow >= 0) {
			JTableTableModel c = (JTableTableModel) jTableAppliedRules.getModel();

			Object selected = c.getValueAt(selectedRow, 0);
			if (selected instanceof DataHelper) {
				return ((DataHelper) selected).getId();
			}
		}
		return -1L;
	}
}
