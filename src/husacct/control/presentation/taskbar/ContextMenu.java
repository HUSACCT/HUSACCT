package husacct.control.presentation.taskbar;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.control.presentation.util.InternalFrameUtils;
import husacct.control.task.InternalFrameController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyVetoException;

import javax.swing.DesktopManager;
import javax.swing.JInternalFrame;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.apache.log4j.Logger;

public class ContextMenu extends JPopupMenu{
	private static final long serialVersionUID = 1L;
	
	private Logger logger = Logger.getLogger(ContextMenu.class);
	
	private JInternalFrame internalFrame;
	
	private JMenuItem maximize;
	private JMenuItem restore;
	private JMenuItem minimize;
	private JMenuItem close;
	
	public ContextMenu(JInternalFrame internalFrame){
		this.internalFrame = internalFrame;
		addComponents();
		setListeners();
	}
	
	private void addComponents() {
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		
		maximize = new JMenuItem(localeService.getTranslatedString("Maximize"));
		restore = new JMenuItem(localeService.getTranslatedString("Restore"));
		minimize = new JMenuItem(localeService.getTranslatedString("Minimize"));
		close = new JMenuItem(localeService.getTranslatedString("Close"));
		
		add(maximize);
		add(restore);
		add(minimize);
		add(close);
	}
	
	private void setListeners() {
		maximize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					internalFrame.setMaximum(true);
				} catch (PropertyVetoException e) {
					logger.debug(e.getMessage());
				}
				activateFrame(internalFrame);
			}
		});
		
		restore.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					internalFrame.setMaximum(false);
				} catch (PropertyVetoException e) {
					logger.debug(e.getMessage());
				}
				internalFrame.setSize(InternalFrameController.defaultDimension);
				InternalFrameUtils.alignCenter(internalFrame);
				activateFrame(internalFrame);
			}
		});
		
		minimize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				try {
					internalFrame.setIcon(true);
					internalFrame.setVisible(false);
					deactivateFrame(internalFrame);
				} catch (PropertyVetoException e) {
					logger.debug(e.getMessage());
				}
				activateFrame(internalFrame);
			}
		});
		
		close.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				internalFrame.dispose();
			}
		});
	}
	
	private void activateFrame(JInternalFrame internalFrame){
		DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
		manager.activateFrame(internalFrame);
		try {
			internalFrame.setSelected(true);
		} catch (PropertyVetoException event) {
			logger.debug(event.getMessage());
		}
	}
	
	private void deactivateFrame(JInternalFrame internalFrame){
		DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
		manager.deactivateFrame(internalFrame);
		try {
			internalFrame.setSelected(false);
		} catch (PropertyVetoException event) {
			logger.debug(event.getMessage());
		}		
	}
}
