package husacct.graphics.presentation.tables;

import husacct.common.dto.ViolationDTO;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class ViolationTable extends JTable {
	private static final long	serialVersionUID	= -1359180631818542012L;
	private ViolationDataModel	data;
	
	public ViolationTable(ViolationDTO[] violationDTOs) {
		data = new ViolationDataModel(violationDTOs);
		setModel(data);
		setColumnWidths();
		setAutoCreateRowSorter(true);
	}

    protected void setColumnWidths() {
	TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
		    column = getColumnModel().getColumn(i);
		    if (i == 0) {
			column.setPreferredWidth(315); // From
		    } else if (i == 1) {
			column.setPreferredWidth(315); // To
		    } else if (i == 2) {
			column.setPreferredWidth(150); // Rule Type
		    } else if (i == 3) {
			column.setPreferredWidth(70); // Dep. Type
		    } else if (i == 3) {
			column.setPreferredWidth(50); // Direct
		    }
		}
	}
}
