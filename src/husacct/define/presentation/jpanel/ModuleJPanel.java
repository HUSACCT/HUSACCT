package husacct.define.presentation.jpanel;

import husacct.ServiceProvider;
import husacct.common.services.IServiceListener;
import husacct.control.presentation.util.DialogUtils;
import husacct.define.domain.services.DomainGateway;
import husacct.define.presentation.draganddrop.customdroptargetlisterner.ModuleDropTarget;
import husacct.define.presentation.draganddrop.customtransferhandlers.ModuleTrasferhandler;
import husacct.define.presentation.jdialog.AddModuleValuesJDialog;
import husacct.define.presentation.jpopup.ModuletreeContextMenu;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.presentation.utils.UiDialogs;
import husacct.define.task.DefinitionController;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.components.LayerComponent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class ModuleJPanel extends JPanel implements ActionListener,
		TreeSelectionListener, Observer, IServiceListener, KeyListener {

	private static final long serialVersionUID = 6141711414139061921L;

	private JMenuItem addModuleItem = new JMenuItem();
	private ModuleTree moduleTree;

	private JScrollPane moduleTreeScrollPane;
	private JButton moveModuleDownButton = new JButton();
	private JMenuItem moveModuleDownItem = new JMenuItem();
	private JButton moveModuleUpButton = new JButton();

	private JMenuItem moveModuleUpItem = new JMenuItem();
	private JButton newModuleButton = new JButton();
	private JPopupMenu popupMenu = new JPopupMenu();
	private JButton removeModuleButton = new JButton();
	private JMenuItem removeModuleItem = new JMenuItem();

	public ModuleJPanel() {
		super();

	}

	/**
	 * Handling ActionPerformed
	 */
	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == newModuleButton
				|| action.getSource() == addModuleItem) {
			newModule();
		} else if (action.getSource() == removeModuleButton
				|| action.getSource() == removeModuleItem) {
			removeModule();
		} else if (action.getSource() == moveModuleUpButton
				|| action.getSource() == moveModuleUpItem) {
			moveLayerUp();
		} else if (action.getSource() == moveModuleDownButton
				|| action.getSource() == moveModuleDownItem) {
			moveLayerDown();
		}
		updateModuleTree();
	}

	protected JPanel addButtonPanel() {
		JPanel buttonPanel = new JPanel();
		buttonPanel.setLayout(createButtonPanelLayout());
		buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 0, 0, 0));

		newModuleButton = new JButton();
		buttonPanel.add(newModuleButton);
		newModuleButton.addActionListener(this);
		newModuleButton.addKeyListener(this);

		moveModuleUpButton = new JButton();
		buttonPanel.add(moveModuleUpButton);
		moveModuleUpButton.addActionListener(this);
		moveModuleUpButton.addKeyListener(this);

		removeModuleButton = new JButton();
		buttonPanel.add(removeModuleButton);
		removeModuleButton.addActionListener(this);
		removeModuleButton.addKeyListener(this);

		moveModuleDownButton = new JButton();
		buttonPanel.add(moveModuleDownButton);
		moveModuleDownButton.addActionListener(this);
		moveModuleDownButton.addKeyListener(this);

		setButtonTexts();
		return buttonPanel;
	}

	// Has side effects, might wanna change?
	public void checkLayerComponentIsSelected() {
		TreePath path = moduleTree.getSelectionPath();
		if (path != null
				&& path.getLastPathComponent() instanceof LayerComponent) {
			enableMoveLayerObjects();
		} else {
			disableMoveLayerObjects();
		}
	}

	private GridLayout createButtonPanelLayout() {
		GridLayout buttonPanelLayout = new GridLayout(2, 2);
		buttonPanelLayout.setColumns(2);
		buttonPanelLayout.setHgap(5);
		buttonPanelLayout.setVgap(5);
		buttonPanelLayout.setRows(2);
		return buttonPanelLayout;
	}

	public JPanel createInnerModulePanel() {
		JPanel innerModulePanel = new JPanel();
		BorderLayout innerModulePanelLayout = new BorderLayout();
		innerModulePanel.setLayout(innerModulePanelLayout);
		innerModulePanel.setBorder(BorderFactory
				.createTitledBorder(ServiceProvider.getInstance()
						.getLocaleService()
						.getTranslatedString("ModuleHierachy")));
		innerModulePanel.add(createModuleTreePanel(), BorderLayout.CENTER);
		innerModulePanel.add(addButtonPanel(), BorderLayout.SOUTH);
		return innerModulePanel;
	}

	private JPanel createModuleTreePanel() {
		JPanel moduleTreePanel = new JPanel();

		BorderLayout moduleTreePanelLayout = new BorderLayout();
		moduleTreePanel.setLayout(moduleTreePanelLayout);
		createModuleTreeScrollPane();
		moduleTreePanel.add(moduleTreeScrollPane, BorderLayout.CENTER);

		return moduleTreePanel;
	}

	private void createModuleTreeScrollPane() {
		moduleTreeScrollPane = new JScrollPane();
		moduleTreeScrollPane.setPreferredSize(new java.awt.Dimension(383, 213));
		updateModuleTree();
	}

	private void createPopup(MouseEvent event) {
		if (SwingUtilities.isRightMouseButton(event)) {
			int row = moduleTree.getClosestRowForLocation(event.getX(),
					event.getY());
			moduleTree.setSelectionRow(row);
			checkLayerComponentIsSelected();
			popupMenu.show(moduleTree, event.getX(), event.getY());
		}
	}

	private void createPopupMenu() {
		addModuleItem = new JMenuItem(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("NewModule"));
		addModuleItem.addActionListener(this);
		removeModuleItem = new JMenuItem(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("RemoveModule"));
		removeModuleItem.addActionListener(this);
		moveModuleUpItem = new JMenuItem(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("MoveUp"));
		moveModuleUpItem.addActionListener(this);
		moveModuleDownItem = new JMenuItem(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("MoveDown"));
		moveModuleDownItem.addActionListener(this);

		popupMenu.add(addModuleItem);
		popupMenu.add(removeModuleItem);
		popupMenu.add(moveModuleUpItem);
		popupMenu.add(moveModuleDownItem);
	}

	public void disableMoveLayerObjects() {
		moveModuleDownButton.setEnabled(false);
		moveModuleUpButton.setEnabled(false);
		moveModuleDownItem.setEnabled(false);
		moveModuleUpItem.setEnabled(false);
	}

	public void enableMoveLayerObjects() {
		moveModuleDownButton.setEnabled(true);
		moveModuleUpButton.setEnabled(true);
		moveModuleDownItem.setEnabled(true);
		moveModuleUpItem.setEnabled(true);
	}

	private long getSelectedModuleId() {
		long moduleId = -1;

		TreePath path = moduleTree.getSelectionPath();
		if (path != null) {// returns null if nothing is selected
			AbstractDefineComponent selectedComponent = (AbstractDefineComponent) path
					.getLastPathComponent();
			moduleId = selectedComponent.getModuleId();
		}

		return moduleId;

	}

	public void initGui() {
		DefinitionController.getInstance().addObserver(this);
		BorderLayout modulePanelLayout = new BorderLayout();
		setLayout(modulePanelLayout);
		setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		this.add(createInnerModulePanel(), BorderLayout.CENTER);
		updateModuleTree();
		ServiceProvider.getInstance().getControlService()
				.addServiceListener(this);
		createPopupMenu();
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			if (event.getSource() == newModuleButton) {
				newModule();
			} else if (event.getSource() == removeModuleButton) {
				removeModule();
			} else if (event.getSource() == moveModuleUpButton) {
				moveLayerUp();
			} else if (event.getSource() == moveModuleDownButton) {
				moveLayerDown();
			}
			updateModuleTree();
		}
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	public void moveLayerDown() {
		long layerId = getSelectedModuleId();
		DomainGateway.getInstance().moveLayerDown(layerId);

		updateModuleTree();
	}

	public void moveLayerUp() {
		long layerId = getSelectedModuleId();
		DomainGateway.getInstance().moveLayerUp(layerId);

		updateModuleTree();
	}

	public void newModule() {
		AddModuleValuesJDialog addModuleFrame = new AddModuleValuesJDialog(this);
		DialogUtils.alignCenter(addModuleFrame);
		addModuleFrame.initGUI();
	}

	public void removeModule() {
		long moduleId = getSelectedModuleId();
		HashMap<String, Object> moduleDetails = DefinitionController
				.getInstance().getModuleDetails(moduleId);
		if (moduleDetails.get("type").equals("Facade")) {
			boolean confirm = UiDialogs.confirmDialog(this,
					ServiceProvider.getInstance().getLocaleService()
							.getTranslatedString("RemoveConfirm"),
					ServiceProvider.getInstance().getLocaleService()
							.getTranslatedString("RemovePopupTitle"));
			if (confirm) {
				JOptionPane.showMessageDialog(this,
						ServiceProvider.getInstance().getLocaleService()
								.getTranslatedString("DefaultModule"),
						"Message", JOptionPane.WARNING_MESSAGE);
				return;
			}
		} else if (moduleId != -1 && moduleId != 0) {
			boolean confirm = UiDialogs.confirmDialog(this,
					ServiceProvider.getInstance().getLocaleService()
							.getTranslatedString("RemoveConfirm"),
					ServiceProvider.getInstance().getLocaleService()
							.getTranslatedString("RemovePopupTitle"));
			if (confirm) {
				moduleTree.clearSelection();
				DefinitionController.getInstance().removeModuleById(moduleId);
			}
		}
	}

	private void setButtonTexts() {
		newModuleButton.setText(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("NewModule"));
		moveModuleUpButton.setText(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("MoveUp"));
		removeModuleButton.setText(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("RemoveModule"));
		moveModuleDownButton.setText(ServiceProvider.getInstance()
				.getLocaleService().getTranslatedString("MoveDown"));
	}

	@Override
	public void update() {
		setButtonTexts();
	}

	/**
	 * Observer
	 */
	@Override
	public void update(Observable o, Object arg) {
		updateModuleTree();
	}

	public void updateModuleTree() {
		AbstractDefineComponent rootComponent = DefinitionController
				.getInstance().getModuleTreeComponents();

		moduleTree = new ModuleTree(rootComponent);
		moduleTree.setContextMenu(new ModuletreeContextMenu(this));
		;
		moduleTreeScrollPane.setViewportView(moduleTree);
		moduleTree.addTreeSelectionListener(this);
		checkLayerComponentIsSelected();

		
		ModuleDropTarget moduledroptarget = new ModuleDropTarget(moduleTree);

		moduleTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				createPopup(event);
			}

			@Override
			public void mouseEntered(MouseEvent event) {
				createPopup(event);
			}

			@Override
			public void mousePressed(MouseEvent event) {
				createPopup(event);
			}
		});

		moduleTree.setSelectedRow(DefinitionController.getInstance()
				.getSelectedModuleId());

		for (int i = 0; i < moduleTree.getRowCount(); i++) {
			moduleTree.expandRow(i);
		}
	}

	private void updateSelectedModule(long moduleId) {
		DefinitionController.getInstance().setSelectedModuleId(moduleId);
	}

	@Override
	public void valueChanged(TreeSelectionEvent event) {
		TreePath path = event.getPath();
		AbstractDefineComponent selectedComponent = (AbstractDefineComponent) path
				.getLastPathComponent();
		if (selectedComponent.getModuleId() != DefinitionController
				.getInstance().getSelectedModuleId()) {
			updateSelectedModule(selectedComponent.getModuleId());
		}
		checkLayerComponentIsSelected();
	}

}