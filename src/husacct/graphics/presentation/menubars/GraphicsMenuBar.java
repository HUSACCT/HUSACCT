package husacct.graphics.presentation.menubars;

import husacct.graphics.presentation.dialogs.GraphicsOptionsDialog;
import husacct.graphics.presentation.figures.BaseFigure;
import husacct.graphics.util.DrawingLayoutStrategy;
import husacct.graphics.util.UserInputListener;

import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.apache.log4j.Logger;

public class GraphicsMenuBar extends JPanel implements UserInputListener {
	private static final long serialVersionUID = -7419378432318031359L;
	private static final double MIN_SCALEFACTOR = 0.25;
	private static final double MAX_SCALEFACTOR = 1.75;

	protected Logger logger = Logger.getLogger(GraphicsMenuBar.class);
	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();

	private HashMap<String, String> icons;
	private ArrayList<JComponent> actions;

	private JButton zoomInButton, zoomOutButton, refreshButton, exportToImageButton, optionsDialogButton, showDependenciesButton, showViolationsButton, outOfDateButton;

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
		icons.put("outofdate", "/husacct/common/resources/graphics/icon-outofdate.png");
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
		actions.add(outOfDateButton);
	}
	
	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}

	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
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
		zoomInButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moduleZoom();
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
				if(showDependenciesButton.getToolTipText().equals(menuBarLocale.get("HideDependencies"))){
					hideDependencies();
				}else{
					showDependencies();
				}
				refreshDrawing();
			}
		});
		add(showDependenciesButton);

		showViolationsButton = new JButton();
		showViolationsButton.setSize(40, menuItemMaxHeight);
		showViolationsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(showViolationsButton.getToolTipText().equals(menuBarLocale.get("HideViolations"))){
					hideViolations();
				}else{
					showViolations();
				}
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
				int scale = ((JSlider)ce.getSource()).getValue();
				graphicsOptionsDialog.setZoomValue(scale);
				drawingZoomChanged(scale);
			}
		});
		add(zoomSlider);
		
		outOfDateButton = new JButton();
		outOfDateButton.setSize(50, menuItemMaxHeight);
		setButtonIcon(outOfDateButton, "outofdate");
	}
	
	public void setOptionsDialogAction(ActionListener listener) {
		optionsDialogButton.addActionListener(listener);
	}

	public void setLayoutStrategyAction(ActionListener listener) {
		graphicsOptionsDialog.setLayoutStrategyAction(listener);
	}

	public void setOutOfDateAction(ActionListener listener) {
		outOfDateButton.addActionListener(listener);
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

	public void setLayoutStrategyItems(String[] layoutStrategyItems) {
		graphicsOptionsDialog.setLayoutStrategyItems(layoutStrategyItems);
	}

	public void setSelectedLayoutStrategyItem(String item) {
		graphicsOptionsDialog.setSelectedLayoutStrategyItem(item);
	}

	public String getSelectedLayoutStrategyItem() {
		return graphicsOptionsDialog.getSelectedLayoutStrategyItem();
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
		validate();
		updateUI();
	}

	public void turnOnBar(){
		for(JComponent comp : actions){
			comp.setEnabled(true);
		}
		graphicsOptionsDialog.turnOn();
		validate();
		updateUI();
	}
	
	public void setUpToDate() {
		remove(outOfDateButton);
		validate();
		updateUI();
	}

	public void setOutOfDate() {
		add(outOfDateButton);
		validate();
		updateUI();
	}

	@Override
	public void moduleZoom() {
		for(UserInputListener listener : listeners){
			listener.moduleZoom();
		}
	}

	@Override
	public void moduleOpen(String[] paths) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moduleZoom(BaseFigure[] zoomedModuleFigure) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void moduleZoomOut() {
		for(UserInputListener listener : listeners){
			listener.moduleZoomOut();
		}
	}

	@Override
	public void figureSelected(BaseFigure[] figures) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void figureDeselected(BaseFigure[] figures) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exportToImage() {
		for(UserInputListener listener : listeners){
			listener.exportToImage();
		}
	}

	@Override
	public void refreshDrawing() {
		for(UserInputListener listener : listeners){
			listener.refreshDrawing();
		}
	}

	@Override
	public void changeLayoutStrategy(DrawingLayoutStrategy selectedStrategyEnum) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showDependencies() {
		for(UserInputListener listener : listeners){
			listener.showDependencies();
		}
	}
	
	@Override
	public void hideDependencies() {
		for(UserInputListener listener : listeners){
			listener.hideDependencies();
		}
	}
	
	public void setDependeciesUIToActive(){
		setButtonIcon(showDependenciesButton, "dependenciesHide");
		showDependenciesButton.setToolTipText(menuBarLocale.get("HideDependencies"));
		graphicsOptionsDialog.setDependenciesUIToActive();
	}
	
	public void setDependeciesUIToInactive(){
		setButtonIcon(showDependenciesButton, "dependenciesShow");
		showDependenciesButton.setToolTipText(menuBarLocale.get("ShowDependencies"));
		graphicsOptionsDialog.setDependenciesUIToInactive();
	}

	@Override
	public void showViolations() {
		for (UserInputListener l : listeners) {
			l.showViolations();
		}
	}

	@Override
	public void hideViolations() {
		for (UserInputListener l : listeners) {
			l.hideViolations();
		}
	}
	
	public void setViolationsUIToActive(){
		setButtonIcon(showViolationsButton, "violationsHide");
		showViolationsButton.setToolTipText(menuBarLocale.get("HideViolations"));
		graphicsOptionsDialog.setViolationsUIToActive();
	}
	
	public void setViolationsUIToInactive(){
		setButtonIcon(showViolationsButton, "violationsShow");
		showViolationsButton.setToolTipText(menuBarLocale.get("ShowViolations"));
		graphicsOptionsDialog.setViolationsUIToInactive();
	}
	
	@Override
	public void showSmartLines() {
		for (UserInputListener l : listeners) {
			l.showSmartLines();
		}
	}

	@Override
	public void hideSmartLines() {
		for (UserInputListener l : listeners) {
			l.hideSmartLines();
		}
	}
	
	public void setSmartLinesUIToActive() {
		graphicsOptionsDialog.setSmartLinesUIToActive();
	}
	
	public void setSmartLinesUIToInactive() {
		graphicsOptionsDialog.setSmartLinesUIToInactive();
	}

	@Override
	public void drawingZoomChanged(double zoomFactor) {
		zoomSlider.setValue((int) zoomFactor);
		for(UserInputListener listener : listeners){
			listener.drawingZoomChanged(zoomFactor);
		}
	}

	@Override
	public void hideModules() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void restoreModules() {
		// TODO Auto-generated method stub
		
	}

}
