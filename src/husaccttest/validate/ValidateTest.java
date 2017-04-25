package husaccttest.validate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.SoftwareUnitDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.common.enums.ExtensionTypes;
import husacct.common.enums.ModuleTypes;
import husacct.define.IDefineService;
import husacct.validate.IValidateService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.validate.domain.validation.ruletype.RuleTypeCategories;
import husacct.validate.domain.validation.ruletype.RuleTypes;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ValidateTest {
	private static IDefineService define;
	private static IValidateService validate;
	private static Logger logger = Logger.getLogger(ValidateTest.class);

	@Before
	public void setup() {
	}
	
	@BeforeClass
	public static void beforeClass() {
		try {
			setLog4jConfiguration();
			logger.info(String.format(new Date().toString() + " Start: ValidateTest"));
			define = ServiceProvider.getInstance().getDefineService();
			
			ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
			for(int counter = 0; counter < 3; counter ++) {
				projects.add(new ProjectDTO("TEST_PROJECT_" + (counter)+1, new ArrayList<String>(), "Java", "1.0", 
						"DESCRIPTION PROJECT " + counter, new ArrayList<SoftwareUnitDTO>()));
			}
			define.createApplication("TEST_APPLICATION", projects, "1.0");
			validate = ServiceProvider.getInstance().getValidateService();
			
		} catch (Exception e){
			String errorMessage =  "Exception: " + e.getMessage();
			logger.warn(errorMessage);
		}
	}

	@AfterClass
	public static void tearDown(){
		logger.info(String.format(new Date().toString() + " Finished: ValidateTest"));
	}
	
	@Test
	public void componentDefineIsNotNull() {
		assertNotNull(define);
		assertNotNull(define.getApplicationDetails());
		assertNotNull(define.getApplicationDetails().projects.get(0));
		assertNotNull(define.getApplicationDetails().projects.get(1));
		assertNotNull(define.getApplicationDetails().projects.get(2));
	}
	
	@Test
	public void componentValidateIsNotNull() {
		assertNotNull(validate);
	}
	
	@Test
	public void getFirstProjectLanguage() {
		ProjectDTO project = define.getApplicationDetails().projects.get(0);
		assertNotNull(project);
		
		String language = project.programmingLanguage;
		assertEquals("Java", language);
	}
	
	@Test
	public void getExportExtentions() {
		String[] exportExtensions = new String[] { 
				ExtensionTypes.XLS.getExtension(),
				ExtensionTypes.HTML.getExtension(),
				ExtensionTypes.PDF.getExtension(), 
				ExtensionTypes.XML.getExtension()
		};
		
		assertArrayEquals(exportExtensions, validate.getExportExtentions());
	}

	@Test
	public void exportViolations() {
		String fileExtension = validate.getExportExtentions()[0];
		String fileRelativePath = "src/husaccttest/validate/exportTestReports." + fileExtension;
		boolean testResult = false;

		File exportTestFile = new File(fileRelativePath); 
		validate.exportViolations(exportTestFile, fileExtension);

		File checkExportFile = new File(fileRelativePath); 

		if (checkExportFile.exists()) {
			checkExportFile.delete();
			testResult = true;
		}
		assertTrue(testResult);
	}

	@Test
	public void getCategories() {
		CategoryDTO[] dtos = validate.getCategories();
		String[] ruleTypeCategories = new String[] { 
				RuleTypeCategories.PROPERTY_RULE_TYPES.getCategoryName().toLowerCase().replace(" ", ""), 
				RuleTypeCategories.RELATION_RULE_TYPES.getCategoryName().toLowerCase().replace(" ", "") };
		
		assertArrayEquals(ruleTypeCategories, getCategoryStringArray(dtos));
	}
	
	@Test
	public void getRuleTypes() {
		CategoryDTO[] dtos = validate.getCategories();
		final String[] currentRuletypes = new String[]{
				RuleTypes.FACADE_CONVENTION.toString(),
                RuleTypes.INHERITANCE_CONVENTION.toString(),
				RuleTypes.NAMING_CONVENTION.toString(), 
				RuleTypes.VISIBILITY_CONVENTION.toString(),
				RuleTypes.IS_NOT_ALLOWED_BACK_CALL.toString(),
				RuleTypes.IS_NOT_ALLOWED_SKIP_CALL.toString(), 
				RuleTypes.IS_NOT_ALLOWED_TO_USE.toString(), 
				RuleTypes.IS_ONLY_ALLOWED_TO_USE.toString(), 
				RuleTypes.IS_THE_ONLY_MODULE_ALLOWED_TO_USE.toString(), 
				RuleTypes.MUST_USE.toString(),
		};
		String[] ruletypes = getRuleTypesStringArray(dtos);
		assertArrayEquals(currentRuletypes, ruletypes);
	}

	@Test
	public void getAndPrintAllowedRuleTypesOfModules() {
		try {
			String[] modules = { 
					ModuleTypes.COMPONENT.toString(),
					ModuleTypes.LAYER.toString(),
					ModuleTypes.SUBSYSTEM.toString(),
					ModuleTypes.EXTERNAL_LIBRARY.toString(),
					ModuleTypes.FACADE.toString()
			};
			for (String module : modules) {
				//System.out.print("\nAllowedRuleTypes for " + module + ": ");
				RuleTypeDTO[] allowedRuleTypes = validate.getAllowedRuleTypesOfModule(module);
				assertNotNull(allowedRuleTypes);
				/* for (RuleTypeDTO allowedRuleType : allowedRuleTypes) {
					System.out.print(allowedRuleType.getKey() + ", ");
				} */
			}
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}

	@Test
	public void getAndPrintDefaultRuleTypesOfModules() {
		try {
			String[] modules = { 
					ModuleTypes.COMPONENT.toString(),
					ModuleTypes.LAYER.toString(), 
					ModuleTypes.SUBSYSTEM.toString(), 
					ModuleTypes.EXTERNAL_LIBRARY.toString(), 
					ModuleTypes.FACADE.toString() 
			};
			
			for (String module : modules) {
				// System.out.print("\nDefaultRuleTypes for " + module + ": ");
				
				RuleTypeDTO[] defaultRuleTypes = validate.getDefaultRuleTypesOfModule(module);
				assertNotNull(defaultRuleTypes);
				/* for (RuleTypeDTO defaultRuleType : defaultRuleTypes) {
					System.out.print(defaultRuleType.getKey() + ", ");
				} */
			}
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}

	@Test
	public void getViolationTypesJavaLanguage() {
		CategoryDTO[] dtos = validate.getCategories();
		assertEquals(7, getViolationTypesStringArray(dtos, RuleTypes.IS_NOT_ALLOWED_TO_USE).length);
		assertEquals(7, getViolationTypesStringArray(dtos, RuleTypes.IS_ALLOWED_TO_USE).length);
		assertEquals(4, getViolationTypesStringArray(dtos, RuleTypes.VISIBILITY_CONVENTION).length);
	}

/*	@Test
	public void getViolationTypesCSharpLanguage() {
		CategoryDTO[] dtos = validate.getCategories();
		assertEquals(7, getViolationTypesStringArray(dtos, RuleTypes.IS_NOT_ALLOWED_TO_USE).length);
		assertEquals(7, getViolationTypesStringArray(dtos, RuleTypes.IS_ALLOWED_TO_USE).length);
		assertEquals(4, getViolationTypesStringArray(dtos, RuleTypes.VISIBILITY_CONVENTION).length);
	}
*/
	
	private String[] getCategoryStringArray(CategoryDTO[] dtos) {
		ArrayList<String> categoryList = new ArrayList<String>();

		for (CategoryDTO dto : dtos) {
			categoryList.add(dto.getKey());
		}
		return categoryList.toArray(new String[]{});
	}

	private String[] getRuleTypesStringArray(CategoryDTO[] dtos) {
		ArrayList<String> ruletypeList = new ArrayList<String>();

		for (CategoryDTO cDTO : dtos) {
			for (RuleTypeDTO rDTO : cDTO.getRuleTypes()) {
				ruletypeList.add(rDTO.getKey());
			}
		}
		return ruletypeList.toArray(new String[]{});
	}

	private String[] getViolationTypesStringArray(CategoryDTO[] dtos, RuleTypes ruleTypeKey) {
		ArrayList<String> violationtypeList = new ArrayList<String>();

		for (CategoryDTO cDTO : dtos) {
			for (RuleTypeDTO ruleTypeDTO : cDTO.getRuleTypes()) {
				if (ruleTypeDTO.getKey().equals(ruleTypeKey.toString())) {
					return getViolationTypesStringArray(ruleTypeDTO);
				} else {
					for (RuleTypeDTO exceptionDTO : ruleTypeDTO.getExceptionRuleTypes()) {
						if (exceptionDTO.getKey().equals(ruleTypeKey.toString())) {
							return getViolationTypesStringArray(exceptionDTO);
						}
					}
				}
			}
		}
		return violationtypeList.toArray(new String[]{});
	}

	private String[] getViolationTypesStringArray(RuleTypeDTO rule) {
		ArrayList<String> violationTypeList = new ArrayList<String>();
		for (ViolationTypeDTO vDTO : rule.getViolationTypes()) {
			violationTypeList.add(vDTO.getKey());
		}
		return violationTypeList.toArray(new String[]{});
	}

	//	@Test
	/*
	 * Not reliable testable with the current structure. 
	 */
	//	public void isValidatedBeforeValidation() {
	//		assertFalse(validate.isValidated());
	//	}

	@Test
	public void getViolationsByLogicalPath() {
		// Can't test this now, need to find a way with define (and analyze) to test this method
	}

	@Test
	public void getViolationsByPhysicalPath() {
		// Can't test this now, need to find a way with define (and analyze) to test this method
	}

	@Test
	public void isValidatedAfterValidation() {
		boolean exceptionOccured = false;
		try {
			validate.checkConformance();
		} catch (ProgrammingLanguageNotFoundException e) {
			exceptionOccured = true;
			assertFalse(validate.isValidated());
		}

		if (!exceptionOccured) {
			assertTrue(validate.isValidated());
		}
	}

	private static void setLog4jConfiguration() {
		URL propertiesFile = Class.class.getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
	}
}
