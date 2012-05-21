package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.presentation.taskbar.TaskBar;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class ViewController {

	private ServiceProvider serviceProvider;
	private MainController mainController;
	
	private JInternalFrame defineInternalFrame;
	private JInternalFrame violationsInternalFrame;
	private JInternalFrame configurationInternalFrame;
	private JInternalFrame definedArchitectureInternalFrame;
	private JInternalFrame analysedArchitectureInternalFrame;
	private JInternalFrame applicationOverviewInternalFrame;
	
	public static Dimension defaultDimension = new Dimension(800, 600);
	private Point lastStartPosition = new Point(10, 10);
	
	public ViewController(MainController maincontroller){
		this.mainController = maincontroller;
		serviceProvider = ServiceProvider.getInstance();
	}
	
	public List<JInternalFrame> getOpenFrames(){
		List<JInternalFrame> frames = new ArrayList<JInternalFrame>();
		frames.add(defineInternalFrame);
		frames.add(violationsInternalFrame);
		frames.add(configurationInternalFrame);
		frames.add(definedArchitectureInternalFrame);
		frames.add(analysedArchitectureInternalFrame);
		frames.add(applicationOverviewInternalFrame);
		
		List<JInternalFrame> openedFrames = new ArrayList<JInternalFrame>();
		for(JInternalFrame frame : frames){
			if(frame != null) openedFrames.add(frame);
		}
		
		return openedFrames;
	}
	
	private void setupFrame(final JInternalFrame internalFrame, String title){
		internalFrame.setTitle(title);
		internalFrame.setMaximizable(true);
		internalFrame.setResizable(true);
		internalFrame.setIconifiable(false);
		internalFrame.setFrameIcon(null);
		internalFrame.setClosable(true);
		internalFrame.pack();
		internalFrame.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				internalFrame.dispose();
			}
		});
		registerInternalFrameToTaskBar(internalFrame);
	}
	
	private void resetFrame(JInternalFrame internalFrame){
		if(internalFrame != null){
			internalFrame.dispose();
			mainController.getMainGui().getDesktopPane().remove(internalFrame);
			mainController.getMainGui().getDesktopPane().repaint();
			internalFrame = null;
		}
	}
	
	private Rectangle getBounds(JInternalFrame internalFrame){
		Rectangle rect = null;
		if(internalFrame != null){
			rect = internalFrame.getBounds();
		}
		return rect;
	}
	
	private void updateStartPosition(){
		lastStartPosition = new Point((int)lastStartPosition.getX()+25, (int)lastStartPosition.getY()+25);
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
	
	private void registerInternalFrameToTaskBar(JInternalFrame internalFrame){
		TaskBar taskBar = mainController.getMainGui().getTaskBar();
		taskBar.registerInternalFrame(internalFrame);
	}
	
	public void setDefineGui(){
		defineInternalFrame = serviceProvider.getDefineService().getDefinedGUI();
		mainController.getMainGui().getDesktopPane().add(defineInternalFrame);
		setupFrame(defineInternalFrame, serviceProvider.getControlService().getTranslatedString("DefineArchitecture"));	
		defineInternalFrame.setSize(defaultDimension);
	}
	
	public void setViolationsGui(){
		violationsInternalFrame = serviceProvider.getValidateService().getBrowseViolationsGUI();
		mainController.getMainGui().getDesktopPane().add(violationsInternalFrame);
		setupFrame(violationsInternalFrame, serviceProvider.getControlService().getTranslatedString("Violations"));
		violationsInternalFrame.setSize(defaultDimension);
	}
	
	public void setConfigurationGui() {
		configurationInternalFrame = serviceProvider.getValidateService().getConfigurationGUI();
		mainController.getMainGui().getDesktopPane().add(configurationInternalFrame);
		setupFrame(configurationInternalFrame, serviceProvider.getControlService().getTranslatedString("Configuration"));
		configurationInternalFrame.setSize(defaultDimension);
	}
	
	public void setDefinedArchitectureGui(){
		serviceProvider.getGraphicsService().drawDefinedArchitecture();
		definedArchitectureInternalFrame = serviceProvider.getGraphicsService().getDefinedArchitectureGUI();
		mainController.getMainGui().getDesktopPane().add(definedArchitectureInternalFrame);
		setupFrame(definedArchitectureInternalFrame, serviceProvider.getControlService().getTranslatedString("DefineArchitecture"));
		definedArchitectureInternalFrame.setSize(defaultDimension);
	}
	
	public void setAnalysedArchitectureGui(){
		serviceProvider.getGraphicsService().drawAnalysedArchitecture();
		analysedArchitectureInternalFrame = serviceProvider.getGraphicsService().getAnalysedArchitectureGUI();
		mainController.getMainGui().getDesktopPane().add(analysedArchitectureInternalFrame);
		setupFrame(analysedArchitectureInternalFrame, serviceProvider.getControlService().getTranslatedString("AnalysedArchitecture"));
		analysedArchitectureInternalFrame.setSize(defaultDimension);
	}
	
	public void setApplicationOverviewGui(){
		applicationOverviewInternalFrame = serviceProvider.getAnalyseService().getJInternalFrame();
		mainController.getMainGui().getDesktopPane().add(applicationOverviewInternalFrame);
		setupFrame(applicationOverviewInternalFrame, serviceProvider.getControlService().getTranslatedString("AnalysedApplicationOverview"));
		applicationOverviewInternalFrame.setSize(defaultDimension);
	}
	
	public void showDefineGui() {
		Point newPosition = getNewPosition(defineInternalFrame);
		Rectangle rect = getBounds(defineInternalFrame);
		resetFrame(defineInternalFrame);
		setDefineGui();
		setBounds(defineInternalFrame, rect);
		setNewPosition(defineInternalFrame, newPosition);
		defineInternalFrame.setVisible(true);
		defineInternalFrame.toFront();
	}
	
	public void showViolationsGui() {
		Point newPosition = getNewPosition(violationsInternalFrame);
		Rectangle rect = getBounds(violationsInternalFrame);
		resetFrame(violationsInternalFrame);
		setViolationsGui();
		setBounds(violationsInternalFrame, rect);
		setNewPosition(violationsInternalFrame, newPosition);
		violationsInternalFrame.setVisible(true);
		violationsInternalFrame.toFront();
	}
	
	public void showConfigurationGui() {
		Point newPosition = getNewPosition(configurationInternalFrame);
		Rectangle rect = getBounds(configurationInternalFrame);
		resetFrame(configurationInternalFrame);
		setConfigurationGui();
		setBounds(configurationInternalFrame, rect);
		setNewPosition(configurationInternalFrame, newPosition);
		configurationInternalFrame.setVisible(true);
		configurationInternalFrame.toFront();
	}	
	
	public void showDefinedArchitectureGui() {
		Point newPosition = getNewPosition(definedArchitectureInternalFrame);
		Rectangle rect = getBounds(definedArchitectureInternalFrame);
		resetFrame(definedArchitectureInternalFrame);
		setDefinedArchitectureGui();
		setBounds(definedArchitectureInternalFrame, rect);
		setNewPosition(definedArchitectureInternalFrame, newPosition);
		definedArchitectureInternalFrame.setVisible(true);
		definedArchitectureInternalFrame.toFront();
	}	
		
	public void showAnalysedArchitectureGui() {	
		Point newPosition = getNewPosition(analysedArchitectureInternalFrame);
		Rectangle rect = getBounds(analysedArchitectureInternalFrame);
		resetFrame(analysedArchitectureInternalFrame);
		setAnalysedArchitectureGui();
		setBounds(analysedArchitectureInternalFrame, rect);
		setNewPosition(analysedArchitectureInternalFrame, newPosition);
		analysedArchitectureInternalFrame.setVisible(true);
		analysedArchitectureInternalFrame.toFront();
	}
	
	public void showApplicationOverviewGui() {
		Point newPosition = getNewPosition(applicationOverviewInternalFrame);
		Rectangle rect = getBounds(applicationOverviewInternalFrame);
		resetFrame(applicationOverviewInternalFrame);
		setApplicationOverviewGui();
		setBounds(applicationOverviewInternalFrame, rect);
		setNewPosition(applicationOverviewInternalFrame, newPosition);
		applicationOverviewInternalFrame.setVisible(true);
		applicationOverviewInternalFrame.toFront();
	}
	
	public void closeAll(){		
		for(JInternalFrame frame : getOpenFrames()){
			frame.dispose();
			frame = null;
		}
	}
}
