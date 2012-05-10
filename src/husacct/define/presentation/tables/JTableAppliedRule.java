package husacct.define.presentation.tables;

import javax.swing.table.TableColumn;

public class JTableAppliedRule extends AbstractJTable {

	private static final long serialVersionUID = 3535559394466714205L;

	public JTableAppliedRule() {
		super();
	}
	
	@Override
	protected void setDefaultTableSettings() {
		
	}
	
	@Override
	protected void setColumnHeaders() {
//		tablemodel.addColumn("Id");
		tablemodel.addColumn("Rule Type");
		tablemodel.addColumn("To layer");
		tablemodel.addColumn("Enabled");
		tablemodel.addColumn("# Exceptions");
	}
	
	@Override
	protected void setColumnWidths() {
		TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
			column = getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(75); // Rule Type
			} else if (i == 1) {
				column.setPreferredWidth(75); // To layer
			} else if (i == 2) {
				column.setPreferredWidth(25); // Enabled
			} else if (i == 3) {
				column.setPreferredWidth(50); // Exceptions
			}
//			} else if (i == 4) {
//				column.setPreferredWidth(50); // Exceptions
//			}
		}
	}
	public int getRuleTypeColumnIndex(){
		return 0;
	}
}
