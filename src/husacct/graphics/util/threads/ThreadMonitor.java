package husacct.graphics.util.threads;

import husacct.graphics.task.DrawingController;
import husacct.graphics.util.ListUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThreadMonitor implements ThreadListener {
	private static final int SLEEP_TIME = 10;

	private DrawingController controller;
	private Thread thread;
	private boolean isRunning;
	private List<Thread> runningThreads;
	private List<Runnable> pooledThreads;

	public ThreadMonitor(DrawingController theController) {
		controller = theController;
		runningThreads = Collections.synchronizedList(new ArrayList<Thread>());
		pooledThreads = Collections.synchronizedList(new ArrayList<Runnable>());
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		isRunning = true;

		while (isRunning) {
			updateScheduledTasks();
			updateRunningTasks();
			suspend();
		}
	}

	private void updateScheduledTasks() {
		synchronized (pooledThreads) {
			if (!pooledThreads.isEmpty()) {
				Runnable target = ListUtils.pop(pooledThreads);
				ObservableThread t = new ObservableThread(target);

				if (controller.isDrawingVisible())
					controller.setDrawingViewNonVisible();

				t.addThreadListener(this);
				t.start();

				synchronized (runningThreads) {
					runningThreads.add(t);
				}
			}
		}
	}

	private void updateRunningTasks() {
		synchronized (runningThreads) {
			if (runningThreads.isEmpty() && !controller.isDrawingVisible())
				controller.setDrawingViewVisible();
		}
	}

	private void suspend() {
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException ie) {

		}
	}

	public synchronized void add(Runnable target) {
		synchronized (pooledThreads) {
			pooledThreads.add(target);
		}
	}

	@Override
	public void update(ObservableThread source, int progress) {
	}

	@Override
	public void threadTerminated(ObservableThread source) {
		synchronized (runningThreads) {
			runningThreads.remove(source);
		}
	}
}
