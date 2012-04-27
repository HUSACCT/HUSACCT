package husacct.define.presentation.jpanel;

import husacct.define.domain.DefineDomainService;
import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.DefinitionController;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public class ModuleJPanel extends AbstractDefinitionJPanel implements ActionListener, ListSelectionListener, Observer {

	private static final long serialVersionUID = 6141711414139061921L;
	private JList moduleTreeJList;
	
	private JButton newModuleButton;
	private JButton moveModuleUpButton;
	private JButton removeModuleButton;
	private JButton moveModuleDownButton;
	
	public ModuleJPanel() {
		super();
	}

	
	/**
	 * Creating Gui
	 */
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
	
	/**
	 * Observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		updateModulesTreeList();
	}
	
	public void updateModulesTreeList() {
		DefinitionController.getInstance().updateModuleTreeList(this.moduleTreeJList);
	}
	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.newModuleButton) {
			this.newModule();
		} else if (action.getSource() == this.moveModuleUpButton) {
			this.moveLayerUp();
		} else if (action.getSource() == this.removeModuleButton) {
			this.removeModule();
		} else if (action.getSource() == this.moveModuleDownButton) {
			this.moveLayerDown();
		}
		this.updateModulesTreeList();
	}
	private void newModule() {
		//TODO call AddModuleValuesJFrame instead of the following code.
		
		// Ask the user for the module name
		String moduleName = UiDialogs.inputDialog(this, "Please enter module name", "Please input a value", JOptionPane.QUESTION_MESSAGE);
		if (moduleName != null) {
			//Creating a new module of type Layer
			//has yet to be implemented to support other module types
			String layerLevelString = UiDialogs.inputDialog(this, "Please enter layer level", "Please input a value", JOptionPane.QUESTION_MESSAGE);
			if (layerLevelString != null) {
				int layerLevel = Integer.parseInt(layerLevelString);
				// Call task to create the layer
				DefinitionController.getInstance().addLayer(moduleName, layerLevel);
			}
		}
		this.updateModulesTreeList();
	}
	private void removeModule() {
		long moduleId = getSelectedModuleId();
		if (moduleId != -1){
			boolean confirm = UiDialogs.confirmDialog(this, "Are you sure you want to remove module: \"" + getSelectedValue() + "\"", "Remove?");
			if (confirm) {
				moduleTreeJList.clearSelection();
				DefinitionController.getInstance().removeModuleById(moduleId);
			}
		}
	}
	private void moveLayerUp() {
		long layerId = getSelectedModuleId();
		DefinitionController.getInstance().moveLayerUp(layerId);
	}
	private void moveLayerDown() {
		long layerId = getSelectedModuleId();
		DefineDomainService.getInstance().moveLayerDown(layerId);
	}
	
	@Override
	public void valueChanged(ListSelectionEvent event) {
		if (event.getSource() == this.moduleTreeJList && !event.getValueIsAdjusting()) {
			this.moduleTreeJListAction(event);
		}
	}
	
	public Object getSelectedValue(){
		return moduleTreeJList.getSelectedValue();
	}
	
	public long getSelectedModuleId() {
		Object selected = getSelectedValue();
		if (selected instanceof DataHelper) {
			long id = ((DataHelper) selected).getId();
			return id;
		}
		return -1;
	}
	
	private void moduleTreeJListAction(ListSelectionEvent event) {
		long moduleId = -1;
		Object selectedModule = this.moduleTreeJList.getSelectedValue();
		if (selectedModule instanceof DataHelper) {
			moduleId = ((DataHelper) selectedModule).getId();
		}
		DefinitionController.getInstance().notifyObservers(moduleId);	
	}
}
