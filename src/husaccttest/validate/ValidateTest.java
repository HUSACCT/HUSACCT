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

import java.io.File;
import java.util.ArrayList;

import javax.swing.JInternalFrame;

import org.junit.Before;
import org.junit.Test;

public class ValidateTest {
	private IDefineService define;
	private IValidateService validate;

	@Before
	public void setup() {
		define = ServiceProvider.getInstance().getDefineService();
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		for(int counter = 0; counter < 3; counter ++) {
			projects.add(new ProjectDTO("TEST_PROJECT_" + (counter)+1, new ArrayList<String>(), "java", "1.0", 
					"DESCRIPTION PROJECT " + counter, new ArrayList<AnalysedModuleDTO>()));
		}
		define.createApplication("TEST_APPLICATION", projects, "1.0");
		validate = ServiceProvider.getInstance().getValidateService();

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
		assertArrayEquals(new String[]{ "pdf", "html", "xml" }, validate.getExportExtentions());
	}

	@Test
	public void exportViolations() {
		String fileExtension = validate.getExportExtentions()[0];
		String fileRelativePath = "src/husaccttest/validate/exportTestReports." + fileExtension;
		boolean testResult = false;

		File exportTestFile = new File(fileRelativePath); 
		validate.exportViolations(exportTestFile, fileExtension);

		File checkExportFile = new File(fileRelativePath); 

		if(checkExportFile.exists()) {
			checkExportFile.delete();
			testResult = true;
		}

		assertTrue(testResult);
	}

	@Test
	public void getCategories() {
		CategoryDTO[] dtos = validate.getCategories();
		assertArrayEquals(new String[]{ "propertyruletypes", "relationruletypes" }, getCategoryStringArray(dtos));
	}

	@Test
	public void getRuleTypes() {
		CategoryDTO[] dtos = validate.getCategories();
		final String[] currentRuletypes = new String[]{ "InterfaceConvention", "NamingConvention", "FacadeConvention", "SubClassConvention", "VisibilityConvention", "IsNotAllowedToUse", "IsOnlyAllowedToUse", "IsNotAllowedToMakeSkipCall", "IsOnlyModuleAllowedToUse", "MustUse", "IsNotAllowedToMakeBackCall" };
		assertArrayEquals(currentRuletypes, getRuleTypesStringArray(dtos));
	}

	@Test
	public void getAndPrintAllowedRuleTypesOfModules() {
		try {
			String[] modules = {"Component", "Layer", "SubSystem", "ExternalLibrary"};
			for (String module : modules) {
				RuleTypeDTO[] allowedRuleTypes = validate.getAllowedRuleTypesOfModule(module);
				System.out.print("\nallowedRuleTypes for " + module + " : ");
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
			String[] modules = {"Component", "Layer", "SubSystem", "ExternalLibrary"};
			for (String module : modules) {
				RuleTypeDTO[] defaultRuleTypes = validate.getDefaultRuleTypesOfModule(module);
				System.out.print("\ndefaultRuleTypes for " + module + " : ");
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
		assertEquals(12, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
		assertEquals(12, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
		assertEquals(4, getViolationTypesStringArray(dtos, "VisibilityConvention").length);
	}

	@Test
	public void getViolationTypesCSharpLanguage() {
		CategoryDTO[] dtos = validate.getCategories();
		assertEquals(12, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
		assertEquals(12, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
		assertEquals(4, getViolationTypesStringArray(dtos, "VisibilityConvention").length);
	}

	@Test
	public void getViolationTypesNoLanguage() {
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		projects.add(new ProjectDTO("project", new ArrayList<String>(), "", "", "", new ArrayList<AnalysedModuleDTO>()));
		define.createApplication("TEST_APPLICATION", projects, "1.0");

		CategoryDTO[] dtos = validate.getCategories();
		assertEquals(0, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
		assertEquals(0, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
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

	private String[] getViolationTypesStringArray(CategoryDTO[] dtos, String ruleTypeKey) {
		ArrayList<String> violationtypeList = new ArrayList<String>();

		for (CategoryDTO cDTO : dtos) {
			for (RuleTypeDTO rDTO : cDTO.getRuleTypes()) {
				if (rDTO.getKey().equals(ruleTypeKey)) {
					return getViolationTypesStringArray(rDTO);
				} else {
					for (RuleTypeDTO exceptionDTO : rDTO.getExceptionRuleTypes()) {
						if (exceptionDTO.getKey().equals(ruleTypeKey)) {
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