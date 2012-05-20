package husacct.control.presentation.toolbar;

import husacct.control.task.ApplicationController;
import husacct.control.task.IStateChangeListener;
import husacct.control.task.MainController;
import husacct.control.task.StateController;
import husacct.control.task.States;
import husacct.control.task.ViewController;
import husacct.control.task.WorkspaceController;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar{
	private static final long serialVersionUID = 1L;
	
	private StateController stateController;
	private WorkspaceController workspaceController;
	private ViewController viewController;
	private ApplicationController applicationController;
	
	private ToolBarItem createWorkspace, openWorkspace, saveWorkspace;
	private ToolBarItem defineArchitecture, defineArchitectureDiagram;
	private ToolBarItem applicationProperties, analysedApplicationOverview, analysedApplicationDiagram;
	private ToolBarItem validateNow;
	
	
	public ToolBar(MainController mainController){
		stateController = mainController.getStateController();
		workspaceController = mainController.getWorkspaceController();
		viewController = mainController.getViewController();
		applicationController = mainController.getApplicationController();
		
		setup();
		addComponents();
		addListeners();
	}
	
	private void setup(){
		setFloatable(false);
	}
	
	private void addComponents(){
		ImageIcon icon;
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-newworkspace.png"));
		createWorkspace = new ToolBarItem("CreateWorkspace", icon);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-openworkspace.png"));
		openWorkspace = new ToolBarItem("OpenWorkspace", icon);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-saveworkspace.png"));
		saveWorkspace = new ToolBarItem("SaveWorkspace", icon);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-definearchitecture.png"));
		defineArchitecture = new ToolBarItem("DefineArchitecture", icon);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-definedarchitecturediagram.png"));
		defineArchitectureDiagram = new ToolBarItem("DefinedArchitectureDiagram", icon);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-applicationproperties.png"));
		applicationProperties = new ToolBarItem("ApplicationProperties", icon);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-applicationoverview.png"));
		analysedApplicationOverview = new ToolBarItem("AnalysedApplicationOverview", icon);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-analysedarchitecturediagram.png"));
		analysedApplicationDiagram = new ToolBarItem("AnalysedArchitectureDiagram", icon);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-validate.png"));
		validateNow = new ToolBarItem("ValidateNow", icon);
		
		add(createWorkspace);
		add(openWorkspace);
		add(saveWorkspace);
		addSeparator();
		add(defineArchitecture);
		add(defineArchitectureDiagram);
		addSeparator();
		add(applicationProperties);
		add(analysedApplicationOverview);
		add(analysedApplicationDiagram);
		addSeparator();
		add(validateNow);
		
	}
	
	private void addListeners(){
		createWorkspace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				workspaceController.showCreateWorkspaceGui();
			}
		});
		openWorkspace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				workspaceController.showOpenWorkspaceGui();
			}
		});
		saveWorkspace.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				workspaceController.showSaveWorkspaceGui();
			}
		});
		defineArchitecture.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				viewController.showDefineGui();
			}
		});
		defineArchitectureDiagram.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				viewController.showDefinedArchitectureGui();
			}
		});
		analysedApplicationOverview.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				viewController.showApplicationOverviewGui();
			}
		});
		applicationProperties.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				applicationController.showApplicationDetailsGui();
			}
		});
		analysedApplicationDiagram.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				viewController.showAnalysedArchitectureGui();
			}
		});
		validateNow.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				viewController.showViolationsGui();
			}
		});
		
		stateController.addStateChangeListener(new IStateChangeListener() {
			@Override
			public void changeState(List<States> states) {
				
				createWorkspace.setEnabled(false);
				openWorkspace.setEnabled(false);
				saveWorkspace.setEnabled(false);
				defineArchitecture.setEnabled(false);
				defineArchitectureDiagram.setEnabled(false);
				applicationProperties.setEnabled(false);
				analysedApplicationOverview.setEnabled(false);
				analysedApplicationDiagram.setEnabled(false);
				validateNow.setEnabled(false);
				
				// TODO: Efficienter
				if(states.contains(States.NONE)){
					createWorkspace.setEnabled(true);
					openWorkspace.setEnabled(true);
				}
				if(states.contains(States.OPENED)){
					createWorkspace.setEnabled(true);
					openWorkspace.setEnabled(true);
					saveWorkspace.setEnabled(true);
					defineArchitecture.setEnabled(true);
					applicationProperties.setEnabled(true);
				}
				if(states.contains(States.DEFINED)){
					createWorkspace.setEnabled(true);
					openWorkspace.setEnabled(true);
					saveWorkspace.setEnabled(false);
					defineArchitecture.setEnabled(true);
					defineArchitectureDiagram.setEnabled(true);
					applicationProperties.setEnabled(true);
				}
				if(states.contains(States.MAPPED)){
					createWorkspace.setEnabled(true);
					openWorkspace.setEnabled(true);
					saveWorkspace.setEnabled(true);
					defineArchitecture.setEnabled(true);
					defineArchitectureDiagram.setEnabled(true);
					applicationProperties.setEnabled(true);
					validateNow.setEnabled(true);
				}
				if(states.contains(States.ANALYSED)){
					createWorkspace.setEnabled(true);
					openWorkspace.setEnabled(true);
					saveWorkspace.setEnabled(true);
					defineArchitecture.setEnabled(true);
					applicationProperties.setEnabled(true);
					analysedApplicationOverview.setEnabled(true);
					analysedApplicationDiagram.setEnabled(true);
				}
				if(states.contains(States.VALIDATED)){
					createWorkspace.setEnabled(true);
					openWorkspace.setEnabled(true);
					saveWorkspace.setEnabled(true);
					defineArchitecture.setEnabled(true);
					defineArchitectureDiagram.setEnabled(true);
					applicationProperties.setEnabled(true);
					analysedApplicationOverview.setEnabled(true);
					analysedApplicationDiagram.setEnabled(true);
					validateNow.setEnabled(true);
				}
			}
		});
	}
}
