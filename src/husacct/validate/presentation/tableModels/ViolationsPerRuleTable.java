package husacct.validate.presentation.tableModels;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import husacct.validate.presentation.browseViolations.ViolationPerRulePanel;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class ViolationsPerRuleTable extends JTable {
	private static final long	serialVersionUID	= -1359180781818542012L;
	private final ViolationPerRulePanel violationPerRulePanel;

	public ViolationsPerRuleTable(ViolationPerRulePanel vprPanel) {
		violationPerRulePanel = vprPanel;
		setColumnWidths();
		setAutoCreateRowSorter(true);
        addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int row = getSelectedRow();
		        int id = (int) getValueAt(row, 0);
				violationPerRulePanel.showViolationsSelectedRule(id);
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

	}

    public void setColumnWidths() {
	TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
		    column = getColumnModel().getColumn(i);
		    if (i == 0) {
			column.setPreferredWidth(25); // Id
		    } else if (i == 1) {
			column.setPreferredWidth(310); // From
		    } else if (i == 2) {
			column.setPreferredWidth(190); // Rule
		    } else if (i == 3) {
			column.setPreferredWidth(310); // To
		    } else if (i == 4) {
			column.setPreferredWidth(60); // Nr of Violations
		    }
		}
	}

}
