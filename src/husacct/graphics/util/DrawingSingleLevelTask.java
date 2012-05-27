package husacct.graphics.util;

import husacct.common.dto.AbstractDTO;
import husacct.graphics.task.DrawingController;

public class DrawingSingleLevelTask implements Runnable {
	
	private DrawingController controller;
	private AbstractDTO[] toDrawModules;
	
	public DrawingSingleLevelTask(DrawingController theController, AbstractDTO[] modules){
		controller = theController;
		toDrawModules = modules;
	}

	@Override
	public void run() {
		try {
			controller.drawSingleLevel(toDrawModules);
			Thread.sleep(10);
			controller.refreshFrameClean();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
