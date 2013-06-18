package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.control.ControlServiceImpl;
import husacct.define.domain.services.DomainGateway;
import husacct.define.presentation.draganddrop.customdroptargetlisterner.SoftwareUnitDropListerner;
import husacct.define.presentation.draganddrop.customtransferhandlers.ModuleTrasferhandler;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.utils.DefaultMessages;
import husacct.define.presentation.utils.ExpressionEngine;
import husacct.define.task.DefinitionController;
import husacct.define.task.JtreeController;
import husacct.define.task.PopUpController;
import husacct.define.task.SoftwareUnitController;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class SoftwareUnitJDialog extends JDialog implements ActionListener, KeyListener {

	private static final long serialVersionUID = 3093579720278942807L;
	
	private JPanel UIMappingPanel;
	private JPanel regExMappingPanel;
	
	private JButton saveButton;
	private JButton cancelButton;
	
	private JRadioButton UIMapping;
	private JRadioButton regExMapping;
	private JButton testButton;
	private JTextField regExTextField;
	
	private JLabel dynamicRegExLabel;
	
	private JCheckBox packageCheckBox;
	private JCheckBox classCheckBox;
	
	public AnalyzedModuleTree softwareDefinitionTree;
	private SoftwareUnitController softwareUnitController;
	private long _moduleId;
	
	public SoftwareUnitJDialog(long moduleId) {
		super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), false);
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
			
			this.getContentPane().add(this.createTypeSelectionPanel(), BorderLayout.NORTH);
			this.getContentPane().add(this.createUIMappingPanel(), BorderLayout.CENTER);
			this.getContentPane().add(this.createButtonPanel(), BorderLayout.SOUTH);
			
			UIMapping.setEnabled(false);
			this.setResizable(false);
			this.softwareDefinitionTree.addTreeSelectionListener(treeselectionListener);
		
		
			
		
			this.setSize(650, 300);
			this.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JPanel createTypeSelectionPanel() {
		JPanel typeSelectionPanel = new JPanel();
		typeSelectionPanel.setLayout(new GridLayout(2,2));
		typeSelectionPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		JLabel typeSelectionLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SelectSoftwareDefinitionType"));
		Font bold=new Font(typeSelectionLabel.getFont().getName(),Font.BOLD,typeSelectionLabel.getFont().getSize());  
		typeSelectionLabel.setFont(bold);
		typeSelectionPanel.add(typeSelectionLabel);
		typeSelectionPanel.add(new JLabel(""));
		
		UIMapping = new JRadioButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("UIMapping"));
		UIMapping.setSelected(true);
		UIMapping.addActionListener(this);
		
		regExMapping = new JRadioButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RegExMapping"));
		regExMapping.addActionListener(this);
		
		ButtonGroup mappingRadioButtonsGroup = new ButtonGroup();
		mappingRadioButtonsGroup.add(UIMapping);
		mappingRadioButtonsGroup.add(regExMapping);
		
		typeSelectionPanel.add(UIMapping);
		typeSelectionPanel.add(regExMapping);
		
		return typeSelectionPanel;
	}
	
	private JPanel createUIMappingPanel() {
		if(regExMappingPanel != null)
			this.getContentPane().remove(regExMappingPanel);
		
		UIMappingPanel = new JPanel();
		UIMappingPanel.setLayout(this.createUIMappingPanelLayout());
		UIMappingPanel.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		
		JLabel softwareUnitsLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SelectSoftwareDefinition"));
		UIMappingPanel.add(softwareUnitsLabel, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.FIRST_LINE_START, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
		UIMappingPanel.add(this.getUIMappingScrollPane(), new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 220));
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
		JScrollPane softwareUnitScrollPane = new JScrollPane();
		softwareUnitScrollPane.setSize(400, 220);
		softwareUnitScrollPane.setPreferredSize(new java.awt.Dimension(500, 220));
		getSoftwareDefinationTree();
		softwareUnitScrollPane.setViewportView(this.softwareDefinitionTree);
		return softwareUnitScrollPane;
	}
	


	private JPanel createRegExMappingPanel() {
		this.getContentPane().remove(UIMappingPanel);
		
		regExMappingPanel = new JPanel();
		regExMappingPanel.setLayout(new GridLayout(7,3));
		regExMappingPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		
		packageCheckBox = new JCheckBox("Packages");
		classCheckBox = new JCheckBox("Classes");
		packageCheckBox.setSelected(true);
		classCheckBox.setSelected(true);
		
		JLabel packageClassChoice = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ChooseClassOrPackage"));
		regExMappingPanel.add(packageClassChoice);
		Font bold=new Font(packageClassChoice.getFont().getName(),Font.BOLD,packageClassChoice.getFont().getSize());  
		packageClassChoice.setFont(bold);
		regExMappingPanel.add(new JLabel(""));
		
		regExMappingPanel.add(packageCheckBox);
		regExMappingPanel.add(classCheckBox);
		
		regExMappingPanel.add(new JLabel(""));
		regExMappingPanel.add(new JLabel(""));
		
		JLabel regExLabel = new JLabel();
		regExMappingPanel.add(regExLabel);
		regExMappingPanel.add(new JLabel(""));
		regExLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RegularExpression"));
		
		regExTextField = new JTextField();
		regExTextField.setToolTipText(DefaultMessages.TIP_REGEXLANGUAGE);
		 testButton = new JButton();
		testButton.setText("add");
		testButton.addActionListener(this);
		regExTextField.getDocument().addDocumentListener(new DocumentListener() {
			public void changedUpdate(DocumentEvent e) {
				updateDynamicRegExField();
			}
			public void removeUpdate(DocumentEvent e) {
				updateDynamicRegExField();
			}
			public void insertUpdate(DocumentEvent e) {
				updateDynamicRegExField();
			}
			});
		
		regExMappingPanel.add(regExTextField);
		regExMappingPanel.add(new JLabel(""));
		dynamicRegExLabel = new JLabel(ServiceProvider.getInstance().getLocaleService().getTranslatedString("EnterRegExLabel"));
		regExMappingPanel.add(dynamicRegExLabel);
		
		return regExMappingPanel;
	}
	
	
	private void getSoftwareDefinationTree() {
	this.softwareDefinitionTree=new AnalyzedModuleTree(DomainGateway.getInstance().treeModel());
	this.softwareDefinitionTree.setTransferHandler(new ModuleTrasferhandler());
	 SoftwareUnitDropListerner  dropListener = new SoftwareUnitDropListerner (softwareDefinitionTree);
	this.softwareDefinitionTree.setDragEnabled(true);
	
	
		
	}
	
	private void updateDynamicRegExField() {
		String enteredText = regExTextField.getText();
		if(enteredText.startsWith("*") && enteredText.endsWith("*")) {
			enteredText = enteredText.replace("*", "");
			dynamicRegExLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RegExContains") + " '" + enteredText + "'");
		}
		else if(enteredText.startsWith("*")) {
			enteredText = enteredText.replace("*", "");
			dynamicRegExLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RegExEndsWith") + " '" + enteredText + "'");
		}
		else if(enteredText.endsWith("*")) {
			enteredText = enteredText.replace("*", "");
			dynamicRegExLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RegExStartsWith") + " '" + enteredText + "'");
		}
		else {
			dynamicRegExLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("RegExIsExactly") + " '" + enteredText + "'");
		}
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		
		saveButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		buttonPanel.add(saveButton);
		saveButton.addActionListener(this);
		saveButton.setEnabled(false);
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
		else if (action.getSource() == this.UIMapping) {
			this.getContentPane().add(this.createUIMappingPanel(), BorderLayout.CENTER);
			UIMapping.setEnabled(false);
			regExMapping.setEnabled(true);
			regExMappingPanel = null;
			saveButton.setEnabled(false);
			this.pack();
		}
		else if (action.getSource() == this.regExMapping) {
			this.getContentPane().add(this.createRegExMappingPanel(), BorderLayout.CENTER);
			UIMapping.setEnabled(true);
			regExMapping.setEnabled(false);
			saveButton.setEnabled(true);
			this.pack();
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
		_moduleId=DefinitionController.getInstance().getSelectedModuleId();
		boolean canclose= false;
		if(regExMappingPanel != null) {
			ExpressionEngine expressionEngine = new ExpressionEngine();
			if(!regExTextField.getText().equals("")) {
				if(packageCheckBox.isSelected() || classCheckBox.isSelected()) {
					
					AnalyzedModuleTree resultTree = JtreeController.instance().getResultTree();
					if(packageCheckBox.isSelected() && classCheckBox.isSelected()) {
							expressionEngine.saveRegExToResultTree(regExTextField.getText(), "PC");
					}
					else if(packageCheckBox.isSelected()) {
						expressionEngine.saveRegExToResultTree(regExTextField.getText(), "P");
					}
					else if(classCheckBox.isSelected()) {
						expressionEngine.saveRegExToResultTree(regExTextField.getText(), "C");
					}
					this.dispose();
					new SoftwareUnitResultJDialog(_moduleId, resultTree, regExTextField.getText(), this);
				}
				else {
					JOptionPane.showMessageDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("SelectRegExCheckBoxError"),  "Message", JOptionPane.WARNING_MESSAGE);
				}
			}
			else {
				JOptionPane.showMessageDialog(this, ServiceProvider.getInstance().getLocaleService().getTranslatedString("FillInRegExError"),  "Message", JOptionPane.WARNING_MESSAGE);
			}
		}
		else {
			TreeSelectionModel paths = this.softwareDefinitionTree.getSelectionModel();
			ArrayList<AnalyzedModuleComponent> units = new ArrayList<AnalyzedModuleComponent>();
			for (TreePath path : paths.getSelectionPaths()){
				AnalyzedModuleComponent selectedComponent = (AnalyzedModuleComponent) path.getLastPathComponent();
				units.add(selectedComponent);
						
			}
			canclose= DomainGateway.getInstance().saveAnalzedModule(units);	
			
			
			if (canclose) {
				
				this.dispose();
			}
			
		}

	}
	
	private void cancel() {
		this.dispose();
	}
	
	private TreeSelectionListener treeselectionListener = new TreeSelectionListener() {
		
		@Override
		public void valueChanged(TreeSelectionEvent arg0) {
			TreeSelectionModel paths = softwareDefinitionTree.getSelectionModel();
			boolean isButtonAddEnabled =true;
			for (TreePath path : paths.getSelectionPaths()){
				
			
				
				AnalyzedModuleComponent selectedComponent = (AnalyzedModuleComponent) path.getLastPathComponent();
				if (selectedComponent.isMapped()|| selectedComponent.getType().toLowerCase().equals("root")||selectedComponent.getType().toLowerCase().equals("application")
						||selectedComponent.getType().toLowerCase().equals("externalpackage")) {
					isButtonAddEnabled=false;
				}
			}
		
			saveButton.setEnabled(isButtonAddEnabled);
	}

	
	};



	


	
}
