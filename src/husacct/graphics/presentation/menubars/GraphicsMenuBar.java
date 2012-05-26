package husacct.graphics.presentation.menubars;

import husacct.graphics.presentation.dialogs.GraphicsOptionsDialog;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
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

	private JButton zoomInButton, zoomOutButton, refreshButton, exportToImageButton, optionsDialogButton;
	private JCheckBox showDependenciesOptionMenu, showViolationsOptionMenu;

	private JSlider zoomSlider;
	private GraphicsOptionsDialog graphicsOptionsDialog;

	private int menuItemMaxHeight = 45;

	public GraphicsMenuBar() {
		icons = new HashMap<String, String>();
		icons.put("options", "/husacct/common/resources/graphics/icon-cog.png");
		icons.put("zoomIn", "/husacct/common/resources/icon-zoom.png");
		icons.put("zoomOut", "/husacct/common/resources/icon-back.png");
		icons.put("refresh", "/husacct/common/resources/icon-refresh.png");
		icons.put("save", "/husacct/common/resources/icon-save.png");
		icons.put("dependenciesShow", "/husacct/common/resources/graphics/icon-connect.png");
		icons.put("dependenciesHide", "/husacct/common/resources/graphics/icon-disconnect.png");
		icons.put("violationsShow", "/husacct/common/resources/graphics/icon-error.png");
		icons.put("violationsHide", "/husacct/common/resources/graphics/icon-error_delete.png");
		initializeComponents();
		setLayout(new FlowLayout(FlowLayout.LEFT));
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
	
	private void setCheckBoxIcon(JCheckBox checkbox, String iconKey){
		try {
			ImageIcon icon = new ImageIcon(getClass().getResource(icons.get(iconKey)));
			checkbox.setIcon(icon);
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

		exportToImageButton = new JButton();
		exportToImageButton.setSize(50, menuItemMaxHeight);
		add(exportToImageButton);
		setButtonIcon(exportToImageButton, "save");

		showDependenciesOptionMenu = new JCheckBox();
		showDependenciesOptionMenu.setSize(40, menuItemMaxHeight);
		add(showDependenciesOptionMenu);
		setCheckBoxIcon(showDependenciesOptionMenu, "dependenciesShow");

		showViolationsOptionMenu = new JCheckBox();
		showViolationsOptionMenu.setSize(40, menuItemMaxHeight);
		add(showViolationsOptionMenu);
		setCheckBoxIcon(showViolationsOptionMenu, "violationsHide");

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
		ActionListener action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JCheckBox checkbox = (JCheckBox) event.getSource();
				if (checkbox.isSelected()) {
					setCheckBoxIcon(showDependenciesOptionMenu, "dependenciesShow");
				} else {
					setCheckBoxIcon(showDependenciesOptionMenu, "dependenciesHide");
				}
			}
		};
		showDependenciesOptionMenu.addActionListener(action);
		showDependenciesOptionMenu.addActionListener(listener);
		graphicsOptionsDialog.setToggleDependenciesAction(action);
		graphicsOptionsDialog.setToggleDependenciesAction(listener);
	}

	public void setToggleViolationsAction(ActionListener listener) {
		ActionListener action = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				JCheckBox checkbox = (JCheckBox) event.getSource();
				ImageIcon icon = null;
				if (checkbox.isSelected()) {
					icon = new ImageIcon(getClass().getResource(icons.get("violationsShow")));
				} else {
					icon = new ImageIcon(getClass().getResource(icons.get("violationsHide")));
				}
				if (icon != null) {
					showViolationsOptionMenu.setIcon(icon);
				}
			}
		};
		showViolationsOptionMenu.addActionListener(action);
		showViolationsOptionMenu.addActionListener(listener);
		graphicsOptionsDialog.setToggleViolationsAction(action);
		graphicsOptionsDialog.setToggleViolationsAction(listener);
	}

	public void setToggleContextUpdatesAction(ActionListener listener) {
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
				// TODO notify the other slider in the options dialog
				listener.stateChanged(ce);
			}
		});
		graphicsOptionsDialog.setZoomChangeListener(listener);
	}

	public void setLocale(HashMap<String, String> menuBarLocale) {
		try {
			zoomInButton.setToolTipText(menuBarLocale.get("ZoomIn"));
			zoomOutButton.setToolTipText(menuBarLocale.get("ZoomOut"));
			refreshButton.setToolTipText(menuBarLocale.get("Refresh"));
			exportToImageButton.setToolTipText(menuBarLocale.get("ExportToImage"));
			optionsDialogButton.setToolTipText(menuBarLocale.get("Options"));
			
			showDependenciesOptionMenu.setToolTipText(menuBarLocale.get("showDependencies"));
			showViolationsOptionMenu.setToolTipText(menuBarLocale.get("showViolations"));
			
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

	public void setViolationToggle(boolean setting) {
		showViolationsOptionMenu.setSelected(setting);
		graphicsOptionsDialog.setViolationToggle(setting);
	}

	public void setContextUpdatesToggle(boolean setting) {
		graphicsOptionsDialog.setContextUpdatesToggle(setting);
	}

	public void setDependencyToggle(boolean setting) {
		showDependenciesOptionMenu.setSelected(setting);
		graphicsOptionsDialog.setDependencyToggle(setting);
	}

	public double getScaleFactor() {
		double scaleFactor = zoomSlider.getValue() / 100.0;
		scaleFactor = Math.max(MIN_SCALEFACTOR, scaleFactor);
		scaleFactor = Math.min(MAX_SCALEFACTOR, scaleFactor);

		return scaleFactor;
	}

}
