package husacct.control.task;

import husacct.ServiceProvider;

import java.awt.Dimension;
import java.awt.GridBagConstraints;

import javax.swing.JInternalFrame;
import javax.swing.JLabel;

public class ViewController {

	private ServiceProvider serviceProvider;
	private MainController mainController;
	
	private JInternalFrame defineInternalFrame;
	private JInternalFrame violationsInternalFrame;
	private JInternalFrame analysedArchitectureInternalFrame;
	private JInternalFrame definedArchitectureInternalFrame;
		
	private GridBagConstraints constraint = new GridBagConstraints();
	
	public ViewController(MainController maincontroller){
		this.mainController = maincontroller;
	}
	
	public void setDefineGui(){
		serviceProvider = ServiceProvider.getInstance();
		this.defineInternalFrame = serviceProvider.getDefineService().getDefinedGUI();
		constraint.gridx = 0;
		constraint.gridy = 0;
		constraint.weightx = 0.5;
		constraint.weighty = 0.75;
		constraint.gridwidth = 1;
		constraint.fill = GridBagConstraints.BOTH;
		mainController.getMainGui().getContentPane().add(defineInternalFrame, constraint);
	}
	
	public void showDefineGui() {
		if (defineInternalFrame != null){
			defineInternalFrame.setMaximizable(true);
			defineInternalFrame.setVisible(true);
		}		
	}

	public void setViolationsGui(){
		this.serviceProvider = ServiceProvider.getInstance();
		this.violationsInternalFrame = serviceProvider.getValidateService().getBrowseViolationsGUI();
		constraint.fill = GridBagConstraints.BOTH;
		constraint.gridx = 0;
		constraint.gridy = 1;
		constraint.gridwidth = 2;
		constraint.weightx = 0.0;
		constraint.weighty = 0.25;
		mainController.getMainGui().getContentPane().add(violationsInternalFrame, constraint);
	}
	
	public void showViolationsGui() {
		if (violationsInternalFrame != null){
			violationsInternalFrame.setClosable(false);
			violationsInternalFrame.setVisible(true);
		}
	}
	
	public void setDefinedArchitectureGui(){
		this.serviceProvider = ServiceProvider.getInstance();
		this.definedArchitectureInternalFrame = serviceProvider.getGraphicsService().getDefinedArchitectureGUI();
		constraint.gridx = 1;
		constraint.gridy = 0;
		constraint.weightx = 0.5;
		constraint.weighty = 0.75;
		constraint.gridwidth = 1;
		constraint.fill = GridBagConstraints.BOTH;
		mainController.getMainGui().getContentPane().add(definedArchitectureInternalFrame, constraint);
	}
	
	public void showDefinedArchitectureGui() {
		if (definedArchitectureInternalFrame != null){
			definedArchitectureInternalFrame.setMaximizable(true);
			definedArchitectureInternalFrame.setVisible(true);
		}		
	}	
	
	public void setAnalysedArchitectureGui(){
		this.serviceProvider = ServiceProvider.getInstance();
		this.analysedArchitectureInternalFrame = serviceProvider.getGraphicsService().getAnalysedArchitectureGUI();
	}
	
	public void showAnalysedArchitectureGui() {
		if (analysedArchitectureInternalFrame != null){
			mainController.getMainGui().getContentPane().add(analysedArchitectureInternalFrame);
			analysedArchitectureInternalFrame.setVisible(true);
		}		
	}	
}
