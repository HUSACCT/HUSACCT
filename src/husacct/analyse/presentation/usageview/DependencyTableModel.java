package husacct.analyse.presentation.usageview;

import husacct.analyse.presentation.AnalyseUIController;
import husacct.analyse.serviceinterface.dto.DependencyDTO;

import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

class DependencyTableModel extends AbstractTableModel {

    private static final long serialVersionUID = 1L;
    private String[] columnNames;
    private String titleFrom, titleVia, titleTo, titleLine, titleType, titleIndirect;
    private List<DependencyDTO> data = new ArrayList<DependencyDTO>();
    private AnalyseUIController uiController;

    public DependencyTableModel(List<DependencyDTO> data, AnalyseUIController uiController) {
        this.initiateTableModel(data, uiController);
    }

    private void initiateTableModel(List<DependencyDTO> data, AnalyseUIController uiController){//, boolean showIndirect) {
        this.data = data;
        this.uiController = uiController;
        titleFrom = uiController.translate("From");
        titleVia = "Via";
        titleTo = uiController.translate("To");
        titleLine = uiController.translate("Linenumber");
        titleType = uiController.translate("Type");
        titleIndirect = uiController.translate("Direct");
        columnNames = new String[]{titleFrom, titleTo, titleType, titleLine, titleIndirect};
    }

    public void setModel(List<DependencyDTO> newData) {
        this.data = newData;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public int getRowCount() {
        return data.size();
    }

    @Override
    public Object getValueAt(int row, int field) {
        String column = getColumnName(field);
        if (column.equals(titleFrom)) {
            return data.get(row).from;
        } else if (column.equals(titleVia)) {
        	return "";
        } else if (column.equals(titleTo)) {
            return data.get(row).to;
        } else if (column.equals(titleLine)) {
            return new Integer(data.get(row).lineNumber);
        } else if (column.equals(titleType)) {
            return uiController.translate(data.get(row).type);
        } else if (column.equals(titleIndirect)) {
            if (data.get(row).isIndirect) {
                return uiController.translate("Indirect");
            } else {
                return uiController.translate("Direct");
            }
        } else {
            return null;
        }
    }

    public String getColumnName(int columnIndex) {
        return columnNames[columnIndex];
    }

    //JTable uses this method to determine the default renderer/editor for each cell.  
    @SuppressWarnings("unchecked")
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
