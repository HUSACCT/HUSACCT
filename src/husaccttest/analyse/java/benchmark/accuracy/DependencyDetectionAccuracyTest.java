package husaccttest.analyse.java.benchmark.accuracy;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husaccttest.analyse.TestProjectFinder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class DependencyDetectionAccuracyTest {
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspaceLocation = "C:\\Tools\\Eclipse43\\Workspace\\HUSACCT\\testprojects\\workspaces\\AccuracyTest-Java-2013-06-11.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null; 
	private static String language = "Java";

// To be removed
	private static DependencyDTO[] allDependencies = null;
	private static String path = TestProjectFinder.lookupProject("java", "accuracy");

	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format("Running HUSACCT using workspace: " + workspaceLocation));

			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			workspaceController = mainController.getWorkspaceController();
			loadWorkspace(workspaceLocation);
	
			analyseApplication();
			//analyseApplication() starts a different Thread, and needs some time
			boolean isAnalysing = true;
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			while(isAnalysing){
				try {
					Thread.sleep((long)10);
				} catch (InterruptedException e) {}
				isAnalysing = mainController.getStateController().isAnalysing();
			}
			
/*			setLog4jConfiguration();

			ArrayList<ProjectDTO> projects = createProjectDTOs();

			ControlServiceImpl ctrlS = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			ctrlS.getMainController().getWorkspaceController().createWorkspace("JavaAnalyseTestWorkspace");
			ServiceProvider.getInstance().getDefineService().createApplication(language+" test", projects, "1.0");
			ctrlS.getMainController().getWorkspaceController().getCurrentWorkspace().setApplicationData(ServiceProvider.getInstance().getDefineService().getApplicationDetails());
			ctrlS.getMainController().getApplicationController().analyseApplication();
			analyseService = ServiceProvider.getInstance().getAnalyseService();

			logger.debug("PROJECT LOADED");

			//analyse is in a different Thread, and needs some time
			while(!isAnalysed){
				try {
					Thread.sleep((long)10);
				} catch (InterruptedException e) {}
				isAnalysed = analyseService.isAnalysed();
			}
			allDependencies = analyseService.getAllUnfilteredDependencies();
*/			
		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
		}
	}

	// TESTS
	// DIRECT
	// Access
	@Test
	public void AccessClassVariable(){
		String fromModule = "domain.direct.violating.AccessClassVariable";
		String toModule = "technology.direct.dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessClassVariableConstant(){
		String fromModule = "domain.direct.violating.AccessClassVariableConstant";
		String toModule = "technology.direct.dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessClassVariableInterface(){
		String fromModule = "domain.direct.violating.AccessClassVariableInterface";
		String toModule = "technology.direct.dao.ISierraDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessEnumeration(){
		String fromModule = "domain.direct.violating.AccessEnumeration";
		String toModule = "technology.direct.dao.TipDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessInstanceVariableRead(){
		String fromModule = "domain.direct.violating.AccessInstanceVariableRead";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessInstanceVariableWrite(){
		String fromModule = "domain.direct.violating.AccessInstanceVariableWrite";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessInstanceVariableConstant(){
		String fromModule = "domain.direct.violating.AccessInstanceVariableConstant";
		String toModule = "technology.direct.dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessInstanceVariableSuperClass(){
		String fromModule = "domain.direct.violating.AccessInstanceVariableSuperClass";
		String toModule = "technology.direct.subclass.CallInstanceSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind); 
		toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void AccessInstanceVariableSuperSuperClass(){
		String fromModule = "domain.direct.violating.AccessInstanceVariableSuperSuperClass";
		String toModule = "technology.direct.subclass.CallInstanceSubSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind); 
		toModule = "technology.direct.subclass.CallInstanceSubClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind); 
		toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome3 = areDependencyTypesDetected(fromModule, toModule, typesToFind); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true) || (outcome3 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void AccessObjectReferenceAsParameter(){
		String fromModule = "domain.direct.violating.AccessObjectReferenceAsParameter";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessObjectReferenceWithinIfStatement(){
		String fromModule = "domain.direct.violating.AccessObjectReferenceWithinIfStatement";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	// Annotation	
	@Test
	public void AnnotationDependency(){
		String fromModule = "domain.direct.violating.AnnotationDependency";
		String toModule = "technology.direct.dao.SettingsAnnotation";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Annotation");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	// Call
	@Test
	public void CallClassMethod(){
		String fromModule = "domain.direct.violating.CallClassMethod";
		String toModule = "technology.direct.dao.BadgesDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallConstructor(){
		String fromModule = "domain.direct.violating.CallConstructor";
		String toModule = "technology.direct.dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallConstructorLibraryClass(){
		String fromModule = "domain.direct.violating.CallConstructorLibraryClass";
		String toModule = "fi.foyt.foursquare.api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstance(){
		String fromModule = "domain.direct.violating.CallInstance";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceInnerClass(){
		String fromModule = "domain.direct.violating.CallInstanceInnerClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceInterface(){
		String fromModule = "domain.direct.violating.CallInstanceInterface";
		String toModule = "technology.direct.dao.CallInstanceInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceLibraryClass(){
		String fromModule = "domain.direct.violating.CallInstanceLibraryClass";
		String toModule = "fi.foyt.foursquare.api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceSuperClass(){
		String fromModule = "domain.direct.violating.CallInstanceSuperClass";
		String toModule = "technology.direct.subclass.CallInstanceSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind); 
		toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void CallInstanceSuperSuperClass(){
		String fromModule = "domain.direct.violating.CallInstanceSuperSuperClass";
		String toModule = "technology.direct.subclass.CallInstanceSubSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind); 
		toModule = "technology.direct.subclass.CallInstanceSubClassDOA";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind); 
		toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome3 = areDependencyTypesDetected(fromModule, toModule, typesToFind); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true) || (outcome3 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	// Declaration
	@Test
	public void DeclarationExceptionThrows(){
		String fromModule = "domain.direct.violating.DeclarationExceptionThrows";
		String toModule = "technology.direct.dao.StaticsException";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void DeclarationParameter(){
		String fromModule = "domain.direct.violating.DeclarationParameter";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void DeclarationReturnType(){
		String fromModule = "domain.direct.violating.DeclarationReturnType";
		String toModule = "technology.direct.dao.VenueDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void DeclarationTypeCast(){
		String fromModule = "domain.direct.violating.DeclarationTypeCast";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void DeclarationTypeCastOfArgument(){
		String fromModule = "domain.direct.violating.DeclarationTypeCastOfArgument";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void DeclarationVariableInstance(){
		String fromModule = "domain.direct.violating.DeclarationVariableInstance";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void DeclarationVariableStatic(){
		String fromModule = "domain.direct.violating.DeclarationVariableStatic";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void DeclarationVariableLocal(){
		String fromModule = "domain.direct.violating.DeclarationVariableLocal";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void DeclarationVariableLocal_Initialized(){
		String fromModule = "domain.direct.violating.DeclarationVariableLocal_Initialized";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	// Import
	@Test
	public void ImportDependencyUnused(){
		String fromModule = "domain.direct.violating.ImportDependencyUnused";
		String toModule = "technology.direct.dao.SpecialDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	// Inheritance
	@Test
	public void InheritanceExtends(){
		String fromModule = "domain.direct.violating.InheritanceExtends";
		String toModule = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));	}

	@Test
	public void InheritanceExtendsAbstractClass(){
		String fromModule = "domain.direct.violating.InheritanceExtendsAbstractClass";
		String toModule = "technology.direct.dao.FriendsDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void InheritanceImplementsInterface(){
		String fromModule = "domain.direct.violating.InheritanceImplementsInterface";
		String toModule = "technology.direct.dao.IMapDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	//TESTS
	//INDIRECT

	@Test
	public void AccessInstanceVariableIndirect_MethodVar(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessInstanceVariableIndirect_MethodVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVar(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessInstanceVariableIndirect_VarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVarToString(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessInstanceVariableIndirect_VarVarToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_MethVarVar(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessInstanceVariableIndirectIndirect_MethVarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_VarVarVar(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessInstanceVariableIndirectIndirect_VarVarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter_POI(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessObjectReferenceIndirect_AsParameter_POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessObjectReferenceIndirect_AsParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament_POI(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessObjectReferenceIndirect_WithinIfStament_POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessObjectReferenceIndirect_WithinIfStament";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessStaticVariableIndirect_MethodVar(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessStaticVariableIndirect_MethodVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVar(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessStaticVariableIndirect_VarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVarToString(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessStaticVariableIndirect_VarVarToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_MethodVarVar(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessStaticVariableIndirect_VarVarToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_VarVarVar(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "AccessStaticVariableIndirectIndirect_VarVarVar";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethod(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallInstanceMethodIndirect_MethodMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethodToString(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallInstanceMethodIndirect_MethodMethodToString";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethod_ViaConstructor(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallInstanceMethodIndirect_MethodMethod_ViaConstructor";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_StaticMethodInstanceMethod(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallInstanceMethodIndirect_StaticMethodInstanceMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperClass(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallInstanceMethodIndirect_SuperClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperSuperClass(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallInstanceMethodIndirect_SuperSuperClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirect_VarMethod(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallInstanceMethodIndirect_VarMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_MethodVarMethod(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallInstanceMethodIndirectIndirect_MethodVarMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_VarVarMethod(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallInstanceMethodIndirectIndirect_VarVarMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallStaticMethodIndirect_MethodStaticMethod(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallStaticMethodIndirect_MethodStaticMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void CallStaticMethodIndirect_VarStaticMethod(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "CallStaticMethodIndirect_VarStaticMethod";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("undefined");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void InheritanceExtendsExtendsIndirect(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "InheritanceExtendsExtendsIndirect";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("ExtendsConcreteExtendsAbstract");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void InheritanceExtendsImplementsIndirect(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "InheritanceExtendsImplementsIndirect";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("ExtendsConcreteImplements");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	@Test
	public void InheritanceImplementsExtendsIndirect(){
		String fromModule = "domain.indirect.violatingFrom.AccessClassVariable";
		String toModule = "domain.indirect.violatingTo.CheckInDAO";
		String fromToTest = "InheritanceImplementsExtendsIndirect";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("ImplementsExtendsInterface");

		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind));
	}

	//
	//private helpers
	//
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(DependencyDetectionAccuracyTest.class);
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

	private boolean areDependencyTypesDetected(String moduleFrom, String moduleTo, ArrayList<String> dependencyTypes) {
		boolean dependencyTypesDetected = false;
		TreeMap<String, Boolean> foundDependencyTypes = new TreeMap<String, Boolean>();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] foundDependencies = analyseService.getDependenciesFromTo(moduleFrom, moduleTo);
		int numberOfDependencies = foundDependencies.length;
		for (String dependencyType : dependencyTypes) {
			boolean found = false;
			for (int i=0 ; i < numberOfDependencies; i++){
				if (foundDependencies[i].type.equals(dependencyType)) {
					found = true;
				}
			}
			foundDependencyTypes.put(dependencyType,found);
		}
		if (!foundDependencyTypes.containsValue(false)){
			dependencyTypesDetected = true;
		}
		return dependencyTypesDetected;
	}

// To be removed
	private static ArrayList<ProjectDTO> createProjectDTOs(){
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		ArrayList<String> paths = new ArrayList<String>();
		paths.add(path);
		ProjectDTO project = new ProjectDTO("TestAccuracy", paths, language, "1", "for testing purposes", new ArrayList<AnalysedModuleDTO>());
		projects.add(project);
		return projects;
	}


	private static String getClass(String fromPath){
		return (String) fromPath.subSequence(fromPath.lastIndexOf('.')+1, fromPath.length());
	}

	private boolean areDependenciesDetected(String fromToTest, ArrayList<String> typesToFind) {
		boolean foundFrom = false;
		boolean foundTypes = false;
		int foundTypeCount = 0;
		int amountOfTypes = typesToFind.size();

		for(DependencyDTO dependency : allDependencies){
			String from = getClass(dependency.from);
			String type = dependency.type.toString();
			if(fromToTest.equals(from) && typesToFind.contains(type)){
				foundFrom = true;
				foundTypeCount++;
			}
			if(foundTypeCount == amountOfTypes){
				foundTypes = true;
				break;
			}
		}

		return (foundFrom == foundTypes && foundTypes == true) ? true : false;
	}
}