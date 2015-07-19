package husacct.validate.presentation.browseViolations;

import husacct.ServiceProvider;
import husacct.analyse.presentation.AnalyseUIController;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.common.help.presentation.HelpableJScrollPane;
import husacct.common.help.presentation.HelpableJTable;
import husacct.validate.domain.validation.Severity;
import husacct.validate.domain.validation.Violation;
import husacct.validate.presentation.tableModels.ViolationDataModel;
import husacct.validate.presentation.tableModels.ViolationTable;
import husacct.validate.presentation.tableModels.ViolationsPerRuleTable;
import husacct.validate.presentation.tableModels.ViolationsPerRuleDataModel;
import husacct.validate.task.TaskServiceImpl;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.border.TitledBorder;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;


public class ViolationPerRulePanel extends HelpableJPanel {
    private static final long serialVersionUID = 1L;
    private static final Color PANELBACKGROUND = UIManager.getColor("Panel.background");
	private final TaskServiceImpl taskServiceImpl;
    private GroupLayout theLayout;
    private ViolationsPerRuleTable violationsPerRuleTable;
    private ViolationsPerRuleDataModel violationsPerRuleDataModel;
    private HelpableJScrollPane ruleScrollPane, violationsScrollPane;
    private ViolationTable violationTable;
    private ViolationDataModel tableModel;

    public ViolationPerRulePanel(TaskServiceImpl taskServiceImpl) {
		this.taskServiceImpl = taskServiceImpl;
        createLayout();
        violationsPerRuleTable = new ViolationsPerRuleTable(this);
        violationsPerRuleDataModel = new ViolationsPerRuleDataModel(taskServiceImpl);
        violationsPerRuleTable.setModel(violationsPerRuleDataModel);
        ruleScrollPane.setViewportView(violationsPerRuleTable);
        violationsPerRuleTable.setBackground(PANELBACKGROUND);
        violationsPerRuleTable.setAutoCreateRowSorter(true);

        violationTable = new ViolationTable();
        tableModel = new ViolationDataModel();
        violationTable.setModel(tableModel);
        violationsScrollPane.setViewportView(violationTable);
        violationTable.setBackground(PANELBACKGROUND);
        violationTable.setAutoCreateRowSorter(true);
        
        setLayout(theLayout);
    }

    private void createLayout() {
        ruleScrollPane = new HelpableJScrollPane();
        ruleScrollPane.setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ViolationsPerRuleTotalTitle")));
        violationsScrollPane = new HelpableJScrollPane();
        violationsScrollPane.setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Violations")));

        theLayout = new GroupLayout(this);
        theLayout.setHorizontalGroup(theLayout.createParallelGroup(Alignment.TRAILING)
            .addGroup(theLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(theLayout.createParallelGroup(Alignment.TRAILING)
	                .addComponent(ruleScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE)
	                .addComponent(violationsScrollPane, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 497, Short.MAX_VALUE))
                .addContainerGap()));
        theLayout.setVerticalGroup(theLayout.createParallelGroup(Alignment.LEADING)
            .addGroup(theLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ruleScrollPane, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addPreferredGap(ComponentPlacement.RELATED)
                .addComponent(violationsScrollPane, GroupLayout.DEFAULT_SIZE, 235, Short.MAX_VALUE)
                .addContainerGap()));

        ruleScrollPane.setBackground(PANELBACKGROUND);
        violationsScrollPane.setBackground(PANELBACKGROUND);
    }

    public void reload() {
        violationsPerRuleDataModel = new ViolationsPerRuleDataModel(taskServiceImpl);
    	violationsPerRuleTable.setModel(violationsPerRuleDataModel);
    	violationsPerRuleTable.setColumnWidths();
    	violationsPerRuleTable.setAutoCreateRowSorter(true);
        ruleScrollPane.setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("ViolationsPerRuleTotalTitle")));
        ruleScrollPane.repaint();
        this.repaint();
        if (violationsPerRuleTable.getRowCount() >= 1) {
        	violationsPerRuleTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        	violationsPerRuleTable.setRowSelectionInterval(0, 0);
        	int selectedRow = violationsPerRuleTable.getSelectedRow();
	        int id = (int) violationsPerRuleTable.getValueAt(selectedRow, 0);
        	showViolationsSelectedRule(id);
        } else {
        	clearViolationTable();
        }
    }
    
    protected void setColumnWidths() {
	TableColumn column = null;
		for (int i = 0; i < violationTable.getColumnCount(); i++) {
		    column = violationTable.getColumnModel().getColumn(i);
		    if (i == 0) {
			column.setPreferredWidth(290); // From
		    } else if (i == 1) {
			column.setPreferredWidth(290); // To
		    } else if (i == 2) {
			column.setPreferredWidth(150); // Rule
		    } else if (i == 3) {
			column.setPreferredWidth(70); // DependencyKind
		    } else if (i == 4) {
			column.setPreferredWidth(50); // Direct
		    } else if (i == 5) {
			column.setPreferredWidth(50); // LineNumber
		    }
		}
	}
    
    public void showViolationsSelectedRule(int id) {
        // Load violationTable
 		String moduleFrom = (String) violationsPerRuleDataModel.getValueAtNotTranslated(id -1, 1);
		String moduleTo = (String) violationsPerRuleDataModel.getValueAtNotTranslated(id -1, 3);
		String ruleTypeKey = (String) violationsPerRuleDataModel.getValueAtNotTranslated(id -1, 2);
		List<Violation> violationsPerRuleList = taskServiceImpl.getViolationsByRule(moduleFrom, moduleTo, ruleTypeKey);
		tableModel = new ViolationDataModel();
		tableModel.setData(violationsPerRuleList);
        violationTable.setModel(tableModel);
        violationTable.setAutoCreateRowSorter(true);
        violationTable.setColumnWidths();
        violationsScrollPane.setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Violations")));
        violationsScrollPane.repaint();
    }
    
    private void clearViolationTable() {
		tableModel = new ViolationDataModel();
        violationTable.setModel(tableModel);
        violationTable.setAutoCreateRowSorter(true);
        violationTable.setColumnWidths();
        violationsScrollPane.setBorder(new TitledBorder(ServiceProvider.getInstance().getLocaleService().getTranslatedString("Violations")));
        violationsScrollPane.repaint();
    }
}