package husacct.define.presentation.jpanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JSplitPane;

public class DefinitionJPanel extends javax.swing.JPanel {

	private static final long serialVersionUID = 7442552399461704491L;
	public AppliedRulesJPanel appliedRulesPanel;
	private JPanel mappingPanel;
	public EditModuleJPanel moduleEditPanel;
	public ModuleJPanel modulePanel;

	public SoftwareUnitsJPanel sofwareUnitsPanel;

	public DefinitionJPanel() {
		super();
		initUI();
	}

	private JPanel createAppliedRulesPanel() {
		appliedRulesPanel = new AppliedRulesJPanel();
		appliedRulesPanel.initGui();
		return appliedRulesPanel;
	}

	private JPanel createDefaultMappingPanel() {
		mappingPanel = new JPanel();
		mappingPanel.setLayout(createMappingPanelLayout());
		mappingPanel.add(createSoftwareUnitsPanel());
		mappingPanel.add(createAppliedRulesPanel());
		return mappingPanel;
	}

	private JPanel createEditModulePanel() {
		moduleEditPanel = new EditModuleJPanel();
		moduleEditPanel.initGui();
		return moduleEditPanel;
	}

	private GridLayout createMappingPanelLayout() {
		GridLayout mappingPanelLayout = new GridLayout(2, 1);
		mappingPanelLayout.setColumns(1);
		mappingPanelLayout.setRows(2);
		mappingPanelLayout.setHgap(5);
		mappingPanelLayout.setVgap(5);
		return mappingPanelLayout;
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
		rightPanel.add(createEditModulePanel(), BorderLayout.NORTH);
		rightPanel.add(createDefaultMappingPanel(), BorderLayout.CENTER);
		return rightPanel;
	}

	private JPanel createSoftwareUnitsPanel() {
		sofwareUnitsPanel = new SoftwareUnitsJPanel();
		sofwareUnitsPanel.initGui();
		return sofwareUnitsPanel;
	}

	public final void initUI() {
		try {

			BorderLayout thisLayout = new BorderLayout();
			setLayout(thisLayout);

			JSplitPane mainSplitPane = new JSplitPane();
			mainSplitPane.setDividerLocation(300);
			this.add(mainSplitPane, BorderLayout.CENTER);
			mainSplitPane
					.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

			mainSplitPane.add(createModulePanel(), JSplitPane.LEFT);
			mainSplitPane.add(createRightPanel(), JSplitPane.RIGHT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
