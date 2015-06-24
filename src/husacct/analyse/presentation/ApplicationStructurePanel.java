package husacct.analyse.presentation;

import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.help.presentation.HelpableJScrollPane;
import husacct.common.help.presentation.HelpableJTree;

import java.awt.Color;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeSelectionModel;

class ApplicationStructurePanel extends HelpableJPanel implements TreeSelectionListener {

    private static final long serialVersionUID = 1L;
    private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
    private HelpableJTree analysedCodeTree;
    private HelpableJScrollPane jScrollPaneTree;
    private StatisticsPanel statisticsPanel;
    private AnalyseUIController dataControl;

    public ApplicationStructurePanel() {
        dataControl = new AnalyseUIController();
        createLayout();
        createanalysedCodeTree();
    }

    private void createanalysedCodeTree() {

        SoftwareUnitDTO rootModule = new SoftwareUnitDTO("", "", "", "");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootModule);
        analysedCodeTree = new HelpableJTree(root);
        createTreeLayout(analysedCodeTree);
        analysedCodeTree.addTreeSelectionListener(this);
        analysedCodeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        List<SoftwareUnitDTO> rootModules = dataControl.getRootModules();
        for (SoftwareUnitDTO module : rootModules) {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(module);
            root.add(rootNode);
            fillNode(rootNode);
        }
        this.expandLeaf(analysedCodeTree, 1);
        
        jScrollPaneTree.setViewportView(analysedCodeTree);

    }

    private void expandLeaf(JTree tree, int level) {
        for (int i = 0; i < level; i++) {
            tree.expandRow(i);
        }
    }

    private void fillNode(DefaultMutableTreeNode node) {
        SoftwareUnitDTO module = (SoftwareUnitDTO) node.getUserObject();
        List<SoftwareUnitDTO> children = dataControl.getModulesInModules(module.uniqueName);
        if (!children.isEmpty()) {
            for (SoftwareUnitDTO child : children) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(child);
                fillNode(childNode);
                node.add(childNode);
            }
        }
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

    private void createLayout() {
        jScrollPaneTree = new HelpableJScrollPane();
    	jScrollPaneTree.setBackground(PANELBACKGROUND);
        jScrollPaneTree.setBorder(new TitledBorder(dataControl.translate("ApplicationComposition")));

        statisticsPanel = new StatisticsPanel(dataControl);

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneTree, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addGap(18)
                .addComponent(statisticsPanel, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
                .addContainerGap()));
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
        		.addContainerGap()
                .addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
                .addComponent(jScrollPaneTree, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addComponent(statisticsPanel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap()));

        setLayout(groupLayout);
    }

    public void valueChanged(TreeSelectionEvent eventTree) {
    	    DefaultMutableTreeNode selected = (DefaultMutableTreeNode) analysedCodeTree.getLastSelectedPathComponent();
            if (selected != null) {
                SoftwareUnitDTO selectedModule = (SoftwareUnitDTO) selected.getUserObject();
                statisticsPanel.reload(selectedModule);
                jScrollPaneTree.repaint();
                repaint();
            }
    }

    public void reload() {
        statisticsPanel.reload(null);
        jScrollPaneTree.repaint();
        this.invalidate();
        this.revalidate();
        this.repaint();
    }
}
