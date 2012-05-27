package husacct.validate.presentation;

import husacct.validate.task.TaskServiceImpl;

import javax.swing.JCheckBox;
import javax.swing.JTable;

public class LoadViolationHistoryPointsTask implements Runnable {
	
	private final JTable chooseViolationHistoryTable;
	private final BrowseViolations browseViolations;
	private final TaskServiceImpl taskServiceImpl;
	private final JCheckBox applyFilter;

	
	public LoadViolationHistoryPointsTask(JTable chooseViolationHistoryTable, BrowseViolations browseViolations, TaskServiceImpl taskServiceImpl, JCheckBox applyFilter) {
		this.chooseViolationHistoryTable = chooseViolationHistoryTable;
		this.browseViolations = browseViolations;
		this.taskServiceImpl = taskServiceImpl;
		this.applyFilter = applyFilter;
	}


	@Override
	public void run() {
		int row = chooseViolationHistoryTable.convertRowIndexToModel(chooseViolationHistoryTable.getSelectedRow());
		browseViolations.fillViolationsTable(taskServiceImpl.getViolationHistories().get(row).getViolations());
		browseViolations.loadAfterChange();
		applyFilter.setSelected(false);
	}
}