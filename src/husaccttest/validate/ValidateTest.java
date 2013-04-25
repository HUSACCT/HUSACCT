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
import husacct.define.domain.AppliedRule;
import husacct.define.domain.SoftwareUnitDefinition;
import husacct.define.domain.SoftwareUnitDefinition.Type;
import husacct.define.domain.module.Layer;
import husacct.define.domain.module.Module;
import husacct.validate.IValidateService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;

import java.io.File;
import java.util.ArrayList;

import javax.swing.JInternalFrame;

import org.junit.Before;
import org.junit.Test;

public class ValidateTest {
	IDefineService define;
	IValidateService validate;
	DefineTestLibrary defineTestLibrary; 

	@Before
	public void setup() {
		ServiceProvider.getInstance().getControlService();
		define = ServiceProvider.getInstance().getDefineService();
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		for(int counter = 0; counter < 3; counter ++) {
			projects.add(new ProjectDTO("TEST_PROJECT_" + (counter)+1, new ArrayList<String>(), "C#", "1.0", 
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
		assertArrayEquals(new String[]{ "contentsofamodule", "legalityofdependency" }, getCategoryStringArray(dtos));
	}

	@Test
	public void getRuleTypes() {
		CategoryDTO[] dtos = validate.getCategories();
		final String[] currentRuletypes = new String[]{ "InterfaceConvention", "NamingConvention", "FacadeConvention", "SubClassConvention", "VisibilityConvention", "IsNotAllowedToUse", "IsOnlyAllowedToUse", "IsNotAllowedToMakeSkipCall", "IsOnlyModuleAllowedToUse", "MustUse", "IsNotAllowedToMakeBackCall" };
		assertArrayEquals(currentRuletypes, getRuleTypesStringArray(dtos));
	}
	
	@Test
	public void checkFacadeRuleConventionSupport() {
		//TODO Add rule in define (facade convention rule)
		
		define = ServiceProvider.getInstance().getDefineService();
		ArrayList<ProjectDTO> project = new ArrayList<ProjectDTO>();
		project.add(new ProjectDTO("TEST_PROJECT", new ArrayList<String>(), "", "1.0",
				"DESCRIPTION PROJECT", new ArrayList<AnalysedModuleDTO>()));
		define.createApplication("TEST_APPLICATION", project, "1.0");
		defineTestLibrary.setInstance();
		Layer moduleFrom = defineTestLibrary.addLayerModule(new Layer("Layer1"));
		defineTestLibrary.addSoftwareDefinition(new SoftwareUnitDefinition("infrastructure.socialmedia.twitter.TwitterFacade", Type.CLASS));
		defineTestLibrary.addAppliedRule(new AppliedRule("FacadeConvention", "", moduleFrom , new Module("", "")));
		
		//TODO Check violation output, check current support of this rule (not finished yet)
		validate.checkConformance();
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
		define = ServiceProvider.getInstance().getDefineService();
		ArrayList<ProjectDTO> projects = new ArrayList<ProjectDTO>();
		for(int counter = 0; counter < 3; counter ++) {
			projects.add(new ProjectDTO("TEST_PROJECT_" + (counter)+1, new ArrayList<String>(), "", "1.0", 
				"DESCRIPTION PROJECT " + counter, new ArrayList<AnalysedModuleDTO>()));
		}
		define.createApplication("TEST_APPLICATION", projects, "1.0");
		validate = ServiceProvider.getInstance().getValidateService();
		
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

	@Test
	public void isValidatedBeforeValidation() {
		assertFalse(validate.isValidated());
	}

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