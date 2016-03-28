package husaccttest.analyse;

import husacct.ServiceProvider;
import husacct.analyse.IAnalyseService;
import husacct.common.dto.DependencyDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.dto.UmlLinkDTO;
import husacct.common.enums.DependencySubTypes;
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
import java.util.HashSet;
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
		String fromClass = "Domain.Direct.Violating.AccessClassVariable";
		String toClass = "Technology.Direct.Dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void AccessClassVariableConstant(){
		String fromClass = "Domain.Direct.Violating.AccessClassVariableConstant";
		String toClass = "Technology.Direct.Dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void AccessClassVariableInterface(){
		String fromClass = "Domain.Direct.Violating.AccessClassVariableInterface";
		String toClass = "Technology.Direct.Dao.ISierraDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void AccessEnumeration(){
		String fromClass = "Domain.Direct.Violating.AccessEnumeration";
		String toClass = "Technology.Direct.Dao.TipDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void AccessEnumerationInner(){
		String fromClass = "Domain.Direct.Violating.AccessInnerEnumeration";
		String toClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.InnerEnumeration";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void AccessInstanceVariableRead(){
		String fromClass = "Domain.Direct.Violating.AccessInstanceVariableRead";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void AccessInstanceVariableWrite(){
		String fromClass = "Domain.Direct.Violating.AccessInstanceVariableWrite";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void AccessInstanceVariableConstant(){
		String fromClass = "Domain.Direct.Violating.AccessInstanceVariableConstant";
		String toClass = "Technology.Direct.Dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void AccessInstanceVariableSuperClass(){
		String fromClass = "Domain.Direct.Violating.AccessInstanceVariableSuperClass";
		String toClass = "Technology.Direct.Subclass.CallInstanceSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		boolean outcome1 = areDependencyTypesDetected(fromClass, toClass, typesToFind, false); 
		toClass = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromClass, toClass, typesToFind, true); 
		boolean totalOutcome = false;
		if ((outcome1 == true) && (outcome2 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void AccessInstanceVariableSuperSuperClass(){
		String fromClass = "Domain.Direct.Violating.AccessInstanceVariableSuperSuperClass";
		String toClass = "Technology.Direct.Subclass.CallInstanceSubSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		boolean outcome1 = areDependencyTypesDetected(fromClass, toClass, typesToFind, false); 
		toClass = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromClass, toClass, typesToFind, true); 
		boolean totalOutcome = false;
		if (outcome1 && outcome2) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void AccessFromInnerClass(){
		String fromClass = "Domain.Direct.Violating.CallFromInnerClass.CallingInnerClass";
		String toClass = "Domain.Direct.Base";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

@Test
	public void AccessInstanceVariable_SetArgumentValue(){
		String fromClass = "Domain.Direct.Violating.AccessInstanceVariable_SetArgumentValue";
		String toClass = "Technology.Direct.Dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceAsParameter(){
		String fromClass = "Domain.Direct.Violating.AccessObjectReferenceAsParameter";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void AccessLocalVariable_ReadArgumentValue(){
		String fromClass = "Domain.Direct.Violating.AccessLocalVariable_ReadArgumentValue";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void AccessLocalVariable_SetArgumentValue(){
		String fromClass = "Domain.Direct.Violating.AccessLocalVariable_SetArgumentValue";
		String toClass = "Technology.Direct.Dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceWithinIfStatement(){
		String fromClass = "Domain.Direct.Violating.AccessObjectReferenceWithinIfStatement";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	// Annotation	
	@Test
	public void AnnotationDependency(){ // Asserts False, since annotations are not supported in C#
		String fromClass = "Domain.Direct.Violating.AnnotationDependency";
		String toClass = "Technology.Direct.Dao.SettingsAnnotation";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Annotation");
		Assert.assertFalse(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	// Call
	@Test
	public void CallClassMethod(){
		String fromClass = "Domain.Direct.Violating.CallClassMethod";
		String toClass = "Technology.Direct.Dao.BadgesDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallConstructor(){
		String fromClass = "Domain.Direct.Violating.CallConstructor";
		String toClass = "Technology.Direct.Dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallConstructor_GenericType_MultipleTypeParameters(){
		String fromClass = "Domain.Direct.Violating.CallConstructor_GenericType_MultipleTypeParameters";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.ProfileDAO", typesToFind, DependencySubTypes.DECL_TYPE_PARAMETER.toString(), false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.UserDAO", typesToFind, DependencySubTypes.DECL_TYPE_PARAMETER.toString(), false));
	}

	@Test
	public void CallConstructorInnerClass(){
		String fromClass = "Domain.Direct.Violating.CallConstructorInnerClass";
		String toClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallConstructorLibraryClass(){ // Asserts False, since HUSACCT is not able to detect invocations on library classes.
		String fromClass = "Domain.Direct.Violating.CallConstructorLibraryClass";
		String toClass = "FI.Foyt.Foursquare.Api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertFalse(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallConstructorSuper(){
		String fromClass = "Domain.Direct.Violating.CallConstructorSuper";
		String toClass = "Technology.Direct.Dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));	}

	@Test
	public void CallFromInnerClass(){
		String fromClass = "Domain.Direct.Violating.CallFromInnerClass.CallingInnerClass";
		String toClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallInstance(){
		String fromClass = "Domain.Direct.Violating.CallInstance";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallInstanceGeneric(){
		String fromClass = "Domain.Direct.Violating.CallInstanceGeneric";
		String toClass = "Technology.Direct.Dao.ProfileDAO<p1, p2>";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallInstanceInnerClass(){
		String fromClass = "Domain.Direct.Violating.CallInstanceInnerClass";
		String toClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallConstructorInnerClassDefault(){
		String fromClass = "Domain.Direct.Violating.CallConstructorInnerClassDefault";
		String toClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallConstructorInnerClassFromOtherInnerClass(){
		String fromClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.TestConstructorCallOfInnerClass";
		String toClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallInstanceInnerInterface(){
		String fromClass = "Domain.Direct.Violating.CallInstanceInnerInterface";
		String toClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.CallInstanceInnerInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallInstanceInterface(){
		String fromClass = "Domain.Direct.Violating.CallInstanceInterface";
		String toClass = "Technology.Direct.Dao.CallInstanceInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallInstanceInterfaceGenericInterface(){
		String fromClass = "Domain.Direct.Violating.CallInstanceInterfaceGenericInterface";
		String toClass = "Technology.Direct.Dao.CallInstanceInterfaceDAO<p1>";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallInstanceLibraryClass(){
		String fromClass = "Domain.Direct.Violating.CallInstanceLibraryClass";
		String toClass = "xLibraries.FI.Foyt.Foursquare.Api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertFalse(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
		typesToFind.clear();
		typesToFind.add("Declaration");
		Assert.assertFalse(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
		typesToFind.clear();
		typesToFind.add("Inheritance");
		toClass = "Domain.Direct.Base";
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallInstanceOfSuperOverridden(){
		String fromClass = "Domain.Direct.Violating.CallInstanceOfSuperOverridden";
		String toClass = "Technology.Direct.Dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));	}

	@Test
	public void CallInstanceSuperClass(){
		String fromClass = "Domain.Direct.Violating.CallInstanceSuperClass";
		String toClass = "Technology.Direct.Subclass.CallInstanceSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		boolean outcome1 = areDependencyTypesDetected(fromClass, toClass, typesToFind, false); 
		toClass = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromClass, toClass, typesToFind, false); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void CallInstanceSuperSuperClass(){
		String fromClass = "Domain.Direct.Violating.CallInstanceSuperSuperClass";
		String toClass = "Technology.Direct.Subclass.CallInstanceSubSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		boolean outcome1 = areDependencyTypesDetected(fromClass, toClass, typesToFind, false); 
		toClass = "Technology.Direct.Subclass.CallInstanceSubClassDOA";
		boolean outcome2 = areDependencyTypesDetected(fromClass, toClass, typesToFind, false); 
		toClass = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		boolean outcome3 = areDependencyTypesDetected(fromClass, toClass, typesToFind, false); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true) || (outcome3 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	// Declaration
	@Test
	public void DeclarationExceptionThrows(){
		String fromClass = "Domain.Direct.Violating.DeclarationExceptionThrows";
		String toClass = "Technology.Direct.Dao.StaticsException";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationInnerClass(){
		String fromClass = "Domain.Direct.Base";
		String toClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationOuterClassByInnerClass(){
		String fromClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.StaticNestedClass";
		String toClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationParameter(){
		String fromClass = "Domain.Direct.Violating.DeclarationParameter";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationParameter_GenericType_OneTypeParameter(){
		String fromClass = "Domain.Direct.Violating.DeclarationParameter_GenericType_OneTypeParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.ProfileDAO", typesToFind, "Parameter", false));
	}

	@Test
	public void DeclarationReturnType(){
		String fromClass = "Domain.Direct.Violating.DeclarationReturnType";
		String toClass = "Technology.Direct.Dao.VenueDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationReturnType_GenericType_OneTypeParameter(){
		String fromClass = "Domain.Direct.Violating.DeclarationReturnType_GenericType_OneTypeParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.ProfileDAO", typesToFind, DependencySubTypes.DECL_RETURN_TYPE.toString(), false));
	}

	@Test
	public void DeclarationTypeCast(){
		String fromClass = "Domain.Direct.Violating.DeclarationTypeCast";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationTypeCastToInnerClass(){
		String fromClass = "Domain.Direct.Violating.DeclarationTypeCastToInnerClass";
		String toClass = "Technology.Direct.Dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationTypeCastOfArgument(){
		String fromClass = "Domain.Direct.Violating.DeclarationTypeCastOfArgument";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationVariableInstance(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationVariableInstance_GenericType_OneTypeParameter(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.AccountDAO", typesToFind, "Instance Variable", false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.BadgesDAO", typesToFind, "Instance Variable", false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.CheckInDAO", typesToFind, "Instance Variable", false));
	}

	@Test
	public void DeclarationVariableInstance_GenericType_MultipleTypeParameters(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance_GenericType_MultipleTypeParameters";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.ProfileDAO", typesToFind, "Instance Variable", false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.FriendsDAO", typesToFind, "Instance Variable", false));
	}

	@Test
	public void DeclarationVariableInstance_GenericType_MultipleTypeParameters_Complex(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance_GenericType_MultipleTypeParameters_Complex";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.ProfileDAO", typesToFind, "Instance Variable", false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "Technology.Direct.Dao.FriendsDAO", typesToFind, "Instance Variable", false));
	}

	@Test
	public void DeclarationVariableStatic(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableStatic";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationVariableLocal(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableLocal";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationVariableLocal_Initialized(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableLocal_Initialized";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void DeclarationVariableWithinForStatement(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableWithinForStatement";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	// Import
	@Test
	public void ImportDependencyUnused(){
		String fromClass = "Domain.Direct.Violating.ImportDependencyUnused";
		String toClass = "Technology.Direct.Dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	// Inheritance
	@Test
	public void InheritanceExtends(){
		String fromClass = "Domain.Direct.Violating.InheritanceExtends";
		String toClass = "Technology.Direct.Dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));	
		toClass = "Technology.Direct.Dao.IMapDAO";
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void InheritanceExtendsAbstractClass(){
		String fromClass = "Domain.Direct.Violating.InheritanceExtendsAbstractClass";
		String toClass = "Technology.Direct.Dao.FriendsDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void InheritanceExtendsFullPath(){
		String fromClass = "Domain.Direct.Violating.InheritanceExtendsFullPath";
		String toClass = "Technology.Direct.Dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));	}

	@Test
	public void InheritanceImplementsInterface(){
		String fromClass = "Domain.Direct.Violating.InheritanceImplementsInterface";
		String toClass = "Technology.Direct.Dao.IMapDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	//TESTS
	//INDIRECT
	// Access
	@Test
	public void AccessInstanceVariableIndirect_MethodVar(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirect_MethodVar";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVar(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirect_VarVar";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVarToString(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirect_VarVarToString";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_MethodVarVar(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirectIndirect_MethodVarVar";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_VarVarVar(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirectIndirect_VarVarVar";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_SuperClass(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirect_SuperClass";
		String toClass = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_SuperSuperClass(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessInstanceVariableIndirect_SuperSuperClass";
		String toClass = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter_POI(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_AsParameter_POI";
		String toClass = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide";
		String toClass = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType";
		String toClass = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic";
		String toClass = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_AsParameter";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament_POI(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_WithinIfStament_POI";
		String toClass = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessObjectReferenceIndirect_WithinIfStament";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_MethodVar(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessStaticVariableIndirect_MethodVar";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVar(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessStaticVariableIndirect_VarVar";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVarToString(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessStaticVariableIndirect_VarVarToString";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_MethodVarVar(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessStaticVariableIndirectIndirect_MethodVarVar";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_VarVarVar(){
		String fromClass = "Domain.Indirect.ViolatingFrom.AccessStaticVariableIndirectIndirect_VarVarVar";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	// Call
	@Test
	public void CallInstanceMethodIndirect_MethodMethod(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_MethodMethod";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethodToString(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_MethodMethodToString";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethod_ViaConstructor(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_MethodMethod_ViaConstructor";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_StaticMethodInstanceMethod(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_StaticMethodInstanceMethod";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperClass(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_SuperClass";
		String toClass = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperSuperClass(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_SuperSuperClass";
		String toClass = "Technology.Direct.Dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_VarMethod(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirect_VarMethod";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_MethodVarMethod(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirectIndirect_MethodVarMethod";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_VarVarMethod(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallInstanceMethodIndirectIndirect_VarVarMethod";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallStaticMethodIndirect_MethodStaticMethod(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallStaticMethodIndirect_MethodStaticMethod";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void CallStaticMethodIndirect_VarStaticMethod(){
		String fromClass = "Domain.Indirect.ViolatingFrom.CallStaticMethodIndirect_VarStaticMethod";
		String toClass = "Domain.Indirect.IndirectTo.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	// Inheritance
	@Test
	public void InheritanceExtendsExtendsIndirect(){
		String fromClass = "Domain.Indirect.ViolatingFrom.InheritanceExtendsExtendsIndirect";
		String toClass = "Domain.Indirect.IndirectTo.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void InheritanceExtendsImplementsIndirect(){
		String fromClass = "Domain.Indirect.ViolatingFrom.InheritanceExtendsImplementsIndirect";
		String toClass = "Domain.Indirect.IndirectTo.IPreferences";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void InheritanceFromInnerClass(){
		String fromClass = "Domain.Direct.Violating.CallFromInnerClass.CallingInnerClass";
		String toClass = "Domain.Direct.Base";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void InheritanceImplementsExtendsIndirect(){
		String fromClass = "Domain.Indirect.ViolatingFrom.InheritanceImplementsExtendsIndirect";
		String toClass = "Domain.Indirect.IndirectTo.IPreferences";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	// Violations: Test if all found violating dependencies are reported as violations
/*	@Test
	public void ViolationsDirect_CompareToNumberOfDirectDependencies_violating_dao(){
		String fromClass = "Domain.Direct.Violating";
		String toClass = "technology.direct";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromClass, toClass).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromClass, toClass).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}

	@Test
	public void ViolationsDirect_CompareToNumberOfDirectDependencies_violating_foursquare(){
		String fromClass = "Domain.Direct.Violating";
		String toClass = "Fi.Foyt.Foursquare.Api";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromClass, toClass).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromClass, toClass).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}
	
	@Test
	public void ViolationsInDirect_CompareToNumberOfInDirectDependencies_violatingfrom_indirectto(){
		String fromClass = "Domain.Indirect.ViolatingFrom";
		String toClass = "Domain.Indirect.IndirectTo";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromClass, toClass).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromClass, toClass).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}

	@Test
	public void ViolationsInDirect_CompareToNumberOfInDirectDependencies_violatingfrom_dao(){
		String fromClass = "Domain.Indirect.ViolatingFrom";
		String toClass = "Technology.Direct.Dao";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependencies(fromClass, toClass).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromClass, toClass).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}
*/

	
	// UmlLinkTypes: Positive
	@Test
	public void UmlLinkType_InstanceVariableDeclaration_NotComposite(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		String fromAttribute = "pdao";
		boolean isComposite = false;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_Composite_Array(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		String toClass = "Technology.Direct.Dao.AccountDAO";
		String fromAttribute = "aDao";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_Composite_List(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		String toClass = "Technology.Direct.Dao.BadgesDAO";
		String fromAttribute = "bDao";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_Composite_HashSet(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		String toClass = "Technology.Direct.Dao.CheckInDAO";
		String fromAttribute = "cDao";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_VariableInstance_GenericType_MultipleTypeParameters(){ 
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance_GenericType_MultipleTypeParameters";
		String toClass = "Technology.Direct.Dao.FriendsDAO";
		String fromAttribute = "dictionary";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_MultipleAttributesOfTheSameTypeAtTheSameLine(){
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance_MultipleAttributesAtTheSameLine";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		String fromAttribute = "p1Dao";
		boolean isComposite = false;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
		fromAttribute = "p2Dao";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
		fromAttribute = "p3Dao";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_Inherits(){
		String fromClass = "Domain.Direct.Violating.InheritanceExtends";
		String toClass = "Technology.Direct.Dao.HistoryDAO";
		String fromAttribute = "";
		boolean isComposite = false;
		String typeToFind = "Inherits";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}
	
	@Test
	public void UmlLinkType_Implements(){
		String fromClass = "Domain.Direct.Violating.InheritanceImplementsInterface";
		String toClass = "Technology.Direct.Dao.IMapDAO";
		String fromAttribute = "";
		boolean isComposite = false;
		String typeToFind = "Implements";
		Assert.assertTrue(isUmlLinkDetected(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	
	// UmlLinkTypes: Negative
	@Test
	public void UmlLinkType_NotFromClassVariable(){ 
		String fromClass = "Domain.Direct.Violating.DeclarationVariableStatic";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		Assert.assertTrue(isUmlLinkNotDetected(fromClass, toClass));
	}

	@Test
	public void UmlLinkType_NotFromGenericTypeDeclarationWithMultipleTypeParameters(){ 
		String fromClass = "Domain.Direct.Violating.DeclarationVariableInstance_GenericType_MultipleTypeParameters";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		Assert.assertTrue(isUmlLinkNotDetected(fromClass, toClass));
	}

	@Test
	public void UmlLinkType_NotFromLocalVariableDeclaration(){ // No UmlLinks should be caused by local variables.
		String fromClass = "Domain.Direct.Violating.DeclarationVariableLocal";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		Assert.assertTrue(isUmlLinkNotDetected(fromClass, toClass));
	}

	@Test
	public void UmlLinkType_NotFromParameterDeclaration(){ // No UmlLinks should be caused by local variables.
		String fromClass = "Domain.Direct.Violating.DeclarationParameter";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		Assert.assertTrue(isUmlLinkNotDetected(fromClass, toClass));
	}

	@Test
	public void UmlLinkType_NotFromReturnTypeDeclaration(){ // No UmlLinks should be caused by local variables.
		String fromClass = "Domain.Direct.Violating.DeclarationReturnType_GenericType_OneTypeParameter";
		String toClass = "Technology.Direct.Dao.ProfileDAO";
		Assert.assertTrue(isUmlLinkNotDetected(fromClass, toClass));
	}

	
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
		ArrayList<SoftwareUnitDTO> analysedModules = new ArrayList<SoftwareUnitDTO>();
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

	private boolean areDependencyTypesDetected(String fromClass, String toClass, ArrayList<String> dependencyTypes, boolean isIndirect) {
		boolean dependencyTypesDetected = false;
		TreeMap<String, Boolean> foundDependencyTypes = new TreeMap<String, Boolean>();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] foundDependencies = analyseService.getDependenciesFromClassToClass(fromClass, toClass);
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

	private boolean areDependencyTypesDetected(String classFrom, String classTo, ArrayList<String> dependencyTypes, String subType, boolean isIndirect) {
		boolean dependencyTypesDetected = false;
		TreeMap<String, Boolean> foundDependencyTypes = new TreeMap<String, Boolean>();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] foundDependencies = analyseService.getDependenciesFromClassToClass(classFrom, classTo);
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

	private boolean isUmlLinkDetected(String classFrom, String classTo, String attributeFrom, boolean isComposite, String linkType) {
		boolean umlLinkDetected = false;
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		HashSet<UmlLinkDTO>  umlLinkDTOs = analyseService.getUmlLinksFromClassToToClass(classFrom, classTo);
		for (UmlLinkDTO linkDTO : umlLinkDTOs) {
			if (linkDTO.from.equals(classFrom) && linkDTO.to.equals(classTo) && linkDTO.attributeFrom.equals(attributeFrom) && 
					(linkDTO.isComposite == isComposite) && linkDTO.type.equals(linkType)) {
				umlLinkDetected = true;
			}
		}
		return umlLinkDetected;
	}

	private boolean isUmlLinkNotDetected(String classFrom, String classTo) {
		boolean umlLinkDetected = true;
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		HashSet<UmlLinkDTO>  umlLinkDTOs = analyseService.getUmlLinksFromClassToToClass(classFrom, classTo);
		for (UmlLinkDTO linkDTO : umlLinkDTOs) {
			if (linkDTO.from.equals(classFrom) && linkDTO.to.equals(classTo)) {
				umlLinkDetected = false;
			}
		}
		return umlLinkDetected;
	}

}