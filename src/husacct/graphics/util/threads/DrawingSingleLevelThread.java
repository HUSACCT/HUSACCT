package husacct.graphics.util.threads;

import husacct.common.dto.AbstractDTO;
import husacct.graphics.task.DrawingController;

public class DrawingSingleLevelThread implements Runnable {
	
	private DrawingController controller;
	private AbstractDTO[] toDrawModules;
	
	public DrawingSingleLevelThread(DrawingController theController, AbstractDTO[] modules){
		controller = theController;
		toDrawModules = modules;
	}

	@Override
	public void run() {
		try {
			controller.drawSingleLevel(toDrawModules);
			Thread.sleep(10);
			controller.setDrawingViewVisible();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
