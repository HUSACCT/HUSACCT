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
import javax.swing.JCheckBox;
import java.awt.ComponentOrientation;
import javax.swing.SwingConstants;

class DependencyPanel extends JPanel implements TreeSelectionListener{  
	
	private static final long serialVersionUID = 1L;
	private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
	
	private GroupLayout theLayout;
	private JScrollPane fromModuleScrollPane, toModuleScrollPane, dependencyScrollPane;
	private JTree fromModuleTree, toModuleTree;
	private JTable dependencyTable;
	private JCheckBox indirectFilterBox;
	private JPanel filterPanel;
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
		dependencyTable.setBackground(UIManager.getColor("Panel.background"));
		
		initialiseTrees();
		
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
		this.expandLeaf(toModuleTree, 1);
		this.expandLeaf(fromModuleTree, 1);
		
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
	
	private void expandLeaf(JTree tree, int level) {
		for (int i = 0; i < level; i++) {
			tree.expandRow(i);
		}
	}
	
	private void createLayout(){
		fromModuleScrollPane = new JScrollPane();
		fromModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("FromModuleTreeTitle")));
		
		toModuleScrollPane = new JScrollPane();
		toModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("ToModuleTreeTitle")));
		
		dependencyScrollPane = new JScrollPane();
		dependencyScrollPane.setBorder(new TitledBorder(dataControl.translate("DependencyTableTitle")));
		
		this.filterPanel = new JPanel();
		filterPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		filterPanel.setBorder(new TitledBorder(dataControl.translate("AnalyseDependencyFilter")));
		
		theLayout = new GroupLayout(this);
		theLayout.setHorizontalGroup(
			theLayout.createParallelGroup(Alignment.TRAILING)
				.addGroup(theLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(theLayout.createParallelGroup(Alignment.TRAILING)
						.addComponent(filterPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
						.addComponent(dependencyScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
						.addGroup(theLayout.createSequentialGroup()
							.addComponent(fromModuleScrollPane, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(toModuleScrollPane, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)))
					.addContainerGap())
		);
		theLayout.setVerticalGroup(
			theLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(theLayout.createSequentialGroup()
					.addContainerGap()
					.addGroup(theLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(toModuleScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
						.addComponent(fromModuleScrollPane, GroupLayout.DEFAULT_SIZE, 285, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(filterPanel, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
					.addGap(2)
					.addComponent(dependencyScrollPane, GroupLayout.DEFAULT_SIZE, 229, Short.MAX_VALUE)
					.addGap(2))
		);
		
		this.indirectFilterBox = new JCheckBox(dataControl.translate("ShowIndirectDependencies"));
		indirectFilterBox.setSelected(true);
		indirectFilterBox.setHorizontalAlignment(SwingConstants.LEFT);
		filterPanel.add(indirectFilterBox);
		fromModuleScrollPane.setBackground(PANELBACKGROUND);
		toModuleScrollPane.setBackground(PANELBACKGROUND);
		dependencyScrollPane.setBackground(UIManager.getColor("Panel.background"));
	}
	
	@Override
	public void valueChanged(TreeSelectionEvent e) {
		if(e.getSource() == fromModuleTree){
			DefaultMutableTreeNode selected = (DefaultMutableTreeNode)fromModuleTree.getLastSelectedPathComponent();
			AnalysedModuleDTO selectedModule = (AnalysedModuleDTO) selected.getUserObject();
			if(selectedModule != null){
				fromSelected.clear();
				fromSelected.add(selectedModule);
			}
		}else if(e.getSource() == toModuleTree){
			DefaultMutableTreeNode selected = (DefaultMutableTreeNode)toModuleTree.getLastSelectedPathComponent();
			AnalysedModuleDTO selectedModule = (AnalysedModuleDTO) selected.getUserObject();
			if(selectedModule != null){
				toSelected.clear();
				toSelected.add(selectedModule);
			}
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
		filterPanel.setBorder(new TitledBorder(dataControl.translate("AnalyseDependencyFilter")));
		this.indirectFilterBox.setText(dataControl.translate("ShowIndirectDependencies"));
		toModuleScrollPane.repaint(); 
		fromModuleScrollPane.repaint();
		dependencyScrollPane.repaint();
		filterPanel.repaint();
		updateTableModel();
		this.repaint();
	}
 }
