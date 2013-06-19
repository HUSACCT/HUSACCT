package husacct.graphics.presentation.menubars;

import husacct.common.Resource;
import husacct.graphics.presentation.dialogs.GraphicsOptionsDialog;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.util.DrawingLayoutStrategy;
import husacct.graphics.util.UserInputListener;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

public class GraphicsMenuBar extends JPanel implements UserInputListener {
	private static final long serialVersionUID = -7419378432318031359L;
	
	protected Logger logger = Logger.getLogger(GraphicsMenuBar.class);
	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();
	
	private HashMap<String, String> icons;
	private ArrayList<JComponent> actions;
	
	private JButton zoomInButton, zoomOutButton, refreshButton,
	exportToImageButton, optionsDialogButton, showDependenciesButton,
	showViolationsButton, outOfDateButton, panToolButton,
	selectToolButton;
	
	private JSlider zoomSlider;
	private GraphicsOptionsDialog graphicsOptionsDialog;
	
	private int menuItemMaxHeight = 45;
	private HashMap<String, String> menuBarLocale;
	
	private final ContextMenuButton zoomOptionsMenu;
	
	public GraphicsMenuBar() {
		zoomOptionsMenu = new ContextMenuButton();
		
		icons = new HashMap<String, String>();
		icons.put("options", Resource.ICON_OPTIONS);
		icons.put("zoomIn", Resource.ICON_ZOOM);
		icons.put("zoomInContext", Resource.ICON_ZOOMCONTEXT);
		icons.put("zoomOut", Resource.ICON_BACK);
		icons.put("refresh", Resource.ICON_REFRESH);
		icons.put("save", Resource.ICON_SAVE);
		icons.put("dependenciesShow", Resource.ICON_DEPENDENCIES_ACTIVE);
		icons.put("dependenciesHide", Resource.ICON_DEPENDENCIES_INACTIVE);
		icons.put("violationsShow", Resource.ICON_VIOLATIONS_ACTIVE);
		icons.put("violationsHide", Resource.ICON_VIOLATIONS_INACTIVE);
		icons.put("outofdate", Resource.ICON_OUTOFDATE);
		icons.put("showFigures", Resource.ICON_FIGURES_SHOW);
		icons.put("hideFigures", Resource.ICON_FIGURES_HIDE);
		icons.put("panTool", Resource.ICON_PAN_TOOL);
		icons.put("selectTool", Resource.ICON_SELECT_TOOL);
		initializeComponents();
		setLayout(new FlowLayout(FlowLayout.LEFT));
		
		actions = new ArrayList<JComponent>();
		actions.add(zoomInButton);
		actions.add(zoomOutButton);
		actions.add(refreshButton);
		actions.add(exportToImageButton);
		actions.add(panToolButton);
		actions.add(selectToolButton);
		actions.add(optionsDialogButton);
		actions.add(showDependenciesButton);
		actions.add(showViolationsButton);
		actions.add(zoomSlider);
		actions.add(outOfDateButton);
	}
	
	public void addListener(UserInputListener listener) {
		listeners.add(listener);
		zoomOptionsMenu.addListener(listener);
	}
	
	@Override
	public void changeLayoutStrategy(DrawingLayoutStrategy selectedStrategyEnum) {
		for (UserInputListener listener : listeners)
			listener.changeLayoutStrategy(selectedStrategyEnum);
	}
	
	@Override
	public void drawingZoomChanged(double zoomFactor) {
		zoomSlider.setValue((int) zoomFactor);
		for (UserInputListener listener : listeners)
			listener.drawingZoomChanged(zoomFactor);
	}
	
	@Override
	public void exportToImage() {
		for (UserInputListener listener : listeners)
			listener.exportToImage();
	}
	
	@Override
	public void figureDeselected(BaseFigure[] figures) {
	}
	
	@Override
	public void figureSelected(BaseFigure[] figures) {
	}
	
	public double getScaleFactor() {
		double scaleFactor = zoomSlider.getValue() / 100.0;
		return scaleFactor;
	}
	
	@Override
	public void hideDependencies() {
		for (UserInputListener listener : listeners)
			listener.hideDependencies();
	}
	
	@Override
	public void hideModules() {
		for (UserInputListener listener : listeners)
			listener.hideModules();
	}
	
	@Override
	public void hideSmartLines() {
		for (UserInputListener l : listeners)
			l.hideSmartLines();
	}
	
	@Override
	public void hideViolations() {
		for (UserInputListener l : listeners)
			l.hideViolations();
	}
	
	private void initializeComponents() {
		zoomInButton = new JButton();
		zoomInButton.setSize(50, menuItemMaxHeight);
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (zoomOptionsMenu.canZoomModule()) moduleZoom("zoom");
				else if (zoomOptionsMenu.canZoomModuleContext()) moduleZoom();
			}
		});
		zoomInButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e)
						&& e.getClickCount() == 1)
					zoomOptionsMenu.show(zoomInButton, e.getX(), e.getY());
			}
			
			@Override
			public void mouseEntered(MouseEvent e) {
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
			}
			
			@Override
			public void mousePressed(MouseEvent e) {
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
			}
		});
		add(zoomInButton);
		setButtonIcon(zoomInButton, "zoomIn");
		
		zoomOutButton = new JButton();
		zoomOutButton.setSize(50, menuItemMaxHeight);
		zoomOutButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleZoomOut();
			}
		});
		add(zoomOutButton);
		setButtonIcon(zoomOutButton, "zoomOut");
		
		refreshButton = new JButton();
		refreshButton.setSize(50, menuItemMaxHeight);
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshDrawing();
			}
		});
		add(refreshButton);
		setButtonIcon(refreshButton, "refresh");
		
		showDependenciesButton = new JButton();
		showDependenciesButton.setSize(40, menuItemMaxHeight);
		showDependenciesButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (showDependenciesButton.getToolTipText().equals(
						menuBarLocale.get("HideDependencies")))
					hideDependencies();
				else
					showDependencies();
				refreshDrawing();
			}
		});
		add(showDependenciesButton);
		
		showViolationsButton = new JButton();
		showViolationsButton.setSize(40, menuItemMaxHeight);
		showViolationsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (showViolationsButton.getToolTipText().equals(
						menuBarLocale.get("HideViolations")))
					hideViolations();
				else
					showViolations();
				refreshDrawing();
			}
		});
		add(showViolationsButton);
		
		exportToImageButton = new JButton();
		exportToImageButton.setSize(50, menuItemMaxHeight);
		exportToImageButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				exportToImage();
			}
		});
		add(exportToImageButton);
		setButtonIcon(exportToImageButton, "save");
		
		selectToolButton = new JButton();
		selectToolButton.setSize(50, menuItemMaxHeight);
		selectToolButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				useSelectTool();
				selectToolButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
				panToolButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
			}
		});
		add(selectToolButton);
		selectToolButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
		setButtonIcon(selectToolButton, "selectTool");
		
		panToolButton = new JButton();
		panToolButton.setSize(50, menuItemMaxHeight);
		panToolButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				usePanTool();
				selectToolButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
				panToolButton.setBorder(BorderFactory.createLineBorder(Color.BLUE));
			}
		});
		add(panToolButton);
		panToolButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		setButtonIcon(panToolButton, "panTool");
		
		graphicsOptionsDialog = new GraphicsOptionsDialog();
		graphicsOptionsDialog.addListener(this);
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
		zoomSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent ce) {
				int scale = ((JSlider) ce.getSource()).getValue();
				graphicsOptionsDialog.setZoomValue(scale);
				drawingZoomChanged(scale);
			}
		});
		add(zoomSlider);
		
		outOfDateButton = new JButton();
		outOfDateButton.setSize(50, menuItemMaxHeight);
		setButtonIcon(outOfDateButton, "outofdate");
	}
	
	@Override
	public void moduleOpen(String[] paths) {
		// Not used from this UI
	}
	
	@Override
	public void moduleZoom() {
		for (UserInputListener listener : listeners)
			listener.moduleZoom();
	}
	
	
	@Override
	public void moduleZoom(BaseFigure[] zoomedModuleFigure) {
		// Not used from this UI
	}
	
	@Override
	public void moduleZoom(String zoomType) {
		for (UserInputListener listener : listeners)
			listener.moduleZoom(zoomType);
	}
	
	@Override
	public void moduleZoomOut() {
		for (UserInputListener listener : listeners)
			listener.moduleZoomOut();
	}
	
	@Override
	public void refreshDrawing() {
		for (UserInputListener listener : listeners)
			listener.refreshDrawing();
	}
	
	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
	}
	
	@Override
	public void restoreModules() {
		for (UserInputListener listener : listeners)
			listener.restoreModules();
	}
	
	private void setButtonIcon(JButton button, String iconKey) {
		try {
			ImageIcon icon = new ImageIcon(Resource.get(icons.get(iconKey)));
			button.setIcon(icon);
			button.setMargin(new Insets(1, 5, 1, 5));
		} catch (Exception e) {
			logger.warn("Could not find icon for \"" + iconKey + "\".");
		}
	}
	
	public void setDependeciesUIToActive() {
		setButtonIcon(showDependenciesButton, "dependenciesHide");
		showDependenciesButton.setToolTipText(menuBarLocale
				.get("HideDependencies"));
		graphicsOptionsDialog.setDependenciesUIToActive();
	}
	
	public void setDependeciesUIToInactive() {
		setButtonIcon(showDependenciesButton, "dependenciesShow");
		showDependenciesButton.setToolTipText(menuBarLocale
				.get("ShowDependencies"));
		graphicsOptionsDialog.setDependenciesUIToInactive();
	}
	
	public void setLocale(HashMap<String, String> locale) {
		menuBarLocale = locale;
		try {
			zoomInButton.setToolTipText(menuBarLocale.get("ZoomIn"));
			zoomOutButton.setToolTipText(menuBarLocale.get("ZoomOut"));
			refreshButton.setToolTipText(menuBarLocale.get("Refresh"));
			exportToImageButton.setToolTipText(menuBarLocale
					.get("ExportToImage"));
			optionsDialogButton.setToolTipText(menuBarLocale.get("Options"));
			outOfDateButton.setToolTipText(menuBarLocale
					.get("DrawingOutOfDate"));
			outOfDateButton.setText(menuBarLocale.get("DrawingOutOfDate"));
			
			optionsDialogButton.setText(menuBarLocale.get("Options"));
			graphicsOptionsDialog.setLocale(menuBarLocale);
			graphicsOptionsDialog.setIcons(icons);
		} catch (NullPointerException e) {
			logger.warn("Locale for GraphicsMenuBar is not set properly.");
		}
	}
	
	public void setOptionsDialogAction(ActionListener listener) {
		optionsDialogButton.addActionListener(listener);
	}
	
	public void setOutOfDate() {
		add(outOfDateButton);
		validate();
		updateUI();
	}
	
	public void setOutOfDateAction(ActionListener listener) {
		outOfDateButton.addActionListener(listener);
	}
	
	public void setSelectedLayoutStrategyItem(DrawingLayoutStrategy item) {
		graphicsOptionsDialog.setSelectedLayoutStrategyItem(item);
	}
	
	public void setSmartLinesUIToActive() {
		graphicsOptionsDialog.setSmartLinesUIToActive();
	}
	
	public void setSmartLinesUIToInactive() {
		graphicsOptionsDialog.setSmartLinesUIToInactive();
	}
	
	public void setUpToDate() {
		remove(outOfDateButton);
		validate();
		updateUI();
	}
	
	public void setViolationsUIToActive() {
		setButtonIcon(showViolationsButton, "violationsHide");
		showViolationsButton
		.setToolTipText(menuBarLocale.get("HideViolations"));
		graphicsOptionsDialog.setViolationsUIToActive();
	}
	
	public void setViolationsUIToInactive() {
		setButtonIcon(showViolationsButton, "violationsShow");
		showViolationsButton
		.setToolTipText(menuBarLocale.get("ShowViolations"));
		graphicsOptionsDialog.setViolationsUIToInactive();
	}
	
	@Override
	public void setZoomSlider(double zoomFactor) {
		int value = (int) (zoomFactor * 100);
		zoomSlider.setValue(value);
	}
	
	@Override
	public void showDependencies() {
		for (UserInputListener listener : listeners)
			listener.showDependencies();
	}
	
	@Override
	public void showSmartLines() {
		for (UserInputListener l : listeners)
			l.showSmartLines();
	}
	
	@Override
	public void showViolations() {
		for (UserInputListener l : listeners)
			l.showViolations();
	}
	public void turnOffBar() {
		for (JComponent comp : actions)
			comp.setEnabled(false);
		graphicsOptionsDialog.turnOff();
		validate();
		updateUI();
	}
	
	public void turnOnBar() {
		for (JComponent comp : actions)
			comp.setEnabled(true);
		graphicsOptionsDialog.turnOn();
		validate();
		updateUI();
	}
	
	@Override
	public void usePanTool() {
		for (UserInputListener l : listeners)
			l.usePanTool();
	}
	
	@Override
	public void useSelectTool() {
		for (UserInputListener l : listeners)
			l.useSelectTool();
	}
}
