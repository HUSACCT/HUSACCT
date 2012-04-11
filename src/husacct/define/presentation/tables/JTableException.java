package husacct.define.presentation.tables;

import husacct.define.presentation.helper.MyComboBoxEditor;

import javax.swing.table.TableColumn;


public class JTableException extends AbstractJTable {

	private static final long serialVersionUID = 3535559394466714205L;

	public JTableException() {
		super();

		JTableTableModel tablemodel = (JTableTableModel) getModel();
		tablemodel.setEditable(true);

		tablemodel.addColumn("Software unit name");
		tablemodel.addColumn("Type");

		TableColumn column = null;
		for (int i = 0; i < getColumnCount(); i++) {
			column = getColumnModel().getColumn(i);
			if (i == 0) {
				column.setPreferredWidth(275); // Software unit name
			} else if (i == 1) {
				column.setPreferredWidth(25); // Type
			}
		}

		// old
		// String[] values = new String[] { SoftwareUnitDefinition.PACKAGE, SoftwareUnitDefinition.CLASS, SoftwareUnitDefinition.METHOD };
		String[] values = new String[] { "package", "class", "method" };
		// Set the combobox editor on the 1st visible column
		TableColumn col = getColumnModel().getColumn(1);
		col.setCellEditor(new MyComboBoxEditor(values));
	}
}
