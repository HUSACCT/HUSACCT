package husacct.graphics.presentation.tables;

import husacct.common.dto.ViolationDTO;

import javax.swing.JTable;

public class ViolationTable extends JTable {

    private static final long serialVersionUID = -1359180631818542012L;
    private ViolationDataModel data;

    public ViolationTable(ViolationDTO[] violationDTOs) {
        data = new ViolationDataModel(violationDTOs);
        setModel(data);
        setAutoCreateRowSorter(true);
    }
}
