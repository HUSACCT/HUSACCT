package husacct.graphics.util.threads;

import husacct.graphics.task.DrawingController;

public class DrawingLinesThread implements Runnable {
	
	private DrawingController	controller;
	
	public DrawingLinesThread(DrawingController theController) {
		controller = theController;
	}
	
	@Override
	public void run() {
		controller.drawLinesBasedOnSetting();
	}
	
}
