package husacct.graphics.task;

import husacct.common.dto.AbstractDTO;

public class DrawingTask implements Runnable {
	
	private DrawingController controller;
	private AbstractDTO[] toDrawModules;
	
	public DrawingTask(DrawingController theController, AbstractDTO[] modules){
		controller = theController;
		toDrawModules = modules;
	}

	@Override
	public void run() {
		try {
			System.out.println("start");
			controller.actuallyDraw(toDrawModules);
			Thread.sleep(10);
			controller.refreshFrameClean();
			System.out.println("stop");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
