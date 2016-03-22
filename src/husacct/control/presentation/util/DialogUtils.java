package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;

import javax.swing.JDialog;

public class DialogUtils {
	public static void alignCenter(JDialog dialog){
		ControlServiceImpl controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		MainController mainController = controlService.getMainController();
		dialog.setLocationRelativeTo(mainController.getMainGui());
	}
	
}
