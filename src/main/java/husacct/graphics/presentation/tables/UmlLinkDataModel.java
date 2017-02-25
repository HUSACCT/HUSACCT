package husacct.graphics.presentation.tables;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import husacct.ServiceProvider;
import husacct.common.dto.UmlLinkDTO;
import husacct.common.locale.ILocaleService;

public class UmlLinkDataModel extends AbstractTableModel {
	private static final long		serialVersionUID	= -298507387139026205L;
	
	private ILocaleService			localeService;
	private UmlLinkDTO[]			data;
	
	private HashMap<String, String>	columnNames;
	private String[]				columnKeys;
	
	public UmlLinkDataModel(UmlLinkDTO[] dtos) {
		localeService = ServiceProvider.getInstance().getLocaleService();
		columnKeys = new String[] { "From", "To", "Type", "Attribute" };
		columnNames = new HashMap<>();
		columnNames.put(columnKeys[0], localeService.getTranslatedString(columnKeys[0]));
		columnNames.put(columnKeys[1], localeService.getTranslatedString(columnKeys[1]));
		columnNames.put(columnKeys[2], localeService.getTranslatedString(columnKeys[2]));
		columnNames.put(columnKeys[3], localeService.getTranslatedString(columnKeys[3]));
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
		UmlLinkDTO row = data[rowIndex];
		if (columnKey.equals(columnKeys[0])) return row.from;
		else if (columnKey.equals(columnKeys[1])) return row.to;
		else if (columnKey.equals(columnKeys[2])) return row.type;
		else if (columnKey.equals(columnKeys[3])) return row.attributeFrom;
		else return null;
    }
	
    //JTable uses this method to determine the default renderer/editor for each cell.  
    @Override
    public Class getColumnClass(int c) {
        if (c == 5) {
            return new Integer(4).getClass();
        } else {
        	String s = "";
            return s.getClass();
        }
    } 
}