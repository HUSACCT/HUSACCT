package husaccttest.graphics;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.DependencyDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.define.IDefineService;
import husacct.graphics.domain.Drawing;
import husacct.graphics.domain.figures.BaseFigure;
import husacct.graphics.domain.figures.ModuleFigure;
import husacct.graphics.domain.figures.ParentFigure;
import husacct.graphics.domain.figures.RelationFigure;
import husacct.graphics.task.AnalysedController;
import husacct.graphics.task.DefinedController;
import husaccttest.TestResourceFinder;
import husaccttest.define.DefineSarServicesTest_SRMA;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.jhotdraw.draw.ConnectionFigure;
import org.jhotdraw.draw.Figure;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class DrawingControllerTest {
	private static String workspacePath;
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspace = "SrmaTest-2014-11-12.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null;
	private static IDefineService defineService = null;

	private static AnalysedController graphicsAnalysedController;
	private static DefinedController graphicsDefinedController;
	
	@Before
	public void setup() {
	}

	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format("Start: Graphics DrawingControllerTest"));
			workspacePath = TestResourceFinder.findHusacctWorkspace("java", workspace);
			logger.info(String.format("Running HUSACCT using workspace: " + workspacePath));

			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			workspaceController = mainController.getWorkspaceController();
			loadWorkspace(workspacePath);
	
			analyseApplication(); //analyseApplication() starts a different Thread, and needs some time
			boolean isAnalysing = true;
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			while(isAnalysing){
				try {
					Thread.sleep((long)10);
				} catch (InterruptedException e) {}
				isAnalysing = mainController.getStateController().isAnalysing();
			}

			graphicsAnalysedController = new AnalysedController();
			graphicsAnalysedController.getDrawingSettingsHolder().dependenciesShow();
			graphicsDefinedController = new DefinedController();
			graphicsDefinedController.getDrawingSettingsHolder().dependenciesShow();

		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
		}
	}

	@AfterClass
	public static void tearDown(){
		workspaceController.closeWorkspace();
		logger.info(String.format("Finished: Graphics DrawingControllerTest"));
	}

	// TESTS 

	@Test
	public void analysed_DrawArchitectureTopLevelTest_withoutExternalSystems() {
		graphicsAnalysedController.drawArchitectureTopLevel();
		assertEquals("wrong amount of figures drawn", 3, graphicsAnalysedController.getDrawing().getShownModules().length);
		for (Figure f : graphicsAnalysedController.getDrawing().getShownModules()) {
			if (!(f instanceof BaseFigure)) {
				fail("non-basefigure in drawing");
			} else {
				BaseFigure baseF = (BaseFigure) f;
				assertTrue("module figure says not to be a module", baseF.isModule());
			}
		}
	}

	@Test
	public void analysed_DrawArchitectureTopLevelTest_withExternalSystems() {
		graphicsAnalysedController.getDrawingSettingsHolder().librariesShow();
		graphicsAnalysedController.drawArchitectureTopLevel();
		assertEquals("wrong amount of figures drawn", 4, graphicsAnalysedController.getDrawing().getShownModules().length);
		boolean libraryPresent = false; 
		for (ModuleFigure mf : graphicsAnalysedController.getDrawing().getShownModules()) { 
			if (mf.getType().toLowerCase().equals("library")) {
				libraryPresent = true;
			} 
		}
		assertTrue("No library module", libraryPresent);
		graphicsAnalysedController.getDrawingSettingsHolder().librariesHide();
	}

	@Test
	public void analysed_DrawSingleLevelModulesTest_WithoutContext() {
		graphicsAnalysedController.drawArchitectureTopLevel();
		graphicsAnalysedController.resetContextFigures();
		graphicsAnalysedController.getDrawingSettingsHolder().zoomTypeChange("zoom");
		graphicsAnalysedController.gatherChildModuleFiguresAndContextFigures_AndDraw(new String[] {"technology"});
		int nrOfModules = graphicsAnalysedController.getDrawing().getShownModules().length;
		int numberOfParentFigures = 0;
		for (Figure f : graphicsAnalysedController.getDrawing().getChildren()) { 
			if (f instanceof ParentFigure) {
				ParentFigure parentFigure = (ParentFigure) f;
				if (parentFigure.getName().toLowerCase().equals("technology") || parentFigure.getName().toLowerCase().equals("domain") || 
						parentFigure.getName().toLowerCase().equals("presentation")) {
					numberOfParentFigures ++;
				}
			}
		}
		assertEquals("wrong amount of module figures drawn", 2, nrOfModules);
		assertEquals(0, numberOfParentFigures);
	}

	@Test
	public void analysed_DrawMultiLevelModulesTest_WithContext() {
		graphicsAnalysedController.drawArchitectureTopLevel();
		graphicsAnalysedController.resetContextFigures();
		for (ModuleFigure mf : graphicsAnalysedController.getDrawing().getShownModules()) { 
			if (mf.getName().toLowerCase().equals("technology") || mf.getName().toLowerCase().equals("domain")) {
				graphicsAnalysedController.contextFigures.add(mf);
			} 
		}
		graphicsAnalysedController.getDrawingSettingsHolder().zoomTypeChange("context");
		graphicsAnalysedController.gatherChildModuleFiguresAndContextFigures_AndDraw(new String[] {"technology"});
		int nrOfModules = graphicsAnalysedController.getDrawing().getShownModules().length;
		int numberOfParentFigures = 0;
		for (Figure f : graphicsAnalysedController.getDrawing().getChildren()) { 
			if (f instanceof ParentFigure) {
				ParentFigure parentFigure = (ParentFigure) f;
				if (parentFigure.getName().toLowerCase().equals("technology") || parentFigure.getName().toLowerCase().equals("domain") || 
						parentFigure.getName().toLowerCase().equals("presentation")) {
					numberOfParentFigures ++;
				}
			}
		}
		assertEquals("wrong amount of figures drawn", 3, nrOfModules);
		assertEquals(1, numberOfParentFigures);
	}

	@Test
	public void analysed_DrawMultiLevelModulesTest_WithOutContext() {
		graphicsAnalysedController.drawArchitectureTopLevel();
		graphicsAnalysedController.resetContextFigures();
		graphicsAnalysedController.getDrawingSettingsHolder().zoomTypeChange("zoom");
		graphicsAnalysedController.gatherChildModuleFiguresAndContextFigures_AndDraw(new String[] {"technology", "domain"});
		int nrOfModules = graphicsAnalysedController.getDrawing().getShownModules().length;
		int numberOfParentFigures = 0;
		for (Figure f : graphicsAnalysedController.getDrawing().getChildren()) { 
			if (f instanceof ParentFigure) {
				ParentFigure parentFigure = (ParentFigure) f;
				if (parentFigure.getName().toLowerCase().equals("technology") || parentFigure.getName().toLowerCase().equals("domain") || 
						parentFigure.getName().toLowerCase().equals("presentation")) {
					numberOfParentFigures ++;
				}
			}
		}
		assertEquals("wrong amount of figures drawn",4, nrOfModules);
		assertEquals(2, numberOfParentFigures);
	}

	@Test
	public void defined_DrawArchitectureTopLevelTest_withoutExternalSystems() {
		graphicsDefinedController.drawArchitectureTopLevel();
		graphicsDefinedController.resetContextFigures();
		graphicsDefinedController.getDrawingSettingsHolder().librariesHide();
		graphicsDefinedController.gatherChildModuleFiguresAndContextFigures_AndDraw(new String[] {"Technology"});
		graphicsDefinedController.drawArchitectureTopLevel();
		int nrOfModules = graphicsDefinedController.getDrawing().getShownModules().length;
		for (Figure f : graphicsAnalysedController.getDrawing().getShownModules()) {
			if (!(f instanceof BaseFigure)) {
				fail("non-basefigure in drawing");
			} else {
				BaseFigure baseF = (BaseFigure) f;
				assertTrue("module figure says not to be a module", baseF.isModule());
			}
		}
		assertEquals("wrong amount of figures drawn", 3, nrOfModules);
	}

	@Test
	public void defined_DrawMultiLevelModulesTest_WithContext() {
		graphicsDefinedController.drawArchitectureTopLevel();
		graphicsDefinedController.resetContextFigures();
		for (ModuleFigure mf : graphicsDefinedController.getDrawing().getShownModules()) { 
			if (mf.getName().equals("Technology") || mf.getName().equals("Domain")) {
				graphicsDefinedController.contextFigures.add(mf);
			} 
		}
		graphicsDefinedController.getDrawingSettingsHolder().zoomTypeChange("context");
		graphicsDefinedController.gatherChildModuleFiguresAndContextFigures_AndDraw(new String[] {"Technology"});
		int nrOfModules = graphicsDefinedController.getDrawing().getShownModules().length;
		int numberOfParentFigures = 0;
		for (Figure f : graphicsDefinedController.getDrawing().getChildren()) { 
			if (f instanceof ParentFigure) {
				ParentFigure parentFigure = (ParentFigure) f;
				if (parentFigure.getName().equals("Technology") || parentFigure.getName().equals("Domain") || 
						parentFigure.getName().equals("Presentation")) {
					numberOfParentFigures ++;
				}
			}
		}
		assertEquals("wrong amount of figures drawn", 3, nrOfModules);
		assertEquals(1, numberOfParentFigures);
	}

	@Test
	public void defined_DrawRelation_NumberOfDependencies() {
		graphicsDefinedController.drawArchitectureTopLevel();
		graphicsDefinedController.resetContextFigures();
		for (ModuleFigure mf : graphicsDefinedController.getDrawing().getShownModules()) { 
			if (mf.getName().equals("Technology") || mf.getName().equals("Domain")) {
				graphicsDefinedController.contextFigures.add(mf);
			} 
		}
		graphicsDefinedController.getDrawingSettingsHolder().zoomTypeChange("context");
		graphicsDefinedController.gatherChildModuleFiguresAndContextFigures_AndDraw(new String[] {"Technology"});
		Drawing drawing = graphicsDefinedController.getDrawing();
		RelationFigure[] relations = drawing.getShownRelations();
		for (Figure f : relations) { 
			if (f instanceof RelationFigure) {
				ConnectionFigure cf = (ConnectionFigure) f;
				ModuleFigure start = (ModuleFigure) cf.getStartFigure();
				ModuleFigure end = (ModuleFigure) cf.getEndFigure();
				if ((start != null) && start.getUniqueName().equals("Domain")&& (end != null) && end.getUniqueName().equals("Technology.RelationRules")) {
					int nrOfDependencies = getNumberofDependenciesBetweenModulesInIntendedArchitecture(start.getUniqueName(), end.getUniqueName());
					int nrOfDependenciesShown = ((RelationFigure) f).getAmount();
					assertEquals("wrong amount of figures drawn", nrOfDependenciesShown, nrOfDependencies);
				}
			} 
		}
	}

	
	//
	//private helpers
	//
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(DefineSarServicesTest_SRMA.class);
	}
	
	private static void loadWorkspace(String location) {
		logger.info(String.format("Loading workspace %s", location));
		File file = new File(location);
		if(file.exists()){
			HashMap<String, Object> dataValues = new HashMap<String, Object>();
			dataValues.put("file", file);
			workspaceController.loadWorkspace("Xml", dataValues);
			if(workspaceController.isOpenWorkspace()){
				logger.info(String.format("Workspace %s loaded", location));
			} else {
				logger.warn(String.format("Unable to open workspace %s", file.getAbsoluteFile()));
			}
		} else {
			logger.warn(String.format("Unable to locate %s", file.getAbsoluteFile()));
		}
	}

	private static void analyseApplication() {
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		mainController.getApplicationController().analyseApplication();
	}

	private int getNumberofDependenciesBetweenModulesInIntendedArchitecture(String fromModule, String toModule) {
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		defineService = ServiceProvider.getInstance().getDefineService();
		HashSet<String> physicalFromClassPaths = defineService.getModule_AllPhysicalClassPathsOfModule(fromModule);
		HashSet<String> physicalToClassPaths = defineService.getModule_AllPhysicalClassPathsOfModule(toModule);
		ArrayList<DependencyDTO> allFoundDependencies = new ArrayList<DependencyDTO>();
		for (String fromPackages : physicalFromClassPaths) {
			for (String toPackages: physicalToClassPaths) {
				for (DependencyDTO dependency : analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(fromPackages, toPackages)) {
					allFoundDependencies.add(dependency);
				}
			}
		}
		int numberOfDependencies = allFoundDependencies.size();
		return numberOfDependencies;
	}

}
