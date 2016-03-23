package husacct.control.presentation.viewcontrol;

import husacct.ServiceProvider;
import husacct.common.services.IServiceListener;
import husacct.control.task.MainController;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;

import javax.swing.DesktopManager;
import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.JToggleButton;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.log4j.Logger;

abstract public class InternalFrameController {
	
	private MainController mainController;
	private JInternalFrame internalFrame;
	private ImageIcon frameIcon;
	private String stringIdentifier;
	private TaskBar taskBar;
	private JToggleButton toggleButton;

	public static final Dimension defaultDimension = new Dimension(950, 600);
	private static Point lastStartPosition = new Point(0, 0);
	private static Point positionIncrement = new Point(20, 20);
	private Point startPosition = new Point(0, 0);
	private boolean isMaximixed = true;
	
	// Listeners
	private InternalFrameAdapter internalFrameAdapter;
	private MouseAdapter internalFrameMouseAdapter;
	private ToolBarButtonListener toggleButtonContextClickListener;
	private ActionListener toggleButtonActionListener;

	private Logger logger = Logger.getLogger(InternalFrameController.class);
	
	public InternalFrameController(MainController mainController, ImageIcon frameIcon, String stringIdentifier){
		this.mainController = mainController;
		this.frameIcon = frameIcon;
		this.stringIdentifier = stringIdentifier;
	}
	
	public void setLocaleListener(){
		ServiceProvider.getInstance().getLocaleService().addServiceListener(new IServiceListener() {
			@Override
			public void update() {
				if(internalFrame != null){
					internalFrame.setTitle(getTitle());
					toggleButton.setText(internalFrame.getTitle());
				}
			}
		});
	}
	
	public void showView(){
		JInternalFrame  newInternalFrame = getInternalFrame();
		if (newInternalFrame != null) {
			if ((newInternalFrame == internalFrame)) {
				if (internalFrame.isClosed() == false) {
					setInternalFrameAndButtonVisible();
				} else {
					addInternalFrame();
				}
			} else {
				calculateNewStartPosition();
				addInternalFrame();
			}
		}
	}

	private void addInternalFrame(){
		try {
			closeInternalFrame(); // To clean-up
			internalFrame = getInternalFrame();
			if (internalFrame != null) {
				if ((mainController.getMainGui() != null) && (mainController.getMainGui().getDesktopPane() != null) && (mainController.getMainGui().getTaskBar() != null)) {
					internalFrame.setVisible(true);
					mainController.getMainGui().getDesktopPane().add(internalFrame);
					taskBar = mainController.getMainGui().getTaskBar();
					setupFrame();
					createToggleButton();
					internalFrame.setBounds(startPosition.x, startPosition.y, InternalFrameController.defaultDimension.width, InternalFrameController.defaultDimension.height);
					internalFrame.setMaximum(isMaximixed);
					internalFrame.setVisible(true);
					activateInternalFrame();
					mainController.getMainGui().revalidate();
					mainController.getMainGui().getDesktopPane().repaint();
				}
			}
		} catch (Exception e) {
			logger.error(" Exception: " + e.getMessage());
		}
	}

	private void setupFrame(){
		internalFrame.setTitle(getTitle());
		internalFrame.setMaximizable(true);
		internalFrame.setResizable(true);
		internalFrame.setIconifiable(true);
		internalFrame.setFrameIcon(frameIcon);
		internalFrame.setClosable(true);
		internalFrame.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
		addListenersToInternalFrame();
	}
	
	public void setInternalFrameAndButtonInvisible(){
		if (internalFrame != null) {
			deactivateInternalFrame();
			internalFrame.setVisible(false);
			taskBar.removeToggleButton(toggleButton);
			if ((mainController.getMainGui() != null) && (mainController.getMainGui().getDesktopPane() != null)) {
				mainController.getMainGui().getDesktopPane().validate();
				mainController.getMainGui().repaint(); // In case a scrollbar is added to the taskbar, or removed. 
			}
		}
	}

	private void setInternalFrameAndButtonVisible(){
		if (internalFrame != null) {
			internalFrame.setVisible(true);
			taskBar.addToggleButton(toggleButton);
			activateInternalFrame();
			if ((mainController.getMainGui() != null) && (mainController.getMainGui().getDesktopPane() != null)) {
				mainController.getMainGui().getDesktopPane().validate();
				mainController.getMainGui().repaint(); // In case a scrollbar is added to the taskbar, or removed. 
			}
		}
	}

	
	private void closeInternalFrame(){
		if (internalFrame != null) {
			isMaximixed = internalFrame.isMaximum(); // Store current setting; used while internal frame is added again
			internalFrame.dispose();
			removeListenersFromInternalFrame();
			if ((mainController.getMainGui() != null) && (mainController.getMainGui().getDesktopPane() != null)) {
				mainController.getMainGui().getDesktopPane().remove(internalFrame);
				mainController.getMainGui().getDesktopPane().validate();
			}
			removeToggleButton();
			internalFrame = null;
		}
	}
	
	public void closeFrame(){
		if(internalFrame != null) {
			closeInternalFrame();
		}
	}

	// Abstract method, overridden by ViewController with a call to the specific method.
	abstract public JInternalFrame getInternalFrame();
	
	private String getTitle(){
		return ServiceProvider.getInstance().getLocaleService().getTranslatedString(stringIdentifier);
	}
	
	public static void resetLastStartPosition() {
		lastStartPosition = new Point(0, 0);
	}

	private void calculateNewStartPosition(){
		if(internalFrame == null){ // Only do it the first time for a specific internal frame. 
			int newX = InternalFrameController.lastStartPosition.x + InternalFrameController.positionIncrement.x;
			int newY = InternalFrameController.lastStartPosition.y + InternalFrameController.positionIncrement.y;
			InternalFrameController.lastStartPosition = new Point(newX, newY);
			startPosition = new Point(newX, newY);
		}
	}

	private void addListenersToInternalFrame() {
		internalFrameAdapter = new InternalFrameAdapter() {
			@Override
			public void internalFrameActivated(InternalFrameEvent e) {
				activateInternalFrame();
			}

			@Override
			public void internalFrameDeactivated(InternalFrameEvent e) {
				deactivateInternalFrame();

			}
			public void internalFrameClosing(InternalFrameEvent e) {
				setInternalFrameAndButtonInvisible();
			}

			public void internalFrameClosed(InternalFrameEvent e) {
			}

			@Override
			public void internalFrameOpened(InternalFrameEvent e) {
				activateInternalFrame();
			}
			
			@Override
			public void internalFrameIconified(InternalFrameEvent e){
				iconifyInternalFrame();
			}
			
			@Override
			public void internalFrameDeiconified(InternalFrameEvent e){
				deIconifyInternalFrame();
			}
			
		};
		internalFrame.addInternalFrameListener(internalFrameAdapter);

		internalFrameMouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				activateInternalFrame();
			}
		};
		internalFrame.addMouseListener(internalFrameMouseAdapter);
	}
	
	private void removeListenersFromInternalFrame() {
		internalFrame.removeInternalFrameListener(internalFrameAdapter);
		internalFrame.removeMouseListener(internalFrameMouseAdapter);
	}
	
	private void activateInternalFrame(){
		try {
			if ((internalFrame != null) && (internalFrame.getDesktopPane() != null)) {
				if ( internalFrame.isIcon() == true) {
					internalFrame.setIcon(false);
				}
				internalFrame.toFront();
				internalFrame.setSelected(true);
				DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
				manager.activateFrame(internalFrame);
			}
		} catch (Exception event) {
			logger.error(event.getMessage());
			event.printStackTrace();
		}
		toggleButton.setSelected(true);
	}
	
	private void deactivateInternalFrame(){
		try {
			internalFrame.setSelected(false);
			DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
			manager.deactivateFrame(internalFrame);
		} catch (Exception event) {
			logger.error(event.getMessage());
		}
		toggleButton.setSelected(false);
		
	}
	
	public void iconifyInternalFrame(){
		try {
			internalFrame.setIcon(true);
			DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
			manager.iconifyFrame(internalFrame);
			internalFrame.setVisible(false);
		} catch (Exception event) {
			logger.error(event.getMessage());
		}
		deactivateInternalFrame();
	}
	
	private void deIconifyInternalFrame(){
		try {
			internalFrame.setIcon(false);
			DesktopManager manager = internalFrame.getDesktopPane().getDesktopManager();
			manager.deiconifyFrame(internalFrame);
			internalFrame.setVisible(true);		
		} catch (Exception event) {
			logger.error(event.getMessage());
		}
		activateInternalFrame();
	}	

	public void maximizeInternalFrame() {
		try {
			internalFrame.setMaximum(true);
			activateInternalFrame();
		} catch (PropertyVetoException e) {
			logger.error(e.getMessage());
		}
	}
	
	public void restoreInternalFrame() {
		try {
			internalFrame.setMaximum(false);
			activateInternalFrame();
		} catch (PropertyVetoException e) {
			logger.error(e.getMessage());
		}
	}

	/* 
	 * Adding a button to the taskbar may cause the appearance of scrollbars, which causes a change in size of the taskbar.  
	 */
	private void createToggleButton() {
		toggleButton = new JToggleButton(internalFrame.getTitle());
		toggleButton.setIcon(internalFrame.getFrameIcon());
		taskBar.addToggleButton(toggleButton);
		// Add listeners to toggleButton
		toggleButtonContextClickListener = new ToolBarButtonListener(this);
		toggleButton.addMouseListener(toggleButtonContextClickListener);
		toggleButtonActionListener = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				activateInternalFrame();
			}
		};
		toggleButton.addActionListener(toggleButtonActionListener);
	}

	/* 
	 *   Removing the button from the taskbar may cause the disappearance of scrollbars, which causes a change in size of the taskbar.
	 */
	private void removeToggleButton() {
		taskBar.removeToggleButton(toggleButton);
		toggleButton.removeMouseListener(toggleButtonContextClickListener);
		toggleButton.removeActionListener(toggleButtonActionListener);
	}

}
