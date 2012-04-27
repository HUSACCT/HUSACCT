package husacct.control.task;

import husacct.ServiceProvider;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class ViewController {

	private ServiceProvider serviceProvider;
	private MainController mainController; 
	private JInternalFrame defineFrame;
	private JInternalFrame violationsFrame;
	private JInternalFrame analysedArchitectureFrame;
	private JInternalFrame definedArchitectureFrame;
	private boolean hidden = true;

	private JInternalFrame testinternalframe;
	
	public ViewController(MainController maincontroller){
		this.mainController = maincontroller;
	}
	
	private void createTestFrame() {
		this.testinternalframe = new JInternalFrame("TestFrame");
		JLabel testlabel = new JLabel("Testing 1,2,3...");
		testinternalframe.setPreferredSize(new Dimension(700, 500));
		testinternalframe.setLocation(0, 0);
		testinternalframe.add(testlabel);
		testinternalframe.setVisible(true);
	}
	
//	private GridBagConstraints setFrameInMainGUI(int gridx, int gridy) {
//		GridBagConstraints constraint = new GridBagConstraints();
//		
//		constraint.fill = GridBagConstraints.BOTH;
//		constraint.gridx = gridx;
//		constraint.gridy = gridy;
//		return constraint;
//	}
	
	public void setDefineGui(){
		serviceProvider = ServiceProvider.getInstance();
		this.defineFrame = serviceProvider.getDefineService().getDefinedGUI();
		
	}
	
	public void showDefineGui() {
		if (defineFrame != null){
			GridBagConstraints constraint = new GridBagConstraints();
			constraint.fill = GridBagConstraints.BOTH;
			constraint.gridx = 0;
			constraint.gridy = 0;
			constraint.weightx = 0.5;
			constraint.anchor = GridBagConstraints.NORTHWEST;
			mainController.getMainGui().getContentPane().add(defineFrame, constraint);
			defineFrame.setVisible(true);
			this.hidden = false;
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
			GridBagConstraints constraint = new GridBagConstraints();
			constraint.fill = GridBagConstraints.BOTH;
			constraint.gridx = 0;
			constraint.gridy = 1;
			constraint.weightx = 0.0;
			constraint.gridwidth = 2;
			mainController.getMainGui().getContentPane().add(violationsFrame, constraint);
			violationsFrame.setVisible(true);
			violationsFrame.setLocation(0, 0);
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
	
	public void setDefinedArchitectureGui(){
		this.serviceProvider = ServiceProvider.getInstance();
		this.definedArchitectureFrame = serviceProvider.getGraphicsService().getDefinedArchitectureGUI();
	}
	
	public void showDefinedArchitectureGui() {
		if (definedArchitectureFrame != null){
			GridBagConstraints constraint = new GridBagConstraints();
			constraint.fill = GridBagConstraints.BOTH;
			constraint.gridx = 1;
			constraint.gridy = 0;
			constraint.weightx = 0.5;
			constraint.anchor = GridBagConstraints.NORTHEAST;
			mainController.getMainGui().getContentPane().add(definedArchitectureFrame, constraint);
			definedArchitectureFrame.setVisible(true);
			definedArchitectureFrame.setLocation(0, 0);
			//this.hidden = false;
		}		
	}

	public void hideDefinedArchitectureGui(){
		this.defineFrame.setVisible(false);
		this.hidden = true;
	}

	public void toggleDefinedArchitectureGui() {
		if(hidden) {
			this.defineFrame.setVisible(true);
			this.hidden = false;
		}
		else {
			hideDefineGui();
		}
	}	
	
	public void setAnalysedArchitectureGui(){
		this.serviceProvider = ServiceProvider.getInstance();
		this.analysedArchitectureFrame = serviceProvider.getGraphicsService().getAnalysedArchitectureGUI();
	}
	
	public void showAnalysedArchitectureGui() {
		if (analysedArchitectureFrame != null){
			mainController.getMainGui().getContentPane().add(analysedArchitectureFrame);
			analysedArchitectureFrame.setVisible(true);
			//this.hidden = false;
		}		
	}

	public void hideAnalysedArchitectureGui(){
		this.defineFrame.setVisible(false);
		this.hidden = true;
	}

	public void toggleAnalysedArchitectureGui() {
		if(hidden) {
			this.defineFrame.setVisible(true);
			this.hidden = false;
		}
		else {
			hideDefineGui();
		}
	}	
	
	
}
