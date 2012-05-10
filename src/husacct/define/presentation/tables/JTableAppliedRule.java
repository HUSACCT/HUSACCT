package husacct.define.presentation.tables;

import husacct.define.abstraction.language.DefineTranslator;

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
	public Long getValueAt(int row, int column) {
		Long value;
		try {
			value = (Long) super.getValueAt(row, column);
		} catch(Exception e) {
			value = -1L;
		}
		return value;
	}
	
	@Override
	protected void setColumnHeaders() {
		tablemodel.addColumn(DefineTranslator.translate("RuleType"));
		tablemodel.addColumn(DefineTranslator.translate("ToModule"));
		tablemodel.addColumn(DefineTranslator.translate("Enabled"));
		tablemodel.addColumn("# " + DefineTranslator.translate("Exceptions"));
	}
	
	public void changeColumnHeaders() {
		this.getTableHeader().getColumnModel().getColumn(0).setHeaderValue(DefineTranslator.translate("RuleType"));
		this.getTableHeader().getColumnModel().getColumn(1).setHeaderValue(DefineTranslator.translate("ToModule"));
		this.getTableHeader().getColumnModel().getColumn(2).setHeaderValue(DefineTranslator.translate("Enabled"));
		this.getTableHeader().getColumnModel().getColumn(3).setHeaderValue(DefineTranslator.translate("Exceptions"));
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
		}
	}
}
