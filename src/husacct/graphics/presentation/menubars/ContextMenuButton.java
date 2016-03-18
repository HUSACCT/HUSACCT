package husacct.graphics.presentation.menubars;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.graphics.presentation.UserInputListener;

import java.awt.Component;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

public class ContextMenuButton extends JPopupMenu {
	private static final long				serialVersionUID	= -6033808567664371902L;
	protected ILocaleService				localeService		= ServiceProvider
																		.getInstance()
																		.getLocaleService();
	protected Logger						logger				= Logger.getLogger(GraphicsMenuBar.class);
	
	private ArrayList<UserInputListener>	listeners			= new ArrayList<UserInputListener>();
	
	private HashMap<String, String>			icons;
	
	private JMenuItem						zoomModuleContext;
	private JMenuItem						zoomModule;
	private JButton							parentZoomButton;
	
	public ContextMenuButton() {
		
		ImageIcon icon;
		
		icons = new HashMap<String, String>();
		icons.put("zoomIn", Resource.ICON_ZOOM);
		icons.put("zoomInContext", Resource.ICON_ZOOMCONTEXT);
		
		icon = new ImageIcon(Resource.get(Resource.ICON_ZOOMCONTEXT));
		zoomModuleContext = new JMenuItem(localeService.getTranslatedString("ZoomContext"), icon);
		add(zoomModuleContext);
		
		icon = new ImageIcon(Resource.get(Resource.ICON_ZOOM));
		zoomModule = new JMenuItem(localeService.getTranslatedString("ZoomIn"), icon);
		zoomModule.setEnabled(true);
		
		add(zoomModule);
		
		hookupEventHandlers();
	}
	
	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}
	
	public boolean canZoomModule() {
		return zoomModuleContext.isEnabled();
	}
	
	public boolean canZoomModuleContext() {
		return zoomModule.isEnabled();
	}
	
	private void hookupEventHandlers() {
		zoomModuleContext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setButtonIcon(parentZoomButton, "zoomInContext");
				zoomModuleContext.setEnabled(false);
				zoomModule.setEnabled(true);
				for (UserInputListener listener : listeners)
					listener.zoomTypeChange("context");
			}
		});
		
		zoomModule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setButtonIcon(parentZoomButton, "zoomIn");
				zoomModuleContext.setEnabled(true);
				zoomModule.setEnabled(false);
				for (UserInputListener listener : listeners)
					listener.zoomTypeChange("zoom");
			}
		});
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
	
	@Override
	public void show(Component invoker, int x, int y) {
		//Commented 2015-10-26: Option to select "Zoom with context". Activate after implementation of this functionality.
		super.show(invoker, x, y);
		parentZoomButton = (JButton) invoker;
	}
	
	protected void triggerHideModules() {
		for (UserInputListener l : listeners)
			l.moduleHide();
	}
	
	protected void triggerRestoreModules() {
		for (UserInputListener l : listeners)
			l.moduleRestoreHiddenModules();
	}
	
	protected void triggerZoomInContext() {
		for (UserInputListener l : listeners)
			l.zoomIn();
	}
}
