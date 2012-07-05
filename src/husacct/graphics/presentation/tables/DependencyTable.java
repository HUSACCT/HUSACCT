package husacct.graphics.presentation.tables;

import husacct.common.dto.DependencyDTO;

import javax.swing.JTable;

public class DependencyTable extends JTable {
	private static final long serialVersionUID = -2746864291996733501L;
	private DependencyDataModel data;

	public DependencyTable(DependencyDTO[] dependencyDTOs) {
		data = new DependencyDataModel(dependencyDTOs);
		setModel(data);
		setAutoCreateRowSorter(true);
	}
}
