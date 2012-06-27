package husacct.graphics.presentation.dialogs;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.graphics.util.DrawingLayoutStrategy;
import husacct.graphics.util.UserInputListener;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

public class GraphicsOptionsDialog extends JDialog {
	private static final long serialVersionUID = 4794939901459687332L;
	protected Logger logger = Logger.getLogger(GraphicsOptionsDialog.class);
	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();

	private JPanel mainPanel, settingsPanel, actionsPanel, optionsPanel, zoomPanel;

	private int menuItemMaxHeight = 45;

	private JButton zoomInButton, zoomOutButton, refreshButton, exportToImageButton, okButton, applyButton, cancelButton;
	private JCheckBox showDependenciesOptionMenu, showViolationsOptionMenu, smartLinesOptionMenu;
	private JComboBox layoutStrategyOptions;
	private JSlider zoomSlider;
	private JLabel layoutStrategyLabel, zoomLabel;
	private ArrayList<JComponent> interfaceElements;
	private HashMap<String, Object> currentSettings;

	private int width, height;
	private HashMap<String, DrawingLayoutStrategy> layoutStrategiesTranslations;
	private String[] layoutStrategyItems;
	private IControlService controlService;

	public GraphicsOptionsDialog() {
		super();
		currentSettings = new HashMap<String, Object>();
		currentSettings.put("dependencies", true);
		currentSettings.put("violations", false);
		currentSettings.put("smartLines", false);
		currentSettings.put("layoutStrategy", DrawingLayoutStrategy.BASIC_LAYOUT);
		controlService = ServiceProvider.getInstance().getControlService();
		width = 550;
		height = 230;

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		add(mainPanel);

		layoutStrategiesTranslations = new HashMap<String, DrawingLayoutStrategy>();
		int i = 0;
		layoutStrategyItems = new String[DrawingLayoutStrategy.values().length];
		for (DrawingLayoutStrategy strategy : DrawingLayoutStrategy.values()) {
			String translation = controlService.getTranslatedString(strategy.toString());
			layoutStrategiesTranslations.put(translation, strategy);
			layoutStrategyItems[i] = translation;
			i++;
		}

		initGUI();

		interfaceElements = new ArrayList<JComponent>();
		interfaceElements.add(zoomInButton);
		interfaceElements.add(zoomOutButton);
		interfaceElements.add(refreshButton);
		interfaceElements.add(exportToImageButton);
		interfaceElements.add(showDependenciesOptionMenu);
		interfaceElements.add(showViolationsOptionMenu);
		interfaceElements.add(smartLinesOptionMenu);
		interfaceElements.add(layoutStrategyOptions);
		interfaceElements.add(zoomSlider);
		interfaceElements.add(okButton);
		interfaceElements.add(applyButton);
		interfaceElements.add(cancelButton);
	}

	public void showDialog() {
		setResizable(false);
		setSize(width, height);
		ServiceProvider.getInstance().getControlService().centerDialog(this);
		setVisible(true);
	}

	public void initGUI() {
		actionsPanel = new JPanel();
		zoomInButton = new JButton();
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (UserInputListener listener : listeners) {
					listener.moduleZoom();
				}
			}
		});
		actionsPanel.add(zoomInButton);

		zoomOutButton = new JButton();
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (UserInputListener listener : listeners) {
					listener.moduleZoomOut();
				}
			}
		});
		actionsPanel.add(zoomOutButton);

		refreshButton = new JButton();
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (UserInputListener listener : listeners) {
					listener.refreshDrawing();
				}
			}
		});
		actionsPanel.add(refreshButton);

		exportToImageButton = new JButton();
		exportToImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (UserInputListener listener : listeners) {
					listener.exportToImage();
				}
			}
		});
		actionsPanel.add(exportToImageButton);

		mainPanel.add(actionsPanel);

		optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(3, 1));

		showDependenciesOptionMenu = new JCheckBox();
		showDependenciesOptionMenu.setSize(40, menuItemMaxHeight);
		optionsPanel.add(showDependenciesOptionMenu);

		showViolationsOptionMenu = new JCheckBox();
		showViolationsOptionMenu.setSize(40, menuItemMaxHeight);
		optionsPanel.add(showViolationsOptionMenu);

		smartLinesOptionMenu = new JCheckBox();
		smartLinesOptionMenu.setSize(40, menuItemMaxHeight);
		optionsPanel.add(smartLinesOptionMenu);

		mainPanel.add(optionsPanel);

		settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridLayout(1, 2));
		layoutStrategyLabel = new JLabel();
		settingsPanel.add(layoutStrategyLabel);
		layoutStrategyOptions = new JComboBox(layoutStrategyItems);
		settingsPanel.add(layoutStrategyOptions);

		mainPanel.add(settingsPanel);

		zoomPanel = new JPanel();
		zoomPanel.setLayout(new GridLayout(1, 2));
		zoomLabel = new JLabel();
		zoomPanel.add(zoomLabel);
		zoomSlider = new JSlider(25, 175, 100);
		zoomSlider.setSize(50, width);
		zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				int scale = ((JSlider) ce.getSource()).getValue();
				for (UserInputListener listener : listeners) {
					listener.drawingZoomChanged(scale);
				}
			}
		});
		zoomPanel.add(zoomSlider);

		mainPanel.add(zoomPanel);

		JPanel confirmPanel = new JPanel();
		okButton = new JButton();
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyListeners();
				setVisible(false);
			}
		});
		confirmPanel.add(okButton);

		applyButton = new JButton();
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				notifyListeners();
			}
		});
		confirmPanel.add(applyButton);

		cancelButton = new JButton();
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetUIElementsToCurrentSettings();
				setVisible(false);
			}
		});
		confirmPanel.add(cancelButton);
		mainPanel.add(confirmPanel);
	}

	private void resetUIElementsToCurrentSettings() {
		showDependenciesOptionMenu.setSelected((Boolean) currentSettings.get("dependencies"));
		showViolationsOptionMenu.setSelected((Boolean) currentSettings.get("violations"));
		smartLinesOptionMenu.setSelected((Boolean) currentSettings.get("smartLines"));
		layoutStrategyOptions.setSelectedItem(controlService.getTranslatedString(currentSettings.get("layoutStrategy").toString()));
	}

	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}

	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
	}

	public void notifyListeners() {
		for (UserInputListener listener : listeners) {
			if (showDependenciesOptionMenu.isSelected()) {
				currentSettings.put("dependencies", true);
				listener.showDependencies();
			} else {
				currentSettings.put("dependencies", false);
				listener.hideDependencies();
			}
			if (showViolationsOptionMenu.isSelected()) {
				currentSettings.put("violations", true);
				listener.showViolations();
			} else {
				currentSettings.put("violations", true);
				listener.hideViolations();
			}
			if (smartLinesOptionMenu.isSelected()) {
				currentSettings.put("smartLines", true);
				listener.showSmartLines();
			} else {
				currentSettings.put("smartLines", false);
				listener.hideSmartLines();
			}
			DrawingLayoutStrategy selectedStrategy = getSelectedLayoutStrategyItem();
			if (null != selectedStrategy) {
				currentSettings.put("layoutStrategy", selectedStrategy);
				listener.changeLayoutStrategy(selectedStrategy);
			}
			listener.refreshDrawing();
		}
	}

	public void setLocale(HashMap<String, String> menuBarLocale) {
		try {
			zoomLabel.setText(menuBarLocale.get("Zoom"));
			layoutStrategyLabel.setText(menuBarLocale.get("LayoutStrategy"));
			zoomInButton.setText(menuBarLocale.get("ZoomIn"));
			zoomOutButton.setText(menuBarLocale.get("ZoomOut"));
			refreshButton.setText(menuBarLocale.get("Refresh"));
			exportToImageButton.setText(menuBarLocale.get("ExportToImage"));
			showDependenciesOptionMenu.setText(menuBarLocale.get("ShowDependencies"));
			showViolationsOptionMenu.setText(menuBarLocale.get("ShowViolations"));
			okButton.setText(menuBarLocale.get("Ok"));
			applyButton.setText(menuBarLocale.get("Apply"));
			cancelButton.setText(menuBarLocale.get("Cancel"));
			smartLinesOptionMenu.setText(menuBarLocale.get("LineContextUpdates"));
			setTitle(menuBarLocale.get("DiagramOptions"));
		} catch (NullPointerException e) {
			logger.warn("Locale is not set properly.");
		}
	}

	public void setIcons(HashMap<String, String> icons) {
		try {
			ImageIcon icon = new ImageIcon(getClass().getResource(icons.get("zoomIn")));
			zoomInButton.setIcon(icon);
			icon = new ImageIcon(getClass().getResource(icons.get("zoomOut")));
			zoomOutButton.setIcon(icon);
			icon = new ImageIcon(getClass().getResource(icons.get("refresh")));
			refreshButton.setIcon(icon);
			icon = new ImageIcon(getClass().getResource(icons.get("save")));
			exportToImageButton.setIcon(icon);
		} catch (NullPointerException e) {
			logger.warn("Icons are not set properly.");
		}
	}

	public void setSelectedLayoutStrategyItem(DrawingLayoutStrategy item) {
		currentSettings.put("layoutStrategy", item);
		layoutStrategyOptions.setSelectedItem(controlService.getTranslatedString(item.toString()));
	}

	public DrawingLayoutStrategy getSelectedLayoutStrategyItem() {
		DrawingLayoutStrategy selectedStrategy = null;
		String selectedItem = null;
		try {
			selectedItem = (String) layoutStrategyOptions.getSelectedItem();
			selectedStrategy = layoutStrategiesTranslations.get(selectedItem);
		} catch (Exception ex) {
			logger.debug("Could not find the selected layout strategy \"" + (selectedItem == null ? "null" : selectedItem) + "\".");
		}
		return selectedStrategy;
	}

	public void setDependenciesUIToActive() {
		currentSettings.put("dependencies", true);
		showDependenciesOptionMenu.setSelected(true);
	}

	public void setDependenciesUIToInactive() {
		currentSettings.put("dependencies", false);
		showDependenciesOptionMenu.setSelected(false);
	}

	public void setViolationsUIToActive() {
		currentSettings.put("violations", true);
		showViolationsOptionMenu.setSelected(true);
	}

	public void setViolationsUIToInactive() {
		currentSettings.put("violations", false);
		showViolationsOptionMenu.setSelected(false);
	}

	public void setSmartLinesUIToActive() {
		currentSettings.put("smartLines", true);
		smartLinesOptionMenu.setSelected(true);
	}

	public void setSmartLinesUIToInactive() {
		currentSettings.put("smartLines", false);
		smartLinesOptionMenu.setSelected(false);
	}

	public void setZoomValue(int value) {
		zoomSlider.setValue(value);
	}

	public void turnOn() {
		for (JComponent element : interfaceElements) {
			element.setEnabled(true);
		}
	}

	public void turnOff() {
		for (JComponent element : interfaceElements) {
			element.setEnabled(false);
		}
	}
}