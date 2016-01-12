package husacct.analyse.presentation.decompositionview;

import husacct.ServiceProvider;
import husacct.analyse.presentation.AnalyseUIController;
import husacct.analyse.presentation.SoftwareTreeCellRenderer;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.help.presentation.HelpableJScrollPane;
import husacct.common.help.presentation.HelpableJTree;
import husacct.control.presentation.util.DialogUtils;
import husacct.define.presentation.jdialog.AddModuleValuesJDialog;
import husacct.define.presentation.moduletree.ModuletreeContextMenu;
import husacct.define.task.components.AbstractDefineComponent;
import husacct.validate.domain.validation.Severity;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

public class ApplicationStructurePanel extends HelpableJPanel implements ActionListener, TreeSelectionListener {

    private static final long serialVersionUID = 1L;
    private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
    private HelpableJTree codeTree;
	private JPopupMenu popupMenu = new JPopupMenu();
	private JMenuItem viewCodeItem = new JMenuItem();
    private HelpableJScrollPane jScrollPaneTree;
    private StatisticsPanel statisticsPanel;
    private AnalyseUIController dataControl;

    public ApplicationStructurePanel() {
        dataControl = new AnalyseUIController();
        createLayout();
        createCodeTree();
		createPopupMenu();
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

    private void createCodeTree() {

        SoftwareUnitDTO rootModule = new SoftwareUnitDTO("", "", "", "");
        DefaultMutableTreeNode root = new DefaultMutableTreeNode(rootModule);
        codeTree = new HelpableJTree(root);
        createTreeLayout(codeTree);
        codeTree.addTreeSelectionListener(this);
        codeTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

        List<SoftwareUnitDTO> rootModules = dataControl.getRootModules();
        for (SoftwareUnitDTO module : rootModules) {
            DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode(module);
            root.add(rootNode);
            fillNode(rootNode);
        }
        this.expandLeaf(codeTree, 1);
        
        jScrollPaneTree.setViewportView(codeTree);

        codeTree.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				createPopup(event);
			}
			@Override
			public void mouseEntered(MouseEvent event) {
				createPopup(event);
			}
			@Override
			public void mousePressed(MouseEvent event) {
				createPopup(event);
			}
		});

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

    public void valueChanged(TreeSelectionEvent eventTree) {
    	    DefaultMutableTreeNode selected = (DefaultMutableTreeNode) codeTree.getLastSelectedPathComponent();
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
        createPopupMenu();
        this.invalidate();
        this.revalidate();
        this.repaint();
    }

    private void createPopup(MouseEvent event) {
		if (SwingUtilities.isRightMouseButton(event)) {
			int row = codeTree.getClosestRowForLocation(event.getX(), event.getY());
			codeTree.setSelectionRow(row);
    	    DefaultMutableTreeNode selected = (DefaultMutableTreeNode) codeTree.getLastSelectedPathComponent();
            if (selected != null) {
                SoftwareUnitDTO selectedModule = (SoftwareUnitDTO) selected.getUserObject();
                String type = selectedModule.type;
                if (type.toLowerCase().equals("class") || type.toLowerCase().equals("interface")) {
                	popupMenu.show(codeTree, event.getX(), event.getY());
                }
            }
		}
	}

	private void createPopupMenu() {
		if (viewCodeItem != null) {
			popupMenu.removeAll();
		}
		viewCodeItem = new JMenuItem(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ShowCode"));
		viewCodeItem.addActionListener(this);
		popupMenu.add(viewCodeItem);
	}

	@Override
	public void actionPerformed(ActionEvent action) {
		if (action.getSource() == viewCodeItem) {
			// Start the CodeViewer
    	    DefaultMutableTreeNode selected = (DefaultMutableTreeNode) codeTree.getLastSelectedPathComponent();
            if (selected != null) {
                SoftwareUnitDTO selectedModule = (SoftwareUnitDTO) selected.getUserObject();
                String type = selectedModule.type;
                if (type.toLowerCase().equals("class") || type.toLowerCase().equals("interface")) {
                	String uniqueName = selectedModule.uniqueName;
					dataControl.getControlService().displayErrorInFile(uniqueName, 0, null);
                }
            }
		} 
	}

}
