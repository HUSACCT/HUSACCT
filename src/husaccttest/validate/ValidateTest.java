package husaccttest.validate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.common.dto.AnalysedModuleDTO;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.ProjectDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.define.IDefineService;
import husacct.validate.IValidateService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;
import husacct.validate.domain.validation.module.ModuleTypes;
import husacct.validate.domain.validation.ruletype.RuleTypeCategories;
import husacct.validate.domain.validation.ruletype.RuleTypes;
import husacct.validate.task.extensiontypes.ExtensionTypes.ExtensionType;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JInternalFrame;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;

public class ValidateTest {
	private IDefineService define;
	private IValidateService validate;

	@Before
	public void setup() {
		setLog4jConfiguration();
		define = ServiceProvider.getInstance().getDefineService();
		
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		for(int counter = 0; counter < 3; counter ++) {
			projects.add(new ProjectDTO("TEST_PROJECT_" + (counter)+1, new ArrayList<String>(), "Java", "1.0", 
					"DESCRIPTION PROJECT " + counter, new ArrayList<AnalysedModuleDTO>()));
		}
		define.createApplication("TEST_APPLICATION", projects, "1.0");
		validate = ServiceProvider.getInstance().getValidateService();
	}
	
	private void setLog4jConfiguration() {
		URL propertiesFile = getClass().getResource("/husacct/common/resources/log4j.properties");
		PropertyConfigurator.configure(propertiesFile);
	}
	
	@Test
	public void testSetup() {
		assertNotNull(define);
		assertNotNull(define.getApplicationDetails());
		assertNotNull(define.getApplicationDetails().projects.get(0));
		assertNotNull(define.getApplicationDetails().projects.get(1));
		assertNotNull(define.getApplicationDetails().projects.get(2));
		assertNotNull(validate);
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
	public void getBrowseViolationsGUI() {
		Object screen = validate.getBrowseViolationsGUI();
		assertNotNull(screen);
		assertTrue(screen instanceof JInternalFrame);
		assertFalse(((JInternalFrame) screen).isVisible());
	}

	@Test
	public void getConfigurationGUI() {
		Object screen = validate.getConfigurationGUI();
		assertNotNull(screen);
		assertTrue(screen instanceof JInternalFrame);
		assertFalse(((JInternalFrame) screen).isVisible());
	}

	@Test
	public void getExportExtentions() {
		String[] exportExtensions = new String[] { 
				ExtensionType.PDF.getExtension(), 
				ExtensionType.HTML.getExtension(),
				ExtensionType.XML.getExtension()				
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
                RuleTypes.SUPERCLASSINHERITANCE_CONVENTION.toString(),
                RuleTypes.INTERFACE_CONVENTION.toString(),
				RuleTypes.NAMING_CONVENTION.toString(), 
				RuleTypes.FACADE_CONVENTION.toString(),
				RuleTypes.VISIBILITY_CONVENTION.toString(), 
				RuleTypes.IS_NOT_ALLOWED_TO_USE.toString(), 
				RuleTypes.IS_ONLY_ALLOWED_TO_USE.toString(), 
				RuleTypes.IS_NOT_ALLOWED_SKIP_CALL.toString(), 
				RuleTypes.IS_ONLY_MODULE_ALLOWED_TO_USE.toString(), 
				RuleTypes.MUST_USE.toString(),
				RuleTypes.IS_NOT_ALLOWED_BACK_CALL.toString()
		};
		assertArrayEquals(currentRuletypes, getRuleTypesStringArray(dtos));
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
				System.out.print("\nAllowedRuleTypes for " + module + ": ");
				
				RuleTypeDTO[] allowedRuleTypes = validate.getAllowedRuleTypesOfModule(module);
				assertNotNull(allowedRuleTypes);
				for (RuleTypeDTO allowedRuleType : allowedRuleTypes) {
					System.out.print(allowedRuleType.getKey() + ", ");
				}
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
				System.out.print("\nDefaultRuleTypes for " + module + ": ");
				
				RuleTypeDTO[] defaultRuleTypes = validate.getDefaultRuleTypesOfModule(module);
				assertNotNull(defaultRuleTypes);
				for (RuleTypeDTO defaultRuleType : defaultRuleTypes) {
					System.out.print(defaultRuleType.getKey() + ", ");
				}
			}
		} catch(Exception exc) {
			exc.printStackTrace();
		}
	}

	@Test
	public void getViolationTypesJavaLanguage() {
		CategoryDTO[] dtos = validate.getCategories();
		assertEquals(6, getViolationTypesStringArray(dtos, RuleTypes.IS_NOT_ALLOWED_TO_USE).length);
		assertEquals(6, getViolationTypesStringArray(dtos, RuleTypes.IS_ALLOWED_TO_USE).length);
		assertEquals(4, getViolationTypesStringArray(dtos, RuleTypes.VISIBILITY_CONVENTION).length);
	}

	@Test
	public void getViolationTypesCSharpLanguage() {
		CategoryDTO[] dtos = validate.getCategories();
		assertEquals(6, getViolationTypesStringArray(dtos, RuleTypes.IS_NOT_ALLOWED_TO_USE).length);
		assertEquals(6, getViolationTypesStringArray(dtos, RuleTypes.IS_ALLOWED_TO_USE).length);
		assertEquals(4, getViolationTypesStringArray(dtos, RuleTypes.VISIBILITY_CONVENTION).length);
	}

	@Test
	public void getViolationTypesNoLanguage() {
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		projects.add(new ProjectDTO("project", new ArrayList<String>(), "", "", "", new ArrayList<AnalysedModuleDTO>()));
		define.createApplication("TEST_APPLICATION", projects, "1.0");

		CategoryDTO[] dtos = validate.getCategories();
		assertEquals(0, getViolationTypesStringArray(dtos, RuleTypes.IS_NOT_ALLOWED_TO_USE).length);
		assertEquals(0, getViolationTypesStringArray(dtos, RuleTypes.IS_ALLOWED_TO_USE).length);
	}

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
}