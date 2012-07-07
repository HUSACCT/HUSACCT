package husacct.graphics.util.threads;

public interface ThreadListener extends Runnable {
	public void update(ObservableThread source, int progress);
	public void threadTerminated(ObservableThread source);
}
