package husacct.define.presentation.tables;

import javax.swing.table.DefaultTableModel;

public class JTableTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 3535559394466714205L;
    private boolean editable = false;

    public JTableTableModel() {
	super();
    }

    @Override
    public boolean isCellEditable(int row, int column) {
	return isEditable();
    }

    public boolean isEditable() {
	return editable;
    }

    public void setEditable(boolean editable) {
	this.editable = editable;
    }
}
