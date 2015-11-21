package husacct.graphics.presentation.dialogs;

import husacct.ServiceProvider;
import husacct.common.help.presentation.HelpableJDialog;
import husacct.common.locale.ILocaleService;
import husacct.graphics.util.DrawingLayoutStrategy;
import husacct.graphics.util.UserInputListener;

import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

public class GraphicsOptionsDialog extends HelpableJDialog {
	private static final long						serialVersionUID	= 4794939901459687332L;
	protected Logger								logger				= Logger.getLogger(GraphicsOptionsDialog.class);
	private ArrayList<UserInputListener>			listeners			= new ArrayList<UserInputListener>();
	
	private JPanel									mainPanel, settingsPanel,
	globalActionsPanel, figuresActionsPanel, optionsPanel, zoomPanel,
	layoutStrategyPanel;
	
	private int										menuItemMaxHeight	= 45;
	
	private JButton									zoomInButton,
	zoomOutButton, refreshButton, exportToImageButton,
	hideFiguresButton, showFiguresButton, okButton, applyButton,
	cancelButton;
	private JCheckBox								showDependenciesOptionMenu,
	showViolationsOptionMenu, smartLinesOptionMenu,
	showExternalLibraries, enableThickLines;
	private JComboBox<String>						layoutStrategyOptions;
	private JSlider									zoomSlider;
	private JLabel									layoutStrategyLabel,
	zoomLabel;
	private ArrayList<JComponent>					interfaceElements;
	private HashMap<String, Object>					currentSettings;
	
	private int										totalWidth, totalHeight,
	paddingSize, labelWidth, elementWidth, elementHeight;
	private HashMap<String, DrawingLayoutStrategy>	layoutStrategiesTranslations;
	private String[]								layoutStrategyItems;
	private ILocaleService							localeService = ServiceProvider.getInstance().getLocaleService();
	
	public GraphicsOptionsDialog() {
		super((GraphicsOptionsDialog) null, true);
		currentSettings = new HashMap<String, Object>();
		currentSettings.put("dependencies", true);
		currentSettings.put("violations", false);
		currentSettings.put("thickLines", false); //representativeLineWidth is false
		currentSettings.put("smartLines", true);
		currentSettings.put("libraries", true);
		currentSettings.put("layoutStrategy", DrawingLayoutStrategy.BASIC_LAYOUT);
		
		totalWidth = 550;
		totalHeight = 260;
		paddingSize = 10;
		labelWidth = 100;
		elementHeight = 20;
		elementWidth = totalWidth - labelWidth - paddingSize * 2 - 20;
		
		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		add(mainPanel);
		
		layoutStrategiesTranslations = new HashMap<String, DrawingLayoutStrategy>();
		int i = 0;
		layoutStrategyItems = new String[DrawingLayoutStrategy.values().length];
		for (DrawingLayoutStrategy strategy : DrawingLayoutStrategy.values()) {
			String translation = localeService.getTranslatedString(strategy.toString());
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
		interfaceElements.add(showExternalLibraries);
		interfaceElements.add(enableThickLines);
		interfaceElements.add(smartLinesOptionMenu);
		interfaceElements.add(layoutStrategyOptions);
		interfaceElements.add(zoomSlider);
		interfaceElements.add(okButton);
		interfaceElements.add(applyButton);
		interfaceElements.add(cancelButton);
	}
	
	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}
	
	public DrawingLayoutStrategy getSelectedLayoutStrategyItem() {
		DrawingLayoutStrategy selectedStrategy = null;
		String selectedItem = null;
		try {
			selectedItem = (String) layoutStrategyOptions.getSelectedItem();
			selectedStrategy = layoutStrategiesTranslations.get(selectedItem);
		} catch (Exception ex) {
			logger.debug("Could not find the selected layout strategy \""
					+ (selectedItem == null ? "null" : selectedItem) + "\".");
		}
		return selectedStrategy;
	}
	
	public void initGUI() {
		globalActionsPanel = new JPanel();
		zoomInButton = new JButton();
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (UserInputListener listener : listeners)
					listener.zoomIn();
			}
		});
		globalActionsPanel.add(zoomInButton);
		
		zoomOutButton = new JButton();
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (UserInputListener listener : listeners)
					listener.zoomOut();
			}
		});
		globalActionsPanel.add(zoomOutButton);
		
		refreshButton = new JButton();
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (UserInputListener listener : listeners)
					listener.refreshDrawing();
			}
		});
		globalActionsPanel.add(refreshButton);
		
		exportToImageButton = new JButton();
		exportToImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (UserInputListener listener : listeners)
					listener.exportImage();
			}
		});
		globalActionsPanel.add(exportToImageButton);
		mainPanel.add(globalActionsPanel);
		
		figuresActionsPanel = new JPanel();
		hideFiguresButton = new JButton();
		hideFiguresButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (UserInputListener listener : listeners)
					listener.moduleHide();
			}
		});
		figuresActionsPanel.add(hideFiguresButton);
		
		showFiguresButton = new JButton();
		showFiguresButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (UserInputListener listener : listeners)
					listener.moduleRestoreHiddenModules();
			}
		});
		figuresActionsPanel.add(showFiguresButton);
		mainPanel.add(figuresActionsPanel);
		
		optionsPanel = new JPanel();
		optionsPanel.setBorder(new EmptyBorder(0, paddingSize, 0, paddingSize));
		optionsPanel.setLayout(new GridLayout(3, 1));
		
		showDependenciesOptionMenu = new JCheckBox();
		showDependenciesOptionMenu.setPreferredSize(new Dimension(40,
				menuItemMaxHeight));
		showDependenciesOptionMenu.setMaximumSize(new Dimension(40,
				menuItemMaxHeight));
		optionsPanel.add(showDependenciesOptionMenu);
		
		showViolationsOptionMenu = new JCheckBox();
		showViolationsOptionMenu.setSize(40, menuItemMaxHeight);
		optionsPanel.add(showViolationsOptionMenu);
		
		showExternalLibraries = new JCheckBox();
		showExternalLibraries.setSize(40, menuItemMaxHeight);
		optionsPanel.add(showExternalLibraries);
		
		enableThickLines = new JCheckBox();
		enableThickLines.setSize(40, menuItemMaxHeight);
		optionsPanel.add(enableThickLines);
		
		smartLinesOptionMenu = new JCheckBox();
		smartLinesOptionMenu.setSize(40, menuItemMaxHeight);
		optionsPanel.add(smartLinesOptionMenu);
		
		mainPanel.add(optionsPanel);
		
		settingsPanel = new JPanel();
		settingsPanel.setLayout(new GridLayout(2, 2));
		settingsPanel.setBorder(new EmptyBorder(0, paddingSize, 0, paddingSize));
		
		layoutStrategyPanel = new JPanel();
		layoutStrategyPanel.setSize(getWidth(), getHeight());
		layoutStrategyPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		layoutStrategyLabel = new JLabel();
		layoutStrategyLabel.setPreferredSize(new Dimension(labelWidth, elementHeight));
		layoutStrategyPanel.add(layoutStrategyLabel);
		
		layoutStrategyOptions = new JComboBox<String>(layoutStrategyItems);
		layoutStrategyOptions.setPreferredSize(new Dimension(elementWidth, elementHeight));
		layoutStrategyPanel.add(layoutStrategyOptions);
		settingsPanel.add(layoutStrategyPanel);
		
		zoomPanel = new JPanel();
		zoomPanel.setSize(getWidth(), getHeight());
		zoomPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		zoomLabel = new JLabel();
		zoomLabel.setPreferredSize(new Dimension(labelWidth, elementHeight));
		zoomPanel.add(zoomLabel);
		
		zoomSlider = new JSlider(25, 175, 100);
		zoomSlider.setPreferredSize(new Dimension(elementWidth, elementHeight));
		zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				int scale = ((JSlider) ce.getSource()).getValue();
				for (UserInputListener listener : listeners)
					listener.zoomFactorChanged(scale);
			}
		});
		zoomPanel.add(zoomSlider);
		settingsPanel.add(zoomPanel);
		
		mainPanel.add(settingsPanel);
		
		JPanel confirmPanel = new JPanel();
		confirmPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
		okButton = new JButton();
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				notifyListeners();
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
	
	public void notifyListeners() {
		for (UserInputListener listener : listeners) {
			if (showDependenciesOptionMenu.isSelected()) {
				currentSettings.put("dependencies", true);
				listener.dependenciesShow();
			} else {
				currentSettings.put("dependencies", false);
				listener.dependenciesHide();
			}
			if (enableThickLines.isSelected()) {
				currentSettings.put("thickLines", true);
				listener.proportionalLinesEnable();
			} else {
				currentSettings.put("thickLines", false);
				listener.proportionalLinesDisable();
			}
			if (showViolationsOptionMenu.isSelected()) {
				currentSettings.put("violations", true);
				listener.violationsShow();
			} else {
				currentSettings.put("violations", true);
				listener.violationsHide();
			}
			if (smartLinesOptionMenu.isSelected()) {
				currentSettings.put("smartLines", true);
				listener.smartLinesEnable();
			} else {
				currentSettings.put("smartLines", false);
				listener.smartLinesDisable();
			}
			if (showExternalLibraries.isSelected()) {
				currentSettings.put("libraries", true);
				listener.librariesShow();
			} else {
				currentSettings.put("libraries", false);
				listener.librariesHide();
			}
			DrawingLayoutStrategy selectedStrategy = getSelectedLayoutStrategyItem();
			if ((selectedStrategy != null) && !selectedStrategy.toString().equals(currentSettings.get("layoutStrategy").toString())) {
				currentSettings.put("layoutStrategy", selectedStrategy);
				listener.layoutStrategyChange(selectedStrategy);
			}
			listener.refreshDrawing();
		}
	}
	
	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
	}
	
	private void resetUIElementsToCurrentSettings() {
		showDependenciesOptionMenu.setSelected((Boolean) currentSettings.get("dependencies"));
		showViolationsOptionMenu.setSelected((Boolean) currentSettings.get("violations"));
		showExternalLibraries.setSelected((Boolean) currentSettings.get("libraries"));
		enableThickLines.setSelected((Boolean) currentSettings.get("thickLines"));
		smartLinesOptionMenu.setSelected((Boolean) currentSettings.get("smartLines"));
		layoutStrategyOptions.setSelectedItem(localeService.getTranslatedString(currentSettings.get("layoutStrategy").toString()));
	}
	
	public void setDependenciesUIToActive() {
		currentSettings.put("dependencies", true);
		showDependenciesOptionMenu.setSelected(true);
	}
	
	public void setDependenciesUIToInactive() {
		currentSettings.put("dependencies", false);
		showDependenciesOptionMenu.setSelected(false);
	}
	
	public void setIcons(HashMap<String, String> icons) {
		try {
			ImageIcon icon = new ImageIcon(getClass().getResource(
					icons.get("zoomIn")));
			zoomInButton.setIcon(icon);
			icon = new ImageIcon(getClass().getResource(icons.get("zoomOut")));
			zoomOutButton.setIcon(icon);
			icon = new ImageIcon(getClass().getResource(icons.get("refresh")));
			refreshButton.setIcon(icon);
			icon = new ImageIcon(getClass().getResource(icons.get("save")));
			exportToImageButton.setIcon(icon);
			icon = new ImageIcon(getClass().getResource(
					icons.get("hideFigures")));
			hideFiguresButton.setIcon(icon);
			icon = new ImageIcon(getClass().getResource(
					icons.get("showFigures")));
			showFiguresButton.setIcon(icon);
		} catch (NullPointerException e) {
			logger.warn("Icons are not set properly.");
		}
	}
	
	// TODO add a button to GraphicsMenuBar to enable libraries
	public void setLibrariesUIToActive() {
		currentSettings.put("libraries", true);
		showExternalLibraries.setSelected(true);
	}
	
	// TODO add a button to GraphicsMenuBar to disable libraries
	public void setLibrariesUIToInactive() {
		currentSettings.put("libraries", false);
		showExternalLibraries.setSelected(false);
	}
	
	public void setThickLinesUIToActive() {
		currentSettings.put("thickLines", true);
		enableThickLines.setSelected(true);
	}
	
	public void setThickLinesUIToInactive() {
		currentSettings.put("thickLines", false);
		enableThickLines.setSelected(false);
	}
	
	public void setLocale(HashMap<String, String> menuBarLocale) {
		try {
			zoomLabel.setText(menuBarLocale.get("Zoom"));
			layoutStrategyLabel.setText(menuBarLocale.get("LayoutStrategy"));
			zoomInButton.setText(menuBarLocale.get("ZoomIn"));
			zoomOutButton.setText(menuBarLocale.get("ZoomOut"));
			refreshButton.setText(menuBarLocale.get("Refresh"));
			exportToImageButton.setText(menuBarLocale.get("ExportToImage"));
			
			showDependenciesOptionMenu.setText(menuBarLocale
					.get("ShowDependencies"));
			showViolationsOptionMenu.setText(menuBarLocale
					.get("ShowViolations"));
			showExternalLibraries.setText(menuBarLocale
					.get("ShowExternalLibraries"));
			//TODO name for checkbox 
			enableThickLines.setText("Proportional Line Width");
			
			okButton.setText(menuBarLocale.get("Ok"));
			applyButton.setText(menuBarLocale.get("Apply"));
			cancelButton.setText(menuBarLocale.get("Cancel"));
			smartLinesOptionMenu.setText(menuBarLocale
					.get("LineContextUpdates"));
			hideFiguresButton.setText(menuBarLocale.get("HideModules"));
			showFiguresButton
			.setText(menuBarLocale.get("RestoreHiddenModules"));
			setTitle(menuBarLocale.get("DiagramOptions"));
		} catch (NullPointerException e) {
			logger.warn("Locale is not set properly.");
		}
	}
	
	public void setSelectedLayoutStrategyItem(DrawingLayoutStrategy item) {
		currentSettings.put("layoutStrategy", item);
		layoutStrategyOptions.setSelectedItem(localeService
				.getTranslatedString(item.toString()));
	}
	
	public void setSmartLinesUIToActive() {
		currentSettings.put("smartLines", true);
		smartLinesOptionMenu.setSelected(true);
	}
	
	public void setSmartLinesUIToInactive() {
		currentSettings.put("smartLines", false);
		smartLinesOptionMenu.setSelected(false);
	}
	
	public void setViolationsUIToActive() {
		currentSettings.put("violations", true);
		showViolationsOptionMenu.setSelected(true);
	}
	
	public void setViolationsUIToInactive() {
		currentSettings.put("violations", false);
		showViolationsOptionMenu.setSelected(false);
	}
	
	public void setZoomValue(int value) {
		zoomSlider.setValue(value);
	}
	
	public void showDialog() {
		setResizable(false);
		setAlwaysOnTop(false);
		setSize(totalWidth, totalHeight);
		ServiceProvider.getInstance().getControlService().centerDialog(this);
		setVisible(true);
	}
	
	public void turnOff() {
		for (JComponent element : interfaceElements)
			element.setEnabled(false);
	}
	
	public void turnOn() {
		for (JComponent element : interfaceElements)
			element.setEnabled(true);
	}
}