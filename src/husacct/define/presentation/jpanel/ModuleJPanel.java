package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.control.ILocaleChangeListener;
import husacct.control.presentation.util.DialogUtils;
import husacct.define.abstraction.language.DefineTranslator;
import husacct.define.presentation.jdialog.AddModuleValuesJDialog;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.DefinitionController;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.LayerComponent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Locale;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class ModuleJPanel extends JPanel implements ActionListener, TreeSelectionListener, Observer, ILocaleChangeListener, KeyListener {

	private static final long serialVersionUID = 6141711414139061921L;

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
		BorderLayout modulePanelLayout = new BorderLayout();
		this.setLayout(modulePanelLayout);
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		this.add(createInnerModulePanel(), BorderLayout.CENTER);
		this.updateModuleTree();
		ServiceProvider.getInstance().getControlService().addLocaleChangeListener(this);
	}
	
	public JPanel createInnerModulePanel() {
		JPanel innerModulePanel = new JPanel();
		BorderLayout innerModulePanelLayout = new BorderLayout();
		innerModulePanel.setLayout(innerModulePanelLayout);
		innerModulePanel.setBorder(BorderFactory.createTitledBorder(DefineTranslator.translate("ModuleHierachy")));
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
		this.newModuleButton.addActionListener(this);
		this.newModuleButton.addKeyListener(this);
			
		this.moveModuleUpButton = new JButton();
		buttonPanel.add(this.moveModuleUpButton);
		this.moveModuleUpButton.addActionListener(this);
		this.moveModuleUpButton.addKeyListener(this);

		this.removeModuleButton = new JButton();
		buttonPanel.add(this.removeModuleButton);
		this.removeModuleButton.addActionListener(this);
		this.removeModuleButton.addKeyListener(this);

		this.moveModuleDownButton = new JButton();
		buttonPanel.add(this.moveModuleDownButton);
		this.moveModuleDownButton.addActionListener(this);
		this.moveModuleDownButton.addKeyListener(this);
		
		this.setButtonTexts();
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
		
		//FIXME need to get the reselect the node with the currently selectedmoduleid
		
		for (int i = 0; i < moduleTree.getRowCount(); i++) {
			moduleTree.expandRow(i);
		}
	}
	
	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.newModuleButton) {
			this.newModule();
		} else if (action.getSource() == this.removeModuleButton) {
			this.removeModule();
		} else if (action.getSource() == this.moveModuleUpButton) {
			this.moveLayerUp();
		} else if (action.getSource() == this.moveModuleDownButton) {
			this.moveLayerDown();
		}
		this.updateModuleTree();
	}
	
	private void newModule() {
		AddModuleValuesJDialog addModuleFrame = new AddModuleValuesJDialog(this);
		DialogUtils.alignCenter(addModuleFrame);
	}
	
	private void removeModule() {
		long moduleId = getSelectedModuleId();
		if (moduleId != -1){
			boolean confirm = UiDialogs.confirmDialog(this, DefineTranslator.translate("RemoveConfirm"), DefineTranslator.translate("RemovePopupTitle"));
			if (confirm) {
				this.moduleTree.clearSelection();
				DefinitionController.getInstance().removeModuleById(moduleId);
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
		DefinitionController.getInstance().moveLayerDown(layerId);
		this.updateModuleTree();
	}
	
	private long getSelectedModuleId() {
		long moduleId = -1;
		TreePath path = this.moduleTree.getSelectionPath();
		if (path != null){//returns null if nothing is selected
			AbstractDefineComponent selectedComponent = (AbstractDefineComponent) path.getLastPathComponent();
			moduleId = selectedComponent.getModuleId();
		}
		return moduleId;
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
        TreePath path = event.getPath();
        AbstractDefineComponent selectedComponent = (AbstractDefineComponent) path.getLastPathComponent();
        this.updateSelectedModule(selectedComponent.getModuleId());
        this.checkLayerComponentIsSelected();
	}
	
	
	private void updateSelectedModule(long moduleId) {
		DefinitionController.getInstance().setSelectedModuleId(moduleId);
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

	@Override
	public void update(Locale newLocale) {
		this.setButtonTexts();
	}
	
	private void setButtonTexts() {
		this.newModuleButton.setText(DefineTranslator.translate("NewModule"));
		this.moveModuleUpButton.setText(DefineTranslator.translate("MoveUp"));
		this.removeModuleButton.setText(DefineTranslator.translate("RemoveModule"));
		this.moveModuleDownButton.setText(DefineTranslator.translate("MoveDown"));
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ENTER){
			if (event.getSource() == this.newModuleButton) {
				this.newModule();
			} else if (event.getSource() == this.removeModuleButton) {
				this.removeModule();
			} else if (event.getSource() == this.moveModuleUpButton) {
				this.moveLayerUp();
			} else if (event.getSource() == this.moveModuleDownButton) {
				this.moveLayerDown();
			}
			this.updateModuleTree();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}
