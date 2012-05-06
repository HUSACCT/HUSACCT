package husaccttest.validate;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import husacct.common.dto.CategoryDTO;
import husacct.common.dto.RuleTypeDTO;
import husacct.common.dto.ViolationDTO;
import husacct.common.dto.ViolationTypeDTO;
import husacct.define.DefineServiceImpl;
import husacct.validate.ValidateServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ValidateTest {

	private ValidateServiceImpl validate;

	@Before
	public void setup()
	{
		validate = new ValidateServiceImpl();
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
		assertArrayEquals(new String[]{"contentsofamodule", "legalityofdependency"}, getCategoryStringArray(dtos));	

		final String [] currentRuletypes = new String[]{"InterfaceConvention", "SubClassConvention", "IsNotAllowedToUse", "IsOnlyAllowedToUse", "IsOnlyModuleAllowedToUse", "IsAllowedToUse", "MustUse", "SkipCall", "BackCall"};
		assertArrayEquals(currentRuletypes, getRuleTypesStringArray(dtos));

		DefineServiceImpl defineService = new DefineServiceImpl();	
		if(defineService.getApplicationDetails().programmingLanguage != null){
			if(defineService.getApplicationDetails().programmingLanguage.isEmpty()){
				assertEquals(10, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
				assertEquals(10, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
			}			
			else if(defineService.getApplicationDetails().programmingLanguage.equals("Java")){
				assertEquals(9, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
				assertEquals(9, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
			}			
		}
		else{
			assertEquals(0, getViolationTypesStringArray(dtos, "IsNotAllowedToUse").length);
			assertEquals(0, getViolationTypesStringArray(dtos, "IsAllowedToUse").length);
		}
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
			if(v.getFromClasspath().equals(value))
				return true;
		}
		return false;
	}
	private boolean listContainsToValue(ViolationDTO[] violationDTOs, String value){
		for(ViolationDTO v: violationDTOs){
			if(v.getToClasspath().equals(value))
				return true;
		}
		return false;
	}
	private boolean listContainsKey(ViolationDTO[] violationDTOs, String value){
		for(ViolationDTO v: violationDTOs){
			if(v.getViolationType().getKey().equals(value))
				return true;
		}
		return false;
	}

	@Test
	public void isValidatedAfterValidation(){
		validate.checkConformance();
		assertTrue(validate.isValidated());
	}

	@Ignore
	@Test
	public void checkConformancePerformance(){
		for(int i = 0; i < 10001; i++){
			validate.checkConformance();
		}
	}
	
//	//This method was created to test specific rules with a hardcoded DefineStub.
//	@Ignore
//	@Test
//	public void testExceptionRule(){
//		DefineServiceStubTest defineTest = new DefineServiceStubTest();
//		validate.Validate(defineTest.getDefinedRulesWithException());
//	}
//	/*This method was created to test specific rules with a hardcoded DefineStub.
//	 * Need to change the CheckConformanceUtil getChildsFromModule to getSkipCallChildsFromModule or getBackCallChildsFromModule
//	 * depending on which scenario you wish to test.
//	 * getDefinedRules is scenario 1, which requires getSkipCallChildsFromModule.
//	 * getDefinedRulesSenarioTwo is scenario 2, which requires getBackCallChildsFromModule.
//	 */	
//	@Ignore 
//	@Test
//	public void specificRuleTest(){
//		DefineServiceStubTest defineTest = new DefineServiceStubTest();
//		validate.Validate(defineTest.getDefinedRulesScenarioOne());
//		validate.Validate(defineTest.getDefinedRulesScenarioTwo());
//	}
}