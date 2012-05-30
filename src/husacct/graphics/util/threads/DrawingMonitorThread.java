package husacct.graphics.util.threads;

import husacct.graphics.task.DrawingController;

public class DrawingMonitorThread extends Thread implements Runnable {
	
	private Thread thread;
	private DrawingController controller;
	
	public DrawingMonitorThread(DrawingController givenController, Thread toRunThread){
		thread = toRunThread;
		controller = givenController;
		thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler(){
			@Override
			public void uncaughtException(Thread runningThread, Throwable exception) {
				threadException(runningThread, exception);
			}
		});
	}

	@Override
	public void run() {
		try {
			controller.setDrawingViewNonVisible();
			thread.join();
			Thread.sleep(10);
			controller.setDrawingViewVisible();
			System.err.println("DrawingMonitorThread: And we're done here");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected void threadException(Thread runningThread, Throwable exception) {
//		controller.reset();
	}
}
