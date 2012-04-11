package husacct.define.presentation.helper;

import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;

public class MyComboBoxEditor extends DefaultCellEditor {

	private static final long serialVersionUID = -4831366454394947146L;

	public MyComboBoxEditor(String[] items) {
		super(new JComboBox(items));
	}
}