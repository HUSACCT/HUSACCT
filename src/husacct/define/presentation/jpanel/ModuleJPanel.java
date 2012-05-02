package husacct.define.presentation.jpanel;

import husacct.define.domain.DefineDomainService;
import husacct.define.presentation.helper.DataHelper;
import husacct.define.presentation.jframe.AddModuleValuesJFrame;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.task.DefinitionController;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.LayerComponent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

import org.apache.log4j.Logger;

/**
 * 
 * @author Henk ter Harmsel
 *
 */
public class ModuleJPanel extends AbstractDefinitionJPanel implements ActionListener, TreeSelectionListener, Observer {

	private static final long serialVersionUID = 6141711414139061921L;
	
	@Deprecated
	private JList moduleTreeJList;
	
	private JScrollPane moduleTreeScrollPane;
	private ModuleTree moduleTree;
	
	private JButton newModuleButton = new JButton();
	private JButton moveModuleUpButton = new JButton();
	private JButton removeModuleButton = new JButton();
	private JButton moveModuleDownButton = new JButton();
	
	public ModuleJPanel() {
		super();
	}

	public void initGui() {
//		DefinitionController.getInstance().addObserver(this);
		BorderLayout modulePanelLayout = new BorderLayout();
		this.setLayout(modulePanelLayout);
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		this.add(createInnerModulePanel(), BorderLayout.CENTER);
		this.updateModuleTree();
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
		this.createModuleTreeScrollPane();
		moduleTreePanel.add(this.moduleTreeScrollPane, BorderLayout.CENTER);
		return moduleTreePanel;
	}
	
	private void createModuleTreeScrollPane() {
		this.moduleTreeScrollPane = new JScrollPane();
		this.moduleTreeScrollPane.setPreferredSize(new java.awt.Dimension(383, 213));
		this.updateModuleTree();
	}

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
		this.updateModuleTree();
	}
	
	public void updateModuleTree() {
		AbstractDefineComponent rootComponent = DefinitionController.getInstance().getModuleTreeComponents();
		this.moduleTree = new ModuleTree(rootComponent);
		this.moduleTreeScrollPane.setViewportView(this.moduleTree);
		this.moduleTree.addTreeSelectionListener(this);
		this.checkLayerComponentIsSelected();
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
		this.updateModuleTree();
	}
	
	private void newModule() {
		this.getSelectedModuleId();
		AddModuleValuesJFrame addModuleFrame = new AddModuleValuesJFrame(this);
		addModuleFrame.initUI();
	}
	
	private void removeModule() {
		long moduleId = getSelectedModuleId();
		if (moduleId != -1){
			boolean confirm = UiDialogs.confirmDialog(this, "Are you sure you want to remove the selected module?", "Remove?");
			if (confirm) {
				this.moduleTree.clearSelection();
				DefinitionController.getInstance().removeModuleById(moduleId);
				this.updateModuleTree();
			}
		}
	}
	
	private void moveLayerUp() {
		long layerId = getSelectedModuleId();
		DefinitionController.getInstance().moveLayerUp(layerId);
		this.updateModuleTree();
	}
	
	private void moveLayerDown() {
		long layerId = getSelectedModuleId();
		DefineDomainService.getInstance().moveLayerDown(layerId);
		this.updateModuleTree();
	}
	
	private long getSelectedModuleId() {
		long moduleId = -1;
		TreePath path = this.moduleTree.getSelectionPath();
		if (path != null){//returns null if nothing is selected
			AbstractDefineComponent selectedComponent = (AbstractDefineComponent) path.getLastPathComponent();
			moduleId = selectedComponent.getModuleId();
		}
		Logger.getLogger(this.getClass()).debug("moduleId " + moduleId);
		return moduleId;
	}
	
	@Deprecated
	public void valueChanged(ListSelectionEvent event) {
		if (event.getSource() == this.moduleTreeJList && !event.getValueIsAdjusting()) {
			this.moduleTreeJListAction(event);
		}
	}
	
	@Deprecated
	private void moduleTreeJListAction(ListSelectionEvent event) {
		long moduleId = -1;
		Object selectedModule = this.moduleTreeJList.getSelectedValue();
		if (selectedModule instanceof DataHelper) {
			moduleId = ((DataHelper) selectedModule).getId();
		}
		DefinitionController.getInstance().notifyObservers(moduleId);	
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent event) {
        TreePath path = event.getPath();
        AbstractDefineComponent selectedComponent = (AbstractDefineComponent) path.getLastPathComponent();
        this.updateSelectedModule(selectedComponent.getModuleId());
        this.checkLayerComponentIsSelected();
	}
	
	private void updateSelectedModule(long moduleId) {
		if(moduleId != -1) {
			DefinitionController.getInstance().setSelectedModuleId(moduleId);
		} else {
			//TODO:: load SofwareArchitecturData
		}
	}
	
	public void checkLayerComponentIsSelected() {
		TreePath path = this.moduleTree.getSelectionPath();
		if(path != null && path.getLastPathComponent() instanceof LayerComponent) {
			this.enableMoveLayerButtons();
		} else {
			this.disableMoveLayerButtons();
		}
	}
	
	public void disableMoveLayerButtons() {
		this.moveModuleDownButton.setEnabled(false);
		this.moveModuleUpButton.setEnabled(false);
	}
	
	public void enableMoveLayerButtons() {
		this.moveModuleDownButton.setEnabled(true);
		this.moveModuleUpButton.setEnabled(true);
	}
}
