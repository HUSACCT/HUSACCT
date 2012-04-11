package husacct.define.presentation.tables;

import javax.swing.JTable;

public class AbstractJTable extends JTable {

	private static final long serialVersionUID = 6421740452550505332L;
	protected JTableTableModel tablemodel = new JTableTableModel();;

	public AbstractJTable() {
		super();

		setAutoCreateRowSorter(true);
		setFillsViewportHeight(true);

		setModel(tablemodel);
	}
}
