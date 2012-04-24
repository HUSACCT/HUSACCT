package husacct.define.presentation.jpanel;

import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.tables.JTableTableModel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;


/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI Builder, which is free for non-commercial use. If Jigloo is being used commercially (ie, by a corporation, company or
 * business for any purpose whatever) then you should purchase a license for each developer using Jigloo. Please visit www.cloudgarden.com for details. Use of Jigloo implies acceptance of these
 * licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
public class DefinitionJPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 7442552399461704491L;
	public ModuleJPanel modulePanel;
	public EditModuleJPanel moduleEditPanel;
	public SoftwareUnitsJPanel sofwareUnitsPanel;
	public AppliedRulesJPanel appliedRulesPanel;
	
	
	private JPanel mappingPanel;
//	public JTable jTableSoftwareUnits;
//	public JTable jTableAppliedRules;
//	public JButton jButtonNext;
//
//	public JCheckBox jCheckBoxAccess;
//	public JList jListLayers;
	
//	public JTextArea jTextAreaLayerDescription;
//	public JTextField jTextFieldModuleName;
	//SoftwareUnits
//	public JButton jButtonAddSoftwareUnit;
//	public JButton jButtonRemoveSoftwareUnit;
//	//AppliedRules
//	public JButton jButtonAddRule;
//	public JButton jButtonRemoveRule;
//	public JButton jButtonEditRule;
	//DefineModules
////	public JButton jButtonMoveLayerDown;
//	public JButton jButtonMoveLayerUp;
//	public JButton jButtonNewModule;
//	public JButton jButtonRemoveModule;

	
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JPanel createModulePanel() {
		modulePanel = new ModuleJPanel();
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
		moduleEditPanel = new EditModuleJPanel();
		moduleEditPanel.initGui();
		return moduleEditPanel;
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
		appliedRulesPanel = new AppliedRulesJPanel();
		appliedRulesPanel.initGui();
		return appliedRulesPanel;
	}
	
	private JPanel createSoftwareUnitsPanel() {
		sofwareUnitsPanel = new SoftwareUnitsJPanel();
		sofwareUnitsPanel.initGui();
		return sofwareUnitsPanel;
	}
	
	@Deprecated
	public long getSelectedModule() {
		Object selected = modulePanel.getSelectedValue();
		if (selected instanceof DataHelper) {
			long id = ((DataHelper) selected).getId();
			return id;
		}
		return -1;
	}

	@Deprecated
	public long getSelectedSoftwareUnit() {
		int selectedRow = sofwareUnitsPanel.getSelectedRow();
		if (selectedRow >= 0) {
			JTableTableModel c = (JTableTableModel) sofwareUnitsPanel.getModel();

			Object selected = c.getValueAt(selectedRow, 0);
			if (selected instanceof DataHelper) {
				return ((DataHelper) selected).getId();
			}
		}
		return -1L;
	}
	
	@Deprecated
	public String getSelectedSoftwareUnitName() {
		String selectedValue = "";
		int selectedRow = sofwareUnitsPanel.getSelectedRow();
		if (selectedRow >= 0) {
			JTableTableModel c = (JTableTableModel) sofwareUnitsPanel.getModel();

			Object selected = c.getValueAt(selectedRow, 0);
			selectedValue = selected.toString();
			
//			if (selected instanceof DataHelper) {
//				selectedValue = ((DataHelper) selected).getValue();
//			}
		}
		return selectedValue;
	}

	@Deprecated
	public long getSelectedAppliedRule() {
		int selectedRow = appliedRulesPanel.getSelectedRow();
		if (selectedRow >= 0) {
			JTableTableModel c = (JTableTableModel) appliedRulesPanel.getModel();

			Object selected = c.getValueAt(selectedRow, 0);
			if (selected instanceof DataHelper) {
				return ((DataHelper) selected).getId();
			}
		}
		return -1L;
	}
}
