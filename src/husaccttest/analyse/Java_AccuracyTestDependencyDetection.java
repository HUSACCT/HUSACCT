package husaccttest.analyse;

import husacct.ServiceProvider;
import husacct.analyse.serviceinterface.IAnalyseService;
import husacct.analyse.serviceinterface.dto.DependencyDTO;
import husacct.analyse.serviceinterface.dto.UmlLinkDTO;
import husacct.analyse.serviceinterface.enums.DependencySubTypes;
import husacct.analyse.serviceinterface.enums.DependencyTypes;
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
		String fromClass = "domain.direct.violating.AccessClassVariable";
		String toClass = "technology.direct.dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Class Variable", false));
	}

	@Test
	public void AccessClassVariableConstant(){
		String fromClass = "domain.direct.violating.AccessClassVariableConstant";
		String toClass = "technology.direct.dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Class Variable Constant", false));
	}

	@Test
	public void AccessClassVariableInterface(){
		String fromClass = "domain.direct.violating.AccessClassVariableInterface";
		String toClass = "technology.direct.dao.ISierraDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Interface Variable", false));
	}

	@Test
	public void AccessEnumeration(){
		String fromClass = "domain.direct.violating.AccessEnumeration";
		String toClass = "technology.direct.dao.TipDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Enumeration Variable", false));
	}

	@Test
	public void AccessEnumerationInner(){
		String fromClass = "domain.direct.violating.AccessInnerEnumeration";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.InnerEnumeration";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Type", false));
	}

	@Test
	public void AccessFromInnerClass(){
		String fromClass = "domain.direct.violating.CallFromInnerClass.CallingInnerClass";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessInstanceVariableInPlusExpression(){
		String fromClass = "domain.direct.violating.AccessInstanceVariableInPlusExpression";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessInstanceVariableRead(){
		String fromClass = "domain.direct.violating.AccessInstanceVariableRead";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessInstanceVariableWrite(){
		String fromClass = "domain.direct.violating.AccessInstanceVariableWrite";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessInstanceVariableConstant(){
		String fromClass = "domain.direct.violating.AccessInstanceVariableConstant";
		String toClass = "technology.direct.dao.UserDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable Constant", false));
	}

	@Test
	public void AccessInstanceVariableSuperClass(){
		String fromClass = "domain.direct.violating.AccessInstanceVariableSuperClass";
		String toClass = "technology.direct.subclass.CallInstanceSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		boolean outcome1 = areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false); 
		toClass = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void AccessInstanceVariableSuperSuperClass(){
		String fromClass = "domain.direct.violating.AccessInstanceVariableSuperSuperClass";
		String toClass = "technology.direct.subclass.CallInstanceSubSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		boolean outcome1 = areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", true); 
		toClass = "technology.direct.subclass.CallInstanceSubClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", true); 
		toClass = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome3 = areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", true); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true) || (outcome3 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void AccessInstanceVariable_SetArgumentValue(){
		String fromClass = "domain.direct.violating.AccessInstanceVariable_SetArgumentValue";
		String toClass = "technology.direct.dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", true));
	}

	@Test
	public void AccessObjectReferenceAsParameter(){
		String fromClass = "domain.direct.violating.AccessObjectReferenceAsParameter";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Type of Variable", false));
	}

	@Test
	public void AccessInstanceWithinAnonymousClass(){
		String fromClass = "domain.direct.violating.AccessInstanceWithinAnonymousClass";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessLocalVariable_ReadArgumentValue(){
		String fromClass = "domain.direct.violating.AccessLocalVariable_ReadArgumentValue";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false));
	}

	@Test
	public void AccessLocalVariable_SetArgumentValue(){
		String fromClass = "domain.direct.violating.AccessLocalVariable_SetArgumentValue";
		String toClass = "technology.direct.dao.CheckInDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", true));
	}

	@Test
	public void AccessObjectReferenceWithinIfStatement(){
		String fromClass = "domain.direct.violating.AccessObjectReferenceWithinIfStatement";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Type of Variable", false));
	}

	// Annotation	
	@Test
	public void AnnotationDependency(){
		String fromClass = "domain.direct.violating.AnnotationDependency";
		String toClass = "technology.direct.dao.SettingsAnnotation";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Annotation");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "", false));
	}

	// Call
	@Test
	public void CallClassMethod(){
		String fromClass = "domain.direct.violating.CallClassMethod";
		String toClass = "technology.direct.dao.BadgesDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Class Method", false));
	}

	@Test
	public void CallConstructor(){
		String fromClass = "domain.direct.violating.CallConstructor";
		String toClass = "technology.direct.dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Constructor", false));
	}

	@Test
	public void CallConstructor_GenericType_MultipleTypeParameters(){
		String fromClass = "domain.direct.violating.CallConstructor_GenericType_MultipleTypeParameters";
		String toClass = "xLibraries.java.util.HashMap";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Library Method", false));
		typesToFind.clear();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.ProfileDAO", typesToFind, DependencySubTypes.DECL_TYPE_PARAMETER.toString(), false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.UserDAO", typesToFind, DependencySubTypes.DECL_TYPE_PARAMETER.toString(), false));
	}

	@Test
	public void CallConstructorInnerClass(){
		String fromClass = "domain.direct.violating.CallConstructorInnerClass";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.StaticNestedClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Constructor", false));
	}

	@Test
	public void CallConstructorInnerClassDefault(){
		String fromClass = "domain.direct.violating.CallConstructorInnerClassDefault";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.StaticNestedClass";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Constructor", false));
	}

	@Test
	public void CallConstructorInnerClassFromOtherInnerClass(){
		String fromClass = "technology.direct.dao.CallInstanceOuterClassDAO.TestConstructorCallOfInnerClass";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Constructor", false));
	}

	@Test
	public void CallConstructorLibraryClass(){
		String fromClass = "domain.direct.violating.CallConstructorLibraryClass";
		String toClass = "xLibraries.fi.foyt.foursquare.api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Library Method", false));
	}

	@Test
	public void CallConstructorSuper(){
		String fromClass = "domain.direct.violating.CallConstructorSuper";
		String toClass = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Constructor", false));	}

	@Test
	public void CallEnumeration(){
		String fromClass = "domain.direct.violating.CallEnumeration";
		String toClass = "technology.direct.dao.TipDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Enumeration Method", false));
	}

	@Test
	public void CallFromInnerClass(){ //To another inner class
		String fromClass = "domain.direct.violating.CallFromInnerClass.CallingInnerClass";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Method", false));
	}

	@Test
	public void CallInstance(){
		String fromClass = "domain.direct.violating.CallInstance";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Method", false));
	}

	@Test
	public void CallInstanceInnerClass(){
		String fromClass = "domain.direct.violating.CallInstanceInnerClass";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Method", false));
	}

	@Test
	public void CallInstanceInnerInterface(){
		String fromClass = "domain.direct.violating.CallInstanceInnerInterface";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Interface Method", false));
	}

	@Test
	public void CallInstanceInPlusExpression(){
		String fromClass = "domain.direct.violating.CallInstanceInPlusExpression";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Method", false));
	}

	@Test
	public void CallInstanceInterface(){
		String fromClass = "domain.direct.violating.CallInstanceInterface";
		String toClass = "technology.direct.dao.CallInstanceInterfaceDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Interface Method", false));
	}

	@Test
	public void CallInstanceLibraryClass(){
		String fromClass = "domain.direct.violating.CallInstanceLibraryClass";
		String toClass = "xLibraries.fi.foyt.foursquare.api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Library Method", false));
	}

	@Test
	public void CallInstanceOfSuperOverridden(){
		String fromClass = "domain.direct.violating.CallInstanceOfSuperOverridden";
		String toClass = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Method", false));	}

	@Test
	public void CallInstanceSuperClass(){
		String fromClass = "domain.direct.violating.CallInstanceSuperClass";
		String toClass = "technology.direct.subclass.CallInstanceSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		boolean outcome1 = areDependencyTypesDetected(fromClass, toClass, typesToFind, false); 
		toClass = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome2 = areDependencyTypesDetected(fromClass, toClass, typesToFind, true); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome2 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void CallInstanceSuperSuperClass(){
		String fromClass = "domain.direct.violating.CallInstanceSuperSuperClass";
		String toClass = "technology.direct.subclass.CallInstanceSubSubClassDOA";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		boolean outcome1 = areDependencyTypesDetected(fromClass, toClass, typesToFind, false); 
		toClass = "technology.direct.dao.CallInstanceSuperClassDAO";
		boolean outcome3 = areDependencyTypesDetected(fromClass, toClass, typesToFind, true); 
		boolean totalOutcome = false;
		if ((outcome1 == true) || (outcome3 == true)) {
			totalOutcome = true;
		}
		Assert.assertTrue(totalOutcome);
	}

	@Test
	public void CallInstanceWithinAnonymousClass(){
		String fromClass = "domain.direct.violating.CallInstanceWithinAnonymousClass";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	// Declaration
	@Test
	public void DeclarationExceptionThrows(){
		String fromClass = "domain.direct.violating.DeclarationExceptionThrows";
		String toClass = "technology.direct.dao.StaticsException";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Exception", false));
	}

	@Test
	public void DeclarationInnerClass(){
		String fromClass = "domain.direct.Base";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false));
	}

	@Test
	public void DeclarationOuterClassByStaticNestedClass(){
		String fromClass = "technology.direct.dao.CallInstanceOuterClassDAO.StaticNestedClass";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false));
	}

	@Test
	public void DeclarationParameter(){
		String fromClass = "domain.direct.violating.DeclarationParameter";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Parameter", false));
	}

	@Test
	public void DeclarationParameter_GenericType_OneTypeParameter(){
		String fromClass = "domain.direct.violating.DeclarationParameter_GenericType_OneTypeParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.ProfileDAO", typesToFind, DependencySubTypes.DECL_PARAMETER.toString(), false));
		typesToFind.clear();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "xLibraries.java.util.ArrayList", typesToFind, "Library Method", false));
	}

	@Test
	public void DeclarationReturnType(){
		String fromClass = "domain.direct.violating.DeclarationReturnType";
		String toClass = "technology.direct.dao.VenueDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Return Type", false));
	}

	@Test
	public void DeclarationReturnType_GenericType_OneTypeParameter(){
		String fromClass = "domain.direct.violating.DeclarationReturnType_GenericType_OneTypeParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.ProfileDAO", typesToFind, DependencySubTypes.DECL_RETURN_TYPE.toString(), false));
	}

	@Test
	public void DeclarationTypeCast(){
		String fromClass = "domain.direct.violating.DeclarationTypeCast";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Type Cast", false));
	}

	@Test
	public void DeclarationTypeCastToInnerClass(){
		String fromClass = "domain.direct.violating.DeclarationTypeCastToInnerClass";
		String toClass = "technology.direct.dao.CallInstanceOuterClassDAO.CallInstanceInnerClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Type Cast", false));
	}

	@Test
	public void DeclarationTypeCastOfArgument(){
		String fromClass = "domain.direct.violating.DeclarationTypeCastOfArgument";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Type Cast", false));
	}

	@Test
	public void DeclarationVariableInstance(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", false));
	}

	@Test
	public void DeclarationVariableInstance_GenericType_OneTypeParameter(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.AccountDAO", typesToFind, DependencySubTypes.DECL_INSTANCE_VAR.toString(), false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.ProfileDAO", typesToFind, DependencySubTypes.DECL_INSTANCE_VAR.toString(), false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "xLibraries.java.util.ArrayList", typesToFind, DependencySubTypes.DECL_INSTANCE_VAR.toString(), false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.BadgesDAO", typesToFind, DependencySubTypes.DECL_INSTANCE_VAR.toString(), false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.CheckInDAO", typesToFind, DependencySubTypes.DECL_INSTANCE_VAR.toString(), false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.UserDAO", typesToFind, DependencySubTypes.DECL_INSTANCE_VAR.toString(), false));
		typesToFind.clear();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "xLibraries.java.util.ArrayList", typesToFind, "Library Method", false));
	}

	@Test
	public void DeclarationVariableInstance_GenericType_MultipleTypeParameters(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_MultipleTypeParameters";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.ProfileDAO", typesToFind, DependencySubTypes.DECL_INSTANCE_VAR.toString(), false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.FriendsDAO", typesToFind, DependencySubTypes.DECL_INSTANCE_VAR.toString(), false));
		typesToFind.clear();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "xLibraries.java.util.HashMap", typesToFind, "Library Method", false));
	}

	@Test
	public void DeclarationVariableInstance_GenericType_MultipleTypeParameters_Complex(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_MultipleTypeParameters_Complex";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.ProfileDAO", typesToFind, DependencySubTypes.DECL_INSTANCE_VAR.toString(), false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.FriendsDAO", typesToFind, DependencySubTypes.DECL_INSTANCE_VAR.toString(), false));
		typesToFind.clear();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "xLibraries.java.util.HashMap", typesToFind, "Library Method", false));
	}

	@Test
	public void DeclarationVariableStatic(){
		String fromClass = "domain.direct.violating.DeclarationVariableStatic";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, DependencySubTypes.DECL_CLASS_VAR.toString(), false));
	}

	@Test
	public void DeclarationVariableStatic_GenericType_OneTypeParameter(){
		String fromClass = "domain.direct.violating.DeclarationVariableStatic_GenericType_OneTypeParameter";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.ProfileDAO", typesToFind, DependencySubTypes.DECL_CLASS_VAR.toString(), false));
		typesToFind.clear();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "xLibraries.java.util.ArrayList", typesToFind, "Library Method", false));
	}

	@Test
	public void DeclarationVariableLocal(){
		String fromClass = "domain.direct.violating.DeclarationVariableLocal";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Local Variable", false));
	}

	@Test
	public void DeclarationVariableLocal_GenericType_MultipleTypeParameters(){
		String fromClass = "domain.direct.violating.DeclarationVariableLocal_GenericType_MultipleTypeParameters";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.ProfileDAO", typesToFind, DependencySubTypes.DECL_LOCAL_VAR.toString(), false));
		Assert.assertTrue(areDependencyTypesDetected(fromClass, "technology.direct.dao.FriendsDAO", typesToFind, DependencySubTypes.DECL_LOCAL_VAR.toString(), false));
	}

	@Test
	public void DeclarationVariableLocal_Initialized(){
		String fromClass = "domain.direct.violating.DeclarationVariableLocal_Initialized";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Local Variable", false));
	}

	@Test
	public void DeclarationVariableWithinForStatement(){
		String fromClass = "domain.direct.violating.DeclarationVariableWithinForStatement";
		String toClass = "technology.direct.dao.ProfileDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Declaration");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Local Variable", false));
	}

	// Import
	@Test
	public void ImportDependencyUnused(){
		String fromClass = "domain.direct.violating.ImportDependencyUnused";
		String toClass = "technology.direct.dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	@Test
	public void ImportDependencyStatic(){
		String fromClass = "domain.direct.violating.ImportDependencyStatic";
		String toClass = "technology.direct.dao.AccountDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Import");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, false));
	}

	// Inheritance
	@Test
	public void InheritanceExtends(){
		String fromClass = "domain.direct.violating.InheritanceExtends";
		String toClass = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Extends Class", false));	
		toClass = "technology.direct.dao.IMapDAO";
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Implements Interface", false));
		}

	@Test
	public void InheritanceExtendsAbstractClass(){
		String fromClass = "domain.direct.violating.InheritanceExtendsAbstractClass";
		String toClass = "technology.direct.dao.FriendsDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Extends Abstract Class", false));
	}

	@Test
	public void InheritanceExtendsFullPath(){
		String fromClass = "domain.direct.violating.InheritanceExtendsFullPath";
		String toClass = "technology.direct.dao.HistoryDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Extends Class", false));	}

	@Test
	public void InheritanceImplementsInterface(){
		String fromClass = "domain.direct.violating.InheritanceImplementsInterface";
		String toClass = "technology.direct.dao.IMapDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Implements Interface", false));
	}

	//TESTS
	//INDIRECT
	// Access
	@Test
	public void AccessInstanceVariableIndirect_MethodVar(){
		String fromClass = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_MethodVar";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVar(){
		String fromClass = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_VarVar";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_VarVarToString(){
		String fromClass = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_VarVarToString";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_MethodVarVar(){
		String fromClass = "domain.indirect.violatingfrom.AccessInstanceVariableIndirectIndirect_MethodVarVar";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", true));
	}

	@Test
	public void AccessInstanceVariableIndirectIndirect_VarVarVar(){
		String fromClass = "domain.indirect.violatingfrom.AccessInstanceVariableIndirectIndirect_VarVarVar";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_SuperClass(){
		String fromClass = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_SuperClass";
		String toClass = "technology.direct.dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessInstanceVariableIndirect_SuperClass_DependencyToBaseIndirect(){
		String fromClass = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_SuperClass";
		String toClass = "domain.indirect.BaseIndirect";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Variable", true));
	}

	@Test
	public void AccessInstanceVariableIndirect_SuperSuperClass(){
		String fromClass = "domain.indirect.violatingfrom.AccessInstanceVariableIndirect_SuperSuperClass";
		String toClass = "technology.direct.dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter_POI(){
		String fromClass = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsParameter_POI";
		String toClass = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Type of Variable", true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsParameter(){
		String fromClass = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsParameter";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Return Type", true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide(){
		String fromClass = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsReturnValueOfSuperClassMethod_FromSide";
		String toClass = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Return Type", true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType(){
		String fromClass = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaArgumentType";
		String toClass = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Return Type", true));
	}

	@Test
	public void AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic(){
		String fromClass = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_AsReturnValue_MethodDerivedViaHeuristic";
		String toClass = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Return Type", true));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament_POI(){
		String fromClass = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_WithinIfStament_POI";
		String toClass = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Type of Variable", true));
	}

	@Test
	public void AccessObjectReferenceIndirect_WithinIfStament(){
		String fromClass = "domain.indirect.violatingfrom.AccessObjectReferenceIndirect_WithinIfStament";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Reference");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Return Type", true));
	}

	@Test
	public void AccessStaticVariableIndirect_MethodVar(){
		String fromClass = "domain.indirect.violatingfrom.AccessStaticVariableIndirect_MethodVar";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVar(){
		String fromClass = "domain.indirect.violatingfrom.AccessStaticVariableIndirect_VarVar";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirect_VarVarToString(){
		String fromClass = "domain.indirect.violatingfrom.AccessStaticVariableIndirect_VarVarToString";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_MethodVarVar(){
		String fromClass = "domain.indirect.violatingfrom.AccessStaticVariableIndirectIndirect_MethodVarVar";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void AccessStaticVariableIndirectIndirect_VarVarVar(){
		String fromClass = "domain.indirect.violatingfrom.AccessStaticVariableIndirectIndirect_VarVarVar";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Access");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	// Call
	@Test
	public void CallInstanceMethodIndirect_MethodMethod(){
		String fromClass = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_MethodMethod";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethodToString(){
		String fromClass = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_MethodMethodToString";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_MethodMethod_ViaConstructor(){
		String fromClass = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_MethodMethodViaConstructor";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_StaticMethodInstanceMethod(){
		String fromClass = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_StaticMethodInstanceMethod";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperClass(){
		String fromClass = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_SuperClass";
		String toClass = "technology.direct.dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_SuperSuperClass(){
		String fromClass = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_SuperSuperClass";
		String toClass = "technology.direct.dao.CallInstanceSuperClassDAO";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirect_VarMethod(){
		String fromClass = "domain.indirect.violatingfrom.CallInstanceMethodIndirect_VarMethod";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, true));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_MethodVarMethod(){
		String fromClass = "domain.indirect.violatingfrom.CallInstanceMethodIndirectIndirect_MethodVarMethod";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Method", true));
	}

	@Test
	public void CallInstanceMethodIndirectIndirect_VarVarMethod(){
		String fromClass = "domain.indirect.violatingfrom.CallInstanceMethodIndirectIndirect_VarVarMethod";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Instance Method", true));
	}

	@Test
	public void CallStaticMethodIndirect_MethodStaticMethod(){
		String fromClass = "domain.indirect.violatingfrom.CallStaticMethodIndirect_MethodStaticMethod";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Class Method", true));
	}

	@Test
	public void CallStaticMethodIndirect_VarStaticMethod(){
		String fromClass = "domain.indirect.violatingfrom.CallStaticMethodIndirect_VarStaticMethod";
		String toClass = "domain.indirect.indirectto.ServiceOne";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Call");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Class Method", true));
	}

	// Inheritance
	@Test
	public void InheritanceExtendsExtendsIndirect(){
		String fromClass = "domain.indirect.violatingfrom.InheritanceExtendsExtendsIndirect";
		String toClass = "domain.indirect.indirectto.POI";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Extends Abstract Class", true));
	}

	@Test
	public void InheritanceExtendsImplementsIndirect(){
		String fromClass = "domain.indirect.violatingfrom.InheritanceExtendsImplementsIndirect";
		String toClass = "domain.indirect.indirectto.IPreferences";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Implements Interface", true));
	}

	@Test
	public void InheritanceExtendsLibraryClass(){
		String fromClass = "domain.direct.violating.InheritanceExtendsLibraryClass";
		String toClass = "xLibraries.fi.foyt.foursquare.api.FoursquareApi";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "From Library Class", false));
	}

	@Test
	public void InheritanceFromInnerClass(){
		String fromClass = "domain.direct.violating.CallFromInnerClass.CallingInnerClass";
		String toClass = "domain.direct.Base";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Extends Class", false));
	}

	@Test
	public void InheritanceImplementsExtendsIndirect(){
		String fromClass = "domain.indirect.violatingfrom.InheritanceImplementsExtendsIndirect";
		String toClass = "domain.indirect.indirectto.IPreferences";
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add("Inheritance");
		Assert.assertTrue(areDependencyTypesDetected(fromClass, toClass, typesToFind, "Implements Interface", true));
	}

	// Violations: Test if all found violating dependencies are reported as violations
	@Test
	public void ViolationsDirect_CompareToNumberOfDirectDependencies_violating_dao(){
		String fromUnit = "domain.direct.violating";
		String toUnit = "technology.direct";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(fromUnit, toUnit).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromUnit, toUnit).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}

	@Test
	public void ViolationsDirect_CompareToNumberOfDirectDependencies_violating_foursquare(){
		String fromUnit = "domain.direct.violating";
		String toUnit = "fi.foyt.foursquare.api";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(fromUnit, toUnit).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromUnit, toUnit).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}
	
	@Test
	public void ViolationsInDirect_CompareToNumberOfInDirectDependencies_violatingfrom_indirectto(){
		String fromUnit = "domain.indirect.violatingfrom";
		String toUnit = "domain.indirect.indirectto";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(fromUnit, toUnit).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromUnit, toUnit).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}

	@Test
	public void ViolationsInDirect_CompareToNumberOfInDirectDependencies_violating_dao(){
		String fromUnit = "domain.direct.violating";
		String toUnit = "technology.direct.dao";
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		validateService = ServiceProvider.getInstance().getValidateService();
		int numberOfDependencies = analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(fromUnit, toUnit).length;
		int numberOfViolations = validateService.getViolationsByPhysicalPath(fromUnit, toUnit).length;
		Assert.assertTrue(numberOfDependencies == numberOfViolations);
	}

	// Test service that provide only dependencies of type Access, Call, or Reference.
	@Test
	public void OnlyDependenciesOfTypeAccessCallReference_fromClasstoClass(){
		String fromClass = "domain.direct.violating.AccessInstanceVariable_SetArgumentValue";
		String toClass = "technology.direct.dao.ProfileDAO";
		// Get all dependencies between the the two classes
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] allDependencies= analyseService.getDependenciesFromClassToClass(fromClass, toClass);
		int numberOfAllDependencies = allDependencies.length;
		// Determine the number of dependencies not of type Access, Call, or Reference.
		ArrayList<DependencyDTO> result = new ArrayList<DependencyDTO>();
    	for (DependencyDTO dependency : allDependencies) {
    		if (dependency.type.equals(DependencyTypes.ANNOTATION.toString()) || dependency.type.equals(DependencyTypes.DECLARATION.toString()) 
    				|| dependency.type.equals(DependencyTypes.IMPORT.toString()) || dependency.type.equals(DependencyTypes.INHERITANCE.toString())) {
        		result.add(dependency);
        	}
    	}
    	int numberOfADII_Dependencies = result.size();
    	int numberOfACR_Dependencies = analyseService.getDependencies_OnlyAccessCallAndReferences_FromClassToClass(fromClass, toClass).length;
		boolean correctNrOfACR_Dependencies = false;
		if ((numberOfACR_Dependencies > 1) && (numberOfACR_Dependencies == numberOfAllDependencies - numberOfADII_Dependencies)) {
			correctNrOfACR_Dependencies = true;
		}
		Assert.assertTrue(correctNrOfACR_Dependencies);
	}

	@Test
	public void OnlyDependenciesOfTypeAccessCallReference_fromSUtoSU(){
		String fromClass = "domain.direct.violating";
		String toClass = "technology.direct.dao";
		// Get all dependencies between the the two classes
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] allDependencies= analyseService.getDependenciesFromSoftwareUnitToSoftwareUnit(fromClass, toClass);
		int numberOfAllDependencies = allDependencies.length;
		// Determine the number of dependencies not of type Access, Call, or Reference.
		ArrayList<DependencyDTO> result = new ArrayList<DependencyDTO>();
    	for (DependencyDTO dependency : allDependencies) {
    		if (dependency.type.equals(DependencyTypes.ANNOTATION.toString()) || dependency.type.equals(DependencyTypes.DECLARATION.toString()) 
    				|| dependency.type.equals(DependencyTypes.IMPORT.toString()) || dependency.type.equals(DependencyTypes.INHERITANCE.toString())) {
        		result.add(dependency);
        	}
    	}
    	int numberOfADII_Dependencies = result.size();
    	int numberOfACR_Dependencies = analyseService.getDependencies_OnlyAccessCallAndReferences_FromSoftwareUnitToSoftwareUnit(fromClass, toClass).length;
		boolean correctNrOfACR_Dependencies = false;
		if ((numberOfACR_Dependencies > 1) && (numberOfACR_Dependencies == numberOfAllDependencies - numberOfADII_Dependencies)) {
			correctNrOfACR_Dependencies = true;
		}
		Assert.assertTrue(correctNrOfACR_Dependencies);
	}

	
	// UmlLinkTypes: Positive
	@Test
	public void UmlLinkType_InstanceVariableDeclaration_NotComposite(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance";
		String toClass = "technology.direct.dao.ProfileDAO";
		String fromAttribute = "pdao";
		boolean isComposite = false;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_NotComposite_LibraryClass(){
		String fromClass = "domain.direct.violating.AccessInstanceVariableLibraryClass";
		String toClass = "xLibraries.fi.foyt.foursquare.api.FoursquareApiException";
		String fromAttribute = "fourExc";
		boolean isComposite = false;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_Composite_Array(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		String toClass = "technology.direct.dao.AccountDAO";
		String fromAttribute = "aDao";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_Composite_List(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		String toClass = "technology.direct.dao.BadgesDAO";
		String fromAttribute = "bDao";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_Composite_HashSet(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		String toClass = "technology.direct.dao.CheckInDAO";
		String fromAttribute = "cDao";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_Composite_ArrayList(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		String toClass = "technology.direct.dao.ProfileDAO";
		String fromAttribute = "pDao";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_Composite_Vector(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_OneTypeParameter";
		String toClass = "technology.direct.dao.UserDAO";
		String fromAttribute = "uDao";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_VariableInstance_GenericType_MultipleTypeParameters(){ 
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_MultipleTypeParameters";
		String toClass = "technology.direct.dao.FriendsDAO";
		String fromAttribute = "hashMap";
		boolean isComposite = true;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}

	@Test
	public void UmlLinkType_InstanceVariableDeclaration_MultipleAttributesOfTheSameTypeAtTheSameLine(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_MultipleAttributesAtTheSameLine";
		String toClass = "technology.direct.dao.ProfileDAO";
		String fromAttribute = "p1Dao";
		boolean isComposite = false;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
		fromAttribute = "p2Dao";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
		fromAttribute = "p3Dao";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}
	
	@Test
	public void UmlLinkType_InstanceVariableDeclaration_MultipleAttr_ViaServiceWIthSoftwareUnits(){
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_MultipleAttributesAtTheSameLine";
		String toClass = "technology.direct.dao.ProfileDAO";
		String fromAttribute = "p1Dao";
		boolean isComposite = false;
		String typeToFind = "Attribute";
		Assert.assertTrue(isUmlLinkDetectedViaSoftwareUnits(fromClass, toClass, fromAttribute, isComposite, typeToFind));
		fromAttribute = "p2Dao";
		Assert.assertTrue(isUmlLinkDetectedViaSoftwareUnits(fromClass, toClass, fromAttribute, isComposite, typeToFind));
		fromAttribute = "p3Dao";
		Assert.assertTrue(isUmlLinkDetectedViaSoftwareUnits(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}
	
	@Test
	public void UmlLinkType_InstanceVariableDeclaration_MultipleAttr_DependencyDTO(){
		String fromClass = "domain.direct.violating"; //.DeclarationVariableInstance_MultipleAttributesAtTheSameLine";
		String toClass = "technology.direct.dao"; //.ProfileDAO";
		String fromAttribute = "p1Dao";
		boolean isComposite = false;
		String typeToFind = "Attr: " + fromAttribute;
		ArrayList<String> typesToFind = new ArrayList<String>();
		typesToFind.add(typeToFind);
		Assert.assertTrue(areUMLLinkDependencyDTOsDetected(fromClass, toClass, typesToFind, fromAttribute, isComposite));
		fromAttribute = "p2Dao";
		typeToFind = "Attr: " + fromAttribute;
		typesToFind.clear();
		typesToFind.add(typeToFind);
		Assert.assertTrue(areUMLLinkDependencyDTOsDetected(fromClass, toClass, typesToFind, fromAttribute, isComposite));
		fromAttribute = "p3Dao";
		typeToFind = "Attr: " + fromAttribute;
		typesToFind.clear();
		typesToFind.add(typeToFind);
		Assert.assertTrue(areUMLLinkDependencyDTOsDetected(fromClass, toClass, typesToFind, fromAttribute, isComposite));
	}
	

	@Test
	public void UmlLinkType_Inherits(){
		String fromClass = "domain.direct.violating.InheritanceExtends";
		String toClass = "technology.direct.dao.HistoryDAO";
		String fromAttribute = "";
		boolean isComposite = false;
		String typeToFind = "Inherits";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}
	
	@Test
	public void UmlLinkType_Implements(){
		String fromClass = "domain.direct.violating.InheritanceImplementsInterface";
		String toClass = "technology.direct.dao.IMapDAO";
		String fromAttribute = "";
		boolean isComposite = false;
		String typeToFind = "Implements";
		Assert.assertTrue(isUmlLinkDetectedViaClass(fromClass, toClass, fromAttribute, isComposite, typeToFind));
	}
	
	// UmlLinkTypes: Negative
	@Test
	public void UmlLinkType_NotFromClassVariable(){ 
		String fromClass = "domain.direct.violating.DeclarationVariableStatic";
		String toClass = "technology.direct.dao.ProfileDAO";
		Assert.assertTrue(isUmlLinkNotDetected(fromClass, toClass));
	}

	@Test
	public void UmlLinkType_NotFromGenericTypeDeclarationWithMultipleTypeParameters(){ 
		String fromClass = "domain.direct.violating.DeclarationVariableInstance_GenericType_MultipleTypeParameters";
		String toClass = "technology.direct.dao.ProfileDAO";
		Assert.assertTrue(isUmlLinkNotDetected(fromClass, toClass));
	}

	@Test
	public void UmlLinkType_NotFromLocalVariableDeclaration(){ // No UmlLinks should be caused by local variables.
		String fromClass = "domain.direct.violating.DeclarationVariableLocal";
		String toClass = "technology.direct.dao.ProfileDAO";
		Assert.assertTrue(isUmlLinkNotDetected(fromClass, toClass));
	}

	@Test
	public void UmlLinkType_NotFromParameterDeclaration(){ // No UmlLinks should be caused by local variables.
		String fromClass = "domain.direct.violating.DeclarationParameter";
		String toClass = "technology.direct.dao.ProfileDAO";
		Assert.assertTrue(isUmlLinkNotDetected(fromClass, toClass));
	}

	@Test
	public void UmlLinkType_NotFromReturnTypeDeclaration(){ // No UmlLinks should be caused by local variables.
		String fromClass = "domain.direct.violating.DeclarationReturnType_GenericType_OneTypeParameter";
		String toClass = "technology.direct.dao.ProfileDAO";
		Assert.assertTrue(isUmlLinkNotDetected(fromClass, toClass));
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

	private boolean areDependencyTypesDetected(String classFrom, String classTo, ArrayList<String> dependencyTypes, boolean isIndirect) {
		return areDependencyTypesDetected(classFrom, classTo, dependencyTypes, "", isIndirect);
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

	private boolean areUMLLinkDependencyDTOsDetected(String classFrom, String classTo, ArrayList<String> dependencyTypes, String subType, boolean isIndirect) {
		boolean dependencyTypesDetected = false;
		TreeMap<String, Boolean> foundDependencyTypes = new TreeMap<String, Boolean>();
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		DependencyDTO[] foundDependencies = analyseService.getUmlLinksAsDependencyDtosFromSoftwareUnitToSoftwareUnit(classFrom, classTo);
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

	private boolean isUmlLinkDetectedViaClass(String classFrom, String classTo, String attributeFrom, boolean isComposite, String linkType) {
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

	private boolean isUmlLinkDetectedViaSoftwareUnits(String classFrom, String classTo, String attributeFrom, boolean isComposite, String linkType) {
		boolean umlLinkDetected = false;
		analyseService = ServiceProvider.getInstance().getAnalyseService();
		UmlLinkDTO[]  umlLinkDTOs = analyseService.getUmlLinksFromSoftwareUnitToSoftwareUnit(classFrom, classTo);
		for (UmlLinkDTO linkDTO : umlLinkDTOs) {
			if (linkDTO.from.equals(classFrom) && linkDTO.to.equals(classTo) && linkDTO.attributeFrom.equals(attributeFrom) && 
					(linkDTO.isComposite == isComposite) && linkDTO.type.equals(linkType)) {
				umlLinkDetected = true;
			}
		}
		return umlLinkDetected;
	}

}