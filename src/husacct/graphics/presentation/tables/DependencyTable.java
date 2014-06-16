package husacct.graphics.presentation.tables;

import husacct.common.dto.DependencyDTO;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class DependencyTable extends JTable {
	private static final long	serialVersionUID	= -2746864291996733501L;
	private DependencyDataModel	data;
	
	public DependencyTable(DependencyDTO[] dependencyDTOs) {
		data = new DependencyDataModel(dependencyDTOs);
		setModel(data);
		setColumnWidths();
		setAutoCreateRowSorter(true);
	}
	
    protected void setColumnWidths() {
	TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
		    column = getColumnModel().getColumn(i);
		    if (i == 0) {
			column.setPreferredWidth(375); // From
		    } else if (i == 1) {
			column.setPreferredWidth(375); // To
		    } else if (i == 2) {
			column.setPreferredWidth(70); // Type
		    } else if (i == 3) {
			column.setPreferredWidth(30); // Line
		    } else if (i == 3) {
			column.setPreferredWidth(50); // Direct
		    }
		}
	}
	
}
