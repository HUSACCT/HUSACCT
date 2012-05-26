package husacct.graphics.presentation.menubars;

import husacct.graphics.presentation.dialogs.GraphicsOptionsDialog;

import java.awt.FlowLayout;
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

	private JButton zoomInButton, zoomOutButton, refreshButton, exportToImageButton, optionsDialogButton;
	private JCheckBox showDependenciesOptionMenu, showViolationsOptionMenu;
	
	private JSlider zoomSlider;
	private GraphicsOptionsDialog graphicsOptionsDialog;

	private int menuItemMaxHeight = 45;

	public GraphicsMenuBar() {
		initializeComponents();
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}

	private void initializeComponents() {
		ImageIcon icon = new ImageIcon(getClass().getResource("/husacct/common/resources/icon-zoom.png"));
		zoomInButton = new JButton();
		zoomInButton.setIcon(icon);
		zoomInButton.setSize(50, menuItemMaxHeight);
		add(zoomInButton);		
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/icon-back.png"));
		zoomOutButton = new JButton();
		zoomOutButton.setIcon(icon);
		zoomOutButton.setSize(50, menuItemMaxHeight);
		add(zoomOutButton);

		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/icon-refresh.png"));
		refreshButton = new JButton();
		refreshButton.setSize(50, menuItemMaxHeight);
		refreshButton.setIcon(icon);
		add(refreshButton);
		
		icon  = new ImageIcon(getClass().getResource("/husacct/common/resources/icon-save.png"));
		exportToImageButton = new JButton();
		exportToImageButton.setIcon(icon);
		exportToImageButton.setSize(50, menuItemMaxHeight);
		add(exportToImageButton);

		showDependenciesOptionMenu = new JCheckBox();
		showDependenciesOptionMenu.setSize(40, menuItemMaxHeight);
		add(showDependenciesOptionMenu);

		showViolationsOptionMenu = new JCheckBox();
		showViolationsOptionMenu.setSize(40, menuItemMaxHeight);
		add(showViolationsOptionMenu);

		graphicsOptionsDialog = new GraphicsOptionsDialog();
		optionsDialogButton = new JButton();
		optionsDialogButton.setSize(40, menuItemMaxHeight);
		optionsDialogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				graphicsOptionsDialog.showDialog();
			}
		});
		add(optionsDialogButton);

		zoomSlider = new JSlider(25, 175, 100);
		zoomSlider.setSize(50, menuItemMaxHeight);
		add(zoomSlider);
	}

	public void setZoomInAction(ActionListener listener) {
		zoomInButton.addActionListener(listener);
	}
	
	public void setZoomOutAction(ActionListener listener) {
		zoomOutButton.addActionListener(listener);
		graphicsOptionsDialog.setLevelUpAction(listener);
	}

	public void setRefreshAction(ActionListener listener) {
		refreshButton.addActionListener(listener);
		graphicsOptionsDialog.setRefreshAction(listener);
	}

	
	public void setToggleDependenciesAction(ActionListener listener) {
		showDependenciesOptionMenu.addActionListener(listener);
		graphicsOptionsDialog.setToggleDependenciesAction(listener);
	}

	public void setToggleViolationsAction(ActionListener listener) {
		showViolationsOptionMenu.addActionListener(listener);
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
		zoomSlider.addChangeListener(new ChangeListener(){
			@Override
			public void stateChanged(ChangeEvent ce) {
				//TODO notify the other slider in the options dialog
		    	listener.stateChanged(ce);
			}
		});
		graphicsOptionsDialog.setZoomChangeListener(listener);
	}

	public void setLocale(HashMap<String, String> menuBarLocale) {
		try {
			showDependenciesOptionMenu.setText(menuBarLocale.get("ShowDependencies"));
			showViolationsOptionMenu.setText(menuBarLocale.get("ShowViolations"));
			graphicsOptionsDialog.setLocale(menuBarLocale);
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
