package husacct.validate.presentation.tableModels;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.validate.domain.validation.Violation;
import husacct.validate.task.TaskServiceImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

public class ViolationsPerRuleDataModel extends AbstractTableModel {
	private static final long		serialVersionUID	= 7993526243931581611L;
	private final TaskServiceImpl taskServiceImpl;
	private ILocaleService			localeService;
	private Object[][]				data;
	private Object[][]				originalData;
	private HashMap<String, String>	columnNames;
	private String[]				columnKeys;
	Set<String> violatedRules;
	
	public ViolationsPerRuleDataModel(TaskServiceImpl taskServiceImpl) {
		this.taskServiceImpl = taskServiceImpl;
		localeService = ServiceProvider.getInstance().getLocaleService();
		columnKeys = new String[] { "Id", "LogicalModuleFrom", "RuleType", "LogicalModuleTo", "NrOfViolations" };
		columnNames = new HashMap<String, String>();
		for (String key : columnKeys){ 
			columnNames.put(key, localeService.getTranslatedString(key));
		}
		setData();
	}
	
	public void setData() {
		violatedRules = taskServiceImpl.getViolatedRules();
		int nrOfRules;
		if (violatedRules != null) {
			nrOfRules = violatedRules.size();
		} else {
			nrOfRules = 0;
		}
		data = new Object[nrOfRules][5];
		originalData = new Object[nrOfRules][5];
		nrOfRules = 0;
		for (String rule : violatedRules) {
			String[] ruleString = rule.split("::");
			List<Violation> violationsPerRule = taskServiceImpl.getViolationsByRule(ruleString[0], ruleString[1], ruleString[2]);
			data [nrOfRules] [0] = nrOfRules + 1;
			data [nrOfRules] [1] = ruleString[0];
			data [nrOfRules] [2] = ruleString[2];
			if (ruleString[0].equals(ruleString[1]) && !ruleString[2].equals("IsOnlyAllowedToUse")){
				data [nrOfRules] [3] = "";
			} else {
				data [nrOfRules] [3] = ruleString[1];
			}
			data [nrOfRules] [4] = violationsPerRule.size();

			originalData [nrOfRules] [0] = nrOfRules + 1;
			originalData [nrOfRules] [1] = ruleString[0];
			originalData [nrOfRules] [2] = ruleString[2];
			originalData [nrOfRules] [3] = ruleString[1];
			originalData [nrOfRules] [4] = violationsPerRule.size();
			
			nrOfRules ++;
		}
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
			if ((columnKey.equals(columnKeys[0])) || (columnKey.equals(columnKeys[1])) || (columnKey.equals(columnKeys[3])) || (columnKey.equals(columnKeys[4]))) 
				return data[rowIndex][columnIndex];
			else if (columnKey.equals(columnKeys[2]))
				return localeService.getTranslatedString((String) data[rowIndex][columnIndex]);
			else
				return null;
	}

	public Object getValueAtNotTranslated(int rowIndex, int columnIndex) {
		return originalData[rowIndex][columnIndex];
}

	
    //JTable uses this method to determine the default renderer/editor for each cell.  
    @Override
    public Class getColumnClass(int c) {
        if ((c == 0) || (c == 4)) {
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
