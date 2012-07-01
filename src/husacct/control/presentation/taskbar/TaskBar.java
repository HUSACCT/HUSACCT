package husacct.control.presentation.taskbar;

import husacct.ServiceProvider;
import husacct.control.IControlService;
import husacct.control.ILocaleChangeListener;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.util.Locale;

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
		
		internalFrame.addInternalFrameListener(new InternalFrameAdapter() {
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
		});
		
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
		
		IControlService controlService = ServiceProvider.getInstance().getControlService();
		controlService.addLocaleChangeListener(new ILocaleChangeListener() {
			@Override
			public void update(Locale newLocale) {
				toggleButton.setText(internalFrame.getTitle());
			}
		});
		
		activateFrame(internalFrame, toggleButton);
		add(toggleButton);
	}
	
	private void activateFrame(JInternalFrame internalFrame, JToggleButton button){
		DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
		manager.activateFrame(internalFrame);
		try {
			internalFrame.setSelected(true);
		} catch (PropertyVetoException event) {
			logger.debug(event.getMessage());
		}
		button.setSelected(true);
	}
	
	private void deactivateFrame(JInternalFrame internalFrame, JToggleButton button){
		DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
		manager.deactivateFrame(internalFrame);
		try {
			internalFrame.setSelected(false);
		} catch (PropertyVetoException event) {
			logger.debug(event.getMessage());
		}
		button.setSelected(false);
	}
}
