package husacct.control.presentation.taskbar;

import husacct.ServiceProvider;
import husacct.common.locale.ILocaleService;
import husacct.common.services.IServiceListener;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.DesktopManager;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.event.InternalFrameEvent;

import org.apache.log4j.Logger;

public class TaskBar extends JPanel{
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(TaskBar.class);
	
	public TaskBar(){
		setup();
	}
	
	private void setup(){
		setLayout(new FlowLayout(FlowLayout.LEFT));
	}
	
	public void registerInternalFrame(final JInternalFrame internalFrame){
		final JToggleButton toggleButton = new JToggleButton(internalFrame.getTitle());
		
		toggleButton.setIcon(internalFrame.getFrameIcon());
		
		InternalFrameAdapter internalFrameAdapter = new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				activateFrame(internalFrame, toggleButton);
			}
			@Override
			public void internalFrameDeactivated(InternalFrameEvent e) {
				deactivateFrame(internalFrame, toggleButton);
			}
			@Override
			public void internalFrameClosed(InternalFrameEvent e) {
				remove(toggleButton);
				validate();
				repaint();
			}
			@Override
			public void internalFrameOpened(InternalFrameEvent e) {
				activateFrame(internalFrame, toggleButton);
			}
			
			@Override
			public void internalFrameIconified(InternalFrameEvent e){
				iconifyFrame(internalFrame, toggleButton);
			}
			
			@Override
			public void internalFrameDeiconified(InternalFrameEvent e){
				deiconifyFrame(internalFrame, toggleButton);
			}
			
		};
		internalFrame.addInternalFrameListener(internalFrameAdapter);
		internalFrame.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				activateFrame(internalFrame, toggleButton);
			}
		});
		
		toggleButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				activateFrame(internalFrame, toggleButton);
			}
		});
		
		toggleButton.addMouseListener(new ContextClickListener(internalFrame));
		
		ILocaleService localeService = ServiceProvider.getInstance().getLocaleService();
		localeService.addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				toggleButton.setText(internalFrame.getTitle());
			}
		});
		
		add(toggleButton);
		activateFrame(internalFrame, toggleButton);
	}
	
	private void activateFrame(JInternalFrame internalFrame, JToggleButton button){
		try {
			internalFrame.setIcon(false);
			internalFrame.setSelected(true);
			DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
			manager.activateFrame(internalFrame);
		} catch (Exception event) { // 
			logger.error(event.getMessage());
			event.printStackTrace();
		}
		button.setSelected(true);
	}
	
	private void deactivateFrame(JInternalFrame internalFrame, JToggleButton button){
		try {
			internalFrame.setSelected(false);
			DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
			manager.deactivateFrame(internalFrame);
		} catch (Exception event) {
			logger.error(event.getMessage());
		}
		button.setSelected(false);
		
	}
	
	private void iconifyFrame(JInternalFrame internalFrame, JToggleButton button){
		try {
			internalFrame.setIcon(true);
			DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
			manager.iconifyFrame(internalFrame);
			internalFrame.setVisible(false);
		} catch (Exception event) {
			logger.error(event.getMessage());
		}
		deactivateFrame(internalFrame, button);
	}
	
	private void deiconifyFrame(JInternalFrame internalFrame, JToggleButton button){
		try {
			internalFrame.setIcon(false);
			DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
			manager.deiconifyFrame(internalFrame);
			internalFrame.setVisible(true);		
		} catch (Exception event) {
			logger.error(event.getMessage());
		}
		activateFrame(internalFrame, button);
	}	
}
