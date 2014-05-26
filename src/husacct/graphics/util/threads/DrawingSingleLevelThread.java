package husacct.graphics.util.threads;

import org.apache.log4j.Logger;

import husacct.common.dto.AbstractDTO;
import husacct.graphics.task.DrawingController;

public class DrawingSingleLevelThread implements Runnable {
	
	private DrawingController	controller;
	private AbstractDTO[]		toDrawModules;
	private Logger logger = Logger.getLogger(DrawingSingleLevelThread.class);
	
	public DrawingSingleLevelThread(DrawingController theController, AbstractDTO[] modules) {
		controller = theController;
		toDrawModules = modules;
	}
	
	@Override
	public void run() {
		try {
			controller.clearDrawing();
			controller.drawSingleLevel(toDrawModules);
			Thread.sleep(10);
		} catch (InterruptedException e) {
			logger.error(" InterruptedException: ", e);
			//e.printStackTrace();
		}
	}
	
}
