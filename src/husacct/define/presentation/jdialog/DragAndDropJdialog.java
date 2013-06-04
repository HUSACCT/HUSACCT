package husacct.define.presentation.jdialog;

import husacct.define.domain.SoftwareArchitecture;
import husacct.define.domain.module.ModuleStrategy;
import husacct.define.domain.services.stateservice.StateService;
import husacct.define.presentation.moduletree.AnalyzedModuleTree;
import husacct.define.presentation.moduletree.CombinedModuleTree;
import husacct.define.presentation.moduletree.ModuleTree;
import husacct.define.presentation.tables.JTableSoftwareUnits;
import husacct.define.task.DefinitionController;
import husacct.define.task.JtreeController;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.define.task.components.AnalyzedModuleComponent;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;

public class DragAndDropJdialog extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JLayeredPane moduleLayerdPane;
	private JLayeredPane softwareMappedUnitLayerdPane;
	private JLayeredPane softwareUnitsLayerdPane;
	private long selectedModuleId=0;
	private ModuleStrategy data;
	private ModuleTree moduleTree;
	private CombinedModuleTree mappedToModuleTree;
	private AnalyzedModuleTree allunits;
	
	
	public DragAndDropJdialog() {
	init();	
	selectedModuleId=0;
	data = SoftwareArchitecture.getInstance().getModuleById(0);	
	}
	
 public	DragAndDropJdialog(long moduleid)
	{
		init();
		selectedModuleId=moduleid;
		data=SoftwareArchitecture.getInstance().getModuleById(moduleid);
	
		
	}
 public	DragAndDropJdialog(ModuleStrategy module)
	{
		init();
		selectedModuleId=module.getId();
		data=module;
	}
	
	
	
	public void init(){
		
		
		setBounds(100, 100, 1182, 531);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		moduleLayerdPane = new JLayeredPane();
		moduleLayerdPane.setBounds(76, 44, 245, 331);
		moduleLayerdPane.add(getModuleScrollPane());
		contentPanel.add(moduleLayerdPane);
		
		
		JLabel lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(76, 19, 46, 14);
		contentPanel.add(lblNewLabel);
		
		softwareMappedUnitLayerdPane = new JLayeredPane();
		softwareMappedUnitLayerdPane.setBounds(457, 44, 245, 331);
		contentPanel.add(softwareMappedUnitLayerdPane);
		
		JLabel lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(457, 19, 46, 14);
		contentPanel.add(lblNewLabel_1);
		
		JButton btnNewButton = new JButton("add");
		btnNewButton.setBounds(731, 130, 116, 23);
		contentPanel.add(btnNewButton);
		
		JButton button = new JButton("remove");
		button.setBounds(731, 218, 116, 23);
		contentPanel.add(button);
		
		softwareUnitsLayerdPane = new JLayeredPane();
		softwareUnitsLayerdPane.add( getAllMappingScrollPane() );
		softwareUnitsLayerdPane.setBounds(868, 44, 245, 331);
		contentPanel.add(softwareUnitsLayerdPane);
		
		JLabel label = new JLabel("New label");
		label.setBounds(868, 19, 46, 14);
		contentPanel.add(label);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("OK");
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		
	
		
		
		
	}
	
	private JScrollPane getModuleScrollPane() {
		JScrollPane softwareUnitScrollPane = new JScrollPane();
		softwareUnitScrollPane.setSize(400, 220);
		softwareUnitScrollPane.setPreferredSize(new java.awt.Dimension(500, 220));
	  moduleTree =JtreeController.instance().getModuleTree();
	  moduleTree.addTreeSelectionListener(moduleSelectionListener);
		softwareUnitScrollPane.setViewportView(moduleTree);
		return softwareUnitScrollPane;
	}
	
	
	private JScrollPane getMappingToScrollPane() {
		JScrollPane softwareUnitScrollPane = new JScrollPane();
		softwareUnitScrollPane.setSize(400, 220);
		softwareUnitScrollPane.setPreferredSize(new java.awt.Dimension(500, 220));
	   AnalyzedModuleComponent rootofMappedUnits = StateService.instance().getAnalyzedSoftWareUnits(data.getUnits());
		
	   softwareUnitScrollPane.setViewportView(new JTableSoftwareUnits());
		return softwareUnitScrollPane;
	}
	
	private JScrollPane getAllMappingScrollPane() {
		JScrollPane softwareUnitScrollPane = new JScrollPane();
		softwareUnitScrollPane.setSize(400, 220);
		softwareUnitScrollPane.setPreferredSize(new java.awt.Dimension(500, 220));
	   allunits=JtreeController.instance().getTree();
	   softwareUnitScrollPane.setViewportView(allunits);
		return softwareUnitScrollPane;
	}
	
private TreeSelectionListener moduleSelectionListener = new TreeSelectionListener() {
	
	@Override
	public void valueChanged(TreeSelectionEvent event) {
		TreePath path = event.getPath();
		AbstractDefineComponent selectedComponent = (AbstractDefineComponent) path
			.getLastPathComponent();
		if (selectedComponent.getModuleId() != DefinitionController
			.getInstance().getSelectedModuleId()) {
	      data=SoftwareArchitecture.getInstance().getModuleById(selectedComponent.getModuleId());
	
	    softwareMappedUnitLayerdPane.add(getMappingToScrollPane());
	   
	  //JtreeController.instance().selectValueOfAnalyzedModuleTree("");
		}
		
	}
};


private TreeSelectionListener mappedSelectionListener = new TreeSelectionListener() {
	
	@Override
	public void valueChanged(TreeSelectionEvent event) {
		TreePath path =event.getPath();
		
		AnalyzedModuleComponent test = (AnalyzedModuleComponent)path.getLastPathComponent();
	
			//TreePath p = new TreePath(test.getParentofChild());
		//JtreeController.instance().getTree().setSelectionPath(p);
		
	}
};
	
	
	

}
