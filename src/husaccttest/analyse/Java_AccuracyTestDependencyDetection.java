package husaccttest.analyse;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.DependencyDTO;
import husacct.control.ControlServiceImpl;
import husacct.control.task.MainController;
import husacct.control.task.WorkspaceController;
import husacct.validate.IValidateService;
import husaccttest.TestResourceFinder;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

public class Java_AccuracyTestDependencyDetection {
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private final static String workspace = "AccuracyTest-Java-2013-06-11.xml";
	private static Logger logger;
	private static IAnalyseService analyseService = null;
	private static IValidateService validateService = null;

	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			String workspacePath = TestResourceFinder.findHusacctWorkspace("java", workspace);
			logger.info(String.format("Running HUSACCT using workspace: " + workspacePath));

			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			workspaceController = mainController.getWorkspaceController();
			loadWorkspace(workspacePath);
	
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
			checkConformance();
			//checkConformance() starts a different Thread, and needs some time
			boolean isValidating = true;
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			while(isValidating){
				try {
					Thread.sleep((long)10);
				} catch (InterruptedException e) {}
				isValidating = mainController.getStateController().isValidating();
			}
		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
		}
	}

	@AfterClass
	public static void tearDown(){
		workspaceController.closeWorkspace();
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
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Class Variable", false));
	}

	@Test
	public void AccessClassVariableConstant(){
		String fromModule = "domain.direct.violating.AccessClassVariableConstant";
		String toModule = "technology.direct.dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Class Variable Constant", false));
	}

	@Test
	public void AccessClassVariableInterface(){
		String fromModule = "domain.direct.violating.AccessClassVariableInterface";
		String toModule = "technology.direct.dao.ISierraDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Interface Variable", false));
	}

	@Test
	public void AccessEnumeration(){
		String fromModule = "domain.direct.violating.AccessEnumeration";
		String toModule = "technology.direct.dao.TipDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Enumeration Variable", false));
	}

	@Test
	public void AccessEnumerationInner(){
		String fromModule = "domain.direct.violating.AccessInnerEnumeration";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.InnerEnumeration";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Enumeration Variable", false));
	}

	@Test
	public void AccessFromInnerClass(){
		String fromModule = "domain.direct.violating.CallFromInnerClass.CallingInnerClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessInstanceVariableInPlusExpression(){
		String fromModule = "domain.direct.violating.AccessInstanceVariableInPlusExpression";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessInstanceVariableRead(){
		String fromModule = "domain.direct.violating.AccessInstanceVariableRead";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessInstanceVariableWrite(){
		String fromModule = "domain.direct.violating.AccessInstanceVariableWrite";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessInstanceVariableConstant(){
		String fromModule = "domain.direct.violating.AccessInstanceVariableConstant";
		String toModule = "technology.direct.dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable Constant", false));
	}

	@Test
	public void AccessInstanceVariableSuperClass(){
		String fromModule = "domain.direct.violating.AccessInstanceVariableSuperClass";
		String toModule = "technology.direct.subclass.CallInstanceSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false); 
		toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false); 
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
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", true); 
		toModule = "technology.direct.subclass.CallInstanceSubClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", true); 
		toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome3 = areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", true); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true) || (outcome3 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void AccessInstanceVariable_SetArgumentValue(){
		String fromModule = "domain.direct.violating.AccessInstanceVariable_SetArgumentValue";
		String toModule = "technology.direct.dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Reference TypeOfUsedVariable", true));
	}

	@Test
	public void AccessObjectReferenceAsParameter(){
		String fromModule = "domain.direct.violating.AccessObjectReferenceAsParameter";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Reference TypeOfUsedVariable", false));
	}

	@Test
	public void AccessInstanceWithinAnonymousClass(){
		String fromModule = "domain.direct.violating.AccessInstanceWithinAnonymousClass";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessLocalVariable_ReadArgumentValue(){
		String fromModule = "domain.direct.violating.AccessLocalVariable_ReadArgumentValue";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessLocalVariable_SetArgumentValue(){
		String fromModule = "domain.direct.violating.AccessLocalVariable_SetArgumentValue";
		String toModule = "technology.direct.dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Reference TypeOfUsedVariable", true));
	}

	@Test
	public void AccessObjectReferenceWithinIfStatement(){
		String fromModule = "domain.direct.violating.AccessObjectReferenceWithinIfStatement";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Reference TypeOfUsedVariable", false));
	}

	// Annotation	
	@Test
	public void AnnotationDependency(){
		String fromModule = "domain.direct.violating.AnnotationDependency";
		String toModule = "technology.direct.dao.SettingsAnnotation";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Annotation");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "", false));
	}

	// Call
	@Test
	public void CallClassMethod(){
		String fromModule = "domain.direct.violating.CallClassMethod";
		String toModule = "technology.direct.dao.BadgesDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Class Method", false));
	}

	@Test
	public void CallConstructor(){
		String fromModule = "domain.direct.violating.CallConstructor";
		String toModule = "technology.direct.dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Constructor", false));
	}

	@Test
	public void CallConstructorInnerClass(){
		String fromModule = "domain.direct.violating.CallConstructorInnerClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Constructor", false));
	}

	@Test
	public void CallConstructorInnerClassDefault(){
		String fromModule = "domain.direct.violating.CallConstructorInnerClassDefault";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Constructor", false));
	}

	@Test
	public void CallConstructorInnerClassFromOtherInnerClass(){
		String fromModule = "technology.direct.dao.CallInstanceOuterClassDAO.TestConstructorCallOfInnerClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Constructor", false));
	}

	@Test
	public void CallConstructorLibraryClass(){
		String fromModule = "domain.direct.violating.CallConstructorLibraryClass";
		String toModule = "xLibraries.fi.foyt.foursquare.api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Library Method", false));
	}

	@Test
	public void CallConstructorSuper(){
		String fromModule = "domain.direct.violating.CallConstructorSuper";
		String toModule = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Constructor", false));	}

	@Test
	public void CallEnumeration(){
		String fromModule = "domain.direct.violating.CallEnumeration";
		String toModule = "technology.direct.dao.TipDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Enumeration Method", false));
	}

	@Test
	public void CallFromInnerClass(){ //To another inner class
		String fromModule = "domain.direct.violating.CallFromInnerClass.CallingInnerClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Method", false));
	}

	@Test
	public void CallInstance(){
		String fromModule = "domain.direct.violating.CallInstance";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Method", false));
	}

	@Test
	public void CallInstanceInnerClass(){
		String fromModule = "domain.direct.violating.CallInstanceInnerClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Method", false));
	}

	@Test
	public void CallInstanceInnerInterface(){
		String fromModule = "domain.direct.violating.CallInstanceInnerInterface";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Interface Method", false));
	}

	@Test
	public void CallInstanceInPlusExpression(){
		String fromModule = "domain.direct.violating.CallInstanceInPlusExpression";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Method", false));
	}

	@Test
	public void CallInstanceInterface(){
		String fromModule = "domain.direct.violating.CallInstanceInterface";
		String toModule = "technology.direct.dao.CallInstanceInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Interface Method", false));
	}

	@Test
	public void CallInstanceLibraryClass(){
		String fromModule = "domain.direct.violating.CallInstanceLibraryClass";
		String toModule = "xLibraries.fi.foyt.foursquare.api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Library Method", false));
	}

	@Test
	public void CallInstanceOfSuperOverridden(){
		String fromModule = "domain.direct.violating.CallInstanceOfSuperOverridden";
		String toModule = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));	}

	@Test
	public void CallInstanceSuperClass(){
		String fromModule = "domain.direct.violating.CallInstanceSuperClass";
		String toModule = "technology.direct.subclass.CallInstanceSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind, true); 
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
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome3 = areDependencyTypesDetected(fromModule, toModule, typesToFind, true); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome3 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void CallInstanceWithinAnonymousClass(){
		String fromModule = "domain.direct.violating.CallInstanceWithinAnonymousClass";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	// Declaration
	@Test
	public void DeclarationExceptionThrows(){
		String fromModule = "domain.direct.violating.DeclarationExceptionThrows";
		String toModule = "technology.direct.dao.StaticsException";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Exception", false));
	}

	@Test
	public void DeclarationInnerClass(){
		String fromModule = "domain.direct.Base";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false));
	}

	@Test
	public void DeclarationOuterClassByStaticNestedClass(){
		String fromModule = "technology.direct.dao.CallInstanceOuterClassDAO.StaticNestedClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false));
	}

	@Test
	public void DeclarationParameter(){
		String fromModule = "domain.direct.violating.DeclarationParameter";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Parameter", false));
	}

	@Test
	public void DeclarationReturnType(){
		String fromModule = "domain.direct.violating.DeclarationReturnType";
		String toModule = "technology.direct.dao.VenueDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Return Type", false));
	}

	@Test
	public void DeclarationTypeCast(){
		String fromModule = "domain.direct.violating.DeclarationTypeCast";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Type Cast", false));
	}

	@Test
	public void DeclarationTypeCastOfArgument(){
		String fromModule = "domain.direct.violating.DeclarationTypeCastOfArgument";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Type Cast", false));
	}

	@Test
	public void DeclarationVariableInstance(){
		String fromModule = "domain.direct.violating.DeclarationVariableInstance";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", false));
	}

	@Test
	public void DeclarationVariableStatic(){
		String fromModule = "domain.direct.violating.DeclarationVariableStatic";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Class Variable", false));
	}

	@Test
	public void DeclarationVariableLocal(){
		String fromModule = "domain.direct.violating.DeclarationVariableLocal";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Local Variable", false));
	}

	@Test
	public void DeclarationVariableLocal_Initialized(){
		String fromModule = "domain.direct.violating.DeclarationVariableLocal_Initialized";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Local Variable", false));
	}

	@Test
	public void DeclarationVariableWithinForStatement(){
		String fromModule = "domain.direct.violating.DeclarationVariableWithinForStatement";
		String toModule = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Local Variable", false));
	}

	// Import
	@Test
	public void ImportDependencyUnused(){
		String fromModule = "domain.direct.violating.ImportDependencyUnused";
		String toModule = "technology.direct.dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void ImportDependencyStatic(){
		String fromModule = "domain.direct.violating.ImportDependencyStatic";
		String toModule = "technology.direct.dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	// Inheritance
	@Test
	public void InheritanceExtends(){
		String fromModule = "domain.direct.violating.InheritanceExtends";
		String toModule = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Extends Class", false));	}

	@Test
	public void InheritanceExtendsAbstractClass(){
		String fromModule = "domain.direct.violating.InheritanceExtendsAbstractClass";
		String toModule = "technology.direct.dao.FriendsDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Extends Abstract Class", false));
	}

	@Test
	public void InheritanceExtendsFullPath(){
		String fromModule = "domain.direct.violating.InheritanceExtendsFullPath";
		String toModule = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Extends Class", false));	}

	@Test
	public void InheritanceImplementsInterface(){
		String fromModule = "domain.direct.violating.InheritanceImplementsInterface";
		String toModule = "technology.direct.dao.IMapDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Ïmplements Interface", false));
	}

	//TESTS
	//INDIRECT
	// Access
	@Test
	public void AccessInstanceVariableIndirect_MethodVar(){
		String fromModule = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_MethodVar";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVar(){
		String fromModule = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_VarVar";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVarToString(){
		String fromModule = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_VarVarToString";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_MethodVarVar(){
		String fromModule = "domain.indirect.violatingfrom.AccessInstanceVariableIndirectIndirect_MethodVarVar";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Variable", true));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_VarVarVar(){
		String fromModule = "domain.indirect.violatingfrom.AccessInstanceVariableIndirectIndirect_VarVarVar";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_SuperClass(){
		String fromModule = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_SuperClass";
		String toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_SuperSuperClass(){
		String fromModule = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_SuperSuperClass";
		String toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter_POI(){
		String fromModule = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsParameter_POI";
		String toModule = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter(){
		String fromModule = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsParameter";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide(){
		String fromModule = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide";
		String toModule = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Reference ReturnTypeUsedMethod", true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType(){
		String fromModule = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType";
		String toModule = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic(){
		String fromModule = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic";
		String toModule = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament_POI(){
		String fromModule = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_WithinIfStament_POI";
		String toModule = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament(){
		String fromModule = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_WithinIfStament";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_MethodVar(){
		String fromModule = "domain.indirect.violatingfrom.AccessStaticVariableIndirect_MethodVar";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVar(){
		String fromModule = "domain.indirect.violatingfrom.AccessStaticVariableIndirect_VarVar";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVarToString(){
		String fromModule = "domain.indirect.violatingfrom.AccessStaticVariableIndirect_VarVarToString";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_MethodVarVar(){
		String fromModule = "domain.indirect.violatingfrom.AccessStaticVariableIndirectIndirect_MethodVarVar";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_VarVarVar(){
		String fromModule = "domain.indirect.violatingfrom.AccessStaticVariableIndirectIndirect_VarVarVar";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	// Call
	@Test
	public void CallInstanceMethodIndirect_MethodMethod(){
		String fromModule = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_MethodMethod";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethodToString(){
		String fromModule = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_MethodMethodToString";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethod_ViaConstructor(){
		String fromModule = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_MethodMethodViaConstructor";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_StaticMethodInstanceMethod(){
		String fromModule = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_StaticMethodInstanceMethod";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperClass(){
		String fromModule = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_SuperClass";
		String toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperSuperClass(){
		String fromModule = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_SuperSuperClass";
		String toModule = "technology.direct.dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_VarMethod(){
		String fromModule = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_VarMethod";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_MethodVarMethod(){
		String fromModule = "domain.indirect.violatingfrom.CallInstanceMethodIndirectIndirect_MethodVarMethod";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Method", true));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_VarVarMethod(){
		String fromModule = "domain.indirect.violatingfrom.CallInstanceMethodIndirectIndirect_VarVarMethod";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Instance Method", true));
	}

	@Test
	public void CallStaticMethodIndirect_MethodStaticMethod(){
		String fromModule = "domain.indirect.violatingfrom.CallStaticMethodIndirect_MethodStaticMethod";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Class Method", true));
	}

	@Test
	public void CallStaticMethodIndirect_VarStaticMethod(){
		String fromModule = "domain.indirect.violatingfrom.CallStaticMethodIndirect_VarStaticMethod";
		String toModule = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Class Method", true));
	}

	// Inheritance
	@Test
	public void InheritanceExtendsExtendsIndirect(){
		String fromModule = "domain.indirect.violatingfrom.InheritanceExtendsExtendsIndirect";
		String toModule = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Extends Abstract Class", true));
	}

	@Test
	public void InheritanceExtendsImplementsIndirect(){
		String fromModule = "domain.indirect.violatingfrom.InheritanceExtendsImplementsIndirect";
		String toModule = "domain.indirect.indirectto.IPreferences";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Ïmplements Interface", true));
	}

	@Test
	public void InheritanceExtendsLibraryClass(){
		String fromModule = "domain.direct.violating.InheritanceExtendsLibraryClass";
		String toModule = "xLibraries.fi.foyt.foursquare.api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "From Library Class", false));
	}

	@Test
	public void InheritanceFromInnerClass(){
		String fromModule = "domain.direct.violating.CallFromInnerClass.CallingInnerClass";
		String toModule = "domain.direct.Base";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Extends Class", false));
	}

	@Test
	public void InheritanceImplementsExtendsIndirect(){
		String fromModule = "domain.indirect.violatingfrom.InheritanceImplementsExtendsIndirect";
		String toModule = "domain.indirect.indirectto.IPreferences";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, "Ïmplements Interface", true));
	}

	// Violations: Test if all found violating dependencies are reported as violations
	@Test
	public void ViolationsDirect_CompareToNumberOfDirectDependencies_violating_dao(){
		String fromModule = "domain.direct.violating";
		String toModule = "technology.direct";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromModule, toModule).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromModule, toModule).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}

	@Test
	public void ViolationsDirect_CompareToNumberOfDirectDependencies_violating_foursquare(){
		String fromModule = "domain.direct.violating";
		String toModule = "fi.foyt.foursquare.api";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromModule, toModule).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromModule, toModule).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}
	
	@Test
	public void ViolationsInDirect_CompareToNumberOfInDirectDependencies_violatingfrom_indirectto(){
		String fromModule = "domain.indirect.violatingfrom";
		String toModule = "domain.indirect.indirectto";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromModule, toModule).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromModule, toModule).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}

	@Test
	public void ViolationsInDirect_CompareToNumberOfInDirectDependencies_violating_dao(){
		String fromModule = "domain.direct.violating";
		String toModule = "technology.direct.dao";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromModule, toModule).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromModule, toModule).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}
	
	//
	//private helpers
	//
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(Java_AccuracyTestDependencyDetection.class);
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

	private static void checkConformance() {
		ServiceProvider.getInstance().getControlService().setValidate(true);
		logger.info(new Date().toString() + " CheckConformanceTask is Starting: IValidateService.checkConformance()" );
		ServiceProvider.getInstance().getValidateService().getCategories();
		ServiceProvider.getInstance().getValidateService().checkConformance();
		ServiceProvider.getInstance().getControlService().setValidate(false);
		logger.info(new Date().toString() + " CheckConformanceTask sets state Validating to false" );
	}

	private boolean areDependencyTypesDetected(String moduleFrom, String moduleTo, ArrayList<String> dependencyTypes, boolean isIndirect) {
		return areDependencyTypesDetected(moduleFrom, moduleTo, dependencyTypes, "", isIndirect);
	}

	private boolean areDependencyTypesDetected(String moduleFrom, String moduleTo, ArrayList<String> dependencyTypes, String subType, boolean isIndirect) {
		boolean dependencyTypesDetected = false;
		TreeMap<String, Boolean> foundDependencyTypes = new TreeMap<String, Boolean>();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] foundDependencies = analyseService.getDependenciesFromTo(moduleFrom, moduleTo);
		int numberOfDependencies = foundDependencies.length;
		for (String dependencyType : dependencyTypes) {
			boolean found = false;
			for (int i=0 ; i < numberOfDependencies; i++){
				if (foundDependencies[i].type.equals(dependencyType) && (foundDependencies[i].isIndirect) == isIndirect) {
					if (!subType.equals("")) {
						if (foundDependencies[i].subType.equals(subType)) {
							found = true;	
						}
					} else {
						found = true;
					}
				}
			}
			foundDependencyTypes.put(dependencyType,found);
		}
		if (!foundDependencyTypes.containsValue(false)){
			dependencyTypesDetected = true;
		}
		return dependencyTypesDetected;
	}

}