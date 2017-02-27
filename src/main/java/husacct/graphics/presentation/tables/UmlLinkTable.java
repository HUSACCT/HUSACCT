package husacct.graphics.presentation.tables;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JTable;
import javax.swing.table.TableColumn;

import husacct.ServiceProvider;
import husacct.common.dto.UmlLinkDTO;
import husacct.validate.domain.validation.Severity;

public class UmlLinkTable  extends JTable {
	private static final long	serialVersionUID	= -2746864291996733501L;
	private UmlLinkDataModel	dataModel;
	
	public UmlLinkTable(UmlLinkDTO[] umlLinksDTOs) {
		dataModel = new UmlLinkDataModel(umlLinksDTOs);
		setModel(dataModel);
		setColumnWidths();
		setAutoCreateRowSorter(true);
        addMouseListener(new MouseListener(){
			@Override
			public void mouseClicked(MouseEvent arg0) {
				if(arg0.getClickCount() >= 2){
					int row = getSelectedRow();
					String cls = getValueAt(row, 0).toString();
					int lineNumber = (int) getValueAt(row, 3);
					ServiceProvider.getInstance().getControlService().displayErrorInFile(cls, lineNumber, new Severity("test", Color.LIGHT_GRAY));
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
			column.setPreferredWidth(375); // From
		    } else if (i == 1) {
			column.setPreferredWidth(375); // To
		    } else if (i == 2) {
			column.setPreferredWidth(70); // Type
		    } else if (i == 3) {
			column.setPreferredWidth(50); // Attribute
		    } 
		}
	}
	
}
