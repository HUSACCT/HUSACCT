package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.help.presentation.HelpableJDialog;
import husacct.control.ControlServiceImpl;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class SoftwareUnitResultJDialog extends HelpableJDialog implements ActionListener {

	private static final long serialVersionUID = 7060253504620240808L;
	private JButton saveButton;
	private JButton backButton;
	private JButton selectAllButton;
	private JButton deSelectAllButton;
	private AnalyzedModuleTree resultTree;
	private SoftwareUnitJDialog previousSoftwareUnitJDialog;
	
	public SoftwareUnitResultJDialog(long moduleId, AnalyzedModuleTree resultTree, String enteredRegEx, SoftwareUnitJDialog previousSoftwareUnitJDialog) {
		super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), true);
		this.resultTree = resultTree;
		this.previousSoftwareUnitJDialog = previousSoftwareUnitJDialog;
		initUI();
	}
	
	private void initUI() {
		try {
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ResultsTitle"));
			setIconImage(new ImageIcon(Resource.get(Resource.HUSACCT_LOGO)).getImage());
			
			this.getContentPane().add(this.createResultPanel(), BorderLayout.CENTER);
			this.getContentPane().add(this.createButtonPanel(), BorderLayout.SOUTH);

			this.setResizable(false);
			this.setSize(500, 100);
			this.pack();
			ServiceProvider.getInstance().getControlService().centerDialog(this);
			this.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JPanel createResultPanel() {
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout(1,1));
		resultPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		
		JScrollPane softwareUnitScrollPane = new JScrollPane();
		//softwareUnitScrollPane.setSize(50, 50);
		softwareUnitScrollPane.setPreferredSize(new java.awt.Dimension(500, 300));
		softwareUnitScrollPane.setViewportView(resultTree);
		int[] selectionRows = new int[resultTree.getRowCount()-1];
		for(int i=1; i<resultTree.getRowCount(); i++){
			selectionRows[i-1] = i;
       }
		resultTree.setSelectionRows(selectionRows);
		resultPanel.add(softwareUnitScrollPane);
		
		return resultPanel;
	}
	
	private JPanel createButtonPanel() {
		JPanel buttonPanel = new JPanel();
		
		selectAllButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SelectAll"));
		buttonPanel.add(selectAllButton);
		selectAllButton.addActionListener(this);
		
		deSelectAllButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("DeselectAll"));
		buttonPanel.add(deSelectAllButton);
		deSelectAllButton.addActionListener(this);
		
		saveButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Add"));
		buttonPanel.add(saveButton);
		saveButton.addActionListener(this);
		
		backButton = new JButton(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Back"));
		buttonPanel.add(backButton);
		backButton.addActionListener(this);
		
		return buttonPanel;
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == this.saveButton) {
			this.save();
		} else if (action.getSource() == this.backButton) {
			this.cancel();
		}
		else if (action.getSource() == this.selectAllButton) {
			int[] selectionRows = new int[resultTree.getRowCount()-1];
			for(int i=1; i<resultTree.getRowCount(); i++){
				selectionRows[i-1] = i;
	       }
			resultTree.setSelectionRows(selectionRows);
		}
		else if (action.getSource() == this.deSelectAllButton) {
			resultTree.setSelectionRow(-1);
		}
	}
	
	private void save() {
		TreeSelectionModel paths = resultTree.getSelectionModel();
		ArrayList<AnalyzedModuleComponent> components = new ArrayList<>();
		for (TreePath path : paths.getSelectionPaths()){
			components.add((AnalyzedModuleComponent) path.getLastPathComponent());	
		}
		//SoftwareUnitController unitController = new SoftwareUnitController(DefinitionController.getInstance().getSelectedModuleId());
		//unitController.saveRegEx(components, enteredRegEx);
		this.dispose();
	}
	
	private void cancel() {
		this.dispose();
		previousSoftwareUnitJDialog.setVisible(true);
	}
}
