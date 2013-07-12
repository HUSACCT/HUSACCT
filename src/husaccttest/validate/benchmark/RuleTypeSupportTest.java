package husaccttest.validate.benchmark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import husacct.common.dto.ApplicationDTO;
import husacct.common.dto.ModuleDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleDTO;
import husacct.define.domain.appliedrule.AppliedRuleStrategy;
import husacct.define.domain.module.ModuleStrategy;
import husacct.validate.domain.validation.Violation;

import java.net.URL;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class RuleTypeSupportTest {
	private static RuleTypeSupport srmaTest;
	private static Logger logger;
	
	@BeforeClass
	public static void beforeClass() {
		setLog4jConfiguration();
		
		srmaTest = new RuleTypeSupport();
		
		srmaTest.setUpSRMATest();
	}
	
	@AfterClass
	public static void tearDown(){
		
	}
	
	@Test
	public void createApplication() {		
		ApplicationDTO applicationDTO = srmaTest.getDefineService().getApplicationDetails();
		ProjectDTO projectDTO = applicationDTO.projects.get(0);
		
		assertEquals(applicationDTO.name, srmaTest.getApplicationName());
		assertEquals(projectDTO.paths.get(0), srmaTest.getProjectSourcePath());
		assertEquals(projectDTO.programmingLanguage, srmaTest.getProgramLanguage());
		assertEquals(applicationDTO.version, srmaTest.getProgramVersion());
	}
	
	@Test
	public void analyseApplicationSucceeded() {
		assertTrue(srmaTest.getAnalyseService().isAnalysed());
	}
	
	@Test
	public void analyseApplicationDetection() {
		assertNotSame(0, srmaTest.getAnalyseService().getAmountOfDependencies());
		assertNotSame(0, srmaTest.getAnalyseService().getAmountOfPackages());
		assertNotSame(0, srmaTest.getAnalyseService().getAmountOfClasses());
		assertNotSame(0, srmaTest.getAnalyseService().getAmountOfInterfaces());
	}
	
	@Test
	public void getAmountStoredModulesSA() {
		ModuleDTO[] storedModules = srmaTest.getDefineService().getRootModules();
		ArrayList<ModuleStrategy> definedModules = srmaTest.getSoftwareArchitecture().getModules();
		
		assertEquals(storedModules.length, definedModules.size());
	}
	
	@Test
	public void getAmountStoredAppliedRules() {
		RuleDTO[] storedRuleTypes = srmaTest.getDefineService().getDefinedRules();
		ArrayList<AppliedRuleStrategy> definedRuleTypes = srmaTest.getSoftwareArchitecture().getAppliedRules();
		
		assertEquals(storedRuleTypes.length, definedRuleTypes.size());
	}
	
	@Test
	public void getAmountGeneratedViolations() {
		ArrayList<Violation> allViolations = srmaTest.getAllViolations();
		
		assertNotSame(0, allViolations.size());
	}
	
	@Test
	public void getGeneratedViolations() {
		int counter = 0;
		for(Violation theViolation: srmaTest.getAllViolations()) {
			counter++;
			StringBuilder violationString = new StringBuilder();
			violationString.append(counter);
			violationString.append(" > ");
			violationString.append(theViolation.getViolationtypeKey());
			violationString.append(" - ");
			violationString.append(theViolation.getRuletypeKey());
			violationString.append(" - ");
			violationString.append(theViolation.getLinenumber());
			violationString.append(" - ");
			violationString.append(theViolation.getClassPathFrom());
			violationString.append(" - ");
			violationString.append(theViolation.getClassPathTo());
			
			System.err.println(violationString.toString());
		}
	}

	@Test
	public void ruleType1_BackCall() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType2_BackCall() {
		fail("Implementatie schrijven.");
	}	
	
	@Test
	public void ruleType3_BackCall() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType4_SkipCall() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType5_NamingConvention_prefix() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType6_NamingConvention_postfix() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType7_NamingConvention_mid() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType8_VisibilityConvention() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType9_FacadeConvention() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType10_SuperClassInheritanceConvention() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType11_InterfaceInheritanceConvention() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType12_IsOnlyAllowedToUse() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType13_IsTheOnlyModuleAllowedToUse() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType14_MustUse_Correct() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType15_MustUse_Violating() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType16_IsNotAllowedToUse() {
		fail("Implementatie schrijven.");
	}
	
	@Test
	public void ruleType17_IsNotAllowedToUse_ExternalSystem() {
		fail("Implementatie schrijven.");
	}
	
	
	//OTHER METHODS
	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
		logger = Logger.getLogger(RuleTypeSupportTest.class);
	}
	
}