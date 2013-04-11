package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.control.ControlServiceImpl;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.task.JtreeController;
import husacct.define.task.PopUpController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;
import husacct.define.task.conventions_checker.AnalyzedComponentHelper;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class SoftwareUnitJDialog extends JDialog implements ActionListener, KeyListener {

	private static final long serialVersionUID = 3093579720278942807L;
	
	public JButton saveButton;
	public JButton cancelButton;
	
	public AnalyzedModuleTree softwareDefinitionTree;
	private SoftwareUnitController softwareUnitController;
	private long _moduleId;
	
	public SoftwareUnitJDialog(long moduleId) {
		super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), true);
		_moduleId=moduleId;
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
			
			this.getContentPane().add(this.createSofwareUnitsPanel(), BorderLayout.CENTER);
			this.getContentPane().add(this.createButtonPanel(), BorderLayout.SOUTH);
			
			this.setResizable(false);
			this.pack();
			this.setSize(650, 300);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JPanel createSofwareUnitsPanel() {
		JPanel softwareUnitsPanel = new JPanel();
		softwareUnitsPanel.setLayout(this.createSoftwareUnitsPanelLayout());
		softwareUnitsPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		JLabel softwareUnitsLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SelectSoftwareDefinition"));
		softwareUnitsPanel.add(softwareUnitsLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		softwareUnitsPanel.add(this.getSoftwareUnitScrollPane(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 220));
		return softwareUnitsPanel;
	}
	
	private GridBagLayout createSoftwareUnitsPanelLayout() {
		GridBagLayout softwareUnitsPanelLayout = new GridBagLayout();
		softwareUnitsPanelLayout.rowWeights = new double[] { 0.0, 0.0, 0.1 };
		softwareUnitsPanelLayout.rowHeights = new int[] { 25, 25, 220 };
		softwareUnitsPanelLayout.columnWeights = new double[] { 0.0 };
		softwareUnitsPanelLayout.columnWidths = new int[] { 500 };
		return softwareUnitsPanelLayout;
	}
	
	private JScrollPane getSoftwareUnitScrollPane() {
		JScrollPane softwareUnitScrollPane = new JScrollPane();
		softwareUnitScrollPane.setSize(400, 220);
		softwareUnitScrollPane.setPreferredSize(new java.awt.Dimension(500, 220));
		if(JtreeController.instance().isLoaded())
		{
			this.softwareDefinitionTree= JtreeController.instance().getTree();
			softwareUnitScrollPane.setViewportView(this.softwareDefinitionTree);
		}else {
		AnalyzedModuleComponent rootComponent = this.softwareUnitController.getSoftwareUnitTreeComponents();
		AnalyzedComponentHelper helpercheker= new AnalyzedComponentHelper();
		helpercheker.chekIfDataIsTheSame(rootComponent);
		this.softwareDefinitionTree = new AnalyzedModuleTree(rootComponent);
		JtreeController.instance().setCurrentTree(this.softwareDefinitionTree);
		softwareUnitScrollPane.setViewportView(this.softwareDefinitionTree);
		JtreeController.instance().setLoadState(true);
		}
		return softwareUnitScrollPane;
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		
		saveButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		buttonPanel.add(saveButton);
		saveButton.addActionListener(this);
		
		cancelButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Cancel"));
		buttonPanel.add(cancelButton);
		cancelButton.addActionListener(this);
		
		return buttonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.saveButton) {
			this.save();
		} else if (action.getSource() == this.cancelButton) {
			this.cancel();
		}
	}
	
	/**
	 * Do nothing
	 */
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
	
	/**
	 * Do nothing
	 */
	@Override
	public void keyTyped(KeyEvent event) {
		
	}

	private void save() {
		TreeSelectionModel paths = this.softwareDefinitionTree.getSelectionModel();
		for (TreePath path : paths.getSelectionPaths()){
			AnalyzedModuleComponent selectedComponent = (AnalyzedModuleComponent) path.getLastPathComponent();
			this.softwareDefinitionTree.removeTreeItem(_moduleId,selectedComponent);
			this.softwareUnitController.save(selectedComponent.getUniqueName(), selectedComponent.getType());			
		}
		this.dispose();
		
		

	}
	
	private void cancel() {
		this.dispose();
	}
}
