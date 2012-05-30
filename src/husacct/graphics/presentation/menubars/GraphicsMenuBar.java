package husacct.graphics.presentation.menubars;

import husacct.graphics.presentation.dialogs.GraphicsOptionsDialog;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

public class GraphicsMenuBar extends JPanel {
	private static final long serialVersionUID = -7419378432318031359L;
	private static final double MIN_SCALEFACTOR = 0.25;
	private static final double MAX_SCALEFACTOR = 1.75;

	protected Logger logger = Logger.getLogger(GraphicsMenuBar.class);

	private HashMap<String, String> icons;
	private ArrayList<JComponent> actions;

	private JButton zoomInButton, zoomOutButton, refreshButton, exportToImageButton, optionsDialogButton, showDependenciesButton, showViolationsButton;

	private JSlider zoomSlider;
	private GraphicsOptionsDialog graphicsOptionsDialog;

	private int menuItemMaxHeight = 45;
	private HashMap<String, String> menuBarLocale;

	public GraphicsMenuBar() {
		icons = new HashMap<String, String>();
		icons.put("options", "/husacct/common/resources/graphics/icon-cog.png");
		icons.put("zoomIn", "/husacct/common/resources/graphics/icon-zoom.png");
		icons.put("zoomOut", "/husacct/common/resources/icon-back.png");
		icons.put("refresh", "/husacct/common/resources/icon-refresh.png");
		icons.put("save", "/husacct/common/resources/icon-save.png");
		icons.put("dependenciesShow", "/husacct/common/resources/graphics/icon-link.png");
		icons.put("dependenciesHide", "/husacct/common/resources/graphics/icon-unlink.png");
		icons.put("violationsShow", "/husacct/common/resources/graphics/icon-errors-show.png");
		icons.put("violationsHide", "/husacct/common/resources/graphics/icon-errors-hide.png");
		initializeComponents();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		actions = new ArrayList<JComponent>();
		actions.add(zoomInButton);
		actions.add(zoomOutButton);
		actions.add(refreshButton);
		actions.add(exportToImageButton);
		actions.add(optionsDialogButton);
		actions.add(showDependenciesButton);
		actions.add(showViolationsButton);
		actions.add(zoomSlider);
	}
	
	private void setButtonIcon(JButton button, String iconKey){
		try {
			ImageIcon icon = new ImageIcon(getClass().getResource(icons.get(iconKey)));
			button.setIcon(icon);
			button.setMargin(new Insets(1,5,1,5));
		} catch (Exception e) {
			logger.warn("Could not find icon for \""+iconKey+"\".");
		}
	}

	private void initializeComponents() {
		zoomInButton = new JButton();
		zoomInButton.setSize(50, menuItemMaxHeight);
		add(zoomInButton);
		setButtonIcon(zoomInButton, "zoomIn");

		zoomOutButton = new JButton();
		zoomOutButton.setSize(50, menuItemMaxHeight);
		add(zoomOutButton);
		setButtonIcon(zoomOutButton, "zoomOut");

		refreshButton = new JButton();
		refreshButton.setSize(50, menuItemMaxHeight);
		add(refreshButton);
		setButtonIcon(refreshButton, "refresh");

		showDependenciesButton = new JButton();
		showDependenciesButton.setSize(40, menuItemMaxHeight);
		add(showDependenciesButton);

		showViolationsButton = new JButton();
		showViolationsButton.setSize(40, menuItemMaxHeight);
		add(showViolationsButton);
		
		exportToImageButton = new JButton();
		exportToImageButton.setSize(50, menuItemMaxHeight);
		add(exportToImageButton);
		setButtonIcon(exportToImageButton, "save");

		graphicsOptionsDialog = new GraphicsOptionsDialog();
		graphicsOptionsDialog.setIcons(icons);
		optionsDialogButton = new JButton();
		optionsDialogButton.setSize(40, menuItemMaxHeight);
		optionsDialogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graphicsOptionsDialog.showDialog();
			}
		});
		add(optionsDialogButton);
		setButtonIcon(optionsDialogButton, "options");

		zoomSlider = new JSlider(25, 175, 100);
		zoomSlider.setSize(50, menuItemMaxHeight);
		add(zoomSlider);
	}

	public void setZoomInAction(ActionListener listener) {
		zoomInButton.addActionListener(listener);
		graphicsOptionsDialog.setZoomInAction(listener);
	}

	public void setZoomOutAction(ActionListener listener) {
		zoomOutButton.addActionListener(listener);
		graphicsOptionsDialog.setZoomOutAction(listener);
	}

	public void setRefreshAction(ActionListener listener) {
		refreshButton.addActionListener(listener);
		graphicsOptionsDialog.setRefreshAction(listener);
	}

	public void setToggleDependenciesAction(ActionListener listener) {
		showDependenciesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JButton button = (JButton) event.getSource();
				if (button.isSelected()) {
					setDependecyIconToInactive();
				} else {
					setDependecyIconToActive();
				}
			}
		});
		showDependenciesButton.addActionListener(listener);
		graphicsOptionsDialog.setToggleDependenciesAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JCheckBox checkbox = (JCheckBox) event.getSource();
				if (checkbox.isSelected()) {
					setDependecyIconToInactive();
				} else {
					setDependecyIconToActive();
				}
			}
		});
		graphicsOptionsDialog.setToggleDependenciesAction(listener);
	}
	
	public void setDependecyIconToActive(){
		setButtonIcon(showDependenciesButton, "dependenciesShow");
		showDependenciesButton.setToolTipText(menuBarLocale.get("ShowDependencies"));
	}
	public void setDependecyIconToInactive(){
		setButtonIcon(showDependenciesButton, "dependenciesHide");
		showDependenciesButton.setToolTipText(menuBarLocale.get("HideDependencies"));
	}

	public void setToggleViolationsAction(ActionListener listener) {
		showViolationsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JButton button = (JButton) event.getSource();
				if (button.isSelected()) {
					setViolationsButtonToInactive();
				} else {
					setViolationsButtonToActive();
				}
			}
		});
		showViolationsButton.addActionListener(listener);
		graphicsOptionsDialog.setToggleViolationsAction(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JCheckBox checkbox = (JCheckBox) event.getSource();
				if (checkbox.isSelected()) {
					setViolationsButtonToInactive();
				} else {
					setViolationsButtonToActive();
				}
			}
		});
		graphicsOptionsDialog.setToggleViolationsAction(listener);
	}
	
	public void setViolationsButtonToActive(){
		setButtonIcon(showViolationsButton, "violationsShow");
		showViolationsButton.setToolTipText(menuBarLocale.get("ShowViolations"));
	}
	public void setViolationsButtonToInactive(){
		setButtonIcon(showViolationsButton, "violationsHide");
		showViolationsButton.setToolTipText(menuBarLocale.get("HideViolations"));
	}

	public void setToggleSmartLinesAction(ActionListener listener) {
		graphicsOptionsDialog.setToggleContextUpdatesAction(listener);
	}

	public void setOptionsDialogAction(ActionListener listener) {
		optionsDialogButton.addActionListener(listener);
	}

	public void setExportToImageAction(ActionListener listener) {
		exportToImageButton.addActionListener(listener);
		graphicsOptionsDialog.setExportToImageAction(listener);
	}

	public void setLayoutStrategyAction(ActionListener listener) {
		graphicsOptionsDialog.setLayoutStrategyAction(listener);
	}

	public void setZoomChangeListener(final ChangeListener listener) {
		zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				graphicsOptionsDialog.setZoomValue(((JSlider)ce.getSource()).getValue());
			}
		});
		zoomSlider.addChangeListener(listener);
		
		graphicsOptionsDialog.setZoomChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				zoomSlider.setValue(((JSlider)ce.getSource()).getValue());
			}
		});
		graphicsOptionsDialog.setZoomChangeListener(listener);
	}

	public void setLocale(HashMap<String, String> locale) {
		menuBarLocale = locale;
		try {
			zoomInButton.setToolTipText(menuBarLocale.get("ZoomIn"));
			zoomOutButton.setToolTipText(menuBarLocale.get("ZoomOut"));
			refreshButton.setToolTipText(menuBarLocale.get("Refresh"));
			exportToImageButton.setToolTipText(menuBarLocale.get("ExportToImage"));
			optionsDialogButton.setToolTipText(menuBarLocale.get("Options"));
			
			optionsDialogButton.setText(menuBarLocale.get("Options"));
			graphicsOptionsDialog.setLocale(menuBarLocale);
			graphicsOptionsDialog.setIcons(icons);
		} catch (NullPointerException e) {
			logger.warn("Locale for GraphicsMenuBar is not set properly.");
		}
	}

	public void setLayoutStrategyItems(String[] layoutStrategyItems) {
		graphicsOptionsDialog.setLayoutStrategyItems(layoutStrategyItems);
	}

	public void setSelectedLayoutStrategyItem(String item) {
		graphicsOptionsDialog.setSelectedLayoutStrategyItem(item);
	}

	public String getSelectedLayoutStrategyItem() {
		return graphicsOptionsDialog.getSelectedLayoutStrategyItem();
	}
	
	public void setDependencyToggle(boolean setting) {
		if(showDependenciesButton.isSelected()!=setting){
			showDependenciesButton.setSelected(setting);
		}
		if(setting){
			setDependecyIconToInactive();
		}else{
			setDependecyIconToActive();
		}
		graphicsOptionsDialog.setDependencyToggle(setting);
	}

	public void setViolationToggle(boolean setting) {
		if(showViolationsButton.isSelected()!=setting){
			showViolationsButton.setSelected(setting);
		}
		if(setting){
			setViolationsButtonToInactive();
		}else{
			setViolationsButtonToActive();
		}
		graphicsOptionsDialog.setViolationToggle(setting);
	}

	public void setContextUpdatesToggle(boolean setting) {
		graphicsOptionsDialog.setContextUpdatesToggle(setting);
	}

	public double getScaleFactor() {
		double scaleFactor = zoomSlider.getValue() / 100.0;
		scaleFactor = Math.max(MIN_SCALEFACTOR, scaleFactor);
		scaleFactor = Math.min(MAX_SCALEFACTOR, scaleFactor);

		return scaleFactor;
	}
	
	public void turnOffBar() {
		for(JComponent comp : actions){
			comp.setEnabled(false);
		}
		graphicsOptionsDialog.turnOff();
	}

	public void turnOnBar(){
		for(JComponent comp : actions){
			comp.setEnabled(true);
		}
		graphicsOptionsDialog.turnOn();
	}

}
