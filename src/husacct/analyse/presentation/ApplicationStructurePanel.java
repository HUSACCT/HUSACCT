package husacct.analyse.presentation;

import husacct.common.dto.AnalysedModuleDTO;
import java.awt.Color;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.JScrollPane; 
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.UIManager;

class ApplicationStructurePanel extends JPanel implements TreeSelectionListener{  
	
	private static final long serialVersionUID = 1L;
	private JTree analyzedCodeTree;
	private JScrollPane jScrollPaneTree;
	
	private AnalyseUIController dataControl;
	
	public ApplicationStructurePanel(){
		dataControl = new AnalyseUIController();
		createPanel();
	} 
	
	private void createPanel() {
		
		AnalysedModuleDTO rootModule = new AnalysedModuleDTO("", "", "", "");
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootModule);
		
		List<AnalysedModuleDTO> rootModules = dataControl.getRootModules();
		for(AnalysedModuleDTO module : rootModules){
			DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(module);
			root.add(rootNode);    
		}
     	
    	analyzedCodeTree = new JTree(root);
    	analyzedCodeTree.setBackground(UIManager.getColor("Panel.background"));
		analyzedCodeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		analyzedCodeTree.addTreeSelectionListener(this);
		jScrollPaneTree = new JScrollPane(analyzedCodeTree);
		jScrollPaneTree.setBackground(UIManager.getColor("Panel.background"));
		jScrollPaneTree.setBorder(null);
		jScrollPaneTree.setBackground(getBackground());
		
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addContainerGap()
					.addComponent(jScrollPaneTree, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(5)
					.addComponent(jScrollPaneTree, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
					.addContainerGap())
		);
		
		DefaultTreeCellRenderer renderer = new SoftwareTreeCellRenderer();
		renderer.setBackground(UIManager.getColor("Panel.background"));
		renderer.setBackgroundNonSelectionColor(UIManager.getColor("Panel.background"));
		renderer.setBackgroundSelectionColor(UIManager.getColor("Panel.background"));
		renderer.setTextNonSelectionColor(Color.black);
		renderer.setTextSelectionColor(Color.black);
		analyzedCodeTree.setCellRenderer(renderer);
		setLayout(groupLayout);
	} 
	
	@Override
	public void valueChanged(TreeSelectionEvent eventTree) {
		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) analyzedCodeTree.getLastSelectedPathComponent();
    	AnalysedModuleDTO selectedModule = (AnalysedModuleDTO)currentNode.getUserObject();
		List<AnalysedModuleDTO> children = dataControl.getModulesInModules(selectedModule.uniqueName);
		if(!children.isEmpty()){
			for(AnalysedModuleDTO child: children){
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
			 	currentNode.add(childNode); 
			} 
		}
	}
 }
