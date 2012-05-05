package husacct.control.task;

import husacct.ServiceProvider;

import java.awt.Dimension;
import java.awt.Rectangle;

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
	
	private Dimension defaultDimension = new Dimension(800, 600);
	
	public ViewController(MainController maincontroller){
		this.mainController = maincontroller;
		serviceProvider = ServiceProvider.getInstance();
	}
	
	private void setupFrame(final JInternalFrame internalFrame, String title){
		internalFrame.setTitle(title);
		internalFrame.setMaximizable(true);
		internalFrame.setResizable(true);
		internalFrame.setIconifiable(true);
		internalFrame.setClosable(true);
		internalFrame.pack();
		internalFrame.addInternalFrameListener(new InternalFrameAdapter() {
			public void internalFrameClosing(InternalFrameEvent e) {
				internalFrame.dispose();
			}
		});
	}
	
	private void resetFrame(JInternalFrame internalFrame){
		if(internalFrame != null){
			if(internalFrame.isIcon()){
				internalFrame.getDesktopPane().getDesktopManager().deiconifyFrame(internalFrame);
			}
			mainController.getMainGui().getContentPane().remove(internalFrame);
			mainController.getMainGui().getContentPane().repaint();
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

	public void setDefineGui(){
		defineInternalFrame = serviceProvider.getDefineService().getDefinedGUI();
		setupFrame(defineInternalFrame, "Define Architecture");	
		defineInternalFrame.setSize(defaultDimension);
		mainController.getMainGui().getContentPane().add(defineInternalFrame);	
	}
	
	public void setViolationsGui(){
		violationsInternalFrame = serviceProvider.getValidateService().getBrowseViolationsGUI();
		setupFrame(violationsInternalFrame, "Violations");
		violationsInternalFrame.setSize(defaultDimension);
		mainController.getMainGui().getContentPane().add(violationsInternalFrame);
	}
	
	public void setConfigurationGui() {
		configurationInternalFrame = serviceProvider.getValidateService().getConfigurationGUI();
		setupFrame(configurationInternalFrame, "Configuration");
		configurationInternalFrame.setSize(defaultDimension);
		mainController.getMainGui().getContentPane().add(configurationInternalFrame);
	}
	
	public void setDefinedArchitectureGui(){
		definedArchitectureInternalFrame = serviceProvider.getGraphicsService().getDefinedArchitectureGUI();
		setupFrame(definedArchitectureInternalFrame, "Defined architecture");
		definedArchitectureInternalFrame.setSize(defaultDimension);
		mainController.getMainGui().getContentPane().add(definedArchitectureInternalFrame);
	}
	
	public void setAnalysedArchitectureGui(){
		analysedArchitectureInternalFrame = serviceProvider.getGraphicsService().getAnalysedArchitectureGUI();
		setupFrame(analysedArchitectureInternalFrame, "Analysed architecture");
		analysedArchitectureInternalFrame.setSize(defaultDimension);
		mainController.getMainGui().getContentPane().add(analysedArchitectureInternalFrame);
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
}
