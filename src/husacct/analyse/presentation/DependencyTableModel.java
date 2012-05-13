package husacct.analyse.presentation;

import husacct.analyse.abstraction.language.AnalyseTranslater;
import husacct.common.dto.DependencyDTO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

class DependencyTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private String[] fields;
	private String titleFrom, titleTo, titleLine, titleType;
	
	private List<DependencyDTO> data = new ArrayList<DependencyDTO>();

	public DependencyTableModel(List<DependencyDTO> data){
		this.data = data;
		titleFrom = AnalyseTranslater.getValue("From");
		titleTo = AnalyseTranslater.getValue("To");
		titleLine = AnalyseTranslater.getValue("Linenumber");
		titleType = AnalyseTranslater.getValue("Type");
		fields = new String[]{titleFrom, titleTo, titleLine, titleType};
	}
	
	public void setModel(List<DependencyDTO> newData){
		this.data = newData;
	}
	
	@Override
	public int getColumnCount() {
		return fields.length;
	}

	@Override
	public int getRowCount() {
		return data.size();
	}

	@Override
	public Object getValueAt(int row, int field) {
		String column = getColumnName(field);
		if(column.equals(titleFrom)) return data.get(row).from;
		else if(column.equals(titleTo)) return data.get(row).to;
		else if(column.equals(titleLine)) return data.get(row).lineNumber;
		else if(column.equals(titleType)) return AnalyseTranslater.getValue(data.get(row).type);
		else return null;
	}
	
	public String getColumnName(int columnIndex) {
        return fields[columnIndex];
    }
	
}
