package husacct.validate.presentation.tableModels;

import husacct.validate.domain.validation.Severity;

import java.util.List;
import java.util.Vector;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.table.AbstractTableModel;

public class ComboBoxTableModel extends AbstractTableModel {

	private static final long serialVersionUID = -7158951096196307267L;
	private Vector dataVector;
	private Vector columnIdentifiers;
	private DefaultCellEditor editor;
	private List<Severity> comboboxValues;
	private Object[] columnNames = {};

	public DefaultCellEditor getEditor() {
		return editor;
	}

	public void setComboboxValues(List<Severity> comboboxValues) {
		this.comboboxValues = comboboxValues;
	}

	public ComboBoxTableModel(Vector columnNames, int rowCount) {
		setDataVector(newVector(rowCount), columnNames);
	}

	public ComboBoxTableModel(Object[] columnNames, int rowCount, List<Severity> comboboxValues) {
		this(convertToVector(columnNames), rowCount);
		this.columnNames = columnNames;
		this.comboboxValues = comboboxValues;
		JComboBox comboBox = new JComboBox();// comboboxValues);
		for (Severity severity : comboboxValues) {
			comboBox.addItem(severity);
		}
		comboBox.setEditable(true);
		editor = new DefaultCellEditor(comboBox);
	}
	Class<?>[] types;
	Boolean[] canEdit;

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return types[columnIndex];
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return canEdit[columnIndex];
	}

	@Override
	public int getRowCount() {
		return dataVector.size();
	}

	@Override
	public int getColumnCount() {
		return columnNames.length;
	}

	@Override
	public Object getValueAt(int row, int column) {
		Vector rowVector = (Vector) dataVector.elementAt(row);
		return rowVector.elementAt(column);
	}

	@Override
	public String getColumnName(int column) {
		return (String) columnNames[column];
	}

	@Override
	public void setValueAt(Object value, int row, int column) {
		if (isValidValue(value)) {
			Vector rowVector = (Vector) dataVector.elementAt(row);
			rowVector.setElementAt(value, column);
			fireTableRowsUpdated(row, row);
		}
	}

	protected boolean isValidValue(Object value) {
		Object sValue = value;

        for (Severity comboboxValue : comboboxValues) {
            if (sValue.equals(comboboxValue)) {
                return true;
            }
        }

		return false;
	}

	protected static Vector convertToVector(Object[] anArray) {
		if (anArray == null) {
			return null;
		}
		Vector v = new Vector(anArray.length);
        for (Object anAnArray : anArray) {
            v.addElement(anAnArray);
        }
		return v;
	}

	private static Vector newVector(int size) {
		Vector v = new Vector(size);
		v.setSize(size);
		return v;
	}

	public final void setDataVector(Vector dataVector, Vector columnIdentifiers) {
		this.dataVector = nonNullVector(dataVector);
		this.columnIdentifiers = nonNullVector(columnIdentifiers);
		justifyRows(0, getRowCount());
		fireTableStructureChanged();
	}

	private static Vector nonNullVector(Vector v) {
		return (v != null) ? v : new Vector();
	}

	public void checkValuesAreValid() {
		for (int i = 0; i < getRowCount(); i++) {
			boolean b = false;
			for (Severity value : comboboxValues) {
				if (value.equals((Severity) getValueAt(i, 1))) {
					b = true;

				}
			}
			if (!b) {
				setValueAt(comboboxValues.get(0), i, 1);
			}
		}
	}

	private void justifyRows(int from, int to) {
		dataVector.setSize(getRowCount());

		for (int i = from; i < to; i++) {
			if (comboboxValues.contains(dataVector.elementAt(i))) {
				dataVector.setElementAt(new Vector(), i);
			}
			((Vector) dataVector.elementAt(i)).setSize(getColumnCount());
		}
	}

	public void addRow(Vector rowData) {
		insertRow(getRowCount(), rowData);
	}

	public void addRow(Object[] rowData) {
		addRow(convertToVector(rowData));
	}

	public void insertRow(int row, Vector rowData) {
		dataVector.insertElementAt(rowData, row);
		justifyRows(row, row + 1);
		fireTableRowsInserted(row, row);
	}

	public void insertRow(int row, Object[] rowData) {
		insertRow(row, convertToVector(rowData));
	}

	public void removeRow(int row) {
		dataVector.removeElementAt(row);
		fireTableRowsDeleted(row, row);
	}

	public void setTypes(Class<?>[] types) {
		this.types = types;
	}

	public void setCanEdit(Boolean[] canEdit) {
		this.canEdit = canEdit;
	}

	public void clear() {
		int rows = getRowCount();
		while (0 < rows) {
			removeRow(0);
			rows--;
		}
	}
}