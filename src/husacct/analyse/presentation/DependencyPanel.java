package husacct.analyse.presentation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.LayoutStyle.ComponentPlacement;

class DependencyPanel extends JPanel implements TreeSelectionListener{  
	
	private static final long serialVersionUID = 1L;
	private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
	
	private GroupLayout theLayout;
	private JScrollPane fromModuleScrollPane, toModuleScrollPane, dependencyScrollPane;
	private JTree fromModuleTree, toModuleTree;
	private JTable dependencyTable;
	private AbstractTableModel tableModel;
	
	private List<AnalysedModuleDTO> fromSelected = new ArrayList<AnalysedModuleDTO>(); 
	private List<AnalysedModuleDTO> toSelected = new ArrayList<AnalysedModuleDTO>();

	private AnalyseUIController dataControl;
	
	public DependencyPanel(){
		dataControl = new AnalyseUIController();
		createLayout();
		
		dependencyTable = new JTable();
		tableModel = new DependencyTableModel(new ArrayList<DependencyDTO>(), dataControl);
		
		dependencyTable.setModel(tableModel);
		dependencyScrollPane.setViewportView(dependencyTable);
		
		toModuleScrollPane.setViewportView(toModuleTree);
		fromModuleScrollPane.setViewportView(fromModuleTree);
		
		initialiseTrees();
		
		dependencyTable.setBackground(UIManager.getColor("Panel.background"));
		setLayout(theLayout);
	}
	
	private void initialiseTrees(){
		AnalysedModuleDTO rootModule = new AnalysedModuleDTO("", "", "", "");
		DefaultMutableTreeNode rootTo = new DefaultMutableTreeNode(rootModule);
		DefaultMutableTreeNode rootFrom = new DefaultMutableTreeNode(rootModule);
		
		this.fromModuleTree = new JTree(rootTo);
		createTreeLayout(fromModuleTree);
		fromModuleTree.addTreeSelectionListener(this);
		
		this.toModuleTree = new JTree(rootFrom);
		createTreeLayout(toModuleTree);
		toModuleTree.addTreeSelectionListener(this);
		
		List<AnalysedModuleDTO> rootModules = dataControl.getRootModules();
		
		for(AnalysedModuleDTO module : rootModules){
			DefaultMutableTreeNode toNode = new DefaultMutableTreeNode(module);
			DefaultMutableTreeNode fromNode = new DefaultMutableTreeNode(module);
			rootTo.add(toNode);
			fillNode(toNode);
			rootFrom.add(fromNode);
			fillNode(fromNode);
		}
		
		fromModuleScrollPane.setBackground(UIManager.getColor("Panel.background"));
		fromModuleScrollPane.setViewportView(fromModuleTree);
		toModuleScrollPane.setBackground(UIManager.getColor("Panel.background"));
		toModuleScrollPane.setViewportView(toModuleTree);
	}
	
	private void createTreeLayout(JTree theTree){
		DefaultTreeCellRenderer moduleNodeRenderer = new SoftwareTreeCellRenderer(dataControl);
		moduleNodeRenderer.setBackground(UIManager.getColor("Panel.background"));
		moduleNodeRenderer.setBackgroundNonSelectionColor(UIManager.getColor("Panel.background"));
		moduleNodeRenderer.setBackgroundSelectionColor(UIManager.getColor("Table.sortIconColor"));
		moduleNodeRenderer.setTextNonSelectionColor(UIManager.getColor("Panel.background"));
		moduleNodeRenderer.setTextSelectionColor(UIManager.getColor("Table.sortIconColor"));
		theTree.setCellRenderer(moduleNodeRenderer);
		theTree.setBackground(PANELBACKGROUND);
	}
		
	private void fillNode(DefaultMutableTreeNode node){
		AnalysedModuleDTO module = (AnalysedModuleDTO)node.getUserObject();
		List<AnalysedModuleDTO> children = dataControl.getModulesInModules(module.uniqueName);
		if(!children.isEmpty()){
			for(AnalysedModuleDTO child: children){
				DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
				fillNode(childNode);
			 	node.add(childNode); 
			} 
		}
	}
	
	private void createLayout(){
		fromModuleScrollPane = new JScrollPane();
		fromModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("FromModuleTreeTitle")));
		
		toModuleScrollPane = new JScrollPane();
		toModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("ToModuleTreeTitle")));
		
		dependencyScrollPane = new JScrollPane();
		dependencyScrollPane.setBorder(new TitledBorder(dataControl.translate("DependencyTableTitle")));
		
		theLayout = new GroupLayout(this);
		theLayout.setHorizontalGroup(
			theLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(theLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(theLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(dependencyScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
						.addGroup(theLayout.createSequentialGroup()
							.addComponent(fromModuleScrollPane, GroupLayout.DEFAULT_SIZE, 204, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(toModuleScrollPane, GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)))
					.addContainerGap())
		);
		theLayout.setVerticalGroup(
			theLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(theLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(theLayout.createParallelGroup(Alignment.LEADING, false)
						.addComponent(toModuleScrollPane, 0, 0, Short.MAX_VALUE)
						.addComponent(fromModuleScrollPane, GroupLayout.DEFAULT_SIZE, 265, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(dependencyScrollPane, GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
					.addGap(2))
		);
		fromModuleScrollPane.setBackground(PANELBACKGROUND);
		toModuleScrollPane.setBackground(PANELBACKGROUND);
		dependencyScrollPane.setBackground(UIManager.getColor("Panel.background"));
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if(e.getSource() == fromModuleTree){
			DefaultMutableTreeNode selected = (DefaultMutableTreeNode)fromModuleTree.getLastSelectedPathComponent();
			AnalysedModuleDTO selectedModule = (AnalysedModuleDTO) selected.getUserObject();
			fromSelected.clear();
			fromSelected.add(selectedModule);
		}else if(e.getSource() == toModuleTree){
			DefaultMutableTreeNode selected = (DefaultMutableTreeNode)toModuleTree.getLastSelectedPathComponent();
			AnalysedModuleDTO selectedModule = (AnalysedModuleDTO) selected.getUserObject();
			toSelected.clear();
			toSelected.add(selectedModule);
		}
		updateTableModel();
	}
	
	private void updateTableModel(){
		List<DependencyDTO> allFoundDependencies = dataControl.listDependencies(fromSelected, toSelected);
		dependencyTable.setModel(new DependencyTableModel(allFoundDependencies, dataControl));
		dependencyTable.repaint();
	}
	
	public void reload(){
		tableModel = new DependencyTableModel(new ArrayList<DependencyDTO>(), dataControl);
		fromModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("FromModuleTreeTitle")));
		toModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("ToModuleTreeTitle")));
		dependencyScrollPane.setBorder(new TitledBorder(dataControl.translate("DependencyTableTitle")));
		toModuleScrollPane.repaint(); 
		fromModuleScrollPane.repaint();
		dependencyScrollPane.repaint();
		updateTableModel();
		this.repaint();
	}
 }
