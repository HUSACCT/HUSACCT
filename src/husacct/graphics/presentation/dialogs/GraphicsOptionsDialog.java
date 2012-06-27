package husacct.graphics.presentation.dialogs;

import husacct.ServiceProvider;
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
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

public class GraphicsOptionsDialog extends JDialog {
	private static final long serialVersionUID = 4794939901459687332L;
	protected Logger logger = Logger.getLogger(GraphicsOptionsDialog.class);
	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();

	private JPanel mainPanel, settingsPanel, actionsPanel, optionsPanel, zoomPanel;

	private int menuItemMaxHeight = 45;
	private boolean dependenciesToggleChanged, violationsToggleChanged, smartLinesToggleChanged, layoutStrategyChanged;

	private JButton zoomInButton, zoomOutButton, refreshButton, exportToImageButton, okButton, applyButton, cancelButton;
	private JCheckBox showDependenciesOptionMenu, showViolationsOptionMenu, smartLinesOptionMenu;
	private JComboBox layoutStrategyOptions;
	private JSlider zoomSlider;
	private JLabel layoutStrategyLabel, zoomLabel;
	private ArrayList<JComponent> interfaceElements;

	private int width, height;

	public GraphicsOptionsDialog() {
		super();
		width = 550;
		height = 230;
		resetChangeBooleans();

		mainPanel = new JPanel();
		mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
		add(mainPanel);
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

	private void resetChangeBooleans() {
		dependenciesToggleChanged = false;
		violationsToggleChanged = false;
		smartLinesToggleChanged = false;
		layoutStrategyChanged = false;
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
		actionsPanel.add(zoomInButton);

		zoomOutButton = new JButton();
		actionsPanel.add(zoomOutButton);

		refreshButton = new JButton();
		actionsPanel.add(refreshButton);

		exportToImageButton = new JButton();
		actionsPanel.add(exportToImageButton);

		mainPanel.add(actionsPanel);

		optionsPanel = new JPanel();
		optionsPanel.setLayout(new GridLayout(3, 1));

		showDependenciesOptionMenu = new JCheckBox();
		showDependenciesOptionMenu.setSize(40, menuItemMaxHeight);
		showDependenciesOptionMenu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dependenciesToggleChanged = true; //TODO: additional check if this isn't the original value
			}
		});
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
		layoutStrategyOptions = new JComboBox();
		settingsPanel.add(layoutStrategyOptions);

		mainPanel.add(settingsPanel);

		zoomPanel = new JPanel();
		zoomPanel.setLayout(new GridLayout(1, 2));
		zoomLabel = new JLabel();
		zoomPanel.add(zoomLabel);
		zoomSlider = new JSlider(25, 175, 100);
		zoomSlider.setSize(50, width);
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
				//TODO: Reset changes
				setVisible(false);
			}
		});
		confirmPanel.add(cancelButton);
		mainPanel.add(confirmPanel);
	}
	
	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}

	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
	}
	
	public void notifyListeners(){
		for(UserInputListener listener : listeners){
			if(showDependenciesOptionMenu.isSelected()){
				listener.showDependencies();
			}else{
				listener.hideDependencies();
			}
			if(showViolationsOptionMenu.isSelected()){
				listener.showViolations();
			}else{
				listener.hideViolations();
			}
		}
		resetChangeBooleans();
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

	public void setZoomInAction(ActionListener listener) {
		zoomInButton.addActionListener(listener);
	}

	public void setZoomOutAction(ActionListener listener) {
		zoomOutButton.addActionListener(listener);
	}

	public void setRefreshAction(ActionListener listener) {
		refreshButton.addActionListener(listener);
	}

	public void setExportToImageAction(ActionListener listener) {
		exportToImageButton.addActionListener(listener);
	}

	public void setToggleContextUpdatesAction(ActionListener listener) {
		smartLinesOptionMenu.addActionListener(listener);
	}

	public void setLayoutStrategyAction(ActionListener listener) {
		layoutStrategyOptions.addActionListener(listener);
	}

	public void setContextUpdatesToggle(boolean setting) {
		smartLinesOptionMenu.setSelected(setting);
	}

	public void setZoomChangeListener(ChangeListener listener) {
		zoomSlider.addChangeListener(listener);
	}

	public void setLayoutStrategyItems(String[] layoutStrategyItems) {
		layoutStrategyOptions.removeAllItems();
		for (String item : layoutStrategyItems) {
			layoutStrategyOptions.addItem(item);
		}
	}

	public void setSelectedLayoutStrategyItem(String item) {
		layoutStrategyOptions.setSelectedItem(item);
	}

	public String getSelectedLayoutStrategyItem() {
		return (String) layoutStrategyOptions.getSelectedItem();
	}

	public void setDependenciesUIToActive() {
		showDependenciesOptionMenu.setSelected(true);
	}
	
	public void setDependenciesUIToInactive() {
		showDependenciesOptionMenu.setSelected(false);
	}
	
	public void setViolationsUIToActive() {
		showViolationsOptionMenu.setSelected(true);
	}
	
	public void setViolationsUIToInactive() {
		showViolationsOptionMenu.setSelected(false);
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
