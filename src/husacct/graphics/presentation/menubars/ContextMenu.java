package husacct.graphics.presentation.menubars;

import husacct.graphics.task.UserInputListener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ContextMenu extends JPopupMenu {
	private static final long serialVersionUID = -6033808567664371902L;

	private ArrayList<UserInputListener> listeners = new ArrayList<UserInputListener>();
	
//	private JMenuItem zoomIn;
	private JMenuItem zoomOut;
	private JMenuItem hide;
	private JMenuItem restore;
	
	public ContextMenu() {
		ImageIcon icon;
//		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/icon-zoom.png"));
//		zoomIn = new JMenuItem("Zoom in", icon);
//		add(zoomIn);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/icon-back.png"));
		zoomOut = new JMenuItem("Zoom out", icon);
		add(zoomOut);
		
		addSeparator();
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/icon-hide.png"));
		hide = new JMenuItem("Hide selected modules", icon);
		add(hide);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/icon-restore.png"));
		restore = new JMenuItem("Restore hidden modules", icon);
		add(restore);	
		
		hookupEventHandlers();
	}
	
	private void hookupEventHandlers() {
		zoomOut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				triggerZoomOut();
			}
		});
		
		hide.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				triggerHideModules();
			}
		});
		
		restore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				triggerRestoreModules();
			}
		});
		
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

	private void triggerZoomOut() {
		for (UserInputListener l : listeners) {
			l.moduleZoomOut();
		}
	}
	
	public void addListener(UserInputListener listener) {
		listeners.add(listener);
	}
	
	public void removeListener(UserInputListener listener) {
		listeners.remove(listener);
	}
}
