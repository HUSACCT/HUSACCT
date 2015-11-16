package husacct.graphics.util.threads;

import husacct.common.dto.AbstractDTO;
import husacct.graphics.task.DrawingController;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class DrawingMultiLevelThread implements Runnable {
	
	private DrawingController						controller;
	private HashMap<String, ArrayList<AbstractDTO>>	toDrawModules;
	private Logger logger = Logger.getLogger(DrawingMultiLevelThread.class);

	
	public DrawingMultiLevelThread(DrawingController theController,
			HashMap<String, ArrayList<AbstractDTO>> modules) {
		controller = theController;
		toDrawModules = modules;
	}
	
	@Override
	public void run() {
		try {
			controller.clearDrawing();
			controller.drawMultiLevel(toDrawModules);
			//Thread.sleep(10);
			Thread.sleep(1);
		} catch (InterruptedException e) {
			logger.error(" InterruptedException: ", e);
			//e.printStackTrace();
		}
	}
}