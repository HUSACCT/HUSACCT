package husacct.validate.presentation.threadTasks;

import husacct.control.task.AnalyseTask;
import husacct.validate.presentation.BrowseViolations;
import org.apache.log4j.Logger;

public class BrowseViolationsUpdateTask implements Runnable {

    private Logger logger = Logger.getLogger(AnalyseTask.class);
    private final BrowseViolations browseViolations;

    public BrowseViolationsUpdateTask(BrowseViolations browseViolations) {
        this.browseViolations = browseViolations;
    }

    @Override
    public void run() {
        // Thread.sleep added to support InterruptedException catch
        // InterruptedException is not yet implemented by analyse
        // Therefor this thread can never be interrupted.
        try {
            Thread.sleep(1);
            browseViolations.updateViolationsTable();
        } catch (InterruptedException exception) {
            logger.debug("Validate interupted");
        }
    }
}
