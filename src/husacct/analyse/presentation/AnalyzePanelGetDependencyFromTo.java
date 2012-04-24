package husacct.analyse.presentation;

import husacct.analyse.AnalyseServiceImpl;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

public class AnalyzePanelGetDependencyFromTo extends JInternalFrame implements TreeSelectionListener{  
	private static final long serialVersionUID = 1L;
	public IAnalyseService analyseService;
	public JTree analyzedCodeTree;
	public JTree analyzedCodeTree2;
	public JScrollPane jScrollPaneTable;
	public JTable detailsTable ;
	public DefaultTableModel model;
	public JDesktopPane desktop;
	public JInternalFrame internalFrame2;
	public JInternalFrame internalFrame3;
	
	
	public JLabel fromSelection;
	public JLabel toSelection;
	
	public JButton invokeService;

	public AnalyzePanelGetDependencyFromTo(){
		setFrameSettings();
		addComponents();
	 
	} 
	private void addComponents() {
		DefaultMutableTreeNode top =  new DefaultMutableTreeNode("Analysed Application Unique Names");
		 
		fillNode(top); 
		 
		analyzedCodeTree = new JTree(top);
		analyzedCodeTree.setPreferredSize(new Dimension(150, 200)); 
	 	analyzedCodeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	 	analyzedCodeTree.addTreeSelectionListener(this);
	 	
	 	analyzedCodeTree2 = new JTree(top);
		analyzedCodeTree2.setPreferredSize(new Dimension(150, 200)); 
	 	analyzedCodeTree2.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
	 	analyzedCodeTree2.addTreeSelectionListener(this);
	 	
	 	desktop = new JDesktopPane();
	 	desktop.setBackground(Color.WHITE);
	    JInternalFrame internalFrame = new JInternalFrame("Select Package/Class-> From", true, true, true, true); 
	    desktop.add(internalFrame);

	    internalFrame.setBounds(0, 0, 535, 400); 
	    internalFrame.add(analyzedCodeTree, BorderLayout.CENTER); 
	    internalFrame.setVisible(true);

	    model = new DefaultTableModel(); 
		model.addColumn("From Tree Value");
		model.addColumn("To Tree Value");
		model.addRow(new Object[]{"<--","Select Package/Class"});  
		detailsTable = new JTable(model);
	    
	    jScrollPaneTable = new JScrollPane(detailsTable);
		jScrollPaneTable.setPreferredSize(new Dimension(670, 400));
		jScrollPaneTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);		  
	 
	    
	    internalFrame2 = new JInternalFrame("Select Package/Class-> To", true, true, true, true);
	    desktop.add(internalFrame2);
	    internalFrame2.setBounds(535, 0, 535, 400); 
	    internalFrame2.add(analyzedCodeTree2, BorderLayout.CENTER); 
	    internalFrame2.setVisible(true);

	    
	    internalFrame3 = new JInternalFrame("Dependency View - Service Input (From, To)", true, true, true, true);
	    desktop.add(internalFrame3);
	    internalFrame3.setBounds(0, 400, 1070, 400); 
	    internalFrame3.add(jScrollPaneTable, BorderLayout.CENTER); 
	    internalFrame3.setVisible(true);

	    
		
	    this.add(desktop);
	} 
	private void setFrameSettings(){ 
		Dimension frameSize = new Dimension(300,400);
		this.setTitle("Service - Get Dependency's (From, To) ");
		this.setPreferredSize(frameSize);
 	}
	@Override
	public void valueChanged(TreeSelectionEvent eventTree) {
 		
		if(analyzedCodeTree.getLastSelectedPathComponent() != null && analyzedCodeTree2.getLastSelectedPathComponent() != null){
		
			DefaultMutableTreeNode currentNodeFrom = (DefaultMutableTreeNode) analyzedCodeTree.getLastSelectedPathComponent();
	 		DefaultMutableTreeNode currentNodeTo = (DefaultMutableTreeNode) analyzedCodeTree2.getLastSelectedPathComponent();
	 		
	 		final String nodeDataFrom = currentNodeFrom.toString(); 
	 		final String nodeDataTo = currentNodeTo.toString();  
	 		model.setRowCount(0);
 
	 		JPanel containerResult = new JPanel();
	 		containerResult.setVisible(true);
	 		
	 		
	 		fromSelection = new JLabel("");
	 		fromSelection.setText("["+nodeDataFrom + " => ");
	 		toSelection = new JLabel(""); 
	 		toSelection.setText(nodeDataTo + "]");
	 		invokeService = new JButton("getDependency(from, to)" ); 
	 		invokeService.addActionListener(new ActionListener(){

				@Override
				public void actionPerformed(ActionEvent arg0) {
					 
					model.setRowCount(0);
					DependencyDTO[] dependencysPerName = analyseService.getDependencies(nodeDataFrom, nodeDataTo);
		    		  
					if(dependencysPerName.length == 0){
						model.addRow(new Object[]{"No Dependency Found From", "No Dependency Found To"});  
		        		
					}
					else{
					for(DependencyDTO currDep: dependencysPerName){
					 
						
		        		model.addRow(new Object[]{"From", currDep.from});  
		        		model.addRow(new Object[]{"To", currDep.to}); 
		        		model.addRow(new Object[]{"LineNumber", currDep.lineNumber}); 
		        		model.addRow(new Object[]{"Type",  currDep.type});
		        		model.addRow(new Object[]{"",  ""}); 
		        	}
					}
					
		    		JTable detailsTableV2 = new JTable(model);
		    		 
		    		jScrollPaneTable.setViewportView(detailsTableV2); 
					//internalFrame3 = new JInternalFrame("Dependency View", true, true, true, true);
				    desktop.remove(internalFrame3);
				    internalFrame3 = new JInternalFrame("Dependency View", true, true, true, true);
		    		desktop.add(internalFrame3);
				    internalFrame3.setBounds(0, 400, 1070, 400);
				   // internalFrame2.add(detailsTable, BorderLayout.CENTER); 
				    internalFrame3.add(jScrollPaneTable, BorderLayout.CENTER); 
				    internalFrame3.setVisible(true);
				 
					
				}
	 			
	 		});
	 		
	 		containerResult.add(fromSelection);
	 		containerResult.add(toSelection);
	 		containerResult.add(invokeService);
	 		
	 		
	 		desktop.remove(internalFrame3);
	 	    internalFrame3 = new JInternalFrame("Dependency View", true, true, true, true);
		    desktop.add(internalFrame3);
		    internalFrame3.setBounds(0, 400, 1070, 400);
		    internalFrame3.add(containerResult, BorderLayout.CENTER); 
		    internalFrame3.setVisible(true);
		
		}
		
		else{
			 
			if (analyzedCodeTree.getLastSelectedPathComponent() != null){
				model.setRowCount(0);
				model.addRow(new Object[]{"From value saved", "! Select a To value !"});
		      	JTable detailsTableV2 = new JTable(model);
		  		jScrollPaneTable.setViewportView(detailsTableV2);
			 }
			if (analyzedCodeTree2.getLastSelectedPathComponent() != null){
				model.setRowCount(0);
				model.addRow(new Object[]{"! Select a From value !", "To tree value saved"}); 
				JTable detailsTableV2 = new JTable(model);
		  		jScrollPaneTable.setViewportView(detailsTableV2);
			 }
			
		}
		    	
	}
	
	
	
	public void fillNode(DefaultMutableTreeNode top) {
		analyseService = new AnalyseServiceImpl(); 
    	analyseService.analyseApplication();
     	AnalysedModuleDTO[] rootModules = analyseService.getRootModules();
     		for(AnalysedModuleDTO rootNode: rootModules){
    			String UniqueNameValue = rootNode.uniqueName;
	    		DefaultMutableTreeNode packageNode = new DefaultMutableTreeNode(UniqueNameValue);
	            top.add(packageNode);    
	            
	            AnalysedModuleDTO[] rootModulesChild1 = analyseService.getChildModulesInModule(UniqueNameValue);
	     		for(AnalysedModuleDTO rootNodeChild1: rootModulesChild1){
	    			String UniqueNameValueChild1 = rootNodeChild1.uniqueName;
		    		DefaultMutableTreeNode packageChild1Node = new DefaultMutableTreeNode(UniqueNameValueChild1);
		            top.add(packageChild1Node);
		            
		            AnalysedModuleDTO[] rootModulesChild2 = analyseService.getChildModulesInModule(UniqueNameValueChild1);
		     		for(AnalysedModuleDTO rootNodeChild2: rootModulesChild2){
		    			String UniqueNameValueChild2 = rootNodeChild2.uniqueName;
			    		DefaultMutableTreeNode packageChild2Node = new DefaultMutableTreeNode(UniqueNameValueChild2);
			            top.add(packageChild2Node);
 
			            AnalysedModuleDTO[] rootModulesChild3 = analyseService.getChildModulesInModule(UniqueNameValueChild2);
			     		for(AnalysedModuleDTO rootNodeChild3: rootModulesChild3){
			    			String UniqueNameValueChild3 = rootNodeChild3.uniqueName;
				    		DefaultMutableTreeNode packageChild3Node = new DefaultMutableTreeNode(UniqueNameValueChild3);
				            top.add(packageChild3Node);
	 
				            AnalysedModuleDTO[] rootModulesChild4 = analyseService.getChildModulesInModule(UniqueNameValueChild3);
				     		for(AnalysedModuleDTO rootNodeChild4: rootModulesChild4){
				    			String UniqueNameValueChild4 = rootNodeChild4.uniqueName;
					    		DefaultMutableTreeNode packageChild4Node = new DefaultMutableTreeNode(UniqueNameValueChild4);
					            top.add(packageChild4Node);
		 
				     		}
			     		}
			            
		     		}
	     		} 
          	}

	}
 }
