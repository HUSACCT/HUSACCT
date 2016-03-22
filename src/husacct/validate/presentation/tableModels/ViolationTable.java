package husacct.validate.presentation.tableModels;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import husacct.ServiceProvider;
import husacct.validate.domain.validation.Severity;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

public class ViolationTable extends JTable {
	private static final long	serialVersionUID	= -1359180631818542012L;
	public ViolationTable() {
		setColumnWidths();
		setAutoCreateRowSorter(true);
        addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() >= 2){
					int row = getSelectedRow();
					String cls = getValueAt(row, 0).toString();
					int lineNumber = (int) getValueAt(row, 5);
					if (lineNumber == 0) {
						lineNumber = 1;
					}
					ServiceProvider.getInstance().getControlService().displayErrorInFile(cls, lineNumber, new Severity("test", Color.ORANGE));
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

	}

    public void setColumnWidths() {
	TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
		    column = getColumnModel().getColumn(i);
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

}
