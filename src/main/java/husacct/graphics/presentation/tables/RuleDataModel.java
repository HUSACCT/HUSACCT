package husacct.graphics.presentation.tables;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

import husacct.ServiceProvider;
import husacct.common.dto.RuleDTO;
import husacct.common.locale.ILocaleService;

public class RuleDataModel extends AbstractTableModel {
	private static final long 		serialVersionUID = 8906869180757577755L;

	private Logger					logger				= Logger.getLogger(ViolationDataModel.class);
	
	private ILocaleService			localeService;
	private RuleDTO[]				data;
	
	private HashMap<String, String>	columnNames;
	private String[]				columnKeys;
	
	public RuleDataModel(RuleDTO[] dtos) {
		localeService = ServiceProvider.getInstance().getLocaleService();
		columnKeys = new String[] { "From", "To", "Type", "Exceptions", "IsException"};
		columnNames = new HashMap<>();
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
			RuleDTO row = data[rowIndex];
			if (columnKey.equals(columnKeys[0])) 
				return row.moduleFrom.logicalPath;
			else if (columnKey.equals(columnKeys[1])) 
				return row.moduleTo.logicalPath;
			else if (columnKey.equals(columnKeys[2])) {
				return row.ruleTypeKey;
				// TODO translation of row headers
//				return localeService.getTranslatedString(row.ruleType.key);
			} else if (columnKey.equals(columnKeys[3])) { 
				return row.exceptionRules.length;
			} else if (columnKey.equals(columnKeys[4])) 
				return row.isException;
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