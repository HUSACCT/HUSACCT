package husacct.analyse.presentation.usageview;

import husacct.ServiceProvider;
import husacct.analyse.presentation.AnalyseUIController;
import husacct.analyse.presentation.SoftwareTreeCellRenderer;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.SoftwareUnitDTO;
import husacct.analyse.task.AnalyseTaskControl;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.help.presentation.HelpableJScrollPane;
import husacct.common.help.presentation.HelpableJTable;
import husacct.common.help.presentation.HelpableJTree;
import husacct.validate.domain.validation.Severity;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

public class DependencyPanel extends HelpableJPanel implements TreeSelectionListener, ActionListener {

    private static final long serialVersionUID = 1L;
    private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
    private GroupLayout theLayout;
    private HelpableJScrollPane fromModuleScrollPane, toModuleScrollPane, dependencyScrollPane;
    private HelpableJTree fromModuleTree, toModuleTree;
    private HelpableJTable dependencyTable;
    private JCheckBox indirectFilterBox;
    private JCheckBox directFilterBox;
    private JPanel numberOfDependenciesPanel, filterPanel;
	private JLabel totalDependenciesLabel, totalDependenciesNumber;
	private AbstractTableModel tableModel;
    private List<SoftwareUnitDTO> fromSelected = new ArrayList<SoftwareUnitDTO>();
    private List<SoftwareUnitDTO> toSelected = new ArrayList<SoftwareUnitDTO>();
    protected List<DependencyDTO> filteredList;
    private AnalyseUIController dataControl;
    private AnalyseTaskControl analyseTaskControl;

    public DependencyPanel(AnalyseTaskControl atc) {
        dataControl = new AnalyseUIController(analyseTaskControl);
        createLayout();

        dependencyTable = new HelpableJTable();
        tableModel = new DependencyTableModel(new ArrayList<DependencyDTO>(), dataControl);

        dependencyTable.setModel(tableModel);
        dependencyScrollPane.setViewportView(dependencyTable);
        dependencyTable.setBackground(PANELBACKGROUND);
        dependencyTable.setAutoCreateRowSorter(true);
        
        dependencyTable.addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() >= 2){
					int row = dependencyTable.getSelectedRow();
					String cls = dependencyTable.getValueAt(row, 0).toString();
					int lineNumber = (int) dependencyTable.getValueAt(row, 3);
					dataControl.getControlService().displayErrorInFile(cls, lineNumber, new Severity("test", Color.LIGHT_GRAY));
				}
			}
			@Override
			public void mouseEntered(MouseEvent arg0) {}
			@Override
			public void mouseExited(MouseEvent arg0) {}
			@Override
			public void mousePressed(MouseEvent arg0) {}
			@Override
			public void mouseReleased(MouseEvent arg0) {}
    	});
        
        initialiseTrees();

        setLayout(theLayout);
    }

    private void initialiseTrees() {
        SoftwareUnitDTO rootModule = new SoftwareUnitDTO("", "", "", "");
        DefaultMutableTreeNode rootTo = new DefaultMutableTreeNode(rootModule);
        DefaultMutableTreeNode rootFrom = new DefaultMutableTreeNode(rootModule);

        this.fromModuleTree = new HelpableJTree(rootTo);
        createTreeLayout(fromModuleTree);
        fromModuleTree.addTreeSelectionListener(this);

        this.toModuleTree = new HelpableJTree(rootFrom);
        createTreeLayout(toModuleTree);
        toModuleTree.addTreeSelectionListener(this);

        List<SoftwareUnitDTO> rootModules = dataControl.getRootModules();
        for (SoftwareUnitDTO module : rootModules) {
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

    private void expandLeaf(JTree tree, int level) {
        for (int i = 0; i < level; i++) {
            tree.expandRow(i);
        }
    }

    private void createLayout() {
        fromModuleScrollPane = new HelpableJScrollPane();
        fromModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("FromModuleTreeTitle")));
        fromModuleScrollPane.setBackground(PANELBACKGROUND);

        toModuleScrollPane = new HelpableJScrollPane();
        toModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("ToModuleTreeTitle")));
        toModuleScrollPane.setBackground(PANELBACKGROUND);

        numberOfDependenciesPanel = new JPanel();
        FlowLayout flowLayout1 = (FlowLayout) numberOfDependenciesPanel.getLayout();
        flowLayout1.setAlignment(FlowLayout.LEFT);
        numberOfDependenciesPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        numberOfDependenciesPanel.setBorder(new TitledBorder(dataControl.translate("NumberOfDependencies")));
		totalDependenciesLabel = new JLabel();
		numberOfDependenciesPanel.add(totalDependenciesLabel);
		totalDependenciesNumber = new JLabel("0");
		numberOfDependenciesPanel.add(totalDependenciesNumber);
        
        filterPanel = new JPanel();
        FlowLayout flowLayout2 = (FlowLayout) filterPanel.getLayout();
        flowLayout2.setAlignment(FlowLayout.LEFT);
        filterPanel.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
        filterPanel.setBorder(new TitledBorder(dataControl.translate("AnalyseDependencyFilter")));
        directFilterBox = new JCheckBox(dataControl.translate("ShowDirectDependencies"));
        directFilterBox.addActionListener(this);
        directFilterBox.setSelected(true);
        directFilterBox.setHorizontalAlignment(SwingConstants.LEFT);
        filterPanel.add(directFilterBox);
        indirectFilterBox = new JCheckBox(dataControl.translate("ShowIndirectDependencies"));
        indirectFilterBox.addActionListener(this);
        indirectFilterBox.setSelected(true);
        indirectFilterBox.setHorizontalAlignment(SwingConstants.LEFT);
        filterPanel.add(indirectFilterBox);

        dependencyScrollPane = new HelpableJScrollPane();
        dependencyScrollPane.setBorder(new TitledBorder(dataControl.translate("DependencyTableTitle")));
        dependencyScrollPane.setBackground(PANELBACKGROUND);

        theLayout = new GroupLayout(this);
        theLayout.setHorizontalGroup(
                theLayout.createParallelGroup(Alignment.TRAILING)
                .addGroup(theLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(theLayout.createParallelGroup(Alignment.TRAILING)
	                .addComponent(dependencyScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
	                .addGroup(theLayout.createSequentialGroup()
	        		.addGroup(theLayout.createParallelGroup(Alignment.TRAILING)
		                .addComponent(fromModuleScrollPane, GroupLayout.DEFAULT_SIZE, 234, Short.MAX_VALUE)
		                .addComponent(numberOfDependenciesPanel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
	                .addGap(18)
	                .addGroup(theLayout.createParallelGroup(Alignment.TRAILING)
		        		.addComponent(toModuleScrollPane, GroupLayout.DEFAULT_SIZE, 245, Short.MAX_VALUE)
		                .addComponent(filterPanel, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))))
                .addContainerGap()));
        theLayout.setVerticalGroup(
                theLayout.createParallelGroup(Alignment.LEADING)
                .addGroup(theLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(theLayout.createParallelGroup(Alignment.LEADING)
	                .addComponent(fromModuleScrollPane, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
	                .addComponent(toModuleScrollPane, GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addGroup(theLayout.createParallelGroup(Alignment.LEADING)
    	            .addComponent(numberOfDependenciesPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
	                .addComponent(filterPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(dependencyScrollPane, GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                .addContainerGap()));

    }

    @Override
    public void valueChanged(TreeSelectionEvent e) {
        if (e.getSource() == fromModuleTree) {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) fromModuleTree.getLastSelectedPathComponent();
            if (selected != null) {
                SoftwareUnitDTO selectedModule = (SoftwareUnitDTO) selected.getUserObject();
                fromSelected.clear();
                fromSelected.add(selectedModule);
            }
        } else if (e.getSource() == toModuleTree) {
            DefaultMutableTreeNode selected = (DefaultMutableTreeNode) toModuleTree.getLastSelectedPathComponent();
            if (selected != null) {
                SoftwareUnitDTO selectedModule = (SoftwareUnitDTO) selected.getUserObject();
                toSelected.clear();
                toSelected.add(selectedModule);
            }
        }
        updateDependencyTable(indirectFilterBox.isSelected(), directFilterBox.isSelected());
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        if (event.getSource() == indirectFilterBox || event.getSource() == directFilterBox) {
            updateDependencyTable(indirectFilterBox.isSelected(), directFilterBox.isSelected());
        }
    }
    
    private void updateDependencyTable(boolean indirect, boolean direct){
    	filteredList = new ArrayList<DependencyDTO>();
        List<DependencyDTO> allDependencies = dataControl.listDependencies(fromSelected, toSelected);
        if (indirect && direct) {
        	filteredList.addAll(allDependencies);
        } else {
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
        }
        dependencyTable.setModel(new DependencyTableModel(filteredList, dataControl));
        setColumnWidths();
        dependencyTable.setAutoCreateRowSorter(true);
        dependencyTable.repaint();
		totalDependenciesNumber.setText(filteredList.size() + "");
        numberOfDependenciesPanel.repaint();
    }
    
    public void reload() {
    	updateDependencyTable(indirectFilterBox.isSelected(), directFilterBox.isSelected());
        fromModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("FromModuleTreeTitle")));
        toModuleScrollPane.setBorder(new TitledBorder(dataControl.translate("ToModuleTreeTitle")));
		totalDependenciesLabel.setText(ServiceProvider.getInstance().getLocaleService().getTranslatedString("NumberOfDependenciesBetweenSelectedModules") + ":");
		totalDependenciesNumber.setText(filteredList.size() + "");
        filterPanel.setBorder(new TitledBorder(dataControl.translate("AnalyseDependencyFilter")));
        indirectFilterBox.setText(dataControl.translate("ShowIndirectDependencies"));
        directFilterBox.setText(dataControl.translate("ShowDirectDependencies"));
        dependencyScrollPane.setBorder(new TitledBorder(dataControl.translate("DependencyTableTitle")));
        toModuleScrollPane.repaint();
        fromModuleScrollPane.repaint();
        numberOfDependenciesPanel.repaint();
        filterPanel.repaint();
        dependencyScrollPane.repaint();
        this.repaint();
    }
    
    protected void setColumnWidths() {
	TableColumn column = null;
		for (int i = 0; i < dependencyTable.getColumnCount(); i++) {
		    column = dependencyTable.getColumnModel().getColumn(i);
		    if (i == 0) {
			column.setPreferredWidth(350); // From
		    } else if (i == 1) {
			column.setPreferredWidth(350); // To
		    } else if (i == 2) {
			column.setPreferredWidth(70); // Type
		    } else if (i == 3) {
			column.setPreferredWidth(50); // Line
		    } else if (i == 4) {
			column.setPreferredWidth(50); // Direct
		    }
		}
	}
}