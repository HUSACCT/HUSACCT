package husacct.graphics.presentation.tables;

import husacct.ServiceProvider;
import husacct.common.dto.ViolationDTO;
import husacct.common.locale.ILocaleService;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

public class ViolationDataModel extends AbstractTableModel {
	private static final long		serialVersionUID	= 7140981906234538035L;
	private Logger					logger				= Logger.getLogger(ViolationDataModel.class);
	
	private ILocaleService			localeService;
	private ViolationDTO[]			data;
	
	private HashMap<String, String>	columnNames;
	private String[]				columnKeys;
	
	public ViolationDataModel(ViolationDTO[] dtos) {
		localeService = ServiceProvider.getInstance().getLocaleService();
		columnKeys = new String[] { "Source", "Target", "RuleType", "DependencyKind", "Linenumber" };
		columnNames = new HashMap<String, String>();
		for (String key : columnKeys){ 
			columnNames.put(key, localeService.getTranslatedString(key));
		}
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
			ViolationDTO row = data[rowIndex];
			if (columnKey.equals(columnKeys[0])) 
				return row.fromClasspath;
			else if (columnKey.equals(columnKeys[1])) 
				return row.toClasspath;
			else if (columnKey.equals(columnKeys[2])) {
				return localeService.getTranslatedString(row.ruleType.key);
			} else if (columnKey.equals(columnKeys[3])) { 
				return localeService.getTranslatedString(row.violationType.key);
			} else if (columnKey.equals(columnKeys[4])) 
				return row.linenumber;
			else
				return null;
	}
	
    //JTable uses this method to determine the default renderer/editor for each cell.  
    @Override
    public Class getColumnClass(int c) {
        if (c == 4) {
            return new Integer(5).getClass();
        } else {
        	String s = "";
            return s.getClass();
        }
    } 
}
