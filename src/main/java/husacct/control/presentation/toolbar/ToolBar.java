package husacct.control.presentation.toolbar;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;

import husacct.common.Resource;
import husacct.control.presentation.menubar.AnalyseMenu;
import husacct.control.presentation.menubar.DefineMenu;
import husacct.control.presentation.menubar.FileMenu;
import husacct.control.presentation.menubar.MenuBar;
import husacct.control.presentation.menubar.ValidateMenu;
import husacct.control.task.StateController;

public class ToolBar extends JToolBar{
	private static final long serialVersionUID = 1L;
	
	private StateController stateController;
	
	private ToolBarItem createWorkspace, openWorkspace, saveWorkspace;
	private ToolBarItem defineArchitecture, definedArchitectureDiagram; //, moduleAndDependenciesDiagram, moduleAndRulesDiagram //Disabled in vs 5.1; waiting for improvement
	private ToolBarItem applicationProperties, analysedApplicationOverview, analysedApplicationDiagram;
	private ToolBarItem validate;
	
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
		JMenuItem definedArchitectureDiagramItem = defineMenu.definedArchitectureDiagramItem();
		//JMenuItem moduleAndRulesDiagramItem = defineMenu.getModuleAndRuleDiagramItem();
		//JMenu defineArchitectureDiagramMenu = defineMenu.getArchitectureDiagram();
		
		JMenuItem setApplicationPropertiesItem = analyseMenu.getSetApplicationPropertiesItem();
		JMenuItem analysedApplicationOverviewItem = analyseMenu.getAnalysedApplicationOverviewItem();
		JMenuItem analysedApplicationDiagramItem = analyseMenu.getAnalysedArchitectureDiagramItem();
		
		JMenuItem validateItem = validateMenu.getValidateItem();
		
		ImageIcon icon;
		
		icon = new ImageIcon(Resource.get(Resource.ICON_NEW));
		createWorkspace = new ToolBarItem("CreateWorkspace", icon, createWorkspaceItem, stateController);
		
		icon = new ImageIcon(Resource.get(Resource.ICON_OPEN));
		openWorkspace = new ToolBarItem("OpenWorkspace", icon, openWorkspaceItem, stateController);
		
		icon = new ImageIcon(Resource.get(Resource.ICON_SAVE));
		saveWorkspace = new ToolBarItem("SaveWorkspace", icon, saveWorkspaceItem, stateController);
		
		icon = new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE));
		defineArchitecture = new ToolBarItem("DefineArchitecture", icon, defineArchitectureItem, stateController);
		
		//icon = new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE_DIAGRAM));
		//defineArchitectureDiagram = new ToolBarItem("DefineArchitectureDiagram", icon, defineArchitectureDiagramMenu, stateController);
				
		icon = new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE_DIAGRAM));
		definedArchitectureDiagram = new ToolBarItem("DefinedArchitectureDiagram", icon, definedArchitectureDiagramItem, stateController);
		
		//icon = new ImageIcon(Resource.get(Resource.ICON_DEFINE_ARCHITECTURE_DIAGRAM));
		//moduleAndRulesDiagram = new ToolBarItem("ModuleAndRuleDiagram", icon, moduleAndRulesDiagramItem, stateController);
		
		icon = new ImageIcon(Resource.get(Resource.ICON_APPLICATION_PROPERTIES));
		applicationProperties = new ToolBarItem("ApplicationProperties", icon, setApplicationPropertiesItem, stateController);
		
		icon = new ImageIcon(Resource.get(Resource.ICON_APPLICATION_OVERVIEW));
		analysedApplicationOverview = new ToolBarItem("AnalysedApplicationOverview", icon, analysedApplicationOverviewItem, stateController);
		
		icon = new ImageIcon(Resource.get(Resource.ICON_ANALYSED_ARCHITECTURE_DIAGRAM));
		analysedApplicationDiagram = new ToolBarItem("AnalysedArchitectureDiagram", icon, analysedApplicationDiagramItem, stateController);
		
		icon = new ImageIcon(Resource.get(Resource.ICON_VALIDATE));
		validate = new ToolBarItem("Validate", icon, validateItem, stateController);
		
		createWorkspace.setEnabled(false);
		openWorkspace.setEnabled(false);
		saveWorkspace.setEnabled(false);
		defineArchitecture.setEnabled(false);
		definedArchitectureDiagram.setEnabled(false);
		//moduleAndRulesDiagram.setEnabled(false);
		applicationProperties.setEnabled(false);
		analysedApplicationOverview.setEnabled(false);
		analysedApplicationDiagram.setEnabled(false);
		validate.setEnabled(false);
		
		add(createWorkspace);
		add(openWorkspace);
		add(saveWorkspace);
		addSeparator();
		add(defineArchitecture);
		add(definedArchitectureDiagram);
		//add(moduleAndRulesDiagram);
		//add(defineArchitectureDiagram);
		addSeparator();
		add(applicationProperties);
		add(analysedApplicationOverview);
		add(analysedApplicationDiagram);
		addSeparator();
		add(validate);
		
	}
}
