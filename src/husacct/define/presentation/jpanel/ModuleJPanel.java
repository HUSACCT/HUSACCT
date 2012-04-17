package husacct.define.presentation.jpanel;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ModuleJPanel extends AbstractDefinitionJPanel{

	private static final long serialVersionUID = 6141711414139061921L;
	
	public ModuleJPanel() {
		super();
	}

	@Override
	public void initGui() {
		BorderLayout modulePanelLayout = new BorderLayout();
		this.setLayout(modulePanelLayout);
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		this.add(createInnerModulePanel(), BorderLayout.CENTER);
	}
	
	public JPanel createInnerModulePanel() {
		JPanel innerModulePanel = new JPanel();
		BorderLayout innerModulePanelLayout = new BorderLayout();
		innerModulePanel.setLayout(innerModulePanelLayout);
		innerModulePanel.setBorder(BorderFactory.createTitledBorder("Module hierarchy"));
		innerModulePanel.add(this.createModuleTreePanel(), BorderLayout.CENTER);
		innerModulePanel.add(this.addButtonPanel(), BorderLayout.SOUTH);
		return innerModulePanel;
	}
	
	private JPanel createModuleTreePanel() {
		JPanel moduleTreePanel = new JPanel();
		BorderLayout moduleTreePanelLayout = new BorderLayout();
		moduleTreePanel.setLayout(moduleTreePanelLayout);
		moduleTreePanel.add(this.createModuleTreeScrollPane(), BorderLayout.CENTER);
		
		return moduleTreePanel;
	}
	
	private JScrollPane createModuleTreeScrollPane() {
		JScrollPane moduleTreeScrollPane = new JScrollPane();
		moduleTreeScrollPane.setPreferredSize(new java.awt.Dimension(383, 213));
		
		JList moduleTreeJList = new JList();
		moduleTreeJList.setModel(new DefaultListModel());
		moduleTreeScrollPane.setViewportView(moduleTreeJList);
		
		return moduleTreeScrollPane;
	}

	@Override
	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(this.createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		
		JButton newModuleButton = new JButton();
		buttonPanel.add(newModuleButton);
		newModuleButton.setText("New Module");
			
		JButton moveModuleUpButton = new JButton();
		buttonPanel.add(moveModuleUpButton);
		moveModuleUpButton.setText("Move up");

		JButton removeModuleButton = new JButton();
		buttonPanel.add(removeModuleButton);
		removeModuleButton.setText("Remove Module");

		JButton moveModuleDownButton = new JButton();
		buttonPanel.add(moveModuleDownButton);
		moveModuleDownButton.setText("Move down");
		
		return buttonPanel;
	}
	
	private GridLayout createButtonPanelLayout() {
		GridLayout buttonPanelLayout = new GridLayout(2, 2);
		buttonPanelLayout.setColumns(2);
		buttonPanelLayout.setHgap(5);
		buttonPanelLayout.setVgap(5);
		buttonPanelLayout.setRows(2);
		return buttonPanelLayout;
	}
}
