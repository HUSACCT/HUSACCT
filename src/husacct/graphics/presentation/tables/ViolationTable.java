package husacct.graphics.presentation.tables;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import husacct.ServiceProvider;
import husacct.common.dto.ViolationDTO;
import husacct.validate.domain.validation.Severity;

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
        addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() >= 2){
					int row = getSelectedRow();
					String cls = getValueAt(row, 0).toString();
					int lineNumber = (int) getValueAt(row, 4);
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
			column.setPreferredWidth(70); // Dep. Type
		    } else if (i == 4) {
			column.setPreferredWidth(50); // Line
		    }
		}
	}
}
