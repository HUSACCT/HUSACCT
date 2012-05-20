package husacct.graphics.presentation.menubars;

import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenuBar;

public class ZoomLocationBar extends JMenuBar {
	private static final long serialVersionUID = 1025962225565217061L;
	private String rootLocale;
	private JButton rootLocationButton;
	private final String LOCATION_SEPERATOR = ".";

	private int menuItemMaxHeight = 45;

	private HashMap<JButton, String> buttonPaths = new HashMap<JButton, String>();
	private ArrayList<LocationButtonActionListener> locationButtonPressListener;

	public ZoomLocationBar() {
		locationButtonPressListener = new ArrayList<LocationButtonActionListener>();
	}

	public void setLocale(String rootString) {
		rootLocale = rootString;
	}

	public void updateLocationBar(String currentPath) {
		removeAll();
		createAndAddRootLocationButton();

		String pathUntilNow = "";
		String[] pathParts = new String[] {};
		if (!currentPath.equals("")) {
			pathParts = currentPath.split("\\" + LOCATION_SEPERATOR);
		}
		if (pathParts.length > 0) {
			addLocationSeperator();
			for (String part : pathParts) {
				pathUntilNow = pathUntilNow + part;
				createAndAddLocationButton(part, pathUntilNow);

				if (!pathParts[pathParts.length - 1].equals(part)) {
					addLocationSeperator();
					pathUntilNow = pathUntilNow + LOCATION_SEPERATOR;
				}
			}
		}
	}

	private void createAndAddRootLocationButton() {
		rootLocationButton = createLocationButton(rootLocale, "");
		add(rootLocationButton);
	}

	private void createAndAddLocationButton(String levelName, String fullPath) {
		add(createLocationButton(levelName, fullPath));
	}

	private JButton createLocationButton(String levelName, String fullPath) {
		JButton locationStringButton = new JButton(levelName);
		locationStringButton.setSize(10, menuItemMaxHeight);
		locationStringButton.setMargin(new Insets(0, 0, 0, 0));
		buttonPaths.put(locationStringButton, fullPath);
		locationStringButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				pressLocationButton(event);
			}
		});
		return locationStringButton;
	}

	private void pressLocationButton(ActionEvent event) {
		for (LocationButtonActionListener listener : locationButtonPressListener) {
			listener.actionPerformed(buttonPaths.get(event.getSource()));
		}
	}

	private void addLocationSeperator() {
		add(new JLabel(" " + LOCATION_SEPERATOR + " "));
	}

	public void addLocationButtonPressListener(LocationButtonActionListener actionListener) {
		locationButtonPressListener.add(actionListener);
	}

	public void removeLocationButtonPressListener(LocationButtonActionListener actionListener) {
		locationButtonPressListener.remove(actionListener);
	}
}
