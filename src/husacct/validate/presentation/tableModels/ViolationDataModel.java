package husacct.validate.presentation.tableModels;

import husacct.ServiceProvider;
import husacct.common.dto.ViolationDTO;
import husacct.common.locale.ILocaleService;
import husacct.validate.domain.validation.Violation;

import java.util.HashMap;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

public class ViolationDataModel extends AbstractTableModel {
	private static final long		serialVersionUID	= 7993526243751581611L;
	private Logger					logger				= Logger.getLogger(ViolationDataModel.class);
	
	private ILocaleService			localeService;
	private Violation[]				data;
	
	private HashMap<String, String>	columnNames;
	private String[]				columnKeys;
	
	public ViolationDataModel() {
		data = new Violation[0];
		localeService = ServiceProvider.getInstance().getLocaleService();
		columnKeys = new String[] { "Source", "Target", "RuleType", "DependencyKind", "Direct", "Linenumber" };
		columnNames = new HashMap<String, String>();
		for (String key : columnKeys){ 
			columnNames.put(key, localeService.getTranslatedString(key));
		}
	}
	
	public void setData(List<Violation> violations) {
		data = violations.toArray(new Violation[violations.size()]);
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
			Violation row = data[rowIndex];
			if (columnKey.equals(columnKeys[0])) 
				return row.getClassPathFrom();
			else if (columnKey.equals(columnKeys[1])) 
				return row.getClassPathTo();
			else if (columnKey.equals(columnKeys[2])) {
				return localeService.getTranslatedString(row.getRuletypeKey());
			} else if (columnKey.equals(columnKeys[3])) { 
				return localeService.getTranslatedString(row.getViolationTypeKey());
			} else if (columnKey.equals(columnKeys[4])) { 
				return localeService.getTranslatedString(row.isIndirect() ? localeService.getTranslatedString("Indirect") : localeService.getTranslatedString("Direct"));
			} else if (columnKey.equals(columnKeys[5])) 
				return row.getLinenumber();
			else
				return null;
	}
	
    //JTable uses this method to determine the default renderer/editor for each cell.  
    @Override
    public Class getColumnClass(int c) {
        if (c == 5) {
            return new Integer(5).getClass();
        } else {
        	String s = "";
            return s.getClass();
        }
    } 

    @Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
    }
}
