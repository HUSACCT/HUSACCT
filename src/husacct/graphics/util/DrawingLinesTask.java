package husacct.graphics.util;

import husacct.graphics.task.DrawingController;

public class DrawingLinesTask implements Runnable {
	
	private DrawingController controller;
	
	public DrawingLinesTask(DrawingController theController){
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
