package husacct.define.presentation.tables;

import javax.swing.table.TableColumn;

public class JTableSoftwareUnits extends AbstractJTable {

	private static final long serialVersionUID = 3535559394466714205L;

	public JTableSoftwareUnits() {
		super();
	}
	
	@Override
	protected void setDefaultTableSettings() {
		
	}
	
	@Override
	protected void setColumnHeaders() {
		tablemodel.addColumn("Software unit name");
		tablemodel.addColumn("Type");
	}
	
	@Override
	protected void setColumnWidths() {
		TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
			column = getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(275); // Software unit name
			} else if (i == 2) {
				column.setPreferredWidth(25); // Type
			}
		}
	}
}
