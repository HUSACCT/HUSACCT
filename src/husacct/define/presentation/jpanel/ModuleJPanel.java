package husacct.define.presentation.jpanel;

import husacct.define.presentation.helper.DataHelper;
import husacct.define.task.DefinitionController;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public class ModuleJPanel extends AbstractDefinitionJPanel implements ActionListener, ListSelectionListener {

	private static final long serialVersionUID = 6141711414139061921L;
	private JList moduleTreeJList;
	
	private JButton newModuleButton;
	private JButton moveModuleUpButton;
	private JButton removeModuleButton;
	private JButton moveModuleDownButton;
	
	public ModuleJPanel() {
		super();
	}

	@Override
	public void initGui() {
		BorderLayout modulePanelLayout = new BorderLayout();
		this.setLayout(modulePanelLayout);
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		this.add(createInnerModulePanel(), BorderLayout.CENTER);
		this.updateModulesTreeList();
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
		
		this.moduleTreeJList = new JList();
		this.moduleTreeJList.setModel(new DefaultListModel());
		moduleTreeScrollPane.setViewportView(this.moduleTreeJList);
		this.moduleTreeJList.addListSelectionListener(this);
		
		return moduleTreeScrollPane;
	}

	@Override
	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(this.createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));
		
		this.newModuleButton = new JButton();
		buttonPanel.add(this.newModuleButton);
		this.newModuleButton.setText("New Module");
		this.newModuleButton.addActionListener(this);
			
		this.moveModuleUpButton = new JButton();
		buttonPanel.add(this.moveModuleUpButton);
		this.moveModuleUpButton.setText("Move up");
		this.moveModuleUpButton.addActionListener(this);

		this.removeModuleButton = new JButton();
		buttonPanel.add(this.removeModuleButton);
		this.removeModuleButton.setText("Remove Module");
		this.removeModuleButton.addActionListener(this);

		this.moveModuleDownButton = new JButton();
		buttonPanel.add(this.moveModuleDownButton);
		this.moveModuleDownButton.setText("Move down");
		this.moveModuleDownButton.addActionListener(this);
		
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
	
	public void updateModulesTreeList() {
		DefinitionController.getInstance().updateModuleTreeList(this.moduleTreeJList);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (event.getSource() == this.moduleTreeJList && !event.getValueIsAdjusting()) {
			this.moduleTreeJListAction(event);
		}
	}
	
	private void moduleTreeJListAction(ListSelectionEvent event) {
		long moduleId = -1;
		Object selectedModule = this.moduleTreeJList.getSelectedValue();
		if (selectedModule instanceof DataHelper) {
			moduleId = ((DataHelper) selectedModule).getId();
		}
		
		DefinitionController.getInstance().loadLayerDetail(moduleId);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.newModuleButton) {
			this.newModuleAction(action);
		} else if (action.getSource() == this.moveModuleUpButton) {
			this.moveModuleUpAction(action);
		} else if (action.getSource() == this.removeModuleButton) {
			this.removeModuleAction(action);
		} else if (action.getSource() == this.moveModuleDownButton) {
			this.moveModuleDownAction(action);
		}
	}

	private void moveModuleDownAction(ActionEvent action) {
		DefinitionController.getInstance().moveLayerDown();
		this.updateModulesTreeList();
	}

	private void removeModuleAction(ActionEvent action) {
		DefinitionController.getInstance().removeLayer();
		this.updateModulesTreeList();
	}

	private void moveModuleUpAction(ActionEvent action) {
		DefinitionController.getInstance().moveLayerUp();
		this.updateModulesTreeList();
	}

	private void newModuleAction(ActionEvent action) {
		DefinitionController.getInstance().newLayer();
		this.updateModulesTreeList();
	}
}
