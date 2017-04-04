package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.help.presentation.HelpableJDialog;
import husacct.control.ControlServiceImpl;
import husacct.define.presentation.draganddrop.customtransferhandlers.ModuleTrasferhandler;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.task.DefinitionController;
import husacct.define.task.JtreeController;
import husacct.define.task.PopUpController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class SoftwareUnitJDialog extends HelpableJDialog implements ActionListener, KeyListener, Observer {

	private static final long serialVersionUID = 3093579720278942807L;

	private JPanel UIMappingPanel;

	private JButton addButton;
	private JButton cancelButton;
	private JScrollPane softwareUnitScrollPane;
	public AnalyzedModuleTree softwareDefinitionTree;
	private SoftwareUnitController softwareUnitController;

	public SoftwareUnitJDialog(long moduleId) {
		super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), false);
		this.softwareUnitController = new SoftwareUnitController(moduleId);
		this.softwareUnitController.setAction(PopUpController.ACTION_NEW);
		initUI();
	}

	/**
	 * Creating Gui
	 */
	private void initUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareUnitTitle"));
			setIconImage(new ImageIcon(Resource.get(Resource.HUSACCT_LOGO)).getImage());
			DefinitionController.getInstance().addObserver(this);
			DefinitionController.getInstance().addObserverWithinDefineOfAnalyse(this);
			this.getContentPane().add(this.createUIMappingPanel(), BorderLayout.CENTER);
			this.getContentPane().add(this.createButtonPanel(), BorderLayout.SOUTH);
			this.setResizable(false);
			this.setSize(650, 300);
			this.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private JPanel createUIMappingPanel() {
		UIMappingPanel = new JPanel();
		UIMappingPanel.setLayout(this.createUIMappingPanelLayout());
		UIMappingPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));

		JLabel softwareUnitsLabel = new JLabel(ServiceProvider.getInstance()
				.getLocaleService()
				.getTranslatedString("SelectSoftwareDefinition"));
		UIMappingPanel.add(softwareUnitsLabel, new GridBagConstraints(0, 1, 1,
				1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		UIMappingPanel.add(this.getUIMappingScrollPane(),
				new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
						GridBagConstraints.CENTER,
						GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0),
						0, 220));
		return UIMappingPanel;
	}

	private GridBagLayout createUIMappingPanelLayout() {
		GridBagLayout UIMappingPanelLayout = new GridBagLayout();
		UIMappingPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
		UIMappingPanelLayout.rowHeights = new int[] { 25, 25, 220 };
		UIMappingPanelLayout.columnWeights = new double[] { 0.0 };
		UIMappingPanelLayout.columnWidths = new int[] { 500 };
		return UIMappingPanelLayout;
	}

	private JScrollPane getUIMappingScrollPane() {
		softwareUnitScrollPane = new JScrollPane();
		softwareUnitScrollPane.setSize(400, 220);
		softwareUnitScrollPane
				.setPreferredSize(new java.awt.Dimension(500, 220));
		getSoftwareDefinationTree();
		softwareUnitScrollPane.setViewportView(this.softwareDefinitionTree);
		return softwareUnitScrollPane;
	}

	private void getSoftwareDefinationTree() {
		this.softwareDefinitionTree = new AnalyzedModuleTree(JtreeController.instance().getRootOfModel());
		this.softwareDefinitionTree.setTransferHandler(new ModuleTrasferhandler());
		this.softwareDefinitionTree.addTreeSelectionListener(treeselectionListener);
		this.softwareDefinitionTree.setDragEnabled(true);
	}

	private TreeSelectionListener treeselectionListener = new TreeSelectionListener() {
		@Override
		public void valueChanged(TreeSelectionEvent arg0) {
			boolean isButtonAddEnabled = true;
/*	Code disabled 2014-03-25, since in some cases the saveButton was disabled incorrectly 		
			for (TreePath path : paths.getSelectionPaths()) {
				AnalyzedModuleComponent selectedComponent = (AnalyzedModuleComponent) path.getLastPathComponent();
				boolean unitIsMappeed = selectedComponent.isMapped();
				if (unitIsMappeed || selectedComponent.getType().toLowerCase().equals("root")
						|| selectedComponent.getType().toLowerCase().equals("application")
						|| selectedComponent.getType().toLowerCase().equals("externalpackage")) {
					isButtonAddEnabled = false;
				}
			}
*/			
			addButton.setEnabled(isButtonAddEnabled);
		}
	};


	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();

		addButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		buttonPanel.add(addButton);
		addButton.addActionListener(this);
		addButton.setEnabled(false);
		cancelButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Cancel"));
		buttonPanel.add(cancelButton);
		cancelButton.addActionListener(this);

		return buttonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.addButton) {
			this.save();
		} else if (action.getSource() == this.cancelButton) {
			this.cancel();
		}
	}

	//Do nothing
	@Override
	public void keyPressed(KeyEvent event) {

	}

	@Override
	public void keyReleased(KeyEvent event) {
		if (event.getKeyCode() == KeyEvent.VK_ESCAPE) {
			this.dispose();
		} else if (event.getKeyCode() == KeyEvent.VK_ENTER) {
			this.save();
		}
	}

	// Do nothing
	@Override
	public void keyTyped(KeyEvent event) {

	}

	private void save() {
		boolean canclose = false;
		TreeSelectionModel paths = this.softwareDefinitionTree.getSelectionModel();
		ArrayList<AnalyzedModuleComponent> units = new ArrayList<AnalyzedModuleComponent>();
		for (TreePath path : paths.getSelectionPaths()) {
			AnalyzedModuleComponent selectedComponent = (AnalyzedModuleComponent) path.getLastPathComponent();
			units.add(selectedComponent);
		}
		canclose = DefinitionController.getInstance().saveAnalzedModule(units);
		if (canclose) {
			this.dispose();
		}
	}

	private void cancel() {
		this.dispose();
	}

	@Override
	public void update(Observable o, Object arg) {
		if (arg instanceof String) {
			if ("updateSoftwareTree".equals(arg)) {
				getSoftwareDefinationTree();
				softwareUnitScrollPane.setViewportView(softwareDefinitionTree);
				SwingUtilities.getWindowAncestor(softwareUnitScrollPane).repaint();
			}
		}

	}

}
