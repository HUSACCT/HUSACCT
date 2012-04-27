package husacct.control.task;

import husacct.ServiceProvider;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class ViewController {

	private ServiceProvider serviceProvider;
	private MainController mainController; 
	private JFrame defineFrame;
	private JInternalFrame violationsFrame;	
	private boolean hidden = true;

	private JInternalFrame testinternalframe;
	
	public ViewController(MainController maincontroller){
		this.mainController = maincontroller;
	}
	
	private void createTestFrame() {
		this.testinternalframe = new JInternalFrame("TestFrame");
		JLabel testlabel = new JLabel("Testing 1,2,3...");
		testinternalframe.add(testlabel);
		testinternalframe.setVisible(true);
	}
	
	public void setDefineGui(){
		serviceProvider = ServiceProvider.getInstance();
		this.defineFrame = serviceProvider.getDefineService().getDefinedGUI();
	}
	
	public void showDefineGui() {
		if (defineFrame != null){
			createTestFrame();
			
			mainController.getMainGui().getContentPane().add(testinternalframe);
			defineFrame.setVisible(true);
			//this.hidden = false;
		}		
	}

	public void hideDefineGui(){
		this.defineFrame.setVisible(false);
		this.hidden = true;
	}

	public void toggleDefineGui() {
		if(hidden) {
			this.defineFrame.setVisible(true);
			this.hidden = false;
		}
		else {
			hideDefineGui();
		}
	}
	
	public void setViolationsGui(){
		this.serviceProvider = ServiceProvider.getInstance();
		this.violationsFrame = serviceProvider.getValidateService().getBrowseViolationsGUI();
	}
	
	public void showViolationsGui() {
		if (violationsFrame != null){
			mainController.getMainGui().getContentPane().add(violationsFrame);
			//defineFrame.setVisible(true);
			//this.hidden = false;
		}		
	}

	public void hideViolationsGui(){
		this.defineFrame.setVisible(false);
		this.hidden = true;
	}

	public void toggleViolationsGui() {
		if(hidden) {
			this.defineFrame.setVisible(true);
			this.hidden = false;
		}
		else {
			hideDefineGui();
		}
	}
	
}
