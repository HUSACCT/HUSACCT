package husacct.graphics.presentation.menubars;

import husacct.ServiceProvider;
import husacct.common.Resource;
import husacct.common.locale.ILocaleService;
import husacct.graphics.util.UserInputListener;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ContextMenuButton extends JPopupMenu {
	private static final long serialVersionUID = -6033808567664371902L;
	protected ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();

	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();
	
	private JMenuItem zoomModuleContext;
	private JMenuItem zoomModule;
	
	
	public ContextMenuButton() {
		
		ImageIcon icon;

		icon = new ImageIcon(Resource.get(Resource.ICON_ZOOMCONTEXT));
		zoomModuleContext = new JMenuItem(localeService.getTranslatedString("ZoomContext"), icon);
		add(zoomModuleContext);
		
		icon = new ImageIcon(Resource.get(Resource.ICON_ZOOM));
		zoomModule = new JMenuItem(localeService.getTranslatedString("ZoomIn"), icon);
		zoomModule.setEnabled(false);
		
		add(zoomModule);

		hookupEventHandlers();
	}
	
	private void hookupEventHandlers() {
		zoomModuleContext.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				triggerZoomInContext();
				zoomModuleContext.setEnabled(false);
				zoomModule.setEnabled(true);
			}
		});
		
		zoomModule.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				zoomModuleContext.setEnabled(true);
				zoomModule.setEnabled(false);
			}
		});
	}
	
	protected void triggerZoomInContext() {
		for (UserInputListener l : listeners) {
			l.moduleZoom();
		}
	}

	protected void triggerRestoreModules() {
		for (UserInputListener l : listeners) {
			l.restoreModules();
		}
	}

	protected void triggerHideModules() {
		for (UserInputListener l : listeners) {
			l.hideModules();
		}	
	}

	
	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
	}
	
	public boolean canZoomModule(){
		return zoomModule.isEnabled();
	}
	public boolean canZoomModuleContext(){
		return zoomModuleContext.isEnabled();
	}
	
	@Override
	public void show(Component invoker, int x, int y) {
		super.show(invoker, x, y);
	}
}
