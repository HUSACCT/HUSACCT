package husacct.validate.task.TableModels;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class ColorChooserEditor extends AbstractCellEditor implements
		TableCellEditor {

	private JButton delegate;// = new JButton();
	private Color savedColor;

	public ColorChooserEditor(JButton button) {
		ActionListener actionListener = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent actionEvent) {
				Color color = JColorChooser.showDialog(delegate, "Color Chooser",
													   savedColor);
				ColorChooserEditor.this.changeColor(color);
			}
		};
		delegate = button;
		delegate.addActionListener(actionListener);

	}

	@Override
	public Object getCellEditorValue() {
		return savedColor;
	}

	private void changeColor(Color color) {
		if (color != null) {
			savedColor = color;
			delegate.setBackground(color);
		}
	}

	public Color setBackgroundColor(){
		return savedColor;
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value,
			boolean isSelected,
			int row, int column) {
		changeColor((Color) value);
		return delegate;
	}
}