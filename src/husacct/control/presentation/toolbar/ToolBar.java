package husacct.control.presentation.toolbar;

import husacct.control.presentation.menubar.AnalyseMenu;
import husacct.control.presentation.menubar.DefineMenu;
import husacct.control.presentation.menubar.FileMenu;
import husacct.control.presentation.menubar.MenuBar;
import husacct.control.presentation.menubar.ValidateMenu;
import husacct.control.task.StateController;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

public class ToolBar extends JToolBar{
	private static final long serialVersionUID = 1L;
	
	private StateController stateController;
	
	private ToolBarItem createWorkspace, openWorkspace, saveWorkspace;
	private ToolBarItem defineArchitecture, defineArchitectureDiagram;
	private ToolBarItem applicationProperties, analysedApplicationOverview, analysedApplicationDiagram;
	private ToolBarItem validateNow;
	
	private MenuBar menuBar;
	
	
	public ToolBar(MenuBar menuBar, StateController stateController){
		this.menuBar = menuBar;
		this.stateController = stateController;
		setup();
		addComponents();
	}
	
	private void setup(){
		setFloatable(false);
	}
	
	private void addComponents(){
		
		FileMenu fileMenu = menuBar.getFileMenu();
		DefineMenu defineMenu = menuBar.getDefineMenu();
		AnalyseMenu analyseMenu = menuBar.getAnalyseMenu();
		ValidateMenu validateMenu = menuBar.getValidateMenu();
		
		JMenuItem createWorkspaceItem = fileMenu.getCreateWorkspaceItem();
		JMenuItem openWorkspaceItem = fileMenu.getOpenWorkspaceItem();
		JMenuItem saveWorkspaceItem = fileMenu.getSaveWorkspaceItem();
		
		JMenuItem defineArchitectureItem = defineMenu.getDefineArchitectureItem();
		JMenuItem defineArchitectureDiagramItem = defineMenu.getDefinedArchitectureDiagramItem();
		
		JMenuItem setApplicationPropertiesItem = analyseMenu.getSetApplicationPropertiesItem();
		JMenuItem analysedApplicationOverviewItem = analyseMenu.getAnalysedApplicationOverviewItem();
		JMenuItem analysedApplicationDiagramItem = analyseMenu.getAnalysedArchitectureDiagramItem();
		
		JMenuItem validateNowItem = validateMenu.getValidateNowItem();
		
		ImageIcon icon;
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-newworkspace.png"));
		createWorkspace = new ToolBarItem("CreateWorkspace", icon, createWorkspaceItem, stateController);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-openworkspace.png"));
		openWorkspace = new ToolBarItem("OpenWorkspace", icon, openWorkspaceItem, stateController);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-saveworkspace.png"));
		saveWorkspace = new ToolBarItem("SaveWorkspace", icon, saveWorkspaceItem, stateController);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-definearchitecture.png"));
		defineArchitecture = new ToolBarItem("DefineArchitecture", icon, defineArchitectureItem, stateController);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-definedarchitecturediagram.png"));
		defineArchitectureDiagram = new ToolBarItem("DefinedArchitectureDiagram", icon, defineArchitectureDiagramItem, stateController);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-applicationproperties.png"));
		applicationProperties = new ToolBarItem("ApplicationProperties", icon, setApplicationPropertiesItem, stateController);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-applicationoverview.png"));
		analysedApplicationOverview = new ToolBarItem("AnalysedApplicationOverview", icon, analysedApplicationOverviewItem, stateController);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-analysedarchitecturediagram.png"));
		analysedApplicationDiagram = new ToolBarItem("AnalysedArchitectureDiagram", icon, analysedApplicationDiagramItem, stateController);
		
		icon = new ImageIcon(getClass().getResource("/husacct/common/resources/control/icon-validate.png"));
		validateNow = new ToolBarItem("ValidateNow", icon, validateNowItem, stateController);
		
		createWorkspace.setEnabled(false);
		openWorkspace.setEnabled(false);
		saveWorkspace.setEnabled(false);
		defineArchitecture.setEnabled(false);
		defineArchitectureDiagram.setEnabled(false);
		applicationProperties.setEnabled(false);
		analysedApplicationOverview.setEnabled(false);
		analysedApplicationDiagram.setEnabled(false);
		validateNow.setEnabled(false);
		
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
}
