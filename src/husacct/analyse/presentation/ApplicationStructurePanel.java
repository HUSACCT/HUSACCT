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

class ApplicationStructurePanel extends JPanel implements TreeSelectionListener {

    private static final long serialVersionUID = 1L;
    private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
    private JTree analysedCodeTree;
    private JScrollPane jScrollPaneTree;
    private DefaultTreeCellRenderer renderer;
    private AnalyseUIController dataControl;

    public ApplicationStructurePanel() {
        dataControl = new AnalyseUIController();
        renderer = new SoftwareTreeCellRenderer(dataControl);
        createPanel();
    }

    private void createPanel() {

        AnalysedModuleDTO rootModule = new AnalysedModuleDTO("", "", "", "");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootModule);
        this.analysedCodeTree = new JTree(root);
        this.createTreeLayout(analysedCodeTree);

        List<AnalysedModuleDTO> rootModules = dataControl.getRootModules();
        for (AnalysedModuleDTO module : rootModules) {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(module);
            root.add(rootNode);
            fillNode(rootNode);
        }
        this.expandLeaf(analysedCodeTree, 1);

        analysedCodeTree.setBackground(UIManager.getColor("Panel.background"));
        analysedCodeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        analysedCodeTree.addTreeSelectionListener(this);
        jScrollPaneTree = new JScrollPane(analysedCodeTree);

        createLayout();

    }

    private void expandLeaf(JTree tree, int level) {
        for (int i = 0; i < level; i++) {
            tree.expandRow(i);
        }
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

    private void createTreeLayout(JTree theTree) {
        DefaultTreeCellRenderer moduleNodeRenderer = new SoftwareTreeCellRenderer(dataControl);
        moduleNodeRenderer.setBackground(UIManager.getColor("Panel.background"));
        moduleNodeRenderer.setBackgroundNonSelectionColor(UIManager.getColor("Panel.background"));
        moduleNodeRenderer.setBackgroundSelectionColor(UIManager.getColor("Table.sortIconColor"));
        moduleNodeRenderer.setTextNonSelectionColor(UIManager.getColor("Panel.background"));
        moduleNodeRenderer.setTextSelectionColor(UIManager.getColor("Table.sortIconColor"));
        theTree.setCellRenderer(moduleNodeRenderer);
        theTree.setBackground(PANELBACKGROUND);
    }

    private void createLayout() {
        jScrollPaneTree.setBackground(UIManager.getColor("Panel.background"));
        jScrollPaneTree.setBorder(null);
        jScrollPaneTree.setBackground(getBackground());

        GroupLayout groupLayout = new GroupLayout(this);
        groupLayout.setHorizontalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPaneTree, GroupLayout.DEFAULT_SIZE, 438, Short.MAX_VALUE)
                .addContainerGap()));
        groupLayout.setVerticalGroup(
                groupLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(groupLayout.createSequentialGroup()
                .addGap(5)
                .addComponent(jScrollPaneTree, GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                .addContainerGap()));

        analysedCodeTree.setCellRenderer(renderer);
        setLayout(groupLayout);
    }

    public void valueChanged(TreeSelectionEvent eventTree) {
        //Implement extra functionality here if needed in the future
    }

    public void reload() {
        renderer = new SoftwareTreeCellRenderer(dataControl);
        analysedCodeTree.setCellRenderer(renderer);
        repaint();
    }
}
