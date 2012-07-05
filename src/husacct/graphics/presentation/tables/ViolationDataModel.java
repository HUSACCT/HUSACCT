package husacct.graphics.presentation.tables;

import husacct.ServiceProvider;
import husacct.common.dto.ViolationDTO;
import husacct.common.locale.ILocaleService;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

public class ViolationDataModel extends AbstractTableModel {
	private static final long serialVersionUID = 7140981906234538035L;

	private ILocaleService localeService;
	private ViolationDTO[] data;

	private HashMap<String, String> columnNames;
	private String[] columnKeys;

	public ViolationDataModel(ViolationDTO[] dtos) {
		localeService = ServiceProvider.getInstance().getLocaleService();
		columnKeys = new String[] { "ErrorMessage", "RuleType", "ViolationType", "Severity", "LineNumber" };
		columnNames = new HashMap<String, String>();
		for (String key : columnKeys) {
			columnNames.put(key, localeService.getTranslatedString(key));
		}
		data = dtos;
	}

	@Override
	public int getRowCount() {
		return data.length;
	}

	@Override
	public int getColumnCount() {
		return columnNames.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return columnNames.get(columnKeys[columnIndex]);
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String value = null;
		String columnKey = columnKeys[columnIndex];
		ViolationDTO row = data[rowIndex];
		if (columnKey.equals(columnKeys[0])) {
			value = row.message;
		} else if (columnKey.equals(columnKeys[1])) {
			value = row.ruleType.getDescriptionKey();
		} else if (columnKey.equals(columnKeys[2])) {
			value = row.violationType.getDescriptionKey();
		} else if (columnKey.equals(columnKeys[3])) {
			value = "" + row.severityValue;
		} else if (columnKey.equals(columnKeys[4])) {
			value = "" + row.linenumber;
		}
		return value;
	}

}
