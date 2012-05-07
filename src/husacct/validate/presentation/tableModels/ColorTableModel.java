package husacct.validate.presentation.tableModels;

import husacct.validate.abstraction.language.ResourceBundles;
import husacct.validate.presentation.ColorRenderer;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableColumn;

public class ColorTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 2492345975488386436L;
	
	@SuppressWarnings("rawtypes")
	private Vector dataVector = new Vector();
    private String columnNames[] = {ResourceBundles.getValue("SeverityName"), ResourceBundles.getValue("Color")};
    private Class<?>[] types = new Class[]{String.class, JButton.class};
    private boolean[] canEdit = new boolean[]{true, true};
    private List<Color> rowColours = new ArrayList<Color>();

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return types[columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return canEdit[columnIndex];
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public int getRowCount() {
        return dataVector.size();
    }
    
	@Override
	@SuppressWarnings("rawtypes")
    public Object getValueAt(int row, int column) {
        Vector rowVector = (Vector) dataVector.elementAt(row);
        return rowVector.elementAt(column);
    }
    
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
    public void setValueAt(Object value, int row, int column) {
        Vector rowVector = (Vector) dataVector.elementAt(row);
        rowVector.setElementAt(value, column);
        fireTableRowsUpdated(row, row);
    }

    public void setColorEditor(JTable table, int columnNumber) {
        TableColumn column = table.getColumnModel().getColumn(columnNumber);
        TableCellEditor editor = new ColorChooserEditor();
        column.setCellEditor(editor);
        column.setCellRenderer(new ColorRenderer(true));
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected static Vector convertToVector(Object[] anArray) {
        if (anArray == null) {
            return null;
        }
        Vector v = new Vector(anArray.length);
        for (int i = 0; i < anArray.length; i++) {
            v.addElement(anArray[i]);
        }
        return v;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	private void justifyRows(int from, int to) {
        dataVector.setSize(getRowCount());

        for (int i = from; i < to; i++) {
            if (dataVector.elementAt(i) == null) {
                dataVector.setElementAt(new Vector(), i);
            }
            ((Vector) dataVector.elementAt(i)).setSize(getColumnCount());
        }
    }

    @SuppressWarnings("rawtypes")
	public void addRow(Vector rowData) {
        insertRow(getRowCount(), rowData);
    }

    public void addRow(Object[] rowData) {
        addRow(convertToVector(rowData));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void insertRow(int row, Vector rowData) {
        dataVector.insertElementAt(rowData, row);
        justifyRows(row, row + 1);
        fireTableRowsInserted(row, row);
    }

    public void insertRow(int row, Object[] rowData) {
        insertRow(row, convertToVector(rowData));
    }

    private static int gcd(int i, int j) {
        return (j == 0) ? i : gcd(j, i % j);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static void rotate(Vector v, int a, int b, int shift) {
        int size = b - a;
        int r = size - shift;
        int g = gcd(size, r);
        for (int i = 0; i < g; i++) {
            int to = i;
            Object tmp = v.elementAt(a + to);
            for (int from = (to + r) % size; from != i; from = (to + r) % size) {
                v.setElementAt(v.elementAt(a + from), a + to);
                to = from;
            }
            v.setElementAt(tmp, a + to);
        }
    }

    public void moveRow(int start, int end, int to) {
        int shift = to - start;
        int first, last;
        if (shift < 0) {
            first = to;
            last = end;
        } else {
            first = start;
            last = to + end - start;
        }
        rotate(dataVector, first, last + 1, shift);

        fireTableRowsUpdated(first, last);
    }

    public void removeRow(int row) {
        dataVector.removeElementAt(row);
        fireTableRowsDeleted(row, row);
    }

    public void setRowColour(int row, Color c) {
        rowColours.add(c);
        fireTableRowsUpdated(row, row);
    }

    public Color getRowColour(int row) {
        return rowColours.get(row);
    }
}