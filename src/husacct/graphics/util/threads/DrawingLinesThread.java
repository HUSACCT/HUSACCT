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
			controller.drawLinesBasedOnSetting();
			Thread.sleep(10);
			controller.setDrawingViewVisible();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
