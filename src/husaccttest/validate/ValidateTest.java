package husaccttest.validate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import husacct.ServiceProvider;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.define.IDefineService;
import husacct.validate.IValidateService;
import husacct.validate.domain.exception.ProgrammingLanguageNotFoundException;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ValidateTest {
	IDefineService define;
	IValidateService validate;
	
	@Before
	public void setup()
	{
		ServiceProvider.getInstance().getControlService().startApplication(new String[]{"nogui"});
		this.define = ServiceProvider.getInstance().getDefineService();
		this.validate = ServiceProvider.getInstance().getValidateService();
	}
	@Test
	public void getExportExtentions()
	{
		assertArrayEquals(new String[]{"pdf","html","xml"}, validate.getExportExtentions());
	}
	@Test
	public void exportViolations()
	{
		//cant test void
	}


	@Test
	public void getCategories()
	{
		CategoryDTO[] dtos = validate.getCategories();		
		assertArrayEquals(new String[]{"contentsofamodule", "legalityofdependency", "dependencylimitation"}, getCategoryStringArray(dtos));	
	}
	
	@Test
	public void getRuleTypes(){
		CategoryDTO[] dtos = validate.getCategories();	
		final String [] currentRuletypes = new String[]{"NamingConvention", "VisibilityConvention", "IsNotAllowedToUse", "IsOnlyAllowedToUse", "IsOnlyModuleAllowedToUse", "IsAllowedToUse", "MustUse", "SkipCall", "BackCall", "LoopsInModule"};
		assertArrayEquals(currentRuletypes, getRuleTypesStringArray(dtos));
	}
	
	@Test
	public void getViolationTypesJavaLanguage(){
		define.createApplication("", new String[]{}, "Java", "");
		CategoryDTO[] dtos = validate.getCategories();
		assertEquals(11, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
		assertEquals(11, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
		assertEquals(4, getViolationTypesStringArray(dtos, "VisibilityConvention").length);
	}
	
	@Test
	public void getViolationTypesCSharpLanguage(){
		define.createApplication("", new String[]{}, "C#", "");
		CategoryDTO[] dtos = validate.getCategories();
		assertEquals(10, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
		assertEquals(10, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
		assertEquals(4, getViolationTypesStringArray(dtos, "VisibilityConvention").length);
	}
	
	@Test
	public void getViolationTypesNoLanguage(){
		define.createApplication("", new String[]{}, "", "");
		CategoryDTO[] dtos = validate.getCategories();
		assertEquals(0, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
		assertEquals(0, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
	}

	private String[] getCategoryStringArray(CategoryDTO[] dtos){
		List<String> categoryList = new ArrayList<String>();
		for(CategoryDTO dto: dtos){
			categoryList.add(dto.getKey());
		}
		return categoryList.toArray(new String[]{});
	}

	private String[] getRuleTypesStringArray(CategoryDTO[] dtos){
		List<String> ruletypeList = new ArrayList<String>();
		for(CategoryDTO cDTO : dtos){
			for(RuleTypeDTO rDTO : cDTO.getRuleTypes()){
				ruletypeList.add(rDTO.getKey());
			}
		}
		return ruletypeList.toArray(new String[]{});
	}

	private String[] getViolationTypesStringArray(CategoryDTO[] dtos, String ruleTypeKey){
		List<String> violationtypeList = new ArrayList<String>();
		for(CategoryDTO cDTO : dtos){
			for(RuleTypeDTO rDTO : cDTO.getRuleTypes()){
				if(rDTO.getKey().equals(ruleTypeKey)){
					for(ViolationTypeDTO vDTO : rDTO.getViolationTypes()){
						violationtypeList.add(vDTO.getKey());
					}
				}
			}
		}
		return violationtypeList.toArray(new String[]{});
	}

	@Test
	public void isValidatedBeforeValidation(){
		assertFalse(validate.isValidated());
	}

	@Test
	public void getViolationsByLogicalPath()
	{
		validate.checkConformance();
		assertTrue(listContainsFromValue(validate.getViolationsByLogicalPath("DomainLayer", "Infrastructure"),"domain.locationbased.foursquare.Account"));
		assertTrue(listContainsToValue(validate.getViolationsByLogicalPath("DomainLayer", "Infrastructure"),"infrastructure.socialmedia.locationbased.foursquare.AccountDAO"));
		assertTrue(listContainsKey(validate.getViolationsByLogicalPath("DomainLayer", "Infrastructure"),"InvocConstructor"));
	}

	@Test
	public void getViolationsByPhysicalPath() {
		validate.checkConformance();
		assertTrue(listContainsFromValue(validate.getViolationsByPhysicalPath("domain.locationbased.foursquare", "infrastructure.socialmedia.locationbased.foursquare"),"domain.locationbased.foursquare.Account"));
		assertTrue(listContainsToValue(validate.getViolationsByLogicalPath("DomainLayer", "Infrastructure"), "infrastructure.socialmedia.locationbased.foursquare.AccountDAO"));
		assertTrue(listContainsKey(validate.getViolationsByLogicalPath("DomainLayer", "Infrastructure"),"InvocConstructor"));

	}
	private boolean listContainsFromValue(ViolationDTO[] violationDTOs, String value){
		for(ViolationDTO v: violationDTOs){
			if(v.fromClasspath.equals(value))
				return true;
		}
		return false;
	}
	private boolean listContainsToValue(ViolationDTO[] violationDTOs, String value){
		for(ViolationDTO v: violationDTOs){
			if(v.toClasspath.equals(value))
				return true;
		}
		return false;
	}
	private boolean listContainsKey(ViolationDTO[] violationDTOs, String value){
		for(ViolationDTO v: violationDTOs){
			if(v.violationType.getKey().equals(value))
				return true;
		}
		return false;
	}

	@Test
	public void isValidatedAfterValidation(){
		boolean exceptionOccured = false;		
		try{
			validate.checkConformance();	
		}
		catch(ProgrammingLanguageNotFoundException e){
			exceptionOccured = true;
			assertFalse(validate.isValidated());
		}

		if(!exceptionOccured){
			assertTrue(validate.isValidated());
		}
	}

	@Ignore
	@Test
	public void checkConformancePerformance(){
		for(int i = 0; i < 10001; i++){
			validate.checkConformance();
		}
	}

	//This method was created to test specific rules with a hardcoded DefineStub.
	@Ignore
	@Test
	public void testExceptionRule(){
		//DefineServiceStubTest defineTest = new DefineServiceStubTest();
		//validate.Validate(defineTest.getDefinedRulesWithException());
	}
	/*This method was created to test specific rules with a hardcoded DefineStub.
	 * Need to change the CheckConformanceUtil getChildsFromModule to getSkipCallChildsFromModule or getBackCallChildsFromModule
	 * depending on which scenario you wish to test.
	 * getDefinedRules is scenario 1, which requires getSkipCallChildsFromModule.
	 * getDefinedRulesSenarioTwo is scenario 2, which requires getBackCallChildsFromModule.
	 */	
	@Ignore 
	@Test
	public void specificRuleTest(){
		//DefineServiceStubTest defineTest = new DefineServiceStubTest();
		//validate.Validate(defineTest.getDefinedRulesScenarioOne());
		//validate.Validate(defineTest.getDefinedRulesScenarioTwo());
	}
}