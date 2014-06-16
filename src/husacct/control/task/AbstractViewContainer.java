package husacct.control.task;

import husacct.ServiceProvider;
import husacct.common.services.IServiceListener;
import husacct.control.presentation.taskbar.TaskBar;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

abstract public class AbstractViewContainer {
	
	private JInternalFrame internalFrame;
	private MainController mainController;
	
	public static final Dimension defaultDimension = new Dimension(950, 600);
	private static Point lastStartPosition = new Point(10, 10);
	private static Point positionIncrement = new Point(25, 25);
	
	private ImageIcon frameIcon;
	private String stringIdentifier;
	
	public AbstractViewContainer(MainController mainController, ImageIcon frameIcon, String stringIdentifier){
		this.mainController = mainController;
		this.frameIcon = frameIcon;
		this.stringIdentifier = stringIdentifier;
	}
	
	public void setLocaleListener(){
		//System.out.println("setting localelistener for " + this.stringIdentifier);
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
		if(internalFrame != null){
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
		updateInternalFrame();
		mainController.getMainGui().getDesktopPane().add(internalFrame);
		setupFrame();	
		internalFrame.setSize(AbstractViewContainer.defaultDimension);
	}

	private Rectangle getBounds(JInternalFrame internalFrame){
		Rectangle rect = null;
		if(internalFrame != null){
			rect = internalFrame.getBounds();
		}
		return rect;
	}

	private void updateStartPosition(){
		int newX = AbstractViewContainer.lastStartPosition.x + AbstractViewContainer.positionIncrement.x;
		int newY = AbstractViewContainer.lastStartPosition.y + AbstractViewContainer.positionIncrement.x;
		AbstractViewContainer.lastStartPosition = new Point(newX, newY);
	}
	
	private Point getNewPosition(JInternalFrame internalFrame){
		Point newPosition = null;
		if(internalFrame == null){
			newPosition = new Point(lastStartPosition);
			updateStartPosition();
		}
		return newPosition;
	}
	
	private void setNewPosition(JInternalFrame internalFrame, Point newPosition){
		if(newPosition != null) {
			internalFrame.setLocation(newPosition);
		}
	}
	
	private void setBounds(JInternalFrame internalFrame, Rectangle rect){
		if(rect != null) internalFrame.setBounds(rect);
	}	

	public void showView(){
		Point newPosition = getNewPosition(internalFrame);
		Rectangle rect = getBounds(internalFrame);
		resetFrame();
		updateView();
		setBounds(internalFrame, rect);
		setNewPosition(internalFrame, newPosition);
		internalFrame.setVisible(true);
		internalFrame.toFront();
	}
	
	abstract public JInternalFrame getInternalFrame();
}
