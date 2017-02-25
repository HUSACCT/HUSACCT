package husacct.control.presentation.viewcontrol;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class ToolBarButtonContextMenu extends JPopupMenu{
	private static final long serialVersionUID = 1L;
	
	private InternalFrameController internalFrameController;
	
	private JMenuItem maximize;
	private JMenuItem restore;
	private JMenuItem minimize;
	private JMenuItem close;
	
	public ToolBarButtonContextMenu(InternalFrameController internalFrameController){
		this.internalFrameController = internalFrameController;
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
		maximize.addActionListener(event -> internalFrameController.maximizeInternalFrame());
		
		restore.addActionListener(event -> internalFrameController.restoreInternalFrame());
		
		minimize.addActionListener(event -> internalFrameController.iconifyInternalFrame());
		
		close.addActionListener(event -> internalFrameController.setInternalFrameAndButtonInvisible());
	}
}
