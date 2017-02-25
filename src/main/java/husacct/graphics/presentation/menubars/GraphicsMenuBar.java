package husacct.graphics.presentation.menubars;

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
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import husacct.common.enums.DependencyTypeOption;
import org.apache.log4j.Logger;

import husacct.common.Resource;
import husacct.common.help.presentation.HelpableJPanel;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.presentation.GraphicsPresentationController;
import husacct.graphics.presentation.UserInputListener;
import husacct.graphics.presentation.dialogs.GraphicsOptionsDialog;
import husacct.graphics.task.DrawingTypesEnum;
import husacct.graphics.task.modulelayout.ModuleLayoutsEnum;

public class GraphicsMenuBar extends HelpableJPanel implements UserInputListener {
	private static final long				serialVersionUID	= -7419378432318031359L;
	
	protected Logger						logger				= Logger.getLogger(GraphicsMenuBar.class);
	private ArrayList<UserInputListener>	listeners			= new ArrayList<>();
	
	private HashMap<String, String>			icons;
	private ArrayList<JComponent>			actions;
	
	private JButton							zoomInButton, zoomOutButton, refreshButton, exportToImageButton, optionsDialogButton,
											showDependenciesButton, showViolationsButton, outOfDateButton, panToolButton, selectToolButton;
	
	private JSlider							zoomSlider;
	private GraphicsOptionsDialog			graphicsOptionsDialog;
	
	private int								menuItemMaxHeight	= 45;
	private HashMap<String, String>			menuBarLocale;
	
	private final ContextMenuButton			zoomOptionsMenu;

	private boolean showDependencyOptions = false;
	
	public GraphicsMenuBar(DrawingTypesEnum drawingType) {
		showDependencyOptions = drawingType == DrawingTypesEnum.IMPLEMENTED_ARCHITECTURE;

		zoomOptionsMenu = new ContextMenuButton();
		
		icons = new HashMap<>();
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
		
		actions = new ArrayList<>();
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
	
	private void initializeComponents() {
		zoomInButton = new JButton();
		zoomInButton.setSize(50, menuItemMaxHeight);
		zoomInButton.addActionListener(e -> zoomIn());
		zoomInButton.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) 
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
		zoomOutButton.addActionListener(e -> zoomOut());
		add(zoomOutButton);
		setButtonIcon(zoomOutButton, "zoomOut");
		
		refreshButton = new JButton();
		refreshButton.setSize(50, menuItemMaxHeight);
		refreshButton.addActionListener(e -> refreshDrawing());
		add(refreshButton);
		setButtonIcon(refreshButton, "refresh");
		
		showDependenciesButton = new JButton();
		showDependenciesButton.setSize(40, menuItemMaxHeight);
		showDependenciesButton.addActionListener(e -> {
            if (showDependenciesButton.getToolTipText().equals(menuBarLocale.get("HideDependencies"))) {
                dependenciesHide();
                refreshDrawing();
            } else {
                dependenciesShow();
                refreshDrawing();
            }
        });
		add(showDependenciesButton);
		
		showViolationsButton = new JButton();
		showViolationsButton.setSize(40, menuItemMaxHeight);
		showViolationsButton.addActionListener(e -> {
            if (showViolationsButton.getToolTipText().equals(menuBarLocale.get("HideViolations"))) {
                violationsHide();
                refreshDrawing();
            } else {
                violationsShow();
                refreshDrawing();
            }
        });
		add(showViolationsButton);
		
		exportToImageButton = new JButton();
		exportToImageButton.setSize(50, menuItemMaxHeight);
		exportToImageButton.addActionListener(e -> exportImage());
		add(exportToImageButton);
		setButtonIcon(exportToImageButton, "save");
		
		selectToolButton = new JButton();
		selectToolButton.setSize(50, menuItemMaxHeight);
		selectToolButton.addActionListener(e -> {
            useSelectTool();
            selectToolButton.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
            panToolButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        });
		add(selectToolButton);
		selectToolButton.setBorder(BorderFactory
				.createLineBorder(Color.MAGENTA));
		setButtonIcon(selectToolButton, "selectTool");
		
		panToolButton = new JButton();
		panToolButton.setSize(50, menuItemMaxHeight);
		panToolButton.addActionListener(e -> {
            usePanTool();
            selectToolButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panToolButton.setBorder(BorderFactory.createLineBorder(Color.MAGENTA));
        });
		add(panToolButton);
		panToolButton.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		setButtonIcon(panToolButton, "panTool");
		
		graphicsOptionsDialog = new GraphicsOptionsDialog(showDependencyOptions);
		graphicsOptionsDialog.addListener(this);
		graphicsOptionsDialog.setIcons(icons);
		
		optionsDialogButton = new JButton();
		optionsDialogButton.setSize(40, menuItemMaxHeight);
		optionsDialogButton.addActionListener(e -> graphicsOptionsDialog.showDialog());
		add(optionsDialogButton);
		setButtonIcon(optionsDialogButton, "options");
		
		zoomSlider = new JSlider(25, 175, 100);
		zoomSlider.setSize(50, menuItemMaxHeight);
		zoomSlider.addChangeListener(ce -> {
            int scale = ((JSlider) ce.getSource()).getValue();
            graphicsOptionsDialog.setZoomValue(scale);
            zoomFactorChanged(scale);
        });
		add(zoomSlider);
		
		outOfDateButton = new JButton();
		outOfDateButton.setSize(50, menuItemMaxHeight);
		setButtonIcon(outOfDateButton, "outofdate");
	}
	
	public void addListener(UserInputListener listener) {
		listeners.add(listener); 				// PresentationController
		zoomOptionsMenu.addListener(listener);
	}
	
	@Override
	public void dependenciesHide() {
		for (UserInputListener listener : listeners)
			listener.dependenciesHide();
	}
	
	@Override
	public void dependenciesShow() {
		for (UserInputListener listener : listeners)
			listener.dependenciesShow();
	}
	
	@Override
	public void exportImage() {
		for (UserInputListener listener : listeners)
			listener.exportImage();
	}
	
	public double getScaleFactor() {
		double scaleFactor = zoomSlider.getValue() / 100.0;
		return scaleFactor;
	}
	
	public void hideOutOfDateWarning() {
		remove(outOfDateButton);
		validate();
		updateUI();
	}
	
	@Override
	public void layoutStrategyChange(ModuleLayoutsEnum selectedStrategyEnum) {
		for (UserInputListener listener : listeners)
			listener.layoutStrategyChange(selectedStrategyEnum);
	}
	
	@Override
	public void librariesHide() {
		for (UserInputListener l : listeners)
			l.librariesHide();
	}
	
	@Override
	public void librariesShow() {
		for (UserInputListener l : listeners)
			l.librariesShow();
	}
	
	@Override
	public void moduleHide() {
		for (UserInputListener listener : listeners)
			listener.moduleHide();
	}
	
	@Override
	public void moduleRestoreHiddenModules() {
		for (UserInputListener listener : listeners)
			listener.moduleRestoreHiddenModules();
	}
	
	@Override
	public void moduleOpen(String[] paths) {
		// Not used from this UI
	}
	
	@Override
	public void propertiesPaneHide(){
	}
	
	@Override
	public void propertiesPaneShowDependencies(BaseFigure selectedLine) {
	}
	
	@Override
	public void propertiesPaneShowUmlLinks(BaseFigure selectedLine){
	}
	
	@Override
	public void propertiesPaneShowViolations(BaseFigure selectedLine) {
	}
	
	@Override
	public void propertiesPaneShowRules(BaseFigure selectedFigure) {
	}
	
	@Override
	public void proportionalLinesDisable() {
		for (UserInputListener l : listeners)
			l.proportionalLinesDisable();
	}

	@Override
	public void proportionalLinesEnable() {
		for (UserInputListener l : listeners)
			l.proportionalLinesEnable();
	}

	@Override
	public void refreshDrawing() {
		for (UserInputListener listener : listeners)
			listener.refreshDrawing();
	}
	
	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
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
	
	public void setDependeciesButtonsToShow() {
		setButtonIcon(showDependenciesButton, "dependenciesHide");
		showDependenciesButton.setToolTipText(menuBarLocale.get("HideDependencies"));
		graphicsOptionsDialog.setDependeciesButtonsToShow();
	}
	
	public void setDependeciesButtonToDontShow() {
		setButtonIcon(showDependenciesButton, "dependenciesShow");
		showDependenciesButton.setToolTipText(menuBarLocale.get("ShowDependencies"));
		graphicsOptionsDialog.setDependeciesButtonToDontShow();
	}
	
	public void setLocale(HashMap<String, String> locale) {
		menuBarLocale = locale;
		try {
			zoomInButton.setToolTipText(menuBarLocale.get("ZoomIn"));
			zoomOutButton.setToolTipText(menuBarLocale.get("ZoomOut"));
			refreshButton.setToolTipText(menuBarLocale.get("Refresh"));
			exportToImageButton.setToolTipText(menuBarLocale.get("ExportToImage"));
			optionsDialogButton.setToolTipText(menuBarLocale.get("Options"));
			outOfDateButton.setToolTipText(menuBarLocale.get("DrawingOutOfDate"));
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
	
	public void showOutOfDateWarning() {
		add(outOfDateButton);
		validate();
		updateUI();
	}
	
	public void setOutOfDateAction(ActionListener listener) {
		outOfDateButton.addActionListener(listener);
	}
	
	public void setSelectedLayoutStrategyItem(ModuleLayoutsEnum item) {
		graphicsOptionsDialog.setSelectedLayoutStrategyItem(item);
	}
	
	public void setSmartLinesButtonsToShow() {
		graphicsOptionsDialog.setSmartLinesButtonsToShow();
	}
	
	public void setSmartLinesButtonsToDontShow() {
		graphicsOptionsDialog.setSmartLinesButtonsToDontShow();
	}
	
	public void setViolationsButtonsToShow() {
		setButtonIcon(showViolationsButton, "violationsHide");
		showViolationsButton.setToolTipText(menuBarLocale.get("HideViolations"));
		graphicsOptionsDialog.setViolationsButtonsToShow();
	}
	
	public void setViolationsButtonsToDontShow() {
		setButtonIcon(showViolationsButton, "violationsShow");
		showViolationsButton.setToolTipText(menuBarLocale.get("ShowViolations"));
		graphicsOptionsDialog.setViolationsButtonsToDontShow();
	}
	
	@Override
	public void smartLinesDisable() {
		for (UserInputListener l : listeners)
			l.smartLinesDisable();
	}
	
	@Override
	public void smartLinesEnable() {
		for (UserInputListener l : listeners)
			l.smartLinesEnable();
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

	@Override
	public void violationsHide() {
		for (UserInputListener l : listeners)
			l.violationsHide();
	}
	
	@Override
	public void violationsShow() {
		for (UserInputListener l : listeners)
			l.violationsShow();
	}
	
	@Override
	public void zoomFactorChanged(double zoomFactor) {
		zoomSlider.setValue((int) zoomFactor);
		for (UserInputListener listener : listeners)
			listener.zoomFactorChanged(zoomFactor);
	}
	
	@Override
	public void zoomIn() {
		for (UserInputListener listener : listeners)
			listener.zoomIn();
	}
	
	@Override
	public void zoomTypeChange(String zoomType) {
		for (UserInputListener listener : listeners)
			listener.zoomTypeChange(zoomType);
	}
	
	@Override
	public void zoomOut() {
		for (UserInputListener listener : listeners)
			listener.zoomOut();
	}
	
	@Override
	public void zoomSliderSetZoomFactor(double zoomFactor) {
		int value = (int) (zoomFactor * 100);
		zoomSlider.setValue(value);
	}

  public void dependencyTypeChange(DependencyTypeOption option) {
      for (UserInputListener listener : listeners)
          if (listener instanceof GraphicsPresentationController) {
              ((GraphicsPresentationController) listener).dependencyTypeChange(option);
          }
  }
}
