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
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class DependencyPanel extends JPanel implements TreeSelectionListener, ActionListener {

    private static final long serialVersionUID = 1L;
    private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
    private GroupLayout theLayout;
    private JScrollPane fromModuleScrollPane, toModuleScrollPane, dependencyScrollPane;
    private JTree fromModuleTree, toModuleTree;
    private JTable dependencyTable;
    private JCheckBox indirectFilterBox;
    private JCheckBox directFilterBox;
    private JPanel filterPanel;
    private AbstractTableModel tableModel;
    private List<AnalysedModuleDTO> fromSelected = new ArrayList<AnalysedModuleDTO>();
    private List<AnalysedModuleDTO> toSelected = new ArrayList<AnalysedModuleDTO>();
    private AnalyseUIController dataControl;

    public DependencyPanel() {
        dataControl = new AnalyseUIController();
        this.indirectFilterBox = new JCheckBox(dataControl.translate("ShowIndirectDependencies"));
        this.indirectFilterBox.addActionListener(this);
        this.directFilterBox = new JCheckBox(dataControl.translate("ShowDirectDependencies"));
        this.directFilterBox.addActionListener(this);
        createLayout();

        dependencyTable = new JTable();
        tableModel = new DependencyTableModel(new ArrayList<DependencyDTO>(), dataControl);

        dependencyTable.setModel(tableModel);
        dependencyScrollPane.setViewportView(dependencyTable);
        dependencyTable.setBackground(PANELBACKGROUND);
        dependencyTable.setAutoCreateRowSorter(true);
        initialiseTrees();

        setLayout(theLayout);
    }

    private void initialiseTrees() {
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
        for (AnalysedModuleDTO module : rootModules) {
            DefaultMutableTreeNode toNode = new DefaultMutableTreeNode(module);
            DefaultMutableTreeNode fromNode = new DefaultMutableTreeNode(module);
            rootTo.add(toNode);
            fillNode(toNode);
            rootFrom.add(fromNode);
            fillNode(fromNode);
        }
        this.expandLeaf(toModuleTree, 1);
        this.expandLeaf(fromModuleTree, 1);

        fromModuleScrollPane.setBackground(PANELBACKGROUND);
        fromModuleScrollPane.setViewportView(fromModuleTree);
        toModuleScrollPane.setBackground(PANELBACKGROUND);
        toModuleScrollPane.setViewportView(toModuleTree);
    }

    private void createTreeLayout(JTree theTree) {
        DefaultTreeCellRenderer moduleNodeRenderer = new SoftwareTreeCellRenderer(dataControl);
        moduleNodeRenderer.setBackground(PANELBACKGROUND);
        moduleNodeRenderer.setBackgroundNonSelectionColor(PANELBACKGROUND);
        moduleNodeRenderer.setBackgroundSelectionColor(UIManager.getColor("Table.sortIconColor"));
        moduleNodeRenderer.setTextNonSelectionColor(PANELBACKGROUND);
        moduleNodeRenderer.setTextSelectionColor(UIManager.getColor("Table.sortIconColor"));
        theTree.setCellRenderer(moduleNodeRenderer);
        theTree.setBackground(PANELBACKGROUND);
    }

    private void fillNode(DefaultMutableTreeNode node) {
        AnalysedModuleDTO module = (AnalysedModuleDTO) node.getUserObject();
        List<AnalysedModuleDTO> children = dataControl.getModulesInModules(module.uniqueName);
        if (!children.isEmpty()) {
            for (AnalysedModuleDTO child : children) {
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

    private void createLayout() {
        fromModuleScrollPane = new JScrollPane();
        fromModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("FromModuleTreeTitle")));

        toModuleScrollPane = new JScrollPane();
        toModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("ToModuleTreeTitle")));

        dependencyScrollPane = new JScrollPane();
        dependencyScrollPane.setBorder(new TitledBorder(dataControl.translate("DependencyTableTitle")));

        this.filterPanel = new JPanel();
        FlowLayout flowLayout = (FlowLayout) filterPanel.getLayout();
        flowLayout.setAlignment(FlowLayout.LEFT);
        filterPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        filterPanel.setBorder(new TitledBorder(dataControl.translate("AnalyseDependencyFilter")));

        theLayout = new GroupLayout(this);
        theLayout.setHorizontalGroup(
                theLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(theLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(theLayout.createParallelGroup(Alignment.TRAILING)
                .addComponent(dependencyScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .addComponent(filterPanel, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
                .addGroup(theLayout.createSequentialGroup()
                .addComponent(fromModuleScrollPane, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
                .addGap(18)
                .addComponent(toModuleScrollPane, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)))
                .addContainerGap()));
        theLayout.setVerticalGroup(
                theLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(theLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(theLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(fromModuleScrollPane, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addComponent(toModuleScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(filterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(dependencyScrollPane, GroupLayout.DEFAULT_SIZE, 187, Short.MAX_VALUE)
                .addContainerGap()));

        directFilterBox.setSelected(true);
        directFilterBox.setHorizontalAlignment(SwingConstants.LEFT);
        filterPanel.add(directFilterBox);
        indirectFilterBox.setSelected(true);
        indirectFilterBox.setHorizontalAlignment(SwingConstants.LEFT);
        filterPanel.add(indirectFilterBox);
        fromModuleScrollPane.setBackground(PANELBACKGROUND);
        toModuleScrollPane.setBackground(PANELBACKGROUND);
        dependencyScrollPane.setBackground(PANELBACKGROUND);
    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (e.getSource() == fromModuleTree) {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) fromModuleTree.getLastSelectedPathComponent();
            if (selected != null) {
                AnalysedModuleDTO selectedModule = (AnalysedModuleDTO) selected.getUserObject();
                fromSelected.clear();
                fromSelected.add(selectedModule);
            }
        } else if (e.getSource() == toModuleTree) {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) toModuleTree.getLastSelectedPathComponent();
            if (selected != null) {
                AnalysedModuleDTO selectedModule = (AnalysedModuleDTO) selected.getUserObject();
                toSelected.clear();
                toSelected.add(selectedModule);
            }
        }
        updateTableModel();
    }

    private void updateTableModel() {
    	toggleDependencies(this.indirectFilterBox.isSelected(), this.directFilterBox.isSelected());
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == this.indirectFilterBox || event.getSource() == this.directFilterBox) {
            toggleDependencies(this.indirectFilterBox.isSelected(), this.directFilterBox.isSelected());
        }
    }
    
    private void toggleDependencies(boolean indirect, boolean direct){
    	List<DependencyDTO> filteredList = new ArrayList<DependencyDTO>();
        List<DependencyDTO> allDependencies = dataControl.listDependencies(fromSelected, toSelected);
        for (DependencyDTO dependency : allDependencies) {
        	if(indirect){
        		if(dependency.isIndirect)
        			filteredList.add(dependency);
        	}
        	if(direct){
        		if(!dependency.isIndirect)
        			filteredList.add(dependency);
        	}
        }
        dependencyTable.setModel(new DependencyTableModel(filteredList, dataControl));
        dependencyTable.repaint();
    }
    
    public void reload() {
        tableModel = new DependencyTableModel(new ArrayList<DependencyDTO>(), dataControl);
        fromModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("FromModuleTreeTitle")));
        toModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("ToModuleTreeTitle")));
        dependencyScrollPane.setBorder(new TitledBorder(dataControl.translate("DependencyTableTitle")));
        filterPanel.setBorder(new TitledBorder(dataControl.translate("AnalyseDependencyFilter")));
        this.indirectFilterBox.setText(dataControl.translate("ShowIndirectDependencies"));
        this.directFilterBox.setText(dataControl.translate("ShowDirectDependencies"));
        toModuleScrollPane.repaint();
        fromModuleScrollPane.repaint();
        dependencyScrollPane.repaint();
        filterPanel.repaint();
        updateTableModel();
        this.repaint();
    }
}
