package husacct.graphics.presentation.menubars;

import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JMenuBar;

import org.apache.log4j.Logger;

public class GraphicsMenuBar extends JMenuBar {
	private static final long serialVersionUID = -7419378432318031359L;
	protected Logger logger = Logger.getLogger(GraphicsMenuBar.class);
	private JButton goToParentMenu, refreshMenu, exportToImageMenu;
	private JCheckBox showViolationsOptionMenu;

	private int menuItemMaxHeight = 45;

	public GraphicsMenuBar() {
		initializeComponents();
	}

	private void initializeComponents() {
		goToParentMenu = new JButton();
		goToParentMenu.setSize(50, menuItemMaxHeight);
		add(goToParentMenu);

		refreshMenu = new JButton();
		refreshMenu.setSize(50, menuItemMaxHeight);
		add(refreshMenu);

		showViolationsOptionMenu = new JCheckBox();
		showViolationsOptionMenu.setSize(40, menuItemMaxHeight);
		add(showViolationsOptionMenu);

		exportToImageMenu = new JButton();
		exportToImageMenu.setSize(50, menuItemMaxHeight);
		add(exportToImageMenu);
	}

	public void setLevelUpAction(ActionListener action) {
		goToParentMenu.addActionListener(action);
	}

	public void setRefreshAction(ActionListener action) {
		refreshMenu.addActionListener(action);
	}

	public void setToggleViolationsAction(ActionListener action) {
		showViolationsOptionMenu.addActionListener(action);
	}

	public void setExportToImageAction(ActionListener action) {
		exportToImageMenu.addActionListener(action);
	}

	public void setLocale(HashMap<String, String> menuBarLocale) {
		try {
			goToParentMenu.setText(menuBarLocale.get("LevelUp"));
			refreshMenu.setText(menuBarLocale.get("Refresh"));
			showViolationsOptionMenu.setText(menuBarLocale.get("ShowViolations"));
			exportToImageMenu.setText(menuBarLocale.get("ExportToImage"));
		} catch (NullPointerException e) {
			logger.warn("Locale for GraphicsMenuBar is not set properly.");
		}
	}

	public void setViolationToggle(boolean setting) {
		showViolationsOptionMenu.setSelected(setting);
	}

}
