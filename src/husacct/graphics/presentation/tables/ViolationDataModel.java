package husacct.graphics.presentation.tables;

import husacct.ServiceProvider;
import husacct.common.dto.ViolationDTO;
import husacct.common.locale.ILocaleService;

import java.util.HashMap;

import javax.swing.table.AbstractTableModel;

import org.apache.log4j.Logger;

public class ViolationDataModel extends AbstractTableModel {

    private static final long serialVersionUID = 7140981906234538035L;
    private Logger logger = Logger.getLogger(ViolationDataModel.class);
    private ILocaleService localeService;
    private ViolationDTO[] data;
    private HashMap<String, String> columnNames;
    private String[] columnKeys;

    public ViolationDataModel(ViolationDTO[] dtos) {
        localeService = ServiceProvider.getInstance().getLocaleService();
        columnKeys = new String[]{"Source", "Rule", "DependencyKind", "Target", "Severity"};
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
        String value = "";
        try {
            String columnKey = columnKeys[columnIndex];
            ViolationDTO row = data[rowIndex];
            if (columnKey.equals(columnKeys[0])) {
                value = row.fromClasspath;
            } else if (columnKey.equals(columnKeys[1])) {
                value = localeService.getTranslatedString(row.ruleType.key);
            } else if (columnKey.equals(columnKeys[2])) {
                value = localeService.getTranslatedString(row.violationType.key) + ", ";
                value += row.indirect ? localeService.getTranslatedString("Indirect") : localeService.getTranslatedString("Direct");
            } else if (columnKey.equals(columnKeys[3])) {
                value = "" + row.toClasspath;
            } else if (columnKey.equals(columnKeys[4])) {
                value = "" + row.severityName;
            }
        } catch (Exception e) {
            logger.error("Could not fill column " + columnIndex + " at row " + rowIndex);
        }
        return value;
    }
}
