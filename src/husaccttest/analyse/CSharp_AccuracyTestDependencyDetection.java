package husaccttest.analyse;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
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

public class CSharp_AccuracyTestDependencyDetection {
	private static ControlServiceImpl controlService;
	private static MainController mainController;
	private static WorkspaceController workspaceController;
	private static Logger logger;
	private static IAnalyseService analyseService = null;
	private static IValidateService validateService = null;
	private static String path;
	private static String language = "C#";

	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format("Starting CSharp Accuracy Test"));
			createWorkspace("CSharpAccuracyTest");
			
			// Create Application
			path = new File(TestResourceFinder.findSourceCodeDirectory("csharp", "benchmark")).getAbsolutePath();
			ArrayList<ProjectDTO> projects = createProjectDTOs();
			ServiceProvider.getInstance().getDefineService().createApplication(language +" test", projects, "1.0");
			
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
/*			checkConformance();
			//checkConformance() starts a different Thread, and needs some time
			boolean isValidating = true;
			controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
			mainController = controlService.getMainController();
			while(isValidating){
				try {
					Thread.sleep((long)10);
				} catch (InterruptedException e) {}
				isValidating = mainController.getStateController().isValidating();
			} */
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
		String fromModule = "Domain.Direct.Violating.AccessClassVariable";
		String toModule = "Technology.Direct.Dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessClassVariableConstant(){
		String fromModule = "Domain.Direct.Violating.AccessClassVariableConstant";
		String toModule = "Technology.Direct.Dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessClassVariableInterface(){
		String fromModule = "Domain.Direct.Violating.AccessClassVariableInterface";
		String toModule = "Technology.Direct.Dao.ISierraDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessEnumeration(){
		String fromModule = "Domain.Direct.Violating.AccessEnumeration";
		String toModule = "Technology.Direct.Dao.TipDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessEnumerationInner(){
		String fromModule = "domain.direct.violating.AccessInnerEnumeration";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.InnerEnumeration";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessInstanceVariableRead(){
		String fromModule = "Domain.Direct.Violating.AccessInstanceVariableRead";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessInstanceVariableWrite(){
		String fromModule = "Domain.Direct.Violating.AccessInstanceVariableWrite";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessInstanceVariableConstant(){
		String fromModule = "Domain.Direct.Violating.AccessInstanceVariableConstant";
		String toModule = "Technology.Direct.Dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessInstanceVariableSuperClass(){
		String fromModule = "Domain.Direct.Violating.AccessInstanceVariableSuperClass";
		String toModule = "Technology.Direct.Subclass.CallInstanceSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		toModule = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void AccessInstanceVariableSuperSuperClass(){
		String fromModule = "Domain.Direct.Violating.AccessInstanceVariableSuperSuperClass";
		String toModule = "Technology.Direct.Subclass.CallInstanceSubSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		toModule = "Technology.Direct.Subclass.CallInstanceSubClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		toModule = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		boolean outcome3 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true) || (outcome3 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void AccessFromInnerClass(){
		String fromModule = "domain.direct.violating.CallFromInnerClass.CallingInnerClass";
		String toModule = "domain.direct.Base";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

@Test
	public void AccessInstanceVariable_SetArgumentValue(){
		String fromModule = "Domain.Direct.Violating.AccessInstanceVariable_SetArgumentValue";
		String toModule = "Technology.Direct.Dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceAsParameter(){
		String fromModule = "Domain.Direct.Violating.AccessObjectReferenceAsParameter";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessLocalVariable_ReadArgumentValue(){
		String fromModule = "Domain.Direct.Violating.AccessLocalVariable_ReadArgumentValue";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessLocalVariable_SetArgumentValue(){
		String fromModule = "Domain.Direct.Violating.AccessLocalVariable_SetArgumentValue";
		String toModule = "Technology.Direct.Dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceWithinIfStatement(){
		String fromModule = "Domain.Direct.Violating.AccessObjectReferenceWithinIfStatement";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	// Annotation	
	@Test
	public void AnnotationDependency(){ // Asserts False, since annotations are not supported in C#
		String fromModule = "Domain.Direct.Violating.AnnotationDependency";
		String toModule = "Technology.Direct.Dao.SettingsAnnotation";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Annotation");
		Assert.assertFalse(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	// Call
	@Test
	public void CallClassMethod(){
		String fromModule = "Domain.Direct.Violating.CallClassMethod";
		String toModule = "Technology.Direct.Dao.BadgesDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallConstructor(){
		String fromModule = "Domain.Direct.Violating.CallConstructor";
		String toModule = "Technology.Direct.Dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallConstructorInnerClass(){
		String fromModule = "domain.direct.violating.CallConstructorInnerClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallConstructorLibraryClass(){ // Asserts False, since HUSACCT is not able to detect invocations on library classes.
		String fromModule = "Domain.Direct.Violating.CallConstructorLibraryClass";
		String toModule = "FI.Foyt.Foursquare.Api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertFalse(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallFromInnerClass(){
		String fromModule = "domain.direct.violating.CallFromInnerClass.CallingInnerClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallInstance(){
		String fromModule = "Domain.Direct.Violating.CallInstance";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallInstanceInnerClass(){
		String fromModule = "Domain.Direct.Violating.CallInstanceInnerClass";
		String toModule = "Technology.Direct.Dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallInstanceInnerInterface(){
		String fromModule = "domain.direct.violating.CallInstanceInnerInterface";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallInstanceInterface(){
		String fromModule = "Domain.Direct.Violating.CallInstanceInterface";
		String toModule = "Technology.Direct.Dao.CallInstanceInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallInstanceLibraryClass(){ // Asserts False, since HUSACCT is not able to detect invocations on library classes.
		String fromModule = "Domain.Direct.Violating.CallInstanceLibraryClass";
		String toModule = "FI.Foyt.Foursquare.Api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertFalse(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallInstanceSuperClass(){
		String fromModule = "Domain.Direct.Violating.CallInstanceSuperClass";
		String toModule = "Technology.Direct.Subclass.CallInstanceSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		toModule = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void CallInstanceSuperSuperClass(){
		String fromModule = "Domain.Direct.Violating.CallInstanceSuperSuperClass";
		String toModule = "Technology.Direct.Subclass.CallInstanceSubSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		boolean outcome1 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		toModule = "Technology.Direct.Subclass.CallInstanceSubClassDOA";
		boolean outcome2 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		toModule = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		boolean outcome3 = areDependencyTypesDetected(fromModule, toModule, typesToFind, false); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true) || (outcome3 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	// Declaration
	@Test
	public void DeclarationExceptionThrows(){
		String fromModule = "Domain.Direct.Violating.DeclarationExceptionThrows";
		String toModule = "Technology.Direct.Dao.StaticsException";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void DeclarationInnerClass(){
		String fromModule = "domain.direct.Base";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void DeclarationOuterClassByInnerClass(){
		String fromModule = "technology.direct.dao.CallInstanceOuterClassDAO.StaticNestedClass";
		String toModule = "technology.direct.dao.CallInstanceOuterClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void DeclarationParameter(){
		String fromModule = "Domain.Direct.Violating.DeclarationParameter";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void DeclarationReturnType(){
		String fromModule = "Domain.Direct.Violating.DeclarationReturnType";
		String toModule = "Technology.Direct.Dao.VenueDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void DeclarationTypeCast(){
		String fromModule = "Domain.Direct.Violating.DeclarationTypeCast";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void DeclarationTypeCastOfArgument(){
		String fromModule = "Domain.Direct.Violating.DeclarationTypeCastOfArgument";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void DeclarationVariableInstance(){
		String fromModule = "Domain.Direct.Violating.DeclarationVariableInstance";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void DeclarationVariableStatic(){
		String fromModule = "Domain.Direct.Violating.DeclarationVariableStatic";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void DeclarationVariableLocal(){
		String fromModule = "Domain.Direct.Violating.DeclarationVariableLocal";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void DeclarationVariableLocal_Initialized(){
		String fromModule = "Domain.Direct.Violating.DeclarationVariableLocal_Initialized";
		String toModule = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	// Import
	@Test
	public void ImportDependencyUnused(){
		String fromModule = "Domain.Direct.Violating.ImportDependencyUnused";
		String toModule = "Technology.Direct.Dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	// Inheritance
	@Test
	public void InheritanceExtends(){
		String fromModule = "Domain.Direct.Violating.InheritanceExtends";
		String toModule = "Technology.Direct.Dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));	}

	@Test
	public void InheritanceExtendsAbstractClass(){
		String fromModule = "Domain.Direct.Violating.InheritanceExtendsAbstractClass";
		String toModule = "Technology.Direct.Dao.FriendsDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void InheritanceExtendsFullPath(){
		String fromModule = "domain.direct.violating.InheritanceExtendsFullPath";
		String toModule = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));	}

	@Test
	public void InheritanceImplementsInterface(){
		String fromModule = "Domain.Direct.Violating.InheritanceImplementsInterface";
		String toModule = "Technology.Direct.Dao.IMapDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	//TESTS
	//INDIRECT
	// Access
	@Test
	public void AccessInstanceVariableIndirect_MethodVar(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirect_MethodVar";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVar(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirect_VarVar";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVarToString(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirect_VarVarToString";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_MethodVarVar(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirectIndirect_MethodVarVar";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_VarVarVar(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirectIndirect_VarVarVar";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_SuperClass(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirect_SuperClass";
		String toModule = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_SuperSuperClass(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirect_SuperSuperClass";
		String toModule = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter_POI(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_AsParameter_POI";
		String toModule = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide";
		String toModule = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType";
		String toModule = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic";
		String toModule = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_AsParameter";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament_POI(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_WithinIfStament_POI";
		String toModule = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_WithinIfStament";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_MethodVar(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessStaticVariableIndirect_MethodVar";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVar(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessStaticVariableIndirect_VarVar";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVarToString(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessStaticVariableIndirect_VarVarToString";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_MethodVarVar(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessStaticVariableIndirectIndirect_MethodVarVar";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_VarVarVar(){
		String fromModule = "Domain.Indirect.ViolatingFrom.AccessStaticVariableIndirectIndirect_VarVarVar";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	// Call
	@Test
	public void CallInstanceMethodIndirect_MethodMethod(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_MethodMethod";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethodToString(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_MethodMethodToString";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethod_ViaConstructor(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_MethodMethod_ViaConstructor";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_StaticMethodInstanceMethod(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_StaticMethodInstanceMethod";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperClass(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_SuperClass";
		String toModule = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperSuperClass(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_SuperSuperClass";
		String toModule = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_VarMethod(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_VarMethod";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_MethodVarMethod(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirectIndirect_MethodVarMethod";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_VarVarMethod(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirectIndirect_VarVarMethod";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void CallStaticMethodIndirect_MethodStaticMethod(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallStaticMethodIndirect_MethodStaticMethod";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void CallStaticMethodIndirect_VarStaticMethod(){
		String fromModule = "Domain.Indirect.ViolatingFrom.CallStaticMethodIndirect_VarStaticMethod";
		String toModule = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	// Inheritance
	@Test
	public void InheritanceExtendsExtendsIndirect(){
		String fromModule = "Domain.Indirect.ViolatingFrom.InheritanceExtendsExtendsIndirect";
		String toModule = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void InheritanceExtendsImplementsIndirect(){
		String fromModule = "Domain.Indirect.ViolatingFrom.InheritanceExtendsImplementsIndirect";
		String toModule = "Domain.Indirect.IndirectTo.IPreferences";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	@Test
	public void InheritanceFromInnerClass(){
		String fromModule = "domain.direct.violating.CallFromInnerClass.CallingInnerClass";
		String toModule = "domain.direct.Base";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, false));
	}

	@Test
	public void InheritanceImplementsExtendsIndirect(){
		String fromModule = "Domain.Indirect.ViolatingFrom.InheritanceImplementsExtendsIndirect";
		String toModule = "Domain.Indirect.IndirectTo.IPreferences";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromModule, toModule, typesToFind, true));
	}

	// Violations: Test if all found violating dependencies are reported as violations
/*	@Test
	public void ViolationsDirect_CompareToNumberOfDirectDependencies_violating_dao(){
		String fromModule = "Domain.Direct.Violating";
		String toModule = "technology.direct";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromModule, toModule).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromModule, toModule).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}

	@Test
	public void ViolationsDirect_CompareToNumberOfDirectDependencies_violating_foursquare(){
		String fromModule = "Domain.Direct.Violating";
		String toModule = "Fi.Foyt.Foursquare.Api";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromModule, toModule).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromModule, toModule).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}
	
	@Test
	public void ViolationsInDirect_CompareToNumberOfInDirectDependencies_violatingfrom_indirectto(){
		String fromModule = "Domain.Indirect.ViolatingFrom";
		String toModule = "Domain.Indirect.IndirectTo";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromModule, toModule).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromModule, toModule).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}

	@Test
	public void ViolationsInDirect_CompareToNumberOfInDirectDependencies_violatingfrom_dao(){
		String fromModule = "Domain.Indirect.ViolatingFrom";
		String toModule = "Technology.Direct.Dao";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromModule, toModule).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromModule, toModule).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}
*/
	
	//
	//private helpers
	//
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(CSharp_AccuracyTestDependencyDetection.class);
	}
	
	private static ArrayList<ProjectDTO> createProjectDTOs(){
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		ArrayList<String> paths = new ArrayList<String>();
		paths.add(path);
		ArrayList<AnalysedModuleDTO> analysedModules = new ArrayList<AnalysedModuleDTO>();
		ProjectDTO project = new ProjectDTO("C# Accuracy Test", paths, language, "version0", "for testing purposes", analysedModules);
		projects.add(project);
		return projects;
	}

	private static void createWorkspace(String name) {
		controlService = (ControlServiceImpl) ServiceProvider.getInstance().getControlService();
		mainController = controlService.getMainController();
		workspaceController = mainController.getWorkspaceController();
		workspaceController.createWorkspace(name);
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
		boolean dependencyTypesDetected = false;
		TreeMap<String, Boolean> foundDependencyTypes = new TreeMap<String, Boolean>();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] foundDependencies = analyseService.getDependenciesFromTo(moduleFrom, moduleTo);
		int numberOfDependencies = foundDependencies.length;
		for (String dependencyType : dependencyTypes) {
			boolean found = false;
			for (int i=0 ; i < numberOfDependencies; i++){
				if (foundDependencies[i].type.equals(dependencyType) && (foundDependencies[i].isIndirect) == isIndirect) {
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
}