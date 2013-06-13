package husacct.graphics.util.threads;

public interface ThreadListener extends Runnable {
	public void threadTerminated(ObservableThread source);

	public void update(ObservableThread source, int progress);
}
