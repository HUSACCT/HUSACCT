package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.services.IServiceListener;
import husacct.control.presentation.taskbar.TaskBar;

import java.awt.Dimension;
import java.awt.Point;
import java.beans.PropertyVetoException;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

import org.apache.log4j.Logger;

abstract public class InternalFrameController {
	
	private JInternalFrame internalFrame;
	private MainController mainController;
	
	public static final Dimension defaultDimension = new Dimension(950, 600);
	private static Point lastStartPosition = new Point(0, 0);
	private static Point positionIncrement = new Point(20, 20);
	private boolean isMaximixed = true;
	
	private Point startPosition = new Point(0, 0);
	private ImageIcon frameIcon;
	private String stringIdentifier;
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
				}
			}
		});
	}
	
	private void updateInternalFrame(){
		internalFrame = getInternalFrame();
	}
	
	private void setupFrame(){
		internalFrame.setTitle(getTitle());
		internalFrame.setMaximizable(true);
		internalFrame.setResizable(true);
		internalFrame.setIconifiable(true);
		internalFrame.setFrameIcon(frameIcon);
		internalFrame.setClosable(true);
		internalFrame.addInternalFrameListener(new InternalFrameAdapter() { // In ToolBar, event internalFrameClosed is processed.
			public void internalFrameClosing(InternalFrameEvent e) {
				internalFrame.dispose();
			}
		});
	}
	
	private void resetFrame(){
		if ((mainController.getMainGui() != null) && (mainController.getMainGui().getDesktopPane() != null) && (internalFrame != null)) {
			isMaximixed = internalFrame.isMaximum();
			internalFrame.dispose();
			mainController.getMainGui().getDesktopPane().remove(internalFrame);
			internalFrame = null;
		}
	}
	
	public void closeFrame(){
		if(internalFrame != null) {
			resetFrame();
		}
	}
	private String getTitle(){
		return ServiceProvider.getInstance().getLocaleService().getTranslatedString(stringIdentifier);
	}
	
	private void registerInternalFrameToTaskBar(JInternalFrame internalFrame){
		TaskBar taskBar = mainController.getMainGui().getTaskBar();
		taskBar.registerInternalFrame(internalFrame);
	}
	
	private void updateView(){
		try {
			if ((mainController.getMainGui() != null) && (mainController.getMainGui().getDesktopPane() != null) && (internalFrame != null)) {
				mainController.getMainGui().getDesktopPane().add(internalFrame);
				setupFrame();
				registerInternalFrameToTaskBar(internalFrame);
				internalFrame.setBounds(startPosition.x, startPosition.y, InternalFrameController.defaultDimension.width, InternalFrameController.defaultDimension.height);
				internalFrame.setMaximum(isMaximixed);
				internalFrame.setVisible(true);
				internalFrame.toFront();
				mainController.getMainGui().revalidate();
				mainController.getMainGui().getDesktopPane().repaint();
			}
		} catch (Exception e) {
			logger.error(" Exception: " + e.getMessage());
		}
	}

	// Abstract method, overridden by ViewController with a call to the specific method.
	abstract public JInternalFrame getInternalFrame();
	
	public static void resetLastStartPosition() {
		lastStartPosition = new Point(0, 0);
	}

	public void showView(){
		JInternalFrame  newInternalFrame = getInternalFrame();
		if (newInternalFrame != null) {
			if ((newInternalFrame == internalFrame) && !internalFrame.isClosed()) {
				if (internalFrame.isIcon()) {
					try {
						internalFrame.setIcon(false);
					} catch (PropertyVetoException e) {
						logger.error(" Exception: " + e.getMessage());
					}
				}
				internalFrame.setVisible(true);
				internalFrame.toFront();
				try {
					internalFrame.setSelected(true);
				} catch (PropertyVetoException e) {
					logger.error(" Exception: " + e.getMessage());
				}
				mainController.getMainGui().getDesktopPane().getDesktopManager().activateFrame(internalFrame);
				mainController.getMainGui().revalidate();
				mainController.getMainGui().getDesktopPane().repaint();
			} else {
				calculateNewStartPosition(internalFrame);
				resetFrame();
				updateInternalFrame();
				updateView();
			}
		}
	}

	private void calculateNewStartPosition(JInternalFrame internalFrame){
		if(internalFrame == null){ // Only do it the first time for a specific internal frame. 
			int newX = InternalFrameController.lastStartPosition.x + InternalFrameController.positionIncrement.x;
			int newY = InternalFrameController.lastStartPosition.y + InternalFrameController.positionIncrement.y;
			InternalFrameController.lastStartPosition = new Point(newX, newY);
			startPosition = new Point(newX, newY);
		}
	}
	
}
