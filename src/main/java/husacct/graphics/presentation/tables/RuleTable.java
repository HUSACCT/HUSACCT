package husacct.graphics.presentation.tables;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import husacct.common.dto.RuleDTO;

public class RuleTable extends JTable {
	private static final long 	serialVersionUID = 3921383787714082150L;
	private RuleDataModel		data;
	
	public RuleTable(RuleDTO[] RuleDTOs) {
		data = new RuleDataModel(RuleDTOs);
		setModel(data);
		setColumnWidths();
		setAutoCreateRowSorter(true);
	}

    protected void setColumnWidths() {
	TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
		    column = getColumnModel().getColumn(i);
		    if (i == 0) {
			column.setPreferredWidth(330); // From
		    } else if (i == 1) {
			column.setPreferredWidth(330); // To
		    } else if (i == 2) {
			column.setPreferredWidth(150); // Rule Type
		    } else if (i == 3) {
			column.setPreferredWidth(70); // Exceptions
		    } else if (i == 4) {
			column.setPreferredWidth(50); // Is Exception
		    }
		}
	}
}