package husacct.analyse.presentation;

import husacct.analyse.AnalyseServiceImpl;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;

import java.awt.Dimension;
 
import javax.swing.JInternalFrame; 
import javax.swing.JScrollPane; 
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class AnalyzePanelGetRootModules extends JInternalFrame implements TreeSelectionListener{  
	private static final long serialVersionUID = 1L;
	public IAnalyseService analyseService;
	public JTree analyzedCodeTree;
	public JScrollPane jScrollPaneTree;

	public AnalyzePanelGetRootModules(){
		setFrameSettings();
		addTextArea();
	 
	} 
	private void addTextArea() {
		DefaultMutableTreeNode top =  new DefaultMutableTreeNode("root");
		
		analyseService = new AnalyseServiceImpl(); 
    	analyseService.analyseApplication();
     	AnalysedModuleDTO[] rootModules = analyseService.getRootModules();
     		for(AnalysedModuleDTO rootNode: rootModules){
    			String UniqueNameValue = rootNode.uniqueName;
	    		DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(UniqueNameValue);
	            top.add(packageNode);    
     		}
     		
    	analyzedCodeTree = new JTree(top);
    	analyzedCodeTree.setPreferredSize(new Dimension(350, 300)); 
     	
		analyzedCodeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		analyzedCodeTree.addTreeSelectionListener(this);
		
		jScrollPaneTree = new JScrollPane(analyzedCodeTree);
		jScrollPaneTree.setPreferredSize(new Dimension(550, 400));
		jScrollPaneTree.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		  
	 	
		this.add(jScrollPaneTree); 
	} 
	private void setFrameSettings(){ 
		Dimension frameSize = new Dimension(800,600);
		this.setTitle("Service - Get RootModules");
		this.setPreferredSize(frameSize);
 	}
	@Override
	public void valueChanged(TreeSelectionEvent eventTree) {
 		DefaultMutableTreeNode currentNode = (DefaultMutableTreeNode) analyzedCodeTree.getLastSelectedPathComponent();
    	String nodeData = currentNode.toString();
		
		if(nodeData.equalsIgnoreCase("root")){}
		else
		{ 
			AnalysedModuleDTO[] rootModulesChilds = analyseService.getChildModulesInModule(nodeData);
			  for(AnalysedModuleDTO rootNodeChild: rootModulesChilds){
			  	String UniqueNameValueChild = rootNodeChild.uniqueName;
				DefaultMutableTreeNode packageNodeChild = new DefaultMutableTreeNode(UniqueNameValueChild);
				currentNode.add(packageNodeChild); 
			  } 
		}		
	}
 }
