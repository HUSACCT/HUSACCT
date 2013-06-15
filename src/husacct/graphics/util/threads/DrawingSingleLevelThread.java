package husacct.graphics.util.threads;

import husacct.common.dto.AbstractDTO;
import husacct.common.dto.ExternalSystemDTO;
import husacct.common.dto.ProjectDTO;
import husacct.graphics.task.DrawingController;

public class DrawingSingleLevelThread implements Runnable {

	private DrawingController controller;
	private AbstractDTO[] toDrawModules;
	private ExternalSystemDTO[] toDrawExtSystems;

	public DrawingSingleLevelThread(DrawingController theController,
			AbstractDTO[] modules) {
		controller = theController;
		toDrawModules = modules;
	}
	
	public DrawingSingleLevelThread(DrawingController theController,
			AbstractDTO[] modules, ExternalSystemDTO[] extSystems) {
		controller = theController;
		toDrawModules = modules;
		toDrawExtSystems = extSystems;
	}

	@Override
	public void run() {
		try {
			controller.clearDrawing();
			if(toDrawExtSystems == null){
				controller.drawSingleLevel(toDrawModules);
			}else{
				controller.drawSingleLevel(toDrawModules, toDrawExtSystems);
			}
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
