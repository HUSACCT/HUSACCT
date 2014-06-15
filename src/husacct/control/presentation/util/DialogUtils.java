package husacct.control.presentation.util;

import husacct.ServiceProvider;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;

import javax.swing.JDialog;

public class DialogUtils {
	public static void alignCenter(JDialog dialog){
		ControlServiceImpl controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		MainController mainController = controlService.getMainController();
		Dimension screenSize = mainController.mainGUI.getSize();
		Point topLeftCorner = mainController.mainGUI.getLocationOnScreen();
		final int x = ((screenSize.width - dialog.getWidth()) / 2) + topLeftCorner.x;
		final int y = ((screenSize.height - dialog.getHeight()) / 2) + topLeftCorner.x;
		dialog.setLocation(x, y);
	}
}
