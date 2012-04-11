package husacct.define.presentation.utils;

import java.awt.Component;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class UiDialogs {

	private UiDialogs() {
	}

	/**
	 * Method which will show an inputdialog. The dialog keeps asking for input if no input is given.
	 * 
	 * @param component The component where the dialog needs to hover above
	 * @param message The message
	 * @param title The title of the dialog
	 * @param type Dialog type. Example: <code>JOptionPane.ERROR_MESSAGE</code>
	 * @return
	 */
	public static String inputDialog(Component component, String message, String title, int type) {
		Log.i(component, "inputDialog(" + component + "," + message + "," + title + "," + type + ")");

		String inputValue = "";
		while (inputValue.trim().equals("")) {
			inputValue = JOptionPane.showInputDialog(component, message, title, type);
			if (inputValue == null) {
				return null;
			} else {
				if (!inputValue.trim().equals("")) {
					return inputValue;
				} else {
					Log.i(component, "inputDialog() - no value entered");
					errorDialog(component, "Please enter an value!", "Error");
				}
			}
		}
		return inputValue;
	}

	/**
	 * Method which will show an errordialog to the user.
	 * 
	 * @param component The component where the dialog needs to hover above
	 * @param message The message
	 * @param title The title of the dialog
	 */
	public static void errorDialog(Component component, String message, String title) {
		Log.i(component, "errorDialog(" + component + "," + message + "," + title + ")");
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Method which will show an normal message dialog to the user.
	 * 
	 * @param component The component where the dialog needs to hover above
	 * @param message The message
	 * @param title The title of the dialog
	 */
	public static void messageDialog(Component component, String message, String title) {
		Log.i(component, "messageDialog(" + component + "," + message + "," + title + ")");
		JOptionPane.showMessageDialog(component, message, title, JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Method which will show an confirm message dialog to the user
	 * 
	 * @param component The component where the dialog needs to hover above
	 * @param message The message
	 * @param title The title of the dialog
	 */
	public static boolean confirmDialog(Component component, String message, String title) {
		Log.i(component, "confirmDialog(" + component + "," + message + "," + title + ")");
		int result = JOptionPane.showConfirmDialog(component, message, title, JOptionPane.OK_CANCEL_OPTION);

		return result == JOptionPane.OK_OPTION;
	}

	/**
	 * This method is a safe way of centring the jframe on an screen if there are multiple screens.
	 * 
	 * @param screen
	 * @param frame
	 */
	public static void showOnScreen(int screen, JFrame frame) {
		GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] gs = ge.getScreenDevices();
		if (screen > -1 && screen < gs.length) {
			Rectangle r = gs[screen].getDefaultConfiguration().getBounds();
			int windowWidth = (int) r.getWidth();
			int windowHeigh = (int) r.getHeight();

			int frameWidth = (int) frame.getWidth();
			int frameHeigh = (int) frame.getHeight();

			frame.setLocation((windowWidth - frameWidth) / 2, (windowHeigh - frameHeigh) / 2);
		} else if (gs.length <= 0) {
			throw new RuntimeException("No Screens Found");
		}
	}
}
