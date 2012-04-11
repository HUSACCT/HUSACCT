package husacct.define.presentation.jpanel;

import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.tables.JTableAppliedRule;
import husacct.define.presentation.tables.JTableSoftwareUnits;
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
	private JSplitPane jSplitPane;
	private JScrollPane jScrollPane1;
	private JScrollPane jScrollPane2;
	private JScrollPane jScrollPane3;
	private JScrollPane jScrollPane4;
	public JTable jTableSoftwareUnits;
	public JButton jButtonNext;

	public JCheckBox jCheckBoxAccess;
	public JTable jTableAppliedRules;
	public JList jListLayers;
	
	private JLabel jLabelModuleAccess;
	private JLabel jLabelModuleDescription;
	private JLabel jLabelModuleName;
	
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
	
	private JPanel jPanel9;
	private JPanel jPanel11;
	private JPanel jPanel10;
	private JPanel jPanelLeft;
	private JPanel jPanelRight;
	private JPanel jPanel8;
	private JPanel jPanel7;
	private JPanel jPanel6;
	private JPanel jPanel5;
	private JPanel jPanel4;
	private JPanel jPanel3;

	{
		// Set Look & Feel
		try {
			javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Auto-generated main method to display this JPanel inside a new JFrame.
	 */
	public DefinitionJPanel() {
		super();
		initUI();
	}

	public final void initUI() {
		try {
			{
				BorderLayout thisLayout = new BorderLayout();
				this.setLayout(thisLayout);
				this.setPreferredSize(new java.awt.Dimension(753, 476));
				{
					jSplitPane = new JSplitPane();
					jSplitPane.setDividerLocation(300);
					this.add(jSplitPane, BorderLayout.CENTER);
					jSplitPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
					{
						jPanelLeft = new JPanel();
						BorderLayout jPanel2Layout = new BorderLayout();
						jPanelLeft.setLayout(jPanel2Layout);
						jSplitPane.add(jPanelLeft, JSplitPane.LEFT);
						jPanelLeft.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
						{
							jPanel3 = new JPanel();
							BorderLayout jPanel3Layout = new BorderLayout();
							jPanel3.setLayout(jPanel3Layout);
							jPanelLeft.add(jPanel3, BorderLayout.CENTER);
							jPanel3.setBorder(BorderFactory.createTitledBorder("Module hierarchy"));
							{
								jPanel5 = new JPanel();
								BorderLayout jPanel5Layout = new BorderLayout();
								jPanel5.setLayout(jPanel5Layout);
								jPanel3.add(jPanel5, BorderLayout.CENTER);
								{
									jScrollPane1 = new JScrollPane();
									jPanel5.add(jScrollPane1, BorderLayout.CENTER);
									jScrollPane1.setPreferredSize(new java.awt.Dimension(383, 213));
									{
										jListLayers = new JList();
										jListLayers.setModel(new DefaultListModel());
										jScrollPane1.setViewportView(jListLayers);
									}
								}
							}
							{
								jPanel6 = new JPanel();
								GridLayout jPanel6Layout = new GridLayout(2, 2);
								jPanel6.setLayout(jPanel6Layout);
								jPanel3.add(jPanel6, BorderLayout.SOUTH);
								jPanel6.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
								jPanel6Layout.setColumns(2);
								jPanel6Layout.setHgap(5);
								jPanel6Layout.setVgap(5);
								jPanel6Layout.setRows(2);
								{
									jButtonNewLayer = new JButton();
									jPanel6.add(jButtonNewLayer);
									jButtonNewLayer.setText("New Module");

								}
								{
									jButtonMoveLayerUp = new JButton();
									jPanel6.add(jButtonMoveLayerUp);
									jButtonMoveLayerUp.setText("Move up");

								}
								{
									jButtonRemoveLayer = new JButton();
									jPanel6.add(jButtonRemoveLayer);
									jButtonRemoveLayer.setText("Remove Module");

								}
								{
									jButtonMoveLayerDown = new JButton();
									jPanel6.add(jButtonMoveLayerDown);
									jButtonMoveLayerDown.setText("Move down");

								}
							}
						}
					}
					{
						jPanelRight = new JPanel();
						BorderLayout jPanel1Layout = new BorderLayout();
						jPanelRight.setLayout(jPanel1Layout);
						jSplitPane.add(jPanelRight, JSplitPane.RIGHT);
						jPanelRight.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
						{
							jPanel4 = new JPanel();
							GridBagLayout jPanel4Layout = new GridBagLayout();
							jPanel4Layout.rowWeights = new double[] { 0.0, 0.1, 0.1 };
							jPanel4Layout.rowHeights = new int[] { 27, 7, 7 };
							jPanel4Layout.columnWeights = new double[] { 0.0, 0.1 };
							jPanel4Layout.columnWidths = new int[] { 118, 7 };
							jPanel4.setLayout(jPanel4Layout);
							jPanelRight.add(jPanel4, BorderLayout.NORTH);
							jPanel4.setBorder(BorderFactory.createTitledBorder("Module configuration"));
							jPanel4.setPreferredSize(new java.awt.Dimension(442, 105));
							{
								jLabelModuleName = new JLabel();
								jPanel4.add(jLabelModuleName, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jLabelModuleName.setText("Module name");
							}
							{
								jTextFieldLayerName = new JTextField();
								jTextFieldLayerName.setToolTipText(DefaultMessages.TIP_LAYER);
								jPanel4.add(jTextFieldLayerName, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
							}
							{
								jLabelModuleDescription = new JLabel();
								jPanel4.add(jLabelModuleDescription, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jLabelModuleDescription.setText("Description");
							}
							{
								jScrollPane2 = new JScrollPane();
								jPanel4.add(jScrollPane2, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
								jPanel4.add(getJCheckBox1(), new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jPanel4.add(getJLabelModuleAccess(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
								jScrollPane2.setPreferredSize(new java.awt.Dimension(142, 26));
								{
									jTextAreaLayerDescription = new JTextArea();
									jTextAreaLayerDescription.setFont(new java.awt.Font("Tahoma", 0, 11));
									jTextAreaLayerDescription.setToolTipText(DefaultMessages.TIP_LAYERDESCRIPTION);
									jScrollPane2.setViewportView(jTextAreaLayerDescription);
								}
							}
						}
						{
							jPanel11 = new JPanel();
							GridLayout jPanel11Layout = new GridLayout(2, 1);
							jPanel11Layout.setColumns(1);
							jPanel11Layout.setRows(2);
							jPanel11Layout.setHgap(5);
							jPanel11Layout.setVgap(5);
							jPanelRight.add(jPanel11, BorderLayout.CENTER);
							jPanel11.setLayout(jPanel11Layout);
							{
								jPanel7 = new JPanel();
								jPanel11.add(jPanel7);
								BorderLayout jPanel7Layout = new BorderLayout();
								jPanel7.setLayout(jPanel7Layout);
								jPanel7.setBorder(BorderFactory.createTitledBorder("Software units which are assigned to this layer"));
								{
									jScrollPane3 = new JScrollPane();
									jPanel7.add(jScrollPane3, BorderLayout.CENTER);
									jScrollPane3.setPreferredSize(new java.awt.Dimension(227, 249));
									{
										jTableSoftwareUnits = new JTableSoftwareUnits();
										jScrollPane3.setViewportView(jTableSoftwareUnits);
									}
								}
								{
									jPanel9 = new JPanel();
									GridBagLayout jPanel9Layout = new GridBagLayout();
									jPanel9Layout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
									jPanel9Layout.rowHeights = new int[] { 13, 13, 7 };
									jPanel9Layout.columnWeights = new double[] { 0.1 };
									jPanel9Layout.columnWidths = new int[] { 7 };
									jPanel9.setLayout(jPanel9Layout);
									jPanel7.add(jPanel9, BorderLayout.EAST);
									jPanel9.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 3));
									{
										jButtonAddSoftwareUnit = new JButton();
										jPanel9.add(jButtonAddSoftwareUnit, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
										jButtonAddSoftwareUnit.setText("Add");
									}
//									{
//										jButtonEditSoftwareUnit = new JButton();
//										jPanel9.add(jButtonEditSoftwareUnit, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//										jButtonEditSoftwareUnit.setText("Edit");
//									}
									{
										jButtonRemoveSoftwareUnit = new JButton();
										jPanel9.add(jButtonRemoveSoftwareUnit, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
										jButtonRemoveSoftwareUnit.setText("Remove");
									}
								}
							}
							{
								jPanel8 = new JPanel();
								jPanel11.add(jPanel8);
								BorderLayout jPanel8Layout = new BorderLayout();
								jPanel8.setLayout(jPanel8Layout);
								jPanel8.setBorder(BorderFactory.createTitledBorder("Applied rules for this layer"));
								{
									jScrollPane4 = new JScrollPane();
									jPanel8.add(jScrollPane4, BorderLayout.CENTER);
									jScrollPane4.setPreferredSize(new java.awt.Dimension(278, 32));
									{
										jTableAppliedRules = new JTableAppliedRule();
										jScrollPane4.setViewportView(jTableAppliedRules);
									}
								}
								{
									jPanel10 = new JPanel();
									GridBagLayout jPanel10Layout = new GridBagLayout();
									jPanel10Layout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
									jPanel10Layout.rowHeights = new int[] { 0, 11, 7 };
									jPanel10Layout.columnWeights = new double[] { 0.1 };
									jPanel10Layout.columnWidths = new int[] { 7 };
									jPanel10.setLayout(jPanel10Layout);
									jPanel8.add(jPanel10, BorderLayout.EAST);
									jPanel10.setBorder(BorderFactory.createEmptyBorder(0, 3, 0, 4));
									jPanel10.setPreferredSize(new java.awt.Dimension(78, 156));
									{
										jButtonAddRule = new JButton();
										jPanel10.add(jButtonAddRule, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
										jButtonAddRule.setText("Add");
									}
									{
										jButtonEditRule = new JButton();
										jPanel10.add(jButtonEditRule, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
										jButtonEditRule.setText("Edit");
									}
									{
										jButtonRemoveRule = new JButton();
										jPanel10.add(jButtonRemoveRule, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
										jButtonRemoveRule.setText("Remove");
									}
								}
							}
						}
					}
//					JPanel jPanelBottom = new JPanel();
//					BorderLayout jPanel2Layout = new BorderLayout();
//					jPanelBottom.setLayout(jPanel2Layout);
//					jSplitPane.add(jPanelBottom, JSplitPane.BOTTOM);
//					jPanelBottom.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
//					
//					jButtonNext = new JButton();
//					jPanelBottom.add(jButtonNext, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
//					jButtonNext.setText("Next");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
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

	private JCheckBox getJCheckBox1() {
		if (jCheckBoxAccess == null) {
			jCheckBoxAccess = new JCheckBox();
			jCheckBoxAccess.setText("Only accessible by a facade (interface)");
			jCheckBoxAccess.setToolTipText(DefaultMessages.TIP_FACADE);
		}
		return jCheckBoxAccess;
	}

	private JLabel getJLabelModuleAccess() {
		if (jLabelModuleAccess == null) {
			jLabelModuleAccess = new JLabel();
			jLabelModuleAccess.setText("Access:");
		}
		return jLabelModuleAccess;
	}
}
