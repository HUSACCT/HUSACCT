package husacct.define.presentation.tables;

import javax.swing.JTable;

public abstract class AbstractJTable extends JTable {

    private static final long serialVersionUID = 6421740452550505332L;
    protected JTableTableModel tablemodel = new JTableTableModel();

    public AbstractJTable() {
        super();

        setAutoCreateRowSorter(false);
        setFillsViewportHeight(true);

        setModel(tablemodel);
        this.setDefaultTableSettings();
        this.setColumnHeaders();
        this.setColumnWidths();
    }

    protected abstract void setDefaultTableSettings();

    protected abstract void setColumnHeaders();

    protected abstract void setColumnWidths();
}
