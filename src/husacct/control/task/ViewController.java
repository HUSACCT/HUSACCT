package husacct.control.task;

import husacct.ServiceProvider;

import javax.swing.JInternalFrame;
import javax.swing.event.InternalFrameAdapter;
import javax.swing.event.InternalFrameEvent;

public class ViewController {

	private ServiceProvider serviceProvider;
	private MainController mainController;
	
	private JInternalFrame defineInternalFrame;
	private JInternalFrame violationsInternalFrame;
	private JInternalFrame analysedArchitectureInternalFrame;
	private JInternalFrame definedArchitectureInternalFrame;	

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
			@Override
			public void internalFrameClosing(InternalFrameEvent e) {
				internalFrame.setVisible(false);
			}
		});
		
	}

	public void setDefineGui(){
		defineInternalFrame = serviceProvider.getDefineService().getDefinedGUI();
		setupFrame(defineInternalFrame, "Define Architecture");	
		mainController.getMainGui().getContentPane().add(defineInternalFrame);	
	}
	
	public void setViolationsGui(){
		violationsInternalFrame = serviceProvider.getValidateService().getBrowseViolationsGUI();
		setupFrame(violationsInternalFrame, "Violations");
		mainController.getMainGui().getContentPane().add(violationsInternalFrame);
	}
	
	public void setDefinedArchitectureGui(){
		definedArchitectureInternalFrame = serviceProvider.getGraphicsService().getDefinedArchitectureGUI();
		setupFrame(definedArchitectureInternalFrame, "Defined architecture");
		mainController.getMainGui().getContentPane().add(definedArchitectureInternalFrame);
	}
	
	public void setAnalysedArchitectureGui(){
		analysedArchitectureInternalFrame = serviceProvider.getGraphicsService().getAnalysedArchitectureGUI();
		setupFrame(analysedArchitectureInternalFrame, "Analysed architecture");
		mainController.getMainGui().getContentPane().add(analysedArchitectureInternalFrame);
	}
	
	public void showDefineGui() {
		if(defineInternalFrame == null){
			setDefineGui();
		}	
		defineInternalFrame.setVisible(true);
	}
	
	public void showViolationsGui() {
		if (violationsInternalFrame == null){
			setViolationsGui();
		}
		violationsInternalFrame.setVisible(true);
	}	
	
	public void showDefinedArchitectureGui() {
		if (definedArchitectureInternalFrame == null){
			setDefinedArchitectureGui();
		}		
		definedArchitectureInternalFrame.setVisible(true);
	}	
		
	public void showAnalysedArchitectureGui() {
		if (analysedArchitectureInternalFrame == null){
			setAnalysedArchitectureGui();
		}		
		analysedArchitectureInternalFrame.setVisible(true);
	}	
}
