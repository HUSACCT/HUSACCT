package husacct.graphics.util.threads;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ObservableThread extends Thread {
	private List<ThreadListener>	listeners;
	
	public ObservableThread(Runnable target) {
		super(target);
		
		listeners = Collections
				.synchronizedList(new ArrayList<ThreadListener>());
	}
	
	public synchronized void addThreadListener(ThreadListener listener) {
		synchronized (listeners) {
			if (!listeners.contains(listener)) listeners.add(listener);
		}
	}
	
	@Override
	public void run() {
		super.run();
		threadTerminated();
		listeners.clear();
	}
	
	protected synchronized void threadTerminated() {
		List<ThreadListener> copy = Collections.unmodifiableList(listeners);
		for (ThreadListener l : copy)
			l.threadTerminated(this);
	}
	
	protected synchronized void update(int progress) {
		List<ThreadListener> copy = Collections.unmodifiableList(listeners);
		for (ThreadListener l : copy)
			l.update(this, progress);
	}
}
