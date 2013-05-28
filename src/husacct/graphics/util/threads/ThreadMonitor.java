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

	public synchronized boolean add(Runnable target) {
		synchronized (pooledThreads) {
			synchronized (runningThreads) {
				if (pooledThreads.isEmpty() && runningThreads.isEmpty()) {
					pooledThreads.add(target);
					return true;
				} else
					return false;
			}
		}
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

	private void suspend() {
		try {
			Thread.sleep(SLEEP_TIME);
		} catch (InterruptedException ie) {

		}
	}

	@Override
	public void threadTerminated(ObservableThread source) {
		synchronized (runningThreads) {
			runningThreads.remove(source);
		}
	}

	@Override
	public void update(ObservableThread source, int progress) {
	}

	private void updateRunningTasks() {
		synchronized (runningThreads) {
			if (runningThreads.isEmpty() && !controller.isDrawingVisible())
				controller.setDrawingViewVisible();
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
}
