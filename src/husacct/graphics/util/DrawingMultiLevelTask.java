package husacct.graphics.util;

import java.util.ArrayList;
import java.util.HashMap;

import husacct.common.dto.AbstractDTO;
import husacct.graphics.task.DrawingController;

public class DrawingMultiLevelTask implements Runnable {
	
	private DrawingController controller;
	private HashMap<String, ArrayList<AbstractDTO>> toDrawModules;
	
	public DrawingMultiLevelTask(DrawingController theController, HashMap<String, ArrayList<AbstractDTO>> modules){
		controller = theController;
		toDrawModules = modules;
	}

	@Override
	public void run() {
		try {
			controller.drawMultiLevel(toDrawModules);
			Thread.sleep(10);
			controller.refreshFrameClean();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
