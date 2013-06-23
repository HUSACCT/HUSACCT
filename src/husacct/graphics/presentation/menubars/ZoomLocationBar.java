package husacct.graphics.presentation.menubars;

import husacct.common.help.presentation.HelpableJPanel;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JLabel;

public class ZoomLocationBar extends HelpableJPanel {
	private static final long serialVersionUID = 1025962225565217061L;
	private String rootLocale;
	private JButton rootLocationButton;
	private final String LOCATION_COMBINER = "+";
	private final String LOCATION_SEPERATOR = ".";
	
	private int menuItemMaxHeight = 45;
	
	private ArrayList<JButton> buttons;
	private HashMap<JButton, String[]> buttonPaths = new HashMap<JButton, String[]>();
	private ArrayList<LocationButtonActionListener> locationButtonPressListener;
	
	public ZoomLocationBar() {
		setLayout(new FlowLayout(FlowLayout.LEFT));
		locationButtonPressListener = new ArrayList<LocationButtonActionListener>();
	}
	
	public void addLocationButtonPressListener(
			LocationButtonActionListener actionListener) {
		locationButtonPressListener.add(actionListener);
	}
	
	private void addLocationSeperator() {
		add(new JLabel(" " + LOCATION_SEPERATOR + " "));
	}
	
	private void createAndAddLocationButton(String levelName, String[] fullPath) {
		add(createLocationButton(levelName, fullPath));
	}
	
	private void createAndAddRootLocationButton() {
		rootLocationButton = createLocationButton(rootLocale, new String[] {});
		add(rootLocationButton);
	}
	
	private JButton createLocationButton(String levelName, String[] fullPath) {
		JButton locationStringButton = new JButton(levelName);
		locationStringButton.setSize(10, menuItemMaxHeight);
		locationStringButton.setMargin(new Insets(0, 0, 0, 0));
		buttonPaths.put(locationStringButton, fullPath);
		buttons.add(locationStringButton);
		locationStringButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				pressLocationButton(event);
			}
		});
		return locationStringButton;
	}
	
	public String[] getSecondLastPath() {
		try {
			int secondLastIndex = buttons.size() - 2;
			JButton secondLastButton = buttons.get(secondLastIndex);
			return buttonPaths.get(secondLastButton);
		} catch (Exception e) {
			return new String[] {};
		}
	}
	
	private void pressLocationButton(ActionEvent event) {
		for (LocationButtonActionListener listener : locationButtonPressListener)
			listener.actionPerformed(buttonPaths.get(event.getSource()));
	}
	
	public void removeLocationButtonPressListener(
			LocationButtonActionListener actionListener) {
		locationButtonPressListener.remove(actionListener);
	}
	
	public void setLocale(String rootString) {
		rootLocale = rootString;
	}
	
	public void turnOffBar() {
		for (JButton button : buttons)
			button.setEnabled(false);
	}
	
	public void turnOnBar() {
		for (JButton button : buttons)
			button.setEnabled(true);
	}
	
	public void updateLocationBar(String[] currentPaths) {
		removeAll();
		buttons = new ArrayList<JButton>();
		createAndAddRootLocationButton();
		
		HashMap<Integer, HashMap<Integer, String>> multiLevelPath = new HashMap<Integer, HashMap<Integer, String>>();
		if (currentPaths.length > 0)
			for (int pathID = 0; pathID < currentPaths.length; pathID++) {
				String selectedPath = currentPaths[pathID];
				String[] pathParts = selectedPath.split("\\"
						+ LOCATION_SEPERATOR);
				for (int pathLevel = 0; pathLevel < pathParts.length; pathLevel++) {
					if (null == multiLevelPath.get(pathLevel))
						multiLevelPath.put(pathLevel,
								new HashMap<Integer, String>());
					HashMap<Integer, String> tmpAdderHashMap = multiLevelPath
							.get(pathLevel);
					tmpAdderHashMap.put(pathID, pathParts[pathLevel]);
					multiLevelPath.put(pathLevel, tmpAdderHashMap);
				}
			}
		
		if (multiLevelPath.size() > 0) {
			addLocationSeperator();
			Integer[] levelKeySet = multiLevelPath.keySet().toArray(
					new Integer[] {});
			HashMap<Integer, String> currentPath = new HashMap<Integer, String>();
			for (Integer level : levelKeySet) {
				String visiblePath = "";
				HashMap<Integer, String> levelPath = multiLevelPath.get(level);
				Set<Integer> keySet = levelPath.keySet();
				ArrayList<String> capturedLevels = new ArrayList<String>();
				for (Integer pathID : keySet) {
					if (!capturedLevels.contains(levelPath.get(pathID)))
						visiblePath += " " + LOCATION_COMBINER + " "
								+ levelPath.get(pathID);
					String currentValuePath = currentPath.get(pathID);
					if (null == currentValuePath) {
						currentValuePath = "";
						currentPath.put(pathID, currentValuePath);
					} else
						currentValuePath = currentPath.get(pathID)
						+ LOCATION_SEPERATOR;
					currentPath.put(pathID,
							currentValuePath + levelPath.get(pathID));
					capturedLevels.add(levelPath.get(pathID));
				}
				ArrayList<String> entrySetTranformToArray = new ArrayList<String>();
				for (Entry<Integer, String> p : currentPath.entrySet())
					entrySetTranformToArray.add(p.getValue());
				createAndAddLocationButton(
						visiblePath.replaceFirst("\\" + LOCATION_COMBINER, "")
						.trim(),
						entrySetTranformToArray.toArray(new String[] {}));
				
				if (!levelKeySet[levelKeySet.length - 1].equals(level))
					addLocationSeperator();
			}
		}
	}
}
