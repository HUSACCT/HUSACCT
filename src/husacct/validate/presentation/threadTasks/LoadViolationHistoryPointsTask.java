package husacct.validate.presentation.threadTasks;

import husacct.validate.domain.validation.ViolationHistory;
import husacct.validate.presentation.BrowseViolations;
import husacct.validate.task.TaskServiceImpl;

import javax.swing.JCheckBox;
import javax.swing.JTable;

import org.apache.log4j.Logger;

public class LoadViolationHistoryPointsTask implements Runnable {

	private final JTable chooseViolationHistoryTable;
	private final BrowseViolations browseViolations;
	private final TaskServiceImpl taskServiceImpl;
	private final JCheckBox applyFilter;

	private Logger logger = Logger.getLogger(LoadViolationHistoryPointsTask.class);

	public LoadViolationHistoryPointsTask(JTable chooseViolationHistoryTable, BrowseViolations browseViolations, TaskServiceImpl taskServiceImpl, JCheckBox applyFilter) {
		this.chooseViolationHistoryTable = chooseViolationHistoryTable;
		this.browseViolations = browseViolations;
		this.taskServiceImpl = taskServiceImpl;
		this.applyFilter = applyFilter;
	}

	@Override
	public void run() {
		try{
			Thread.sleep(1);

			int row = chooseViolationHistoryTable.convertRowIndexToModel(chooseViolationHistoryTable.getSelectedRow());
			ViolationHistory selectedViolationHistory = taskServiceImpl.getViolationHistories().get(row);
			browseViolations.setSelectedViolationHistory(selectedViolationHistory);
			browseViolations.fillViolationsTable(selectedViolationHistory.getViolations());
			applyFilter.setSelected(false);
		}catch (InterruptedException e) {
			logger.debug(e.getMessage());
		}	
	}
}