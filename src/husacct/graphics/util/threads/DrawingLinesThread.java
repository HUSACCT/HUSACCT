package husacct.graphics.util.threads;

import husacct.graphics.task.DrawingController;

public class DrawingLinesThread implements Runnable {
	
	private DrawingController controller;
	
	public DrawingLinesThread(DrawingController theController){
		controller = theController;
	}

	@Override
	public void run() {
		try {
			controller.setDrawingViewNonVisible();
			controller.drawLines();
			controller.setDrawingViewVisible();
			Thread.sleep(10);
			controller.refreshFrameClean();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
