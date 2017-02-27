package husacct.graphics.presentation.tables;

import husacct.ServiceProvider;
import husacct.common.dto.DependencyDTO;
import husacct.common.locale.ILocaleService;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

public class DependencyDataModel extends AbstractTableModel {
	private static final long		serialVersionUID	= -298507387139026205L;
	
	private ILocaleService			localeService;
	private DependencyDTO[]			data;
	
	private HashMap<String, String>	columnNames;
	private String[]				columnKeys;
	
	public DependencyDataModel(DependencyDTO[] dtos) {
		localeService = ServiceProvider.getInstance().getLocaleService();
		columnKeys = new String[] { "From", "To", "Type", "Linenumber", "Direct/Indirect" };
		columnNames = new HashMap<>();
		columnNames.put(columnKeys[0], localeService.getTranslatedString(columnKeys[0]));
		columnNames.put(columnKeys[1], localeService.getTranslatedString(columnKeys[1]));
		columnNames.put(columnKeys[2], localeService.getTranslatedString(columnKeys[2]));
		columnNames.put(columnKeys[3], localeService.getTranslatedString(columnKeys[3]));
		columnNames.put(columnKeys[4], localeService.getTranslatedString("Direct"));
		data = dtos;
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
	public int getRowCount() {
		return data.length;
	}
	
	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		String columnKey = columnKeys[columnIndex];
		DependencyDTO row = data[rowIndex];
		if (columnKey.equals(columnKeys[0])) return row.from;
		else if (columnKey.equals(columnKeys[1])) return row.to;
		else if (columnKey.equals(columnKeys[2])) return localeService.getTranslatedString(row.type);
		else if (columnKey.equals(columnKeys[3])) return row.lineNumber;
		else if (columnKey.equals(columnKeys[4])) return row.isIndirect ? localeService.getTranslatedString("Indirect") : localeService.getTranslatedString("Direct");
		else return null;
    }
	
    //JTable uses this method to determine the default renderer/editor for each cell.  
    @Override
    public Class getColumnClass(int c) {
        if (c == 3) {
            return new Integer(5).getClass();
        } else {
        	String s = "";
            return s.getClass();
        }
    } 
}
