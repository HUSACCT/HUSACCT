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
	
	private ImageIcon frameIcon;
	private String stringIdentifier;
	private Logger logger = Logger.getLogger(InternalFrameController.class);
	
	public InternalFrameController(MainController mainController, ImageIcon frameIcon, String stringIdentifier){
		this.mainController = mainController;
		this.frameIcon = frameIcon;
		this.stringIdentifier = stringIdentifier;
	}
	
	public void setLocaleListener(){
		//System.out.println("setting localeListener for " + this.stringIdentifier);
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
		internalFrame.pack();
		internalFrame.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				internalFrame.dispose();
			}
		});
		registerInternalFrameToTaskBar(internalFrame);
	}
	
	private void resetFrame(){
		if ((mainController.getMainGui() != null) && (mainController.getMainGui().getDesktopPane() != null) && (internalFrame != null)) {
			internalFrame.dispose();
			mainController.getMainGui().getDesktopPane().remove(internalFrame);
			mainController.getMainGui().getDesktopPane().repaint();
			internalFrame = null;
		}
	}
	
	public void closeFrame(){
		if(internalFrame != null) {
			internalFrame.dispose();
			internalFrame = null;
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
			updateInternalFrame();
			if ((mainController.getMainGui() != null) && (mainController.getMainGui().getDesktopPane() != null) && (internalFrame != null)) {
				mainController.getMainGui().getDesktopPane().add(internalFrame);
				setupFrame();
				internalFrame.setBounds(InternalFrameController.lastStartPosition.x, InternalFrameController.lastStartPosition.y, InternalFrameController.defaultDimension.width, InternalFrameController.defaultDimension.height);
				internalFrame.setMaximum(true);
			}
		} catch (PropertyVetoException e) {
			logger.warn(" Exception: " + e.getMessage());
		}
	}

	abstract public JInternalFrame getInternalFrame();
	
	public static void resetLastStartPosition() {
		lastStartPosition = new Point(0, 0);
	}

	public void showView(){
			calculateNewStartPosition(internalFrame);
			resetFrame();
			updateView();
			internalFrame.setVisible(true);
			internalFrame.toFront();
	}

	private void calculateNewStartPosition(JInternalFrame internalFrame){
		if(internalFrame == null){
			int newX = InternalFrameController.lastStartPosition.x + InternalFrameController.positionIncrement.x;
			int newY = InternalFrameController.lastStartPosition.y + InternalFrameController.positionIncrement.y;
			InternalFrameController.lastStartPosition = new Point(newX, newY);
		}
	}
	
}
