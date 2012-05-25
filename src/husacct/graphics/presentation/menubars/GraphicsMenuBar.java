package husacct.graphics.presentation.menubars;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

public class GraphicsMenuBar extends JPanel {
	private static final long serialVersionUID = -7419378432318031359L;
	private static final double MIN_SCALEFACTOR = 0.25;
	private static final double MAX_SCALEFACTOR = 1.75;
			
	protected Logger logger = Logger.getLogger(GraphicsMenuBar.class);
	
	private JButton goToParentMenu, refreshMenu, exportToImageMenu;
	private JCheckBox showDependenciesOptionMenu, showViolationsOptionMenu, contextUpdatesOptionMenu;
	private JComboBox<String> layoutStrategyOptions;
	private JSlider zoomSlider;

	private int menuItemMaxHeight = 45;

	public GraphicsMenuBar() {
		initializeComponents();
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	private void initializeComponents() {
		goToParentMenu = new JButton();
		goToParentMenu.setSize(50, menuItemMaxHeight);
		add(goToParentMenu);

		refreshMenu = new JButton();
		refreshMenu.setSize(50, menuItemMaxHeight);
		add(refreshMenu);

		showDependenciesOptionMenu = new JCheckBox();
		showDependenciesOptionMenu.setSize(40, menuItemMaxHeight);
		add(showDependenciesOptionMenu);

		showViolationsOptionMenu = new JCheckBox();
		showViolationsOptionMenu.setSize(40, menuItemMaxHeight);
		add(showViolationsOptionMenu);

		contextUpdatesOptionMenu = new JCheckBox();
		contextUpdatesOptionMenu.setSize(40, menuItemMaxHeight);
		add(contextUpdatesOptionMenu);

		layoutStrategyOptions = new JComboBox<String>();
		add(layoutStrategyOptions);

		exportToImageMenu = new JButton();
		exportToImageMenu.setSize(50, menuItemMaxHeight);
		add(exportToImageMenu);
		
		zoomSlider = new JSlider(25, 175, 100);
		zoomSlider.setSize(50, menuItemMaxHeight);
		add(zoomSlider);
	}

	public void setLevelUpAction(ActionListener listener) {
		goToParentMenu.addActionListener(listener);
	}

	public void setRefreshAction(ActionListener listener) {
		refreshMenu.addActionListener(listener);
	}

	public void setToggleDependenciesAction(ActionListener listener) {
		showDependenciesOptionMenu.addActionListener(listener);
	}

	public void setToggleViolationsAction(ActionListener listener) {
		showViolationsOptionMenu.addActionListener(listener);
	}

	public void setToggleContextUpdatesAction(ActionListener listener) {
		contextUpdatesOptionMenu.addActionListener(listener);
	}

	public void setExportToImageAction(ActionListener listener) {
		exportToImageMenu.addActionListener(listener);
	}

	public void setLayoutStrategyAction(ActionListener listener) {
		layoutStrategyOptions.addActionListener(listener);
	}
	
	public void setZoomChangeListener(ChangeListener listener) {
		zoomSlider.addChangeListener(listener);
	}

	public void setLocale(HashMap<String, String> menuBarLocale) {
		try {
			goToParentMenu.setText(menuBarLocale.get("LevelUp"));
			refreshMenu.setText(menuBarLocale.get("Refresh"));
			showDependenciesOptionMenu.setText(menuBarLocale.get("ShowDependencies"));
			showViolationsOptionMenu.setText(menuBarLocale.get("ShowViolations"));
			contextUpdatesOptionMenu.setText(menuBarLocale.get("LineContextUpdates"));
			exportToImageMenu.setText(menuBarLocale.get("ExportToImage"));
		} catch (NullPointerException e) {
			logger.warn("Locale for GraphicsMenuBar is not set properly.");
		}
	}

	public void setLayoutStrategyItems(ArrayList<String> items) {
		int selectedItem = layoutStrategyOptions.getSelectedIndex();
		if (selectedItem < 0) {
			selectedItem = 0;
		}
		layoutStrategyOptions.removeAllItems();
		for (String item : items) {
			layoutStrategyOptions.addItem(item);
		}
		layoutStrategyOptions.setSelectedIndex(selectedItem);
	}

	public void setSelectedLayoutStrategyItem(int selectedItem) {
		layoutStrategyOptions.setSelectedItem(selectedItem);
	}

	public String getSelectedLayoutStrategyItem() {
		return (String) layoutStrategyOptions.getSelectedItem();
	}

	public void setViolationToggle(boolean setting) {
		showViolationsOptionMenu.setSelected(setting);
	}

	public void setContextUpdatesToggle(boolean setting) {
		contextUpdatesOptionMenu.setSelected(setting);
	}

	public void setContextDependencyToggle(boolean setting) {
		showDependenciesOptionMenu.setSelected(setting);
	}

	public double getScaleFactor() {
		double scaleFactor = zoomSlider.getValue() / 100.0;
		scaleFactor = Math.max(MIN_SCALEFACTOR, scaleFactor);
		scaleFactor = Math.min(MAX_SCALEFACTOR, scaleFactor);
		
		return scaleFactor;
	}

}
