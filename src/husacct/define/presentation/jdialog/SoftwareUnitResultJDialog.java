package husacct.define.presentation.jdialog;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.control.ControlServiceImpl;
import husacct.control.presentation.util.DialogUtils;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.task.JtreeController;
import husacct.define.task.PopUpController;
import husacct.define.task.SoftwareUnitController;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.WindowConstants;

public class SoftwareUnitResultJDialog extends JDialog implements ActionListener {

	private static final long serialVersionUID = 7060253504620240808L;
	
	private SoftwareUnitController softwareUnitController;
	private long _moduleId;
	private JButton saveButton;
	private JButton cancelButton;
	
	
	private AnalyzedModuleTree softwareDefinitionTree;
	
	public SoftwareUnitResultJDialog(long moduleId, AnalyzedModuleTree softwareDefinitionTree) {
		super(((ControlServiceImpl) ServiceProvider.getInstance().getControlService()).getMainController().getMainGui(), true);
		_moduleId=moduleId;
		this.softwareDefinitionTree = softwareDefinitionTree;
		this.softwareUnitController = new SoftwareUnitController(moduleId);
		this.softwareUnitController.setAction(PopUpController.ACTION_NEW);
		initUI();
	}
	
	private void initUI() {
		try {
			DialogUtils.alignCenter(this);
			setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			setTitle(ServiceProvider.getInstance().getLocaleService().getTranslatedString("SoftwareUnitTitle"));
			setIconImage(new ImageIcon(Resource.get(Resource.HUSACCT_LOGO)).getImage());
			
			
			this.getContentPane().add(createResultPanel(), BorderLayout.NORTH);
			this.getContentPane().add(createButtonPanel(), BorderLayout.SOUTH);
			this.setVisible(true);

			this.setResizable(false);
			this.setSize(650, 300);
			this.pack();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private JPanel createButtonPanel() {
		saveButton = new JButton("save");
		cancelButton = new JButton("cancel");
		saveButton.addActionListener(this);
		cancelButton.addActionListener(this);
		JSplitPane	splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,saveButton,cancelButton);
		JPanel panel= new JPanel();
		panel.add(splitPane);
		panel.setVisible(true);
		return panel;
	}

	private JPanel createResultPanel() {
		JPanel resultPanel = new JPanel();
		resultPanel.setLayout(new GridLayout(2,2));
		resultPanel.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		
		
		JScrollPane softwareUnitScrollPane = new JScrollPane();
		softwareUnitScrollPane.setSize(200, 200);
		softwareUnitScrollPane.setPreferredSize(new java.awt.Dimension(200, 200));
		softwareUnitScrollPane.setViewportView(softwareDefinitionTree);
		resultPanel.add(softwareUnitScrollPane);
		
		return resultPanel;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==saveButton)
		{
			JtreeController.registerResultRemovals();
		}
		if (e.getSource()==cancelButton) {
			this.dispose();
		}
		
	}

}
