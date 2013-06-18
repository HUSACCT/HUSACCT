package husacct.graphics.util.threads;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ExternalSystemDTO;
import husacct.graphics.task.DrawingController;

import java.util.ArrayList;
import java.util.HashMap;

public class DrawingMultiLevelThread implements Runnable {

	private DrawingController controller;
	private HashMap<String, ArrayList<AbstractDTO>> toDrawModules;

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

			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}