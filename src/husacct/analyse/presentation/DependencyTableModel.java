package husacct.analyse.presentation;

import husacct.common.dto.DependencyDTO;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;

class DependencyTableModel extends AbstractTableModel{

	private static final long serialVersionUID = 1L;
	private static final String[] fields = new String[]{"From", "To", "Linenumber", "Type"};
	
	private List<DependencyDTO> data = new ArrayList<DependencyDTO>();

	public DependencyTableModel(List<DependencyDTO> data){
		this.data = data;
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
		if(column.equals("From")) return data.get(row).from;
		else if(column.equals("To")) return data.get(row).to;
		else if(column.equals("Linenumber")) return data.get(row).lineNumber;
		else if(column.equals("Type")) return data.get(row).type;
		else return null;
	}
	
	public String getColumnName(int columnIndex) {
        return fields[columnIndex];
    }
	
}
