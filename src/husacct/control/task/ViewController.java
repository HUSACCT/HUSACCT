package husacct.control.task;

import husacct.ServiceProvider;
import husacct.control.presentation.taskbar.TaskBar;

import java.awt.Dimension;
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
		Rectangle rect = getBounds(defineInternalFrame);
		resetFrame(defineInternalFrame);
		setDefineGui();
		setBounds(defineInternalFrame, rect);
		defineInternalFrame.setVisible(true);
		defineInternalFrame.toFront();
	}
	
	public void showViolationsGui() {		
		Rectangle rect = getBounds(violationsInternalFrame);
		resetFrame(violationsInternalFrame);
		setViolationsGui();
		setBounds(violationsInternalFrame, rect);
		violationsInternalFrame.setVisible(true);
		violationsInternalFrame.toFront();
	}
	
	public void showConfigurationGui() {		
		Rectangle rect = getBounds(configurationInternalFrame);
		resetFrame(configurationInternalFrame);
		setConfigurationGui();
		setBounds(configurationInternalFrame, rect);
		configurationInternalFrame.setVisible(true);
		configurationInternalFrame.toFront();
	}	
	
	public void showDefinedArchitectureGui() {
		Rectangle rect = getBounds(definedArchitectureInternalFrame);
		resetFrame(definedArchitectureInternalFrame);
		setDefinedArchitectureGui();
		setBounds(definedArchitectureInternalFrame, rect);
		definedArchitectureInternalFrame.setVisible(true);
		definedArchitectureInternalFrame.toFront();
	}	
		
	public void showAnalysedArchitectureGui() {		
		Rectangle rect = getBounds(analysedArchitectureInternalFrame);
		resetFrame(analysedArchitectureInternalFrame);
		setAnalysedArchitectureGui();
		setBounds(analysedArchitectureInternalFrame, rect);
		analysedArchitectureInternalFrame.setVisible(true);
		analysedArchitectureInternalFrame.toFront();
	}
	
	public void showApplicationOverviewGui() {		
		Rectangle rect = getBounds(applicationOverviewInternalFrame);
		resetFrame(applicationOverviewInternalFrame);
		setApplicationOverviewGui();
		setBounds(applicationOverviewInternalFrame, rect);
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
